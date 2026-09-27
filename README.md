# SSCRM - Super Simple CRM

SSCRM is a small Java CRM used for the CEN 4802C CI, testing, packaging, and containerization assignments.

## Current CRM page

The browser page includes:

- Customer & Revenue Intelligence
- Active Opportunities
- Pipeline Value
- Weighted Pipeline
- Average Active Deal
- Needs Action
- Sales Funnel
- Estimated value, probability, weighted value, and next follow-up date

"Needs Action" is the short owner-facing list of customers or opportunities that require a sales action now.  The score is based on follow-up urgency, opportunity value, and funnel stage.

## Project files

```text
src/main/java/
  Main.java
  Lead.java
  CRMService.java
  BusinessMetrics.java

src/test/java/
  LeadTest.java
  CRMServiceTest.java
  BusinessMetricsTest.java
  MainTest.java
```

## Local verification

From the project root:

```bat
mvn clean test
mvn package
java -jar target\sscrm-1.1.0.jar
```

Then open:

```text
http://localhost:8080
```

Jenkins uses port 8081 so SSCRM can use port 8080.

## Docker

The Dockerfile does not rebuild the Java source.  It copies the JAR that Maven/Jenkins already built and tested.

```bat
docker build -t sscrm:1.1.0 .
docker run --name sscrm-container -p 8080:8080 sscrm:1.1.0
```

The application is then available at `http://localhost:8080` from inside the container.
