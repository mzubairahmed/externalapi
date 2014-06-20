package com.asi.service.product.list;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asi.service.product.vo.ServiceVersion;
@RestController
@RequestMapping("healthCheck")
public class ServiceHeathCheck {

	@RequestMapping(value = "status", method = RequestMethod.GET,headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ServiceVersion> serviceInfo() throws UnsupportedEncodingException {
		ServiceVersion version = new ServiceVersion();
		RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
		version.setBootClassPath(mx.getBootClassPath());
		version.setClassPath(mx.getClassPath());
		version.setCmdArgs(mx.getInputArguments());
		version.setSystemProperties(mx.getSystemProperties());
		version.setStartTime(new Date(mx.getStartTime()));
		version.setUpTime(mx.getUptime());
		return new ResponseEntity<ServiceVersion>(version, null, HttpStatus.OK);
	}

}
