//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.12 at 08:39:46 PM PDT 
//


package com.tmobile.federation.beans.req.userProfile;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dial-permission-type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="dial-permission-type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="disallow"/>
 *     &lt;enumeration value="allow-domestic"/>
 *     &lt;enumeration value="allow-all"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "dial-permission-type", namespace = "urn:oma:xml:xdm:user-profile")
@XmlEnum
public enum DialPermissionType {

    @XmlEnumValue("disallow")
    DISALLOW("disallow"),
    @XmlEnumValue("allow-domestic")
    ALLOW_DOMESTIC("allow-domestic"),
    @XmlEnumValue("allow-all")
    ALLOW_ALL("allow-all");
    private final String value;

    DialPermissionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DialPermissionType fromValue(String v) {
        for (DialPermissionType c: DialPermissionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
