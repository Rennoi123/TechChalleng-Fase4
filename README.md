# Tech Challenge - Fase 4 (Quarkus + AWS Lambda) — Java 17

## O que este projeto entrega
- Cloud + Serverless (AWS Lambda)
- 2 funções com responsabilidade única
- Endpoint `POST /avaliacao`
- Notificação automática de crítico (SNS -> email)
- Relatório semanal (média + contagens por dia e urgência) + envio por SNS
- Deploy automatizado (AWS SAM)
- Monitoramento (CloudWatch logs + Alarm) e tracing ativo

## Módulos
- `common`: modelos, repositório DynamoDB e serviços (SNS, relatório)
- `api-lambda`: Lambda HTTP com `POST /avaliacao`
- `report-lambda`: Lambda agendada para gerar relatório semanal

## Build
Na raiz:
```bash
mvn -q -DskipTests package
```

Saídas esperadas:
- `api-lambda/target/function.zip`
- `report-lambda/target/function.zip`

## Deploy (AWS SAM)
```bash
sam build
sam deploy --guided
```

Ao informar `AdminEmail`, confirme a inscrição que chega por email do SNS.

## Teste do endpoint
Após o deploy, use o output `ApiUrl`:

```bash
curl -X POST "$API_URL/avaliacao" \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Aula confusa", "nota": 2}'
```