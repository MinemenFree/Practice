package rip.crystal.practice.utilities.elo;

import rip.crystal.practice.game.kit.Kit;
import rip.crystal.practice.player.profile.Profile;

import java.util.stream.Stream;

public class EloUtil {

	private static final KFactor[] K_FACTORS = {
			new KFactor(0, 1000, 25),
			new KFactor(1001, 1400, 20),
			new KFactor(1401, 1800, 15),
			new KFactor(1801, 2200, 10)
	};

	private static final int DEFAULT_K_FACTOR = 25;
	private static final int WIN = 1;
	private static final int LOSS = 0;

	public static int getNewRating(int rating, int opponentRating, boolean won) {
		if (won) {
			return EloUtil.getNewRating(rating, opponentRating, EloUtil.WIN);
		} else {
			return EloUtil.getNewRating(rating, opponentRating, EloUtil.LOSS);
		}
	}

	public static int getNewRating(int rating, int opponentRating, int score) {
		double kFactor = EloUtil.getKFactor(rating);
		double expectedScore = EloUtil.getExpectedScore(rating, opponentRating);
		int newRating = EloUtil.calculateNewRating(rating, score, expectedScore, kFactor);

		if (score == 1) {
			if (newRating == rating) {
				newRating++;
			}
		}
		return newRating;
	}

	private static int calculateNewRating(int oldRating, int score, double expectedScore, double kFactor) {
		return oldRating + (int) (kFactor * (score - expectedScore));
	}

	private static double getKFactor(int rating) {
		for (int i = 0; i < EloUtil.K_FACTORS.length; i++) {
			if (rating >= EloUtil.K_FACTORS[i].getStartIndex() && rating <= EloUtil.K_FACTORS[i].getEndIndex()) {
				return EloUtil.K_FACTORS[i].getValue();
			}
		}

		return EloUtil.DEFAULT_K_FACTOR;
	}

	private static double getExpectedScore(int rating, int opponentRating) {
		return 1 / (1 + Math.pow(10, ((double) (opponentRating - rating) / 400)));
	}

	public static int getGlobalElo(Profile profile) {
		int[] wrapper = new int[2];
		Stream<Kit> kit = Kit.getKits().stream().filter(kits -> kits.getGameRules().isRanked());
		kit.forEach(kits -> {
			wrapper[0] = wrapper[0] + 1;
			wrapper[1] = wrapper[1] + profile.getKitData().get(kits).getElo();
		});
		try {
			return (wrapper[1] / wrapper[0]);
		} catch (Exception e) {
			return 0;
		}
	}

	public static int getNoDebuffElo(Profile profile) {
		int[] wrapper = new int[2];
		Kit kit = Kit.getByName("NoDebuff");
		if(kit.getGameRules().isRanked()) {
			profile.getKitData().get(kit).getElo();
		}
		try {
			return (wrapper[1] / wrapper[0]);
		} catch (Exception e) {
			return 0;
		}
	}

}
