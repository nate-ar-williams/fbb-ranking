package fantasybaseball.ranker.repository;

import fantasybaseball.ranker.model.scoring.PlayerScore;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface PlayerScoreRepository extends MongoRepository<PlayerScore, String> {
    Stream<PlayerScore> findAllBy();
}
