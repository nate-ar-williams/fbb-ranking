package fantasybaseball.ranker.controllers;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import fantasybaseball.ranker.csv.metadata.HitterCsvMetadata;
import fantasybaseball.ranker.csv.metadata.PitcherCsvMetadata;
import fantasybaseball.ranker.csv.metadata.PositionsCsvMetadata;
import fantasybaseball.ranker.model.player.*;
import fantasybaseball.ranker.repository.PlayerRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static fantasybaseball.ranker.csv.metadata.HitterCsvMetadata.*;
import static fantasybaseball.ranker.csv.metadata.PitcherCsvMetadata.*;

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
        return repository.save(input);
    }

    @GetMapping("/hydrate")
    public String hydrateDb() throws IOException {
        int playersSaved = 0;
        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader("/resources/fangraphs-leaderboard-projections-hitters.csv"))
                .withSkipLines(1)
                .build()) {
            String[] nextRecord = null;
            while ((nextRecord = reader.readNext()) != null) {
                Optional<Player> found = repository.findById(nextRecord[PitcherCsvMetadata.ID_INDEX]);
                PitcherProjections pitcherProjections = found.orElse(Player.builder().build()).pitcherProjections;
                Player player =
                        Player.builder()
                                .id(nextRecord[HitterCsvMetadata.ID_INDEX])
                                .name(StringUtils.stripAccents(nextRecord[HitterCsvMetadata.NAME_INDEX]).toUpperCase())
                                .team(Team.getValue(nextRecord[HitterCsvMetadata.TEAM_INDEX]))
                                .hitterProjections(
                                        HitterProjections.builder()
                                                .plateAppearances(Integer.parseInt(nextRecord[PLATE_APPEARANCES_INDEX]))
                                                .totalBases(
                                                        Integer.parseInt(nextRecord[SINGLES_INDEX])
                                                        + 2 * Integer.parseInt(nextRecord[DOUBLES_INDEX])
                                                        + 3 * Integer.parseInt(nextRecord[TRIPLES_INDEX])
                                                        + 4 * Integer.parseInt(nextRecord[HOME_RUNS_INDEX])
                                                )
                                                .runs(Integer.parseInt(nextRecord[RUNS_INDEX]))
                                                .rbis(Integer.parseInt(nextRecord[RBIS_INDEX]))
                                                .walks(Integer.parseInt(nextRecord[HitterCsvMetadata.WALKS_INDEX]))
                                                .strikeouts(Integer.parseInt(nextRecord[HitterCsvMetadata.STRIKEOUTS_INDEX]))
                                                .stolenBases(Integer.parseInt(nextRecord[STOLEN_BASES_INDEX]))
                                                .caughtStealing(Integer.parseInt(nextRecord[CAUGHT_STEALING_INDEX]))
                                                .build()
                                )
                                .pitcherProjections(pitcherProjections)
                                .build();
                repository.save(player);
                playersSaved++;
            }
        }

        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader("/resources/fangraphs-leaderboard-projections-pitchers.csv"))
                .withSkipLines(1)
                .build()) {
            String[] nextRecord = null;
            while ((nextRecord = reader.readNext()) != null) {
                Optional<Player> found = repository.findById(nextRecord[PitcherCsvMetadata.ID_INDEX]);
                HitterProjections hitterProjections = found.orElse(Player.builder().build()).hitterProjections;
                Player player = Player.builder()
                        .id(nextRecord[PitcherCsvMetadata.ID_INDEX])
                        .name(StringUtils.stripAccents(nextRecord[PitcherCsvMetadata.NAME_INDEX]).toUpperCase())
                        .team(Team.getValue(nextRecord[PitcherCsvMetadata.TEAM_INDEX]))
                        .positions(List.of(
                                Position.P,
                                Integer.parseInt(nextRecord[QUALITY_START_INDEX]) > 0 ? Position.SP : Position.RP))
                        .pitcherProjections(
                                PitcherProjections.builder()
                                        .outsRecorded(Integer.parseInt(nextRecord[OUTS_RECORDED_INDEX]) * 3)
                                        .strikeouts(Integer.parseInt(nextRecord[PitcherCsvMetadata.STRIKEOUTS_INDEX]))
                                        .walks(Integer.parseInt(nextRecord[PitcherCsvMetadata.WALKS_INDEX]))
                                        .earnedRunsAllowed(Integer.parseInt(nextRecord[EARNED_RUNS_INDEX]))
                                        .qualityStarts(Integer.parseInt(nextRecord[QUALITY_START_INDEX]))
                                        .completeGames(0)
                                        .saves(Integer.parseInt(nextRecord[SAVES_INDEX]))
                                        .holds(Integer.parseInt(nextRecord[HOLDS_INDEX]))
                                        .build())
                        .hitterProjections(hitterProjections)
                        .build();
                repository.save(player);
                playersSaved++;
            }
        }
        try (CSVReader reader = new CSVReaderBuilder(
                new FileReader("/resources/positions-hitters.csv"))
                .withSkipLines(1)
                .build()) {
            String[] nextRecord = null;
            // add positions
            while ((nextRecord = reader.readNext()) != null) {
                List<Player> found = repository.findByName(
                        StringUtils.stripAccents(nextRecord[PositionsCsvMetadata.NAME_INDEX]).toUpperCase());
                if (!found.isEmpty()) {
                    Player player = found.get(0);
                    List<Position> positions = player.positions == null ? new ArrayList<>() : player.positions;
                    if (Integer.parseInt(nextRecord[PositionsCsvMetadata.C_INDEX]) >=20) {
                        positions.add(Position.C);
                    }
                    if (Integer.parseInt(nextRecord[PositionsCsvMetadata.FIRST_BASE_INDEX]) >=20) {
                        positions.add(Position.FIRST);
                    }
                    if (Integer.parseInt(nextRecord[PositionsCsvMetadata.SECOND_BASE_INDEX]) >=20) {
                        positions.add(Position.SECOND);
                    }
                    if (Integer.parseInt(nextRecord[PositionsCsvMetadata.THIRD_BASE_INDEX]) >=20) {
                        positions.add(Position.THIRD);
                    }
                    if (Integer.parseInt(nextRecord[PositionsCsvMetadata.SHORTSTOP_INDEX]) >=20) {
                        positions.add(Position.SS);
                    }
                    if (Integer.parseInt(nextRecord[PositionsCsvMetadata.LEFT_FIELD_INDEX]) >=20) {
                        positions.add(Position.LF);
                    }
                    if (Integer.parseInt(nextRecord[PositionsCsvMetadata.RIGHT_FIELD_INDEX]) >=20) {
                        positions.add(Position.RF);
                    }
                    if (Integer.parseInt(nextRecord[PositionsCsvMetadata.CENTER_FIELD_INDEX]) >=20) {
                        positions.add(Position.CF);
                    }
                    if (Integer.parseInt(nextRecord[PositionsCsvMetadata.OUTFIELD_INDEX]) >=20) {
                        positions.add(Position.OF);
                    }
                    positions.add(Position.UTIL);
                    player.positions = positions.stream().distinct().collect(Collectors.toList());

                    repository.save(player);
                }
            }
        }
        return "saved " + playersSaved + " players" ;
    }

    @DeleteMapping
    public String deletePlayers() {
        repository.deleteAll();
        return "Cleared db";
    }
}
