package com.acxiom.acxiomcrm.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    @Column(unique=true, nullable=false)
    public String username;

    @Column(nullable=false)
    public String passwordHash;

    @Column(nullable=false)
    public String role = "SALES_EXECUTIVE";

    public boolean active = true;
    public int failedLoginCount = 0;
    public LocalDateTime lockoutEnd;
    public LocalDateTime createdDate = LocalDateTime.now();

    public User() {}
}
