package com.acxiom.acxiomcrm.service;

import com.acxiom.acxiomcrm.model.AuditLog;
import com.acxiom.acxiomcrm.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditLogRepository repo;
    public AuditService(AuditLogRepository repo) { this.repo = repo; }

    public void log(String user, String action, String entity, Long id, String oldV, String newV, String result) {
        AuditLog a = new AuditLog();
        a.username=user; a.action=action; a.entityName=entity; a.recordId=id;
        a.oldValue=oldV; a.newValue=newV; a.result=result; a.ipAddress="localhost";
        repo.save(a);
    }
}
