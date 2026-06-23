# SGC - Sistema de Gestão Comercial (Loja de Informática)

Este repositório contém o código-fonte completo do **SGC (Sistema de Gestão Comercial)** desenvolvido para a disciplina de Desenvolvimento de Sistemas. A aplicação consiste em um ecossistema comercial robusto que integra uma interface Desktop ágil a uma API REST inteligente.

---

## 🚀 O que foi implementado no Projeto Final

### 1. Frontend Desktop (Interface Gráfica)
- **Java Swing:** Telas de Login e Painel Principal intuitivas e responsivas para o operador do sistema.
- **Integração Completa:** Comunicação via HTTP enviando e recebendo dados formatados em JSON (DTOs) para o Backend.

### 2. Controle de Estoque Automatizado & Transações
- **Controle Transacional:** Uso da anotação `@Transactional` no serviço de vendas, garantindo rollback automático caso ocorra qualquer falha no processo.
- **Validação em Tempo Real:** Sistema impede a venda se a quantidade solicitada for maior que o estoque, atualizando os valores (ex: de 10 para 9) instantaneamente no banco de dados após a confirmação.
- **Precisão Financeira:** Uso estrito de `BigDecimal` no Java e `DECIMAL` no MySQL para evitar furos de arredondamento monetário em cálculos de vendas.

### 3. Arquitetura em Camadas (Backend)
O projeto segue o padrão de mercado para divisão de responsabilidades:
- **Models/Entities:** Mapeamento ORM das tabelas de banco de dados.
- **Repositories:** Interfaces com Spring Data JPA para comunicação ágil com a base de dados.
- **Services:** Isolamento total das regras de faturamento e negócio.
- **Controllers:** Exposição dos endpoints REST e manipulação de requisições HTTP.

### 4. Segurança e Autenticação com Spring Security & JWT
- Integração do **Spring Security** com a biblioteca **Java JWT (Auth0)**.
- Geração de tokens JWT criptografados (HMAC256) com tempo de expiração de 2 horas.
- Bloqueio nativo de rotas privadas (ex: `/clientes` e `/produtos`) contra acessos não autenticados (**Status 403 Forbidden**).

### 5. Tratamento Global de Exceções
- Criação de um Handler centralizado com a anotação `@RestControllerAdvice` para intercepção de falhas e retornos JSON limpos e padronizados.

---

## 🛠️ Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA & Spring Security**
- **Java JWT (Auth0)**
- **Java Swing (Interface Desktop)**
- **MySQL**

---

## 🎲 Estrutura do Banco de Dados (Scripts SQL)

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
