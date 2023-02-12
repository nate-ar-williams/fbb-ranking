package fantasybaseball.ranker.entities;

import org.springframework.data.annotation.Id;

import java.util.List;

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
