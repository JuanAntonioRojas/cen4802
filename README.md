# SSCRM CI Update — Customer \& Revenue Intelligence



This package replaces the unrelated array/list benchmark code with a small Java CRM domain and a new CRM-specific JUnit suite.

## 

## Meaningful Assignment 4 revision



The Java application now includes:

* Customer \& Revenue Intelligence cards
* Total active pipeline value
* Weighted pipeline value
* Average active deal value
* A Today / Next Best Action priority list
* Priority rules intentionally aligned with the n8n prototype
* A responsive dark-mode CRM page suitable for browser demonstration

`Financial Health \& Cash Flow` is deliberately shown as the next planned revision so it can become the visible source change used during the Assignment 5 container rebuild.

## 

## Files



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

## 

## Local verification



From the project root:

```bat
mvn clean test
mvn package
java -jar target\\sscrm-1.1.0.jar
```

Then open:

```text
http://localhost:8080
```

If Jenkins is already using port 8080, start Jenkins on another port such as 8081 before running SSCRM.

## 

## Suggested Git workflow



```bat
git checkout -b feature/customer-revenue-intelligence
git add .
git commit -m "Add CRM customer and revenue intelligence"
git push -u origin feature/customer-revenue-intelligence
```

Create a Pull Request into `main`, merge it, and then execute the Jenkins pipeline again. The new CI build should show this new Git commit, the new CRM-specific unit tests, a successful package stage, and a new `sscrm-1.1.0.jar` artifact.

