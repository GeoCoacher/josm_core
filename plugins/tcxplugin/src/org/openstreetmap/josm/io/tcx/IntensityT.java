//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.08.10 at 10:24:05 AM CEST 
//


package org.openstreetmap.josm.io.tcx;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Intensity_t.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Intensity_t">
 *   &lt;restriction base="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}Token_t">
 *     &lt;enumeration value="Active"/>
 *     &lt;enumeration value="Resting"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Intensity_t")
@XmlEnum
public enum IntensityT {

    @XmlEnumValue("Active")
    ACTIVE("Active"),
    @XmlEnumValue("Resting")
    RESTING("Resting");
    private final String value;

    IntensityT(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IntensityT fromValue(String v) {
        for (IntensityT c: IntensityT.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
