package com.example.magiclink.model.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTokenRepository extends CrudRepository<EmailToken, Long> {

  EmailToken findByToken(String token);
}
