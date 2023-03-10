package fantasybaseball.ranker.model.player;

import lombok.Builder;

@Builder
public class HitterProjections {

    public int runs;

    public int totalBases;

    public int rbis;

    public int walks;

    public int strikeouts;

    public int stolenBases;

    public int caughtStealing;

    public int plateAppearances;
}
