FROM python:3.9.21

WORKDIR /app

COPY requirements.txt requirements.txt

RUN pip install --upgrade pip

RUN pip install -r requirements.txt

EXPOSE 5000

COPY . .

CMD ["python3", "api.py"]