package libgdx.game.lib.logic;

import java.util.HashMap;

import util.MatrixValue;

import main.balloonchallenge.R;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

	private Context context;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> sounds = new HashMap<Integer, Integer>();

	public SoundManager(Context context) {
		this.context = context;
		preloadSounds();
	}

	public void playSound(int row, int cellValue) {
		GameSettingsStateManager settingsStateManager = new GameSettingsStateManager(context);
		if (settingsStateManager.isSoundOn()) {
			int soundForPosition = getSoundForPosition(row, cellValue);
			soundPool.play(soundForPosition, 1f, 1f, 1, 0, 1);
		}
	}

	private void preloadSounds() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

		sounds.put(R.raw.l0, soundPool.load(context, R.raw.l0, 1));
		sounds.put(R.raw.l1, soundPool.load(context, R.raw.l1, 1));
		sounds.put(R.raw.l2, soundPool.load(context, R.raw.l2, 1));
		sounds.put(R.raw.l3, soundPool.load(context, R.raw.l3, 1));
		sounds.put(R.raw.l4, soundPool.load(context, R.raw.l4, 1));
		sounds.put(R.raw.l5, soundPool.load(context, R.raw.l5, 1));
		sounds.put(R.raw.l6, soundPool.load(context, R.raw.l6, 1));
		sounds.put(R.raw.l7, soundPool.load(context, R.raw.l7, 1));
		sounds.put(R.raw.l8, soundPool.load(context, R.raw.l8, 1));
		sounds.put(R.raw.l9, soundPool.load(context, R.raw.l9, 1));
		sounds.put(R.raw.l10, soundPool.load(context, R.raw.l10, 1));
		sounds.put(R.raw.l11, soundPool.load(context, R.raw.l11, 1));
		sounds.put(R.raw.l12, soundPool.load(context, R.raw.l12, 1));
		sounds.put(R.raw.l13, soundPool.load(context, R.raw.l13, 1));
		sounds.put(R.raw.l14, soundPool.load(context, R.raw.l14, 1));
		sounds.put(R.raw.l15, soundPool.load(context, R.raw.l15, 1));
		sounds.put(R.raw.l16, soundPool.load(context, R.raw.l16, 1));

		sounds.put(R.raw.explosion, soundPool.load(context, R.raw.explosion, 1));
		sounds.put(R.raw.coin, soundPool.load(context, R.raw.coin, 1));
		sounds.put(R.raw.tornado, soundPool.load(context, R.raw.tornado, 1));

		soundPool.play(sounds.get(R.raw.l0), 0, 0, 0, -1, 1f);
	}

	private int getSoundForPosition(int row, int cellValue) {
		if (cellValue == MatrixValue.POINTS.getValue()) {
			return sounds.get(R.raw.coin);
		} else if (cellValue == MatrixValue.TORNADO.getValue()) {
			return sounds.get(R.raw.tornado);
		} else if (MatrixValue.isDestroyed(cellValue)) {
			return sounds.get(R.raw.explosion);
		}
		switch (row) {
		case 0:
			return sounds.get(R.raw.l0);
		case 1:
			return sounds.get(R.raw.l1);
		case 2:
			return sounds.get(R.raw.l2);
		case 3:
			return sounds.get(R.raw.l3);
		case 4:
			return sounds.get(R.raw.l4);
		case 5:
			return sounds.get(R.raw.l5);
		case 6:
			return sounds.get(R.raw.l6);
		case 7:
			return sounds.get(R.raw.l7);
		case 8:
			return sounds.get(R.raw.l8);
		case 9:
			return sounds.get(R.raw.l9);
		case 10:
			return sounds.get(R.raw.l10);
		case 11:
			return sounds.get(R.raw.l11);
		case 12:
			return sounds.get(R.raw.l12);
		case 13:
			return sounds.get(R.raw.l13);
		case 14:
			return sounds.get(R.raw.l14);
		case 15:
			return sounds.get(R.raw.l15);
		case 16:
			return sounds.get(R.raw.l16);
		default:
			return sounds.get(R.raw.l16);
		}
	}
}
