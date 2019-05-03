package ninckblokje.poc.db.poller.poller;

import lombok.extern.slf4j.Slf4j;
import ninckblokje.poc.db.poller.entity.PollRecord;
import ninckblokje.poc.db.poller.repository.PollerRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Slf4j
public class DelayPoller {

    private PollerRepository repository;

    public DelayPoller(PollerRepository repository) {
        this.repository = repository;
    }

    public List<PollRecord> getNextPollCycle() {
        log.info("Polling next 10 records");
        List<PollRecord> records = repository.getFirst10ByValueIsNotNull();

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        log.info("Done polling next 10 records");
        return records;
    }

    public Future<List<PollRecord>> doPoll() {
        return CompletableFuture.supplyAsync(() -> getNextPollCycle());
    }
}
