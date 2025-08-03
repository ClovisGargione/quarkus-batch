package br.com.sourcesystems.process;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import br.com.sourcesystems.model.Registro;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;

@Dependent
@Named("MongoWriter")
public class MongoWriter extends AbstractItemWriter {

    MongoClient mongoClient;

    public MongoWriter(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void writeItems(List<Object> items) {
        MongoCollection<Document> collection = mongoClient.getDatabase("meu_banco").getCollection("registros");
        List<Document> docs = items.stream()
            .map(item -> {
                Registro r = (Registro) item;
                return new Document("nome", r.getNome())
                           .append("email", r.getEmail())
                           .append("telefone", r.getTelefone())
                           .append("cpf", r.getCpf())
                           .append("dataLeitura", r.getDataLeitura());
            }).toList();
        collection.insertMany(docs);
    }
}