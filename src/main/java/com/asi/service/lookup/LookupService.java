package com.asi.service.lookup;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asi.core.repo.lookup.LookupValuesRepo;
import com.asi.service.lookup.vo.AsiColor;
import com.asi.service.lookup.vo.AsiColorsList;

@RestController
@RequestMapping("resources/lookup")
public class LookupService {
	@Autowired LookupValuesRepo colorRepository;
	@Secured("ROLE_CUSTOMER")
	@RequestMapping(value = "colors", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<AsiColorsList> getColor(HttpEntity<byte[]> requestEntity) throws UnsupportedEncodingException {
		List<AsiColor> colors = colorRepository.getColors();
		AsiColorsList colorResponse = new AsiColorsList();
		colorResponse.setColorGroup(colors);
		
		return new ResponseEntity<AsiColorsList>(colorResponse, null, HttpStatus.OK);
	}
}
