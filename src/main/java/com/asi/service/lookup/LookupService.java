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
import com.asi.service.lookup.vo.ArtworksList;
import com.asi.service.lookup.vo.AsiColor;
import com.asi.service.lookup.vo.AsiColorsList;
import com.asi.service.lookup.vo.CategoriesList;
import com.asi.service.lookup.vo.SizeInfo;
import com.asi.service.product.client.vo.material.Materials;
import com.asi.service.product.vo.OriginOfCountries;

@RestController
@RequestMapping("resources/lookup")
public class LookupService {
	@Autowired private LookupValuesRepo lookupValueRepository;
	@Autowired
	private MessageSource messageSource;
	@RequestMapping(value = "colors", headers="content-type=application/com.asi.util.json, application/xml" ,produces={"application/xml", "application/com.asi.util.json"} )
	public ResponseEntity<AsiColorsList> getColor() throws UnsupportedEncodingException {
		List<AsiColor> colors = lookupValueRepository.getColors();
		AsiColorsList colorResponse = new AsiColorsList();
		colorResponse.setColorGroup(colors);
		
		return new ResponseEntity<AsiColorsList>(colorResponse, null, HttpStatus.OK);
	}
	@RequestMapping(value = "origins", headers="content-type=application/com.asi.util.json, application/xml" ,produces={"application/xml", "application/com.asi.util.json"} )
	public ResponseEntity<OriginOfCountries> getcountryOfOrigin()
	{
		OriginOfCountries countires = lookupValueRepository.getOrigin();
		return new ResponseEntity<OriginOfCountries> (countires, null, HttpStatus.OK);
		
	}
	@RequestMapping(value = "materials", headers="content-type=application/com.asi.util.json, application/xml" ,produces={"application/xml", "application/com.asi.util.json"} )
	public ResponseEntity<Materials> getMaterials()
	{
		Materials materialsList = lookupValueRepository.getAllMaterials();
		return new ResponseEntity<Materials> (materialsList, null, HttpStatus.OK);
		
	}
	@RequestMapping(value = "sizeInfo", headers="content-type=application/com.asi.util.json, application/xml" ,produces={"application/xml", "application/com.asi.util.json"} )
	public ResponseEntity<SizeInfo> getsizeInfo()
	{
		SizeInfo sizeInfo = lookupValueRepository.getSizeInfo();
		return new ResponseEntity<SizeInfo> (sizeInfo, null, HttpStatus.OK);
		
	}	
	@RequestMapping(value = "categoriesList", headers="content-type=application/com.asi.util.json, application/xml" ,produces={"application/xml", "application/com.asi.util.json"} )
	public ResponseEntity<CategoriesList> getcategoriesList()
	{
		CategoriesList categoriesList = lookupValueRepository.getAllCategories();
		return new ResponseEntity<CategoriesList> (categoriesList, null, HttpStatus.OK);
		
	}	
	@RequestMapping(value = "artworksList", headers="content-type=application/com.asi.util.json, application/xml" ,produces={"application/xml", "application/com.asi.util.json"} )
	public ResponseEntity<ArtworksList> getArtworksList()
	{
		ArtworksList artworkList = lookupValueRepository.getAllArtworks();
		return new ResponseEntity<ArtworksList> (artworkList, null, HttpStatus.OK);
		
	}
}
