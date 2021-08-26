package com.cos.jwt.model;

import lombok.Data;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<UserRole> roleSet =new HashSet<>();
}
