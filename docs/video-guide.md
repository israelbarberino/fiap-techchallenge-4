# Roteiro de gravacao - fiap-techchallenge-4

## Objetivo da apresentacao

Mostrar que o projeto esta funcionando end-to-end, com foco em decisoes de arquitetura, tradeoffs e demonstracao pratica do fluxo principal.

## Roteiro sugerido

### 1. Abertura

- Apresentar o problema: registro e tratamento de feedbacks em um backend serverless.
- Reforcar a decisao confirmada do projeto: manter `POST /feedback` com `content` e `urgency`, sem migrar para outro modelo de contrato.
- Explicar rapidamente a stack: Java 21, Quarkus 3, Maven, AWS Lambda, SAM, DynamoDB, EventBridge e SES.

### 2. Decisoes e tradeoffs

- Justificar arquitetura hexagonal: separacao clara entre dominio, use cases e adaptadores.
- Explicar o uso de um unico artefato Lambda com `QUARKUS_LAMBDA_HANDLER` para manter o deploy simples.
- Comentar o tradeoff de usar `Scan` no relatorio semanal: suficiente para escopo academico, mas nao ideal para alto volume.
- Explicar o uso de `Resource: '*'` no SES: simplifica a demonstracao e depende da validacao de identidades no SES.
- Destacar que o SAM ficou apenas com infraestrutura e deploy, enquanto o empacotamento fica no Maven/Quarkus.

### 3. Mostrando a arquitetura

- Abrir o `template.yaml` e apontar as 3 Lambdas.
- Mostrar que cada Lambda tem seu `QUARKUS_LAMBDA_HANDLER`.
- Mostrar a tabela DynamoDB `Feedback`.
- Mostrar a regra `FeedbackCreatedRule` e a `WeeklyReportScheduleRule`.
- Mostrar a separacao de responsabilidades entre handlers, use cases e adapters.

### 4. Demonstracao funcionando localmente

- Subir a API com `sam local start-api --template template.yaml`.
- Abrir o Postman com o environment `fiap-techchallenge-4`.
- Executar `Healthcheck` em `GET /health` e mostrar `200 {"status":"ok"}`.
- Executar `Create feedback` em `POST /feedback` com payload valido.
- Mostrar a resposta com `feedbackId`.
- Se quiser, repetir com urgencia `CRITICAL` para explicar o caminho que aciona a notificacao.

### 5. Demonstracao na AWS

- Trocar o `baseUrl` do Postman para `https://<HttpApiEndpoint>`.
- Executar novamente o `Healthcheck` e o `Create feedback`.
- Explicar que o deploy usa `mvn package` + `sam deploy`.
- Mostrar rapidamente os outputs da stack e, se possivel, os logs no CloudWatch.

### 6. Fechamento

- Resumir o que foi entregue: API, persistencia, evento, notificacao e relatorio semanal.
- Reforcar que a implementacao seguiu o recorte do projeto sem adicionar Spring, Docker, Terraform ou microsservicos.
- Encerrar apontando a conformidade com o fluxo pedido e a cobertura de testes.

## Resumo dos endpoints

### GET /health

- Finalidade: checagem de disponibilidade da API.
- Resposta esperada: `200` com `{"status":"ok"}`.
- Uso no video: mostrar que a API esta viva antes do fluxo principal.

### POST /feedback

- Finalidade: registrar feedback.
- Payload:

```json
{
  "content": "Excelente atendimento",
  "urgency": "MEDIUM"
}
```

- Resposta esperada: `201` com o `feedbackId` gerado.
- Uso no video: demonstrar gravacao no DynamoDB e publicacao do evento.

## Payloads de demonstracao

### Caso normal

```json
{
  "content": "Excelente atendimento",
  "urgency": "MEDIUM"
}
```

### Caso critico

```json
{
  "content": "Falha grave no atendimento",
  "urgency": "CRITICAL"
}
```

## Ordem pratica para a gravacao

1. Mostrar a arquitetura no README e no template.
2. Abrir o Postman com o environment local.
3. Rodar o healthcheck.
4. Rodar o create feedback com urgencia MEDIUM.
5. Rodar o create feedback com urgencia CRITICAL.
6. Trocar o environment para AWS.
7. Repetir healthcheck e create feedback na AWS.
8. Finalizar mostrando testes e cobertura.
