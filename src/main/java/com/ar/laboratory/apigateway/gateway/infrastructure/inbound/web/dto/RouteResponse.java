package com.ar.laboratory.apigateway.gateway.infrastructure.inbound.web.dto;

/** Vista de una ruta configurada. */
public record RouteResponse(String id, String pathPrefix, String targetUri) {}
