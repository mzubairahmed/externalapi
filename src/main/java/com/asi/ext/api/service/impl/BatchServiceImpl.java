package com.asi.ext.api.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.asi.ext.api.service.BatchService;
import com.asi.service.batch.client.vo.Batch;
import com.asi.service.product.exception.ExternalApiAuthenticationException;

public class BatchServiceImpl implements BatchService {

	private static final Logger _LOGGER = Logger.getLogger(BatchServiceImpl.class);

	private RestTemplate restTemplate;

	private String createBatchURL;
	private String finalizeBatchURL;

	@Override
	public ResponseEntity<Batch> create(String authToken) throws ExternalApiAuthenticationException {

		if (_LOGGER.isTraceEnabled()) {
			_LOGGER.trace("Creating batch -- Hitting RADAR Create Batch Service...");
		}
		
		ResponseEntity<Batch> response = null;
		
		try {

			HttpHeaders header = new HttpHeaders();
			header.add("AuthToken", authToken);
			header.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> requestEntity = new HttpEntity<String>(header);
			response = restTemplate.exchange(createBatchURL, HttpMethod.POST, requestEntity, Batch.class);
			
		} catch (HttpClientErrorException hce) {
			_LOGGER.error("Exception while posting product to Radar API", hce);
			if (hce.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				throw new ExternalApiAuthenticationException(hce, null, hce.getStatusCode());
			}
		} catch (RestClientException ex) {
			_LOGGER.error(ex.getMessage());
		}

		return response;
	}

	@Override
	public ResponseEntity<Batch> finalize(String authToken, Batch batch) throws ExternalApiAuthenticationException {

		if (_LOGGER.isTraceEnabled()) {
			_LOGGER.trace("Creating batch -- Hitting RADAR Create Batch Service...");
		}
		
		ResponseEntity<Batch> response = null;

		try {

			HttpHeaders header = new HttpHeaders();
			header.add("AuthToken", authToken);
			header.setContentType(MediaType.APPLICATION_JSON);
			
			HttpEntity<Batch> requestEntity = new HttpEntity<Batch>(batch, header);
			
			response = restTemplate.exchange(finalizeBatchURL, HttpMethod.PUT, requestEntity, Batch.class);
			
			
		} catch (HttpClientErrorException hce) {
			_LOGGER.error("Exception while posting product to Radar API", hce);
			if (hce.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				throw new ExternalApiAuthenticationException(hce, null, hce.getStatusCode());
			} else {
				throw hce;
			}
		} catch (RestClientException ex) {
			_LOGGER.error(ex.getMessage());
		}

		
		return response;
	}

    /**
     * @param restTemplate
     *            the restTemplate to set
     */
    @Required
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param createBatchURL
     *            the createBatchURL to set
     */
    @Required
    public void setCreateBatchURL(String createBatchURL) {
        this.createBatchURL = createBatchURL;
    }

	/**
	 * @param finalizeBatchURL the finalizeBatchURL to set
	 */
	public void setFinalizeBatchURL(String finalizeBatchURL) {
		this.finalizeBatchURL = finalizeBatchURL;
	}
    


	
}
