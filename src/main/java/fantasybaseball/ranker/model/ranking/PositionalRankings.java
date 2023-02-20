package fantasybaseball.ranker.model.ranking;

import fantasybaseball.ranker.model.player.Position;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Builder
public class PositionalRankings {
    @Id
    public Position position;
    public List<PlayerValue> playerValues;
}
