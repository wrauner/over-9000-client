package com.mkoi.over9000.secure;

import android.util.Base64;

import com.mkoi.over9000.message.SecuredMessage;

import org.androidannotations.annotations.EBean;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

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

    /**
     * Oblicza HMAC dla danych wejściowych
     * @param input dane
     * @param secret wspólny sekret
     * @return hmac w postaci base64
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public String calculateHMAC(String input, byte[] secret) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = new SecretKeySpec(secret, HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(secretKey);
        byte[] message = input.getBytes();
        mac.update(message);
        byte[] resultTab = mac.doFinal();
        return Base64.encodeToString(resultTab, Base64.DEFAULT);
    }

    /**
     * Tworzy dane do wysłania
     * @param blocks dane do wysłania
     * @param secret wspólny sekret
     * @return lista danych do wysłania
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public ArrayList<SecuredMessage> createBlocksToSend(ArrayList<String> blocks, byte[] secret) throws InvalidKeyException, NoSuchAlgorithmException {
        ArrayList<SecuredMessage> result = new ArrayList<>();
        for(int i = 0; i<blocks.size(); i++){
            ArrayList<SecuredMessage> messages = createBlocks(blocks, secret, i);
            Collections.shuffle(messages);
            result.addAll(messages);
        }
        return result;
    }

    /**
     * Tworzy dane do wysłania razem z fałszywymi danymi
     * @param blocks dane do wysłania
     * @param secret sekret
     * @param i który blok
     * @return lista danych
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private ArrayList<SecuredMessage> createBlocks(ArrayList<String> blocks, byte[] secret, int i) throws InvalidKeyException, NoSuchAlgorithmException {
        ArrayList<SecuredMessage> securedMessages = new ArrayList<>();
        securedMessages.add(createSecureMessage(blocks.get(i), secret, i));
        for(int j=0; j<OVERPLUS; j++) {
            securedMessages.add(createFakeMessage(AllOrNothing.BLOCK_SIZE, HMAC_BYTES, i));
        }
        return securedMessages;
    }

    /**
     * Tworzy poprawną wiadomość
     * @param block treść wiadomości
     * @param secret sekret
     * @param id która w kolejności
     * @return wiadomość z hmac
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    private SecuredMessage createSecureMessage(String block, byte[] secret, int id) throws InvalidKeyException, NoSuchAlgorithmException {
        SecuredMessage securedMessage = new SecuredMessage();
        securedMessage.setHmac(calculateHMAC(block, secret));
        securedMessage.setMessage(block);
        securedMessage.setId(id);
        return securedMessage;
    }

    /**
     * Tworzy sztuczną wiadomość (chaff)
     * @param blockLength rozmiar bloku
     * @param hmacLength rozmiar hmac
     * @param id która w kolejności
     * @return
     */
    private SecuredMessage createFakeMessage(int blockLength, int hmacLength, int id) {
        SecuredMessage fakeMessage = new SecuredMessage();
        fakeMessage.setMessage(createRandomBase64(blockLength));
        fakeMessage.setHmac(createRandomBase64(hmacLength));
        fakeMessage.setId(id);
        return fakeMessage;
    }

    /**
     * Filtruje odebrane wiadomości w poszukianiu poprawnych hmac
     * @param receivedPayload odebrane wiadomości
     * @param key wspólny sekret
     * @return po
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
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

    /**
     * Tworzy losowy ciąg znaków zakodowany base64
     * @param length długość
     * @return zakodowany losowy ciąg znakó∑
     */
    private String createRandomBase64(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
