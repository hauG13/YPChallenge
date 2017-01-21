package com.teamdawson.ypchallenge;

import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Each object of this class represents a twitter bot.
 * This bot will run every 5 minutes and look for all tweets with "#askyp" then 
 * reply to them with the nearest place which matches their need.
 * 
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 2017-01-21
 */
public class TwitterBot {
    
    /**
     * Default no parameter constructor for the Bot
     */
    public TwitterBot(){
        
    }
    
    /**
     * Runs the bot.
     */
    public void run(){
        
        //Handle to twitter API
        Twitter twitter = TwitterFactory.getSingleton();
        
        try {
            
            Status status = twitter.updateStatus("Hello Hau Gilles");
            
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
