Documentação Completa do Projeto EducaBitDemo


O projeto EducaBitDemo é uma aplicação de backend robusta, desenvolvida utilizando o framework Spring Boot 3.x e a linguagem Java JDK 21 (LTS). O objetivo central deste sistema é prover uma API RESTful para o gerenciamento de dados educacionais, incluindo usuários, vídeos e diversas atividades. A arquitetura é construída em camadas, priorizando a separação de responsabilidades (Controller, Service, Repository), o que facilita a manutenção e a escalabilidade. Para a persistência de dados, o projeto utiliza o banco de dados PostgreSQL, sendo o controle de versão do esquema (estrutura do banco) gerenciado de forma eficiente pelo Flyway. A produtividade no desenvolvimento é maximizada pelo uso do Lombok, que automatiza a geração de código boilerplate (como getters e setters).

Funcionalidades e Estrutura
A API é estruturada em torno de diversos recursos (recursos), acessíveis através de seus respectivos endpoints (pontos de acesso).

Módulos Principais e Responsabilidades
UsuarioController: Responsável pela manipulação dos dados de usuários. Inclui operações básicas de CRUD (Criar, Ler, Atualizar, Deletar), como adicionar novos usuários via requisições POST e buscar usuários específicos por ID via GET. O mapeamento base das rotas é definido como /educabit/usuario.

VideoController: Gerencia as informações relativas aos vídeos educacionais, incluindo a persistência e consulta por título, utilizando a convenção do Spring Data JPA.

Outros Controladores: O projeto inclui módulos para gerenciar Atividades, TipoAcessibilidade e outros dados, garantindo uma cobertura abrangente dos requisitos do sistema.

Persistência de Dados e Migrações (Flyway)
A integridade estrutural do banco de dados é garantida pelo Flyway. O projeto utiliza migrações SQL que são executadas em ordem sequencial. É fundamental que cada script SQL tenha uma versão única e que a ordem de execução respeite as dependências de chaves estrangeiras (FOREIGN KEY). Por exemplo, se a tabela desplugada referencia a tabela tipoacessibilidade, o script que cria tipoacessibilidade deve ser numerado com uma versão anterior (ex: V2) àquela que cria desplugada (ex: V4). As migrações são armazenadas na pasta src/main/resources/db/migration e seguem o padrão V[Número]__[Descrição].sql.

Guia de Instalação e Execução (Passo a Passo)
Para configurar e rodar o projeto em seu ambiente local, siga as instruções detalhadas abaixo.

1. Pré-requisitos e Configuração Inicial
Instalação de Software: Instale o Java Development Kit (JDK) versão 21 (LTS) e o Git. Instale também o servidor de banco de dados PostgreSQL.

Configuração da IDE: Se estiver usando o IntelliJ IDEA, garanta que o plugin Lombok está instalado e que o Annotation Processing está habilitado nas configurações da IDE.

Configuração do PostgreSQL: Crie um novo banco de dados chamado EducaBit no seu servidor PostgreSQL.

2. Baixar o Projeto e Configurar Credenciais
Clonagem do Repositório: Abra seu terminal de preferência (PowerShell, CMD, Git Bash) e clone o código do projeto usando o Git.

Bash

git clone https://github.com/IsaqueGomesDev/EducaBitDemo.git
cd EducaBitDemo
Configuração do Banco de Dados: Edite o arquivo src/main/resources/application.properties e substitua SUA_SENHA_AQUI pela senha real do seu usuário postgres.

Properties

spring.datasource.url=jdbc:postgresql://localhost:5432/EducaBit
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA_AQUI
3. Compilação e Execução
Compilação via Maven: Execute o comando mvn clean install para baixar todas as dependências e compilar o projeto.

Execução da Aplicação: Abra o projeto na sua IDE. Localize a classe principal EducabitApplication.java e execute-a. O Flyway detectará a ausência das tabelas e aplicará todas as migrações sequencialmente antes de o servidor ser iniciado.

Confirmação: A aplicação estará pronta quando o console exibir a mensagem indicando que o Tomcat foi iniciado na porta 8080.

4. Teste da API com o Insomnia
Utilize o Insomnia (ou Postman) para interagir com os endpoints. Lembre-se de sempre usar o protocolo HTTP e a porta 8080.

Requisição POST (Adicionar Usuário):

Método: POST

URL: http://localhost:8080/educabit/usuario

Body (JSON): Certifique-se de que as chaves JSON estão entre aspas duplas.

JSON

{
  "username": "NovoUsuario",
  "type": "Aluno",
  "email": "novo@educabit.com",
  "password": "senha123"
}
Requisição GET (Buscar Usuário):

Método: GET

URL: http://localhost:8080/educabit/usuario/1 (substitua '1' pelo ID do usuário criado)
