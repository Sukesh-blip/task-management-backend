package com.futuretransformation.taskmanagement.controller;

import com.futuretransformation.taskmanagement.dto.AIRequest;
import com.futuretransformation.taskmanagement.dto.AIResponse;
import com.futuretransformation.taskmanagement.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate")
    public ResponseEntity<AIResponse> generateTaskDetails(
            @RequestBody AIRequest request) {
        return ResponseEntity.ok(aiService.generateTaskDetails(request.getTitle()));
    }
}