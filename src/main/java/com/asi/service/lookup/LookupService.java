package com.asi.service.lookup;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asi.core.repo.lookup.LookupValuesRepo;
import com.asi.service.lookup.vo.AsiColor;
import com.asi.service.lookup.vo.AsiColorsList;
import com.asi.service.lookup.vo.SizeInfo;
import com.asi.service.product.vo.OriginOfCountries;

@RestController
@RequestMapping("resources/lookup")
public class LookupService {
	@Autowired private LookupValuesRepo lookupValueRepository;
	@Autowired
	private MessageSource messageSource;
	@RequestMapping(value = "colors", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<AsiColorsList> getColor() throws UnsupportedEncodingException {
		List<AsiColor> colors = lookupValueRepository.getColors();
		AsiColorsList colorResponse = new AsiColorsList();
		colorResponse.setColorGroup(colors);
		
		return new ResponseEntity<AsiColorsList>(colorResponse, null, HttpStatus.OK);
	}
	@RequestMapping(value = "origins", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<OriginOfCountries> getcountryOfOrigin()
	{
		OriginOfCountries countires = lookupValueRepository.getOrigin();
		return new ResponseEntity<OriginOfCountries> (countires, null, HttpStatus.OK);
		
	}
	@RequestMapping(value = "sizeInfo", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeInfo> getsizeInfo()
	{
		SizeInfo sizeInfo = lookupValueRepository.getSizeInfo();
		return new ResponseEntity<SizeInfo> (sizeInfo, null, HttpStatus.OK);
		
	}	
}
