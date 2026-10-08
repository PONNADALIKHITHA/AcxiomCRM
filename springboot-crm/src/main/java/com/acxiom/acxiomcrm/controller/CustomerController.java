package com.acxiom.acxiomcrm.controller;

import com.acxiom.acxiomcrm.model.*;
import com.acxiom.acxiomcrm.repository.*;
import com.acxiom.acxiomcrm.security.AuthFilter;
import com.acxiom.acxiomcrm.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository repo; private final AuditService audit;
    public CustomerController(CustomerRepository repo, AuditService audit){this.repo=repo;this.audit=audit;}

    public record CustomerRequest(
        @NotBlank @Size(max=100) String name,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp="^[6-9][0-9]{9}$") String phone,
        String companyName,String address,String city,String state,String status) {}

    @GetMapping
    public List<Customer> list(@RequestParam(required=false) String search){
        if(search==null || search.isBlank()) return repo.findAll();
        return repo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(search,search,search);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CustomerRequest r, HttpServletRequest req){
        if(repo.existsByEmail(r.email()) || repo.existsByPhone(r.phone()))
            return ResponseEntity.status(409).body(Map.of("error","Customer email or phone already exists"));
        Customer c=new Customer(); c.name=r.name(); c.email=r.email(); c.phone=r.phone();
        c.companyName=r.companyName(); c.address=r.address(); c.city=r.city(); c.state=r.state();
        c.status=r.status()==null?"ACTIVE":r.status(); c.createdBy=user(req);
        c=repo.save(c); audit.log(user(req),"CREATE","CUSTOMER",c.id,null,c.name,"SUCCESS");
        return ResponseEntity.status(201).body(c);
    }

    @GetMapping("/{id}") public ResponseEntity<?> get(@PathVariable Long id){
        return repo.findById(id).<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@Valid @RequestBody CustomerRequest r,HttpServletRequest req){
        Customer c=repo.findById(id).orElse(null); if(c==null)return ResponseEntity.notFound().build();
        c.name=r.name(); c.email=r.email(); c.phone=r.phone(); c.companyName=r.companyName();
        c.address=r.address(); c.city=r.city(); c.state=r.state(); c.status=r.status();
        repo.save(c); audit.log(user(req),"UPDATE","CUSTOMER",id,null,c.name,"SUCCESS");
        return ResponseEntity.ok(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,HttpServletRequest req){
        if(!repo.existsById(id))return ResponseEntity.notFound().build();
        repo.deleteById(id); audit.log(user(req),"DELETE","CUSTOMER",id,null,"deleted","SUCCESS");
        return ResponseEntity.noContent().build();
    }

    private String user(HttpServletRequest r){return ((User)r.getAttribute(AuthFilter.USER_ATTR)).username;}
}
