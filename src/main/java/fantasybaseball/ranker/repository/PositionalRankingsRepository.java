package fantasybaseball.ranker.repository;

import fantasybaseball.ranker.model.player.Position;
import fantasybaseball.ranker.model.ranking.PositionalRankings;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PositionalRankingsRepository extends MongoRepository<PositionalRankings, String> {
    Optional<PositionalRankings> findByPosition(Position position);
}
