package io.github.muriloaugusto1.hubspotintegration.controller;

import io.github.muriloaugusto1.hubspotintegration.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "hubspot.client-id=test-client-id",
        "hubspot.redirect-uri=http://localhost:8080/auth/callback",
        "hubspot.scopes=contacts"
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void deveGerarUrlDeAutorizacao() throws Exception {
        mockMvc.perform(get("/auth/url"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("https://app.hubspot.com/oauth/authorize")))
                .andExpect(content().string(containsString("client_id=test-client-id")))
                .andExpect(content().string(containsString("redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fauth%2Fcallback")))
                .andExpect(content().string(containsString("scope=contacts")));
    }

    @Test
    void deveTrocarCodePorToken() throws Exception {
        Map<String, Object> tokenMock = Map.of(
                "access_token", "123",
                "refresh_token", "456"
        );

        Mockito.when(authService.exchangeCodeForToken("abc123"))
                .thenReturn(Mono.just(tokenMock));

        mockMvc.perform(get("/auth/callback")
                        .param("code", "abc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("123"))
                .andExpect(jsonPath("$.refresh_token").value("456"));
    }
}
