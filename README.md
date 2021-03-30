# Passos para executar a aplicação

Na pasta do projeto faça o build do projeto.

```bash
mvn clean install
```

Depois faça o build e rode o docker-compose em background do projeto com o seguiente comando

```bash
docker-compose up --build -d
```
Espere um minuto para inserir todos os dados, e pode fazer chamada Rest com essa estrutura

localhost:8090/produto/{nome do produto}?quantidadeLojas={quantidade de estabelecimentos}

Para derrubar o docker use o comando 

```bash
docker-compose down
```