# Notes app: BFF + JWT starter

The notes app from the [Spring Security exercises](https://e26-3sem.osnb.courses/prog2/spring-security-foundations/setup), split into two Spring Boot apps the same way as the [spring-security-jwt-starter](https://github.com/ek-osnb/spring-security-jwt-starter). It is the starter for the [optional notes app exercise](https://e26-3sem.osnb.courses/prog2/spring-security-jwt/notes-app-jwt) in Spring Security JWT. Do the other JWT exercises on the jwt-starter first.

## Architecture

```text
Browser ──> Caddy (localhost:8088)
              ├── /api/*          ──> BFF (port 8080) ──JWT──> Resource server (port 8081)
              └── everything else ──> nginx (frontend/src)
```

| Component | Description |
|---|---|
| **Caddy** | Reverse proxy. Puts everything on one origin (`localhost:8088`), so the browser needs no CORS. |
| **nginx** | Serves the Navigo SPA in `frontend/src`. |
| **BFF** (`bff/`) | Spring Boot on port `8080`. Form login, the session cookie, CSRF protection (SPA mode), the users in H2 and `/api/me`. In the exercise, it signs JWTs with RSA and calls the resource server for the user. |
| **Resource server** (`resource/`) | Spring Boot on port `8081`. Owns the notes in H2. In the exercise, it only accepts requests with a valid JWT from the BFF. |

Both databases are H2 in memory. They start over every time you restart an app.

## Run it

### 1. Start Caddy and nginx

```bash
docker compose -f compose.dev.yaml up -d
```

### 2. Start the BFF

```bash
cd bff
./mvnw spring-boot:run
```

### 3. Start the resource server

```bash
cd resource
./mvnw spring-boot:run
```

Open http://localhost:8088.

## Endpoints

| Method | Path | App | Description |
|---|---|---|---|
| `POST` | `/api/login` | BFF | Form login, sets the session cookie |
| `POST` | `/api/logout` | BFF | Ends the session |
| `GET` | `/api/me` | BFF | The logged-in user and their roles |
| `GET` | `/api/notes` | resource | The user's notes |
| `POST` | `/api/notes` | resource | Adds a note (JSON: `{"text": "..."}`) |
| `GET` | `/api/admin/notes` | resource | Everyone's notes, for admins |

The browser only talks to the BFF. Until you have done the exercise, the BFF has no `/api/notes`, so the notes page says "Could not load notes: HTTP 404". The resource server has no security yet: try `curl http://localhost:8081/api/notes`.

## Test users

| Username | Password | Role |
|---|---|---|
| `user1` | `pw` | USER |
| `user2` | `pw` | USER |
| `admin` | `pw` | ADMIN |

## TODO comments

The places you need to change in the existing code have a `TODO Part N` comment. The classes you create yourself are described in the exercise.
