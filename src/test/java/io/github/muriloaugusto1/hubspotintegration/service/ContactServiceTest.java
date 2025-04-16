package io.github.muriloaugusto1.hubspotintegration.service;

import io.github.muriloaugusto1.hubspotintegration.model.ContactRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContactServiceTest {

    private ContactService contactService;
    private WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @BeforeEach
    void setup() throws Exception {
        webClientBuilder = mock(WebClient.Builder.class);
        webClient = mock(WebClient.class);
        when(webClientBuilder.build()).thenReturn(webClient);

        contactService = new ContactService(webClientBuilder);

        // Injeta o accessToken via reflection
        setField(contactService, "accessToken", "fake-access-token");
    }

    @Test
    void deveCriarContatoComSucesso() {
        // Mock encadeado do WebClient
        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        @SuppressWarnings("unchecked")
        WebClient.RequestHeadersSpec<?> headersSpec = (WebClient.RequestHeadersSpec<?>) mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString())).thenReturn(bodySpec);
        when(bodySpec.header(anyString(), anyString())).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any())).thenAnswer(invocation -> headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);

        Map<String, Object> mockResponse = Map.of("id", "123", "status", "ok");
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(reactor.core.publisher.Mono.just(mockResponse));

        ContactRequest request = new ContactRequest("murilo@example.com", "Murilo", "Augusto");

        Map result = contactService.createContact(request).block();

        assertNotNull(result);
        assertEquals("123", result.get("id"));
        assertEquals("ok", result.get("status"));
    }

    private static void setField(Object target, String fieldName, String value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
