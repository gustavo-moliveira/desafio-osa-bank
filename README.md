
# 🏦 OSA Bank — Sistema de Transações Bancárias com CQRS, Redis e MongoDB

Este projeto simula um sistema bancário moderno, com separação clara entre escrita e leitura, cache de saldo, histórico de transações e autenticação via JWT. A arquitetura segue os padrões de CQRS e DDD, utilizando Redis e MongoDB como mecanismos de leitura otimizada.

---

## 🚀 Tecnologias Utilizadas

| Camada             | Tecnologia                     |
|--------------------|--------------------------------|
| Backend            | Java 17 + Spring Boot          |
| Banco Relacional   | PostgreSQL (ou MySQL)          |
| Cache              | Redis                          |
| Histórico          | MongoDB                        |
| Segurança          | JWT + Spring Security          |
| Arquitetura        | DDD + CQRS + Hexagonal         |
| Containerização    | Docker + Docker Compose        |
| Testes             | JUnit 5 + Mockito              |

---

## 🧠 Arquitetura
<img width="998" height="769" alt="image" src="https://github.com/user-attachments/assets/0045d8e8-9f3d-441a-afe0-dcfa613e6b35" />

---

## 🔐 Autenticação

- Registro via `/auth/register`
- Login via `/auth/login` → retorna JWT
- Endpoints restantes estão protegidos

---

## 🧮 Regras de Negócio

- Saques podem gerar dívida (`negativeDebt`)
- Depósitos quitam dívida com juros de 1,02%
- Saldo exibido já considera a dívida (pode ser negativo)
- Todos os valores são formatados com **duas casas decimais**

---

## 🐳 Docker Compose

O projeto inclui um `docker-compose.yml` com:

- **MongoDB**: para armazenar o histórico de transações
- **Redis**: para cache de saldo ajustado

### Comandos úteis

```bash
# Subir os containers
docker-compose up -d
```

---

## 📘 Collections de Teste

A collection para testes via [Hoppscotch](https://hoppscotch.io) estão disponíveis neste repositório:

📂 [`collection`](collection.json)

Incluem exemplos completos de:
- Registro e login
- Depósito e saque
- Consulta de saldo e histórico

---

## ✅ Checklist de Testes

| Cenário | Requisição | Resultado Esperado |
|--------|------------|--------------------|
| ✅ Registro válido | `POST /auth/register` | Retorna `userId` |
| ✅ Login válido | `POST /auth/login` | Retorna `token` |
| ✅ Depósito com saldo positivo | `POST /commands/deposit` | Saldo aumenta |
| ✅ Saque com saldo suficiente | `POST /commands/withdraw` | Saldo reduz |
| ✅ Saque com saldo insuficiente | `POST /commands/withdraw` | Saldo fica negativo |
| ✅ Depósito que quita dívida | `POST /commands/deposit` | Dívida quitada com juros |
| ✅ Consulta de saldo | `GET /queries/summary` | `SaldoTotal` ajustado com 2 casas decimais |
| ✅ Consulta de histórico | `GET /queries/summary` | Lista ordenada de transações |
| ✅ Valor inválido (zero ou negativo) | `POST /commands/deposit` | Erro 400 com mensagem clara |
| ✅ CPF ou login duplicado | `POST /auth/register` | Erro 422 |
| ✅ Token inválido | Qualquer endpoint protegido | Erro 401 |

---
## 🧱 Melhorias Técnicas Possíveis

### 🔍 Observabilidade
- Tracing distribuído com OpenTelemetry + Jaeger Para rastrear requisições ponta a ponta e diagnosticar gargalos;
- Exposição de métricas com Prometheus + Grafana Para monitorar uso, latência e erros em tempo real.

### 🗃️ Banco de Dados
- Versionamento de schema com Flyway.

### 🧪 Testes e Qualidade
- Testes de integração com Testcontainers Para validar fluxos completos com Redis, MongoDB e banco relacional em ambiente isolado;
- Cobertura de código com JaCoCo + SonarQube Para identificar pontos frágeis e garantir qualidade contínua.

