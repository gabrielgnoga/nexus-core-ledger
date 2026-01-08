# Nexus Core Ledger

Motor de transações financeiras de alta performance, focado em integridade, auditoria e blindagem de dados.

## Sobre o Projeto
O Nexus Core Ledger é um sistema de contabilidade (Ledger) projetado para garantir a imutabilidade e rastreabilidade de transações financeiras. O projeto utiliza conceitos avançados de Domain-Driven Design (DDD) e Pattern de Blindagem para evitar inconsistências de saldo e dados.

## Tech Stack
* Java 21 (LTS)
* Spring Boot 3
* PostgreSQL (Banco de Dados)
* Docker (Containerização)
* Lombok (Produtividade)
* Bean Validation (Segurança de entrada de dados)

## Arquitetura
O projeto segue uma arquitetura em camadas focada em domínio:
* DTOs: Objetos de transferência imutáveis e validados.
* Domain Model: Entidades ricas com auto-validação (@PrePersist).
* Repository: Abstração de persistência segura.

## Como Rodar
1. Clone o repositório:
   git clone https://github.com/gabrielgnoga/nexus-core-ledger.git

2. Configure o banco de dados no application.properties (ou suba via Docker Compose).

3. Execute o projeto via Maven:
   ./mvnw spring-boot:run

## Roadmap
- [x] Estrutura Inicial e Dependências
- [x] Modelagem de Conta (Account) e DTOs
- [ ] Implementação do Repository e Service
- [ ] Controller e Endpoints REST
- [ ] Integração com Swagger/OpenAPI