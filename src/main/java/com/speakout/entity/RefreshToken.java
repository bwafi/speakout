package com.speakout.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  
  @Lob
  private String refreshToken;
  
  private Long expiresIn;
  
  @Column(name = "created_at", insertable = false)
  private Long createdAt;
  
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;
  
  @PrePersist
  private void onPrePersist() {
    this.createdAt = Instant.now().toEpochMilli();
  }
}
