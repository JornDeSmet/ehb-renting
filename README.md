# EhbRenting – Backend Web Project (Spring Boot)

## 📌 Projectbeschrijving
EhbRenting is een **Java Spring Boot webapplicatie** ontwikkeld als **proof of concept** voor een kunstopleiding.  
De applicatie laat studenten toe om **materiaal te reserveren en huren** voor hun projecten en eindwerken.

Het platform beheert een **beperkte maar diverse catalogus** van materialen zoals:
- belichting
- kabels
- controlepanelen
- podium- en technische accessoires

Geregistreerde gebruikers kunnen materiaal selecteren voor een **specifieke periode**, toevoegen aan een **winkelmandje** en hun reservatie bevestigen.  
Beheerders kunnen het materiaal en de reservaties opvolgen en beheren.

---

## 🛠️ Technologieën

### Backend
- Java 17  
- Spring Boot  
- Spring MVC  
- Spring Data JPA  
- Spring Security  
- BCrypt Password Encoder  
- Hibernate  

### Frontend
- Thymeleaf  
- HTML5  
- CSS

### Ontwikkeltools
- IntelliJ IDEA / VS Code  
- Maven  
- H2 / MySQL  

---

## 📚 Inhoudsopgave
- Functionaliteiten
- Architectuur
- Authenticatie & Security
- Winkelmandje & Reservaties
- Installatieproces
- Database & Seeding
- Gebruik
- Documentatie
- AI-gebruik
- Bronnen

---

## ✅ Functionaliteiten

### 👤 Gebruikersfunctionaliteiten

#### Authenticatie
- Registreren van nieuwe gebruikers
- Inloggen en uitloggen
- Wachtwoorden veilig gehashed met BCrypt

#### Catalogus
- Overzicht van alle beschikbare materialen
- Filteren op categorie (bv. kabels, belichting, panelen)
- Detailpagina per product

#### Winkelmandje
- Producten toevoegen aan een winkelmandje
- Aantallen aanpassen of items verwijderen
- Winkelmandje gekoppeld aan de gebruikerssessie

#### Reservatie & Checkout
- Selecteren van een huurperiode
- Bevestigen van een reservatie
- Overzicht van gereserveerd materiaal

#### Gebruikersprofiel
- Inzien van persoonlijke gegevens
- Overzicht van gemaakte reservaties

---

### 🛠️ Beheerdersfunctionaliteiten (Admin)

#### Materiaalbeheer
- Toevoegen van nieuw materiaal
- Bewerken van bestaand materiaal
- Verwijderen van materiaal
- Beheer van categorieën

#### Reservatiebeheer
- Overzicht van alle reservaties
- Aanpassen van reservatiestatus
- Opvolgen van beschikbaarheid

---

## 🧱 Architectuur

De applicatie volgt een **klassieke gelaagde architectuur**:

- **Controller layer**  
  Verwerkt HTTP-requests en verbindt frontend met backend.

- **Service layer**  
  Bevat alle businesslogica (winkelmandje, reservaties, gebruikers).

- **Repository layer**  
  Data-access via Spring Data JPA.

- **DTO’s**  
  Beschermen de interne datamodellen en zorgen voor veilige data-overdracht.

Deze aanpak zorgt voor:
- Onderhoudbare code
- Duidelijke verantwoordelijkheden
- Betere testbaarheid en beveiliging

---

## 🔐 Authenticatie & Security

- Spring Security wordt gebruikt voor authenticatie en autorisatie
- Rollen:
  - `USER` – standaard gebruiker
  - `ADMIN` – beheerder
- Wachtwoorden worden **gehashed met BCrypt**
- Beveiligde routes voor admin-functionaliteit

---

## 🛒 Winkelmandje & Reservaties

- Elke ingelogde gebruiker heeft een **sessiegebaseerd winkelmandje**
- Producten worden tijdelijk opgeslagen in de sessie
- Bij checkout wordt de inhoud van het winkelmandje:
  - gevalideerd
  - omgezet naar een reservatie
  - gekoppeld aan de gebruiker

---

## ⚙️ Installatieproces

### 1. Repository clonen
```bash
git clone https://github.com/jouw-repo/ehbrenting.git
cd ehbrenting
```
### 2. Project bouwen
```bash
mvn clean install
```
### 3. Applicatie starten
```bash
mvn spring-boot:run
```

---

## 🗄️ Database & Seeding

- De applicatie gebruikt **JPA/Hibernate** voor database-interacties.
- Databaseconfiguratie gebeurt via `application.properties`.
- Bij het opstarten worden standaard testgegevens aangemaakt, waaronder:
  - materiaal
  - categorieën
  - gebruikers

---

## ▶️ Gebruik

- Start de applicatie.
- Open je browser en ga naar:  
  `http://localhost:8080`
  
### Testgebruiker

Gebruiker: user@test.be

Wachtwoord: password

### Admin gebruiker

Gebruiker: admin@test.be

Wachtwoord: admin123

---

## 📄 Documentatie
Deze documentatie bevat:
- Uitleg over de werking van het winkelmandje
- De volledige reservatieflow van winkelmandje tot bevestiging
- Toelichting bij de securityconfiguratie
- Motivatie van belangrijke designkeuzes

---

## 🤖 Gebruik van AI-tools

Tijdens de ontwikkeling van dit project werd **ChatGPT** gebruikt voor:
- Uitleg en ondersteuning bij Spring Boot en Spring Security
- Architecturale feedback en best practices
- Hulp bij het schrijven van documentatie
- Suggesties voor refactoring en optimalisatie

Alle gegenereerde suggesties werden kritisch geëvalueerd en volledig begrepen.

---

## 📚 Bronnen

- Spring Boot  
  https://spring.io/projects/spring-boot
- Spring Security  
  https://spring.io/projects/spring-security
- Thymeleaf  
  https://www.thymeleaf.org
