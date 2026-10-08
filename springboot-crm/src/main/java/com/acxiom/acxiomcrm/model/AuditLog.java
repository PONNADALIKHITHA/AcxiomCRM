package com.acxiom.acxiomcrm.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="audit_logs")
public class AuditLog {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long id;

    public String username;
    public String action;
    public String entityName;
    public Long recordId;
    @Column(length=1000)
    public String oldValue;
    @Column(length=1000)
    public String newValue;
    public LocalDateTime createdDate = LocalDateTime.now();
    public String ipAddress;
    public String result;
}
