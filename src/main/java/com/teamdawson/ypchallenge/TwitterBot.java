package com.teamdawson.ypchallenge;

import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Each object of this class represents a twitter bot. This bot will run every 5
 * minutes and look for all tweets with "#askyp" then reply to them with the
 * nearest place which matches their need.
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 2017-01-21
 */
public class TwitterBot {

    //Will search for following hashtag
    private final static String SEARCH = "#haugilles";

    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private long latestID;

    /**
     * Default no parameter constructor for the Bot
     */
    public TwitterBot() {
        latestID = -1;
    }

    /**
     * Runs the bot.
     */
    public void run() {

        //Handle to twitter API
        Twitter twitter = TwitterFactory.getSingleton();

        //Infinite loop. Meant to run as long as this bot exists.
        for (;;) {

            try {

                try {
                    log.debug("Start querying results");

                    Query query = new Query("\"" + SEARCH + "\"");
                    QueryResult result = twitter.search(query);

                    for (Status tweet : result.getTweets()) {
                        //log.info("Processing result for: " + tweet.getUser().getName());
                        
                        if (tweet.getId() > latestID) {
                            log.info("Processing result for: " + tweet.getUser().getName());
                            Merchant merchant = null;
                            
                            if(tweet.getGeoLocation() != null){
                                log.info("Entered GEO");
                                
                                List<String> keywords = Interpreter.retrieveKeyword(tweet.getText());
                                MerchantSearcher searcher = new MerchantSearcher();
                                merchant = searcher.search(tweet.getGeoLocation().getLatitude(), tweet.getGeoLocation().getLongitude(), keywords.get(0));
                                
                                StatusUpdate su = new StatusUpdate("Hey @"+tweet.getUser().getScreenName()+" You might want to check out\n"+merchant.getStore()+" for: "+keywords.get(0)+merchant.getDeal_link());
                                Status status = twitter.updateStatus(su);
                                log.debug(status.getText());
                            }
                            else if(tweet.getPlace() != null){
                                //No Geolocation. Yes Place
                                
                                Place p = tweet.getPlace();
                                if(p == null)
                                    log.error("Place is null");
                                else{
                                    p.getName();
                                }
                            }
                            else{
                                //No location. Must retweet.
                                StatusUpdate su = new StatusUpdate("@"+tweet.getUser().getScreenName()+" We could not find your location. Please reply us with it.");
                                su.inReplyToStatusId(tweet.getId());
                                twitter.updateStatus(su);
                            }
                            
                            latestID = tweet.getId();
                            log.info("Tweeted at: " + tweet.getId() + " " + tweet.getUser().getScreenName());
                            
                            
                        }
                    }

                    log.info("Finished processing results");
                    
                } catch (TwitterException ex) {
                    //May be caused by a repeated tweet.
                    log.error("Something went wrong.");
                }
                
                log.info("Thread will sleep for 1 minutes.");
                Thread.sleep(1000 * 60 * 1);

            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(TwitterBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
