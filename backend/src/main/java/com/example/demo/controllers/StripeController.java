package com.example.demo.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Donation;
import com.example.demo.services.DonationService;
import com.example.demo.services.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private static final Logger logger = LoggerFactory.getLogger(StripeController.class);

    private final StripeService stripeService;
    private final DonationService donationService;

    @Value("${stripe.webhook.secret:}")
    private String webhookSecret;

    public StripeController(StripeService stripeService, DonationService donationService) {
        this.stripeService = stripeService;
        this.donationService = donationService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> createCheckoutSession(@RequestBody CheckoutRequest request, HttpServletRequest httpRequest) {
        if (request.getAmount() == null || request.getAmount() <= 0 || request.getActionId() == null || request.getUserId() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Données de don invalides"));
        }

        Donation donation = new Donation();
        donation.setAmount(request.getAmount());
        donation.setPaymentMethod("STRIPE");
        donation.setStatus("PENDING");
        donation.setUser(request.getUser());
        donation.setAction(request.getAction());
        Donation saved = donationService.save(donation);

        String origin = httpRequest.getHeader("Origin");
        String successUrl = origin != null ? origin + "/actions?donationSuccess=true" : "http://localhost:8081/actions?donationSuccess=true";
        String cancelUrl = origin != null ? origin + "/actions?donationCancelled=true" : "http://localhost:8081/actions?donationCancelled=true";

        try {
            Session session = stripeService.createCheckoutSession(saved.getId(), saved.getAmount(), "mad", successUrl, cancelUrl, "Don à l'action " + (saved.getAction() != null ? saved.getAction().getTitle() : ""));
            return ResponseEntity.ok(Map.of("url", session.getUrl()));
        } catch (StripeException ex) {
            logger.error("Impossible de créer la session Stripe", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erreur Stripe"));
        }
    }

    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,
                                                @RequestHeader(name = "Stripe-Signature", required = false) String sigHeader) {
        String payload = readPayload(request);
        if (payload == null) {
            return ResponseEntity.badRequest().body("Invalid payload");
        }

        Event event;
        try {
            if (webhookSecret != null && !webhookSecret.isBlank()) {
                event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            } else {
                event = Event.GSON.fromJson(payload, Event.class);
            }
        } catch (SignatureVerificationException e) {
            logger.error("Signature Stripe invalide", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Optional<StripeObject> obj = event.getDataObjectDeserializer().getObject();
            if (obj.isPresent() && obj.get() instanceof Session) {
                Session session = (Session) obj.get();
                String donationId = session.getMetadata().get("donationId");
                if (donationId != null) {
                    try {
                        donationService.completeDonation(Long.parseLong(donationId));
                    } catch (Exception e) {
                        logger.error("Erreur lors de la finalisation du don Stripe", e);
                    }
                }
            }
        }

        return ResponseEntity.ok("Received");
    }

    private String readPayload(HttpServletRequest request) {
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                payload.append(line);
            }
        } catch (IOException e) {
            logger.error("Erreur lecture payload webhook Stripe", e);
            return null;
        }
        return payload.toString();
    }

    public static class CheckoutRequest {
        private Double amount;
        private Long userId;
        private Long actionId;

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getActionId() {
            return actionId;
        }

        public void setActionId(Long actionId) {
            this.actionId = actionId;
        }

        public com.example.demo.entities.User getUser() {
            if (this.userId == null) return null;
            com.example.demo.entities.User user = new com.example.demo.entities.User();
            user.setId(this.userId);
            return user;
        }

        public com.example.demo.entities.Action getAction() {
            if (this.actionId == null) return null;
            com.example.demo.entities.Action action = new com.example.demo.entities.Action();
            action.setId(this.actionId);
            return action;
        }
    }
}
