package fantasybaseball.ranker.controllers;

import fantasybaseball.ranker.model.player.Position;
import fantasybaseball.ranker.model.ranking.PlayerValue;
import fantasybaseball.ranker.model.ranking.PositionalRankings;
import fantasybaseball.ranker.model.scoring.PlayerScore;
import fantasybaseball.ranker.model.settings.LeagueSettings;
import fantasybaseball.ranker.repository.LeagueSettingsRepository;
import fantasybaseball.ranker.repository.PlayerRepository;
import fantasybaseball.ranker.repository.PlayerScoreRepository;
import fantasybaseball.ranker.repository.PositionalRankingsRepository;
import fantasybaseball.ranker.scoring.ScoreCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rankings")
public class RankingsController {

    @Autowired
    private LeagueSettingsRepository settingsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerScoreRepository playerScoreRepository;

    @Autowired
    private PositionalRankingsRepository positionalRankingsRepository;

    // I need to be able to assign a score to each player, given some league settings
    @GetMapping("/generate")
    public List<PlayerScore> generateRankings(
            @RequestParam(value="leagueId", defaultValue = "") String leagueId
    ) {
        validateLeagueId(leagueId);
        LeagueSettings settings = validateSettingsExist(leagueId);

        // 1) giving scores for each player for a league.
        playerRepository.findAllBy().forEach(
                p -> {
                    PlayerScore playerScore = ScoreCalculator.calulateScore(settings, p);
                    playerScoreRepository.save(playerScore);
                    if (playerScore.positions != null) {
                        playerScore.positions.forEach(position -> {
                            savePlayerScore(position, playerScore, settings);
                        });
                    }
                }
        );

        // 2) for each position, list the top 12 players


        return List.of();
    }

    private void validateLeagueId(String leagueId) {
        if (leagueId.isEmpty()) {
            throw new IllegalArgumentException("Must supply leagueId");
        }
    }

    private LeagueSettings validateSettingsExist(String leagueId) {
        Optional<LeagueSettings> settingsOptional = settingsRepository.findById(leagueId);
        if (settingsOptional.isEmpty()) {
            throw new IllegalArgumentException("leagueId " + leagueId + " not found");
        }

        return settingsOptional.get();
    }

    private void savePlayerScore(
            final Position position,
            final PlayerScore playerScore,
            final LeagueSettings settings)
    {
        Optional<PositionalRankings> existingPlayerScoresOfPosition =
                positionalRankingsRepository.findByPosition(position);
        if (existingPlayerScoresOfPosition.isEmpty()) {
            positionalRankingsRepository.save(
                    PositionalRankings.builder()
                            .position(position)
                            .playerValues(
                                    List.of(PlayerValue.builder()
                                            .playerScore(playerScore)
                                            .valueOverReplacementPlayer(0.0)
                                            .build()
                                    )
                            ).build()
            );
        }
        else {
            // save if it's top 12
            saveTopNPlayerScore(position, playerScore, existingPlayerScoresOfPosition.get(), settings);
        }
    }
    private void saveTopNPlayerScore(
            final Position position,
            final PlayerScore playerScore,
            final PositionalRankings positionalRankings,
            final LeagueSettings settings) {
        // save if it's top 12
        if (positionalRankings.playerValues.size() < calculateReplacement(position, settings)) {
            positionalRankings.playerValues.add(PlayerValue.builder()
                    .playerScore(playerScore)
                    .valueOverReplacementPlayer(0.0)
                    .build());
            updateValueOverReplacementPlayer(positionalRankings);
            positionalRankingsRepository.save(positionalRankings);
        } else {
            replacePlayerScore(playerScore, positionalRankings);
        }
    }

    private int calculateReplacement(Position position, LeagueSettings settings) {
        return switch (position) {
            case C -> settings.rosterSettings.catchers;
            case FIRST -> settings.rosterSettings.first;
            case SECOND -> settings.rosterSettings.second;
            case THIRD -> settings.rosterSettings.third;
            case SS -> settings.rosterSettings.shortstop;
            case LF -> settings.rosterSettings.left;
            case RF -> settings.rosterSettings.right;
            case CF -> settings.rosterSettings.center;
            case OF -> settings.rosterSettings.outfield;
            case DH -> settings.rosterSettings.designatedHitter;
            case UTIL -> settings.rosterSettings.util;
            case RP -> settings.rosterSettings.reliefPitcher;
            case SP -> settings.rosterSettings.startingPitcher;
            case P -> settings.rosterSettings.pitcher;
        } * settings.numberOfTeams + 1;
    }

    private void updateValueOverReplacementPlayer(PositionalRankings rankings) {
        double replacementPlayerValue = Double.MAX_VALUE;
        for (final PlayerValue value : rankings.playerValues) {
            if (value.playerScore.pitcherScore + value.playerScore.hitterScore <= replacementPlayerValue) {
                replacementPlayerValue = value.playerScore.pitcherScore + value.playerScore.hitterScore;
            }
        }
        for (int i = 0; i < rankings.playerValues.size(); i++) {
            PlayerValue current = rankings.playerValues.get(i);
            current.valueOverReplacementPlayer =
                    (current.playerScore.pitcherScore + current.playerScore.hitterScore) - replacementPlayerValue;
            rankings.playerValues.set(i, current);
        }
    }

    private void replacePlayerScore(final PlayerScore playerScore, final PositionalRankings positionalRankings) {
        for (int i = 0; i < positionalRankings.playerValues.size(); i++) {
            if (playerScore.hitterScore + playerScore.pitcherScore
                    > positionalRankings.playerValues.get(i).playerScore.hitterScore
                    + positionalRankings.playerValues.get(i).playerScore.pitcherScore
            ) {
                positionalRankings.playerValues.remove(i);
                positionalRankings.playerValues.add(
                        PlayerValue.builder()
                                .playerScore(playerScore)
                                .valueOverReplacementPlayer(0.0)
                                .build());
                updateValueOverReplacementPlayer(positionalRankings);
                positionalRankingsRepository.save(positionalRankings);
                break;
            }
        }

    }

    @GetMapping("/scores")
    public List<PlayerScore> getPlayerScores(@RequestParam(value="leagueId", defaultValue = "") String leagueId) {
        validateLeagueId(leagueId);
        return playerScoreRepository.findAll();
    }

    @GetMapping
    public List<PositionalRankings> getPlayerRankings(@RequestParam(value="leagueId", defaultValue = "") String leagueId) {
        validateLeagueId(leagueId);
        LeagueSettings settings = validateSettingsExist(leagueId);

        return positionalRankingsRepository.findAll();
    }

    @DeleteMapping
    public String deleteRankings(@RequestParam(value="leagueId", defaultValue = "") String leagueId) {
        validateLeagueId(leagueId);
        // TODO delete only a specific leagueId
        playerScoreRepository.deleteAll();
        positionalRankingsRepository.deleteAll();
        return "deleted player scores";
    }
}
