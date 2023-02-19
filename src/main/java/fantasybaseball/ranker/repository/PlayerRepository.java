package fantasybaseball.ranker.repository;

import fantasybaseball.ranker.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlayerRepository extends MongoRepository<Player, String> {
    List<Player> findByName(String name);
}
