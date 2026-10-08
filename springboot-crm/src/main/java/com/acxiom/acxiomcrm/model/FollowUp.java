package com.acxiom.acxiomcrm.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="followups")
public class FollowUp {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    public Long customerId;
    public Long leadId;

    @Column(nullable=false)
    public LocalDate followUpDate;

    @Column(nullable=false)
    public String followUpType;

    public String remarks;
    public String status = "PLANNED";
    public String assignedTo;
}
