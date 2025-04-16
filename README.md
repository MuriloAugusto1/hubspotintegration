# HubSpot Integration API

Este projeto é uma aplicação Spring Boot que realiza a integração com a API do HubSpot, incluindo autenticação via OAuth, criação de contatos e recebimento de webhooks.

---

## 🚀 Tecnologias utilizadas

- Java 21
- Spring Boot 3
- WebClient (Spring WebFlux)
- Maven
- JUnit 5 + Mockito

---

---

## 🛠️ Como configurar o HubSpot (App + Conta de Teste)

1. **Crie uma conta de desenvolvedor no HubSpot**  
   👉 Acesse: [https://developers.hubspot.com](https://developers.hubspot.com)

2. **Crie um aplicativo (app) OAuth 2.0**
    - Vá até o menu **"Apps"** e clique em **"Create app"**
    - Preencha os campos obrigatórios (nome, descrição)
    - Em **Auth**, selecione `OAuth 2.0`
    - Adicione os escopos necessários:
      ```
      crm.objects.contacts.read
      crm.objects.contacts.write
      ```
    - Defina o `redirect URI` como:
      ```
      http://localhost:8080/auth/callback
      ```

3. **Copie o `client ID` e `client secret`**

4. **Crie uma conta de teste (sandbox)**
    - No painel do app, clique em **"Test in your account"**
    - Crie uma **test account** (não use conta de desenvolvedor, pois não permite instalação)
    - Instale o app nessa conta

## 🔧 Como rodar localmente

1. **Clone o repositório**  
   ```bash
   git clone https://github.com/muriloaugusto1/hubspotintegration.git
   cd hubspotintegration
   ```

2. **Configure o `application.properties`** em `src/main/resources`:

   ```properties
   hubspot.client-id=SEU_CLIENT_ID
   hubspot.client-secret=SEU_CLIENT_SECRET
   hubspot.redirect-uri=http://localhost:8080/auth/callback
   hubspot.scopes=crm.objects.contacts.read crm.objects.contacts.write
   hubspot.access-token=SEU_ACCESS_TOKEN
   ```

3. **Execute a aplicação**
   ```bash
   ./mvnw spring-boot:run
   ```

---

## ✅ Endpoints disponíveis

| Método | Caminho             | Descrição                                 |
|--------|---------------------|-------------------------------------------|
| GET    | `/auth/url`         | Gera a URL de autorização OAuth           |
| GET    | `/auth/callback`    | Callback do OAuth HubSpot                 |
| POST   | `/contacts`         | Cria um novo contato no HubSpot           |
| POST   | `/webhook`          | Recebe eventos de contato via Webhook     |

---

## 🧪 Como rodar os testes

```bash
./mvnw test
```

---

---

## 🧾 Exemplo de payloads

### POST `/contacts`

```json
{
  "email": "murilo@example.com",
  "firstname": "Murilo",
  "lastname": "Augusto"
}
```

---

### POST `/webhook`

```json
[
  {
    "subscriptionType": "contact.creation",
    "objectId": 123456
  }
]
```

---

## 👤 Autor

Feito com 💻 por Murilo Augusto  
🔗 [LinkedIn](https://www.linkedin.com/in/muriloaugusto1)

---