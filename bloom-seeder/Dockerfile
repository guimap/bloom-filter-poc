FROM python:3.10-slim

WORKDIR /app
COPY populate_bloom.py .
COPY entrypoint.sh .

RUN pip install pymysql redis
RUN chmod +x entrypoint.sh

CMD ["/app/entrypoint.sh"]

# CMD ["python", "populate_bloom.py"]