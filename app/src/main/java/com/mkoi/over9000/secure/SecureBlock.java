package com.mkoi.over9000.secure;

import android.util.Base64;

import com.mkoi.over9000.message.SecuredMessage;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Bartłomiej Borucki
 */
public class SecureBlock {
    public static final int OVERPLUS = 2;

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    public static String calculateHMAC(String input, byte[] key){
        String result = "";
        SecretKey secretKey = new SecretKeySpec(key, HMAC_ALGORITHM);
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKey);
            byte[] message = input.getBytes();
            byte[] resultTab = mac.doFinal(message);
            result = Base64.encodeToString(resultTab, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<SecuredMessage> createBlocksToSend(ArrayList<String> blocks, byte[] key){
        ArrayList<SecuredMessage> result = new ArrayList<>();
        SecuredMessage securedMessage = new SecuredMessage();
        int rounds = 0;
        for(int i = 0; i<blocks.size(); i++){
            //Dobre wiadomości
            securedMessage.setId(i);
            securedMessage.setMessage(blocks.get(i));
            String hmac = calculateHMAC(blocks.get(i), key);
            securedMessage.setHmac(hmac);
            result.add(securedMessage);
            //Złe wiadomości
            while (rounds < OVERPLUS-1){
                createFakeMessage(blocks, result, securedMessage, i, hmac);
                rounds++;
            }
        }
        return result;
    }

    private static void createFakeMessage(ArrayList<String> blocks, ArrayList<SecuredMessage> result, SecuredMessage securedMessage, int i, String hmac) {
        RandomString rMess = new RandomString(blocks.get(i).length());
        RandomString rHash = new RandomString(hmac.length());
        securedMessage.setMessage(rMess.nextString());
        securedMessage.setHmac(rHash.nextString());
        securedMessage.setId(i);
        result.add(securedMessage);
    }

    public static ArrayList<String> prepareReceivedBlocks(ArrayList<SecuredMessage> receivedPayload, byte[] key){
        ArrayList<String> result = new ArrayList<>();
        SecuredMessage securedMessage = new SecuredMessage();
        for(SecuredMessage temp : receivedPayload){
            //pobranie bloku i liczenie hash'a
            String receivedMessage = temp.getMessage();
            String receivedHmac = temp.getHmac();
            if (receivedHmac.equals(calculateHMAC(receivedMessage,key))){
                result.add(receivedMessage);
            }
        }
        return result;
    }
}
