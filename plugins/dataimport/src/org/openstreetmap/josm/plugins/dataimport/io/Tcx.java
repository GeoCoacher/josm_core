// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.dataimport.io;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.ExtensionFileFilter;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.gpx.GpxData;
import org.openstreetmap.josm.data.gpx.ImmutableGpxTrack;
import org.openstreetmap.josm.data.gpx.WayPoint;
import org.openstreetmap.josm.gui.layer.GpxLayer;
import org.openstreetmap.josm.gui.layer.markerlayer.MarkerLayer;
import org.openstreetmap.josm.gui.progress.ProgressMonitor;
import org.openstreetmap.josm.io.FileImporter;
import org.openstreetmap.josm.plugins.dataimport.io.tcx.ActivityLapT;
import org.openstreetmap.josm.plugins.dataimport.io.tcx.ActivityT;
import org.openstreetmap.josm.plugins.dataimport.io.tcx.CourseT;
import org.openstreetmap.josm.plugins.dataimport.io.tcx.PositionT;
import org.openstreetmap.josm.plugins.dataimport.io.tcx.TrackT;
import org.openstreetmap.josm.plugins.dataimport.io.tcx.TrackpointT;
import org.openstreetmap.josm.plugins.dataimport.io.tcx.TrainingCenterDatabaseT;


/**
 * TCX Reader. This class is based on code genarated by the Java Architecture
 * for XML Binding (JAXB). For this class to work you will need the API und IMPL
 * Jars from the RI. JAXB can be downloaded at <a
 * href="https://jaxb.dev.java.net/">https://jaxb.dev.java.net/</a>. This class
 * has been developed using JAXB version 2.1.7.
 * <p>
 * Additional information and tutorial are available at: <a
 * href="http://java.sun.com/developer/technicalArticles/WebServices/jaxb/">http://java.sun.com/developer/technicalArticles/WebServices/jaxb/</a>
 * <p>
 * The Garmin TCX Schema file can be downloaded from: <a
 * href="http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd">http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd</a>
 * The command used to generate the code is: <code>
 * xjc.bat -p org.openstreetmap.josm.io.tcx TrainingCenterDatabasev2.xsd -d <path to the src folder of JOSM>
 * </code>
 * <p>
 * Note: if you get an exception that JAXB 2.1 is not supported on your system, you will have to add the jaxb-api.jar
 * to the endorsed directory (create it if necessary) of your JRE. Usually it is something like this:
 * \<program files>\Java\jre<java version>\lib\endorsed
 *
 * @author adrian <as@nitegate.de>
 *
 */
public class Tcx extends FileImporter {

    //private File tcxFile;

    private GpxData gpxData;


    public Tcx() {
        super(new ExtensionFileFilter("tcx", "tcx",tr("TCX Files (*.tcx)")));
    }

    /**
     * @param tcxFile
     */
    @Override
    public void importData(File tcxFile, ProgressMonitor progressMonitor) throws IOException {
        //this.tcxFile = tcxFile;
        parseFile(tcxFile);

        GpxData gpxData = getGpxData();
        gpxData.storageFile = tcxFile;
        GpxLayer gpxLayer = new GpxLayer(gpxData, tcxFile.getName());
        Main.main.addLayer(gpxLayer);
        if (Main.pref.getBoolean("marker.makeautomarkers", true))
        {
            MarkerLayer ml = new MarkerLayer(gpxData, tr("Markers from {0}", tcxFile.getName()), tcxFile, gpxLayer);
            if (ml.data.size() > 0)
            {
                Main.main.addLayer(ml);
            }
        }

    }

    /**
     *
     */
    @SuppressWarnings("unchecked") private void parseFile(File tcxFile) {
        try {
            JAXBContext jc = JAXBContext
                    .newInstance(TrainingCenterDatabaseT.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement<TrainingCenterDatabaseT> element = (JAXBElement<TrainingCenterDatabaseT>)unmarshaller
                    .unmarshal(tcxFile);

            TrainingCenterDatabaseT tcd = element.getValue();

            gpxData = new GpxData();

            // Usually logged activities are in the activities tag.
            parseDataFromActivities(tcd);
            // GPS tracks in the course tag are generated by the user.
            // Maybe not a good idea to import them.
            parseDataFromCourses(tcd);

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    /** Convert a TrackpointT to a WayPoint.
     * @param tp    TrackpointT to convert
     * @return      tp converted to WayPoint, or null
     */
    private static WayPoint convertPoint(TrackpointT tp) {

        PositionT p = tp.getPosition();

        if (p == null)
            // If the TrackPointT lacks a position, return null.
            return null;

        WayPoint waypt = new WayPoint(new LatLon(p.getLatitudeDegrees(),
                                                 p.getLongitudeDegrees()));
        Double altitudeMeters = tp.getAltitudeMeters();
        if (altitudeMeters != null) {
            waypt.attr.put("ele", altitudeMeters.toString());
        }

        XMLGregorianCalendar time = tp.getTime();

        if (time != null) {
            waypt.attr.put("time", time.toString());
            waypt.time = .001 * time.toGregorianCalendar().getTimeInMillis();
        }

        return waypt;
    }

    /**
     * @param tcd
     */
    private void parseDataFromActivities(TrainingCenterDatabaseT tcd) {
        int lap = 0;
        if ((tcd.getActivities() != null)
                && (tcd.getActivities().getActivity() != null)) {
            for (ActivityT activity : tcd.getActivities().getActivity()) {
                if (activity.getLap() != null) {
                    for (ActivityLapT activityLap : activity.getLap()) {
                        if (activityLap.getTrack() != null) {
                            XMLGregorianCalendar startTime = activityLap
                                    .getStartTime();
                            Collection<Collection<WayPoint>> currentTrack = new ArrayList<Collection<WayPoint>>();
                            for (TrackT track : activityLap.getTrack()) {
                                if (track.getTrackpoint() != null) {
                                    Collection<WayPoint> currentTrackSeg = new ArrayList<WayPoint>();
                                    currentTrack.add(currentTrackSeg);
                                    for (TrackpointT tp :
                                           track.getTrackpoint()) {
                                        WayPoint waypt = convertPoint(tp);

                                        if (waypt != null) {
                                            if (startTime != null) {
                                                waypt.attr.put("name", "LAP"
                                                               + (++lap));
                                                gpxData.waypoints.add(waypt);
                                                startTime = null;
                                            }

                                            currentTrackSeg.add(waypt);
                                        }
                                    }
                                }
                            }
                            gpxData.tracks.add(new ImmutableGpxTrack(currentTrack, Collections.<String, Object>emptyMap()));
                        }
                    }
                }
            }
        }
    }

    /**
     * @param tcd
     */
    private void parseDataFromCourses(TrainingCenterDatabaseT tcd) {
        if ((tcd.getCourses() != null)
                && (tcd.getCourses().getCourse() != null)) {
            for (CourseT course : tcd.getCourses().getCourse()) {
                if (course.getTrack() != null) {
                    Collection<Collection<WayPoint>> currentTrack = new ArrayList<Collection<WayPoint>>();
                    for (TrackT track : course.getTrack()) {
                        if (track.getTrackpoint() != null) {
                            Collection<WayPoint> currentTrackSeg = new ArrayList<WayPoint>();
                            currentTrack.add(currentTrackSeg);
                            for (TrackpointT tp : track.getTrackpoint()) {
                                WayPoint waypt = convertPoint(tp);

                                if (waypt != null) {
                                    currentTrackSeg.add(waypt);
                                }
                            }
                        }
                    }
                    gpxData.tracks.add(new ImmutableGpxTrack(currentTrack, Collections.<String, Object>emptyMap()));
                }
            }
        }
    }

    private GpxData getGpxData() {
        return gpxData;
    }
}
