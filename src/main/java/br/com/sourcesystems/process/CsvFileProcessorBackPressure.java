package br.com.sourcesystems.process;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CsvFileProcessorBackPressure {

     private static final Logger LOG = Logger.getLogger(CsvFileProcessorBackPressure.class);

    @ConfigProperty(name = "producer.batch.size", defaultValue = "100")
    private int batchSize;

    public Iterator<CSVRecord> parseCsv(Reader reader, CSVParser parser) throws IOException {
        LOG.info("Parse do arquivo csv ");
     
            CSVFormat format = CSVFormat.Builder.create()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .setDelimiter(';')
                .get();

            parser = format.parse(reader);
            return parser.iterator();
    }

}
