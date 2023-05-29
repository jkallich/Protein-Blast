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
    "parametersMatrix",
    "parametersExpect",
    "parametersInclude",
    "parametersScMatch",
    "parametersScMismatch",
    "parametersGapOpen",
    "parametersGapExtend",
    "parametersFilter",
    "parametersPattern",
    "parametersEntrezQuery"
})
@XmlRootElement(name = "Parameters")
public class Parameters {

    @XmlElement(name = "Parameters_matrix")
    protected String parametersMatrix;
    @XmlElement(name = "Parameters_expect", required = true)
    protected String parametersExpect;
    @XmlElement(name = "Parameters_include")
    protected String parametersInclude;
    @XmlElement(name = "Parameters_sc-match")
    protected String parametersScMatch;
    @XmlElement(name = "Parameters_sc-mismatch")
    protected String parametersScMismatch;
    @XmlElement(name = "Parameters_gap-open", required = true)
    protected String parametersGapOpen;
    @XmlElement(name = "Parameters_gap-extend", required = true)
    protected String parametersGapExtend;
    @XmlElement(name = "Parameters_filter")
    protected String parametersFilter;
    @XmlElement(name = "Parameters_pattern")
    protected String parametersPattern;
    @XmlElement(name = "Parameters_entrez-query")
    protected String parametersEntrezQuery;

    /**
     * Gets the value of the parametersMatrix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersMatrix() {
        return parametersMatrix;
    }

    /**
     * Sets the value of the parametersMatrix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersMatrix(String value) {
        this.parametersMatrix = value;
    }

    /**
     * Gets the value of the parametersExpect property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersExpect() {
        return parametersExpect;
    }

    /**
     * Sets the value of the parametersExpect property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersExpect(String value) {
        this.parametersExpect = value;
    }

    /**
     * Gets the value of the parametersInclude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersInclude() {
        return parametersInclude;
    }

    /**
     * Sets the value of the parametersInclude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersInclude(String value) {
        this.parametersInclude = value;
    }

    /**
     * Gets the value of the parametersScMatch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersScMatch() {
        return parametersScMatch;
    }

    /**
     * Sets the value of the parametersScMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersScMatch(String value) {
        this.parametersScMatch = value;
    }

    /**
     * Gets the value of the parametersScMismatch property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersScMismatch() {
        return parametersScMismatch;
    }

    /**
     * Sets the value of the parametersScMismatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersScMismatch(String value) {
        this.parametersScMismatch = value;
    }

    /**
     * Gets the value of the parametersGapOpen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersGapOpen() {
        return parametersGapOpen;
    }

    /**
     * Sets the value of the parametersGapOpen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersGapOpen(String value) {
        this.parametersGapOpen = value;
    }

    /**
     * Gets the value of the parametersGapExtend property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersGapExtend() {
        return parametersGapExtend;
    }

    /**
     * Sets the value of the parametersGapExtend property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersGapExtend(String value) {
        this.parametersGapExtend = value;
    }

    /**
     * Gets the value of the parametersFilter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersFilter() {
        return parametersFilter;
    }

    /**
     * Sets the value of the parametersFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersFilter(String value) {
        this.parametersFilter = value;
    }

    /**
     * Gets the value of the parametersPattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersPattern() {
        return parametersPattern;
    }

    /**
     * Sets the value of the parametersPattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersPattern(String value) {
        this.parametersPattern = value;
    }

    /**
     * Gets the value of the parametersEntrezQuery property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParametersEntrezQuery() {
        return parametersEntrezQuery;
    }

    /**
     * Sets the value of the parametersEntrezQuery property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParametersEntrezQuery(String value) {
        this.parametersEntrezQuery = value;
    }

}
