package src.util;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This class checks any provided words to see if they are valid English words.
 * 
 * The "full" list English words are read from src/util/words.txt.
 */
public class WordChecker {
    /** A set of valid English words (may or may not be included by NYT) */
    private static final Set<String> WORDS = new HashSet<>();
    /** A set of valid Wordle words */
    private static final Set<String> WORDLE = new HashSet<>();

    // Load the list of English words from src/util/words.txt
    // and the wordle words from src/util/wordle.txt
    static {
        File wordFile = new File("src/util/words.txt");
        File wordleFile = new File("src/util/wordle.txt");

        Scanner scanner;
        try {
            scanner = new Scanner(wordFile);
            while ((scanner.hasNextLine())) {
                // Set to lower case for case insensitivity later
                WORDS.add(scanner.nextLine().toLowerCase());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            scanner = new Scanner(wordleFile);
            scanner.nextLine(); // skip credits
            while ((scanner.hasNextLine())) {
                // Set to lower case for case insensitivity later (just in case)
                WORDLE.add(scanner.nextLine().toLowerCase());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a word is a valid English word or not. 
     * Case insensitive
     * 
     * @param word the word to check
     * @return whether the word is a valid english word
     */
    public static boolean checkWord(String word) {
        return WORDS.contains(word.toLowerCase());
    }

    /**
     * Checks if a word is a valid English word or not. 
     * Case insensitive
     * 
     * @param word the word to check
     * @return whether the word is a valid english word
     */
    public static boolean checkWordle(String word) {
        return WORDLE.contains(word.toLowerCase());
    }
}
