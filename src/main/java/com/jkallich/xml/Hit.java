//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.05.21 at 08:14:37 AM MDT 
//


package com.jkallich.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "hitNum",
    "hitId",
    "hitDef",
    "hitAccession",
    "hitLen",
    "hitHsps"
})
@XmlRootElement(name = "Hit")
public class Hit {

    @XmlElement(name = "Hit_num", required = true)
    protected String hitNum;
    @XmlElement(name = "Hit_id", required = true)
    protected String hitId;
    @XmlElement(name = "Hit_def", required = true)
    protected String hitDef;
    @XmlElement(name = "Hit_accession", required = true)
    protected String hitAccession;
    @XmlElement(name = "Hit_len", required = true)
    protected String hitLen;
    @XmlElement(name = "Hit_hsps")
    protected HitHsps hitHsps;

    /**
     * Gets the value of the hitNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHitNum() {
        return hitNum;
    }

    /**
     * Sets the value of the hitNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHitNum(String value) {
        this.hitNum = value;
    }

    /**
     * Gets the value of the hitId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHitId() {
        return hitId;
    }

    /**
     * Sets the value of the hitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHitId(String value) {
        this.hitId = value;
    }

    /**
     * Gets the value of the hitDef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHitDef() {
        return hitDef;
    }

    /**
     * Sets the value of the hitDef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHitDef(String value) {
        this.hitDef = value;
    }

    /**
     * Gets the value of the hitAccession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHitAccession() {
        return hitAccession;
    }

    /**
     * Sets the value of the hitAccession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHitAccession(String value) {
        this.hitAccession = value;
    }

    /**
     * Gets the value of the hitLen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHitLen() {
        return hitLen;
    }

    /**
     * Sets the value of the hitLen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHitLen(String value) {
        this.hitLen = value;
    }

    /**
     * Gets the value of the hitHsps property.
     * 
     * @return
     *     possible object is
     *     {@link HitHsps }
     *     
     */
    public HitHsps getHitHsps() {
        return hitHsps;
    }

    /**
     * Sets the value of the hitHsps property.
     * 
     * @param value
     *     allowed object is
     *     {@link HitHsps }
     *     
     */
    public void setHitHsps(HitHsps value) {
        this.hitHsps = value;
    }

}
