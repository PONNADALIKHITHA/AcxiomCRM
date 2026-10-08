package com.acxiom.acxiomcrm.repository;

import com.acxiom.acxiomcrm.model.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {


}
