package com.example.magiclink.repository;

import com.example.magiclink.model.entity.EmailTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTokenRepository extends JpaRepository<EmailTokenEntity, Long> {

}
