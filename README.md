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


## Rodar local
Rodar o banco:
```shell
docker container run -d -e MYSQL_ROOT_PASSWORD=root -e MYSQL_ROOT_HOST=% -e MYSQL_DATABASE=anuncios --network=host mysql:latest
```

Rodar a aplicação:
```shell
docker image build . -t backend
docker container run -e DATASOURCE_URL=jdbc:mysql://localhost:3306/anuncios --network=host backend:latesty
```

## Configurar o EC2 na AWS

- Instalar o Docker
  - `sudo yum install docker`
  - `sudo service docker enable`
  - `sudo service docker start`
  - `sudo groupadd docker`
  - `sudo usermod -a -G docker ec2-user`
  - `newgrp docker`
- Fazer o git pull do projeto
- Reiniciar a maquina