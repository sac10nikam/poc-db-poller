package ninckblokje.poc.db.poller.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PollRecord {

    @Id
    @GeneratedValue
    private Long id;
    private String value;
}
