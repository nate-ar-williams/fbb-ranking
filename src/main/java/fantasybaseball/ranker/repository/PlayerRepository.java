package fantasybaseball.ranker.repository;

import fantasybaseball.ranker.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String> {
}
