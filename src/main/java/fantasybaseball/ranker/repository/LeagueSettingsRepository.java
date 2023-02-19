package fantasybaseball.ranker.repository;

import fantasybaseball.ranker.model.LeagueSettings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeagueSettingsRepository extends MongoRepository<LeagueSettings, String> {
}
