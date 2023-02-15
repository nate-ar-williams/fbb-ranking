package fantasybaseball.ranker.controllers;

import fantasybaseball.ranker.model.Player;
import fantasybaseball.ranker.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerRepository repository;

    @GetMapping
    public Player player(@RequestParam(value="id", defaultValue = "a") String id) {
        //return repository.findById(id);
        return Player.builder()
                .firstName("Shohei")
                .lastName("Ohtani")
                .build();
    }

    @PostMapping
    public Player createPlayer(Player input) {
        return repository.save(input);
    }
}
