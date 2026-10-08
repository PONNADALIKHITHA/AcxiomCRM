package com.acxiom.acxiomcrm.controller;

import com.acxiom.acxiomcrm.model.*;
import com.acxiom.acxiomcrm.repository.FollowUpRepository;
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
@RequestMapping("/api/followups")
public class FollowUpController {
    private final FollowUpRepository repo; private final AuditService audit;
    public FollowUpController(FollowUpRepository repo,AuditService audit){this.repo=repo;this.audit=audit;}

    public record FollowUpRequest(Long customerId,Long leadId,@NotNull LocalDate followUpDate,
      @NotBlank String followUpType,String remarks,String status,String assignedTo){}

    @GetMapping public List<FollowUp> list(){return repo.findAll();}

    @PostMapping public ResponseEntity<?> create(@Valid @RequestBody FollowUpRequest r,HttpServletRequest req){
        if(r.followUpDate().isBefore(LocalDate.now()))
            return ResponseEntity.badRequest().body(Map.of("error","Follow-up date cannot be earlier than today."));
        FollowUp f=new FollowUp();f.customerId=r.customerId();f.leadId=r.leadId();f.followUpDate=r.followUpDate();
        f.followUpType=r.followUpType();f.remarks=r.remarks();f.status=r.status()==null?"PLANNED":r.status();
        f.assignedTo=r.assignedTo();f=repo.save(f);audit.log(user(req),"CREATE","FOLLOWUP",f.id,null,f.followUpType,"SUCCESS");
        return ResponseEntity.status(201).body(f);
    }
    private String user(HttpServletRequest r){return ((User)r.getAttribute(AuthFilter.USER_ATTR)).username;}
}
