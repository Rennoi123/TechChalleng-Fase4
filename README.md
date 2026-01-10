# Tech Challenge – Fase 4

**Serverless Feedback Platform (Quarkus + AWS Lambda + DynamoDB + SNS)**

## Visão Geral

Este projeto implementa uma **plataforma serverless de feedback** para avaliações de aulas online, utilizando **AWS** como provedor de nuvem e **Quarkus** como framework Java.

A aplicação permite:

- Receber feedbacks dos alunos via API REST
- Persistir feedbacks em banco NoSQL (DynamoDB)
- Enviar **notificações automáticas por e-mail** para feedbacks críticos
- Gerar **relatórios semanais** automaticamente
- Monitorar erros via **CloudWatch**

Toda a infraestrutura é criada via **AWS SAM (Infrastructure as Code)**.

---

## Arquitetura da Solução

### Componentes utilizados

- **AWS Lambda**
    - `ApiFunction`: recebe feedbacks via HTTP
    - `WeeklyReportFunction`: gera relatório semanal
- **Amazon API Gateway (HTTP API)**
- **Amazon DynamoDB**
- **Amazon SNS**
- **Amazon CloudWatch**
- **AWS SAM (Serverless Application Model)**
- **Quarkus (Java 17)**

### Fluxo principal

1. Aluno envia feedback (`POST /avaliacao`)
2. API Gateway aciona `ApiFunction`
3. Feedback é salvo no DynamoDB
4. Se crítico, notificação é enviada via SNS (e-mail)
5. `WeeklyReportFunction` roda automaticamente a cada 7 dias
6. CloudWatch monitora erros da API

---

## Funções Serverless (Responsabilidade Única)

### ApiFunction

**Responsabilidade:**

Receber feedbacks e disparar notificações críticas.

- Trigger: API Gateway (HTTP)
- Endpoint:

```
POST /avaliacao

```

**Payload esperado:**

```json
{
"descricao":"Aula muito confusa",
"nota":2
}

```

---

### WeeklyReportFunction

**Responsabilidade:**

Gerar relatório semanal dos feedbacks.

- Trigger: EventBridge Scheduler
- Frequência: a cada 7 dias
- Gera relatório consolidado e envia via SNS

---

## Banco de Dados (DynamoDB)

Tabela criada automaticamente via SAM:

- **Nome:** `FeedbackTable`
- **Chave primária:** `id` (String)
- **Billing:** `PAY_PER_REQUEST` (free tier friendly)

Armazena:

- Descrição
- Nota
- Urgência
- Data de envio

---

## Notificações (SNS)

- Um **tópico SNS** é criado automaticamente
- Um **e-mail administrador** é cadastrado no deploy
- ⚠️ **É obrigatório confirmar o e-mail do SNS** (link enviado pela AWS)

> Sem confirmação, notificações NÃO são entregues.
> 

---

## Monitoramento

- **CloudWatch Logs** para todas as Lambdas
- **CloudWatch Alarm**:
    - Dispara quando a API gera erros (`Errors >= 1`)

---

## Deploy Automatizado (AWS SAM)

### Pré-requisitos

- Java 17
- Maven
- AWS CLI
- AWS SAM CLI
- Conta AWS (Free Tier)

---

### Configurar credenciais AWS

```bash
aws configure

```

Informar:

- **AWS Access Key ID**
- **AWS Secret Access Key**
- **Default region:** `us-east-1`
- **Output format:** `json`

---

### Build do projeto

```bash
mvn -DskipTests package

```

---

### Build SAM

```bash
sam build

```

---

### Deploy guiado (primeira vez)

```bash
sam deploy --guided

```

**Respostas recomendadas:**

| Pergunta | Resposta |
| --- | --- |
| Stack Name | `techchallenge-fase4` |
| AWS Region | `us-east-1` |
| AdminEmail | *seu e-mail* |
| Confirm changes | `y` |
| Allow IAM role creation | `y` |
| Disable rollback | `y` |
| API sem auth | `y` |
| Save config | `y` |

---

### samconfig.toml utilizado

```toml
version =0.1

[default.deploy.parameters]
stack_name ="techchallenge-fase4"
resolve_s3 =true
s3_prefix ="techchallenge-fase4"
confirm_changeset =true
capabilities ="CAPABILITY_IAM"
disable_rollback =true
parameter_overrides ="AdminEmail=\"rennan.ornelas@gmail.com\" AwsRegionParam=\"us-east-1\""
image_repositories = []

[default.global.parameters]
region ="us-east-1"

```

Após isso, deploys futuros podem ser feitos com:

```bash
sam deploy

```

---

## Como testar a aplicação

### Testar API (Postman)

- URL exibida no final do deploy:

```
https://xxxx.execute-api.us-east-1.amazonaws.com

```

- Endpoint:

```
POST /avaliacao

```

- Headers:

```
Content-Type: application/json

```

---

### Testar WeeklyReportFunction

Opções:

- **Executar manualmente** pelo console AWS Lambda
- Aguardar execução automática (7 dias)
- Ver logs em:

```
CloudWatch → Loggroups → /aws/lambda/WeeklyReportFunction

```

---

## Segurança e Governança

- IAM Roles criadas automaticamente
- Políticas mínimas:
    - DynamoDB (read/write)
    - SNS (publish)
- Sem credenciais hardcoded
- Infraestrutura declarativa (IaC)

---

## Estrutura do Projeto

```
techchallenge-fase4-quarkus-aws-lambda
│
├── common
│   └── código compartilhado (modelo, repositório, serviços)
│
├── api-lambda
│   └── Lambda de API (POST /avaliacao)
│
├── report-lambda
│   └── Lambda de relatório semanal
│
├── template.yaml
├── samconfig.toml
└── README.md

```
