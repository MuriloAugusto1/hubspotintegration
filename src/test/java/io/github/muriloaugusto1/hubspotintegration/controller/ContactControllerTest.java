package io.github.muriloaugusto1.hubspotintegration.controller;

import io.github.muriloaugusto1.hubspotintegration.model.ContactRequest;
import io.github.muriloaugusto1.hubspotintegration.service.ContactService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc(addFilters = false)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Test
    void deveCriarContatoComSucesso() throws Exception {
        String json = """
            {
              "email": "murilo@example.com",
              "firstname": "Murilo",
              "lastname": "Augusto"
            }
        """;

        Map<String, Object> respostaMock = Map.of(
                "id", 123456,
                "status", "success"
        );

        Mockito.when(contactService.createContact(Mockito.any(ContactRequest.class)))
                .thenReturn(Mono.just(respostaMock));

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(123456))
                .andExpect(jsonPath("$.status").value("success"));
    }
}
