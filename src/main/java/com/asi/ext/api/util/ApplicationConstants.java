package com.asi.ext.api.util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.asi.ext.api.product.criteria.processor.ProductSizeGroupProcessor;

public class ApplicationConstants {
    private final static Logger                      LOGGER                                      = Logger.getLogger(ApplicationConstants.class
                                                                                                         .getName());
    // Timeout configurations
    public static final int                          PRODUCT_CONNECTION_TIMEOUT                  = 120000;
    public static final int                          PRODUCT_SOCKET_READ_TIMEOUT                 = 120000;
    public final static boolean                      TRACE_ENABLED                               = LOGGER.isTraceEnabled();

    public static final String                       PRODUCT_CRT_SPLITTER_CODE                   = "\\^\\^\\^";
    public static final String                       PRODUCT_CRT_SPLITTER_CODE_NOT_REG           = "^^^";
    public static final String                       PRODUCT_RPBST_ELEMENT_SPLITTER_CODE         = "Z###";
    public static final String                       PRICE_CRT_SPLITTER_BASECRITERIA             = "\\$\\$\\$";
    public static final String                       PRICE_CRT_SPLITTER_REG_EX                   = "\\$\\$\\$\\$";
    public static final String                       PRODUCT_UPDATE_VALIDATE_CHAR                = "----";
    public static final String                       PRODUCT_CRITERIA_VALUE_SPLITTER             = "___";
    public static final String                       ENCODING                                    = "UTF-8";

    // Date Format
    public static final String                       DATE_FORMAT                                 = "MM/dd/yy";
    // Application URLs
    public static final String                       REST_API_IMPORT                             = "ws.api.product.import";

    public static final String                       CRITERIA_INFO_URL                           = "lookup.criteria.info.url";
    public static final String                       ORIGIN_LOOKUP_URL                           = "lookup.orgin.url";
    public static final String                       COLORS_LOOKUP_URL                           = "lookup.color.url";
    public static final String                       MATERIALS_LOOKUP_URL                        = "lookup.material.url";
    public static final String                       CURRENCIES_LOOKUP_URL                       = "lookup.currencies.url";
    public static final String                       DISCOUNT_RATES_LOOKUP_URL                   = "lookup.discount.rates.url";
    public static final String                       PRICE_UNIT_LOOKUP_URL                       = "lookup.price.units.url";
    public static final String                       PRODUCT_CATEGORIES_LOOKUP_URL               = "lookup.product.categories.url";
    public static final String                       PRODUCT_SHAPES_LOOKUP_URL                   = "lookup.product.shapes.url";
    public static final String                       PRODUCT_THEMES_URL                          = "lookup.product.themes.url";
    public static final String                       PRODUCT_KEYWORDS_LOOKUP_URL                 = "lookup.product.keywords.url";
    public static final String                       SIZES_LOOKUP_URL                            = "lookup.sizes.url";
    public static final String                       SIZES_CRITERIA_LOOKUP_URL                   = "lookup.sizes.criteria.url";
    public static final String                       IMPRINT_LOOKUP_URL                          = "lookup.imprint.url";
    public static final String                       IMPRINT_ARTWORK_LOOKUP_URL                  = "lookup.imprint.artwork.url";
    public static final String                       SAFETY_WARNINGS_LOOKUP                      = "lookup.safety.warnings.url";
    public static final String                       PRICING_SUBTYPECODE_LOOKUP                  = "lookup.pricing.subtypecode.url";
    public static final String                       PRICING_USAGELEVEL_LOOKUP                   = "lookup.pricing.usage.level.url";
    public static final String                       PRODUCT_TRADENAMES_LOOKUP                   = "lookup.product.trade.names.url";
    public static final String                       PRODUCT_COMPLIANCECERTS_LOOKUP              = "lookup.product.compliance.certs.url";
    public static final String                       PACKAGING_LOOKUP                            = "lookup.packaging.url";
    public static final String                       BATCH_PROCESSING                            = "lookup.batch.processing";
    public static final String                       BATCH_PROCESSING_FINALIZE                   = "lookup.batch.processing.finalize";
    public static final String                       PRODUCT_MEDIA_CITATION                      = "lookup.media.citation.url";

    public static final String                       OPTION_PRODUCT_LOOKUP                       = "lookup.options.product.url";
    public static final String                       OPTION_SHIPPING_LOOKUP                      = "lookup.options.shipping.url";
    public static final String                       OPTION_IMPRINT_LOOKUP                       = "lookup.options.imprint.url";
    public static final String                       SIZE_GROUP_SHIPPING_WGHT_LOOKUP             = "lookup.sizegroup.shipping.weight.url";
    public static final String                       SIZE_GROUP_SHIPPING_DIMENSION_LOOKUP        = "lookup.sizegroup.shipping.dimension.url";

    public static final String                       ADDITIONAL_COLOR_LOOKUP                     = "lookup.options.imprint.url";
    public static final String                       ADDITIONAL_LOCATION_LOOKUP                  = "lookup.options.imprint.url";

    public static final String                       PRODUCT_SAMPLE_LOOKUP                       = "lookup.options.imprint.url";
    public static final String                       PRODUCT_SPEC_LOOKUP                         = "lookup.options.imprint.url";
    public static final String                       PRODUCT_SHIPPING_ITEM_LOOKUP                = "lookup.options.product.url";
    public static final String                       RUSH_TIME_LOOKUP                            = "lookup.rustime.url";
    public static final String                       PRODUCTION_TIME_LOOKUP                      = "lookup.rustime.url";
    public static final String                       IMPRINT_COLOR_LOOKUP                        = "lookup.imprintcolor.url";
    public static final String                       IMPRINT_SIZE_LOOKUP                         = "lookup.imprintsize.url";
    public static final String                       LESS_THAN_MIN_LOOKUP                        = "lookup.pricing.less.than.min.url";
    public static final String                       PRODUCT_TYPECODE_LOOKUP_URL                 = "lookup.product.typecodes.url";
    public static final String                       SELECTED_LINES_LOOKUP                       = "lookup.product.selected.lines.url";                                 ;
    public static final String                       FOBP_POINTS_LOOKUP                          = "lookup.product.fob.points.url";

    public static final int                          PRD_DESCRIPTION_MAX_LENGTH                  = 800;

    // Application Constants - CODE

    public static final String                       CONST_ORIGIN_CRITERIA_CODE                  = "ORGN";
    public static final String                       CONST_COLORS_CRITERIA_CODE                  = "PRCL";
    public static final String                       CONST_MATERIALS_CRITERIA_CODE               = "MTRL";
    public static final String                       CONST_SHAPE_CRITERIA_CODE                   = "SHAP";
    public static final String                       CONST_THEME_CRITERIA_CODE                   = "THEM";
    public static final String                       CONST_TRADE_NAME_CODE                       = "TDNM";
    public static final String                       CONST_IMPRINT_COLOR_CRITERIA_CODE           = "IMCL";
    public static final String                       CONST_IMPRINT_SIZE_CRITERIA_CODE            = "IMSZ";
    public static final String                       CONST_PACKAGE_CRITERIA_CODE                 = "PCKG";

    public static final String                       CONST_IMAGE_QUALITY_CODE                    = "UNKN";
    public static final String                       CONST_MEDIA_TYPE_CODE                       = "IM";

    public static final String                       CONST_PRODUCTION_TIME_CRITERIA_CODE         = "PRTM";

    public static final String                       CONST_RUSH_TIME_CRITERIA_CODE               = "RUSH";
    public static final String                       CONST_STRING_SAME_DAY_RUSH_SERVICE          = "SDRU";

    public static final String                       CONST_SHIPPING_ITEM_CRITERIA_CODE           = "SHES";

    public static final String                       CONST_PRODUCT_SAMPLE_CRITERIA_CODE          = "SMPL";

    public static final String                       CONST_BASE_PRICE_GRID_CODE                  = "REGL";
    public static final String                       CONST_UPCHARGE_PRICE_GRID_CODE              = "OTCH";
    public static final String                       CONST_MARKET_SEGMENT_CODE                   = "USAALL";
    public static final String                       CONST_LESS_THAN_MIN_CRT_CODE                = "LMIN";
    public static final String                       CONST_PRICE_GRID_ITEM_CODE                  = "CHRG";
    public static final String                       CONST_SIZE_OTHER_CODE                       = "SOTH";
    public static final String                       CONST_IMPRINT_METHOD_CODE                   = "IMMD";
    public static final String                       CONST_ARTWORK_CODE                          = "ARTW";
    public static final String                       CONST_MINIMUM_QUANTITY                      = "MINO";
    public static final String                       CONST_STRING_CONST_QUR                      = "QUR";
    public static final String                       CONST_VALUE_TYPE_CODE_LOOK                  = "LOOK";
    public static final String                       CONST_VALUE_TYPE_CODE_LIST                  = "LIST";
    public static final String                       CONST_VALUE_TYPE_CODE_CUST                  = "CUST";
    public static final String                       CONST_VALUE_TYPE_CODE_COLOR                 = "COLR";
    public static final String                       CONST_VALUE_TYPE_CODE_PMS                   = "PMSN";

    // Size Groups - CODE

    public static final String                       CONST_SIZE_GROUP_CAPACITY                   = "CAPS";
    public static final String                       CONST_SIZE_GROUP_DIMENSION                  = "DIMS";
    public static final String                       CONST_SIZE_GROUP_SHIPPING_WEIGHT            = "SHWT";

    public static final String                       CONST_SIZE_GROUP_SHIPPING_DIMENSION         = "SDIM";

    public static final String                       CONST_SIZE_GROUP_SHIPPING_VOL_WEI           = "SVWT";
    public static final String                       CONST_SIZE_GROUP_SHP_APR_BRA                = "SABR";
    public static final String                       CONST_SIZE_GROUP_SHP_APR_INF_TLDR           = "SAIT";
    public static final String                       CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE      = "SANS";
    public static final String                       CONST_SIZE_GROUP_SHP_APR_PANT_SIZE          = "SAWI";
    public static final String                       CONST_SIZE_GROUP_SHP_APR_HSR_UNIFORM        = "SAHU";
    public static final String                       CONST_SIZE_GROUP_SHP_APR_STD_NUM            = "SSNM";
    // Option lists - CODE

    public static final String                       CONST_PRODUCT_OPTION                        = "PROP";

    public static final String                       CONST_SHIPPING_OPTION                       = "SHOP";

    public static final String                       CONST_IMPRINT_OPTION                        = "IMOP";

    // Additional Codes - CODE

    public static final String                       CONST_ADDITIONAL_LOCATION                   = "ADLN";

    public static final String                       CONST_ADDITIONAL_COLOR                      = "ADCL";

    public static final String                       CONST_CRITERIA_CODE_LNNM                    = "LNNM";
    public static final String                       CONST_CRITERIA_CODE_FOBP                    = "FOBP";

    public static final String                       PRODUCT_KEYWORD_TYPE_CODE                   = "HIDD";
    // Batch Error Log Codes - CODE

    public static final String                       CONST_BATCH_ERR_REQ_FIELD                   = "IREQ";
    public static final String                       CONST_BATCH_ERR_INVALID_VALUE               = "IICF";
    public static final String                       CONST_BATCH_ERR_MIN_QTY_WO_IMP              = "IIMP";
    public static final String                       CONST_BATCH_ERR_UP_CRT                      = "IUPC";
    public static final String                       CONST_BATCH_ERR_LOOKUP_VALUE_NOT_EXIST      = "ILOK";
    public static final String                       CONST_BATCH_ERR_GENERIC_PLHDR               = "IGNL";
    public static final String                       CONST_BATCH_ERR_GENERIC_ERROR               = "GERR";
    public static final String                       CONST_BATCH_TYPE_CODE_IMPRT                 = "IMRT";

    // String litterals
    public static final String                       CONST_STRING_CUSTOM                         = "Custom";
    public static final String                       CONST_STRING_STANDARD                       = "Standard";
    public static final String                       CONST_STRING_OTHER_SIZES                    = "Other Sizes";

    public static final String                       CONST_STRING_CAPACITY                       = "Capacity";
    public static final String                       CONST_STRING_ADDITIONAL_LOCATION            = "AdditionalLocation";
    public static final String                       CONST_STRING_ADDITIONAL_COLOR               = "AdditionalColor";
    public static final String                       CONST_STRING_SHIPPING_DIMENSION             = "ShippingDimension";
    public static final String                       CONST_STRING_SHIPPING_WEIGHT                = "ShippingWeight";
    public static final String                       CONST_STRING_DIMENSION                      = "Dimension";
    public static final String                       CONST_STRING_VOLUME_WEIGHT                  = "Volume/Weight";
    public static final String                       CONST_STRING_APPAREL                        = "Apparel";
    public static final String                       CONST_STRING_APPAREL_INFANT_TODDLER         = "Apparel-Infant/Toddler (3 Months, 2T)";
    public static final String                       CONST_STRING_APPAREL_DRESS_SHIRT_SIZES      = "Apparel-Neck/Sleeve";
    public static final String                       CONST_STRING_APPAREL_PANTS_SIZES            = "Apparel-Waist/Inseam";
    public static final String                       CONST_STRING_APPAREL_HOSIERY_UNIFORM        = "Apparel-Hoisery/Uniform (A,AB)";
    public static final String                       CONST_STRING_APPAREL_BRA_SIZES              = "Apparel-Bra Sizes";
    public static final String                       CONST_STRING_APPAREL_BRA_SIZES_OTHER        = "Apparel-Bra Sizes - Other";
    public static final String                       CONST_STRING_STANDARD_NUMBERED              = "Standard & Numbered";
    public static final String                       CONST_STRING_STANDARD_NUMBERED_OTHER        = "Standard & Numbered - Other";
    public static final String                       CONST_STRING_PRODUCT_OPTION                 = "Product Option";
    public static final String                       CONST_STRING_SHIPPING_OPTION                = "Shipping Option";
    public static final String                       CONST_STRING_IMPRINT_OPTION                 = "Imprint Option";

    public static final String                       CONST_STRING_WEIGHT                         = "Weight";
    public static final String                       CONST_STRING_WEIGHT_SHORT                   = "WEIG";
    public static final String                       CONST_STRING_VOLUME                         = "Volume";
    public static final String                       CONST_STRING_SIZE                           = "Size";
    public static final String                       CONST_STRING_SIZE_CAP                       = "SIZE";

    public static final String                       CONST_STRING_KEY                            = "Key";
    public static final String                       CONST_STRING_VALUE                          = "Value";
    public static final String                       CONST_STRING_CODE_VALUE                     = "CodeValue";
    public static final String                       CONST_STRING_ID_CAP                         = "ID";
    public static final String                       CONST_STRING_OTHER                          = "Other";
    public static final String                       CONST_STRING_PIECE                          = "Piece";
    public static final String                       CONST_STRING_DESCRIPTION                    = "Description";
    public static final String                       CONST_STRING_DISPLAY_NAME                   = "DisplayName";
    public static final String                       CONST_STRING_ITEMS_PER_UNIT                 = "ItemsPerUnit";
    public static final String                       CONST_STRING_RUSH_SERVICE                   = "Rush Service";
    public static final String                       CONST_STRING_SAME_DAY_SERVICE               = "Same Day Service";
    public static final String                       CONST_STRING_METAL                          = "Metal";
    public static final String                       CONST_STRING_UNCLASSIFIED_OTHER             = "UNCLASSIFIED/OTHER";
    // Numbers
    public static final String                       CONST_STRING_ZERO                           = "0";
    public static final String                       CONST_STRING_PRICE_UNIT_DEFAULT_ID          = "3162";

    // Generic Constants
    public static final String                       CONST_STRING_NONE_CAP                       = "NONE";
    public static final String                       CONST_STRING_NONE_SMALL                     = "none";

    public static final String                       CONST_STRING_NULL_CAP                       = "NULL";
    public static final String                       CONST_STRING_NULL_SMALL                     = "null";

    public static final String                       CONST_STRING_FALSE_CAP                      = "FALSE";
    public static final String                       CONST_STRING_FALSE_SMALL                    = "false";

    public static final String                       CONST_STRING_TRUE_CAP                       = "TRUE";
    public static final String                       CONST_STRING_TRUE_SMALL                     = "true";

    public static final String                       CONST_STRING_YES_CAP                        = "YES";
    public static final String                       CONST_STRING_YES_SMALL                      = "yes";

    public static final String                       CONST_STRING_NO_CAP                         = "NO";
    public static final String                       CONST_STRING_NO_SMALL                       = "no";
    public static final String                       CONST_STRING_COMMA_SEP                      = ",";

    // Units
    public static final String                       CONST_STRING_UNITS                          = "UNITS";
    public static final String                       CONST_STRING_UNIT	                         = "UNIT";
    public static final String                       CONST_STRING_FEET_SHORT_SMALL               = "ft";
    public static final String                       CONST_STRING_FEET_SHORT_CAP                 = "FT";
    public static final String                       CONST_STRING_FEET_CAP                       = "FEET";
    public static final String                       CONST_STRING_FEET_SMALL                     = "feet";

    public static final String                       CONST_STRING_INCH_SHORT_SMALL               = "in";
    public static final String                       CONST_STRING_INCH_SHORT_CAP                 = "IN";
    public static final String                       CONST_STRING_INCH_CAP                       = "INCH";
    public static final String                       CONST_STRING_INCH_SMALL                     = "inch";

    public static final String                       CONST_STRING_UNIT_MONTH                     = "MONT";

    public static final String                       CONST_STRING_HTTP                           = "http";
    public static final String                       CONST_STRING_WWW                            = "www";
    public static final String                       CONST_STRING_EMPTY                          = "";

    protected static Map<String, ArrayDeque<String>> CON_MAP_RESERVED_WORD_GROUP                 = null;
    public static final String                       CONST_STRING_GRP_SHIP_BILL_BY               = "SBB";

    public static final String                       CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE_NECK = "Neck";
    public static final String                       CONST_SIZE_GROUP_SHP_APR_DRS_SHRT_SIZE_SLVS = "Sleeve";
    public static final String                       CONST_SIZE_GROUP_SHP_APR_PANT_SIZE_WAIST    = "Waist";
    public static final String                       CONST_SIZE_GROUP_SHP_APR_PANT_SIZE_INSEAM   = "Inseam";

    public static final String                       CONST_ERROR_MSG_PRICE_INCREASE_ORDER        = "Price values must decrease in the pricing grid from left to right";
    public static final String                       CONST_ERROR_MSG_QUANTITY_DECREASE_ORDER     = "Quantities must increase in the pricing grid from left to right";

    public static final Set<String>                  BASE_PRICE_CRITERIA_SET                     = new HashSet<String>();
    public static final Set<String>                  UPCHARGE_PRICE_CRITERIA_SET                 = new HashSet<String>();
    // Base Price criteria codes
    private static final String[]                    BASE_PRICE_CRITERIA_CODES                   = { "CAPS", "DIMS", "IMMD",
            "IMOP", "IMSZ", "MTRL", "ORGN", "PCKG", "PRCL", "PROP", "FOBP", "PRTM", "RUSH", "SABR", "SAHU", "SAIT", "SANS", "SAWI",
            "SDRU", "SHAP", "SHOP", "SOTH", "SSNM", "SVWT", "TDNM"                              };
    // Up-Charge Price criteria codes
    private static final String[]                    UPCHARGE_PRICE_CRITERIA_CODES               = { "ADCL", "ADLN", "CAPS",
            "DIMS", "IMCL", "IMMD", "IMOP", "IMSZ", "LMIN", "MTRL", "ORGN", "PCKG", "FOBP", "PRCL", "PROP", "RUSH", "SABR", "SAHU",
            "SAIT", "SANS", "SAWI", "SDRU", "SHAP", "SHOP", "SMPL", "SOTH", "SSNM", "SVWT"      };

    public final static List<String>                 SIZE_GROUP_CRITERIACODES                    = Arrays.asList(ProductSizeGroupProcessor.SIZE_GROUP_CRITERIACODES);

    public static final String                       CONST_SELECTED_LINE_NAMES                   = "LNNM";
    public static final String                       CONST_PROD_UNDER_REVIEW                     = "INRV";

    // Load required elements at the startup
    static {
        // Load Constant Collection of Reserved words,
        // Now hard coding the reserved words to later in future we can dynamically collect these.
        // **** DO NOT ADD UNIQUE KEYS, and USE UPPER CASE FOR THE VALUES IF THE WORD ARE NOT CASE SENSITIVE ****
        CON_MAP_RESERVED_WORD_GROUP = new Hashtable<>();
        // Loading Reserved word for SHIPPERS BILLS BY
        ArrayDeque<String> shippersBillsBy = new ArrayDeque<>();
        shippersBillsBy.add("WEIGHT");
        shippersBillsBy.add("SIZE");

        CON_MAP_RESERVED_WORD_GROUP.put(CONST_STRING_GRP_SHIP_BILL_BY, shippersBillsBy);

        // Loading BasePrice Criteria Codes
        for (String code : BASE_PRICE_CRITERIA_CODES) {
            BASE_PRICE_CRITERIA_SET.add(code);

        }
        // Loading Upcharge Price Criteria Codes
        for (String code : UPCHARGE_PRICE_CRITERIA_CODES) {
            UPCHARGE_PRICE_CRITERIA_SET.add(code);
        }

    }
}
