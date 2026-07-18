# Changelog

Todas las modificaciones relevantes de este proyecto se documentan acá.
El formato sigue [Keep a Changelog](https://keepachangelog.com/es-ES/1.1.0/) y el versionado [SemVer](https://semver.org/lang/es/).

## [Unreleased]
### Planificado
- Descubrimiento de servicios (service discovery)
- Circuit breaker por ruta
- Strip de prefijo configurable por ruta

## [1.0.0] - 2026-07-17
### Añadido
- Single entry point: todo lo que llega bajo `/gateway/**` se reenvía al downstream.
- Resolución de rutas por prefijo más largo, configurable vía `app.gateway.routes`.
- Reenvío al downstream con `RestClient`.
- Verificación de autenticación (cabecera `Authorization`).
- Rate limiting distribuido con Resilience4j.
- Propagación de `X-Correlation-Id` end-to-end.
- Endpoint `GET /api/v1/routes` para listar las rutas configuradas.
- Arquitectura hexagonal por feature con regla de dependencia verificada por ArchUnit.
- Quality gates: Spotless, Checkstyle, PMD, SpotBugs.
- Tests unitarios e integración con Testcontainers y WireMock.
- Observabilidad con Micrometer + Prometheus y dashboards de Grafana.
- Manifiestos de Kubernetes y `docker-compose` para el entorno local.

[Unreleased]: https://github.com/MaximilianoRodrigoSoria/api-gateway/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/MaximilianoRodrigoSoria/api-gateway/releases/tag/v1.0.0
