package fantasybaseball.ranker.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
public class LeagueSettings {

    @Id
    public String id;

    public String leagueName;

    public int numberOfTeams;

    public RosterSettings rosterSettings;

    public ScoringSettings scoringSettings;
}
