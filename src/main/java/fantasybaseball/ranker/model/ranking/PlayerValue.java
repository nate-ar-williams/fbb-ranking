package fantasybaseball.ranker.model.ranking;

import fantasybaseball.ranker.model.scoring.PlayerScore;
import lombok.Builder;

@Builder
public class PlayerValue {
    public PlayerScore playerScore;
    public Double valueOverReplacementPlayer;
}
