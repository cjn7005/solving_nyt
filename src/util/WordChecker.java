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

    // Load the list of English words from src/util/words.txt
    static {
        File file = new File("src/util/words.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while ((scanner.hasNextLine())) {
                // Set to lower case for case insensitivity later
                WORDS.add(scanner.nextLine().toLowerCase());
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
}
