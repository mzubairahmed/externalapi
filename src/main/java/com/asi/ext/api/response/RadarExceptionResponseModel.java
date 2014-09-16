/**
 * 
 */
package com.asi.ext.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Rahul K
 * 
 */
public class RadarExceptionResponseModel {

    @JsonProperty("type")
    private String type;
    @JsonProperty("msg")
    private String msg;

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg
     *            the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
