package com.mkoi.over9000.secure;

import android.util.Base64;

import com.mkoi.over9000.message.SecuredMessage;

import org.androidannotations.annotations.EBean;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Obliczanie HMAC dla wiadomości, dorzucanie chaffingu
 * @author Bartłomiej Borucki
 */
@EBean
public class SecureBlock {
    public static final int OVERPLUS = 2;

    public static final String HMAC_ALGORITHM = "HmacSHA256";

    public static final int HMAC_BYTES = 32;

    private Mac mac;

    private SecureRandom secureRandom;

    public String calculateHMAC(String input, byte[] secret) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = new SecretKeySpec(secret, HMAC_ALGORITHM);
        mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(secretKey);
        byte[] message = input.getBytes();
        byte[] resultTab = mac.doFinal(message);
        return Base64.encodeToString(resultTab, Base64.DEFAULT);
    }

    public ArrayList<SecuredMessage> createBlocksToSend(ArrayList<String> blocks, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        ArrayList<SecuredMessage> result = new ArrayList<>();
        SecuredMessage securedMessage = new SecuredMessage();
        for(int i = 0; i<blocks.size(); i++){
            createSecureMessage(blocks, key, securedMessage, i);
            for(int j=0; j<OVERPLUS; j++) {
                createFakeMessage(result, AllOrNothing.BLOCK_SIZE, HMAC_BYTES, i); //TODO shuffle
            }
            result.add(securedMessage);
        }
        return result;
    }

    private void createSecureMessage(ArrayList<String> blocks, byte[] key, SecuredMessage securedMessage, int i) throws NoSuchAlgorithmException, InvalidKeyException {
        securedMessage.setId(i);
        securedMessage.setMessage(blocks.get(i));
        String hmac = calculateHMAC(blocks.get(i), key);
        securedMessage.setHmac(hmac);
    }

    private void createFakeMessage(ArrayList<SecuredMessage> securedMessages, int blockLength, int hmacLength, int id) {
        SecuredMessage fakeMessage = new SecuredMessage();
        fakeMessage.setMessage(createRandomBase64(blockLength));
        fakeMessage.setHmac(createRandomBase64(hmacLength));
        fakeMessage.setId(id);
        securedMessages.add(fakeMessage);
    }

    public ArrayList<String> prepareReceivedBlocks(ArrayList<SecuredMessage> receivedPayload, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        ArrayList<String> result = new ArrayList<>();
        for(SecuredMessage temp : receivedPayload){
            String receivedMessage = temp.getMessage();
            String receivedHmac = temp.getHmac();
            if (receivedHmac.equals(calculateHMAC(receivedMessage,key))){
                result.add(receivedMessage);
            }
        }
        return result;
    }

    private String createRandomBase64(int length) {
        secureRandom = new SecureRandom();
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
