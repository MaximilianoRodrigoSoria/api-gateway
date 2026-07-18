# Política de seguridad

## Reportar una vulnerabilidad

Si encontrás una vulnerabilidad de seguridad en **API Gateway**, reportala de forma responsable:

- Escribí a **maximilianorodrigosoria@gmail.com**, o
- Abrí un issue con la etiqueta `security` **sin incluir detalles de explotación** públicos.

Respondo a la brevedad y coordino la divulgación de manera responsable. Por favor, dame un plazo razonable para publicar un fix antes de divulgar públicamente.

## Alcance

Al ser un gateway (punto único de entrada), son especialmente relevantes: bypass de autenticación/autorización, evasión del rate limiting, inyección de cabeceras, SSRF hacia servicios internos y filtrado de `X-Correlation-Id` u otros datos sensibles.

---
Maintainer: **Maximiliano Rodrigo Soria** · maximilianorodrigosoria@gmail.com
