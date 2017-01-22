package com.teamdawson.ypchallenge;

/**
 *
 * @author Thai-vu Nguyen
 * @version 0.0.01
 * @since 2017-01-21
 */
public class MerchantSearcher {
    
    /**
     * Default constructor
     */
    public MerchantSearcher(){
        
    }
    
    /**
     * Gets a merchant based on the geo-location
     * 
     * @param latitude String
     * @param longitude String
     * @param keyword String
     * @return Merchant
     */
    public Merchant search(long latitude, long longitude, String keyword) 
                                throws IllegalArgumentException{
        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException();
        
        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException();
        
        if (keyword == null || keyword.isEmpty())
            throw new IllegalArgumentException();
        
        keyword = keyword.trim();
        
        Merchant merchant = null;
        
        //merchant = MerchantDealsLookUp.getCloserDeal(latitude, longitude, keyword);
        
        if (merchant == null){
            //TODO, wait for Cindy for nearest no deal Merchant
        }
        
        return merchant;
    }
    
    
}
