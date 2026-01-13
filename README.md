# Property Operations & Tenant Management Platform

Java 17 / Spring Boot backend for landlord–tenant style operations with JWT security, PostgreSQL persistence, Redis caching, and Swagger docs.

## Features
- JWT auth with roles ADMIN and TENANT
- Property, tenant, booking, and complaint workflows with business guards
- Redis caching for property list and tenant profile
- Centralized exception handling and validation
- Swagger UI at `/swagger-ui.html`
- AWS-ready configuration via environment variables

## Getting Started
1. **Prerequisites**: Java 17+ (tested with Java 24), Maven (wrapper included).
2. **For Development**: Uses H2 in-memory database and embedded Redis config. No external DB required.
3. **Run locally**:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```
   Or use VS Code tasks: `Tasks: Run Task > run`
4. **For Production**: Set environment variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` (base64), `JWT_EXPIRATION_MS`, `REDIS_HOST`, `REDIS_PORT`, `CACHE_TTL_SECONDS`. Use PostgreSQL and Redis.
5. **Profiles**: `dev` uses H2; default/production uses PostgreSQL with `hibernate.ddl-auto=update`.
6. **Docs**: Browse Swagger UI at `http://localhost:8080/swagger-ui.html` after startup.

## Module Overview
- **Auth**: `/api/auth/signup`, `/api/auth/login` issuing JWTs.
- **Properties**: CRUD for admins; tenants read-only.
- **Tenants**: admin links a signed-up tenant user to a property; tenants fetch `/api/tenants/me`.
- **Bookings**: tenants request bookings for their property; admin decides via `/api/bookings/{id}/decide`.
- **Complaints**: tenants file complaints; admin updates status.

## Testing
Run unit tests (uses H2 and Mockito):
```bash
./mvnw test
```
Or use VS Code tasks: `Tasks: Run Task > test`

## Deployment (AWS EC2 quick notes)
- Provide env vars in systemd or container orchestrator.
- Open ports for app (default 8080), PostgreSQL, and Redis as required.
- Use an externalized `application.yml` or env vars; avoid committing secrets.
- For production, set `spring.jpa.hibernate.ddl-auto=validate` and manage schema migrations separately.

## Postman
Import `postman_collection.json` for starter requests. Replace `{{baseUrl}}` and `{{token}}`.
