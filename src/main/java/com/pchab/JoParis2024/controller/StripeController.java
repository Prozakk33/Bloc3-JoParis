package com.pchab.JoParis2024.controller;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StripeController {

    // Clé secrète Stripe (test)
    private static final String STRIPE_SECRET_KEY = "sk_test_51SBl62Dtl7ori9Lm9tfgr9pc6nWT8ArZkAz6iadDAio23olrfprcjIitg9VAQlNINLW17uULGqBOlSA9OlrkRzN600gJD3Vblz";

    public StripeController() {
        // Initialiser Stripe avec la clé secrète
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    @PostMapping("/payment")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> requestData) {
        try {
            // Domaine de votre application
            String YOUR_DOMAIN = "http://localhost:8080";

            // Récupérer les informations du produit depuis la requête (si nécessaire)
            String priceId = (String) requestData.get("priceId");
            Long quantity = Long.valueOf((Integer) requestData.get("quantity"));

            // Créer les paramètres de la session Stripe Checkout
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(YOUR_DOMAIN + "/success.html")
                    .setCancelUrl(YOUR_DOMAIN + "/shoppingCart.html?error=Paiement échoué, veuillez réessayer.")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(quantity)
                                    .setPrice(priceId) // ID du prix défini dans Stripe
                                    .build()
                    )
                    .build();

            // Créer la session Stripe
            Session session = Session.create(params);

            // Retourner l'URL de la session Stripe Checkout
            return Map.of("url", session.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de la session Stripe : " + e.getMessage());
        }
    }
}