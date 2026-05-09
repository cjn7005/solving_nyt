package src.spellingbee;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * This class recursively searches permutations of the provided letters 
 * for valid English words containing the center letter
 */
public class SpellingBee {
    /** The minimum word length. For Spelling Bee this is 4 */
    public static final int MIN_WORD_LENGTH = 4;
    /** The maximum word length. Note that this has a significant impact
     * on performance with diminishing returns */
    public static final int MAX_WORD_LENGTH = 8;

    /** The array of allowed letters */
    private final char[] letters;
    /** The center character that must be included in the word */
    private final char mainChar;

    /**
     * Returns the available letters
     * 
     * @return the available letters
     */
    public char[] getLetters() {
        return letters;
    }

    /**
     * Returns the center letter
     * 
     * @return the center letter
     */
    public char getMainChar() {
        return mainChar;
    }

    /**
     * Create a new SpellingBee Object
     * 
     * @param letters the array of allowed letters
     * @param mainChar the center letter
     */
    public SpellingBee(char[] letters, char mainChar) {
        this.letters = letters;
        this.mainChar = mainChar;
    }

    /**
     * Create a new SpellingBee Object
     * 
     * The first element of {@code letters} will be interpreted as the center character
     * 
     * @param letters the array of allowed letters
     */
    public SpellingBee(char[] letters) {
        this(letters, letters[0]);
    }

    /**
     * Recursively finds all valid permutations of the allowed letters
     * 
     * @return list of found words
     * @throws InterruptedException
     */
    public List<String> findAll() throws InterruptedException {
        List<String> result = new LinkedList<>();

        Thread[] threads = new Thread[MAX_WORD_LENGTH-MIN_WORD_LENGTH+1];
        for (int i = MIN_WORD_LENGTH; i <= MAX_WORD_LENGTH; i++) {
            threads[i-MIN_WORD_LENGTH] = new PermuteThread(result, letters, mainChar, i);
            threads[i-MIN_WORD_LENGTH].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        return result;
    }
    
    /**
     * Returns whether the provided word is a pangram of {@code letters}
     * 
     * @param str the word to check
     * @return whether the provided word is a pangram of {@code letters}
     */
    public boolean checkPangram(String str) {
        boolean[] checkPangram = new boolean[letters.length];
        for (char c : str.toCharArray()) {
            for (int i = 0; i < letters.length; i++) {
                if (!checkPangram[i] && c == letters[i]) {
                    checkPangram[i] = true;
                }
            }
        }
        boolean isPangram = true;
        for (int i = 0; i < checkPangram.length; i++) {
            if (!checkPangram[i]) {
                isPangram = false;
                break;
            }
        }
        return isPangram;
    }

    /**
     * Prompts the user for Spelling Bee letters and finds all the valid words.
     * 
     * Annotates pangrams with "PANGRAM: ..."
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print(
            "Enter the list of letters with the center as the first (no spaces) :"
        );
        char[] chars = scanner.next().toCharArray();
        scanner.close();

        SpellingBee bee = new SpellingBee(chars);
        List<String> words = bee.findAll();
        for (String str : words) {
            if (bee.checkPangram(str)) {
                System.out.print("PANGRAM: ");
            }
            System.out.println(str);
        }
        System.out.println("Found " + words.size() + " words.");
    }
}
