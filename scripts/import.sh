#!/bin/bash
#!/bin/bash
set -e

echo "Aguardando MySQL estar pronto..."

until mysqladmin ping -h mysql -uroot -proot --silent; do
  sleep 2
done

echo "MySQL está pronto, iniciando importação..."

echo "Importando arquivos CSV em blocos..."

for file in /data/users_chunk_*; do
  echo "Importando $file"
  mysql -h mysql -uuser -ppassword --local-infile=1 myDB -h mysql <<EOF
LOAD DATA LOCAL INFILE '$file'
INTO TABLE users
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;
EOF
done

echo "Importação finalizada."