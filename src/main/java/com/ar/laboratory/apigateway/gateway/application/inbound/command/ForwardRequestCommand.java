package com.ar.laboratory.apigateway.gateway.application.inbound.command;

import com.ar.laboratory.apigateway.gateway.application.model.GatewayRequest;
import com.ar.laboratory.apigateway.gateway.application.model.GatewayResponse;

/** Puerto de entrada: reenviar un request a través del gateway. */
public interface ForwardRequestCommand {
    GatewayResponse execute(GatewayRequest request);
}
