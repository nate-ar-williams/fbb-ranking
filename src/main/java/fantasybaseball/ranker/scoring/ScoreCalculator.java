package fantasybaseball.ranker.scoring;

import fantasybaseball.ranker.model.player.HitterProjections;
import fantasybaseball.ranker.model.player.PitcherProjections;
import fantasybaseball.ranker.model.player.Player;
import fantasybaseball.ranker.model.scoring.PlayerScore;
import fantasybaseball.ranker.model.settings.BattingScoringSettings;
import fantasybaseball.ranker.model.settings.LeagueSettings;
import fantasybaseball.ranker.model.settings.PitchingScoringSettings;

public class ScoreCalculator {
    public static PlayerScore calulateScore(final LeagueSettings settings, final Player player)
    {
        return PlayerScore.builder()
                .playerId(player.id)
                .name(player.name)
                .leagueId(settings.id)
                .leagueName(settings.leagueName)
                .hitterScore(calculateHitterScore(settings.scoringSettings.batting, player))
                .pitcherScore(calculatePitcherScore(settings.scoringSettings.pitching, player))
                .team(player.team)
                .positions(player.positions)
                .build();
    }

    private static double calculateHitterScore(
            final BattingScoringSettings settings,
            final Player player
    ) {
        if (player.hitterProjections == null) {
            return 0.0;
        }
        HitterProjections projections = player.hitterProjections;
        return
                settings.runsScored         * projections.runs
                + settings.runsBattedIn     * projections.rbis
                + settings.totalBases       * projections.totalBases
                + settings.walks            * projections.walks
                + settings.strikeouts       * projections.strikeouts
                + settings.stolenBases      * projections.stolenBases
                + settings.caughtStealing   * projections.caughtStealing;
    }

    private static double calculatePitcherScore(
            final PitchingScoringSettings settings,
            final Player player
    ) {
        if (player.pitcherProjections == null) {
            return 0.0;
        }
        PitcherProjections projections = player.pitcherProjections;
        return
                settings.outsRecorded       * projections.outsRecorded
                + settings.strikeouts       * projections.strikeouts
                + settings.walks            * projections.walks
                + settings.earnedRuns       * projections.earnedRunsAllowed
                + settings.qualityStarts    * projections.qualityStarts
                + settings.completeGames    * projections.completeGames
                + settings.saves            * projections.saves
                + settings.holds            * projections.holds;
    }
}
