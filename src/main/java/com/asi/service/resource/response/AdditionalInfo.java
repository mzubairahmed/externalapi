package com.asi.service.resource.response;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class AdditionalInfo {
    @JsonProperty("Value")
    private String value;
}
