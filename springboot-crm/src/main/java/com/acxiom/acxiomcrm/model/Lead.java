package com.acxiom.acxiomcrm.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="leads")
public class Lead {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    @Column(nullable=false)
    public String name;
    @Column(nullable=false)
    public String email;
    @Column(nullable=false)
    public String phone;
    public String companyName;
    public String source;

    @Column(nullable=false)
    public String status = "NEW";

    public String priority = "MEDIUM";
    public Double expectedValue;
    public LocalDateTime createdDate = LocalDateTime.now();
    public String assignedTo;
}
