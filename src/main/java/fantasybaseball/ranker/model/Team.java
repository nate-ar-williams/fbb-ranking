package fantasybaseball.ranker.model;

public enum Team {
    ARI,
    ATL,
    BAL,
    BOS,
    CHC,
    CHW,
    CIN,
    CLE,
    COL,
    DET,
    HOU,
    KCR,
    LAA,
    LAD,
    MIA,
    MIL,
    MIN,
    NYM,
    NYY,
    OAK,
    PHI,
    PIT,
    SDP,
    SEA,
    SFG,
    STL,
    TBR,
    TEX,
    TOR,
    WSN,
    FA;

    public static Team getValue(final String input) {
        if (input == null || input.isEmpty()) {
            return FA;
        }
        return valueOf(input);
    }
}
