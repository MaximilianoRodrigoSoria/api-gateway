package com.ar.laboratory.apigateway.gateway.domain.exception;

/** Falta autenticación para atravesar el gateway. */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Autenticación requerida");
    }
}
