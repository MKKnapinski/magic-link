package com.example.magiclink.controller;

import com.example.magiclink.service.ExampleService;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExampleController {

  private final ExampleService exampleService;

  @GetMapping
  public void sendTokenEmail()
      throws InvalidAlgorithmParameterException, MessagingException, IllegalBlockSizeException, NoSuchPaddingException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
    exampleService.test();
  }

  @GetMapping("/token/{token}")
  public String parseToken(@PathVariable String token)
      throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
    return exampleService.test(token);
  }

}
