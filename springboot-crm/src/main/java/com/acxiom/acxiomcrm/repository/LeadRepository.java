package com.acxiom.acxiomcrm.repository;

import com.acxiom.acxiomcrm.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeadRepository extends JpaRepository<Lead, Long> {


}
