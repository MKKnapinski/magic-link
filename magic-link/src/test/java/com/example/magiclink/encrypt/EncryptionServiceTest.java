package com.example.magiclink.encrypt;

import com.example.magiclink.config.EncryptionConfig;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EncryptionServiceTest {

  @Autowired
  private EncryptionConfig conf;

  EncryptionService encryptionService;

  String ALGORITHM;

  @BeforeEach
  void setUp() {
    this.encryptionService = new EncryptionService(conf);
    ALGORITHM = conf.getAlgorithm() + "/" + conf.getVariant() + "/" + conf.getPadding();
  }

  @Test
  void shouldEncryptString()
      throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException,
      BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
    // given
    String input = "Test";
    SecretKey key = encryptionService.generateKey(128);
    IvParameterSpec ivParameterSpec = encryptionService.generateInitializationVector();

    // when
    String cipherText = encryptionService.encryptString(ALGORITHM, input, key, ivParameterSpec);
    String plainText = encryptionService.decryptString(ALGORITHM, cipherText, key, ivParameterSpec);

    // then
    Assertions.assertEquals(input, plainText);
  }

  @Test
  void shouldEncryptWithPassword()
      throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException,
      InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

    String plainText = "test";
    String password = "pass";
    String salt = "aaaaaaaaaaaaaaaaaaaa";

    IvParameterSpec ivParameterSpec = encryptionService.generateInitializationVector();
    SecretKey key = encryptionService.getKeyFromPassword(password, salt);

    String cipherText
        = encryptionService.encryptPasswordBased(plainText, key, ivParameterSpec);
    String decryptedCipherText
        = encryptionService.decryptPasswordBased(cipherText, key, ivParameterSpec);

    Assertions.assertEquals(plainText, decryptedCipherText);
  }

}
