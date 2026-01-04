# 📘 Service Layer – Technische Documentatie (EhbRenting)

Dit document beschrijft **uitgebreid** de werking van de **service-laag** binnen het EhbRenting-project.  
De service-laag vormt de **functionele kern** van de applicatie en bevat **alle businesslogica**.

Controllers, repositories en eenvoudige CRUD-operaties worden bewust **niet** gedocumenteerd.  
De focus ligt op:
- complexe methoden
- belangrijke flows
- design- en architectuurkeuzes

---

## 🧠 Algemene ontwerpfilosofie

### Thin Controllers – Fat Services
Controllers zijn beperkt tot:
- HTTP-afhandeling
- validatiebinding (`@Valid`)
- doorgeven van parameters
- teruggeven van views of redirects

**Alle beslissingen en logica bevinden zich in services.**

### Separation of Concerns
Elke service heeft een **duidelijk afgebakende verantwoordelijkheid**:
- geen overlap
- geen duplicatie
- geen verborgen logica

### Exception-driven flow
Services:
- gooien exceptions
- vangen deze niet op

Foutafhandeling gebeurt **centraal** via een `GlobalExceptionHandler`.

---

## 🛒 CartService

### Rol binnen de applicatie
De `CartService` fungeert als **coördinerende service** voor winkelmandje-acties.  
De service bevat zelf geen persistente entiteiten, maar stuurt de reservatieflow aan via andere services.

### Waarom een aparte CartService?
Hoewel reservaties worden opgeslagen via `RentalService`, werd een aparte CartService voorzien om:
- verantwoordelijkheden te scheiden
- controllers eenvoudiger te houden
- winkelmandje-acties logisch te groeperen

---

### Toevoegen aan winkelmandje

**Conceptueel verloop**
1. Ontvangt gebruikerscontext en input
2. Delegeert beschikbaarheidscontrole
3. Start reservatieflow
4. Laat fouten bubbelen via exceptions

**Belangrijke ontwerpkeuze**
De CartService:
- valideert niets zelf
- berekent niets
- beslist niets definitief

Ze **orkestreert**, maar **bezit geen domeinlogica**.

---

## 📦 EquipmentAvailabilityService

### Centrale verantwoordelijkheid
Deze service is verantwoordelijk voor **alle logica rond beschikbaarheid van materiaal**.

### Waarom deze service cruciaal is
Beschikbaarheidslogica:
- is niet triviaal
- bevat datumoverlappingen
- wordt op meerdere plaatsen gebruikt

Zonder centralisatie zou dit leiden tot:
- duplicatie
- inconsistent gedrag
- fouten bij uitbreidingen

---

### Overlappende periodes

De service controleert:
- bestaande reservaties
- overlapping tussen huurperiodes
- cumulatieve aantallen

Een reservatie overlapt wanneer:
- startdatum vóór einddatum van een andere reservatie ligt
- én einddatum na startdatum van een andere reservatie ligt

Deze logica is bewust **gecentraliseerd** en **herbruikbaar**.

---

### Validatie vs berekening

De service onderscheidt:
- **berekening** van beschikbaarheid (voor UI)
- **validatie** van aanvragen (voor businesslogica)

Dit voorkomt:
- foutieve aannames
- dubbele berekeningen
- UI-afhankelijke beslissingen in services

---

## 📚 EquipmentService

### Verantwoordelijkheid
De `EquipmentService` beheert **alle catalogus- en materiaalgerelateerde logica**.

### Waarom niet in controllers?
Cataloguslogica bevat:
- filters
- zoekopdrachten
- paginatie
- statuscontrole (actief/inactief)

Controllers mogen **geen beslissingen nemen** over:
- welke query wordt uitgevoerd
- welke data zichtbaar is

---

### Catalogusselectie

De service bepaalt:
- of gezocht wordt op naam
- of gefilterd wordt op categorie
- of standaardweergave wordt gebruikt

Dit zorgt voor:
- één centrale waarheid
- consistente catalogusweergave
- eenvoudiger onderhoud

---

### Paginatieberekening

De `calculatePageRange`-logica bepaalt:
- welke paginanummers zichtbaar zijn
- hoe breed de navigatie is
- randgevallen (eerste/laatste pagina)

**Waarom in de service?**
- UI-logica ≠ controllerlogica
- controllers mogen geen berekeningen doen
- herbruikbaar voor andere views

---

## 📅 RentalService

### Kernservice van de applicatie
De `RentalService` beheert:
- winkelmandje
- reservaties
- statusovergangen
- checkout

Deze service vormt het **functionele hart** van EhbRenting.

---

### Ontwerpkeuze: Persistent winkelmandje

In plaats van een sessiegebaseerde cart werd gekozen voor:
- persistente `Rental`-records
- statusgebaseerde flow

**Statusmodel**
- `IN_CART` → tijdelijk
- `CONFIRMED` → definitief

---

### Voordelen van dit model
- Geen verlies bij sessieverval
- Transparante reservatiegeschiedenis
- Realistisch verhuurmodel
- Eenvoudig uitbreidbaar (bv. annulaties)

---

### Checkout-flow

1. Ophalen van alle `IN_CART` reservaties
2. Herverifiëren van beschikbaarheid
3. Transactionele statuswijziging
4. Persistente opslag

**Waarom herverifiëren?**
- Voorkomt race conditions
- Garandeert correcte beschikbaarheid
- Verhoogt dataconsistentie

---

### Transactionele grenzen

Checkout-methodes zijn `@Transactional` omdat:
- meerdere updates samen moeten slagen
- gedeeltelijke bevestiging onaanvaardbaar is
- rollback noodzakelijk is bij fouten

---

## 👤 UserService

### Rol
Beheert **alle gebruikersgerelateerde businesslogica** voor reguliere gebruikers.

### Profielbeheer
- Ophalen van profielinformatie via DTO’s
- Validatie van invoer
- Geen directe entity-manipulatie vanuit controllers

---

### Wachtwoordwijziging

De service:
- valideert het huidige wachtwoord
- controleert consistentie van invoer
- gebruikt BCrypt voor hashing
- voorkomt hergebruik van ongeldige data

**Waarom in service?**
- Security hoort niet in controllers
- Controllers mogen geen hashing kennen
- Centralisatie verhoogt veiligheid

---

## 👥 AdminUserService

### Waarom aparte service?
Admin-logica is:
- gevoeliger
- krachtiger
- gevaarlijker bij fouten

Daarom is ze gescheiden van `UserService`.

---

### Functionaliteit
- Gebruikersbeheer
- Roltoekenning
- Accountbeheer

Deze scheiding:
- verhoogt veiligheid
- voorkomt fout gebruik
- maakt auditing eenvoudiger

---

## 🔐 CustomUserDetailsService

### Rol binnen Spring Security
Deze service vormt de **integratielaag** tussen:
- Spring Security
- het eigen User-domein

### Werking
- Laadt gebruiker via repository
- Converteert naar `UserDetails`
- Levert rollen aan security-context

---

### Waarom deze service noodzakelijk is
Spring Security:
- kent het domeinmodel niet
- verwacht specifieke interfaces

Deze service voorkomt:
- securitylogica in UserService
- afhankelijkheden tussen lagen

---

## ⚠️ Exception Handling (Service-niveau)

### Filosofie
Services:
- detecteren fouten
- gooien exceptions
- handelen ze niet af

---

### Centrale afhandeling
Een `GlobalExceptionHandler`:
- vangt exceptions op
- bepaalt gebruikersfeedback
- houdt controllers schoon

---

## 🧩 Samenvattende principes

- Businesslogica zit uitsluitend in services
- Controllers zijn passief
- Statusgebaseerde domeinmodellen
- Centrale validatie en beschikbaarheid
- Security en admin-logica strikt gescheiden
- Exception-driven control flow

---

## 🎓 Verdedigingswaarde

Deze service-architectuur:
- volgt Spring best practices
- sluit aan bij enterprise-ontwerp
- is uitbreidbaar en onderhoudbaar
- is mondeling perfect verdedigbaar
