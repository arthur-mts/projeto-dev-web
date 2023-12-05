# Sistema de apostas
## Apostador
- Criar apostador
- Editar apostador
- Listar apostadores
- Deletar apostador (TODO)

## Concurso
- Criar concurso
- Rodar concurso (realizar o sorteio e retornar ganhadores)
- Listar concursos (sorteados ou não sorteados)
- Cancelar concurso (e invalidar todas as apostas)

## Apostas
- Criar apostas
- Listar apostas por concurso
- Listar apostas por apostador

Rodar o banco:
```shell
docker container run --name projeto-dev-web-db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_ROOT_HOST=% -e MYSQL_DATABASE=anuncios -p 3306:3306 -d mysql:latest
```