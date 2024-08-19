package com.speakout.entity;

import com.speakout.enums.ERoles;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "roles")
//@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  
  @Column(length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private ERoles name;
  
  @OneToMany(mappedBy = "role")
  private List<User> user;
}
