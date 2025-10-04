# JO PARIS 2024 - Ticket Platform

[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/build-with-spring-boot.svg)](https://forthebadge.com) [![forthebadge](https://forthebadge.com/images/badges/made-with-javascript.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-html_css.svg)](https://forthebadge.com)

Cette application est destinée à la vente de tickets pour assister à des évènements sportifs pendant les Jeux Olympiques d'été de Paris 2024.

\_Code réalisé dans le cadre d'un Bachelor Développeur d'application JAVA, avec [STUDI](https://www.studi.com/fr)

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

Ce qu'il est requis pour commencer avec votre projet...

-   Programme 1
-   Programme 2
-   etc...

## Fabriqué avec

Programmes, logiciels et ressources utilisés pour développer le projet :

_Outils de Développement :_

-   [Visual Studio Code](https://code.visualstudio.com/) - Editeur de textes
-   [tailwind.css](https://tailwindcss.com) - Framework CSS (front-end)
-   [AlwaysData](https://www.alwaysdata.com/fr/) - Base de donnée de développement
-   [Stripe](https://stripe.com) - Plateforme de paiement en ligne

_Frameworks Utilisés :_

-   [SpringBoot]
-   [SpringBoot Security]
-   [JPA Repository] -

_Déploiement :_

-   [Heroku](https://www.heroku.com/)

## Base de données :

MySQL ou MariaDB peuvent être utilisés pour ce projet.
The database connection can be configured in the application.properties file, with the appropriate values for the following properties: <br>

spring.datasource.name=[Your Database Name] <br>
spring.datasource.url=jdbc:mysql://localhost:3306/[Your Database Name] <br>
spring.datasource.password=[Your password] <br>
spring.datasource.username=[Your username] <br>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver <br>
spring.jpa.hibernate.ddl-auto=update <br>
server.port=2330[Optional] <br>

## Installation :

1. Clone the repository : $ git clone https://github.com/SuhasKamate/Business_Management_Project.git <br>

2. Import the project inside STS/Eclipse : <br>
    - Open STS/Eclipse > file > import > maven > existing project > browse > finish . <br>
3. Make sure you are in the Business_Management_Project directory. <br>

![packageExplorer](https://github.com/SuhasKamate/Business_Management_Project/assets/126138738/3ea1eb7f-8e49-4b76-96e4-798b6b8e8715)

4.Configure the database connection is application.properties (check the Database section for more information). <br>

5.Run the project (by running main method is BusinessProjectApplication.java) OR right clink on the project > Run As > Spring Boot App. <br>

6.Open http://localhost:2330/home in any browser. <br>

7.Now your tables will be created in the databse. <br>

-   You have to add one admin data manually to login as admin, So add one admin data. <br>

## Versions

Liste des versions :

**Dernière version stable :** 1.0
**Dernière version :** 1.0

## Auteurs

Listez le(s) auteur(s) du projet ici !

-   **Philippe CHABERT** _alias_ [@prozakk33](https://github.com/prozakk33)

## License

Ce projet est sous licence `exemple: WTFTPL` - voir le fichier [LICENSE.md](LICENSE.md) pour plus d'informations
