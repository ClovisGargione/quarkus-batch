package br.com.sourcesystems.process;

import java.time.Instant;

import org.jboss.logging.Logger;

import br.com.sourcesystems.model.Registro;
import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;

@Dependent
@Named("RegistroProcessor")
public class RegistroProcessor implements ItemProcessor {

    private static final Logger LOG = Logger.getLogger(RegistroProcessor.class);

    @Override
    public Object processItem(Object item) {
        LOG.info("Processando o arquivo csv...");
        Registro registro = (Registro) item;
        registro.dataLeitura = Instant.now();
        return registro;
    }
}