package com.example.Backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;
    @Column(name = "username")
    String username;
    @Column(name = "password")
    String password;
    @Column(name = "fullname")
    String fullName;
    @Column(name = "sex")
    String sex;
    @Column(name = "address")
    String address;
    @Column(name = "phone")
    String phone;
    @Column(name = "email")
    String email;
    @Column(name = "cid")
    String cid;
    @Column(name = "role")
    String role;
}
