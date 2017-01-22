package com.teamdawson.ypchallenge;

import edu.stanford.nlp.simple.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Danieil Skrinikov
 * @version 0.0.01
 * @since
 */
public class Interpreter {

    public static void main(String[] args) {

    }

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

            for (int i = 0; i < posTags.size(); i++) {
                if (posTags.get(i).matches(regexNouns) || posTags.get(i).matches(regexPrep)) {
                    if (posTags.get(i).matches(regexNouns)) {
                        if (posTags.get(i).equals("PERSON") && words.get(i - 1)
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

                //return keywords;
            }

        }
        return keywords;
    }

}
