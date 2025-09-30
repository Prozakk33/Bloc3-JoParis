package com.pchab.JoParis2024.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pchab.JoParis2024.pojo.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pchab.JoParis2024.security.payload.request.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.validation.Valid;

@RestController
public class StripeController {

    @Autowired
    private TicketController ticketController;

    @Autowired
    private UserController userController;

    // Clé secrète Stripe (test)
    private static final String STRIPE_SECRET_KEY = "sk_test_51SBl62Dtl7ori9Lm9tfgr9pc6nWT8ArZkAz6iadDAio23olrfprcjIitg9VAQlNINLW17uULGqBOlSA9OlrkRzN600gJD3Vblz";

    public StripeController() {
        // Initialiser Stripe avec la clé secrète
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    @PostMapping("/payment")
    public Map<String, String> createCheckoutSession(@Valid @RequestBody PaymentRequest paymentRequest, @RequestHeader (value = "Authorization", required = true) String authorizationHeader) {

        System.out.println("Initiating Stripe checkout session creation...");
        System.out.println("PaymentRequest received: " + paymentRequest);

        // Récupération de l'ID du client (userId)
        ResponseEntity<?> userResponse = userController.getUserFromToken(authorizationHeader);
        User user = (User) userResponse.getBody();
        Long userId = user.getId();
        if (userId == null) {
            throw new RuntimeException("Utilisateur non authentifié. Veuillez vous connecter.");
        } else {
            System.out.println("User authenticated: " + userId);
        }
        System.out.println("User ID for payment: " + userId);


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

            // Préparer les données du panier à inclure dans les métadonnées
            Map<String, Object> cartData = Map.of(
            "userId", userId,
            "cart", cart
            );
            System.out.println("Cart Data (JSON) : " + cartData);
            // Créer les paramètres de la session Stripe Checkout
            // Convertir cartData en JSON valide
            ObjectMapper objectMapper = new ObjectMapper();
            String cartJsonString = objectMapper.writeValueAsString(cartData);
            System.out.println("Cart Data (JSON) : " + cartJsonString);

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
                    .putMetadata("cart", cartJsonString) // Ajouter les métadonnées du panier
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
    public ResponseEntity<Void> handlePaymentSuccess(@RequestParam("session_id") String sessionId) {

        System.out.println("Handling payment success for session ID: " + sessionId);
        try {
            // Récupérer les détails de la session Stripe
            Session session = Session.retrieve(sessionId);

            // Récupérer les métadonnées
            Map<String, String> metadata = session.getMetadata();
            System.out.println("Metadata retrieved: " + metadata);

            String cartJsonString = metadata.get("cart"); // Récupérer la chaîne JSON

            System.out.println("Cart JSON String: " + cartJsonString);

            // Convertir la chaîne JSON en objet Java
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> cartJson = objectMapper.readValue(cartJsonString, Map.class);

            // Extraire le tableau "panier" et "userId"
            Map<String, Object> cart = (Map<String, Object>) cartJson.get("cart");
            List<Map<String, Object>> panier = (List<Map<String, Object>>) cart.get("panier");
            Long userId = Long.valueOf((Integer) cartJson.get("userId") );

            // Afficher les données extraites
            System.out.println("Panier : " + panier);
            System.out.println("User ID : " + userId);

            // Enregistrement des tickets en base de données
            for (Map<String, Object> item : panier) {
                /*/
                System.out.println("Processing item: " + item);
                System.out.println("Item ID: " + item.get("id"));
                System.out.println("Item Type: " + item.get("type"));
                System.out.println("Item Quantity: " + item.get("quantite"));
                */
                Long eventId = Long.parseLong((String) item.get("id"));
                String ticketType = (String) item.get("type");
                Integer quantite = ((Number) item.get("quantite")).intValue();

                // Créer un ticket pour chaque élément du panier
                for (int i = 0; i < quantite; i++) {
                    // Récupérer la date et l'heure actuelles
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    //System.out.println("Current Timestamp: " + currentTimestamp);
                    ticketController.createTicket(userId, eventId, ticketType, currentTimestamp);
                }
            }
            // Rediriger vers la page HTML dans le dossier static
        return ResponseEntity.status(302).header("Location", "/success.html").build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du traitement du paiement : " + e.getMessage());
        }
    }
}