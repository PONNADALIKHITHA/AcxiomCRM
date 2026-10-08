# AcxiomCRM - 2 Hour Basic Project

A minimal working CRM project aligned to the supplied AcxiomCRM assignment.

## Technology stack
- Java + Spring Boot: core CRM REST API, authentication, validation, role checks, MySQL persistence
- Python + Django: lightweight reporting/dashboard integration service
- REST APIs: Spring Boot APIs consumed by Postman and Django
- MySQL + MySQL Workbench: database
- Postman: API testing
- HTML/CSS/Bootstrap: simple Django dashboard

## Implemented assignment areas
- Authentication: login/logout, BCrypt password hashing, failed-login lockout
- Roles: ADMIN, MANAGER, SALES_EXECUTIVE
- Customers: create/list/update/delete, search, duplicate email/phone validation
- Leads: create/list/update, status validation
- Opportunities: create/list, amount/probability/date business validation, weighted pipeline
- Follow-ups: create/list, date validation
- Audit log: login, create/update/delete events
- Dashboard: KPI counts and pipeline
- Reports: pipeline endpoint and Django dashboard
- REST APIs with DTO-like request/response objects
- Server-side validation and simple client-side validation in Django
- MySQL persistence

## Important scope note
This is intentionally a basic, demo-ready implementation for a short deadline. It covers the core evaluation points from the supplied document, but it is not a production security implementation. For production, replace the demo token mechanism with JWT/OAuth2, use HTTPS, externalized secrets, CSRF protection for MVC forms, stronger rate limiting, and full resource-level authorization.

## Default login
- Username: admin
- Password: Admin@123
- Role: ADMIN

The first run creates the database tables and the default admin user automatically.

## Project structure

AcxiomCRM_QuickProject/
  springboot-crm/
  django-dashboard/
  postman/
  mysql/
  README.md

## 1. Create MySQL database

Open MySQL Workbench and run:

CREATE DATABASE acxiomcrm;

Then check the credentials in:
springboot-crm/src/main/resources/application.properties

Default:
username=root
password=root

Change the password if your MySQL installation uses a different one.

## 2. Run Spring Boot API

Requirements:
- JDK 17+
- Maven 3.9+

Open terminal:

cd springboot-crm
mvn spring-boot:run

API:
http://localhost:8080

Health:
http://localhost:8080/api/health

## 3. Test login in Postman

POST http://localhost:8080/api/auth/login

JSON:
{
  "username": "admin",
  "password": "Admin@123"
}

Copy the returned token.

For protected requests add header:
Authorization: Bearer YOUR_TOKEN

Import:
postman/AcxiomCRM.postman_collection.json

## 4. Run Django dashboard

Requirements:
- Python 3.10+

Open another terminal:

cd django-dashboard
python -m venv venv

Windows:
venv\Scripts\activate

Install:
pip install -r requirements.txt

Run:
python manage.py migrate
python manage.py runserver 8000

Open:
http://127.0.0.1:8000/

The Django service calls the Spring Boot REST API.

## 5. Useful API examples

### Customers
GET  /api/customers
POST /api/customers
PUT  /api/customers/{id}
DELETE /api/customers/{id}

POST body:
{
  "name": "ABC Traders",
  "email": "abc@example.com",
  "phone": "9876543210",
  "companyName": "ABC Traders",
  "address": "Rajam",
  "city": "Rajam",
  "state": "Andhra Pradesh",
  "status": "ACTIVE"
}

### Leads
GET  /api/leads
POST /api/leads

### Opportunities
GET  /api/opportunities
POST /api/opportunities

Example:
{
  "name": "ERP Implementation",
  "customerId": 1,
  "amount": 500000,
  "probability": 60,
  "expectedCloseDate": "2026-12-30",
  "stage": "PROPOSAL",
  "status": "OPEN"
}

### Follow-ups
GET  /api/followups
POST /api/followups

Example:
{
  "customerId": 1,
  "leadId": null,
  "followUpDate": "2026-12-31",
  "followUpType": "CALL",
  "remarks": "Discuss proposal",
  "status": "PLANNED"
}

### Dashboard
GET /api/dashboard

### Pipeline report
GET /api/reports/pipeline

## Validation demonstrations

1. Invalid email -> 400
2. Invalid phone -> 400
3. Duplicate customer email/phone -> 409
4. Opportunity amount <= 0 -> 400
5. Probability outside 0-100 -> 400
6. Active opportunity close date in the past -> 400
7. Planned follow-up date before today -> 400
8. Five failed logins -> account lockout

## Suggested 2-hour demo flow

1. Start MySQL.
2. Start Spring Boot.
3. Login in Postman.
4. Create one customer.
5. Create one lead.
6. Create one opportunity.
7. Create one follow-up.
8. Open /api/dashboard.
9. Open Django dashboard on port 8000.
10. Show MySQL Workbench tables.
11. Show an invalid opportunity request returning 400.
12. Show audit logs in MySQL.

## Assignment mapping

Authentication/Authorization -> Spring Security + BCrypt + roles
CRM -> Customer/Lead/Opportunity/Follow-up entities
Validation -> Bean Validation + business rules
API -> REST controllers
Audit -> AuditLog entity/service
Reports -> dashboard/pipeline endpoints + Django dashboard
Database -> MySQL
Testing -> Postman collection
