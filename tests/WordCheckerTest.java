package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import src.util.WordChecker;

public class WordCheckerTest {
    @Test
    public void isWordOctarius() {
        String input = "octarius";
        boolean expected = true;
        boolean result = WordChecker.checkWord(input);
        assertEquals(expected, result);
    }

    @Test
    public void isWordOctogenarian() {
        String input = "octogenarian";
        boolean expected = true;
        boolean result = WordChecker.checkWord(input);
        assertEquals(expected, result);
    }

    @Test
    public void isWordOctagenarian() {
        String input = "octagenarian";
        boolean expected = false;
        boolean result = WordChecker.checkWord(input);
        assertEquals(expected, result);
    }
}
