/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamdawson.ypchallenge;

/**
 *
 * Merchant bean holding info of store name, deal and the link to the deal
 * 
 * @author Thai-vu Nguyen
 * @version 0.0.01
 * @since 2017-01-21
 */
public class Merchant {
    private String store;
    private String deal_text;
    private String deal_link;

    
    /**
     * Default Constructor
     */
    public Merchant(){
        this("","","");
    }
    
    /**
     * Constructor
     * @param store
     * @param deal_text
     * @param deal_link 
     */
    public Merchant (String store, String deal_text, String deal_link){
        this.store = store;
        this.deal_text = deal_text;
        this.deal_link = deal_link;
    }

    /**
     * @return Store name 
     */
    public String getStore() {
        return store;
    }

    /**
     * Set the store name
     * @param store String
     */
    public void setStore(String store) {
        this.store = store;
    }

    /**
     * @return the deal
     */
    public String getDeal_text() {
        return deal_text;
    }

    /**
     * Set the deal
     * @param deal_text String
     */
    public void setDeal_text(String deal_text) {
        this.deal_text = deal_text;
    }

    /**
     * @return the web link of the deal
     */
    public String getDeal_link() {
        return deal_link;
    }

    /**
     * Set the deal link
     * @param deal_link String
     */
    public void setDeal_link(String deal_link) {
        this.deal_link = deal_link;
    }
    
    
    
    
    
}
