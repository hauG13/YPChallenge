package com.teamdawson.ypchallenge;


import java.io.StringReader;
import javax.json.Json;
import javax.json.stream.JsonParser;

/**
 *
 * @author Thai-vu Nguyen
 * @version 0.0.01
 * @since 2017-01-21
 */
public class MerchantJsonParser {
    private MerchantJsonParser(){
        
    }
    
    /**
     * 
     * Parse through a json to create a merchant object
     * 
     * @param json String
     * @return a store with a deal
     */
    public static Merchant parseNearestDealJson(String json){
        Merchant merchant = null;
        
        JsonParser parser = Json.createParser(new StringReader(json));
        boolean found = false;
        while(parser.hasNext())
        {
            JsonParser.Event event = parser.next();
            
            switch (event){
                case KEY_NAME:
                    if("short_title".equals(parser.getString())){
                        parser.next();
                        if (parser.getString() != null)
                        {
                            merchant = new Merchant();
                            merchant.setDeal_text(parser.getString());
                        }
                    }else if("url".equals(parser.getString())){
                        parser.next();
                        if (merchant != null)
                        {
                            merchant.setDeal_link(parser.getString());
                        }
                        
                    }else if("name".equals(parser.getString())){
                        parser.next();
                        if(merchant != null){
                            merchant.setDeal_link(parser.getString());
                        }
                        found = true;
                    }
                    break;
            }
            
            if (found == true)
                break;
        }
        return merchant;
    }
    
    /**
     * 
     * @param json String
     * @return a Store with no deals
     */
    public static Merchant parseNearestStoreJson(String json){
        Merchant merchant = null;
        
        JsonParser parser = Json.createParser(new StringReader(json));
        boolean found = false;
        while(parser.hasNext()){
            JsonParser.Event event = parser.next();
            
            switch (event){
                case KEY_NAME:
                    if ("businessName".equals(parser.getString()))
                    {
                        parser.next();
                        if (parser.getString() != null)
                        {
                            merchant = new Merchant();
                            merchant.setStore(parser.getString());
                        }
                    }
            }
            if (found == true)
                break;
        }
        return merchant;
    }
    
    
}
