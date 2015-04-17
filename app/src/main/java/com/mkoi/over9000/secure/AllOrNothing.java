package com.mkoi.over9000.secure;

import android.util.Base64;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
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
        b.putInt(blockcounter);
        byte blockCnt[] = b.array();
        //klucz do szyfrowania licznika
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        //do licznika
        SecretKey secretKey = keyGenerator.generateKey();
        byte keyBytes[] = secretKey.getEncoded();
        byte finalBlock[] = new byte[16];
        System.arraycopy(keyBytes,0,finalBlock,0,keyBytes.length);
        //do bloczka
        Key key = keyGenerator.generateKey();
        for (int i = 0; i<blockcounter; i++) {
            int minrange = i * blocksize;
            int maxrange = (i + 1) * blocksize;
            byte[] messagepart;
            messagepart = Arrays.copyOfRange(mes,minrange,maxrange);
            //1. Szyfrowanie licznika
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte encryptedCounter[] = cipher.doFinal(blockCnt);

            //2. XORowanie bloczka z zaszyfrowanym licznikiem
            byte xormessage[] = new byte[16];
            for (int j = 0; j < mes.length; j++) {
                xormessage[j] = (byte) ((int) messagepart[j] ^ (int) encryptedCounter[j]);
            }
            //3. Obliczanie skrótu wyjścia z 2.
            MessageDigest digest = MessageDigest.getInstance("MD5");
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

    public byte[][] divideArray(byte[] source, int blocksize){
        byte[][] ret = new byte[(int)Math.ceil(source.length / (double)blocksize)][blocksize];

        int start = 0;

        for(int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source, start, start + blocksize);
            start += blocksize ;
        }

        return ret;
    }
}
