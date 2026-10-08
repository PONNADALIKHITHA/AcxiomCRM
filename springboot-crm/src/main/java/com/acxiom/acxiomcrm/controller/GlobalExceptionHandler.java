package com.acxiom.acxiomcrm.controller;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException e){
        Map<String,String> errors=new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x->errors.put(x.getField(),x.getDefaultMessage()));
        return ResponseEntity.badRequest().body(Map.of("error","Validation failed","fields",errors));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> general(Exception e){
        return ResponseEntity.status(500).body(Map.of("error","Request failed","message",e.getMessage()==null?"Unknown error":e.getMessage()));
    }
}
