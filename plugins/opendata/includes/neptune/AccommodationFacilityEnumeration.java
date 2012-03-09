//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.5 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2012.03.08 à 06:24:59 PM CET 
//


package neptune;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour AccommodationFacilityEnumeration.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="AccommodationFacilityEnumeration">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="unknown"/>
 *     &lt;enumeration value="pti23_3"/>
 *     &lt;enumeration value="sleeper"/>
 *     &lt;enumeration value="pti23_4"/>
 *     &lt;enumeration value="couchette"/>
 *     &lt;enumeration value="pti23_5"/>
 *     &lt;enumeration value="specialSeating"/>
 *     &lt;enumeration value="pti23_11"/>
 *     &lt;enumeration value="freeSeating"/>
 *     &lt;enumeration value="pti23_12"/>
 *     &lt;enumeration value="recliningSeats"/>
 *     &lt;enumeration value="pti23_13"/>
 *     &lt;enumeration value="babyCompartment"/>
 *     &lt;enumeration value="familyCarriage"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AccommodationFacilityEnumeration", namespace = "http://www.siri.org.uk/siri")
@XmlEnum
public enum AccommodationFacilityEnumeration {

    @XmlEnumValue("unknown")
    UNKNOWN("unknown"),
    @XmlEnumValue("pti23_3")
    PTI_23_3("pti23_3"),
    @XmlEnumValue("sleeper")
    SLEEPER("sleeper"),
    @XmlEnumValue("pti23_4")
    PTI_23_4("pti23_4"),
    @XmlEnumValue("couchette")
    COUCHETTE("couchette"),
    @XmlEnumValue("pti23_5")
    PTI_23_5("pti23_5"),
    @XmlEnumValue("specialSeating")
    SPECIAL_SEATING("specialSeating"),
    @XmlEnumValue("pti23_11")
    PTI_23_11("pti23_11"),
    @XmlEnumValue("freeSeating")
    FREE_SEATING("freeSeating"),
    @XmlEnumValue("pti23_12")
    PTI_23_12("pti23_12"),
    @XmlEnumValue("recliningSeats")
    RECLINING_SEATS("recliningSeats"),
    @XmlEnumValue("pti23_13")
    PTI_23_13("pti23_13"),
    @XmlEnumValue("babyCompartment")
    BABY_COMPARTMENT("babyCompartment"),
    @XmlEnumValue("familyCarriage")
    FAMILY_CARRIAGE("familyCarriage");
    private final String value;

    AccommodationFacilityEnumeration(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AccommodationFacilityEnumeration fromValue(String v) {
        for (AccommodationFacilityEnumeration c: AccommodationFacilityEnumeration.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
