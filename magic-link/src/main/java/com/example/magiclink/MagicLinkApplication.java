package com.example.magiclink;

import com.example.magiclink.mail.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MagicLinkApplication {

    @Autowired
    EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(MagicLinkApplication.class, args);
    }

    @GetMapping
    void test() {
        emailService.sendSimpleMessage("mkknapinski@gmail.com", "test", "test");
    }
}
