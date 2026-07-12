package com.ar.laboratory.apigateway.gateway.infrastructure.web;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Tests de integración del gateway: reenvía a un downstream real simulado con WireMock. Necesita
 * PostgreSQL (el scaffold trae JPA) más el stub de WireMock.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"spring.jpa.hibernate.ddl-auto=validate"})
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
@DisplayName("Gateway - Integration Tests")
class GatewayIT {

    @Container @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    static final WireMockServer wireMock;

    static {
        wireMock = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMock.start();
        wireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/svc/ping"))
                        .willReturn(
                                WireMock.aResponse()
                                        .withStatus(200)
                                        .withHeader("Content-Type", "text/plain")
                                        .withBody("pong")));
    }

    @DynamicPropertySource
    static void routes(DynamicPropertyRegistry registry) {
        registry.add("app.gateway.require-auth", () -> "false");
        registry.add("app.gateway.routes[0].id", () -> "svc");
        registry.add("app.gateway.routes[0].path-prefix", () -> "/svc");
        registry.add("app.gateway.routes[0].target-uri", () -> "http://localhost:" + wireMock.port());
    }

    @AfterAll
    static void stopWireMock() {
        wireMock.stop();
    }

    @LocalServerPort private int port;
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    @DisplayName("reenvía /gateway/svc/ping al downstream (WireMock) → 200 'pong'")
    void forwards() {
        client.get()
                .uri("/api-gateway/gateway/svc/ping")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("pong");
    }

    @Test
    @DisplayName("ruta no configurada → 404")
    void unknownRoute() {
        client.get()
                .uri("/api-gateway/gateway/desconocido/x")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @DisplayName("GET /api/v1/routes lista las rutas configuradas")
    void listsRoutes() {
        client.get()
                .uri("/api-gateway/api/v1/routes")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$[0].id")
                .isEqualTo("svc");
    }
}
