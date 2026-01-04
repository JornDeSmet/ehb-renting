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
- Java 25  
- Spring Boot 3.5.7  
- Spring MVC  
- Spring Data JPA  
- Spring Security  
- Hibernate  
- BCrypt Password Encoder  

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

#### 🔐 Authenticatie
- Registratie van nieuwe gebruikers  
- Inloggen en uitloggen  
- Wachtwoorden veilig gehashed met **BCrypt**

#### 📦 Catalogus
- Overzicht van alle actieve materialen  
- Filteren op categorie  
- Zoeken op naam  
- Detailpagina per product met beschikbaarheidsinformatie  

#### 🛒 Winkelmandje
- Producten toevoegen aan een winkelmandje  
- Winkelmandje gekoppeld aan de gebruiker  
- Validatie van beschikbaarheid per periode  

#### 📅 Reservatie & Checkout
- Selecteren van een huurperiode  
- Bevestigen van een reservatie  
- Reservaties krijgen de status `IN_CART` of `CONFIRMED`  

#### 👤 Gebruikersprofiel
- Inzien en aanpassen van profielgegevens  
- Wachtwoord wijzigen  
- Overzicht van eigen reservaties  

---

### 🛠️ Beheerdersfunctionaliteiten (Admin)

#### 🧰 Materiaalbeheer
- Toevoegen van nieuw materiaal  
- Bewerken van bestaand materiaal  
- Verwijderen van materiaal  
- Beheer van categorieën  
- Activeren en deactiveren van materiaal  

#### 📋 Reservatiebeheer
- Overzicht van alle reservaties  
- Aanpassen van reservatiestatus  
- Inzicht in beschikbaarheid van materiaal  

#### 👥 Gebruikersbeheer
- Overzicht van alle gebruikers  
- Aanmaken van nieuwe gebruikers  
- Bewerken van gebruikersgegevens  
- Toekennen of wijzigen van gebruikersrollen (`USER`, `ADMIN`)  


---

### 🧩 Architectuurlagen

#### 🔹 Controller layer (`controller`)
- Verwerkt HTTP-requests
- Bevat **geen businesslogica**
- Roept uitsluitend services aan
- Verantwoordelijk voor model → view binding

#### 🔹 Service layer (`service`)
- Bevat alle **businesslogica**
- Afhandeling van:
  - reservaties
  - winkelmandje
  - beschikbaarheid
  - gebruikersbeheer
- Gebruikt helper-methodes om verantwoordelijkheden te scheiden
- Transactionele logica via `@Transactional`

#### 🔹 Repository layer (`repository`)
- Data-access via **Spring Data JPA**
- Geen businesslogica
- Communiceert rechtstreeks met de database

#### 🔹 Model layer (`model`)
- JPA-entities
- Domeinrepresentatie van de applicatie
- Bevat geen presentatie- of controllerlogica

#### 🔹 DTO layer (`dto`)
- Data Transfer Objects voor veilige communicatie
- Beschermen interne entities
- Worden gebruikt tussen controller ↔ service ↔ view

#### 🔹 Mapper layer (`mapper`)
- Verantwoordelijk voor conversie tussen entities en DTO’s
- Houdt mappinglogica uit services en controllers

#### 🔹 Exception layer (`exceptions`)
- Custom domein-exceptions
- Centrale foutafhandeling via `GlobalExceptionHandler`
- Controllers bevatten geen `try/catch`

#### 🔹 Config layer (`config`)
- Spring Security configuratie
- Web- en applicatieconfiguratie
- Routing en toegangscontrole

---

## 🔐 Authenticatie & Security

- Spring Security wordt gebruikt voor authenticatie en autorisatie
- Rollen:
  - `USER` – standaard gebruiker
  - `ADMIN` – beheerder
- Wachtwoorden worden **gehashed met BCrypt**
- Beveiligde routes voor admin-functionaliteit

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
  - reservaties

---

## ▶️ Gebruik

- Start de applicatie.
- Open je browser en ga naar:  
  `http://localhost:8080`
  
### Testgebruiker

Gebruiker: student1

Wachtwoord: password

### Admin gebruiker

Gebruiker: admin

Wachtwoord: admin123

---

## 📄 Documentatie

Naast deze README werd **aparte technische documentatie** voorzien, conform de opdrachtvereisten.

📁 **Bestand:** `DOCUMENTATION_SERVICES.md`

Deze documentatie focust specifiek op de **service-laag**, aangezien daar de kernlogica van de applicatie is geïmplementeerd.

### De documentatie bevat onder andere:
- Gedetailleerde uitleg van de **winkelmandje- en reservatieflow**
- Toelichting bij de **beschikbaarheidsberekening**
- Beschrijving van de **statusgebaseerde reservatielogica** (`IN_CART` → `CONFIRMED`)
- Uitleg bij **complexe service-methoden**
- Motivatie van belangrijke **architecturale en designkeuzes**
- Toelichting bij **exception handling** en foutafhandeling

Eenvoudige methoden zoals getters, setters en standaard CRUD-operaties worden bewust **niet** toegelicht.

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
- Chatgpt  
  https://chatgpt.com/share/695ade5f-6bd4-8002-93c7-7113e0031542  
  https://chatgpt.com/share/695ade88-3a54-8002-bae8-a40ca8f9c4ce
