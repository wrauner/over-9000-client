package com.mkoi.over9000.secure;

import android.util.Log;

import org.androidannotations.annotations.EBean;

import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 * Obsługa ustalenia wspólnego sekretu protokołem Diffiego-Hellmana
 * @author Wojciech Rauner
 */
@EBean
public class DiffieHellman {

    public static final String LOG_TAG = "Over9000.DH";

    /**
     * Parametry protokołu DH
     */
    private DHParameterSpec dhParameterSpec;

    /**
     * Obiekt który generuje sekret
     */
    private KeyAgreement keyAgreement;

    /**
     * Sekret
     */
    private byte[] secret;

    public byte[] getSecret() {
        return secret;
    }

    /**
     * Tworzy parametry protokołu DH
     * @throws NoSuchAlgorithmException
     * @throws InvalidParameterSpecException
     */
    public void generateParameters() throws NoSuchAlgorithmException, InvalidParameterSpecException {
        Log.d(LOG_TAG, "Generating new DH Params");
        AlgorithmParameterGenerator parameterGenerator = AlgorithmParameterGenerator.getInstance("DH");
        parameterGenerator.init(192);
        AlgorithmParameters algorithmParameters = parameterGenerator.generateParameters();
        dhParameterSpec = algorithmParameters.getParameterSpec(DHParameterSpec.class);
    }

    /**
     * Rozpoczyna wymianę sekretu DH
     * @return klucz publiczny
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     */
    public PublicKey generateKeypair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        KeyPair keyPair = getKeyPair();
        initializeKeyAgreement(keyPair);
        return keyPair.getPublic();
    }

    /**
     * Drugi krok DH, generuje klucz publiczny z parametrami takimi jak odebrany klucz publiczny
     * inicjalizuje key agreement
     * @param publicKey odebrany klucz publiczny
     * @return utworzony klucz publiczny
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     */
    public PublicKey generateKeypair(byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        PublicKey receivedPublicKey = getPublicKey(publicKey);
        dhParameterSpec = ((DHPublicKey)receivedPublicKey).getParams();
        KeyPair keyPair = getKeyPair();
        initializeKeyAgreement(keyPair);
        keyAgreement.doPhase(receivedPublicKey, true);
        secret = keyAgreement.generateSecret();
        return keyPair.getPublic();
    }

    /**
     * Odebranie klucza drugiej strony i zakończenie protokołu, obie strony mają ustalony sekret
     * @param publicKey odebrany klucz publiczny
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public void finishKeyAgreement(byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        PublicKey receivedPublicKey = getPublicKey(publicKey);
        keyAgreement.doPhase(receivedPublicKey, true);
        secret = keyAgreement.generateSecret();
    }

    /**
     * Inicjalizuje obiekt ustalenia sekretu
     * @param keyPair para kluczy
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    private void initializeKeyAgreement(KeyPair keyPair) throws NoSuchAlgorithmException, InvalidKeyException {
        keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(keyPair.getPrivate());
    }

    /**
     * Tworzy parę kluczy DH
     * @return para kluczy
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     */
    private KeyPair getKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(dhParameterSpec);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Odtwarza klucz publiczny z otrzymanego
     * @param publicKey otrzymany klucz publiczny encoded
     * @return klucz publiczny
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private PublicKey getPublicKey(byte[] publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        return keyFactory.generatePublic(keySpec);
    }
}
