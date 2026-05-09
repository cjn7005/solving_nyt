package src.spellingbee;

import java.util.LinkedList;
import java.util.List;

public class SpellingBee {
    public static final int MIN_WORD_LENGTH = 4;
    public static final int MAX_WORD_LENGTH = 8;

    public final char[] letters;
    public final char mainChar;

    public SpellingBee(char[] letters, char mainChar) {
        this.letters = letters;
        this.mainChar = mainChar;
    }

    public SpellingBee(char[] letters) {
        this(letters, letters[0]);
    }

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

    public static void main(String[] args) throws InterruptedException {
        char[] chars = {'c','g','i','l','o','a','y'};

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
