import pymysql
import redis
import zlib
import time
import os

# Config via env vars
mysql_host = os.environ.get("MYSQL_HOST", "mysql")
mysql_user = os.environ.get("MYSQL_USER", "root")
mysql_password = os.environ.get("MYSQL_PASSWORD", "root")
mysql_db = os.environ.get("MYSQL_DATABASE", "bloomdb")

redis_host = os.environ.get("REDIS_HOST", "redis")
redis_port = int(os.environ.get("REDIS_PORT", "6379"))
redis_password = os.environ.get("REDIS_PASSWORD", None)

bit_size = int(os.environ.get("BLOOM_BITSIZE", "48000000"))
hash_count = int(os.environ.get("BLOOM_HASHCOUNT", "7"))
redis_key = os.environ.get("BLOOM_REDIS_KEY", "bloom:usuarios")

print(f"USER: {mysql_user} | PASS: {mysql_password}")
print("⏳ Aguardando banco e Redis...")
time.sleep(10)

# Conexões
conn = pymysql.connect(host="mysql", user="user", password="password", database="myDB")
cursor = conn.cursor()
r = redis.Redis(host=redis_host, port=redis_port, password=redis_password)

# Função de hash
def hash_fn(value, seed, bit_size):
    h = zlib.crc32((value + ":" + str(seed)).encode())
    return abs(h % bit_size)

# Processamento
cursor.execute("SELECT email FROM users")
count = 0
for (email,) in cursor:
    for i in range(hash_count):
        pos = hash_fn(email, i, bit_size)
        r.setbit(redis_key, pos, 1)
    count += 1
    if count % 100_000 == 0:
        print(f"🚀 {count} e-mails processados...")

print("✅ Bloom Filter populado com sucesso!")
