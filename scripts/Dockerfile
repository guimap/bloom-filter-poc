FROM python:3.10-slim

WORKDIR /scripts
COPY import.sh .
COPY bloom_seed.py .

# Instala dependências Python e o cliente MySQL
RUN pip install redis pymysql && \
    apt-get update && \
    apt-get install -y default-mysql-client && \
    rm -rf /var/lib/apt/lists/*

RUN chmod +x import.sh

CMD ["/scripts/import.sh"]