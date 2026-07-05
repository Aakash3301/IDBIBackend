# 📊 AI-Ready MSME Financial Health Card

Welcome to the backend repository for the **AI-Ready MSME Financial Health Card**. This project was built to revolutionize how financial institutions evaluate the creditworthiness of small and medium businesses.

---

## 🚨 The Problem

Banks and Non-Banking Financial Companies (NBFCs) struggle to evaluate **New-to-Credit (NTC)** and **New-to-Bank (NTB)** Micro, Small, and Medium Enterprises (MSMEs). Traditional underwriting models rely heavily on formal documentation like audited balance sheets, Income Tax Returns (ITR), and long-standing credit histories. 

Many MSMEs lack these documents or experience delays in filing them, causing them to be rejected for critical working capital loans despite having a healthy, cash-flowing business.

---

## 💡 Our Solution

We built an **Alternative Data Scoring Engine**. 

Instead of waiting for an annual ITR, our system ingests high-frequency digital footprints that MSMEs generate every day:
- **GST Filings** (Revenue indicators)
- **UPI Transactions** (Daily cash flow and transaction volume)
- **Account Aggregator Data / Bank Statements** (Liquidity and average balance)
- **EPFO Contributions** (Employee stability)

By aggregating this alternative data, our engine computes a real-time, unified **Financial Health Score** (ranging from 0 to 1000), assigns a Risk Category, and automatically generates an actionable Loan Recommendation.

---

## ⚙️ How It Works (The Flow)

1. **Onboarding**: An MSME registers via the platform providing basic details (Business Name, PAN, GST Number, Industry Type).
2. **Data Aggregation**: The backend hooks into various digital data points. *(Note: For the scope of this hackathon, we built a dynamic `MockDataService` that simulates 6 months of highly realistic, industry-specific historical data).*
3. **The Engine (The Brain)**: The `FinancialHealthEngine` processes the time-series data. It evaluates revenue scale, UPI consistency, and bank balance buffers to generate a score.
4. **Decisioning**: Based on the score, the MSME is categorized into a Risk Tier (LOW, MEDIUM, HIGH) and evaluated for loan eligibility.
5. **The Health Card**: A consolidated, clean JSON payload is delivered via REST API to the frontend dashboard, providing loan officers or the MSME owners themselves with a beautiful, real-time snapshot of their financial health.

---

## 🛠️ Tech Stack & Architecture

We prioritized speed, scalability, and clean code. The project is built using an Enterprise-grade **SOLID Clean Architecture**.

- **Core**: Java 17, Spring Boot 3.4.1
- **Database**: PostgreSQL (Relational mapping via Spring Data JPA & Hibernate)
- **API Documentation**: Swagger / OpenAPI 3
- **Build Tool**: Maven
- **Boilerplate Reduction**: Lombok
- **Architecture Highlights**: 
  - Interface-driven services for loose coupling.
  - Strict separation of concerns (Controllers only handle HTTP, Services handle business logic, Repositories handle Data Access).
  - Global Exception Handling using `@ControllerAdvice`.

---

## 🚀 Future Scope & Enhancements

Because we used interface-driven design, this MVP is fully prepared to evolve into a production-ready system. 

1. **Machine Learning Integration**: 
   - Currently, the engine uses a weighted rule-based algorithm. The architecture is designed so a Data Science team can swap the `FinancialHealthEngineImpl` with an API call to a Python-based ML model (e.g., Random Forest or XGBoost) trained on actual default data, without changing any controller code.
2. **Live Third-Party Integrations**:
   - Replace the `MockDataService` with live webhook integrations to the **Account Aggregator (AA) framework**, **GSTN APIs**, and **UPI Payment Gateways** (like Razorpay or PhonePe).
3. **Continuous Monitoring & Alerts**:
   - Implement scheduled jobs (Cron) to recalculate the score weekly. Trigger push notifications to bank admins if a business's health score drops rapidly (Early Warning System).
4. **Blockchain Audit Trail**:
   - Store snapshots of the Financial Health Score on a ledger for immutable, tamper-proof auditing required by banking regulators.

---

## 🐳 Running Locally with Docker

The easiest way to run the entire stack (Spring Boot Application + PostgreSQL Database) is using Docker Compose.

### Prerequisites
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Steps to Run

1. Open a terminal and navigate to the root directory of the project.
2. Run the following command to build the image and start the containers:

   ```bash
   docker-compose up --build
   ```

3. Once the services are running, the application will be accessible at:
   - **API Base URL**: `http://localhost:8080`
   - **Swagger UI**: `http://localhost:8080/swagger-ui.html`

4. To stop the application, run:

   ```bash
   docker-compose down
   ```
