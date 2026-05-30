# Risk Analysis API

> API REST de análise de risco de crédito em Java com Spring Boot — projeto de portfólio com foco em backend sólido.

---

## Contexto e Problema de Negócio

Instituições financeiras precisam classificar o risco de crédito de clientes de forma padronizada e explicável. A análise manual é lenta e inconsistente — diferentes analistas chegam a conclusões diferentes para o mesmo perfil.

**O desafio:** automatizar a classificação com transparência, retornando sempre o motivo da decisão.

---

## A Solução

Uma API REST que recebe dados financeiros e aplica regras de negócio para classificar o risco em três níveis.

**Entrada:**
```json
{
  "name": "Leticia Lopez",
  "cpf": "123.456.789-00",
  "monthlyIncome": 5000,
  "score": 450,
  "debts": 3500
}
```

**Saída:**
```json
{
  "riskLevel": "ALTO",
  "justification": "Score: 450 | Dívida: 70.0% da renda"
}
```

---

## Regras de Classificação

| Condição | Nível |
|----------|-------|
| Score > 700 e dívida < 30% da renda | BAIXO |
| Score < 500 ou dívida > 60% da renda | ALTO |
| Demais casos | MÉDIO |

A lógica de classificação foi implementada no `RiskAnalysisService`:

```java
private String calculateRiskLevel(Double score, Double debts, Double income) {
    double debtRatio = debts / income;

    if (score > 700 && debtRatio < 0.3) return "BAIXO";
    if (score < 500 || debtRatio > 0.6) return "ALTO";
    return "MEDIO";
}
```

A justificativa é gerada automaticamente com os valores reais:

```java
private String buildJustification(Double score, Double debts, Double income) {
    double debtRatio = debts / income * 100;
    return String.format("Score: %.0f | Dívida: %.1f%% da renda", score, debtRatio);
}
```

---

## Arquitetura

```text
com/riskanalysis/api/
├── controller/     → entrada HTTP, validação de request
├── exception/      → tratamento global de erros
├── model/          → entidades JPA (Person, RiskAnalysis)
├── repository/     → acesso ao banco via Spring Data JPA
└── service/        → regras de negócio e classificação de risco
```

A arquitetura em camadas isola responsabilidades: o Controller não conhece regras de negócio, o Service não conhece HTTP, o Repository não conhece classificação. Cada camada evolui de forma independente.

---

## Validações

Dados inválidos são rejeitados na entrada, antes de chegar ao banco:

```java
// Person.java
@NotBlank(message = "Nome é obrigatório")
private String name;

@NotBlank(message = "CPF é obrigatório")
private String cpf;

@NotNull(message = "Renda mensal é obrigatória")
@Positive(message = "Renda mensal deve ser positiva")
private Double monthlyIncome;
```

O `@Valid` no Controller ativa as validações na entrada da requisição:

```java
@PostMapping
public ResponseEntity<Person> save(@Valid @RequestBody Person person) {
    return ResponseEntity.ok(personService.save(person));
}
```

**Request inválido:**
```json
{ "cpf": "123.456.789-00", "monthlyIncome": 5000 }
```

**Resposta — 400 Bad Request:**
```json
{ "name": "Nome é obrigatório" }
```

---

## Tratamento de Erros

O `GlobalExceptionHandler` centraliza o tratamento de erros para toda a API:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Registro não encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
```

Sem ele, o Spring retornaria stack traces genéricos. Com ele, qualquer erro retorna uma resposta padronizada e legível.

---

## Endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /api/persons | Cadastrar pessoa |
| GET | /api/persons | Listar todas as pessoas |
| GET | /api/persons/{id} | Buscar pessoa por ID |
| PUT | /api/persons/{id} | Atualizar pessoa |
| DELETE | /api/persons/{id} | Remover pessoa |
| POST | /api/risk/analyze/{personId} | Realizar análise de risco |
| GET | /api/risk/history/{personId} | Histórico de análises |

---

## Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5 | Framework backend |
| Spring Data JPA | - | Acesso ao banco de dados |
| Spring Validation | - | Validação de dados de entrada |
| PostgreSQL | - | Banco de dados relacional |
| Lombok | - | Redução de boilerplate |
| SpringDoc OpenAPI | 2.8.6 | Documentação automática (Swagger) |
| Maven | - | Gerenciador de dependências |

---

## Roadmap

### v1.0 — MVP ✓
- [x] Setup do projeto (Spring Boot, Maven, PostgreSQL, Lombok)
- [x] Modelagem das entidades (Person, RiskAnalysis)
- [x] Repositórios com Spring Data JPA
- [x] Regras de classificação de risco no Service
- [x] Controllers e endpoints REST
- [x] Configuração via variáveis de ambiente (.env)
- [x] Testes manuais no Postman

### v1.1 — Qualidade ✓
- [x] Validações de entrada com Spring Validation
- [x] Tratamento global de erros (GlobalExceptionHandler)
- [x] Documentação interativa com Swagger

### v1.2 — Testes
- [ ] Testes unitários no RiskAnalysisService (JUnit 5)
- [ ] Testes de integração nos endpoints (MockMvc)
- [ ] Cobertura mínima de 80%

### v1.3 — Evolução
- [ ] Validação de formato de CPF
- [ ] Paginação no GET /api/persons
- [ ] Endpoint de resumo estatístico por nível de risco
- [ ] Containerização com Docker

### v2.0 — IA
- [ ] Integração com modelo de linguagem para análise contextual
- [ ] Score dinâmico baseado em histórico do cliente
- [ ] Webhook para notificação de análises críticas
---

## Como Rodar

### Pré-requisitos
- Java 21
- PostgreSQL
- Maven

### 1. Clone o repositório
```bash
git clone https://github.com/leticiagiovannalopez/risk.analysis.api.git
cd risk.analysis.api
```

### 2. Configure o `.env`
```env
DB_URL=jdbc:postgresql://localhost:5432/riskanalysis
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha
```

### 3. Rode
```bash
./mvnw spring-boot:run
```

### 4. Documentação interativa

http://localhost:8080/swagger-ui.html

---

## O que evoluiria

Testes unitários no `RiskAnalysisService` com JUnit — especialmente nos casos limite das regras de classificação (score exatamente 700, dívida exatamente 30%). É a próxima etapa natural do projeto.

---

**Leticia Giovanna Lopez**
[GitHub](https://github.com/leticiagiovannalopez) • [LinkedIn](https://linkedin.com/in/leticiagiovannalopez)
