package com.pchab.JoParis2024.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // Charger les variables d'environnement depuis le fichier .env
        Dotenv dotenv = Dotenv.configure().load();

        // Ajouter les variables au système
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        System.out.println("Variables d'environnement chargées depuis .env");
    }
}
