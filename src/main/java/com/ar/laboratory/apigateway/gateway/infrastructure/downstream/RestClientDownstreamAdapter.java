package com.ar.laboratory.apigateway.gateway.infrastructure.downstream;

import com.ar.laboratory.apigateway.gateway.application.model.GatewayResponse;
import com.ar.laboratory.apigateway.gateway.application.outbound.port.DownstreamClientPort;
import com.ar.laboratory.apigateway.shared.infrastructure.exception.InfrastructureException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/** Reenvía el request al downstream con {@link RestClient}, devolviendo su respuesta sin alterarla. */
@Component
public class RestClientDownstreamAdapter implements DownstreamClientPort {

    private static final Set<String> SKIP_REQUEST_HEADERS =
            Set.of("host", "content-length", "connection", "transfer-encoding");

    private final RestClient restClient = RestClient.create();

    @Override
    public GatewayResponse forward(
            String method, String url, Map<String, List<String>> headers, byte[] body) {
        try {
            RestClient.RequestBodySpec spec =
                    restClient.method(HttpMethod.valueOf(method.toUpperCase())).uri(URI.create(url));
            if (headers != null) {
                headers.forEach(
                        (k, vs) -> {
                            if (!SKIP_REQUEST_HEADERS.contains(k.toLowerCase())) {
                                vs.forEach(v -> spec.header(k, v));
                            }
                        });
            }
            RestClient.ResponseSpec responseSpec =
                    (body != null && body.length > 0)
                            ? spec.body(body).retrieve()
                            : spec.retrieve();
            ResponseEntity<byte[]> resp =
                    responseSpec
                            .onStatus(status -> true, (request, response) -> {})
                            .toEntity(byte[].class);
            return new GatewayResponse(
                    resp.getStatusCode().value(), new HashMap<>(resp.getHeaders()), resp.getBody());
        } catch (Exception e) {
            throw new InfrastructureException("Error reenviando al downstream: " + url, e);
        }
    }
}
