package com.speakout.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "users")
@Data
@Builder
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
  
  @ManyToOne
  @JoinColumn(name = "role_id", referencedColumnName = "id")
  private Role role;
  
  @OneToOne
  @JoinColumn(name = "profile_picture_id", referencedColumnName = "id")
  private ProfilePicture profilePicture;
  
  @OneToMany(mappedBy = "user")
  private List<RefreshToken> refreshToken;
  
  @Column(name = "created_at", insertable = false)
  private Long craetedAt;
  
  @Column(name = "updated_at", insertable = false)
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
