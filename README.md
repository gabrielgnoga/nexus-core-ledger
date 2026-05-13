# Nexus Core Ledger

Motor de transações financeiras e gestão de portfólios projetado com foco absoluto em segurança, imutabilidade e alta performance.

##  Sobre o Projeto
O Nexus Core Ledger é uma API RESTful robusta desenvolvida para atuar como o núcleo de um sistema de contabilidade (Ledger). O projeto aplica conceitos avançados de engenharia de software para garantir a integridade de dados financeiros, blindagem de rotas e qualidade contínua de código, sem margem para dívida técnica.

##  Tech Stack & Ferramentas
* **Backend:** Java 21 (LTS), Spring Boot 3
* **Segurança:** Spring Security, JSON Web Tokens (JWT), Bcrypt Hashing
* **Banco de Dados:** PostgreSQL, Spring Data JPA
* **Qualidade & Testes:** JUnit 5, JaCoCo (Code Coverage), SonarCloud (Static Analysis & Security Audit)
* **DevOps & Infraestrutura:** Docker, GitHub Actions (Continuous Integration / Continuous Deployment)
* **Documentação:** Swagger / OpenAPI

##  Arquitetura e Padrões
* **Domain Model:** Entidades ricas com auto-validação (`@PrePersist`) e encapsulamento rigoroso para evitar inconsistências de saldo.
* **Camada de Segurança:** Autenticação *stateless* blindada com JWT e rastreamento claro de falhas através de exceções customizadas.
* **Pipeline CI/CD:** Esteira automatizada no GitHub Actions que valida testes matemáticos (JaCoCo) e bloqueia *merges* que não atinjam o *Quality Gate* rigoroso do SonarCloud.

##  Como Rodar (Local & Docker)

1. Clone o repositório:
   ```bash
   git clone [https://github.com/gabrielgnoga/nexus-core-ledger.git](https://github.com/gabrielgnoga/nexus-core-ledger.git)
2. Suba a infraestrutura do banco de dados em container:
   docker-compose up -d
3. Execute o projeto via Maven:
   ./mvnw spring-boot:run
4. Interaja com os *endpoints* através da documentação viva:
   * **Swagger UI:** `http://localhost:8080/swagger-ui.html`

##  Roadmap e Entregas (v1.0)
- [x] Estrutura Inicial e Configuração do Banco de Dados
- [x] Modelagem de Domínio Imutável (Account, Transaction)
- [x] Implementação de Repositories, Services e Controllers REST
- [x] Blindagem de Rotas com Spring Security e JWT
- [x] Integração de Testes Unitários com JUnit 5
- [x] Métrica de Cobertura de Testes com JaCoCo
- [x] Implementação de Esteira CI/CD via GitHub Actions
- [x] Auditoria Contínua e Zero Dívida Técnica com SonarCloud
- [x] Documentação interativa com Swagger/OpenAPI    
