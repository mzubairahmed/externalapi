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
	
	@RequestMapping(value = "colors", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ColorsList> getColorsList()
	{
		ColorsList colorsList = lookupValueRepository.getAllColors();
		return new ResponseEntity<ColorsList> (colorsList, null, HttpStatus.OK);		
	}

	@RequestMapping(value = "materials", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<MaterialsList> getMaterialsList()
	{
		MaterialsList materialsList = lookupValueRepository.getAllMaterials();
		return new ResponseEntity<MaterialsList> (materialsList, null, HttpStatus.OK);		
	}

	@RequestMapping(value = "shapes", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ShapesList> getShapesList()
	{
		ShapesList shapesList = lookupValueRepository.getAllShapes();
		return new ResponseEntity<ShapesList> (shapesList, null, HttpStatus.OK);		
	}

	@RequestMapping(value = "packages", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<PackagesList> getPackagingList()
	{
		PackagesList packagesList = lookupValueRepository.getAllPackages();
		return new ResponseEntity<PackagesList> (packagesList, null, HttpStatus.OK);		
	}

	@RequestMapping(value = "safetywarnings", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SafetyWarningsList> getSafetyWarnings()
	{
		SafetyWarningsList safetyList = lookupValueRepository.getSafetyWarningsList();
		return new ResponseEntity<SafetyWarningsList> (safetyList, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "imprintmethods", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ImprintMethodsList> getImprintMethods()
	{
		ImprintMethodsList imprintMethodList = lookupValueRepository.getImprintMethodsList();
		return new ResponseEntity<ImprintMethodsList> (imprintMethodList, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "artworks", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ArtworksList> getArtworks()
	{
		ArtworksList artworksList = lookupValueRepository.getArtworksList();
		return new ResponseEntity<ArtworksList> (artworksList, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "compliances", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<ComplianceList> getComplianceCerts()
	{
		ComplianceList complianceList = lookupValueRepository.getComplianceList();
		return new ResponseEntity<ComplianceList> (complianceList, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "discountrates", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<DiscountRatesList> getDiscountRates()
	{
		DiscountRatesList discountList = lookupValueRepository.getDiscountList();
		return new ResponseEntity<DiscountRatesList> (discountList, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "currencies", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<CurrencyList> getCurrencyNames()
	{
		CurrencyList currencyList = lookupValueRepository.getCurrenciesList();
		return new ResponseEntity<CurrencyList> (currencyList, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "criteriacodes", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<CriteriaCodesList> getCriteriaCodesList()
	{
		CriteriaCodesList criteriaCodesList = lookupValueRepository.getCriteriaCodesList();
		return new ResponseEntity<CriteriaCodesList> (criteriaCodesList, null, HttpStatus.OK);		
	}
	
}
