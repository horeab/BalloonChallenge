package libgdx.game.lib.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LevelStateManager {

	public static final String STAGE_LEVEL_DELIMITER = "#";

	private static final String FINISHED_STAGES = "FINISHED_STAGES";

	private static final String FINISHED_GAME = "FINISHED_GAME";

	private static final String FINISHED_LEVELS_FOR_STAGE = "FINISHED_LEVELS_FOR_STAGE";

	private static String SHARED_PREFS_NAME = "statemanager";

	private SharedPreferences prefs;

	public LevelStateManager(Context context) {
		this.prefs = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
	}

	public void putGameFinished() {
		Editor editor = prefs.edit();
		editor.putBoolean(FINISHED_GAME, true);
		editor.commit();
	}

	public boolean isGameFinished() {
		return prefs.getBoolean(FINISHED_GAME, false);
	}

	public void unlockStage(int stageNr) {
		Editor editor = prefs.edit();
		editor.putBoolean(FINISHED_STAGES + stageNr, true);
		editor.commit();
	}

	public boolean isStageUnlocked(int stageNr) {
		if (stageNr == 0) {
			return true;
		}
		return prefs.getBoolean(FINISHED_STAGES + stageNr, false);
	}

	public void unlockLevelForStage(int stageNr, int levelNr) {
		Editor editor = prefs.edit();
		editor.putBoolean(FINISHED_LEVELS_FOR_STAGE + stageNr + STAGE_LEVEL_DELIMITER + levelNr, true);
		editor.commit();
	}

	public boolean isLevelForStageUnlocked(int stageNr, int levelNr) {
		if (levelNr == 0) {
			return true;
		}
		return prefs.getBoolean(FINISHED_LEVELS_FOR_STAGE + stageNr + STAGE_LEVEL_DELIMITER + levelNr, false);
	}

	public void clear() {
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}

}
