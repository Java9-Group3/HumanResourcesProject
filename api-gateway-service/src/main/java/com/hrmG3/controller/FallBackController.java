package com.hrmG3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallBackController {
    @GetMapping("/authservice")
    public ResponseEntity<String> authServiceFallback(){
        return  ResponseEntity.ok("Auth-sevice şuanda hizmet veremiyor.");
    }
    @GetMapping("/userservice")
    public ResponseEntity<String> userServiceFallback(){
        return  ResponseEntity.ok("UserProfile-sevice şuanda hizmet veremiyor.");
    }
    @GetMapping("/companyservice")
    public ResponseEntity<String> companyServiceFallback(){
        return  ResponseEntity.ok("Company-sevice şuanda hizmet veremiyor.");
    }
    @GetMapping("/commentservice")
    public ResponseEntity<String> commentServiceFallback(){
        return  ResponseEntity.ok("Comment-sevice şuanda hizmet veremiyor.");
    }
    @GetMapping("/expenseservice")
    public ResponseEntity<String> expenseServiceFallback(){
        return  ResponseEntity.ok("Expense-sevice şuanda hizmet veremiyor.");
    }
}
