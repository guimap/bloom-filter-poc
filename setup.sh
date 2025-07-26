#!/bin/bash
echo "Gerando dados de teste..."
cd js-db-dataset
node index.mjs
cd ..

echo "Dividindo dados em blocos..."
./split.sh

# echo "Iniciando o docker Compose..."
# docker-compose down && docker-compose up -d --build