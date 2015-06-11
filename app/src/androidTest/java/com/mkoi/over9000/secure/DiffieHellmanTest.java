package com.mkoi.over9000.secure;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;

public class DiffieHellmanTest extends TestCase {

    public static final int NUMBER_OF_TRANSACTIONS = 2;

    public void testDiffieHellman() throws InvalidParameterSpecException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException {
        DiffieHellman alice = new DiffieHellman();
        DiffieHellman bob = new DiffieHellman();

        for(int i=0; i<NUMBER_OF_TRANSACTIONS; i++) {
            alice.generateParameters();
            PublicKey alicePublicKey = alice.generateKeypair();
            PublicKey bobPublicKey = bob.generateKeypair(alicePublicKey.getEncoded());
            alice.finishKeyAgreement(bobPublicKey.getEncoded());
            Assert.assertTrue(Arrays.equals(alice.getSecret(), bob.getSecret()));
        }
    }

}