package com.ar.laboratory.apigateway.gateway.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.ar.laboratory.apigateway.gateway.domain.model.RouteDefinition;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RouteRegistry")
class RouteRegistryTest {

    private final RouteRegistry registry =
            new RouteRegistry(
                    List.of(
                            new RouteDefinition("a", "/a", "http://a"),
                            new RouteDefinition("ab", "/ab", "http://ab")));

    @Test
    @DisplayName("resuelve por el prefijo más largo que matchea")
    void longestPrefix() {
        assertThat(registry.resolve("/abc")).get().extracting(RouteDefinition::id).isEqualTo("ab");
        assertThat(registry.resolve("/a/x")).get().extracting(RouteDefinition::id).isEqualTo("a");
    }

    @Test
    @DisplayName("sin match → vacío")
    void noMatch() {
        assertThat(registry.resolve("/otro")).isEmpty();
    }
}
