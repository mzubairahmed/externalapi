package com.asi.ext.api.product.transformers;

/**
 * {@link CriteriaParser} contains methods related criteria set processing.
 * 
 * @author Sharavan, Murali Ede, Rahul K
 * @version 1.5
 * 
 */
public class CriteriaParser {

    /**
     * Creates a array of Criteria lookup
     * 
     * @param srcData
     *            is the JSON data which contains criteria lookup
     * @return processed criteria lookup values
     */
    public String criteriaLookup(String srcData) {

        String tmpStr = "";
        String returnData = "";
        String finarr[] = new String[50];
        JerseyClientPost jcPost = new JerseyClientPost();
        String[] ary = srcData.split("setCodeValueId");
        int i = 0;
        String dlm;
        for (String src : ary) {
            if (i != 0) {
                dlm = src.substring(0, 5);
                src = src.substring(5);
                tmpStr = src.substring(src.indexOf("\""));
                src = src.substring(0, src.indexOf("\""));
                src = jcPost.getKeyValue(src);
                tmpStr = dlm + src + tmpStr;
                finarr[i] = tmpStr;
            } else {
                finarr[0] = src;
                returnData = src;
            }
            i++;
            if (i != 1)
                returnData += "setCodeValueId" + tmpStr;
            else
                returnData += tmpStr;
        }

        return returnData;
    }

}
