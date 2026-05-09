package src.spellingbee;

import java.util.LinkedList;
import java.util.List;

import src.util.WordChecker;

public class SpellingBee {
    public static final int MIN_WORD_LENGTH = 4;
    public static final int MAX_WORD_LENGTH = 8;

    public final char[] letters;
    public final char mainChar;

    public SpellingBee(char[] letters, char mainChar) {
        this.letters = letters;
        this.mainChar = mainChar;
    }

    public List<String> findAll() {
        int nPermutes = (int) Math.pow(letters.length,MAX_WORD_LENGTH);
        assert nPermutes >= 0 && nPermutes <= 282475249 : "let's not"; // 7^10=that
        List<String> result = new LinkedList<>();

        for (int i = MIN_WORD_LENGTH; i <= MAX_WORD_LENGTH; i++) {
            permute(result, new char[i], 0, i);
        }

        return result;
    }

    private void permute(List<String> result, char[] current, int i, int len) {
        if (i >= len) {
            String word = String.valueOf(current.clone());
            if (WordChecker.checkWord(word)) {
                for (int j = 0; j < current.length; j++) {
                    if (current[j] == mainChar) {
                        result.add(word);
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

    public static void main(String[] args) {
        char[] chars = {'c','g','i','l','o','a','y'};
        char mainChar = 'c';

        SpellingBee bee = new SpellingBee(chars, mainChar);
        List<String> words = bee.findAll();
        for (String str : words) {
            System.out.println(str);
        }
        System.out.println("Found " + words.size() + " words.");
    }
}
