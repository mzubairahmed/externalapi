package com.asi.ext.api.service.model;

import javax.xml.bind.annotation.XmlSeeAlso;
//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME,include=JsonTypeInfo.As.PROPERTY)
//@JsonSubTypes({@JsonSubTypes.Type(value=StringValue.class),@JsonSubTypes.Type(value=Value.class),@JsonSubTypes.Type(value=ListValue.class)})
@XmlSeeAlso({StringValue.class, Value.class, ListValue.class})
public class BaseValue {
 
}
