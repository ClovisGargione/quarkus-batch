package br.com.sourcesystems.exporter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class MongoToCsvExporter {

    private MongoClient mongoClient;

    @ConfigProperty(name = "csv.export.dir", defaultValue = "exportados")
    private String exportDir;

    public MongoToCsvExporter(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void exportarCsv(@Observes StartupEvent event) {
        try {
            // Garante que o diretório existe
            Path dirPath = Paths.get(exportDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Caminho do arquivo
            Path filePath = dirPath.resolve("exporter.csv");

            // Leitura com cursor e escrita com buffer
            try (MongoCursor<Document> cursor = mongoClient
                    .getDatabase("arquivos")
                    .getCollection("registros")
                    .find()
                    .batchSize(1000) // Melhor performance com lotes grandes
                    .iterator();
                 BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {

                writer.write("nome;email;telefone;cpf;dataLeitura\n");

                while (cursor.hasNext()) {
                    Document doc = cursor.next();
                    String linha = String.join(";",
                            nullSafe(doc.getString("nome")),
                            nullSafe(doc.getString("email")),
                            nullSafe(doc.getString("telefone")),
                            nullSafe(doc.getString("cpf")),
                            nullSafe(doc.get("dataLeitura", Date.class))
                    );
                    writer.write(linha + "\n");
                }

                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace(); // Substitua por log se necessário
        }
    }

    // Método utilitário para evitar nulls no CSV
    private String nullSafe(Object value) {
        return value != null ? value.toString() : "";
    }
}