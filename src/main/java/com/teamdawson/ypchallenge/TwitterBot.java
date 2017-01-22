package com.teamdawson.ypchallenge;

import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
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
                        log.info("Processing result for: " + tweet.getUser().getName());
                        
                        if (tweet.getId() > latestID) {
                                log.debug(tweet.getGeoLocation().toString());
                            Merchant merchant = MerchantDealsLookUp.getClosestDeal(tweet.getGeoLocation().getLatitude(), tweet.getGeoLocation().getLongitude(), "book");
                            twitter.updateStatus("@" + tweet.getUser().getScreenName() + " " + merchant.getStore() + " " + merchant.getDeal_text() + " " + merchant.getDeal_link());
                            latestID = tweet.getId();
                            
                            log.info("Tweeted at: " + tweet.getId() + " " + tweet.getUser().getScreenName());
                        }
                    }

                    log.info("Finished processing results");
                    
                } catch (TwitterException ex) {
                    //May be caused by a repeated tweet.
                    log.error("Something went wrong.");
                }
                
                log.info("Thread will sleep for 5 minutes.");
                Thread.sleep(1000 * 60 * 1);

            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(TwitterBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
