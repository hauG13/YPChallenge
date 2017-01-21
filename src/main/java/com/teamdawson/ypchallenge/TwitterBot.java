package com.teamdawson.ypchallenge;

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

        try {
            Query query = new Query("\"#haugilles\"");

            QueryResult result = twitter.search(query);

            for (Status tweet : result.getTweets()) {
                if (tweet.getId() != latestID) {
                    twitter.updateStatus("@" + tweet.getUser().getScreenName() + " test.");
                }
                log.info(tweet.getId() + "");
            }

            //Status status = twitter.updateStatus("Hello Hau Gilles");
        } catch (TwitterException ex) {
            log.error("Connection failed.");
        }
    }
}
