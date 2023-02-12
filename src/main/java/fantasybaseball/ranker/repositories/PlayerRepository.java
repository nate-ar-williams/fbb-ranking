package fantasybaseball.ranker.repositories;

import fantasybaseball.ranker.entities.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "player", path = "player")
public interface PlayerRepository extends MongoRepository<Player, String> {


}
