package fantasybaseball.ranker.repository;

import fantasybaseball.ranker.model.player.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.stream.Stream;

public interface PlayerRepository extends MongoRepository<Player, String> {
    List<Player> findByName(String name);

    Stream<Player> findAllBy();
}
