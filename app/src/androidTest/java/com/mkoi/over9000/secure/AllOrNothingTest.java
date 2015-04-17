package com.mkoi.over9000.secure;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;

public class AllOrNothingTest extends TestCase {

    public void testTransformMessage() throws Exception {
        String message = "Totally random test string";
        ArrayList<String> result = AllOrNothing.transformMessage(message);
        String decodedMessage = AllOrNothing.revertTransformation(result);
        Assert.assertEquals(message,decodedMessage);
    }
}