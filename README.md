<div align="center">

# SSCRM
### Super Simple CRM

**A small Java CRM built for CEN 4802C**  
CI • Testing • Packaging • Containerization

<br>

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Status](https://img.shields.io/badge/Status-Active-success?style=for-the-badge)

<br>

**Current CRM Page includes:**

</div>

| Feature                        | Description                                      |
|--------------------------------|--------------------------------------------------|
| **Customer & Revenue Intelligence** | High-level business overview                  |
| **Active Opportunities**       | Live list of open deals                          |
| **Pipeline Value**             | Total value of the sales pipeline                |
| **Weighted Pipeline**          | Probability-adjusted pipeline value              |
| **Average Active Deal**        | Mean size of open opportunities                  |
| **Needs Action**               | Owner-facing list of customers needing attention |
| **Sales Funnel**               | Stage-based opportunity tracking                 |

<br>

> **Needs Action** is the short owner-facing list of customers or opportunities that require a sales action *now*.  
> The score is based on **follow-up urgency**, **opportunity value**, and **funnel stage**.

Each opportunity shows:
- Estimated value  
- Probability  
- Weighted value  
- Next follow-up date

---

<div align="center">

## Project Structure

</div>

```text
src/main/java/
├── Main.java
├── Lead.java
├── CRMService.java
└── BusinessMetrics.java

src/test/java/
├── LeadTest.java
├── CRMServiceTest.java
├── BusinessMetricsTest.java
└── MainTest.java
