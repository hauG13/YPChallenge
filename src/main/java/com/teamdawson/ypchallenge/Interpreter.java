package com.teamdawson.ypchallenge;

import edu.stanford.nlp.simple.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class used to interpret text in order to retrieve
 * important information from it.
 * @author Danieil Skrinikov
 * @author Hau Gilles Che
 * @version 0.0.01
 * @since
 */
public class Interpreter {

    
//    public static void main(String[]args){
//        System.out.println(Interpreter.retrieveKeyword("I want Louis Vuitton shoes. #askYP"));
//    }
    
    
    /**
     * Retrieves keywords used used to search products from a text.
     * @param text String containing sentence(s) to retrieve keywords from.
     * @return A list of keywords.
     */
    public static List<String> retrieveKeyword(String text) {
        String regexNouns = "NN[P]?[S]?";
        String regexPrep = "\\bIN\\b|\\bVBD\\b|\\bPOS\\b";

        //Will hold the keywords extracted from the text.
        List<String> keywords = new ArrayList<>();

        // Create a document. No computation is done yet.
        Document doc = new Document(text);

        // Will iterate over the sentences
        // When we ask for the parse, it will load and run the parser
        for (Sentence sentence : doc.sentences()) {

            //Retrieves basic dictionnary form of words.
            List<String> words = sentence.lemmas();

            //Retrieves all the Parts Of Speech tags of the text. 
            List<String> posTags = sentence.posTags();

            String temp = "";
            String element = "";

            for (int i = 0; i < posTags.size(); i++) {
                //holds current element of the posTags list.
                element = posTags.get(i);

                if (isNounOrPreposition(element)) {
                    if (element.matches(regexNouns)) {
                        if (element.equals("PERSON") && words.get(i - 1)
                                .equalsIgnoreCase("for")) {
                            keywords.add(temp);
                            temp = "";
                        } else {
                            temp += words.get(i) + " ";
                        }
                    }

                    if (i + 1 != posTags.size() && !posTags.get(i + 1)
                            .matches(regexNouns) && !posTags.get(i + 1)
                            .matches(regexPrep)) {
                        keywords.add(temp);
                        temp = "";
                    }
                }
            }
        }
        return keywords;
    }
    
    /**
     * Checks if the inputted words are a person, or a for.
     * @param element String element to check if it is a person.
     * @param wordElement String element to check if it is a for (preposition).
     * @return true if both variables matches Person and for, false otherwise.
     */
    /*
    private static boolean isPersonAndFor(String element, String wordElement){
        return element.equals("PERSON") && wordElement.equalsIgnoreCase("for");
    }*/
    
    /**
     * Checks if the inputted word is a noun or a preposition.
     * @param element word to be analyzed.
     * @return true if the word is either a noun or a preposition,
     * false otherwise.
     */
    private static boolean isNounOrPreposition(String element){
        //regex expression matching POS codes for nouns and proper nouns
        String regexNouns = "NN[P]?[S]?";
        //regex expression matching POS codes for prepositions
        String regexPrep = "\\bIN\\b|\\bVBD\\b|\\bPOS\\b";
        
        if(element.matches(regexNouns) || element.matches(regexPrep)){
            return true;
        }
        
        return false;
    }

}
