package com.speakout.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Random;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  
  @Column(nullable = false)
  private String firstName;
  
  @Column(nullable = false)
  private String lastName;
  
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
  
  @Column(name = "created_at", insertable = false)
  private Long craetedAt;
  
  @Column(name = "updated_at", insertable = false)
  private Long updatedAt;
  
  @PrePersist
  private void onPrePersist() {
    this.craetedAt = Instant.now().toEpochMilli();
    this.updatedAt = Instant.now().toEpochMilli();
    
    Random random = new Random();
    int randomNumber = random.nextInt(1000);
    
    this.username = this.firstName.charAt(0) + "." + this.lastName + randomNumber;
  }
  
  @PreUpdate
  private void onPreUpdate() {
    this.craetedAt = Instant.now().toEpochMilli();
  }
  
}
