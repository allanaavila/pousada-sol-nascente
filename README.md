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
