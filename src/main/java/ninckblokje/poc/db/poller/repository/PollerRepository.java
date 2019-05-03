package ninckblokje.poc.db.poller.repository;

import ninckblokje.poc.db.poller.entity.PollRecord;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@org.springframework.stereotype.Repository
@Transactional
public interface PollerRepository extends Repository<PollRecord, Long> {

    Long countByValueIsNotNull();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name="javax.persistence.lock.timeout", value="-2")})
    List<PollRecord> getFirst10ByValueIsNotNull();

    PollRecord save(PollRecord pollRecord);
}
