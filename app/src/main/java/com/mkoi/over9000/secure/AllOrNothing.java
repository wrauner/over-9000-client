package com.mkoi.over9000.secure;

import android.util.Base64;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author Bartłomiej Borucki
 */
public class AllOrNothing {

    public static ArrayList<String> transformMessage(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // tablica bajtów wiadomości
        ArrayList<String> result = new ArrayList<>();
        byte mes[] = message.getBytes();
        //rozmiar bloczka (16B = 128b)
        int blocksize = 16;
        //na ile bloczków podzielona będzie wiadomość
        int blockcounter = (int)Math.ceil(mes.length / (double)blocksize);
        //tablica bajtów dla licznika
        ByteBuffer b = ByteBuffer.allocate(4);
//        b.putInt(blockcounter);
//        byte blockCnt[] = b.array();
        //klucz do szyfrowania licznika
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        //do licznika
        SecretKey secretKey = keyGenerator.generateKey();
        byte keyBytes[] = secretKey.getEncoded();
        byte finalBlock[] = new byte[16];
        System.arraycopy(keyBytes,0,finalBlock,0,keyBytes.length);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        MessageDigest digest = MessageDigest.getInstance("MD5");

        for (int i = 0; i<blockcounter; i++) {
            b.clear();
            b.putInt(i+1);
            byte blockCnt[] = b.array();
            int minrange = i * blocksize;
            int maxrange = (i + 1) * blocksize;
            byte[] messagepart;
            messagepart = Arrays.copyOfRange(mes,minrange,maxrange);

            //1. Szyfrowanie licznika
            byte encryptedCounter[] = cipher.doFinal(blockCnt);

            //2. XORowanie bloczka z zaszyfrowanym licznikiem
            byte xormessage[] = new byte[16];
            for (int j = 0; j < blocksize; j++) {
                xormessage[j] = (byte) ((int) messagepart[j] ^ (int) encryptedCounter[j]);
            }
            //3. Obliczanie skrótu wyjścia z 2.

            digest.reset();
            digest.update(xormessage);
            byte[] hashedBytes = digest.digest();

            String messageHash = Base64.encodeToString(hashedBytes, Base64.DEFAULT); //HASH
            String messageOut = Base64.encodeToString(xormessage, Base64.DEFAULT);  //MESSAGE (m')

            //3. XORowanie klucza z haszami
            for (int k = 0; k < finalBlock.length; k++){
                finalBlock[k] = (byte) ((int) finalBlock[k] ^ (int) hashedBytes[k]);
            }
            result.add(messageOut);
        }
        String finalBlockStr = Base64.encodeToString(finalBlock, Base64.DEFAULT);
        result.add(finalBlockStr);

        return result;
    }

    public static String RevertTransformation(ArrayList<String> messages) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        StringBuilder sb = new StringBuilder();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        MessageDigest digest = MessageDigest.getInstance("MD5");
        int noOfBlocks = messages.size();
        byte lastBlock[] = Base64.decode(messages.get(noOfBlocks-1), Base64.DEFAULT);
        byte key[] = new byte[16];
        System.arraycopy(lastBlock,0,key,0,16);
        //odzyskiwanie klucza
        for (int i=0; i< noOfBlocks-1; i++){
            byte messagePart[] = Base64.decode(messages.get(i), Base64.DEFAULT);
            digest.reset();
            digest.update(messagePart);
            byte[] hashedBytes = digest.digest();
            for (int k=0; k<key.length;k++){
                key[k] = (byte)((int)key[k] ^ (int)hashedBytes[k]);
            }
        }
        SecretKey secretKey = new SecretKeySpec(key,"AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        ByteBuffer b = ByteBuffer.allocate(4);

        for (int i=0; i<noOfBlocks-1; i++){
            b.clear();
            b.putInt(i+1);
            byte blockCnt[] = b.array();
            byte encryptedCounter[] = cipher.doFinal(blockCnt);
            byte messagePart[] = Base64.decode(messages.get(i), Base64.DEFAULT);
            for (int j=0;j<messagePart.length;j++){
                messagePart[j] = (byte)((int)messagePart[j] ^ (int)encryptedCounter[j]);
            }
            String partOfMessage = new String(messagePart);
            sb.append(partOfMessage);
        }

        return sb.toString();
    }

}
