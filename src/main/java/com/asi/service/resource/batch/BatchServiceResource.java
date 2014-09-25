package com.asi.service.resource.batch;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.asi.core.exception.ErrorMessage;
import com.asi.core.exception.ErrorMessageHandler;
import com.asi.ext.api.service.BatchService;
import com.asi.service.batch.client.vo.Batch;
import com.asi.service.product.exception.ExternalApiAuthenticationException;

@RestController
@RequestMapping("batch")
public class BatchServiceResource {
	
	@Autowired
	ErrorMessageHandler errorMessageHandler;
	
	@Autowired
	BatchService service;
	
    private static Logger _LOGGER = LoggerFactory.getLogger(BatchServiceResource.class);

	@RequestMapping(value = "Create", method = RequestMethod.POST, headers = "content-type=application/json, application/xml", 
			produces = {"application/xml; charset=UTF-8", "application/json; charset=UTF-8" })
	public ResponseEntity<Batch> createBatch(@RequestHeader("AuthToken") String authToken) throws ExternalApiAuthenticationException {
		
		if(_LOGGER.isTraceEnabled()) {
			_LOGGER.trace("Inovking Create Batch Service on External API...");
		}
		
		if(StringUtils.isEmpty(authToken)) {
			return new ResponseEntity<Batch>(null, null, HttpStatus.UNAUTHORIZED);
		}
		
		try {
			return service.create(authToken);
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			throw e;
		}
		
	}
	
	@RequestMapping(value = "Finalize", method = RequestMethod.PUT, headers = "content-type=application/json, application/xml", 
			produces = {"application/xml; charset=UTF-8", "application/json; charset=UTF-8" })
	public ResponseEntity<Batch> finalizeBatch(@RequestHeader("AuthToken") String authToken, HttpEntity<Batch> payload) throws ExternalApiAuthenticationException {

		if(_LOGGER.isTraceEnabled()) {
			_LOGGER.trace("Inovking Finalize Batch Service on External API...");
		}
		
		if(StringUtils.isEmpty(authToken)) {
			return new ResponseEntity<Batch>(null, null, HttpStatus.UNAUTHORIZED);
		}
		
		try {
			return service.finalize(authToken, payload.getBody());
		} catch (Exception e) {
			_LOGGER.error(e.getMessage(), e);
			throw e;
		}
		
	}
	
	
    @ExceptionHandler(ExternalApiAuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleIOException(ExternalApiAuthenticationException ex, HttpServletRequest request) {
        ErrorMessage errorInfo = errorMessageHandler.prepairError("error.authentication.id", request, null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<ErrorMessage>(errorInfo, null, HttpStatus.BAD_REQUEST);
    }


}
