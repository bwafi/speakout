package com.speakout.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "profile_pictures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePicture {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  
  @Lob
  private byte[] image;
  
  @OneToOne(mappedBy = "profilePicture")
  private User user;
  
  @Column(name = "created_at")
  private Long craetedAt;
  
  @Column(name = "updated_at")
  private Long updatedAt;
  
  @PrePersist
  private void onPrePersist() {
    this.craetedAt = Instant.now().toEpochMilli();
    this.updatedAt = Instant.now().toEpochMilli();
  }
  
  @PreUpdate
  private void onPreUpdate() {
    this.craetedAt = Instant.now().toEpochMilli();
  }
}
