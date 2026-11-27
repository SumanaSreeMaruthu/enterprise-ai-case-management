# Enterprise AI Case Management

A Spring Boot 3 / Java 21 portfolio demonstration for managing synthetic investigation cases.

## Implemented
- REST CRUD API for cases at `/api/v1/cases`
- H2 file-backed JPA persistence
- Case status and assignment updates
- Browser interface at `http://localhost:8080/`
- Gemini 2.5 Flash case-note summary endpoint (requires `GEMINI_API_KEY`)
- Basic Spring Boot context test, Dockerfile, GitHub Actions build

## Run

Requires Java 21 and Maven 3.9+.

```bash
export GEMINI_API_KEY=your_key_here # optional; needed only for AI summaries
mvn spring-boot:run
```

Open `http://localhost:8080/`. The H2 database is stored under `./data` locally.

## REST examples

```bash
curl -X POST http://localhost:8080/api/v1/cases \
  -H 'Content-Type: application/json' \
  -d '{"title":"Synthetic review","notes":"Unusual sample activity for testing"}'

curl http://localhost:8080/api/v1/cases
curl -X PATCH http://localhost:8080/api/v1/cases/1 \
  -H 'Content-Type: application/json' \
  -d '{"status":"IN_PROGRESS","assignedTo":"Demo Analyst"}'
curl -X POST http://localhost:8080/api/v1/cases/1/ai-summary
```

## Limitations

This is a **portfolio demo, not a production-ready financial crime application**. There is no authentication/authorization, audit trail, Kafka integration, workflow engine, PostgreSQL, or Angular frontend. The browser UI is plain HTML/JavaScript. Do not upload real customer data or confidential investigation notes. Gemini summaries are suggestions only and require human review. Tests/build have not been verified in this environment.

## Next improvements
- Spring Security and role-based access
- PostgreSQL and Flyway migrations
- Immutable audit events and workflow validations
- Kafka events, Angular frontend and end-to-end tests
- Rate limits, request timeouts, structured logging and robust AI response handling
