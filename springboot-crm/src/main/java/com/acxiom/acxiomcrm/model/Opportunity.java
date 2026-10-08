package com.acxiom.acxiomcrm.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="opportunities")
public class Opportunity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    @Column(nullable=false)
    public String name;
    public Long customerId;
    public Long leadId;

    @Column(nullable=false)
    public Double amount;

    @Column(nullable=false)
    public Integer probability;

    @Column(nullable=false)
    public LocalDate expectedCloseDate;

    public String stage = "QUALIFICATION";
    public String status = "OPEN";
    public String assignedTo;
    public String notes;
}
