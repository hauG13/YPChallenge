package com.teamdawson.ypchallenge;

/**
 * This class starts the twitter bot "server"
 * 
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since 2017-01-21
 */
public class TwitterBotApp {
    
    /**
     * Main class for the application.
     * 
     * @param args Should not be used. 
     */
    public static void main(String[] args){
        TwitterBot bot = new TwitterBot();
        bot.run();
    }
}
