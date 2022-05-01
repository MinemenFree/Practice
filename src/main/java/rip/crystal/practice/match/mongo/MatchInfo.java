package rip.crystal.practice.match.mongo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rip.crystal.practice.game.kit.Kit;

@RequiredArgsConstructor
@Setter @Getter
public class MatchInfo {

    private final String winningParticipant;
    private final String losingParticipant;
    private final Kit kit;
    private final int newWinnerElo;
    private final int newLoserElo;
    private final String date;
    private final String duration;

//    @Override
//    public Document serialize() {
//        Document document = new Document();
//        document.put("winningParticipant", winningParticipant);
//        document.put("losingParticipant", losingParticipant);
//        document.put("kit", kit.getName());
//        document.put("newWinnerElo", newWinnerElo);
//        document.put("newLoserElo", newLoserElo);
//        document.put("date", date);
//        document.put("duration", duration);
//        return document;
//    }
}