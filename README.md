# Simple Lead Management CRM

**Author:** Tony Rojas  
**Course:** CEN 4802 - Software Integration  
**Repository:** cen4802  

---

## Project Overview
This project is a lightweight, web-accessible Customer Relationship Management (CRM) system built in pure Java. It serves as the baseline project for practicing DevOps workflows, version control management, automated testing, and CI/CD pipelines throughout this semester.

The application allows users to view, track, manage, and directly initiate contact with sales leads via HTTP endpoints in a web browser.

---

## Features
* **Built-in HTTP Server:** Runs locally on port `8080` without requiring external application servers.
* **Lead Tracking Logic:** Manages contact records (ID, Name, Email, Phone, Status).
* **Direct Communication Triggers:**
  * One-click initiate call / SMS routing via Twilio API integration.
  * Direct deep-link actions for external messaging clients (`tel:`, WhatsApp, Messenger).
* **REST/HTTP Endpoints:**
  * `GET /leads` - Returns the interactive lead dashboard.
  * `POST /call` - Triggers outbound call routing.
  * `GET /health` - Service health-check endpoint.

---

## Requirements
* **Java Development Kit (JDK):** Version 17 or higher
* **Git:** For version control management

---

## How to Build and Run Locally

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/JuanAntonioRojas/cen4802.git](https://github.com/JuanAntonioRojas/cen4802.git)
   cd cen4802
   ```

2. **Compile the Java source files:**
   ```bash
   javac -d bin src/*.java
   ```

3. **Run the server:**
   ```bash
   java -cp bin Main
   ```

4. **Access the CRM:**
   Open your browser and navigate to:
   * **Leads Dashboard:** http://localhost:8080/leads
   * **Health Check:** http://localhost:8080/health
