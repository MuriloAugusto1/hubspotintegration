package io.github.muriloaugusto1.hubspotintegration.service;

import io.github.muriloaugusto1.hubspotintegration.model.ContactRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ContactService {

    private final WebClient.Builder webClientBuilder;

    @Value("${hubspot.access-token}")
    private String accessToken;

    public ContactService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Map> createContact(ContactRequest contactRequest) {
        return webClientBuilder.build()
                .post()
                .uri("https://api.hubapi.com/crm/v3/objects/contacts")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "properties", Map.of(
                                "email", contactRequest.getEmail(),
                                "firstname", contactRequest.getFirstname(),
                                "lastname", contactRequest.getLastname()
                        )
                ))
                .retrieve()
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, clientResponse ->
                        Mono.error(new RuntimeException("Rate limit excedido!"))
                )
                .bodyToMono(Map.class);
    }
}
