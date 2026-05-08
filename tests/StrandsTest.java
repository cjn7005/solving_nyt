package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import src.strands.Strands;

public class StrandsTest {
    @Test
    public void isWordOctarius() {
        String input = "octarius";
        boolean expected = true;
        boolean result = Strands.checkWord(input);
        assertEquals(expected, result);
    }

    @Test
    public void isWordOctogenarian() {
        String input = "octogenarian";
        boolean expected = true;
        boolean result = Strands.checkWord(input);
        assertEquals(expected, result);
    }

    @Test
    public void isWordOctagenarian() {
        String input = "octagenarian";
        boolean expected = false;
        boolean result = Strands.checkWord(input);
        assertEquals(expected, result);
    }
}
