package io.github.muriloaugusto1.hubspotintegration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private AuthService authService;
    private WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @BeforeEach
    void setup() throws Exception {
        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        when(webClientBuilder.build()).thenReturn(webClient);

        authService = new AuthService(webClientBuilder);

        // Injeção das propriedades simuladas via reflection
        setField(authService, "clientId", "fake-client-id");
        setField(authService, "clientSecret", "fake-client-secret");
        setField(authService, "redirectUri", "http://localhost:8080/auth/callback");
    }

    @Test
    void deveTrocarCodePorTokenComSucesso() {
        // Mocks encadeados
        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestHeadersSpec<?> headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(uriSpec);
        when(uriSpec.header(eq("Content-Type"), eq("application/x-www-form-urlencoded"))).thenReturn(uriSpec);
        when(uriSpec.bodyValue(contains("authorization_code"))).thenAnswer(invocation -> headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        Map<String, Object> mockToken = Map.of("access_token", "abc123");
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(mockToken));

        Map result = authService.exchangeCodeForToken("codigo123").block();

        assertNotNull(result);
        assertEquals("abc123", result.get("access_token"));
    }

    // Utilitário para injetar propriedades privadas
    private static void setField(Object target, String fieldName, String value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
