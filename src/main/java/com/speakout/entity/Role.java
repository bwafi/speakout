package com.speakout.entity;

import com.speakout.enums.ERoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
  
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  
  @Column(length = 10, nullable = false)
  @Enumerated(EnumType.STRING)
  private ERoles name;
  
  @OneToOne(mappedBy = "role")
  private User user;
}
