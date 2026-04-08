# ⛓ Blockchain-Based Certificate Verification System

> **Final Year Project** — A full-stack web application that uses blockchain concepts (SHA-256 hashing + linked block chain) to issue and verify academic certificates.

---

## 🏗 Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        USER / EMPLOYER                          │
│              (visits frontend in browser)                        │
└─────────────────────────┬───────────────────────────────────────┘
                          │  HTTP requests
┌─────────────────────────▼───────────────────────────────────────┐
│                    FRONTEND (HTML/CSS/JS)                        │
│  index.html | login.html | dashboard.html | add-cert | verify   │
│                  Hosted on: Netlify / Vercel                     │
└─────────────────────────┬───────────────────────────────────────┘
                          │  REST API calls (fetch)
┌─────────────────────────▼───────────────────────────────────────┐
│              BACKEND (Java Spring Boot)                          │
│  Controllers → Services → Repositories                          │
│  JWT Auth | Hash Util | QR Code Gen | Blockchain Service        │
│                   Hosted on: Render.com (free)                   │
└─────────────────────────┬───────────────────────────────────────┘
                          │  JPA / JDBC
┌─────────────────────────▼───────────────────────────────────────┐
│                   DATABASE (MySQL)                               │
│   admins | certificates | blockchain_blocks | verification_logs  │
│             Hosted on: Railway.app / PlanetScale (free)          │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📁 Folder Structure

```
blockchain-cert-system/
├── database/
│   └── schema.sql                    ← Run this first in MySQL
├── backend/
│   ├── pom.xml                       ← Maven dependencies
│   └── src/main/
│       ├── java/com/certverify/
│       │   ├── CertVerifyApplication.java   ← Main entry point
│       │   ├── entity/               ← Database table mappings
│       │   │   ├── Certificate.java
│       │   │   ├── Admin.java
│       │   │   ├── BlockchainBlock.java
│       │   │   └── VerificationLog.java
│       │   ├── repository/           ← Database query methods
│       │   │   ├── CertificateRepository.java
│       │   │   ├── AdminRepository.java
│       │   │   ├── BlockchainBlockRepository.java
│       │   │   └── VerificationLogRepository.java
│       │   ├── dto/                  ← Data Transfer Objects
│       │   │   ├── CertificateRequest.java
│       │   │   ├── CertificateResponse.java
│       │   │   ├── LoginRequest.java
│       │   │   ├── LoginResponse.java
│       │   │   ├── VerifyResponse.java
│       │   │   └── ApiResponse.java
│       │   ├── service/              ← Business logic
│       │   │   ├── AuthService.java
│       │   │   └── CertificateService.java
│       │   ├── controller/           ← REST API endpoints
│       │   │   ├── AuthController.java
│       │   │   └── CertificateController.java
│       │   ├── security/             ← JWT auth
│       │   │   ├── JwtUtil.java
│       │   │   ├── JwtFilter.java
│       │   │   └── SecurityConfig.java
│       │   ├── util/                 ← Helper classes
│       │   │   ├── HashUtil.java
│       │   │   └── QRCodeUtil.java
│       │   └── blockchain/           ← Blockchain simulation
│       │       ├── Block.java
│       │       └── BlockchainService.java
│       └── resources/
│           └── application.properties
└── frontend/
    ├── index.html                    ← Home page
    ├── css/style.css                 ← Global styles
    ├── js/
    │   ├── api.js                    ← All API calls
    │   └── utils.js                  ← Shared helpers
    └── pages/
        ├── login.html                ← Admin login
        ├── dashboard.html            ← Admin dashboard
        ├── add-certificate.html      ← Issue certificate
        ├── all-certificates.html     ← View / manage all certs
        ├── verify.html               ← Public verify page
        └── result.html               ← Verification result
```

---

## ⚙️ REST API Reference

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/login` | Public | Admin login, returns JWT |
| POST | `/api/certificates/issue` | Admin | Issue new certificate |
| GET  | `/api/certificates` | Admin | Get all certificates |
| GET  | `/api/certificates/{id}` | Admin | Get one certificate |
| GET  | `/api/certificates/search?name=X` | Admin | Search by student name |
| GET  | `/api/certificates/verify/{id}` | Public | Verify a certificate |
| PUT  | `/api/certificates/{id}/revoke` | Admin | Revoke a certificate |
| GET  | `/api/certificates/stats` | Admin | Dashboard statistics |

---

## 🚀 LOCAL SETUP (Step-by-Step)

### Prerequisites
- Java 17+ (`java -version`)
- Maven 3.8+ (`mvn -version`)
- MySQL 8.0+
- Any browser (Chrome recommended)
- VS Code (optional, for editing)

### Step 1: Database Setup
```sql
-- Open MySQL Workbench or terminal
mysql -u root -p
-- Run the schema file:
source /path/to/blockchain-cert-system/database/schema.sql;
```

### Step 2: Configure Backend
Edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3: Run Backend
```bash
cd backend
mvn spring-boot:run
# Backend starts at: http://localhost:8080
```

### Step 4: Run Frontend
- Open `frontend/index.html` in browser, OR
- Use VS Code Live Server extension (recommended), OR
- Run: `npx serve frontend` in the frontend folder

### Step 5: Test It
1. Open browser → `http://localhost:5500` (or wherever frontend is)
2. Go to **Admin Login** → user: `admin`, password: `admin123`
3. Click **Issue Certificate** → fill the form → submit
4. Copy the Certificate ID
5. Go to **Verify** → paste the ID → click Verify
6. See the verification result!

---

## 🌐 FREE DEPLOYMENT GUIDE

### Option A: Frontend on Netlify + Backend on Render (RECOMMENDED)

#### Backend → Render.com
1. Push your code to GitHub
2. Go to [render.com](https://render.com) → New Web Service
3. Connect your GitHub repo
4. Set Root Directory: `backend`
5. Build Command: `mvn clean package -DskipTests`
6. Start Command: `java -jar target/blockchain-cert-system-1.0.0.jar`
7. Add Environment Variables:
   - `SPRING_DATASOURCE_URL` → your MySQL URL
   - `SPRING_DATASOURCE_PASSWORD` → your password
8. Click Deploy → get URL like: `https://your-app.onrender.com`

#### Database → Railway.app
1. Go to [railway.app](https://railway.app) → New Project → MySQL
2. Copy the connection details
3. Use them in Render's environment variables

#### Frontend → Netlify
1. In `frontend/js/api.js`, change:
   ```js
   const API_BASE = 'https://your-app.onrender.com/api';
   ```
2. Go to [netlify.com](https://netlify.com) → Add New Site → Deploy manually
3. Drag and drop your `frontend/` folder
4. Done! Get URL like: `https://your-site.netlify.app`

---

### Option B: Frontend on Vercel

1. Install Vercel CLI: `npm i -g vercel`
2. In `frontend/js/api.js` update `API_BASE` to your backend URL
3. `cd frontend && vercel --prod`

---

### Option C: Firebase Hosting (frontend only)
```bash
npm install -g firebase-tools
firebase login
firebase init hosting   # select frontend folder as public dir
firebase deploy
```

---

## 🔐 Blockchain Concept Explained (Simple)

### What is a Hash?
- A hash is like a fingerprint for data
- `SHA-256("Hello") = 185f8db32...` (always same output)
- Change even 1 character → completely different hash

### How Our Blockchain Works:
```
Block 0 (Genesis)            Block 1                    Block 2
┌──────────────────┐         ┌──────────────────┐       ┌──────────────────┐
│ certId: CERT-001 │    ←    │ certId: CERT-002 │  ←   │ certId: CERT-003 │
│ dataHash: abc... │         │ dataHash: def... │       │ dataHash: ghi... │
│ prevHash: 0      │         │ prevHash: abc... │       │ prevHash: def... │
│ blockHash: abc.. │         │ blockHash: def.. │       │ blockHash: ghi.. │
└──────────────────┘         └──────────────────┘       └──────────────────┘
```

### Verification Process:
1. User submits Certificate ID
2. We fetch certificate from database
3. We re-generate the hash from current data
4. We compare with hash stored in blockchain
5. If they match → **VALID** ✅
6. If they don't → **TAMPERED** ❌

### Real Ethereum (Advanced):
- Same concept, but blocks are stored on thousands of computers worldwide
- Uses Solidity smart contracts instead of Java code
- Connect with Web3j library in Java

---

## 👤 Default Login
- Username: `admin`
- Password: `admin123`

---

## 🛠 Modules Explained

| Module | File | Purpose |
|--------|------|---------|
| Entity | `Certificate.java` | Maps to MySQL table |
| Repository | `CertificateRepository.java` | Database queries |
| Service | `CertificateService.java` | Business logic |
| Controller | `CertificateController.java` | REST endpoints |
| HashUtil | `HashUtil.java` | SHA-256 hashing |
| BlockchainService | `BlockchainService.java` | Simulated blockchain |
| JwtUtil | `JwtUtil.java` | Token generation |
| SecurityConfig | `SecurityConfig.java` | Which APIs need login |

---

## 📦 Tech Stack
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Backend**: Java 17, Spring Boot 3.2
- **Database**: MySQL 8
- **Auth**: JWT (JSON Web Tokens)
- **Security**: BCrypt password hashing
- **QR Code**: ZXing library
- **Build**: Maven

---

## 💡 Ideas for Enhancement
- Connect to real Ethereum testnet using Web3j
- Add email notification when certificate is issued
- Add bulk import via Excel
- Add PDF generation with iText library
- Add admin user management

---

*Built as a Final Year Project — Blockchain-Based Certificate Verification System*
