🏥 Clinic Management System

Final project for the Software Engineering course 👩‍💻 Team: 2 members


----------------------------------------------------------------------



📌 Project Description

An online system for managing a medical clinic:

🩺 Patient appointment scheduling

👨‍⚕️ Patient and doctor profiles

💊 Prescription issuance

📝 Maintaining medical records

🏥 Medicine directory management

💰 Billing for services

⭐ Patient reviews


----------------------------------------------------------------------



🛠 Technologies

Spring Boot 3

Spring Data JPA

Spring Security (roles: ADMIN, DOCTOR, PATIENT)

MapStruct for Entity ↔ DTO mapping

Liquibase for database migrations

Lombok

PostgreSQL (or H2 for tests)

JUnit 5 + Mockito for unit tests

REST API with DTO (returning Entity is not allowed)


----------------------------------------------------------------------



📂 Business Entities

👤 User

👨‍⚕️ DoctorProfile

📅 Appointment

📝 MedicalRecord

🧑‍🤝‍🧑 Patient

💊 Prescription

🏷 Medicine

💵 Invoice

⭐ Review

🔑 Role (enum, actively used in security)


----------------------------------------------------------------------



🔑 Roles and Permissions

ADMIN — full access:

Create/delete users, doctors, patients

Manage all data

DOCTOR — work with medical data:

Create medical records

Issue prescriptions

Manage medicine directory

PATIENT — work with their own profile:

Create/edit profile

Book appointments

Leave reviews after appointments

----------------------------------------------------------------------




🚀 Project Setup

Clone the repository:

git clone https://github.com/your-username/clinic-management-system.git


----------------------------------------------------------------------


Set up the database (MySQL or H2)

Run the application via IDE or command line:

mvn spring-boot:run


----------------------------------------------------------------------

Open in browser: http://localhost:8000
