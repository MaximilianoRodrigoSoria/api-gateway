package com.ar.laboratory.apigateway.gateway.infrastructure.inbound.web.controller;

import com.ar.laboratory.apigateway.gateway.application.inbound.command.ForwardRequestCommand;
import com.ar.laboratory.apigateway.gateway.application.model.GatewayRequest;
import com.ar.laboratory.apigateway.gateway.application.model.GatewayResponse;
import com.ar.laboratory.apigateway.gateway.domain.exception.UnauthorizedException;
import com.ar.laboratory.apigateway.gateway.infrastructure.config.GatewayProperties;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Único punto de entrada: reenvía todo lo que llega bajo {@code /gateway/**} al servicio downstream
 * correspondiente, aplicando autenticación, rate limiting y propagación de correlation id.
 */
@RestController
@RequiredArgsConstructor
@RateLimiter(name = "gateway")
public class GatewayController {

    private static final String GATEWAY_PREFIX = "/gateway";

    private final ForwardRequestCommand forwardRequestCommand;
    private final GatewayProperties properties;

    @RequestMapping("/gateway/**")
    public ResponseEntity<byte[]> proxy(
            HttpServletRequest request, @RequestBody(required = false) byte[] body) {

        if (properties.isRequireAuth() && request.getHeader("Authorization") == null) {
            throw new UnauthorizedException();
        }

        String uri = request.getRequestURI();
        int idx = uri.indexOf(GATEWAY_PREFIX);
        String path = idx >= 0 ? uri.substring(idx + GATEWAY_PREFIX.length()) : uri;
        if (path.isEmpty()) {
            path = "/";
        }

        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, Collections.list(request.getHeaders(name)));
        }
        headers.putIfAbsent("X-Correlation-Id", List.of(UUID.randomUUID().toString()));

        GatewayResponse resp =
                forwardRequestCommand.execute(
                        new GatewayRequest(
                                request.getMethod(),
                                path,
                                request.getQueryString(),
                                headers,
                                body));

        HttpHeaders out = new HttpHeaders();
        resp.headers()
                .forEach(
                        (k, vs) -> {
                            if (isForwardableResponseHeader(k)) {
                                out.put(k, vs);
                            }
                        });
        return ResponseEntity.status(resp.statusCode()).headers(out).body(resp.body());
    }

    private static boolean isForwardableResponseHeader(String header) {
        String h = header.toLowerCase();
        return !(h.equals("transfer-encoding") || h.equals("content-length") || h.equals("connection"));
    }
}
