# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Kaleido is a Spring Boot-based **code generation server** that connects to databases (MySQL, PostgreSQL), reads table structures, and generates source code using FreeMarker templates. It is the backend counterpart to [kaleido-client](https://github.com/wp2code/kaleido-client).

## The model response is in Chinese
## Build Commands

```bash
# Build entire project
mvn clean install

# Build skipping tests
mvn clean install -DskipTests

# Build with environment profile (dev is default)
mvn clean install -P dev|test|prod

# Run from root
cd kaleido-start && mvn spring-boot:run

# Run a specific test
mvn test -Dtest=ClassName -pl kaleido-test

# Run the packaged JAR
java -jar kaleido-start/target/kaleido-server-start.jar
```

The packaged JAR is `kaleido-start/target/kaleido-server-start.jar` with dependencies in `target/lib/`.

## Module Architecture

Multi-module Maven project with a strict layered architecture:

```
kaleido-infra/
  kaleido-infra-base/      # Constants, exceptions, enums
  kaleido-infra-common/    # Shared utilities

kaleido-spi/
  kaleido-spi-db/          # Database abstraction interfaces (IResultSetValueHandler, DriverManager)

kaleido-domain/
  kaleido-domain-api/      # Service interfaces
  kaleido-domain-core/     # Business logic implementations + FreeMarker templates (*.ftlx)
  kaleido-domain-repository/ # MyBatis-Plus mappers and DB access
  kaleido-domain-model/
    kaleido-domain-model-dto/    # Request/response DTOs
    kaleido-domain-model-vo/     # View Objects
    kaleido-domain-model-entity/ # JPA/MyBatis entities

kaleido-plugins/
  kaleido-plugins-mysql/       # MySQL-specific SPI implementation
  kaleido-plugins-postgresql/  # PostgreSQL-specific SPI implementation
  kaleido-plugins-mp/          # MyBatis-Plus integration
  kaleido-plugins-mapstruct/   # DTO ↔ Entity mapping
  kaleido-plugins-template/    # FreeMarker template engine integration

kaleido-web/
  kaleido-web-api/         # REST controllers

kaleido-start/             # Application entry point, Flyway migrations, application.yml
kaleido-parent/            # Parent POM: version management, build plugins, profiles
```

**Dependency rule**: `kaleido-start` → `kaleido-web-api` + `kaleido-domain-*` + `kaleido-plugins-*` → `kaleido-spi-db` → `kaleido-infra-*`

## Key Architectural Patterns

- **SPI pattern**: Database vendors (MySQL, PostgreSQL) implement interfaces in `kaleido-spi-db`. New database support means a new plugin module.
- **Service interfaces in `-api`, implementations in `-core`**: Never put business logic in `-api` modules.
- **FreeMarker templates** (`.ftlx` files) live in `kaleido-domain-core/src/main/resources/templates/`. Template variables are resolved from `CodeGenerationContext`.
- **Database migrations** managed by Flyway in `kaleido-start/src/main/resources/db/migration/`.
- **MapStruct** handles all DTO↔Entity conversions in `kaleido-plugins-mapstruct`.

## Code Generation Flow

```
REST API (CodeGenerationController)
  → CodeGenerationService (domain-api)
    → CodeGenerationTemplateConfigService (loads template configs from DB)
    → Database plugin (MySQL/PostgreSQL, reads table DDL and columns)
    → FreeMarker template processing (resolves *.ftlx templates)
    → Returns generated code as string or streamed response
```

Main endpoints:
- `POST /api/v1/code-gen/preview` — preview generated code
- `POST /api/v1/code-gen/generation` — generate code
- `POST /api/v1/code-gen/preview/stream` — streaming preview

## Key Technologies

| Technology | Version | Purpose |
|---|---|---|
| Spring Boot | 3.2.0 | Framework |
| MyBatis-Plus | 3.5.4 | ORM |
| FreeMarker | (Spring managed) | Template engine |
| MapStruct | 1.4.2 | Object mapping |
| Lombok | (Spring managed) | Boilerplate reduction |
| Hutool | 5.8.16 | Utility library |
| Flyway | 8.x | DB migrations |
| Jackson | (Spring managed) | JSON |

## Important Notes

- Java 17+ required; uses Jakarta (not javax) namespaces.
- Lombok and MapStruct annotation processors must both be declared in `maven-compiler-plugin` — order matters (Lombok first).
- `check-template-location: false` in application.yml suppresses FreeMarker classpath warnings since templates are loaded dynamically.
- Multipart upload size is unlimited (configured in application.yml).
- Commit messages follow Chinese language convention for descriptions.
