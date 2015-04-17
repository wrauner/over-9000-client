package com.mkoi.over9000.secure;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;

public class AllOrNothingTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testTransformMessage() throws Exception {
        String message = "Siemka, Wojtek. Nie idę na TIKO TIKO portoryko.";
        ArrayList<String> result = AllOrNothing.transformMessage(message);
        String decodedMessage = AllOrNothing.RevertTransformation(result);
        Assert.assertEquals(message,decodedMessage);
    }
}