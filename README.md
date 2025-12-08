# 🚗 Bilen's Ven

Bilen's Ven er et 3. semester eksamensprojekt, hvor vi udvikler et moderne websystem til et autoværksted i Ishøj. Systemet skal håndtere services/ydelser, medarbejdere, og kontakthenvendelser digitalt.

## 📌 Projektbeskrivelse

Bilen's Ven er et lokalt autoværksted der tilbyder forskellige service-ydelser som olieskift, bremseskift, dækskift, serviceeftersyn m.m. I dag foregår administration primært manuelt, hvilket er ineffektivt. Med det nye system kan både kunder og medarbejdere interagere med værkstedet digitalt.

### Forretningskontekst

**Kunder kan:**
- Se tilgængelige services med priser
- Læse om medarbejdere og deres specialer
- Sende kontakthenvendelser via formular
- Få overblik over værkstedets ydelser

**Medarbejdere (Admin) kan:**
- Oprette, redigere og slette services
- Administrere medarbejder-profiler
- Se og behandle kontakthenvendelser

**Service-data indeholder:**
- Navn (unik)
- Beskrivelse
- Pris (DKK)
- Status (aktiv/inaktiv)
- Billede URL
- Timestamps (oprettet/opdateret)

## ⚙️ Teknologi & Arkitektur

### Tech Stack
```
Frontend:  HTML, CSS, JavaScript
Backend:   Spring Boot 3.4 (Java 24)
Database:  MySQL 
Container: Docker & Docker Compose
CI/CD:     GitHub Actions
```

### Arkitektur Pattern
```
Layered Architecture:
├── config       ✅ Configuration classes
├── controller   ✅ REST API endpoints
├── dto          ✅ Data Transfer Objects
├── exception    ✅ Custom exceptions
├── model        ✅ JPA Entities
├── repository   ✅ Database access
└── service      ✅ Business logic
```

### Design Patterns
- **DTO Pattern**: Separation mellem API og database layer
- **Repository Pattern**: Spring Data JPA repositories
- **Dependency Injection**: Constructor-based med Lombok
- **Exception Handling**: Global @RestControllerAdvice
- **Soft Delete**: Active flag i stedet for hard delete

### Database Model
```
Entities:
├── BaseEntity          ✅ Audit fields (super class)
├── ContactMessage      ✅ Kontaktbeskeder fra kunder
├── Employee            ✅ Medarbejder-profiler
├── Role                ✅ Enum for brugerroller
├── Service             ✅ Værkstedets ydelser
└── User                ✅ Admin login
```

## 🐳 Docker Setup

### Quick Start
```bash
# Clone repository
git clone https://github.com/JuliusLystre/bilensven.git
cd bilensven

# Start systemet (bygger + starter containers)
docker compose up -d --build

# Vent på MySQL health check (~10 sekunder)
docker compose logs -f mysql

# Åbn i browser
open http://localhost:8080
```

### Adgang

**Frontend:**
- Website: `http://localhost:8080`
- Admin Panel: `http://localhost:8080/admin.html`
    - Username: `admin`
    - Password: `bilensven2024`

**Database:**
- Port: `3308` (mapped fra 3306)
- Database: `bilensven`
- Username: `bilensven_user`
- Password: `secure_password_123`

**API Endpoints:**
```
GET    /api/services              # Hent alle services
GET    /api/services/{id}         # Hent specifik service
POST   /api/services              # Opret service
PUT    /api/services/{id}         # Opdater service
DELETE /api/services/{id}         # Slet service (soft)

GET    /api/employees             # Hent alle medarbejdere
GET    /api/employees/{id}        # Hent specifik medarbejder
POST   /api/employees             # Opret medarbejder
PUT    /api/employees/{id}        # Opdater medarbejder
DELETE /api/employees/{id}        # Slet medarbejder (soft)

POST   /api/contact               # Send kontaktbesked
GET    /api/contact/messages      # Hent alle beskeder (admin)
GET    /api/contact/messages/unread  # Hent ulæste beskeder
PATCH  /api/contact/messages/{id}/read  # Marker som læst
DELETE /api/contact/messages/{id} # Slet besked
```

### Nyttige Docker Kommandoer
```bash
# Se logs fra backend
docker compose logs -f app

# Se logs fra MySQL
docker compose logs -f mysql

# Tjek container status
docker compose ps

# Restart efter kodeændringer
docker compose restart app

# Stop systemet
docker compose down

# Fuld reset (SLETTER DATABASE!)
docker compose down -v

# Rebuild efter Dockerfile ændringer
docker compose up -d --build

# Åbn MySQL shell
docker exec -it bilensven-mysql mysql -u bilensven_user -p
```

### Krav
- **Docker Desktop** installeret og kørende
- **Port 8080** (app) og **3308** (MySQL) skal være ledige
- Minimum **2GB RAM** til Docker
- **Java 24** (kun hvis du kører uden Docker)

## 👥 Projekt Info

### Udvikler
- **Julius** - Full Stack Development

### Roller & Ansvar
- **Backend**: Spring Boot API, database design, business logic
- **Frontend**: HTML/CSS/JS, admin panel, responsive design
- **DevOps**: Docker setup, CI/CD, GitHub Actions
- **Testing**: JUnit tests, Mockito, integration tests

```

## ✅ Funktionalitet

### Implementeret (MVP)
- ✅ CRUD operations for Services
- ✅ CRUD operations for Employees
- ✅ Contact message system
- ✅ Admin authentication (simple session-based)
- ✅ Responsive design (mobile-friendly)
- ✅ Docker containerization
- ✅ MySQL database med persistence
- ✅ REST API med proper HTTP status codes
- ✅ Exception handling (GlobalExceptionHandler)
- ✅ JPA auditing (timestamps)
- ✅ Soft delete (active flags)
- ✅ Input validation (Jakarta Bean Validation)
- ✅ GitHub Actions CI/CD
- ✅ Unit tests (JUnit + Mockito)

### Future Enhancements (Nice-to-have)
- 🔄 Online booking system
- 🔄 Employee login (Spring Security)
- 🔄 Email notifications
- 🔄 File upload (employee/service images)
- 🔄 Calendar integration
- 🔄 Payment integration
- 🔄 Customer accounts
- 🔄 Reviews/ratings

## 🧪 Testing

### Kør Tests Lokalt
```bash
# Alle tests
mvn test

```

### CI/CD Pipeline
- **GitHub Actions** kører automatisk ved:
    - Pull requests til `main`
    - Push til `main` branch
- **Pipeline steps:**
    1. Checkout code
    2. Setup Java 24
    3. Build med Maven
    4. Run tests
    5. Build Docker image (kun på main)

### Implementeret
- ✅ Input validation (@Valid annotations)
- ✅ SQL injection protection (JPA prepared statements)
- ✅ Environment variables for passwords
- ✅ @JsonIgnore på User password field

### Production Considerations (ikke implementeret i MVP)
- 🔄 Spring Security med JWT
- 🔄 HTTPS/TLS
- 🔄 Rate limiting
- 🔄 Password hashing (BCrypt)
- 🔄 Role-based access control

## 📊 Performance

### Optimizations
- ✅ Maven dependency caching i Docker
- ✅ Multi-stage Docker build (230 MB image)
- ✅ Database query optimization (findByActiveTrue vs findAll)
- ✅ Connection pooling (HikariCP default)
- ✅ JVM memory limits (-Xmx512m)

### Metrics (lokal test)
```
Cold start:       ~15 sekunder
API response:     <50ms (average)
Database queries: <10ms (average)
Image size:       ~230 MB
Build time:       ~3-5 min
```

## 📖 Dokumentation

### API Documentation
- REST endpoints dokumenteret i denne README
- Request/Response eksempler findes i test classes
- DTO validering beskrevet i kode kommentarer

### Kode Kvalitet
- Clean Code principper
- Functional programming (Streams API)
- SOLID principles
- DRY (Don't Repeat Yourself)

### Lokal Development
```bash
# Med Docker (anbefalet)
docker compose up -d

# Uden Docker (kræver MySQL installeret)
mvn spring-boot:run -Dspring.profiles.active=dev
```

### Production Deployment
```bash
# Build image
docker build -t bilensven:latest .

# Push til registry
docker tag bilensven:latest your-registry/bilensven:latest
docker push your-registry/bilensven:latest

# Deploy med compose
docker compose -f compose.prod.yaml up -d
```

### Demo Flow
1. Start systemet: `docker compose up -d`
2. Vis frontend (services, medarbejdere)
3. Login til admin panel
4. Create/Update/Delete operations
5. Vis database persistence
6. Vis test coverage
7. Forklær CI/CD pipeline

## 📞 Kontakt

**Repository**: [github.com/JuliusLystre/bilensven](https://github.com/JuliusLystre/bilensven)

**Issues**: Hvis du finder bugs eller har forslag, opret en issue på GitHub.

## 📄 Licens

Dette projekt er udviklet som et eksamensproject på Datamatiker-uddannelsen og er ikke licenseret til kommerciel brug.

---
