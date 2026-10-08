package com.acxiom.acxiomcrm.repository;

import com.acxiom.acxiomcrm.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {


}
