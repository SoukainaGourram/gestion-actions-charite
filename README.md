# 📌 Application de Gestion des Actions de Charité

Application web développée avec **Spring Boot** permettant la gestion et le suivi des actions de charité.  
La plateforme connecte les organisations caritatives et les donateurs afin de faciliter les collectes de fonds, la participation aux événements solidaires et la gestion des campagnes humanitaires.

---

# ✨ Fonctionnalités principales

## 👤 Gestion des utilisateurs

- Inscription / Connexion
- Authentification sécurisée avec Spring Security
- Gestion du profil utilisateur
- Historique des dons
- Gestion des rôles :
  - USER
  - ORGANIZATION
  - ADMIN

---

## 🏢 Gestion des organisations

- Création de profils organisations
- Validation par administrateur
- Gestion des informations :
  - logo
  - description
  - coordonnées
- Création et gestion des actions de charité

---

## ❤️ Gestion des actions de charité

- Création d’actions
- Modification et archivage
- Upload d’images et médias
- Gestion :
  - titre
  - description
  - lieu
  - date
  - objectif de collecte
  - montant collecté

---

## 🔍 Exploration des initiatives

- Recherche par catégories :
  - Éducation
  - Santé
  - Environnement
  - Aide sociale
- Filtrage et recommandations

---

## 💳 Dons et participation

- Participation aux actions
- Paiement sécurisé via Stripe / PayPal
- Suivi des contributions
- Progression des collectes en temps réel

---

## 🌍 Fonctionnalités supplémentaires

- Multilingue : Français / Arabe
- Notifications par email
- Tableau de bord administrateur
- Responsive Design

---

# 🛠️ Technologies utilisées

## Backend

- Java 17
- Spring Boot 3
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

---

## Frontend

- Thymeleaf
- HTML5 / CSS3
- Bootstrap / Tailwind CSS
- JavaScript

---

## Base de données

- PostgreSQL / MySQL
- MongoDB
- H2 Database (développement)

---

## Outils

- IntelliJ IDEA
- Postman
- Git & GitHub
- Docker
- Docker Compose

---

# 🏗️ Architecture du projet

```bash
gestion-actions-charite/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   ├── resources/
│   │   │   ├── templates/
│   │   │   ├── static/
│   │   │   └── application.properties
│   │
│   └── test/
│
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
