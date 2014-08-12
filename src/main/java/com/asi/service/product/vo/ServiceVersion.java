package com.asi.service.product.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://www.asicentral.com/schema/product" , name="serviceHealth")
public class ServiceVersion {

	private String version="v.0.0.1";
	private String bootClassPath;
	private String classPath;
	private List<String> cmdArgs;
	private Map<?,?> systemProperties;
	private long upTime;
	private Date startTime;
	

	public String getBootClassPath() {
		return bootClassPath;
	}

	public void setBootClassPath(String bootClassPath) {
		this.bootClassPath = bootClassPath;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}


	public List<String> getCmdArgs() {
		return cmdArgs;
	}

	public void setCmdArgs(List<String> cmdArgs) {
		this.cmdArgs = cmdArgs;
	}

	public Map<?, ?> getSystemProperties() {
		return systemProperties;
	}

	public void setSystemProperties(Map<?, ?> systemProperties) {
		this.systemProperties = systemProperties;
	}

	public long getUpTime() {
		return upTime;
	}

	public void setUpTime(long upTime) {
		this.upTime = upTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
