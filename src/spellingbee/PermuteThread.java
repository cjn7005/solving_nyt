package src.spellingbee;

import java.util.List;

/**
 * This class runs collects valid words of length {@code len}
 */
public class PermuteThread extends Thread {
    /** The collection of verified strings */
    private final List<String> result;
    /** The array of allowed letters */
    private final char[] letters;
    /** The center character that must be included in the word */
    private final char mainChar;
    /** The length of words to search */
    private final int len;

    /**
     * Creates a new PermuteThread
     * 
     * @param result the collection of verified strings to be appended to
     * @param letters the array of allowed letters
     * @param mainChar the center character
     * @param len the length of searched words
     */
    public PermuteThread(List<String> result, char[] letters, char mainChar, int len) {
        this.result = result;
        this.letters = letters;
        this.mainChar = mainChar;
        this.len = len;
    }

    /**
     * Creates a new Thread for each letter each running {@link src.spellingbee.PermuteRunnable}
     */
    @Override 
    public void run() {
        Thread[] threads = new Thread[letters.length];
        for (int i = 0; i < letters.length; i++) {
            char[] current = new char[len];
            current[0] = letters[i];
            Runnable runner = new PermuteRunnable(result, current, mainChar, letters, len);
            threads[i] = new Thread(runner);
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {}
        }
    }
}
