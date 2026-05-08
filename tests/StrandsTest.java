package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assumptions;

import src.strands.Strands;
import src.strands.StrandsResults;

public class StrandsTest {
    /** 
     * A look up table for the known number of words that are
     * present in the grid from 05/08/2026.
     * The first index is the minimum word length and
     * the second index is the maximum word length
     */
    private static final int[][] knownWordSizes = new int[10+1][10+1];
    private static final String strand_05_08_2026 = "cruniaisbocsseaftotdephrriameproniomdinlcmyralno";

    @BeforeClass
    public static void listKnownValues() {
        knownWordSizes[4][10] = 1927;
    }

    @Test
    public void test05_08_2026() {
        Assumptions.assumeTrue(
            knownWordSizes[Strands.MIN_WORD_LENGTH][Strands.MAX_WORD_LENGTH] != 0,
            "Unknown expected value for min="+
            Strands.MIN_WORD_LENGTH+", max="+Strands.MAX_WORD_LENGTH+
            ". Either change the values to ones that have been previously tested or update listKnownValues."
        );

        Strands strands = new Strands();
        String input = strand_05_08_2026;
        strands.buildStrands(input.toCharArray());
        int expectedSize = knownWordSizes[Strands.MIN_WORD_LENGTH][Strands.MAX_WORD_LENGTH];
        int resultSize = -1;
        try {
            StrandsResults results = strands.findAll();
            resultSize = results.getWords().size();
        } catch (InterruptedException e) {
            assertTrue("Encountered InterruptedException",false);
        }
        assertEquals("Incorrect number of words found", expectedSize, resultSize);
    }

    
}
