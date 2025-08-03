# Processamento Batch com JBeret e MongoDB

Este projeto demonstra o uso de processamento batch utilizando a especificação Jakarta Batch (JBeret) em conjunto com Quarkus, com foco na robustez, simplicidade e eficiência. Os dados processados são armazenados no MongoDB.

---

## 📌 Visão Geral

- Leitura de arquivos CSV
- Processamento item a item
- Escrita no MongoDB
- Job batch configurado por XML ou programaticamente

---

## 🚀 Tecnologias Utilizadas

- [Quarkus](https://quarkus.io/)
- [JBeret](https://jberet.github.io/)
- [MongoDB](https://www.mongodb.com/)
- [Apache Commons CSV](https://commons.apache.org/proper/commons-csv/)

---

## ⚙️ Execução

### 🛠 Pré-requisitos
- Java 21+
- MongoDB em execução
- Maven 3.9+

### ▶️ Rodando a aplicação

```bash
./mvnw compile quarkus:dev
