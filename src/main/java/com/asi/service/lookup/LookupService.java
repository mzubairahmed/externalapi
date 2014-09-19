package com.asi.service.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asi.core.repo.lookup.LookupValuesRepo;
import com.asi.ext.api.util.ApplicationConstants;
import com.asi.service.lookup.vo.CategoriesList;
import com.asi.service.lookup.vo.ThemesList;

@RestController
@RequestMapping("lookup")
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
	@RequestMapping(value = "upchargetypes", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<UpchargeTypesList> getUpchargeTypesList()
	{
		UpchargeTypesList upchargeTypesList = lookupValueRepository.getUpchargeTypes();
		return new ResponseEntity<UpchargeTypesList> (upchargeTypesList, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "upchargelevels", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<UpchargeLevelsList> getUpchargeLevelsList()
	{
		UpchargeLevelsList upchargeLevelsList = lookupValueRepository.getUpchargeLevels();
		return new ResponseEntity<UpchargeLevelsList> (upchargeLevelsList, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "pricemodifiers", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<PriceModifiers> getPriceModifiersList()
	{
		PriceModifiers priceModifiers = lookupValueRepository.getPriceModifiers();
		return new ResponseEntity<PriceModifiers> (priceModifiers, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/apparelbra", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getApperalBrasList()
	{
		SizeUnits sizeUnits = new SizeUnits();
		sizeUnits.setSizes(lookupValueRepository.getSizeUnitsInfo(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_BRA,ApplicationConstants.CONST_STRING_UNIT));
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/dressshirt", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getDressShirtsList()
	{
		SizeUnits sizeUnits = new SizeUnits();
		sizeUnits.setSizes(lookupValueRepository.getSizeUnitsInfo(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE,ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE_NECK));
		//List<String> dressShirtUnits=sizeUnits.getValues();
	//	sizeUnits=lookupValueRepository.getSizeUnitsInfo(dressShirtUnits,ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE,ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE_SLVS);
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/hoiseryuniform", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getHoiseryUniformList()
	{
		SizeUnits sizeUnits = new SizeUnits();
		sizeUnits.setSizes(lookupValueRepository.getSizeUnitsInfo(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM,ApplicationConstants.CONST_STRING_UNIT));
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/infanttoddler", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getInfantToddlerList()
	{
		SizeUnits sizeUnits = new SizeUnits();
		sizeUnits.setSizes(lookupValueRepository.getSizeUnitsInfo(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_INF_TLDR,ApplicationConstants.CONST_STRING_UNIT));
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/apparelpants", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getApparelPantsList()
	{
		SizeUnits sizeUnits = new SizeUnits();
		sizeUnits.setSizes(lookupValueRepository.getSizeUnitsInfo(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE,ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_PANT_SIZE_INSEAM));
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/standardnumbered", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getStandardNumberedList()
	{
		SizeUnits sizeUnits = new SizeUnits();
		sizeUnits.setSizes(lookupValueRepository.getSizeUnitsInfo(ApplicationConstants.CONST_SIZE_GROUP_SHP_APR_STD_NUM,ApplicationConstants.CONST_STRING_UNIT));
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/volumeweight", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getVolumeWeightList()
	{
		SizeUnits sizeUnits = new SizeUnits();
		sizeUnits.setSizes(lookupValueRepository.getSizeUnitsInfo(ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI,ApplicationConstants.CONST_STRING_UNIT));
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/capacity", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getCapacityList()
	{
		SizeUnits sizeUnits = new SizeUnits();
		sizeUnits.setSizes(lookupValueRepository.getSizeUnitsInfo(ApplicationConstants.CONST_SIZE_GROUP_CAPACITY,ApplicationConstants.CONST_STRING_UNIT));
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	/*@RequestMapping(value = "sizes/dimension/attributes", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getDimensionAttributesList()
	{
		SizeUnits sizeUnits = null;//lookupValueRepository.getSizeAttributesInfo(null,ApplicationConstants.CONST_SIZE_GROUP_DIMENSION,ApplicationConstants.CONST_STRING_UNIT);
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}*/
	@RequestMapping(value = "sizes/dimension/units", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getDimensionUnitsList()
	{
		SizeUnits sizeUnits = lookupValueRepository.getSizeUnitsOfMeasurements(null,ApplicationConstants.CONST_SIZE_GROUP_DIMENSION,ApplicationConstants.CONST_STRING_UNIT);
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/capacity/units", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getCapacityUnitsList()
	{
		SizeUnits sizeUnits = lookupValueRepository.getSizeUnitsOfMeasurements(null,ApplicationConstants.CONST_SIZE_GROUP_CAPACITY,ApplicationConstants.CONST_STRING_UNIT);
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "sizes/volumeweight/units", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getVolumeWeightUnitsList()
	{
		SizeUnits sizeUnits = lookupValueRepository.getSizeUnitsOfMeasurements(null,ApplicationConstants.CONST_SIZE_GROUP_SHIPPING_VOL_WEI,ApplicationConstants.CONST_STRING_UNIT);
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}

	@RequestMapping(value = "sizes/other", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<SizeUnits> getOtherSizesList()
	{
		SizeUnits sizeUnits = new SizeUnits();
		sizeUnits.setSizes(lookupValueRepository.getSizeUnitsInfo(ApplicationConstants.CONST_SIZE_OTHER_CODE,ApplicationConstants.CONST_STRING_UNIT));
		return new ResponseEntity<SizeUnits> (sizeUnits, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "mediacitations", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<MediaCitation> getMediaCitationsList(@RequestHeader("AuthToken") String authToken)
	{
		MediaCitation mediaCitation = new MediaCitation();
		mediaCitation.setMediaCitation(lookupValueRepository.getMediaCitationsList(authToken));
		return new ResponseEntity<MediaCitation> (mediaCitation, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "selectedlinenames", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<LineNames> getLineNamesList(@RequestHeader("AuthToken") String authToken)
	{
		LineNames lineNames = new LineNames();
		lineNames.setLineNames(lookupValueRepository.getLineNamesList(authToken));
		return new ResponseEntity<LineNames> (lineNames, null, HttpStatus.OK);		
	}
	@RequestMapping(value = "fobpoints", headers="content-type=application/json, application/xml" ,produces={"application/xml", "application/json"} )
	public ResponseEntity<FobPoints> getFobPointsList(@RequestHeader("AuthToken") String authToken)
	{
		FobPoints fobPoints = new FobPoints();
		fobPoints.setFobpoints(lookupValueRepository.getFobPointsList(authToken));
		return new ResponseEntity<FobPoints> (fobPoints, null, HttpStatus.OK);		
	}
//
}
