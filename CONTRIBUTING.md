# Contribuir

¡Gracias por tu interés en mejorar **API Gateway**! Este es un proyecto de portafolio backend, pero las contribuciones son bienvenidas.

## Flujo de trabajo

1. Hacé un **fork** del repo.
2. Creá una rama descriptiva: `feature/mi-cambio` o `fix/algo-roto`.
3. Implementá el cambio con sus **tests** (unit y/o integración con Testcontainers/WireMock).
4. Verificá localmente los quality gates antes de commitear:
   ```bash
   ./gradlew clean build
   ```
   Esto corre Spotless, Checkstyle, PMD, SpotBugs, ArchUnit y la suite de tests.
5. Usá **Conventional Commits** (`feat:`, `fix:`, `chore:`, `docs:`, `test:`, `refactor:`).
6. Abrí un **Pull Request** contra `main`, con un cambio enfocado por PR.

## Estilo

- Código en inglés donde sea idiomático; documentación en español.
- Respetá la **arquitectura hexagonal**: el dominio y los casos de uso no dependen de framework ni de infraestructura. La regla de dependencia está verificada por ArchUnit — si la rompés, el build falla.
- Un cambio, un PR. PRs chicos y revisables.

## Reportar problemas

Usá las plantillas de issue (bug / feature). Para temas de seguridad, ver [SECURITY.md](SECURITY.md).

---
Maintainer: **Maximiliano Rodrigo Soria** · maximilianorodrigosoria@gmail.com
