#!/bin/bash
FLAG_PATH="/data/importer_done.flag"

echo "Aguardando sinal do importer em $FLAG_PATH..."
while [ ! -f "$FLAG_PATH" ]; do
  sleep 5
done

echo "Sinal recebido, iniciando bloom-seeder..."
exec python populate_bloom.py