package fantasybaseball.ranker.model.scoring;

import fantasybaseball.ranker.model.player.Position;
import fantasybaseball.ranker.model.player.Team;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
@Builder
public class PlayerScore {
    public String playerId;
    public String name;
    public Team team;
    public String leagueId;
    public String leagueName;
    public Double hitterScore;
    public Double pitcherScore;
    public List<Position> positions;
}
