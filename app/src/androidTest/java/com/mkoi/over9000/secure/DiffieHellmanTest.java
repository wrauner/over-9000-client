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

    public void testDiffieHellman() {
        DiffieHellman alice = new DiffieHellman();
        DiffieHellman bob = new DiffieHellman();

        try {
            alice.generateParameters();
            PublicKey alicePublicKey = alice.generateKeypair();
            PublicKey bobPublicKey = bob.generateKeypair(alicePublicKey.getEncoded());
            alice.finishKeyAgreement(bobPublicKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(Arrays.equals(alice.getSecret(), bob.getSecret()));
    }

}