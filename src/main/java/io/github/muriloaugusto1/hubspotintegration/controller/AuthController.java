package io.github.muriloaugusto1.hubspotintegration.controller;

import io.github.muriloaugusto1.hubspotintegration.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class AuthController {

    private final AuthService authService;

    @Value("${hubspot.client-id}")
    private String clientId;

    @Value("${hubspot.redirect-uri}")
    private String redirectUri;

    @Value("${hubspot.scopes}")
    private String scopes;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/auth/url")
    public String generateAuthorizationUrl() {
        String baseUrl = "https://app.hubspot.com/oauth/authorize";

        return String.format("%s?client_id=%s&redirect_uri=%s&scope=%s&response_type=code",
                baseUrl,
                clientId,
                URLEncoder.encode(redirectUri, StandardCharsets.UTF_8),
                URLEncoder.encode(scopes, StandardCharsets.UTF_8));
    }

    @GetMapping("/auth/callback")
    public Map handleCallback(@RequestParam("code") String code) {
        return authService.exchangeCodeForToken(code).block();
    }
}

