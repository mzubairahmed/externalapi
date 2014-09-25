package com.asi.ext.api.service;

import org.springframework.http.ResponseEntity;

import com.asi.service.batch.client.vo.Batch;
import com.asi.service.product.exception.ExternalApiAuthenticationException;

public interface BatchService {
	
	public ResponseEntity<Batch> create(String authToken) throws ExternalApiAuthenticationException;
	
	public ResponseEntity<Batch> finalize(String authToken, Batch batch) throws ExternalApiAuthenticationException;

}
