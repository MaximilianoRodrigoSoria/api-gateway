package com.ar.laboratory.apigateway.gateway.infrastructure.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** Configuración del gateway ({@code app.gateway.*}). */
@Data
@ConfigurationProperties(prefix = "app.gateway")
public class GatewayProperties {

    /** Exigir cabecera Authorization para atravesar el gateway. */
    private boolean requireAuth = true;

    /** Rutas configuradas. */
    private List<Route> routes = new ArrayList<>();

    /** Una ruta: prefijo → destino. */
    @Data
    public static class Route {
        private String id;
        private String pathPrefix;
        private String targetUri;
    }
}
