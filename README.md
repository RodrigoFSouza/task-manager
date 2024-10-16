# TaskManager

TaskManager é uma aplicação de gerenciamento de tarefas desenvolvida com **Java 17** e **Spring Boot 3.3.4**. A aplicação utiliza **MySQL** como banco de dados, é containerizada com **Docker**, possui documentação de API com **Swagger**, autenticação via **JWT (Bearer token)**, e migrações de banco de dados gerenciadas pelo **Flyway**. O build do projeto é feito utilizando **Gradle** e conta com testes de unidade e integração.

## Funcionalidades
- **Autenticação JWT**: Acesso seguro através de tokens Bearer.
- **Gerenciamento de Tarefas**: Criação, edição, exclusão e visualização de tarefas.
- **Migração de banco de dados**: Utiliza Flyway para controlar versões e migrações.
- **APIs documentadas com Swagger**: Facilita o consumo e teste das APIs.
- **Testes**: Cobertura com testes de unidade e integração.

## Requisitos

- **Java 17**
- **Gradle**
- **Docker**
- **MySQL**

## Configuração do Projeto

### Banco de Dados

Certifique-se de ter um banco de dados MySQL configurado. Altere as propriedades de conexão no arquivo `application.properties`

```yaml
spring.datasource.url=jdbc:mysql://localhost:3306/tododb
spring.datasource.username=root
spring.datasource.password=root
```

## Flyway
O Flyway será utilizado para executar as migrações do banco de dados. As migrações ficam em src/main/resources/db/migration.

## Docker
Para rodar a aplicação em um contêiner Docker, utilize o Dockerfile presente no projeto. Para criar a imagem Docker e executar o contêiner:

```
docker build -t taskmanager .
docker run -p 8080:8080 taskmanager
```

## Gradle
Para construir e rodar o projeto localmente:

```
./gradlew clean build
./gradlew bootRun
```
## Autenticação JWT
A aplicação utiliza autenticação via Bearer Token. Ao fazer login, um token **JWT** será gerado e deve ser enviado em cada requisição autenticada no cabeçalho:

```
Authorization: Bearer {token}
```

## Documentação da API

A documentação da API pode ser acessada através do Swagger na URL:
```
http://localhost:8080/swagger-ui.html
```

## Testes
O projeto inclui testes de unidade e integração. Para rodar os testes:
```
./gradlew test
```

## Licença
Este projeto está sob a licença MIT.