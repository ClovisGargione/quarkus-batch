package br.com.sourcesystems.process;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;


import br.com.sourcesystems.model.Registro;
import jakarta.batch.api.chunk.ItemReader;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Dependent
@Named(value = "CsvReader")
public class CsvReader implements ItemReader {

    private static final Logger LOG = Logger.getLogger(CsvReader.class);
    
    private Iterator<CSVRecord> iterator;

    @ConfigProperty(name = "app.files.dir")
    private String filesDir;

    @ConfigProperty(name = "app.files.processed.dir")
    private String filesProcessedDir;

    @Inject
    private CsvFileProcessorBackPressure processorBackPressure;

    private Reader reader;

    private CSVParser parser;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        File dir = new File(filesDir);
        if (!dir.exists() || !dir.isDirectory()) {
            LOG.warn("Diretório de arquivos não encontrado: " + filesDir);
            return;
        }

        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));
        if (files == null || files.length == 0) {
            return;
        }

        // Para simplificar, vamos pegar só o primeiro arquivo
        File file = files[0];
        Path origem = file.toPath();
        Path destino = Paths.get(filesProcessedDir, file.getName());

        try {
            Files.createDirectories(destino.getParent());
            Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);
            LOG.info("Arquivo movido para: " + destino.toAbsolutePath());
        } catch (IOException e) {
            LOG.error("Erro ao mover arquivo", e);
            return;
        }
        reader = Files.newBufferedReader(destino.toFile().toPath());
        this.iterator = processorBackPressure.parseCsv(reader, parser);
    }

    @Override
    public Object readItem() {
        if (iterator != null && iterator.hasNext()) {
            CSVRecord record = iterator.next();
            return new Registro(
                record.get("nome"),
                record.get("email"),
                record.get("telefone"),
                record.get("cpf")
            );
        }
        return null; // Finaliza a leitura
    }

    @Override
    public Serializable checkpointInfo() {
        return null; // Se quiser usar checkpointing, implemente um contador de linha
    }

	@Override
	public void close() throws Exception {
        if (parser != null) parser.close();
        if (reader != null) reader.close();
	}
}