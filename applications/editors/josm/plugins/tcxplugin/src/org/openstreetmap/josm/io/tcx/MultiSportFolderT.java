//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.08.10 at 10:24:05 AM CEST 
//


package org.openstreetmap.josm.io.tcx;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MultiSportFolder_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MultiSportFolder_t">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Folder" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}MultiSportFolder_t" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MultisportActivityRef" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}ActivityReference_t" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Week" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}Week_t" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Extensions" type="{http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2}Extensions_t" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiSportFolder_t", propOrder = {
    "folder",
    "multisportActivityRef",
    "week",
    "notes",
    "extensions"
})
public class MultiSportFolderT {

    @XmlElement(name = "Folder")
    protected List<MultiSportFolderT> folder;
    @XmlElement(name = "MultisportActivityRef")
    protected List<ActivityReferenceT> multisportActivityRef;
    @XmlElement(name = "Week")
    protected List<WeekT> week;
    @XmlElement(name = "Notes")
    protected String notes;
    @XmlElement(name = "Extensions")
    protected ExtensionsT extensions;
    @XmlAttribute(name = "Name", required = true)
    protected String name;

    /**
     * Gets the value of the folder property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the folder property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFolder().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MultiSportFolderT }
     * 
     * 
     */
    public List<MultiSportFolderT> getFolder() {
        if (folder == null) {
            folder = new ArrayList<MultiSportFolderT>();
        }
        return this.folder;
    }

    /**
     * Gets the value of the multisportActivityRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the multisportActivityRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMultisportActivityRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ActivityReferenceT }
     * 
     * 
     */
    public List<ActivityReferenceT> getMultisportActivityRef() {
        if (multisportActivityRef == null) {
            multisportActivityRef = new ArrayList<ActivityReferenceT>();
        }
        return this.multisportActivityRef;
    }

    /**
     * Gets the value of the week property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the week property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWeek().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WeekT }
     * 
     * 
     */
    public List<WeekT> getWeek() {
        if (week == null) {
            week = new ArrayList<WeekT>();
        }
        return this.week;
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

}
