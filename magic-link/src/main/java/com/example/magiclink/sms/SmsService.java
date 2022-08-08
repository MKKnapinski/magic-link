package com.example.magiclink.sms;

import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

  public String getSmsCode() {
    return RandomString.make(8);
  }
}
