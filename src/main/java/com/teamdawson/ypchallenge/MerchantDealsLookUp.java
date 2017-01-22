package com.teamdawson.ypchallenge;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
            JSONArray dataArray = getJSONData(dealURL, "GET");

            if (dataArray.length() < 1) {
                log.debug("No result.");
                return null;
            }

            JSONObject data = dataArray.getJSONObject(0).getJSONObject("result");
            JSONObject english = data.getJSONObject("Translation").getJSONObject("en");

            result = new Merchant();
            result.setDeal_text(english.getString("short_title"));
            result.setDeal_link(english.getString("url"));

            String name = data.getJSONArray("Merchants").getJSONObject(0).getJSONObject("Translation").getJSONObject("en").getString("name");
            result.setStore(name);

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
            JSONArray dataArray = getJSONData(keywordURL, "GET");

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject sub = new JSONObject(dataArray.getString(i)).getJSONObject("Translation").getJSONObject("en");

                String regex = keyword + ".?";
                if (sub.get("slug").toString().matches(regex)) {
                    result = Integer.parseInt(sub.getString("id"));
                }
            }

        } catch (IOException ioe) {
            log.debug("getClosesDeal failed: " + ioe.getMessage());
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(MerchantDealsLookUp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private static JSONArray getJSONData(String urlString, String method) throws JSONException, IOException {
        JSONArray result = null;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");

        int responseCode = conn.getResponseCode();

        if (responseCode == 200) {
            JsonReader reader = Json.createReader(new InputStreamReader(conn.getInputStream()));
            JSONObject obj = new JSONObject(reader.readObject().toString());

            result = obj.getJSONArray("data");
            reader.close();
        }

        return result;
    }

    private static HttpURLConnection preparePostRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        return conn;
    }

    private static String preparePostBody(double latitude, double longitude, String keyword) {
        String requestString = "{ \"search\":[{ "
                + "\"searchType\":\"PROXIMITY\", "
                + "\"collection\":\"MERCHANT\", "
                + "\"what\": \"" + keyword + "\", "
                + "\"where\":{ "
                + "\"type\":\"GEO\", "
                + "\"value\":\"" + latitude + "," + longitude + "\" } "
                + "}]}";

        return requestString;
    }

    /**
     *
     * Get the closest Merchant that has no deals on the key item
     *
     * @param latitude long
     * @param longitude long
     * @param keyword String
     * @return Merchant object
     */
    public static Merchant getClosestStore(double latitude, double longitude, String keyword) {
        Merchant result = null;
        String urlPost = "http://hackaton.ypcloud.io/search";

        try {
            HttpURLConnection conn = preparePostRequest(urlPost);

            String requestString = preparePostBody(latitude, longitude, keyword);

            try (OutputStream out = conn.getOutputStream()) {
                out.write(requestString.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (JsonReader reader = Json.createReader(new InputStreamReader(conn.getInputStream()))) {
                    JSONObject obj = new JSONObject(reader.readObject().toString());
                    log.debug(obj.toString());

                    JSONObject data = obj.getJSONArray("searchResult")
                            .getJSONObject(0).getJSONArray("merchants").getJSONObject(0);

                    result = new Merchant();
                    result.setStore(data.getString("businessName"));
                }
            }
            log.debug("result merchant " + result.getStore());

        } catch (IOException ioe) {
            log.debug("getClosesStore failed: " + ioe.getMessage());
        } catch (JSONException ex) {
            java.util.logging.Logger.getLogger(MerchantDealsLookUp.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

}
