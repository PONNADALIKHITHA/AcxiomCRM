-- Run this in MySQL Workbench.
CREATE DATABASE IF NOT EXISTS acxiomcrm;
USE acxiomcrm;
-- Spring Boot JPA creates/updates the tables automatically on first run.
SHOW TABLES;
SELECT * FROM users;
SELECT * FROM customers;
SELECT * FROM leads;
SELECT * FROM opportunities;
SELECT * FROM followups;
SELECT * FROM audit_logs;
