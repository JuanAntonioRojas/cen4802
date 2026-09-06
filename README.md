# SSCRM — Super Simple CRM (Customer Relationship Management)

**Author:** Tony Rojas  
**Course:** CEN 4802C – Software Integration, Configuration, and Testing  
**Repository:** cen4802  

---

## Project Overview
This project is a lightweight, web-accessible Customer Relationship Management (CRM) system built in pure Java. It serves as the baseline project for practicing DevOps workflows, version control management, branch isolation, automated testing, and CI/CD pipelines.

The application allows users to view, track, and manage sales leads across interactive sales funnel stages directly in a web browser.

---

## Features
* **Built-in HTTP Server:** Runs locally on port `8080` using core Java `HttpServer` without requiring heavy external application servers.
* **Lead Tracking Logic:** Manages active pipeline records (ID, Contact Name, Company, Pipeline Stage).
* **Interactive Sales Funnel:** Dynamic stage dropdowns allowing status transitions (*New Lead, Contacted, Qualified, Proposal Sent, Negotiation, Won/Closed*).
* **Pure Java Architecture:** Zero external frontend or backend dependencies for minimal overhead and fast execution.

### Future Roadmap
* Direct communication triggers (Twilio API routing for calls/SMS).
* Deep-link actions for external messaging clients (`tel:`, WhatsApp).
* Extended REST endpoints (`/leads`, `/health`, `/call`) and persistent database back-end.

---

## Requirements
* **Java Development Kit (JDK):** Version 17 or higher
* **Git:** For version control management

---

## How to Build and Run Locally

**1. Clone the repository:**
```bash
git clone [https://github.com/JuanAntonioRojas/cen4802.git](https://github.com/JuanAntonioRojas/cen4802.git)
cd cen4802
