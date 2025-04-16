package io.github.muriloaugusto1.hubspotintegration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebhookController.class)
@AutoConfigureMockMvc(addFilters = false)
class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveProcessarEventoDeCriacaoDeContato() throws Exception {
        String body = """
            [
              {
                "subscriptionType": "contact.creation",
                "objectId": 123456
              }
            ]
        """;

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("Webhook processado com sucesso"));
    }

    @Test
    void deveIgnorarEventoDesconhecido() throws Exception {
        String body = """
            [
              {
                "subscriptionType": "contact.update",
                "objectId": 987654
              }
            ]
        """;

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("Webhook processado com sucesso"));
    }

    @Test
    void deveLidarComListaVaziaSemErro() throws Exception {
        String body = "[]";

        mockMvc.perform(post("/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().string("Webhook processado com sucesso"));
    }
}

