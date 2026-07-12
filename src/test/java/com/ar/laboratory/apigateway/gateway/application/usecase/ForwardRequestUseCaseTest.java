package com.ar.laboratory.apigateway.gateway.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ar.laboratory.apigateway.gateway.application.model.GatewayRequest;
import com.ar.laboratory.apigateway.gateway.application.model.GatewayResponse;
import com.ar.laboratory.apigateway.gateway.application.outbound.port.DownstreamClientPort;
import com.ar.laboratory.apigateway.gateway.application.service.RouteRegistry;
import com.ar.laboratory.apigateway.gateway.domain.exception.RouteNotFoundException;
import com.ar.laboratory.apigateway.gateway.domain.model.RouteDefinition;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ForwardRequestUseCase")
class ForwardRequestUseCaseTest {

    @Mock private DownstreamClientPort downstream;

    private final RouteRegistry registry =
            new RouteRegistry(List.of(new RouteDefinition("svc", "/svc", "http://target")));

    @Test
    @DisplayName("construye la URL del downstream (prefijo + path + query) y reenvía")
    void forwards() {
        when(downstream.forward(any(), any(), any(), any()))
                .thenReturn(new GatewayResponse(200, Map.of(), "ok".getBytes()));

        GatewayResponse resp =
                new ForwardRequestUseCase(registry, downstream)
                        .execute(
                                new GatewayRequest(
                                        "GET", "/svc/ping", "a=1", Map.of(), null));

        assertThat(resp.statusCode()).isEqualTo(200);
        verify(downstream)
                .forward(eq("GET"), eq("http://target/svc/ping?a=1"), any(), any());
    }

    @Test
    @DisplayName("sin ruta → RouteNotFound")
    void noRoute() {
        assertThatThrownBy(
                        () ->
                                new ForwardRequestUseCase(registry, downstream)
                                        .execute(
                                                new GatewayRequest(
                                                        "GET", "/otro", null, Map.of(), null)))
                .isInstanceOf(RouteNotFoundException.class);
    }
}
