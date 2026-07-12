package com.ar.laboratory.apigateway.gateway.application.outbound.port;

import com.ar.laboratory.apigateway.gateway.application.model.GatewayResponse;
import java.util.List;
import java.util.Map;

/** Puerto de salida: reenvía el request al servicio downstream y devuelve su respuesta. */
public interface DownstreamClientPort {
    GatewayResponse forward(
            String method, String url, Map<String, List<String>> headers, byte[] body);
}
