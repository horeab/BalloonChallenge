package libgdx.game.lib.logic;

import android.content.Context;
import android.content.SharedPreferences;

public class GameSettingsStateManager {

	public static String SOUND_ON = "SOUND_ON";
	public static String SHARED_PREFS_NAME = "settings";

	private SharedPreferences prefs;

	public GameSettingsStateManager(Context context) {
		this.prefs = context.getSharedPreferences(SHARED_PREFS_NAME, 0);
	}

	public boolean isSoundOn() {
		boolean prefSoundOn = prefs.getBoolean(SOUND_ON, true);
		return prefSoundOn;
	}

	public void updateSoundOn(boolean soundOn) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(SOUND_ON, soundOn);
		editor.commit();
	}
}
