/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.datum;

import java.util.Collections;
import java.util.Map;

import javax.measure.quantity.Angle;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.util.Utilities;
import org.opengis.referencing.datum.PrimeMeridian;


/**
 * A prime meridian defines the origin from which longitude values are determined.
 * The {@link #getName name} initial value is "Greenwich", and that value shall be
 * used when the {@linkplain #getGreenwichLongitude greenwich longitude} value is
 * zero.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/referencing/src/main/java/org/geotools/referencing/datum/DefaultPrimeMeridian.java $
 * @version $Id: DefaultPrimeMeridian.java 37299 2011-05-25 05:21:24Z mbedward $
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.1
 */
public class DefaultPrimeMeridian extends AbstractIdentifiedObject implements PrimeMeridian {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 541978454643213305L;;

    /**
     * The Greenwich meridian, with angular measurements in decimal degrees.
     */
    public static final DefaultPrimeMeridian GREENWICH =
                    new DefaultPrimeMeridian("Greenwich", 0, NonSI.DEGREE_ANGLE);

    /**
     * Longitude of the prime meridian measured from the Greenwich meridian, positive eastward.
     */
    private final double greenwichLongitude;

    /**
     * The angular unit of the {@linkplain #getGreenwichLongitude Greenwich longitude}.
     */
    private final Unit<Angle> angularUnit;

    /**
     * Constructs a prime meridian from a name.
     *
     * @param name                The datum name.
     * @param greenwichLongitude  The longitude value relative to the Greenwich Meridian.
     * @param angularUnit         The angular unit of the longitude.
     */
    public DefaultPrimeMeridian(final String name, final double greenwichLongitude,
                                final Unit<Angle> angularUnit)
    {
        this(Collections.singletonMap(NAME_KEY, name), greenwichLongitude, angularUnit);
    }

    /**
     * Constructs a prime meridian from a set of properties. The properties map is given
     * unchanged to the {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map)
     * super-class constructor}.
     *
     * @param properties          Set of properties. Should contains at least {@code "name"}.
     * @param greenwichLongitude  The longitude value relative to the Greenwich Meridian.
     * @param angularUnit         The angular unit of the longitude.
     */
    public DefaultPrimeMeridian(final Map<String,?> properties, final double greenwichLongitude,
                                final Unit<Angle> angularUnit)
    {
        super(properties);
        this.greenwichLongitude = greenwichLongitude;
        this.angularUnit        = angularUnit;
        ensureAngularUnit(angularUnit);
    }

    /**
     * Longitude of the prime meridian measured from the Greenwich meridian, positive eastward.
     * The {@code greenwichLongitude} initial value is zero, and that value shall be used
     * when the {@linkplain #getName meridian name} value is "Greenwich".
     *
     * @return The prime meridian Greenwich longitude, in {@linkplain #getAngularUnit angular unit}.
     */
    public double getGreenwichLongitude() {
        return greenwichLongitude;
    }

    /**
     * Returns the longitude value relative to the Greenwich Meridian, expressed in the specified
     * units. This convenience method makes it easier to obtain longitude in decimal degrees
     * ({@code getGreenwichLongitude(NonSI.DEGREE_ANGLE)}), regardless of the underlying
     * angular units of this prime meridian.
     *
     * @param targetUnit The unit in which to express longitude.
     * @return The Greenwich longitude in the given units.
     */
    public double getGreenwichLongitude(final Unit<Angle> targetUnit) {
        return getAngularUnit().getConverterTo(targetUnit).convert(getGreenwichLongitude());
    }

    /**
     * Returns the angular unit of the {@linkplain #getGreenwichLongitude Greenwich longitude}.
     */
    public Unit<Angle> getAngularUnit() {
        return angularUnit;
    }

    /**
     * Compare this prime meridian with the specified object for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object, compareMetadata)) {
            final DefaultPrimeMeridian that = (DefaultPrimeMeridian) object;
            if (compareMetadata) {
                return Utilities.equals(this.greenwichLongitude, that.greenwichLongitude) &&
                       Utilities.equals(this.angularUnit, that.angularUnit);
            } else {
                return Utilities.equals(this.getGreenwichLongitude(NonSI.DEGREE_ANGLE), 
                        that.getGreenwichLongitude(NonSI.DEGREE_ANGLE));
                
                /*
                 * Note: if compareMetadata==false, we relax the unit check because EPSG uses
                 *       sexagesimal degrees for the Greenwich meridian. Requirying the same
                 *       unit prevent Geodetic.isWGS84(...) method to recognize EPSG's WGS84.
                 */
            }
        }
        return false;
    }

    /**
     * Returns a hash value for this prime meridian. {@linkplain #getName Name},
     * {@linkplain #getRemarks remarks} and the like are not taken in account.
     * In other words, two prime meridians will return the same hash value if
     * they are equal in the sense of
     * <code>{@link #equals equals}(AbstractIdentifiedObject, <strong>false</strong>)</code>.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(greenwichLongitude);
        return ((int)(code >>> 32) ^ (int)code) ^ (int)serialVersionUID;
    }
}
