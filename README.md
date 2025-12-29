🏥 Clinic Management System

🎓 Финальный проект по дисциплине Software Engineering
👩‍💻 Команда: 2 человека

📌 Описание проекта

Онлайн-система управления медицинской клиникой:

🩺 Запись пациентов на приём к врачам

👨‍⚕️ Профили пациентов и врачей

💊 Выписка рецептов

📝 Ведение медицинских записей

🏥 Управление справочником лекарств

💰 Выставление счетов за услуги

⭐ Отзывы пациентов

🛠 Технологии

🌱 Spring Boot 3

🗄 Spring Data JPA

🔐 Spring Security (роли: ADMIN, DOCTOR, PATIENT)

🔄 MapStruct для маппинга Entity ↔ DTO

📦 Liquibase для миграций базы данных

✨ Lombok

🐘 PostgreSQL (или H2 для тестов)

🧪 JUnit 5 + Mockito для unit-тестов

🌐 REST API с DTO (запрещено возвращать Entity)

📂 Бизнес-сущности (10 шт.)

👤 User

👨‍⚕️ DoctorProfile

📅 Appointment

📝 MedicalRecord

🧑‍🤝‍🧑 Patient

💊 Prescription

🏷 Medicine

💵 Invoice

⭐ Review

🔑 Role (enum, активно используется в security)

🔑 Роли и права доступа

ADMIN — полный доступ

создание/удаление пользователей, врачей, пациентов

управление всеми данными

DOCTOR — работа с медицинскими данными

создание медицинских записей

выписка рецептов

управление справочником лекарств

PATIENT — работа со своим профилем

создание/редактирование профиля

запись на приём

оставление отзывов после приёма

🚀 Запуск проекта

Склонируйте репозиторий:

git clone https://github.com/your-username/clinic-management-system.git


Настройте базу данных (PostgreSQL или H2)

Запустите приложение через IDE или командой:

mvn spring-boot:run


Откройте в браузере: http://localhost:8000
