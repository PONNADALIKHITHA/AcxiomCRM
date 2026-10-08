package com.acxiom.acxiomcrm.controller;

import com.acxiom.acxiomcrm.model.*;
import com.acxiom.acxiomcrm.repository.OpportunityRepository;
import com.acxiom.acxiomcrm.security.AuthFilter;
import com.acxiom.acxiomcrm.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/opportunities")
public class OpportunityController {
    private final OpportunityRepository repo; private final AuditService audit;
    public OpportunityController(OpportunityRepository repo,AuditService audit){this.repo=repo;this.audit=audit;}

    public record OpportunityRequest(@NotBlank String name,Long customerId,Long leadId,
      @NotNull Double amount,@NotNull Integer probability,@NotNull LocalDate expectedCloseDate,
      String stage,String status,String assignedTo,String notes){}

    @GetMapping public List<Opportunity> list(){return repo.findAll();}

    @PostMapping public ResponseEntity<?> create(@Valid @RequestBody OpportunityRequest r,HttpServletRequest req){
        if(r.amount()<=0)return ResponseEntity.badRequest().body(Map.of("error","Opportunity Amount must be greater than 0."));
        if(r.probability()<0||r.probability()>100)return ResponseEntity.badRequest().body(Map.of("error","Probability must be between 0 and 100."));
        String status=r.status()==null?"OPEN":r.status().toUpperCase();
        if(!status.equals("WON")&&!status.equals("LOST")&&r.expectedCloseDate().isBefore(LocalDate.now()))
            return ResponseEntity.badRequest().body(Map.of("error","Expected Close Date cannot be in the past."));
        Opportunity o=new Opportunity();o.name=r.name();o.customerId=r.customerId();o.leadId=r.leadId();
        o.amount=r.amount();o.probability=r.probability();o.expectedCloseDate=r.expectedCloseDate();
        o.stage=r.stage()==null?"QUALIFICATION":r.stage().toUpperCase();o.status=status;o.assignedTo=r.assignedTo();o.notes=r.notes();
        o=repo.save(o);audit.log(user(req),"CREATE","OPPORTUNITY",o.id,null,o.name,"SUCCESS");
        return ResponseEntity.status(201).body(o);
    }
    private String user(HttpServletRequest r){return ((User)r.getAttribute(AuthFilter.USER_ATTR)).username;}
}
