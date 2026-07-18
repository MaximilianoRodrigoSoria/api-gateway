<!-- banner-badges -->
<p align="center">
  <a href="https://www.linkedin.com/in/soriamaximilianorodrigo/" target="_blank" rel="noopener noreferrer">
    <img width="100%" src="docs/img/banner.gif" alt="API Gateway — Maximiliano Rodrigo Soria">
  </a>
</p>

<p align="center">
  <a href="https://github.com/MaximilianoRodrigoSoria/api-gateway/actions"><img src="https://img.shields.io/github/actions/workflow/status/MaximilianoRodrigoSoria/api-gateway/ci.yml?style=flat-square&logo=githubactions&logoColor=white&label=CI&labelColor=1A1C1F&color=06C69C" alt="CI"></a>
  <a href="LICENSE"><img src="https://img.shields.io/github/license/MaximilianoRodrigoSoria/api-gateway?style=flat-square&labelColor=1A1C1F&color=06C69C" alt="License"></a>
  <img src="https://img.shields.io/github/last-commit/MaximilianoRodrigoSoria/api-gateway?style=flat-square&labelColor=1A1C1F&color=06C69C" alt="Last commit">
  <img src="https://img.shields.io/github/repo-size/MaximilianoRodrigoSoria/api-gateway?style=flat-square&labelColor=1A1C1F&color=06C69C" alt="Repo size">
  <a href="https://maximilianorodrigosoria.github.io/api-gateway/"><img src="https://img.shields.io/badge/GitHub_Pages-online-02ECB6?style=flat-square&logo=githubpages&logoColor=white&labelColor=1A1C1F" alt="Pages"></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-06C69C?style=flat-square&labelColor=1A1C1F&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Spring_Cloud_Gateway-•-06C69C?style=flat-square&labelColor=1A1C1F&logo=spring&logoColor=white" alt="Spring_Cloud_Gateway">
  <img src="https://img.shields.io/badge/Redis-•-06C69C?style=flat-square&labelColor=1A1C1F&logo=redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/Resilience4j-•-06C69C?style=flat-square&labelColor=1A1C1F" alt="Resilience4j">
  <img src="https://img.shields.io/badge/Docker-•-06C69C?style=flat-square&labelColor=1A1C1F&logo=docker&logoColor=white" alt="Docker">
</p>

# API Gateway

Unico punto de entrada por delante de un conjunto de microservicios independientes: resuelve rate limiting, autenticacion y enrutamiento una sola vez; cada servicio es autonomo y dueno de su base de datos.

> Proyecto de portafolio backend. Sigue el estandar de **arquitectura hexagonal (Ports & Adapters)**, Java 21 y Spring Boot, con quality gates (Spotless, Checkstyle, PMD, SpotBugs, ArchUnit), testing con Testcontainers y observabilidad (Micrometer + Prometheus).

## Caracteristicas

- Single entry point: los servicios internos no se exponen
- Rate limiting distribuido (por cliente / API key)
- Autenticacion y autorizacion centralizada (JWT)
- Enrutamiento por path/host a cada microservicio
- Resiliencia: timeouts, retry y circuit breaker
- Database-per-service: cada servicio dueno de su esquema
- Trazabilidad end-to-end (correlation id propagado)
- Despliegue y escalado independiente por servicio

## Stack

Java 21 · Spring Cloud Gateway · Redis · Resilience4j · Docker · Gradle · Flyway · Docker · JUnit 5 · Testcontainers

## Arquitectura

Organizado por **feature** en capas `domain -> application -> infrastructure`, con la regla de dependencia verificada por ArchUnit. La logica de negocio (dominio y casos de uso) no depende de framework ni de infraestructura; los adaptadores (web, persistencia, mensajeria) implementan puertos definidos por la aplicacion.

## API

Contexto `/api-gateway`. Todo lo que llega bajo `/gateway/**` se reenvia al downstream segun las rutas de `app.gateway.routes` (prefijo → target). `GET /api/v1/routes` lista las rutas. Aplica auth (cabecera Authorization), rate limiting y propagacion de `X-Correlation-Id`.

Ejemplo de config:
```yaml
app:
  gateway:
    require-auth: true
    routes:
      - { id: orders, path-prefix: /orders, target-uri: http://orders:8081 }
```

## Estado

✅ Nucleo funcional implementado: single entry point (`/gateway/**`), resolucion de rutas por prefijo mas largo, **reenvio al downstream con RestClient**, verificacion de auth, rate limiting (Resilience4j) y propagacion de correlation id. Tests (unit + integracion con WireMock como downstream real). Capa siguiente: descubrimiento de servicios, circuit breaker por ruta y strip de prefijo configurable.


<!-- diagrama-mermaid -->
## 📊 Arquitectura y flujo de un request

```mermaid
flowchart LR
    C["Cliente / Frontend"] -->|HTTP /gateway/**| GW

    subgraph GW["API Gateway (/api-gateway)"]
        direction TB
        A["Auth<br/>(Authorization header)"] --> R["Rate limiting<br/>(Resilience4j)"]
        R --> RT["Router<br/>(prefijo más largo)"]
        RT --> CID["Correlation-Id<br/>(X-Correlation-Id)"]
    end

    GW -->|RestClient| S1["orders-service"]
    GW -->|RestClient| S2["users-service"]
    GW -->|RestClient| S3["… microservicios"]

    RL[("Redis<br/>rate limit")] -.-> R
    GW -.->|/actuator/prometheus| OBS["Prometheus + Grafana"]

    S1 --- DB1[("DB orders")]
    S2 --- DB2[("DB users")]
```

> **Hexagonal (Ports & Adapters)** por feature: `domain → application → infrastructure`. El dominio y los casos de uso no conocen el framework; los adaptadores (web, persistencia, mensajería) implementan los puertos. La regla de dependencia la verifica **ArchUnit** en cada build.

---

<p align="center">
  <strong>Maximiliano Rodrigo Soria</strong><br>
  <a href="https://www.linkedin.com/in/soriamaximilianorodrigo/">LinkedIn</a> · <a href="mailto:maximilianorodrigosoria@gmail.com">maximilianorodrigosoria@gmail.com</a>
</p>
