package com.example.magiclink.model.entity;


import java.time.OffsetDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "EMAIL_TOKEN")
@Data
public class EmailTokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String salt;

  private Boolean valid;

  private OffsetDateTime createDate;

  private Long ttlHours;

}
