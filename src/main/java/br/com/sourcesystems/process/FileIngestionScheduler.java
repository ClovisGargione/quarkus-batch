package br.com.sourcesystems.process;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.logging.Logger;

import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.batch.operations.JobOperator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class FileIngestionScheduler {

    private static final Logger LOG = Logger.getLogger(FileIngestionScheduler.class);

    private final AtomicBoolean rodando = new AtomicBoolean(false);

    @Inject
    private JobOperator jobOperator;


    @Scheduled(every = "{ingestor.schedule.interval}", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    @Blocking
    public Uni<Void> executarLeituraArquivos() {
        LOG.info("Iniciando execução do agendador de leitura de arquivos CSV");

        if (rodando.getAndSet(true)) {
            LOG.info("Job já em execução, ignorando essa execução");
            return Uni.createFrom().voidItem();
        }

        return Uni.createFrom().item(() -> {
            try {
                jobOperator.start("csv-import", new Properties());
                return null;
            } catch (Exception e) {
                LOG.error("Erro ao iniciar job csv-import", e);
                throw new RuntimeException("Erro ao iniciar o job", e);
            }
        }).replaceWithVoid()
        .eventually(() -> rodando.set(false));
    }

}
