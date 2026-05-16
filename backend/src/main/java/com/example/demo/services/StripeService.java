package com.example.demo.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;

@Service
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${app.base-url:http://localhost:8081}")
    private String appBaseUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createCheckoutSession(Long donationId, Double amount, String currency, String successUrl, String cancelUrl, String description) throws StripeException {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }

        if (currency == null || currency.isBlank()) {
            currency = "mad";
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("donationId", String.valueOf(donationId));
        metadata.put("description", description != null ? description : "Don Stripe");

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(description != null ? description : "Don pour une action")
                .build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(currency)
                .setUnitAmount((long) Math.round(amount * 100))
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl != null ? successUrl : appBaseUrl + "/actions?donationSuccess=true")
                .setCancelUrl(cancelUrl != null ? cancelUrl : appBaseUrl + "/actions?donationCancelled=true")
                .addLineItem(lineItem)
                .putAllMetadata(metadata)
                .build();

        return Session.create(params);
    }
}
