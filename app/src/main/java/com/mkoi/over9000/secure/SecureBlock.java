package com.mkoi.over9000.secure;

import android.util.Base64;

import com.mkoi.over9000.message.SecuredMessage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @author Bartłomiej Borucki
 */
public class SecureBlock {
    public static String calculateHMAC(String input){
        String result = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(input.getBytes());
            byte[] hashedInput = digest.digest();
            result = Base64.encodeToString(hashedInput, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<SecuredMessage> createBlocksToSend(ArrayList<String> blocks){
        ArrayList<SecuredMessage> result = new ArrayList<>();
        SecuredMessage securedMessage = new SecuredMessage();
        int nadmiar = 2;
        int rounds = 0;
        for(int i = 1; i<=blocks.size(); i++){
            //Dobre wiadomości
            securedMessage.setId(i);
            securedMessage.setMessage(blocks.get(i));
            String hmac = calculateHMAC(blocks.get(i));
            securedMessage.setHmac(hmac);
            result.add(securedMessage);
            //Złe wiadomości
            while (rounds < nadmiar-1){
                securedMessage.setId(i);
                RandomString rMess = new RandomString(blocks.get(i).length());
                securedMessage.setMessage(rMess.nextString());
                RandomString rHash = new RandomString(hmac.length());
                securedMessage.setHmac(rHash.nextString());
                result.add(securedMessage);
                rounds ++;
            }
        }
        return result;
    }

    public static ArrayList<String> prepareReceivedBlocks(ArrayList<SecuredMessage> receivedPayload){
        ArrayList<String> result = new ArrayList<>();
        SecuredMessage securedMessage = new SecuredMessage();
        for(SecuredMessage temp : receivedPayload){
            //pobranie bloku i liczenie hash'a
            String receivedMessage = temp.getMessage();
            String receivedHmac = temp.getHmac();
            if (receivedHmac.equals(calculateHMAC(receivedMessage))){
                result.add(receivedMessage);
            }
        }
        return result;
    }
}
