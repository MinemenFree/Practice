package rip.crystal.practice.player.queue.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.crystal.practice.Locale;
import rip.crystal.practice.cPractice;
import rip.crystal.practice.game.arena.Arena;
import rip.crystal.practice.match.Match;
import rip.crystal.practice.match.impl.BasicTeamMatch;
import rip.crystal.practice.match.impl.BasicTeamRoundMatch;
import rip.crystal.practice.match.participant.MatchGamePlayer;
import rip.crystal.practice.player.profile.Profile;
import rip.crystal.practice.player.profile.participant.alone.GameParticipant;
import rip.crystal.practice.player.queue.Queue;
import rip.crystal.practice.player.queue.QueueProfile;
import rip.crystal.practice.utilities.MessageFormat;
import rip.crystal.practice.utilities.PlayerUtil;
import rip.crystal.practice.utilities.TaskUtil;

public class QueueTask implements Runnable {

    @Override
    public void run() {
        for (Queue queue : Queue.getQueues()) {
            queue.getPlayers().forEach(QueueProfile::tickRange);

            if (queue.getPlayers().size() < 2) continue;

            for (QueueProfile firstQueueProfile : queue.getPlayers()) {
                Player firstPlayer = Bukkit.getPlayer(firstQueueProfile.getPlayerUuid());

                if (firstPlayer == null) continue;

                Profile firstProfile = Profile.get(firstPlayer.getUniqueId());

                // Find arena
                Arena arena = Arena.getRandomArena(queue.getKit());

                if (arena == null) {
                    queue.getPlayers().remove(firstQueueProfile);
                    new MessageFormat(Locale.QUEUE_NO_ARENAS_AVAILABLE
                            .format(Profile.get(firstPlayer.getUniqueId()).getLocale()))
                            .add("{kit}", queue.getKit().getName())
                            .send(firstPlayer);
                    break;
                }

                for (QueueProfile secondQueueProfile : queue.getPlayers()) {
                    if (firstQueueProfile.equals(secondQueueProfile)) continue;

                    Player secondPlayer = Bukkit.getPlayer(secondQueueProfile.getPlayerUuid());

                    if (secondPlayer == null) continue;

                    Profile secondProfile = Profile.get(secondPlayer.getUniqueId());


                    if(/*firstProfile.getOptions().isUsingPingFactor() &&*/firstProfile.getPingRange() != -1) {
                        if(PlayerUtil.getPing(secondPlayer) > firstProfile.getPingRange()) {
                            return;
                        }
                    }
                    if(/*secondProfile.getOptions().isUsingPingFactor() &&*/secondProfile.getPingRange() != -1) {
                        if(PlayerUtil.getPing(firstPlayer) > secondProfile.getPingRange()) {
                            return;
                        }
                    }

                    if (queue.isRanked()) {
                        if (!firstQueueProfile.isInRange(secondQueueProfile.getElo()) ||
                                !secondQueueProfile.isInRange(firstQueueProfile.getElo())) {
                            continue;
                        }
                    }

                    // Update arena
                    arena.setActive(true);

                    // Remove players from queue
                    queue.getPlayers().remove(firstQueueProfile);
                    queue.getPlayers().remove(secondQueueProfile);

                    MatchGamePlayer playerA = new MatchGamePlayer(firstPlayer.getUniqueId(),
                            Profile.get(firstPlayer.getUniqueId()).getColor() + firstPlayer.getName(), firstQueueProfile.getElo());

                    MatchGamePlayer playerB = new MatchGamePlayer(secondPlayer.getUniqueId(),
                            Profile.get(secondPlayer.getUniqueId()).getColor() + secondPlayer.getName(), secondQueueProfile.getElo());

                    GameParticipant<MatchGamePlayer> participantA = new GameParticipant<>(playerA);
                    GameParticipant<MatchGamePlayer> participantB = new GameParticipant<>(playerB);

                    // Create match
                    Match match;

                    if (queue.getKit().getGameRules().isBridge()) {
                        match = new BasicTeamRoundMatch(queue, queue.getKit(), arena, queue.isRanked(),
                                participantA, participantB, cPractice.get().getBridgeRounds());
                    }
                    else if (queue.isRanked() && queue.getKit().getGameRules().isSumo()) {
                        match = new BasicTeamRoundMatch(queue, queue.getKit(), arena, queue.isRanked(),
                                participantA, participantB, cPractice.get().getRankedSumoRounds());
                    }
                    else {
                        match = new BasicTeamMatch(queue, queue.getKit(), arena, queue.isRanked(),
                                participantA, participantB);
                    }

                    if (queue.isRanked()) {
                        new MessageFormat(Locale.QUEUE_FOUND_RANKED_MATCH
                                .format(Profile.get(firstPlayer.getUniqueId()).getLocale()))
                                .add("{name}", firstPlayer.getName())
                                .add("{elo}", String.valueOf(firstQueueProfile.getElo()))
                                .add("{opponent}", secondPlayer.getName())
                                .add("{opponent-elo}", String.valueOf(secondQueueProfile.getElo()))
                                .send(firstPlayer);
                        new MessageFormat(Locale.QUEUE_FOUND_RANKED_MATCH
                                .format(Profile.get(secondPlayer.getUniqueId()).getLocale()))
                                .add("{name}", secondPlayer.getName())
                                .add("{elo}", String.valueOf(secondQueueProfile.getElo()))
                                .add("{opponent}", firstPlayer.getName())
                                .add("{opponent-elo}", String.valueOf(firstQueueProfile.getElo()))
                                .send(secondPlayer);
                    } else {
                        new MessageFormat(Locale.QUEUE_FOUND_UNRANKED_MATCH
                                .format(Profile.get(firstPlayer.getUniqueId()).getLocale()))
                                .add("{name}", firstPlayer.getName())
                                .add("{opponent}", secondPlayer.getName())
                                .send(firstPlayer);
                        new MessageFormat(Locale.QUEUE_FOUND_UNRANKED_MATCH
                                .format(Profile.get(secondPlayer.getUniqueId()).getLocale()))
                                .add("{name}", secondPlayer.getName())
                                .add("{opponent}", firstPlayer.getName())
                                .send(secondPlayer);
                    }

                    TaskUtil.run(match::start);
                }
            }
        }
    }
}
