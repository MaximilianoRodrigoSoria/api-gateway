package com.ar.laboratory.apigateway.gateway.application.model;

import java.util.List;
import java.util.Map;

/** Request entrante a reenviar: método, path (post-/gateway), query, headers y cuerpo. */
public record GatewayRequest(
        String method,
        String path,
        String query,
        Map<String, List<String>> headers,
        byte[] body) {}
