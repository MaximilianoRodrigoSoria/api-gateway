package com.ar.laboratory.apigateway.gateway.application.model;

import java.util.List;
import java.util.Map;

/** Respuesta del servicio downstream, tal cual, para devolverla al cliente. */
public record GatewayResponse(int statusCode, Map<String, List<String>> headers, byte[] body) {}
