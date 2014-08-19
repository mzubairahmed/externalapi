package com.asi.service.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asi.core.repo.lookup.LookupValuesRepo;
import com.asi.service.lookup.vo.CategoriesList;
import com.asi.service.lookup.vo.ThemesList;

@RestController
@RequestMapping("resources/lookup")
public class LookupService {
	@Autowired private LookupValuesRepo lookupValueRepository;

	@RequestMapping(value = "categoriesList", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<CategoriesList> getcategoriesList()
	{
		CategoriesList categoriesList = lookupValueRepository.getAllCategories();
		return new ResponseEntity<CategoriesList> (categoriesList, null, HttpStatus.OK);		
	}
	
	@RequestMapping(value = "themes", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ThemesList> getThemesList()
	{
		ThemesList themesList = lookupValueRepository.getAllThemes();
		return new ResponseEntity<ThemesList> (themesList, null, HttpStatus.OK);		
	}

}
