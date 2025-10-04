# JO PARIS 2024 - Ticket Platform

[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/build-with-spring-boot.svg)](https://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/made-with-javascript.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-html_css.svg)](https://forthebadge.com)

**Cette application est destinée à la vente de tickets pour assister à des évènements sportifs pendant les Jeux Olympiques d'été de Paris 2024.**

_Code réalisé dans le cadre d'un Bachelor Développeur d'application JAVA, avec [STUDI](https://www.studi.com/fr)_

## Fonctionnalités :

-   **Gestion Utilisateurs**: Inscription des visiteurs, création de compte, connexion.
-   **Gestion des Evènements**: Création, modification et affichage détaillé de tous les évènements sportifs.
-   **Gestion des Tickets**: Création de tickets lors de l'achat, affichage sous forme de QR Code.
-   **Accès selon le role**: Définition de role, et gestion des accès aux différentes fonctionnalités.
-   **Affichage Dynamique**: Utilisation de Javascript pour l'affichage dynamique du site.
-   **Base de doonées**: Base de données MariaDB pour l'enregistrement des données.
-   **Sécurité** : Utilisation de JWToken pour la sécurisation des accès à l'API.

## Pour commencer

Entrez ici les instructions pour bien débuter avec votre projet...

### Pré-requis

Pour faire fonctionner ce projet sur votre machine, vous aurez besoin de :

-   Java 17 ou plus
-   Maven

### Installation

1 - Cloner le dépot GitHub
`git clone https://github.com/Prozakk33/Bloc3-JoParis.git`

2 - Créer une base de données MariaDB sur [AlwaysData](https://www.alwaysdata.com/fr/)

3 - Créer un compte sur [Stripe](https://stripe.com)

4 - Modifiez les variables dans le fichier src/main/ressources/application.yml
`database:`\
`url: jdbc:your_db_adress`\
`password: your_password`\
`username: your_username`\

`Sripe:`\
`secretKey: your_stripe_secret_key`\
`publicKey: your_stripe_public_key`\

5 - Construisez le projet avec Maven
`mvn clean install`

6 - Ouvrez votre navigateur internet et allez à l'adresse : [http://localhost:8080](http://localhost:8080)

## Fabriqué avec

**Langages, programmes, logiciels et ressources utilisés pour développer le projet :**

### Outils

_Langages utilisés :_

-   [JAVA](https://www.java.com/fr/)
-   [HTML](https://developer.mozilla.org/en-US/docs/Web/HTML)
-   [CSS](https://developer.mozilla.org/en-US/docs/Web/CSS)
-   [JavaScript](https://developer.mozilla.org/en-US/docs/Web/JavaScript)

_Outils de Développement :_

-   [Visual Studio Code](https://code.visualstudio.com/) - Editeur de textes

_Outils externes :_

-   [AlwaysData](https://www.alwaysdata.com/fr/) - Base de donnée de développement
-   [Stripe](https://stripe.com) - Plateforme de paiement en ligne

_Frameworks Utilisés :_

-   [tailwind.css](https://tailwindcss.com) - Framework CSS (front-end)
-   [SpringBoot](https://spring.io/projects/spring-boot)
-   [SpringBoot Security](https://spring.io/projects/spring-security) - Framework de sécurité
-   [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/index.html) - Framework de gestion de base de données

_Déploiement :_

-   [Heroku](https://www.heroku.com/)

## Versions

Liste des versions :

**Dernière version stable :** 1.0\
**Dernière version :** 1.0

## Auteurs

Listez le(s) auteur(s) du projet ici !

-   **Philippe CHABERT** _alias_ [@prozakk33](https://github.com/prozakk33)

## License

Ce projet est sous [Licence de libre diffusion](https://creativecommons.org/licenses/by-nc/4.0/)
