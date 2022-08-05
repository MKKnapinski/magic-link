package com.example.magiclink.encrypt;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import com.example.magiclink.config.EncryptionConfig;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EncryptionService {

  private final EncryptionConfig conf;

  public String encryptString(String algorithm, String input, SecretKey secretKey,
      IvParameterSpec initializationVectorSpec)
      throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException,
      InvalidAlgorithmParameterException, InvalidKeyException {
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(ENCRYPT_MODE, secretKey, initializationVectorSpec);
    byte[] cipherText = cipher.doFinal(input.getBytes());
    return Base64.getEncoder().encodeToString(cipherText);
  }

  public String decryptString(String algorithm, String encrypted, SecretKey secretKey,
      IvParameterSpec initializationVectorSpec)
      throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
      InvalidAlgorithmParameterException, InvalidKeyException {
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(DECRYPT_MODE, secretKey, initializationVectorSpec);
    byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(encrypted));
    return new String(plainText);
  }

  public String encryptPasswordBased(String plainText, SecretKey key,
      IvParameterSpec initializationVector)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
      InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    Cipher cipher
        = Cipher.getInstance(
        conf.getAlgorithm() + "/" + conf.getVariant() + "/" + conf.getPadding());
    cipher.init(ENCRYPT_MODE, key, initializationVector);
    return Base64.getEncoder()
        .encodeToString(cipher.doFinal(plainText.getBytes()));
  }

  public String decryptPasswordBased(String cipherText, SecretKey key,
      IvParameterSpec initializationVector)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
      InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    Cipher cipher = Cipher.getInstance(
        conf.getAlgorithm() + "/" + conf.getVariant() + "/" + conf.getPadding());
    cipher.init(DECRYPT_MODE, key, initializationVector);
    return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
  }

  public SecretKey generateKey(int n) throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(conf.getAlgorithm());
    keyGenerator.init(n);
    return keyGenerator.generateKey();
  }

  public SecretKey getKeyFromPassword(String password, String salt)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    SecretKeyFactory factory = SecretKeyFactory.getInstance(conf.getSecretKeyFactoryAlgorithm());
    KeySpec spec = new PBEKeySpec(
        password.toCharArray(),
        salt.getBytes(),
        conf.getPasswordIterations(),
        conf.getKeyLength());
    return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), conf.getAlgorithm());
  }

  public IvParameterSpec generateInitializationVector() {
    byte[] initializationVector = new byte[16];
    new SecureRandom().nextBytes(initializationVector);
    return new IvParameterSpec(initializationVector);
  }

}
