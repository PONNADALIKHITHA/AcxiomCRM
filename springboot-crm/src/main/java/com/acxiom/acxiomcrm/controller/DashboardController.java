package com.acxiom.acxiomcrm.controller;

import com.acxiom.acxiomcrm.repository.*;
import com.acxiom.acxiomcrm.model.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class DashboardController {
    private final CustomerRepository customers; private final LeadRepository leads;
    private final OpportunityRepository opps; private final FollowUpRepository followups;
    private final AuditLogRepository audits;
    public DashboardController(CustomerRepository c,LeadRepository l,OpportunityRepository o,FollowUpRepository f,AuditLogRepository a){
        customers=c;leads=l;opps=o;followups=f;audits=a;
    }

    @GetMapping("/health") public Map<String,String> health(){return Map.of("status","UP","application","AcxiomCRM");}

    @GetMapping("/dashboard")
    public Map<String,Object> dashboard(){
        var all=opps.findAll();
        double pipeline=all.stream().filter(o->"OPEN".equalsIgnoreCase(o.status))
            .mapToDouble(o->o.amount==null?0:o.amount).sum();
        double weighted=all.stream().filter(o->"OPEN".equalsIgnoreCase(o.status))
            .mapToDouble(o->o.amount*(o.probability/100.0)).sum();
        return Map.of(
            "totalCustomers",customers.count(),
            "totalLeads",leads.count(),
            "openLeads",leads.findAll().stream().filter(l->!List.of("LOST","CONVERTED").contains(l.status)).count(),
            "totalOpportunities",opps.count(),
            "openOpportunities",all.stream().filter(o->"OPEN".equalsIgnoreCase(o.status)).count(),
            "wonOpportunities",all.stream().filter(o->"WON".equalsIgnoreCase(o.status)).count(),
            "lostOpportunities",all.stream().filter(o->"LOST".equalsIgnoreCase(o.status)).count(),
            "pendingFollowUps",followups.findAll().stream().filter(f->"PLANNED".equalsIgnoreCase(f.status)).count(),
            "totalPipelineValue",pipeline,
            "weightedPipelineValue",weighted
        );
    }

    @GetMapping("/reports/pipeline")
    public Map<String,Object> pipeline(){
        Map<String,Double> stage=new LinkedHashMap<>();
        for(Opportunity o:opps.findAll()) stage.merge(o.stage,o.amount==null?0:o.amount,Double::sum);
        return Map.of("stageWisePipeline",stage);
    }

    @GetMapping("/audit")
    public List<AuditLog> audit(){return audits.findAll();}
}
