import requests
import time
import csv
import random
import os

API_BASE = "http://localhost:3000/api"
CSV_PATH = "./data/users_chunk_aa"
TOTAL_EXISTENTES = 5
TOTAL_INEXISTENTES = 5

def carregar_emails_existentes(csv_path, quantidade):
    if not os.path.exists(csv_path):
        raise FileNotFoundError(f"Arquivo CSV não encontrado: {csv_path}")

    with open(csv_path, newline='', encoding="utf-8") as csvfile:
        reader = csv.DictReader(csvfile)
        emails = [row["email"] for row in reader]
    
    return random.sample(emails, quantidade)

def gerar_emails_inexistentes(quantidade):
    return [f"fake_email_{i}@dominiofake.com" for i in range(quantidade)]

def benchmark(endpoint_suffix, emails):
    print(f"\n🔍 Benchmarkando: {API_BASE}/{endpoint_suffix}")
    total_time = 0
    resultados = {"Existe": 0, "Não existe": 0}

    for email in emails:
        start = time.time()
        response = requests.get(f"{API_BASE}/{endpoint_suffix}", params={"email": email})
        duration = time.time() - start
        total_time += duration

        resp_text = response.text
        if response.status_code == 200:
            resultados["Existe"] += 1
        elif response.status_code == 204:
            resultados["Não existe"] += 1
        else:
            print(f"⚠️ Resposta inesperada para {email}: {resp_text}")

    avg_time = total_time / len(emails)
    print(f"📊 Resultados de /{endpoint_suffix}:")
    print(f"⏱️  Tempo médio: {avg_time * 1000:.2f} ms")
    for key, val in resultados.items():
        print(f"🔸 {key}: {val} / {len(emails)}")


if __name__ == "__main__":
    print("📥 Lendo CSV e gerando amostras...")
    emails_existentes = carregar_emails_existentes(CSV_PATH, TOTAL_EXISTENTES)
    emails_inexistentes = gerar_emails_inexistentes(TOTAL_INEXISTENTES)
    all_emails = emails_existentes + emails_inexistentes
    random.shuffle(all_emails)

    benchmark("bloom", all_emails)
    benchmark("no-bloom", all_emails)
