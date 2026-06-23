# SGC - Sistema de Gestão Comercial (Loja de Informática)

Este repositório contém o código-fonte do backend do **SGC (Sistema de Gestão Comercial)** desenvolvido para a disciplina de Desenvolvimento de Sistemas. A aplicação consiste em uma API REST robusta construída com o ecossistema Spring.

---

##  O que foi implementado na Entrega 2

### 1. Arquitetura em Camadas (Refatoração)
O projeto foi totalmente reestruturado seguindo o padrão de mercado para divisão de responsabilidades:
- **Models/Entities:** Mapeamento das tabelas de banco de dados.
- **Repositories:** Interfaces com Spring Data JPA para comunicação com a base de dados.
- **Services:** Isolamento total das regras de negócio.
- **Controllers:** Exposição dos endpoints REST e manipulação de requisições HTTP.

### 2. Segurança e Autenticação com Spring Security & JWT
- Integração do **Spring Security** com a biblioteca **Java JWT (Auth0)**.
- Implementação de endpoints públicos para cadastro e login de usuários.
- Geração de tokens JWT criptografados (HMAC256) com tempo de expiração de 2 horas.
- Bloqueio nativo de rotas privadas (ex: `/clientes` e `/produtos`) contra acessos não autenticados (**Status 403 Forbidden**).

### 3. Tratamento Global de Exceções
- Criação de um Handler centralizado com a anotação `@RestControllerAdvice`.
- Intercepção de falhas de validação e erros de banco de dados para o retorno de payloads JSON limpos e padronizados com os devidos códigos HTTP.

### 4. Padrões de Projeto (Design Patterns)
- **Data Transfer Object (DTO):** Encapsulamento seguro dos dados de requisições de autenticação.
- **Repository Pattern:** Abstração completa da camada de persistência.

---

##  Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Security**
- **Java JWT (Auth0)**
- **MySQL**

---

##  Estrutura do Banco de Dados (Scripts SQL)

O banco de dados utiliza a seguinte estrutura relacional para suporte ao sistema:

```sql
CREATE TABLE usuarios ( 
    id BIGINT AUTO_INCREMENT PRIMARY KEY, 
    username VARCHAR(50) UNIQUE NOT NULL, 
    senha VARCHAR(255) NOT NULL, 
    perfil ENUM('ADMIN', 'FUNCIONARIO') NOT NULL 
);

CREATE TABLE clientes ( 
    id BIGINT AUTO_INCREMENT PRIMARY KEY, 
    nome VARCHAR(100) NOT NULL, 
    cpf VARCHAR(14) UNIQUE NOT NULL, 
    email VARCHAR(100), 
    telefone VARCHAR(20), 
    endereco VARCHAR(255) 
);

CREATE TABLE produtos ( 
    id BIGINT AUTO_INCREMENT PRIMARY KEY, 
    nome VARCHAR(100) NOT NULL, 
    descricao TEXT, 
    preco DECIMAL(10,2) NOT NULL, 
    quantidade_estoque INT NOT NULL 
);

CREATE TABLE vendas ( 
    id BIGINT AUTO_INCREMENT PRIMARY KEY, 
    data_venda DATETIME DEFAULT CURRENT_TIMESTAMP, 
    cliente_id BIGINT, 
    usuario_id BIGINT, 
    valor_total DECIMAL(10,2), 
    FOREIGN KEY (cliente_id) REFERENCES clientes(id), 
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) 
);

CREATE TABLE itens_venda ( 
    id BIGINT AUTO_INCREMENT PRIMARY KEY, 
    venda_id BIGINT, 
    produto_id BIGINT, 
    quantidade INT, 
    preco_unitario DECIMAL(10,2), 
    FOREIGN KEY (venda_id) REFERENCES vendas(id), 
    FOREIGN KEY (produto_id) REFERENCES produtos(id) 
);
