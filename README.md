# Pousada Sol Nascente API

Este repositório contém a API RESTful para o sistema de administração e controle de reservas da **Pousada Sol Nascente**.  
O objetivo é fornecer uma plataforma robusta para gerenciar **quartos, serviços, reservas e dados de clientes**, além de oferecer um portal atrativo para os hóspedes.

---

## ✅ Funcionalidades Implementadas (Módulo Cliente)

Até o momento, o foco principal tem sido o desenvolvimento e a estabilização das funcionalidades relacionadas à administração de **clientes (hóspedes)**. As seguintes operações estão disponíveis:

* **Cadastro de Clientes (Hóspedes):** Permite o registro de novos clientes com validações de dados (**CPF, e-mail, telefone**).

* **Busca de Cliente por ID:** Recupera os detalhes de um cliente específico utilizando seu **identificador único**.

* **Alteração de Dados do Cliente:** Atualiza informações de clientes existentes, com validações para garantir a **integridade dos dados**.

* **Desativação de Cliente:** Altera o status de um cliente para **inativo**, mantendo seu histórico sem permitir novas interações no sistema.

* **Reativação de Cliente:** Permite reativar um cliente previamente desativado.

* **Listagem de Todos os Clientes:** Retorna uma lista completa de todos os clientes cadastrados no sistema.

---

## Tecnologias Utilizadas

* **Linguagem:** Java

* **Framework:** Spring Boot (**versão 3.4.5**)

* **Gerenciador de Dependências:** Maven

* **Persistência:** Spring Data JPA

* **Banco de Dados:** PostgreSQL (configurado via **Docker Compose**)

* **Utilitários:** Lombok (para reduzir boilerplate code)

* **Testes:** JUnit 5 e Mockito (para testes unitários de serviços e controladores)

---

## Configuração do Ambiente

Para rodar o projeto localmente, é necessário ter:

* **Java Development Kit (JDK):** Versão 21 (ou compatível, conforme `pom.xml`).

* **Maven:** Para gerenciamento de dependências e build.

* **Docker e Docker Compose:** Para subir o banco de dados PostgreSQL.

O arquivo `docker-compose.yml` na raiz do projeto configura o serviço de banco de dados PostgreSQL. Para iniciá-lo, execute na raiz do projeto:

```bash
docker-compose up -d
```
---
As configurações de conexão com o banco de dados estão em src/main/resources/application.properties.

Próximos Passos
O desenvolvimento continua na branch feature/endpoint-funcionario, onde serão implementadas as funcionalidades relacionadas à administração de funcionários (cadastro, perfis de usuário/gerente, etc.).

Este README.md será atualizado continuamente à medida que novas funcionalidades forem implementadas e o projeto evoluir.

