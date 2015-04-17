package com.mkoi.over9000.secure;

import android.util.Base64;

import org.androidannotations.annotations.EBean;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * @Author Bartłomiej Borucki
 */

@EBean
public class AllOrNothing {
    List<String> messageblocks= new ArrayList<String>();
    public void transformMessage(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // tablica bajtów wiadomości
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
        Cipher cipherCounter = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //klucz do szyfrowania XOR(licznik, bloczek)
        Cipher cipherMessage = Cipher.getInstance("AES/ECB/PKCS5Padding");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        //do licznika
        SecretKey secretKey = keyGenerator.generateKey();
        //do bloczka
        Key key = keyGenerator.generateKey();
        for (int i = 0; i<blockcounter; i++) {
            int minrange = i * blocksize;
            int maxrange = (i + 1) * blocksize;
            byte[] messagepart;
            messagepart = Arrays.copyOfRange(mes,minrange,maxrange);
            //1. Szyfrowanie licznika
            cipherCounter.init(Cipher.ENCRYPT_MODE, secretKey);
            byte encryptedCounter[] = cipherCounter.doFinal(blockCnt);

            //2. XORowanie bloczka z zaszyfrowanym licznikiem
            byte xormessage[] = new byte[16];
            for (int j = 0; j < mes.length; j++) {
                xormessage[j] = (byte) ((int) messagepart[j] ^ (int) encryptedCounter[j]);
            }

            //3. XORowanie wyjścia z 2. z licznikiem
            for (int j = 0; j < blockCnt.length; j++) {
                xormessage[j] = (byte) ((int) xormessage[j] ^ (int) blockCnt[j]);
            }

            //4. szyfrownaie wiadomości z wyjściem z 3.
            cipherMessage.init(Cipher.ENCRYPT_MODE, key);
            byte encryptedMessage[] = cipherMessage.doFinal(xormessage);
            messageblocks.add(Base64.encodeToString(encryptedMessage, Base64.DEFAULT));
            if(i==1){

            }
        }
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
