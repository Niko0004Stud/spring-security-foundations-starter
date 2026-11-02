# Spring Security Foundations: starter

A small notes app. In the [Spring Security exercises](https://e26-3sem.osnb.courses/prog2/spring-security-foundations/setup) you add security to it, one exercise at a time.

- Spring Boot serves the API (`/api/...`).
- nginx serves the page, a Navigo single-page app in `frontend/src`.
- Caddy puts both on one origin: `/api/*` goes to Spring on port 8080, and everything else goes to nginx.
- The database is H2 in memory. It starts over every time you restart the app.

## Run it

```bash
docker compose -f compose.dev.yaml up -d
./mvnw spring-boot:run
```

Open http://localhost:8088.

## Test users

| Username | Password | Role |
|---|---|---|
| `user1` | `pw` | USER |
| `user2` | `pw` | USER |
| `admin` | `pw` | ADMIN |

Logging in doesn't work until you have done the exercises.

## TODO comments

The places you need to change in the existing code have a `TODO Exercise N` comment. The classes you create yourself are described in the exercises.
