# Tech Challenge – Fase 4 (Quarkus + AWS Lambda | Java 17)

## Arquitetura
Arquitetura serverless orientada a eventos, utilizando 3 funções AWS Lambda, cada uma com responsabilidade única.

1. Feedback API Lambda
2. Notification Lambda
3. Weekly Report Lambda

## Tecnologias
AWS Lambda, API Gateway, DynamoDB, EventBridge, SNS, CloudWatch, Java 17, Quarkus, AWS SAM

## Deploy
mvn clean package -DskipTests
sam build
sam deploy --guided