package com.mkoi.over9000.secure;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;

public class AllOrNothingTest extends TestCase {
    /**
     * Pokazuje ile i jak długie ciągi będziemy testować
     */
    public static final int NUMBER_OF_CASES = 10;

    public void testTransformMessage() throws Exception {
        String testString = "Totally random test string 123@";
        StringBuilder sb = new StringBuilder(testString);
        ArrayList<String> stringsToTest = new ArrayList<>();
        for(int i=0; i<=NUMBER_OF_CASES; i++) {
            sb.append(testString);
            stringsToTest.add(sb.toString());
        }
        for(String stringToTest : stringsToTest) {
            ArrayList<String> result = AllOrNothing.transformMessage(stringToTest);
            String decodedMessage = AllOrNothing.revertTransformation(result);
            Assert.assertEquals(stringToTest,decodedMessage);
        }
    }
}