package com.speakout.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  
  @Column(unique = true, nullable = false)
  private String username;
  
  @Column(nullable = false)
  private String password;
  
  @Lob
  private String bio;
  
  @OneToOne
  @JoinColumn(name = "role_id", referencedColumnName = "id")
  private Role role;
  
  @OneToOne
  @JoinColumn(name = "profil_picture_id", referencedColumnName = "id")
  private ProfilePicture profilePicture;
  
  @JsonProperty("refresh_token")
  private String refreshToken;
  
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
