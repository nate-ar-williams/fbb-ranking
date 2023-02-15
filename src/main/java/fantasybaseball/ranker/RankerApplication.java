package fantasybaseball.ranker;

import fantasybaseball.ranker.repository.PlayerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = PlayerRepository.class)
public class RankerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RankerApplication.class, args);
	}

}
