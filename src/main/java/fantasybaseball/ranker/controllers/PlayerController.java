package fantasybaseball.ranker.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fantasybaseball.ranker.model.Player;
import fantasybaseball.ranker.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerRepository repository;

    @GetMapping
    public List<Player> player(@RequestParam(value="id", defaultValue = "") String id) {
        if (id.isEmpty()) {
            return repository.findAll();
        }
        final Optional<Player> foundPlayer = repository.findById(id);

        return foundPlayer.map(List::of).orElseGet(List::of);
    }

    @PostMapping
    public Player createPlayer(@RequestBody final Player input) {
        Logger.getLogger("post").log(Level.SEVERE, "firstName=" + input.firstName);
        Logger.getLogger("post").log(Level.SEVERE, "lastName=" + input.lastName);
        Logger.getLogger("post").log(Level.SEVERE, "positions=" + input.positions);
        Logger.getLogger("post").log(Level.SEVERE, "full=" + input);
        return repository.save(input);
    }

    @DeleteMapping
    public String deletePlayers() {
        repository.deleteAll();
        return "Cleared db";
    }
}
