package com.teamdawson.ypchallenge;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import javax.json.Json;
import javax.json.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

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
     * Tries to get a deal of a merchant that is within 5km of current location
     * by querying YellowPage.
     *
     * @param latitude
     * @param longitude
     * @param keyword
     *
     * @return Merchant null if no deal in the 5km area.
     */
    public static Merchant getClosestDeal(double latitude, double longitude, String keyword) {
        Merchant result = null;
        
        int keywordID = getKeywordID(keyword);
        
        log.debug("keyword id is: " + keywordID);
        
        if (keywordID == -1) {
            return null;
        }
        
        String dealURL = "http://dcr.yp.ca/api/search/popular?latitude=" + latitude + "&longitude=" + longitude + "&no_nationals=true&radius=5&tags_list[]=" + keywordID;
        
        try {
            URL url = new URL(dealURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            
            int responseCode = conn.getResponseCode();
            
            JsonReader reader = Json.createReader(new InputStreamReader(conn.getInputStream()));
            JSONObject obj = new JSONObject(reader.readObject().toString());
            
            JSONObject data = obj.getJSONArray("data").getJSONObject(0).getJSONObject("result");
            JSONObject english = data.getJSONObject("Translation").getJSONObject("en");
            
            result = new Merchant();
            result.setDeal_text(english.getString("short_title"));
            result.setDeal_link(english.getString("url"));
            
            String name = data.getJSONArray("Merchant").getJSONObject(0).getJSONObject("Translation").getJSONObject("en").getString("name");
            result.setStore(name);
            
            reader.close();
            
        } catch (IOException ioe) {
            log.debug("getClosesDeal failed: " + ioe.getMessage());
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(MerchantDealsLookUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

    /**
     * Queries RedFlag API to extract the tag keyword IDs from the JSON
     * response.
     *
     * @param keyword
     *
     * @return int the id of the keyword.
     */
    private static int getKeywordID(String keyword) {
        int result = -1;
        String keywordURL = "http://api.redflagdeals.com/api/tags/search/" + keyword;
        
        try {
            URL url = new URL(keywordURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            
            int responseCode = conn.getResponseCode();
            
            JsonReader reader = Json.createReader(new InputStreamReader(conn.getInputStream()));
            JSONObject obj = new JSONObject(reader.readObject().toString());
            
            JSONArray array = obj.getJSONArray("data");
            
            for (int i = 0; i < array.length(); i++) {
                log.debug(array.getString(i) + "\n");
                JSONObject sub = new JSONObject(array.getString(i)).getJSONObject("Translation").getJSONObject("en");
                if (sub.get("slug").toString().equalsIgnoreCase(keyword)) {
                    result = Integer.parseInt(sub.getString("id"));
                }
            }
            
            reader.close();
            
        } catch (IOException ioe) {
            log.debug("getClosesDeal failed: " + ioe.getMessage());
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(MerchantDealsLookUp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
}
