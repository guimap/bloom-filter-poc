import sys
import csv
import os
import redis
import zlib

redis_host = os.environ.get("REDIS_HOST", "redis")
redis_port = int(os.environ.get("REDIS_PORT", "6379"))
redis_password = os.environ.get("REDIS_PASSWORD", None)
bit_size = int(os.environ.get("BLOOM_BITSIZE", "48000000"))
hash_count = int(os.environ.get("BLOOM_HASHCOUNT", "7"))
redis_key = os.environ.get("BLOOM_REDIS_KEY", "bloom:usuarios")

def hash_fn(value, seed, bit_size):
    h = zlib.crc32((value + ":" + str(seed)).encode())
    return abs(h % bit_size)

r = redis.Redis(host=redis_host, port=redis_port, password=redis_password)

csv_file = sys.argv[1]
count = 0

with open(csv_file, newline='', encoding='utf-8') as f:
    reader = csv.DictReader(f)
    for row in reader:
        email = row.get("email")
        if email:
            for i in range(hash_count):
                pos = hash_fn(email, i, bit_size)
                r.setbit(redis_key, pos, 1)
            count += 1
            if count % 100_000 == 0:
                print(f"🚀 {count} e-mails processados do arquivo {csv_file}...")

print(f"✅ Bloom Filter populado com sucesso para {csv_file}!")