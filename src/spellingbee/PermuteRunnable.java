package src.spellingbee;

import java.util.List;

import src.util.WordChecker;

/**
 * This class searches permutations after having been called by {@link src.spellingbee.PermuteThread}
 */
public class PermuteRunnable implements Runnable {
    /** The collection of verified strings */
    private final List<String> result;
    /** The array of allowed letters */
    private final char[] letters;
    /** The center character that must be included in the word */
    private final char mainChar;
    /** The length of words to search */
    private final int len;
    /** The current character array that is being permuted */
    private final char[] current;

    /**
     * Creates a new PermuteRunnable object
     * 
     * @param result the collection of verified strings to be appended to
     * @param current the current character array
     * @param letters the array of allowed letters
     * @param mainChar the center character
     * @param len the length of searched words
     */
    public PermuteRunnable(List<String> result, char[] current, char mainChar, char[] letters, int len) {
        this.result = result;
        this.letters = letters;
        this.len = len;
        this.mainChar = mainChar;
        this.current = current;
    }

    /**
     * Searches permutations of {@code letters] of length {@code len} for valid words
     * 
     * @param result the collection of verified strings to be appended to
     * @param current the current character array
     * @param i the current index within {@code current}
     * @param len the length of searched words
     */
    private void permute(List<String> result, char[] current, int i, int len) {
        if (i >= len) {
            String word = String.valueOf(current.clone());
            if (WordChecker.checkWord(word)) {
                for (int j = 0; j < current.length; j++) {
                    if (current[j] == mainChar) {
                        synchronized (result) {
                            result.add(word);
                        }
                        return;
                    }
                }
            }
            return;
        }
        for (char c : letters) {
            current[i] = c;
            permute(result, current, i+1, len);
        }
    }

    /** Initializes {@link src.spellingbee.PermuteRunnable#permute} 
     * @see Runnable#run
    */
    @Override
    public void run() {
        permute(result, current, 1, len);
    }
    
}
