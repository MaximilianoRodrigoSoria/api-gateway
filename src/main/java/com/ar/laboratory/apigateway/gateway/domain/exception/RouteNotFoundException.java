package com.ar.laboratory.apigateway.gateway.domain.exception;

/** No hay ninguna ruta configurada para el path solicitado. */
public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(String path) {
        super("Sin ruta para el path: " + path);
    }
}
