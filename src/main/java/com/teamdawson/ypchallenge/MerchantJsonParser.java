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
     * @param json
     * @return 
     */
    public static Merchant parseNearestStoreJson(String json){
        Merchant merchant = null;
        
        JsonParser parser = Json.createParser(new StringReader(json));
        boolean found = false;
        while(parser.hasNext() && found == false)
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
                    
                    
            }
        }
        return merchant;
    }
    
    
}
