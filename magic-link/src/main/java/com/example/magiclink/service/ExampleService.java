package com.example.magiclink.service;

import com.example.magiclink.config.EncryptionConfig;
import com.example.magiclink.model.entity.EmailToken;
import com.example.magiclink.model.entity.EmailTokenRepository;
import com.example.magiclink.service.encrypt.EncryptionService;
import com.example.magiclink.service.mail.EmailService;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Clock;
import java.time.OffsetDateTime;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExampleService {

  @Value("classpath:email/email.html")
  Resource resource;

  private final EmailService emailService;

  private final EncryptionService encryptionService;

  private final EncryptionConfig encryptionConfig;

  private final EmailTokenRepository emailTokenRepository;

  private final Clock clock;

  public void test()
      throws IOException, MessagingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {
    String email = new String(Files.readAllBytes(resource.getFile().toPath()));

    OffsetDateTime now = OffsetDateTime.now(clock);

    String salt = RandomString.make(16);
    String controlValue = RandomString.make(16);

    SecretKey keyFromPassword = encryptionService.getKeyFromPassword(
        encryptionConfig.getSecret(),
        salt);
    IvParameterSpec ivParameterSpec = encryptionService.generateInitializationVector();

    String token = encryptionService.encryptPasswordBased(
        controlValue,
        keyFromPassword,
        ivParameterSpec);

    EmailToken emailTokenEntity = emailTokenRepository.save(new EmailToken());

    emailTokenEntity.setAlgorithm(encryptionService.getUsedAlgorithm());
    emailTokenEntity.setControlValue(controlValue);
    emailTokenEntity.setSalt(salt);
    emailTokenEntity.setValid(true);
    emailTokenEntity.setInitializationVector(ivParameterSpec.getIV());
    emailTokenEntity.setToken(token);
    emailTokenEntity.setCreateDate(now);
    emailTokenEntity.setUpdateDate(now);
    emailTokenEntity.setValidUntil(now.plusDays(1L));
    emailTokenEntity.setContent(
        "This is a content that I wanted to display or some data that will be used in a further process. "
            + "I din't want it to be the token itself so that it doesn't grow endlessly");

    emailTokenRepository.save(emailTokenEntity);

    email = email.replaceAll("&BUTTON_LINK", "http://localhost:8888/token/" + token);
    emailService.sendHtmlMessage("mkknapinski@gmail.com", "testHTML", email);
  }

  public String test(String token)
      throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
    OffsetDateTime now = OffsetDateTime.now(clock);
    EmailToken found = emailTokenRepository.findByToken(token);

    String decrypted = encryptionService.decryptPasswordBased(
        token,
        encryptionService.getKeyFromPassword(encryptionConfig.getSecret(), found.getSalt()),
        new IvParameterSpec(found.getInitializationVector())
    );

    String content = null;
    if (found.getToken().equals(token)
        && found.getValid()
        && found.getControlValue().equals(decrypted)
        && found.getValid()
        && now.isBefore(found.getValidUntil())) {
      content = found.getContent();

      found.setValid(false);
      emailTokenRepository.save(found);
    }

    return content;
  }
}
