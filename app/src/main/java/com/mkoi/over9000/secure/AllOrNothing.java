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
 * Klasa zapewniająca transformatę AONT
 *
 * @Author Bartłomiej Borucki
 */
public class AllOrNothing {
    /**
     * Rozmiar bloku wiadomości w bajtach
     */
    public static final int BLOCK_SIZE = 16;
    /**
     * Rozmiar klucza w bitach
     */
    public static final int KEY_SIZE = 128;
    /**
     * Algorytm funkcji skrótu
     */
    public static final String HASH_ALGORITHM = "MD5";
    /**
     * Algorytm szyfru blokowego
     */
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    /**
     * Algorytm do instancji klucza
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * Dokonuje transformaty AONT na wiadomości
     *
     * @param message wiadomość
     * @return tablica bloków zakodowanych w postaci base64
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static ArrayList<String> transformMessage(String message) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        ArrayList<String> result = new ArrayList<>();

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        SecretKey secretKey = keyGenerator.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);

        byte keyBytes[] = secretKey.getEncoded();
        byte finalBlock[] = new byte[BLOCK_SIZE];
        byte messageBytes[] = message.getBytes();
        int messageBlocks = (int) Math.ceil(messageBytes.length / (double) BLOCK_SIZE);

        ByteBuffer b = ByteBuffer.allocate(4); //używane do zaszyfrowania integera
        System.arraycopy(keyBytes, 0, finalBlock, 0, keyBytes.length);

        for (int i = 0; i < messageBlocks; i++) {
            b.clear();
            b.putInt(i + 1);
            byte counterBytes[] = b.array();
            int minRange = i * BLOCK_SIZE;
            int maxRange = (i + 1) * BLOCK_SIZE;
            byte[] messagePart = Arrays.copyOfRange(messageBytes, minRange, maxRange);
            byte encryptedCounter[] = cipher.doFinal(counterBytes);
            byte xorMessage[] = new byte[BLOCK_SIZE];
            for (int j = 0; j < BLOCK_SIZE; j++) {
                xorMessage[j] = (byte) ((int) messagePart[j] ^ (int) encryptedCounter[j]);
            }
            digest.update(xorMessage);
            result.add(Base64.encodeToString(xorMessage, Base64.DEFAULT));
        }
        result.add(processFinalBlock(digest, finalBlock));
        return result;
    }

    /**
     * Tworzy ostatni blok wiadomości
     *
     * @param digest     obiekt do tworzenia funkcji skrótu załadowany wcześniej wiadomościami
     * @param finalBlock ostatni blok załadowany kluczem
     * @return ostatni blok base64
     */
    private static String processFinalBlock(MessageDigest digest, byte[] finalBlock) {
        byte[] hashedMessages = digest.digest();
        for (int i = 0; i < finalBlock.length; i++) {
            finalBlock[i] = (byte) ((int) finalBlock[i] ^ (int) hashedMessages[i]);
        }
        return Base64.encodeToString(finalBlock, Base64.DEFAULT);
    }

    /**
     * Odwrócenie transformaty AONT
     *
     * @param messages lista odebranych wiadomości
     * @return wiadomość
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String revertTransformation(ArrayList<String> messages) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        StringBuilder sb = new StringBuilder();
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        int messageBlocks = messages.size();
        byte lastBlock[] = Base64.decode(messages.get(messageBlocks - 1), Base64.DEFAULT);
        SecretKey secretKey = getSecretKey(messages, messageBlocks, lastBlock);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        ByteBuffer b = ByteBuffer.allocate(4);

        for (int i = 0; i < messageBlocks - 1; i++) {
            b.clear();
            b.putInt(i + 1);
            byte blockCnt[] = b.array();
            byte encryptedCounter[] = cipher.doFinal(blockCnt);
            byte messagePart[] = Base64.decode(messages.get(i), Base64.DEFAULT);
            for (int j = 0; j < messagePart.length; j++) {
                messagePart[j] = (byte) ((int) messagePart[j] ^ (int) encryptedCounter[j]);
            }
            sb.append(new String(messagePart));
        }
        return sb.toString().trim();
    }

    /**
     * Odzyskiwanie klucza z ostatniej wiadomości
     *
     * @param messages      lista odebranych wiadomości
     * @param messageBlocks
     * @param lastBlock
     * @return
     */
    private static SecretKey getSecretKey(ArrayList<String> messages, int messageBlocks, byte[] lastBlock)
            throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte key[] = new byte[16];
        System.arraycopy(lastBlock, 0, key, 0, 16);
        for (int i = 0; i < messageBlocks - 1; i++) {
            byte messagePart[] = Base64.decode(messages.get(i), Base64.DEFAULT);
            digest.update(messagePart);
        }
        byte[] hashedBytes = digest.digest();
        for (int k = 0; k < key.length; k++) {
            key[k] = (byte) ((int) key[k] ^ (int) hashedBytes[k]);
        }
        return new SecretKeySpec(key, CIPHER_ALGORITHM);
    }
}
