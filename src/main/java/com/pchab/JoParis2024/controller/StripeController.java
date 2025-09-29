package com.pchab.JoParis2024.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pchab.JoParis2024.security.payload.request.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.validation.Valid;

@RestController
public class StripeController {

    // Clé secrète Stripe (test)
    private static final String STRIPE_SECRET_KEY = "sk_test_51SBl62Dtl7ori9Lm9tfgr9pc6nWT8ArZkAz6iadDAio23olrfprcjIitg9VAQlNINLW17uULGqBOlSA9OlrkRzN600gJD3Vblz";

    public StripeController() {
        // Initialiser Stripe avec la clé secrète
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    @PostMapping("/payment")
    public Map<String, String> createCheckoutSession(@Valid @RequestBody PaymentRequest paymentRequest) {

        System.out.println("Initiating Stripe checkout session creation...");
        System.out.println("PaymentRequest received: " + paymentRequest);
        try {
            System.out.println("Received payment request: " + paymentRequest);
            // Domaine de l'application
            String YOUR_DOMAIN = "http://localhost:8080";

            // Récupérer les informations du produit depuis la requête
            String priceId = paymentRequest.getPriceId();
            Long quantity = paymentRequest.getQuantity();
            Object cart = paymentRequest.getCart();

            System.out.println("Creating checkout session with priceId: " + priceId + " and quantity: " + quantity);

            // Encoder le message d'erreur pour l'URL
            String errorMessage = "Paiement échoué, veuillez réessayer.";
            String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
            //String ticketsString = URLEncoder.encode(tickets.toString(), StandardCharsets.UTF_8);

            // Créer les paramètres de la session Stripe Checkout
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(YOUR_DOMAIN + "/payment-success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(YOUR_DOMAIN + "/shoppingCart.html?error=" + encodedErrorMessage)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(quantity)
                                    .setPrice(priceId)
                                    .build()
                    )
                    .putMetadata("cart", cart.toString())
                    .build();

            // Créer la session Stripe
            Session session = Session.create(params);

            // Retourner l'URL de la session Stripe Checkout
            System.out.println("Checkout session created successfully: " + session.getUrl());

            System.out.println("MAP  = " + Map.of("url", session.getUrl()));
            
            return Map.of("url", session.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la session Stripe : " + e.getMessage());
        }
    }

    @GetMapping("/payment-success")
    public String handlePaymentSuccess(@RequestParam("session_id") String sessionId) {

        System.out.println("Handling payment success for session ID: " + sessionId);
        try {
            // Récupérer les détails de la session Stripe
            Session session = Session.retrieve(sessionId);

            // Traiter les informations de la session
            System.out.println("Paiement réussi pour la session : " + sessionId);
            System.out.println("Détails de la session : " + session);

            // Récupérer les métadonnées
            Map<String, String> metadata = session.getMetadata();
            String cartJson = metadata.get("cart");

            System.out.println("Panier (JSON) : " + cartJson);

            // Exemple : enregistrer les informations dans la base de données
            // savePaymentDetails(session);

            return "Paiement traité avec succès !";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du traitement du paiement : " + e.getMessage());
        }
    }


}