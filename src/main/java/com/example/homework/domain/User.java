package com.example.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // 'user'는 DB 예약어인 경우가 많아 'users'로 테이블명 지정
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 ID를 자동 생성하도록 위임
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String role;
}
