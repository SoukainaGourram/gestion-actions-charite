# 📌 Gestion des Actions de Charité

Application web (backend Java / frontend Thymeleaf) pour la gestion d'actions caritatives, la collecte de dons et la coordination entre organisations et donateurs.

**Objectif** : fournir une plateforme simple, sécurisée et extensible pour créer/consulter des actions, recevoir des dons (Stripe) et administrer les campagnes.

---

**Principales fonctionnalités**
- Gestion des utilisateurs (inscription, authentification, profils, rôles)
- Gestion des organisations et des actions (création, modification, images)
- Paiements sécurisés (Stripe)
- Tableau de bord administrateur
- Notifications email
- Multilingue (Français / Arabe)

---

**Architecture & structure**

- Backend : Spring Boot (Maven)
- Frontend : Thymeleaf templates (servis par le backend)
- DB production : PostgreSQL (H2 pour tests)
- Conteneurisation : Docker + docker-compose

Arborescence (extrait) :

gestion-actions-charite/
- backend/                   (service Spring Boot principal)
- frontend/                  (templates, assets statiques)
- demo/                      (exemples / modules)
- docker-compose.yml        

---

**Prérequis**
- Java 17+ (ou JDK 21 recommandé)
- Maven
- Docker & Docker Compose (pour exécuter via conteneurs)

---

**Démarrage rapide (Docker)**

1. Construire et lancer tous les services (depuis la racine du projet) :

```bash
docker-compose up --build
```

2. L'application sera disponible sur http://localhost:8080 (selon configuration)

---

**Développement local (backend)**

1. Depuis le dossier `backend` :

```bash
./mvnw spring-boot:run
# ou
mvn -f backend/pom.xml spring-boot:run
```

2. Templates frontend : les fichiers sous `frontend/templates` sont copiés lors du build vers `target/classes/templates`.

---

**Variables d'environnement importantes**
- `SPRING_PROFILES_ACTIVE` : profil Spring (dev, prod)
- `SPRING_DATASOURCE_URL` / `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD`
- `STRIPE_API_KEY` : clé secrète Stripe
- `SPRING_MAIL_HOST`, `SPRING_MAIL_PORT`, `SPRING_MAIL_USERNAME`, `SPRING_MAIL_PASSWORD`
- `OAUTH2_CLIENT_ID`, `OAUTH2_CLIENT_SECRET` (si OAuth activé)

Ajoutez ces variables dans votre `.env` ou dans la configuration Docker Compose.

---

**Base de données**
- En développement, le projet utilise H2 (scope test). En production, configurez PostgreSQL et mettez à jour `application.properties` ou vos variables d'environnement.

---

**Tests**

Exécuter les tests unitaires depuis le dossier backend :

```bash
./mvnw test
```

---

**Build & packaging**

```bash
./mvnw -DskipTests package
```

Pour construire une image Docker (exemple) :

```bash
docker build -t gestion-actions-charite-backend -f backend/Dockerfile .
```

---

**Contribuer**

- Forkez le dépôt
- Créez une branche feature/bugfix
- Ouvrez une pull request claire avec description et captures si nécessaire

---

**Aide / Dépannage**

- Si la compilation échoue, vérifiez le `pom.xml` pour conflits de merge et l'encodage UTF-8.
- Pour des problèmes liés à Docker, exécutez `docker-compose logs -f` pour suivre les logs des services.

---


