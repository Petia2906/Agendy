# Agendy – Event Management System

Agendy е Spring Boot REST API система за организиране на събития – от създаване на събития, до управление на програма и обратна връзка.

---

## Основни функционалности

### Authentication

* Регистрация на потребители
* Вход / Изход
* Роли (напр. `ATTENDEE`)

### Events

* Създаване на събития
* Преглед на всички събития
* Детайли за събитие
* Редакция и изтриване

### Tickets

* Закупуване на билети
* Преглед на мои билети
* Отказване на билет

### Sessions (Agenda)

* Програма на събитие
* Добавяне на сесии
* Редактиране и изтриване

### Halls

* Създаване на зали
* Управление на капацитет

### Feedback & Analytics

* Оценка на събитие
* Коментари
* Анализи

---

## API Структура

### Auth

```http
POST /auth/register
POST /auth/login
POST /auth/logout
```

Пример:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "ATTENDEE"
}
```

---

### Events

```http
GET    /events
POST   /events
GET    /events/{eventId}
PUT    /events/{eventId}
DELETE /events/{eventId}
```

---

### Tickets

```http
POST   /events/{eventId}/tickets
GET    /tickets/my
PATCH  /tickets/{ticketId}/cancel
```

---

### Sessions

```http
GET    /events/{eventId}/sessions
POST   /events/{eventId}/sessions
PUT    /sessions/{sessionId}
DELETE /sessions/{sessionId}
```

---

### Halls

```http
POST   /events/{eventId}/halls
GET    /events/{eventId}/halls
DELETE /halls/{hallId}
```

---

### Feedback

```http
POST /events/{eventId}/feedback
GET  /events/{eventId}/feedback
GET  /events/{eventId}/analytics
```

---

## Автори

- Полина Станева – https://www.linkedin.com/in/polina-staneva-0b7aa2324  
- Петя Хрусанова – https://www.linkedin.com/in/petya-hrusanova-455742372

---

⭐ Ако ти харесва проекта – дай star!
