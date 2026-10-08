package com.acxiom.acxiomcrm.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="customers")
public class Customer {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    @Column(nullable=false, length=100)
    public String name;

    @Column(nullable=false, unique=true, length=120)
    public String email;

    @Column(nullable=false, unique=true, length=15)
    public String phone;

    public String companyName;
    public String address;
    public String city;
    public String state;
    public String status = "ACTIVE";
    public LocalDateTime createdDate = LocalDateTime.now();
    public String createdBy;
}
