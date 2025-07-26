# Bloom-filter po
## Como iniciar

### 1. Gere os arquivo .csv com 5kk de linhas
```sh
cd js-db-dataset
node index.mjs
```
Irá gerar um arquivo chamado `users.csv` na raiz do projeto

### 2. Divida os arquivos em menores.
> Caso esteja na pasta do passo 1, volte um nivel
```sh
./split.sh
```
Isso irá gerar varios arquivos dentro da pasta `data/`

```
- data
    users_chunk_aa
    users_chunk_ab
    ...
```

### 3. Execute o docker
Execute o docker-compose
```sh
docker-compose up
```
