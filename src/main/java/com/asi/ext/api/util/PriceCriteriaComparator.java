/**
 * 
 */
package com.asi.ext.api.util;

import java.util.Comparator;

import com.asi.ext.api.service.model.PriceConfiguration;

/**
 * @author krahul
 * 
 */
public class PriceCriteriaComparator implements Comparator<PriceConfiguration> {

    @Override
    public int compare(PriceConfiguration o1, PriceConfiguration o2) {
        // TODO Auto-generated method stub
        return o1.getCriteria().compareTo(o2.getCriteria());
    }

}
