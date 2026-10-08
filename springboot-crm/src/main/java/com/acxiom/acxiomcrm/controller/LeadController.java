package com.acxiom.acxiomcrm.controller;

import com.acxiom.acxiomcrm.model.*;
import com.acxiom.acxiomcrm.repository.LeadRepository;
import com.acxiom.acxiomcrm.security.AuthFilter;
import com.acxiom.acxiomcrm.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/leads")
public class LeadController {
    private final LeadRepository repo; private final AuditService audit;
    public LeadController(LeadRepository repo,AuditService audit){this.repo=repo;this.audit=audit;}

    public record LeadRequest(@NotBlank String name,@NotBlank @Email String email,
      @NotBlank @Pattern(regexp="^[6-9][0-9]{9}$") String phone,String companyName,String source,
      @NotBlank String status,String priority,@PositiveOrZero Double expectedValue,String assignedTo){}

    @GetMapping public List<Lead> list(){return repo.findAll();}

    @PostMapping public ResponseEntity<?> create(@Valid @RequestBody LeadRequest r,HttpServletRequest req){
        List<String> allowed=List.of("NEW","CONTACTED","QUALIFIED","UNQUALIFIED","CONVERTED","LOST");
        if(!allowed.contains(r.status().toUpperCase()))
            return ResponseEntity.badRequest().body(Map.of("error","Invalid lead status"));
        Lead l=new Lead(); l.name=r.name();l.email=r.email();l.phone=r.phone();l.companyName=r.companyName();
        l.source=r.source();l.status=r.status().toUpperCase();l.priority=r.priority();l.expectedValue=r.expectedValue();
        l.assignedTo=r.assignedTo();l=repo.save(l);
        audit.log(user(req),"CREATE","LEAD",l.id,null,l.name,"SUCCESS");
        return ResponseEntity.status(201).body(l);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@Valid @RequestBody LeadRequest r,HttpServletRequest req){
        Lead l=repo.findById(id).orElse(null);if(l==null)return ResponseEntity.notFound().build();
        l.name=r.name();l.email=r.email();l.phone=r.phone();l.companyName=r.companyName();l.source=r.source();
        l.status=r.status().toUpperCase();l.priority=r.priority();l.expectedValue=r.expectedValue();l.assignedTo=r.assignedTo();
        repo.save(l);audit.log(user(req),"UPDATE","LEAD",id,null,l.name,"SUCCESS");return ResponseEntity.ok(l);
    }
    private String user(HttpServletRequest r){return ((User)r.getAttribute(AuthFilter.USER_ATTR)).username;}
}
