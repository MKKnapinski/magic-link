package com.example.magiclink.model.entity;


import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EMAIL_TOKEN")
@Getter
@Setter
public class EmailToken implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String controlValue;

  private String token;

  private String salt;

  private Boolean valid;

  private OffsetDateTime createDate;

  private OffsetDateTime updateDate;

  private OffsetDateTime validUntil;

  private String algorithm;

  private String content;

  @Lob
  private byte[] initializationVector;
}
