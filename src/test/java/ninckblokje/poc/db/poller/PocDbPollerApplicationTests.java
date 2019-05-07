package ninckblokje.poc.db.poller;

import ninckblokje.poc.db.poller.entity.PollRecord;
import ninckblokje.poc.db.poller.poller.DelayPoller;
import ninckblokje.poc.db.poller.repository.PollerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PocDbPollerApplicationTests {

    @Autowired
    private DelayPoller poller;
    @Autowired
    private PollerRepository repository;

    @Before
    @Rollback(false)
    public void before() {
        for(int i=0;i<20;i++) {
            repository.save(PollRecord.builder().value(UUID.randomUUID().toString()).build());
        }
    }

    @Test
    public void testPollLocking() throws ExecutionException, InterruptedException {
        assertThat(repository.countByValueIsNotNull().longValue(), is(20L));

        Future<List<PollRecord>> f1 = doPoll();
        Future<List<PollRecord>> f2 = doPoll();

        List<Long> foundIds = new ArrayList<>();

        List<PollRecord> p1 = f1.get();
        foundIds.addAll(p1.stream()
                .map(PollRecord::getId)
                .collect(Collectors.toList()));

        List<PollRecord> p2 = f2.get();
        foundIds.addAll(p2.stream()
                .map(PollRecord::getId)
                .collect(Collectors.toList()));

        assertThat(foundIds.size(), is(20));

        List<Long> duplicateIds = foundIds.stream()
                .filter(foundId -> Collections.frequency(foundIds, foundId) > 1)
                .collect(Collectors.toList());
        assertThat(duplicateIds.isEmpty(), is(true));
    }

    Future<List<PollRecord>> doPoll() {
        return CompletableFuture.supplyAsync(() -> poller.getNextPollCycle());
    }
}
