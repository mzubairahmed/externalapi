package com.asi.ext.api.service.model;

import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@class")
@JsonSubTypes({@Type(value=StringValue.class, name = "StringValue"), @Type(value=Value.class, name="Value"), @Type(value=ListValue.class, name="ListValue")})
@XmlSeeAlso({StringValue.class, Value.class, ListValue.class})

public class BaseValue {
 
}
