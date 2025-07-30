package br.com.sourcesystems.process;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import br.com.sourcesystems.model.Registro;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Dependent
@Named("MongoWriter")
public class MongoWriter extends AbstractItemWriter {
    @Inject
    MongoClient mongoClient;

    @Override
    public void writeItems(List<Object> items) {
        MongoCollection<Document> collection = mongoClient.getDatabase("meu_banco").getCollection("registros");
        List<Document> docs = items.stream()
            .map(item -> {
                Registro r = (Registro) item;
                return new Document("nome", r.nome)
                           .append("email", r.email)
                           .append("telefone", r.telefone)
                           .append("cpf", r.cpf)
                           .append("dataLeitura", r.dataLeitura);
            }).collect(Collectors.toList());
        collection.insertMany(docs);
    }
}