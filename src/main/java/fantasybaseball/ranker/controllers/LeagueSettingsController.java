package fantasybaseball.ranker.controllers;

import fantasybaseball.ranker.model.settings.LeagueSettings;
import fantasybaseball.ranker.repository.LeagueSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/settings")
public class LeagueSettingsController {

    @Autowired
    private LeagueSettingsRepository repository;

    @GetMapping
    public List<LeagueSettings> getLeagueSettings(@RequestParam(value="id", defaultValue = "") String id) {
        if (id.isEmpty()) {
            return repository.findAll();
        }
        final Optional<LeagueSettings> foundSettings = repository.findById(id);

        return foundSettings.map(List::of).orElseGet(List::of);
    }

    @PostMapping
    public LeagueSettings createLeagueSettings(@RequestBody LeagueSettings settings) {
        return repository.save(settings);
    }

    @DeleteMapping
    public String deletePlayers(@RequestParam(value="id", defaultValue = "") String id) {
        if (id.isEmpty()) {
            repository.deleteAll();
        }
        else {
            repository.deleteById(id);
        }
        return "Cleared db";
    }
}
