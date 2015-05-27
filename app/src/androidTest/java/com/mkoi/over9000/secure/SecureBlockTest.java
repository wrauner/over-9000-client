package com.mkoi.over9000.secure;

import com.mkoi.over9000.message.SecuredMessage;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;

public class SecureBlockTest extends TestCase {

    public void testCreateBlocksToSend() throws Exception {
        SecureBlock secureBlock = new SecureBlock();
        byte[] secret = "elo".getBytes();
        String message = "testy";
        ArrayList<String> transformedMessage = AllOrNothing.transformMessage(message);
        ArrayList<SecuredMessage> result = secureBlock.createBlocksToSend(transformedMessage, secret);
        ArrayList<String> received = secureBlock.prepareReceivedBlocks(result, secret);
        Assert.assertTrue(transformedMessage.size() == received.size());
    }
}