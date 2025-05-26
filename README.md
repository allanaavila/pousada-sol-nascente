
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


# Módulo Funcionário

Este módulo da **API da Pousada Sol Nascente** é responsável pela administração e gerenciamento dos dados dos funcionários da pousada. Ele permite o controle completo do ciclo de vida de um funcionário no sistema, incluindo seus perfis de acesso.

## ✅ Funcionalidades Implementadas

As seguintes operações estão disponíveis para o gerenciamento de funcionários:

### 📥 Cadastro de Funcionários
Permite o registro de novos funcionários no sistema, com validações para dados como **CPF**, **e-mail** e **telefone**.

### 🛡️ Definição de Perfis
Para cada funcionário, é possível definir um **perfil de acesso** entre:

- `USUARIO`
- `GERENTE`

Esses perfis controlam as permissões futuras no sistema.

### 🔍 Consulta de Funcionários
- Busca os detalhes de um funcionário específico por seu **ID**.
- Lista todos os funcionários cadastrados.

### ✏️ Alteração de Funcionários
Atualiza as informações de funcionários existentes, incluindo:

- Dados pessoais
- Cargo
- Perfil

Com validações para garantir a integridade e unicidade dos dados.

### 🚫 Desativação de Funcionários
Altera o status de um funcionário para **inativo**, mantendo seu registro no banco de dados para fins históricos, mas impedindo seu acesso e interações no sistema.

### ✅ Reativação de Funcionários
Permite **reativar** um funcionário que foi previamente desativado, restaurando seu acesso.

---

## 🏗️ Estrutura do Módulo

O módulo de funcionários é composto pelas seguintes camadas:

### 📦 Model
- `Funcionario`: Entidade JPA que representa o funcionário no banco de dados.

### 📦 DTO
- `FuncionarioDTO`: Objeto de transferência de dados para comunicação com a API.

### 📦 Enum
- `Perfil`: Enumeração que define os tipos de perfil:
    - `USUARIO`
    - `GERENTE`

### 📦 Repository
- `FuncionarioRepository`: Interface para operações de acesso a dados.

### 📦 Service
- `FuncionarioService` / `FuncionarioServiceImpl`: Camada de **lógica de negócio e validações**.

### 📦 Controller
- `FuncionarioController`: Endpoints **REST** para interação com o módulo.

---

