package io.github.muriloaugusto1.hubspotintegration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping
    public ResponseEntity<String> receiveWebhook(@RequestBody List<Map<String, Object>> events) {
        for (Map<String, Object> event : events) {
            String type = (String) event.get("subscriptionType");

            if ("contact.creation".equals(type)) {
                Object contactId = event.get("objectId");
                logger.info("Novo contato criado no HubSpot. ID: {}", contactId);
            } else {
                logger.debug("Evento recebido, mas ignorado: {}", type);
            }
        }

        return ResponseEntity.ok("Webhook processado com sucesso");
    }
}
