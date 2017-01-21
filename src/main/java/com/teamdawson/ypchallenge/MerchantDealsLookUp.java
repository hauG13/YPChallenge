package com.teamdawson.ypchallenge;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Uen Yi Cindy Hung
 * @version 0.0.01
 * @since January 21st, 2017
 */
public class MerchantDealsLookUp {
    Logger log = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * Private default constructor.
     */
    private MerchantDealsLookUp() {
    }

    public static Merchant getClosestDeal(double latitude, double longitude, String keyword) {
        List<String> result = null;
        String dealURL = "http://dcr.yp.ca/api/search/popular?latitude=?&longitude=?&radius=5&tags_list[]=?";
        
        int keywordID = getKeywordID(keyword);
        

        return result;
    }
    
    private static int getKeywordID(String keyword){
        int result = -1;
        String keywordURL = "http://api.redflagdeals.com/api/tags/search/";
        
        
        
        return result;
    }
}
