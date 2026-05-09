package src.spellingbee;

import java.util.List;

import src.util.WordChecker;

public class PermuteThread extends Thread {
    private final List<String> result;
    private final char[] letters;
    private final char mainChar;
    private final int len;


    public PermuteThread(List<String> result, char[] letters, char mainChar, int len) {
        this.result = result;
        this.letters = letters;
        this.mainChar = mainChar;
        this.len = len;
    }

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


    @Override 
    public void run() {
        permute(result, new char[len], 0, len);
    }
}
