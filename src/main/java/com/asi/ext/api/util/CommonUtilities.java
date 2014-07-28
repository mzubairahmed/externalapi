package com.asi.ext.api.util;

import java.io.StringReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

import com.asi.ext.api.radar.model.CriteriaSetValues;
import com.asi.ext.api.radar.model.Product;
import com.asi.ext.api.radar.model.ProductCriteriaSets;

public class CommonUtilities {

    public static final Logger  LOGGER                    = Logger.getLogger(CommonUtilities.class.getName());
    public static final String  NUMBER_FORMAT_REG_EX      = "^\\d+(\\.\\d{1,10})?$";
    private final static String SIMPLE_DATE_FORMAT_REG_EX = "^(((((0[1-9])|(1\\d)|(2[0-8]))\\/((0[1-9])|(1[0-2])))|((31\\/((0[13578])|(1[02])))|((29|30)\\/((0[1,3-9])|(1[0-2])))))\\/((20[0-9][0-9])|(19[0-9][0-9])))|((29\\/02\\/(19|20)(([02468][048])|([13579][26]))))$";

    /**
     * parse weight value
     * 
     * @param weightValue
     *            is the string need to be parsed
     * @return SIZE for size and WEIG for weight
     */
    public String parseWeightValue(String weightValue) {
        if (weightValue == null || weightValue.equalsIgnoreCase("null"))
            weightValue = null;
        else if (null != weightValue && weightValue.equalsIgnoreCase("weight"))
            weightValue = "WEIG";
        else if (null != weightValue && weightValue.equalsIgnoreCase("size")) weightValue = "SIZE";

        return weightValue;
    }

    /**
     * convert different form of boolean to true/false
     * 
     * @param String
     *            the string need to be parsed true/false
     * @return string true/false
     */
    public static String parseBooleanValue(String booleanValue) {
        if (null != booleanValue && !booleanValue.trim().isEmpty()
                && !booleanValue.trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_CAP)
                && !booleanValue.equalsIgnoreCase("no")) {
            if (booleanValue.equalsIgnoreCase("yes") || booleanValue.equalsIgnoreCase("true") || booleanValue.equalsIgnoreCase("y"))
                booleanValue = "true";
            else
                booleanValue = "false";
        } else
            booleanValue = "false";
        return booleanValue;
    }

    /*
     * private String parseUValue(String booleanValue) {
     * if(null!=booleanValue && !booleanValue.equalsIgnoreCase("no"))
     * {
     * if(booleanValue.equalsIgnoreCase("yes") || booleanValue.equalsIgnoreCase("true") || booleanValue.equalsIgnoreCase("y"))
     * booleanValue="U";
     * else
     * booleanValue="";
     * }else
     * booleanValue="";
     * return booleanValue;
     * }
     */
    /**
     * finds the number of elements in a given string separated by ","
     * 
     * @param srcString
     * @return the count of elements
     */
    public int findElementsCount(String srcString) {
        int count = 0;
        if (null != srcString) {
            srcString = srcString.trim();
            if (srcString.equals("")) {
                count = 0;
            } else if (srcString.contains(",")) {
                count++;
                for (int i = 0; i < srcString.length(); i++) {
                    if (srcString.charAt(i) == ',') count++;
                }
            } else
                count = 1;
        }
        return count;
    }

    /**
     * @param text
     *            String value to check
     * @return boolean, true if its a valid number
     */
    public boolean isThatValidNumber(String text) {
        boolean blnFlag = false;

        if (text.matches("[0-9.]+")) {
            blnFlag = true;
        }
        return blnFlag;
    }

    /**
     * @param text
     *            String value to check
     * @return boolean, true if its a valid number
     */
    public boolean isThatValidNumberWithCSV(String text) {
        boolean blnFlag = false;

        if (text.matches("[0-9., ]+")) {
            blnFlag = true;
        }
        return blnFlag;
    }

    /**
     * @param text
     *            String value to check
     * @return boolean, true if its a valid Integer
     */
    public boolean isThatValidInteger(String text) {
        boolean blnFlag = true;

        if (text.matches("[0-9$]+")) {
            blnFlag = true;
        }
        return blnFlag;
    }

    /**
     * Truncate the given string
     * 
     * @param value
     *            is the String to truncate
     * @param length
     *            is the truncate length
     * @return truncate string
     */
    public static String truncate(String value, int length) {
        if (value != null && value.length() > length) value = value.substring(0, length);
        return value;
    }

    /**
     * Splits the given string using the delimiter and checks the elements in the result array are null
     * 
     * @param srcString
     *            is the String we need to Split and validate
     * @param delimitter
     *            is the delimiter to to Split the spring
     * 
     * @return <b>True</b> at-least one element is a valid otherwise <B>False</B>
     */
    public static boolean isElementsAreNull(String srcString, String delimitter) {
        if (srcString != null && !srcString.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
            String[] srcElemArray = srcString.split("\\" + delimitter);
            if (srcElemArray != null && srcElemArray.length > 0) {
                for (int i = 0; i < srcElemArray.length; i++) {
                    if (srcElemArray[i] != null && !srcElemArray[i].equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)
                            && !srcElemArray[i].isEmpty()) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isValidDimension(String dimensions, String groupDelimitter, String valueSplitter) {
        if (dimensions != null && !dimensions.isEmpty() && !dimensions.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_CAP)) {

            String[] dimensionGroups = dimensions.split(groupDelimitter);
            if (dimensionGroups != null && (dimensionGroups.length >= 1 && dimensionGroups.length <= 3)) {
                for (String dimension : dimensionGroups) {
                    if (dimension != null && !dimension.isEmpty()
                            && !dimension.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_CAP)) {
                        String[] values = dimension.split(valueSplitter);
                        if (values != null && values.length == 3) {

                        } else {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Not Implemented yet
     * 
     * @param curPrice
     * @param extPrice
     * @return
     */
    public static boolean comparePrices(String curPrice, String extPrice) {
        return false;
    }

    /**
     * Checks whether the given string is empty or null, returns true if the given string is null
     * 
     * @param source
     *            is the String to check for null
     * @return true if the given string is null
     */
    public static boolean isValueNull(String source) {
        if (source != null && !source.trim().isEmpty()
                && !source.trim().equalsIgnoreCase(ApplicationConstants.CONST_STRING_NULL_SMALL)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks the given string is in any of the following forms Yes, Y, No, N
     * 
     * @param source
     *            is the String which we need to check for Yes or No
     * @return true if it matches the criteria else false
     * 
     */
    public static boolean isValueYesOrNo(String source) {

        if (source.equalsIgnoreCase(ApplicationConstants.CONST_STRING_YES_SMALL) || source.equalsIgnoreCase("Y")) {
            return true;
        } else if (source.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NO_SMALL) || source.equalsIgnoreCase("N")) {
            return true;
        }
        return false;
    }

    /**
     * Converts Yes or No to True or False
     * 
     * @param source
     *            is the String to check
     * @return True if value is Yes/Y otherwise False
     */
    public static boolean getBooleanValueFromYesOrNo(String source) {

        if (source != null
                && (source.equalsIgnoreCase(ApplicationConstants.CONST_STRING_YES_SMALL) || source.equalsIgnoreCase("Y"))) {
            return true;
        } else if (source != null
                && (source.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NO_SMALL) || source.equalsIgnoreCase("N"))) {
            return false;
        }
        return false;
    }

    /**
     * Checks whether the given PriceCriteria is well formed. A PriceCriteria should be in <CriteriaCode>:<Values>
     * 
     * @param sourceCriteria
     *            is the PriceCriteria to validate
     * @return true if its valid criteria
     */
    public static boolean isValidPriceCriteria(String sourceCriteria) {
        if (!isValueNull(sourceCriteria)) {
            String[] temp = sourceCriteria.split(":");
            if (temp != null && temp.length > 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Check whether the give sourceWord is in Application Specific reserved keyword List. Application specific keyword means,
     * we have set of values which belongs to certain fields. For Example : Shippers_Bills_By can hold only WEIGHT or SIZE
     * 
     * @param sourceWord
     *            word which we need to find or search
     * @param reservedWordGroup
     *            is the Group be the word belongs
     * @param isCaseSensitive
     *            is case sensitive if it false we convert the given sourceWord to UPPER CASE
     * @return true if value exist else false
     * 
     * @see ApplicationConstants
     */
    public static boolean isWordExistsInReservedWordGroup(String sourceWord, String reservedWordGroup, boolean isCaseSensitive) {

        if (sourceWord == null || sourceWord.isEmpty() || reservedWordGroup == null || reservedWordGroup.isEmpty()) {
            return false;
        }

        if (isCaseSensitive) {
            return isWordExistsInReservedGroup(sourceWord, reservedWordGroup, isCaseSensitive);
        } else {
            return isWordExistsInReservedGroup(sourceWord.toUpperCase(), reservedWordGroup, false);
        }
    }

    /**
     * Private function needs for internal reserve word detection
     */
    private static boolean isWordExistsInReservedGroup(final String sourceWord, final String reservedWordGroup,
            boolean caseSensitive) {
        return ApplicationConstants.CON_MAP_RESERVED_WORD_GROUP.get(reservedWordGroup) != null ? ApplicationConstants.CON_MAP_RESERVED_WORD_GROUP
                .get(reservedWordGroup).contains(sourceWord) : false;
    }

    /**
     * Get CSV values from the given String, this function will able to process embedded comma <B>(,)</B> in values up to one level.
     * Supported values
     * <dl>
     * <li>"a,b,c,d,1,&,C"
     * <li>"1,2,4,5,6"
     * <li>"abc,1234,"Test,Testing","1,2""
     * </dl>
     * 
     * @param source
     *            is CSV String
     * @return Array of CSV values
     */
    public static String[] getCSVValues(String source) {
        if (source != null && !source.isEmpty()) {
            String[] elements = source.split(ApplicationConstants.CONST_STRING_COMMA_SEP);
            List<String> finalResult = new ArrayList<String>();
            for (int i = 0; i < elements.length; i++) {

                if (elements[i] != null && elements[i].trim().startsWith("\"")) {
                    String temp = elements[i];
                    while (i < elements.length) {
                        i++;
                        if (elements[i] != null && elements[i].trim().endsWith("\"")) {
                            temp += ApplicationConstants.CONST_STRING_COMMA_SEP + elements[i];
                            break;
                        } else {
                            temp += ApplicationConstants.CONST_STRING_COMMA_SEP + elements[i];
                        }
                    }
                    finalResult.add(temp.replace("\"", ""));
                } else {
                    finalResult.add(elements[i]);
                }

            }
            return finalResult.toArray(new String[0]);
        } else {
            return null;
        }
    }

    public static List<String> getKeysByValue(Map<String, String> map, String value) {
        if (map != null && !map.isEmpty()) {
            List<String> keyList = new ArrayList<String>();
            for (Entry<String, String> entry : map.entrySet()) {
                if (value.equals(entry.getValue())) {
                    keyList.add(entry.getKey());
                }
            }
            return keyList;
        } else {
            return null;
        }
    }

    public static String getKeysByValueGen(Map<?, ?> map, String value) {
        String valueForId = "";
        if (map != null && !map.isEmpty()) {
            for (Entry<?, ?> entry : map.entrySet()) {
                if (value.equals(String.valueOf(entry.getValue()))) {
                    valueForId = String.valueOf(entry.getKey());
                    return valueForId;
                }
            }
            return valueForId;
        } else {
            return null;
        }
    }

    /**
     * Finds the number comma (,) in the given string.
     * 
     * @param currentString
     *            the source string we need to check
     * @return number of comma's
     */
    public static int commasCount(String currentString) {
        int commas = 0;
        for (int i = 0; i < currentString.length(); i++) {
            if (currentString.charAt(i) == ',') {
                commas++;
            }

        }
        return commas;
    }

    public static String getStringWithBrackets(String srcElement) {
        String returnString = ApplicationConstants.CONST_STRING_FALSE_SMALL;
        if (null != srcElement) {
            Pattern pattern = Pattern.compile("(?<=\\()[a-zA-Z0-9/ ]*(?=\\))");
            Matcher matcher = pattern.matcher(srcElement);

            while (matcher.find()) {
                returnString = matcher.group();
            }
        }
        return returnString;
    }

    /**
     * A dimension value should be in the given format <BR />
     * <B>Attribute1:Value:Units;Dimension2:Value:Units;Dimension3:Value:Units</B>
     * 
     * @param source
     *            is the dimension string needs to be validated
     * @return true if the given dimension is valid
     */
    public static boolean isValidDimension(String source) {

        if (!isValueNull(source)) {
            String[] sizeElements = source.split(";");
            if (sizeElements != null && sizeElements.length >= 0 && sizeElements.length <= 3) {
                Set<String> attributesSet = new HashSet<String>();

                for (int i = 0; i < sizeElements.length; i++) {
                    if (isValueNull(sizeElements[i])) {
                        return false;
                    }
                    // Taking individual group " Attribute1:Value:Units "
                    String[] sizeValues = sizeElements[i].split(":");
                    if (sizeValues != null && sizeValues.length == 3) {
                        if (!isValueNull(sizeValues[0])) {
                            attributesSet.add(sizeValues[0].trim().toUpperCase());
                        } else {
                            return false;
                        }
                        if (isValueNull(sizeValues[1]) /* || !isValidNumber(sizeValues[1]) */) {
                            return false;
                        }
                        if (isValueNull(sizeValues[2])) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
                if (attributesSet.size() != sizeElements.length) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isValidNumber(String source) {
        if (!isValueNull(source)) {

            return source.matches(NUMBER_FORMAT_REG_EX);
        } else {
            return false;
        }
    }

    public static boolean isUpdateNeeded(String source) {

        if (isValueNull(source)) {
            return true;
        } else if (source.trim().startsWith(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isUpdateNeeded(String source, boolean skipNullCheck) {
        if (!skipNullCheck) {
            return isUpdateNeeded(source);
        }
        if (source.trim().startsWith(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidForNewProduct(String source) {
        if (isValueNull(source)) {
            return false;
        } else if (source.trim().startsWith(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * This is a special conversion method for processing boolean typed values. This function will validate the give <b> src </b>
     * with the following string formats
     * <b>True</b> or <b>False</b> or <b>Yes</b> or <b>Y</b> or <b>No</b> or <b>N</b> irrespective of case. If the given value is
     * not null and not in the given format then return value will be null; <br />
     * <br />
     * <b>True</b>, <b>Yes</b>, <b>Y</b> will return <b>boolean true</b> <br />
     * <br />
     * <b>False</b>, <b>No</b>, <b>N</b>, <b>null</b> will return <b>boolean false</b>
     * 
     * @param src
     *            is the value to check
     * @return boolean value , If the given value is not null and not in the given format then return value will be null other wise
     *         corresponding boolean value
     */
    public static Boolean getBooleanValueFromString(String src) {
        if (isValueNull(src)) {
            return false;
        }
        if (src.equalsIgnoreCase(ApplicationConstants.CONST_STRING_TRUE_CAP)
                || src.equalsIgnoreCase(ApplicationConstants.CONST_STRING_YES_CAP) || src.equalsIgnoreCase("Y")) {
            return true;
        } else if (src.equalsIgnoreCase(ApplicationConstants.CONST_STRING_FALSE_CAP)
                || src.equalsIgnoreCase(ApplicationConstants.CONST_STRING_NO_CAP) || src.equalsIgnoreCase("N")) {
            return false;
        }
        return null;
    }

    public static ProductCriteriaSets getProductCriteriaSets(String criteriaCode, Product exisitingProduct) {
        return null;
    }

    /**
     * Checks whether the given string is integer or not. Provides faster result
     * 
     * @param src
     *            is the string needs to be validated
     * @return true if the src is a valid integer
     */
    public static boolean isInteger(String src, boolean checkPositiveValueOnly) {
        if (src == null) {
            return false;
        }
        src = src.trim();
        int length = src.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (checkPositiveValueOnly && (src.charAt(0) == '-' || (src.charAt(0) == '0') && src.length() == 1)) {
            return false;
        }
        if (src.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = src.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

    public static String getValueFromCSV(String src, int index) {
        try {
            if (index < 0 || isValueNull(src)) {
                return null;
            }
            String[] csvValues = getCSVValues(src);
            if (csvValues != null && csvValues.length >= index) {
                return csvValues[index - 1];
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isValidProductionTime(String src) {
        if (isValueNull(src)) {
            return false;
        }
        if (src.trim().contains("-")) {
            if (countSpecifiedChar(src.trim(), '-') > 1) {
                return false;
            } else {
                String[] temp = src.trim().split("-");
                if (temp != null) {
                    if (temp.length != 2) {
                        return false;
                    } else {
                        try {
                            if (isInteger(temp[0], true) && isInteger(temp[1], true)) {
                                return (Integer.parseInt(temp[1]) > Integer.parseInt(temp[0]));
                            } else {
                                return false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                } else {
                    return false;
                }

            }
        } else {
            return (isInteger(src.trim(), true));
        }
    }

    public static String checkAndUpdate(String source, String existing) {
        // TODO Auto-generated method stub
        if (isValueNull(source)) {
            return "";
        } else if (source.trim().startsWith(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR)) {
            return existing;
        } else {
            return source;
        }
    }

    public static String checkAndUpdate(String source) {
        // TODO Auto-generated method stub
        if (isValueNull(source)) {
            return source;
        } else if (source.trim().startsWith(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR)) {
            return "";
        } else {
            return source;
        }
    }

    public static boolean isValidCapacity(String src) {
        if (!isValueNull(src)) {
            String[] temp = src.split(":");
            if (temp != null && temp.length == 2 && isValidNumber(temp[0]) && !isValueNull(temp[1])) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getStringValue(String src) {
        return src;
    }

    /**
     * Parse the given string value to {@link ApplicationConstants #DATE_FORMAT}, the caller must handle the exception
     * 
     * @param date
     *            is the source date in string format
     * @return well formed data value in String format
     * 
     * @throws Exception
     */
    public static String formatDateValue(String date) throws Exception {
        if (date != null) {
            if (date.matches(SIMPLE_DATE_FORMAT_REG_EX)) {
                return new SimpleDateFormat("MM/dd/yyyy")
                        .format(new SimpleDateFormat(ApplicationConstants.DATE_FORMAT).parse(date)).toString();
            } else if (date.matches("^(0?[1-9]|1[012])[\\/\\-](0?[1-9]|[12][0-9]|3[01])[\\/\\-]\\d{2,4}$")) {
                return new SimpleDateFormat("MM/dd/yyyy").format(new SimpleDateFormat("MM/dd/yy").parse(date)).toString();
            } else {
                throw new Exception("Invalid Date format");
            }
        } else {
            return null;
        }
    }

    public static String[] filterDuplicates(List<String> sourceValues) {
        Set<String> sourceValueSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        sourceValueSet.addAll(sourceValues);
        return sourceValueSet.toArray(new String[0]);
    }

    public static String[] filterDuplicates(String[] sourceValues) {
        if (sourceValues != null && sourceValues.length > 0) {
            sourceValues = trimArrayValues(sourceValues);
            return filterDuplicates(Arrays.asList(sourceValues));
        }
        return sourceValues;
    }

    public static String[] trimArrayValues(String[] source) {
        List<String> finalSourceArray = new ArrayList<String>();
        for (int i = 0; i < source.length; i++) {
            if (!isValueNull(source[i])) {
                finalSourceArray.add(source[i].trim());
            }
        }
        return finalSourceArray.toArray(new String[0]);
    }

    public static List<String> getAllCriteriaSetValueIds(ProductCriteriaSets[] productCriteriaSets) {

        if (productCriteriaSets == null || productCriteriaSets.length < 0) {
            return new ArrayList<String>();
        }
        List<String> criteriaSetValueIds = new ArrayList<String>();

        for (ProductCriteriaSets criteriaSet : productCriteriaSets) {
            if (criteriaSet != null) {
                for (CriteriaSetValues criteriaSetValue : criteriaSet.getCriteriaSetValues()) {
                    if (criteriaSetValue != null && criteriaSetValue.getId() != null) {
                        criteriaSetValueIds.add(criteriaSetValue.getId());
                    }
                }
            }
        }
        return criteriaSetValueIds;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, ProductCriteriaSets> removeNullValuesFromCollection(Map<String, ProductCriteriaSets> criteriaSets) {
        Iterator<?> itr = criteriaSets.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, ProductCriteriaSets> pair = (Entry<String, ProductCriteriaSets>) itr.next();
            if (pair.getValue() == null) {
                itr.remove();
            }
        }
        return criteriaSets;
    }

    public static String getURLEncodedValue(String value) {
        try {
            if (value != null) {
                value = value.trim();
            } else {
                return value;
            }
            return URLEncoder.encode(value, ApplicationConstants.ENCODING);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return value;
    }

    public static String checkAndFixCSVValues(String value) {

        if (!isValueNull(value)) {
            String[] tempValues = value.trim().split(ApplicationConstants.CONST_STRING_COMMA_SEP);
            String finalValue = "";
            for (int i = 0; i < tempValues.length; i++) {
                if (!CommonUtilities.isValueNull(tempValues[i])) {
                    finalValue += !finalValue.isEmpty() ? "," + tempValues[i].trim() : tempValues[i].trim();
                }
            }
            return finalValue;
        }
        return value;
    }

    public static String[] removeDuplicatesFromCsv(String csvValue) {
        Set<String> values = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        String[] temp;
        if (csvValue.contains("|")) {
            csvValue = csvValue.replaceAll("\\|", ApplicationConstants.CONST_STRING_COMMA_SEP);
        }
        temp = csvValue.split(ApplicationConstants.CONST_STRING_COMMA_SEP);

        if (temp != null && temp.length > 0) {
            for (String csv : temp) {
                if (!isValueNull(csv)) {
                    values.add(csv);
                }
            }
        }
        return values.toArray(new String[0]);
    }

    public static String[] getValuesFromCsv(String csvValue) {
        List<String> values = new ArrayList<>();
        String[] temp;
        if (csvValue.contains("|")) {
            csvValue = csvValue.replaceAll("\\|", ApplicationConstants.CONST_STRING_COMMA_SEP);
        }
        temp = csvValue.split(ApplicationConstants.CONST_STRING_COMMA_SEP);

        if (temp != null && temp.length > 0) {
            for (String csv : temp) {
                if (!isValueNull(csv)) {
                    values.add(csv);
                }
            }
        }
        return values.toArray(new String[0]);
    }

    public static String appendValue(String orginal, String valueToAppend, String delimiter) {
        if (orginal == null || orginal.isEmpty()) {
            orginal = valueToAppend;
        } else {
            orginal += String.valueOf(delimiter + valueToAppend);
        }
        return orginal;
    }

    public static boolean isSimplifiedTemplate(String templateType) {
        if (isValueNull(templateType)) {
            return false;
        }
        if (templateType.trim().equalsIgnoreCase("ASIS")) {
            return true;
        } else {
            return false;
        }
    }

    protected static boolean isOptionGroup(String criteriaCode) {
        if (criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_PRODUCT_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_SHIPPING_OPTION)
                || criteriaCode.equalsIgnoreCase(ApplicationConstants.CONST_IMPRINT_OPTION)) {
            return true;
        } else {
            return false;
        }
    }

    public static String removeUpdateCharsString(String source) {
        return source.replaceAll(ApplicationConstants.PRODUCT_UPDATE_VALIDATE_CHAR, "null");
    }

    public static int countSpecifiedChar(String source, char elementToCheck) {
        int counter = 0;
        if (source == null) {
            return -1;
        }

        char[] sourceArray = source.toCharArray();
        for (char c : sourceArray) {
            if (c == elementToCheck) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * Starts with 1
     * 
     * @param source
     * @param elementToCheck
     * @param elementPos
     * @return
     */
    public static int indexOfElement(String source, char elementToCheck, int elementPos) {
        int counter = 0;
        if (source == null) {
            return -1;
        }

        char[] sourceArray = source.toCharArray();
        int i = 0;
        for (char c : sourceArray) {
            i++;
            if (c == elementToCheck) {
                counter++;
            }
            if (counter == elementPos) {
                return i - 1;
            }
        }

        return -1;
    }

    public static final String[] getOriginalCSVValues(String src) {
        if (!isValueNull(src)) {
            String[] values = null;
            try {
                CSVReader csvReader = new CSVReader(new StringReader(src));
                values = csvReader.readNext();
                csvReader.close();
                return values;
            } catch (Exception e) {
                LOGGER.error("Invalid CSV data, source " + src);
                // Trying default splitting
                return src.split(ApplicationConstants.CONST_STRING_COMMA_SEP);
            }
        }
        return null;
    }

    public static String removeSpaces(String src) {
        return src.replaceAll(" ", "");
    }

    public static String getValueFromCriteriaSet(CriteriaSetValues criteriaValue) {
        String value = null;

        if (isValueNull(String.valueOf(criteriaValue.getValue()))) {
            if (isValueNull(String.valueOf(criteriaValue.getFormatValue()))) {
                if (!isValueNull(String.valueOf(criteriaValue.getBaseLookupValue()))) {
                    value = String.valueOf(criteriaValue.getBaseLookupValue());
                }
            } else {
                value = String.valueOf(criteriaValue.getFormatValue());
            }
        } else {
            value = String.valueOf(criteriaValue.getValue());
        }

        return isValueNull(value) ? null : value;
    }

    public static String getValueFromCriteriaSet(com.asi.service.product.client.vo.CriteriaSetValues criteriaValue) {
        String value = null;

        if (isValueNull(String.valueOf(criteriaValue.getValue()))) {
            if (isValueNull(String.valueOf(criteriaValue.getFormatValue()))) {
                if (!isValueNull(String.valueOf(criteriaValue.getBaseLookupValue()))) {
                    value = String.valueOf(criteriaValue.getBaseLookupValue());
                }
            } else {
                value = String.valueOf(criteriaValue.getFormatValue());
            }
        } else {
            value = String.valueOf(criteriaValue.getValue());
        }

        return isValueNull(value) ? null : value;
    }
    public static <T> T[] cloneArray(T[] array) {
        if (array == null) {
            return null;
        }
        return Arrays.copyOf(array, array.length);
    }

    public static String convertStringListToCSV(List<String> source) {
        String finalString = "";
        if (source != null && !source.isEmpty()) {
            for (String s : source) {
                if (s != null) {
                    finalString = finalString.isEmpty() ? finalString + s : finalString + "," + s;
                }
            }
        }
        return finalString;
    }
    
    public static <T> T getElementFromSet(Set<T> source, int index) {
        Iterator<T> sourceIter = source.iterator();
        int counter = 1;
        while (sourceIter.hasNext()) {
            T t = (T) sourceIter.next();
            if (counter == index) {
                return t;
            }
        }
        return null;
    }
}
