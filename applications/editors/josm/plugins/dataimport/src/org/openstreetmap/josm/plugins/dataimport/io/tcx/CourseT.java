//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2008.08.10 at 10:24:05 AM CEST
//


package org.openstreetmap.josm.plugins.dataimport.io.tcx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for Course_t complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Course_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}RestrictedToken_t"/>
 *         &lt;element name="Lap" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}CourseLap_t" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Track" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}Track_t" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CoursePoint" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}CoursePoint_t" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Creator" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}AbstractSource_t" minOccurs="0"/>
 *         &lt;element name="Extensions" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}Extensions_t" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Course_t", propOrder = {
    "name",
    "lap",
    "track",
    "notes",
    "coursePoint",
    "creator",
    "extensions"
})
public class CourseT {

    @XmlElement(name = "Name", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlElement(name = "Lap")
    protected List<CourseLapT> lap;
    @XmlElement(name = "Track")
    protected List<TrackT> track;
    @XmlElement(name = "Notes")
    protected String notes;
    @XmlElement(name = "CoursePoint")
    protected List<CoursePointT> coursePoint;
    @XmlElement(name = "Creator")
    protected AbstractSourceT creator;
    @XmlElement(name = "Extensions")
    protected ExtensionsT extensions;

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the lap property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lap property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLap().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourseLapT }
     *
     *
     */
    public List<CourseLapT> getLap() {
        if (lap == null) {
            lap = new ArrayList<CourseLapT>();
        }
        return this.lap;
    }

    /**
     * Gets the value of the track property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the track property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrack().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TrackT }
     *
     *
     */
    public List<TrackT> getTrack() {
        if (track == null) {
            track = new ArrayList<TrackT>();
        }
        return this.track;
    }

    /**
     * Gets the value of the notes property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    /**
     * Gets the value of the coursePoint property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the coursePoint property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoursePoint().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CoursePointT }
     *
     *
     */
    public List<CoursePointT> getCoursePoint() {
        if (coursePoint == null) {
            coursePoint = new ArrayList<CoursePointT>();
        }
        return this.coursePoint;
    }

    /**
     * Gets the value of the creator property.
     *
     * @return
     *     possible object is
     *     {@link AbstractSourceT }
     *
     */
    public AbstractSourceT getCreator() {
        return creator;
    }

    /**
     * Sets the value of the creator property.
     *
     * @param value
     *     allowed object is
     *     {@link AbstractSourceT }
     *
     */
    public void setCreator(AbstractSourceT value) {
        this.creator = value;
    }

    /**
     * Gets the value of the extensions property.
     *
     * @return
     *     possible object is
     *     {@link ExtensionsT }
     *
     */
    public ExtensionsT getExtensions() {
        return extensions;
    }

    /**
     * Sets the value of the extensions property.
     *
     * @param value
     *     allowed object is
     *     {@link ExtensionsT }
     *
     */
    public void setExtensions(ExtensionsT value) {
        this.extensions = value;
    }

}
