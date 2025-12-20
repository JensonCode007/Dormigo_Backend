package com.example.demo.service;

import lombok.extern.slf4j. Slf4j;
import org. springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class MockPaymentService {

    // In-memory storage for mock payment intents
    private final Map<String, MockPaymentIntent> paymentIntents = new HashMap<>();

    /**
     * Create a mock payment intent
     * Simulates Stripe's PaymentIntent creation
     */
    public String createPaymentIntent(BigDecimal amount, String orderNumber) {
        log.info("🎭 MOCK: Creating payment intent for order: {}, amount: ₹{}", orderNumber, amount);

        // Generate mock payment intent ID
        String paymentIntentId = "pi_mock_" + UUID.randomUUID().toString().substring(0, 8);

        // Create mock payment intent
        MockPaymentIntent intent = new MockPaymentIntent();
        intent.setId(paymentIntentId);
        intent.setAmount(amount);
        intent.setOrderNumber(orderNumber);
        intent.setStatus("requires_payment_method");
        intent.setCaptured(false);

        // Store it
        paymentIntents.put(paymentIntentId, intent);

        log.info("✅ MOCK: Payment intent created: {}", paymentIntentId);

        // Return mock client secret
        return paymentIntentId + "_secret_" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Simulate payment confirmation
     * In real app, this would be called by Stripe webhook
     */
    public void confirmPayment(String paymentIntentId) {
        log.info("🎭 MOCK:  Confirming payment: {}", paymentIntentId);

        MockPaymentIntent intent = paymentIntents.get(paymentIntentId);

        // ✅ If payment intent not found, create it (for testing after restart)
        if (intent == null) {
            log.warn("⚠️ Payment intent not found in memory (app may have restarted). Creating mock intent.");
            intent = new MockPaymentIntent();
            intent.setId(paymentIntentId);
            intent.setAmount(BigDecimal.ZERO);
            intent. setOrderNumber("UNKNOWN");
            intent.setStatus("requires_payment_method");
            intent. setCaptured(false);
            paymentIntents.put(paymentIntentId, intent);
        }

        intent.setStatus("requires_capture");
        log.info("✅ MOCK: Payment confirmed (held in escrow): {}", paymentIntentId);
    }

    /**
     * Capture payment (release from escrow)
     */
    public void capturePayment(String paymentIntentId) {
        log.info("🎭 MOCK:  Capturing payment: {}", paymentIntentId);

        MockPaymentIntent intent = paymentIntents.get(paymentIntentId);

        // ✅ If payment intent not found, create it (for testing after restart)
        if (intent == null) {
            log.warn("⚠️ Payment intent not found in memory (app may have restarted). Creating mock intent.");
            intent = new MockPaymentIntent();
            intent.setId(paymentIntentId);
            intent.setAmount(BigDecimal. ZERO);  // We don't know the amount
            intent.setOrderNumber("UNKNOWN");
            intent.setStatus("requires_capture");
            intent.setCaptured(false);
            paymentIntents.put(paymentIntentId, intent);
        }

        if (!intent.getStatus().equals("requires_capture")) {
            log.warn("⚠️ Payment status is '{}', but proceeding with capture anyway", intent.getStatus());
        }

        intent.setStatus("succeeded");
        intent.setCaptured(true);

        log.info("✅ MOCK: Payment captured (₹{}): {}", intent.getAmount(), paymentIntentId);
        log.info("💰 MOCK: Seller would receive: ₹{}", intent.getAmount());
    }

    /**
     * Cancel/Refund payment
     */
    public void cancelPayment(String paymentIntentId) {
        log.info("🎭 MOCK:  Cancelling payment: {}", paymentIntentId);

        MockPaymentIntent intent = paymentIntents.get(paymentIntentId);

        // ✅ If payment intent not found, just log warning
        if (intent == null) {
            log.warn("⚠️ Payment intent not found: {}.  Assuming already cancelled or never created.", paymentIntentId);
            return;
        }

        intent. setStatus("canceled");
        log.info("✅ MOCK: Payment cancelled (refunded): {}", paymentIntentId);
    }

    /**
     * Get payment status
     */
    public String getPaymentStatus(String paymentIntentId) {
        MockPaymentIntent intent = paymentIntents.get(paymentIntentId);
        if (intent == null) {
            log.warn("⚠️ Payment intent not found: {}", paymentIntentId);
            return "unknown";
        }
        return intent.getStatus();
    }

    /**
     * Mock PaymentIntent class
     */
    @lombok.Data
    private static class MockPaymentIntent {
        private String id;
        private BigDecimal amount;
        private String orderNumber;
        private String status;
        private Boolean captured;
    }
}