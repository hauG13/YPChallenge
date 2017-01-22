package com.teamdawson.ypchallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that contains static method that will query either YellowPage or
 * ReflagDeal to find stores in proximity and its deals.
 *
 * @author Uen Yi Cindy Hung
 * @version 0.0.01
 * @since January 21st, 2017
 */
public class MerchantDealsLookUp {
    
    private static Logger log = LoggerFactory.getLogger("MerchantDealsLookUp");

    /**
     * Private default constructor.
     */
    private MerchantDealsLookUp() {
    }

    /**
     * Tries to get a deal of a merchant that is within 5km of current
     * location by querying YellowPage.
     *
     * @param latitude
     * @param longitude
     * @param keyword
     *
     * @return Merchant null if no deal in the 5km area.
     */
    public static String getClosestDeal(double latitude, double longitude, String keyword) {
        String result = null;
        
        int keywordID = getKeywordID(keyword);
        
        if (keywordID != -1) {
            return null;
        }
        
        String dealURL = "http://dcr.yp.ca/api/search/popular?latitude=" + latitude + "&longitude=" + longitude + "&radius=5&tags_list[]=" + keywordID;
        
        try {
            URL url = new URL(dealURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            
            int responseCode = conn.getResponseCode();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            
            String data;
            while ((data = in.readLine()) != null) {
                log.debug(data);
            }
            
            in.close();
            
        } catch (IOException ioe) {
            log.debug("getClosesDeal failed: " + ioe.getMessage());
        }
        
        return result;
    }

    /**
     * Queries RedFlag API for tag keyword IDs.
     *
     * @param keyword
     *
     * @return int the id of the keyword.
     */
    private static int getKeywordID(String keyword) {
        int result = -1;
        String keywordURL = "http://api.redflagdeals.com/api/tags/search/";
        
        return result;
    }
}
