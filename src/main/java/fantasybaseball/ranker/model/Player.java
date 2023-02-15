package fantasybaseball.ranker.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Builder
public class Player {

    @Id
    public String id;

    public String firstName;

    public String lastName;

    public List<Position> positions;

    public HitterProjections hitterProjections;

    public PitcherProjections pitcherProjections;

    @Override
    public String toString() {
        return id + ": " + firstName + ' ' + lastName;
    }
}
