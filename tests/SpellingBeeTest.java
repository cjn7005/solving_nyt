package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

import src.spellingbee.SpellingBee;

/**
 * These are characterization tests (since manually checking for correctness seems infeasable)
 */
public class SpellingBeeTest {
    /** 
     * A look up table for the known number of words that are
     * valid for the Spelling Bee from 05/08/2026.
     * The first index is the minimum word length and
     * the second index is the maximum word length
     */
    private static final int[][] knownWordSizes = new int[11+1][11+1];
    private static final char[] bee_05_08_2026 = {'c','g','i','l','o','a','y'};

    @BeforeClass
    public static void listKnownValues() {
        knownWordSizes[4][8] = 205;
        knownWordSizes[4][9] = 218;
        knownWordSizes[4][10] = 222;
        knownWordSizes[4][11] = 230;
    }

    @Test
    public void test05_08_2026() {
        Assumptions.assumeTrue(
            knownWordSizes[SpellingBee.MIN_WORD_LENGTH][SpellingBee.MAX_WORD_LENGTH] != 0,
            "Unknown expected value for min="+
            SpellingBee.MIN_WORD_LENGTH+", max="+SpellingBee.MAX_WORD_LENGTH+
            ". Either change the values to ones that have been previously tested or update listKnownValues."
        );

        SpellingBee bee = new SpellingBee(bee_05_08_2026);
        int expectedSize = knownWordSizes[SpellingBee.MIN_WORD_LENGTH][SpellingBee.MAX_WORD_LENGTH];
        int resultSize = -1;
        try {
            List<String> results = bee.findAll();
            resultSize = results.size();
        } catch (InterruptedException e) {
            assertTrue("Encountered InterruptedException",false);
        }
        assertEquals("Incorrect number of words found", expectedSize, resultSize);
    }
}