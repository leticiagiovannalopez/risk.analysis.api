# Risk Analysis API

API backend de análise de risco de crédito desenvolvida em Java com Spring Boot.  
O sistema recebe dados financeiros de uma pessoa e retorna uma classificação de risco (**BAIXO**, **MÉDIO** ou **ALTO**) com justificativa, simulando o comportamento de sistemas reais usados em bancos e fintechs.

---

## Contexto e Problema de Negócio

Instituições financeiras precisam avaliar o risco de crédito de clientes de forma rápida e padronizada. A análise manual é lenta e inconsistente — diferentes analistas podem chegar a conclusões diferentes para o mesmo perfil.

**O desafio:** como automatizar a classificação de risco de crédito de forma transparente, explicando o motivo de cada decisão?

---

## Solução

Uma API REST que recebe dados financeiros e aplica regras de negócio para classificar o risco, retornando sempre uma justificativa clara da decisão.

**Exemplo de uso:**

Entrada:
```json
{
  "name": "Leticia Lopez",
  "cpf": "123.456.789-00",
  "monthlyIncome": 5000,
  "score": 450,
  "debts": 3500
}
```

Saída:
```json
{
  "riskLevel": "ALTO",
  "justification": "Score: 450 | Dívida: 70.0% da renda"
}
```

---

## Regras de Classificação

| Condição | Nível |
|---|---|
| Score > 700 e dívida < 30% da renda | BAIXO |
| Score < 500 ou dívida > 60% da renda | ALTO |
| Demais casos | MÉDIO |

---

## Arquitetura

```
src/
└── main/
    └── java/
        └── com/riskanalysis/api/
            ├── controller/     → recebe as requisições HTTP
            ├── service/        → regras de negócio e lógica de classificação
            ├── repository/     → acesso ao banco de dados
            └── model/          → entidades Person e RiskAnalysis
```

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5 | Framework backend |
| Spring Data JPA | - | Acesso ao banco de dados |
| PostgreSQL | - | Banco de dados relacional |
| Lombok | - | Redução de boilerplate |
| Maven | - | Gerenciador de dependências |

---

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/persons` | Cadastrar pessoa |
| GET | `/api/persons` | Listar todas as pessoas |
| GET | `/api/persons/{id}` | Buscar pessoa por ID |
| POST | `/api/risk/analyze` | Realizar análise de risco |
| GET | `/api/risk/history/{personId}` | Histórico de análises de uma pessoa |

---

## Roadmap

- [x] Setup do projeto (Spring Boot, Maven, PostgreSQL, Lombok)
- [x] Modelagem das entidades (`Person`, `RiskAnalysis`)
- [x] Repositórios com Spring Data JPA
- [x] Services com lógica de classificação de risco
- [ ] Controllers e endpoints REST
- [ ] Configuração do PostgreSQL
- [ ] Testes no Postman
- [ ] Validações e tratamento de erros
- [ ] Documentação com Swagger

---

## Como Rodar o Projeto

### Pré-requisitos
- Java 21
- PostgreSQL
- Maven

### 1. Clone o repositório
```bash
git clone https://github.com/leticiagiovannalopez/risk.analysis.api.git
cd risk.analysis.api
```

### 2. Configure o banco de dados

Em `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/riskanalysis
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### 3. Rode o projeto
```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`

---

## Lições Aprendidas

- **Separação de responsabilidades:** a arquitetura em camadas (Controller → Service → Repository) torna o código mais organizado e fácil de manter
- **Spring Data JPA:** elimina a necessidade de escrever SQL manual para operações básicas
- **Regras de negócio isoladas:** concentrar a lógica de classificação no Service facilita testes e manutenção

---

## Desenvolvido por

**Leticia Giovanna Lopez**  
[GitHub](https://github.com/leticiagiovannalopez) • [LinkedIn](https://linkedin.com/in/leticiagiovannalopez)

*Projeto de portfólio — API backend em Java demonstrando classificação de risco de crédito com regras de negócio reais.*