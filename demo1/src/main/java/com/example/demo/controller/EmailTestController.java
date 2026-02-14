package com.example.demo.controller;

import com.example.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org. springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test/email")
@RequiredArgsConstructor
@Slf4j
public class EmailTestController {

    private final EmailService emailService;

    /**
     * Test endpoint to send a simple test email
     */
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendTestEmail(
            @RequestParam String toEmail,
            @RequestParam(defaultValue = "Test User") String name) {

        log.info("Test email request:  to={}, name={}", toEmail, name);

        try {
            emailService.sendTestEmail(toEmail, name);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Test email sent to " + toEmail);
            response.put("recipient", toEmail);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Test email failed", e);

            Map<String, String> response = new HashMap<>();
            response. put("status", "error");
            response.put("message", "Failed to send email: " + e.getMessage());

            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Check email configuration
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> checkConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("status", "Email service is configured");
        config.put("templates", "welcome.ftl, test-email.ftl");

        return ResponseEntity.ok(config);
    }
}