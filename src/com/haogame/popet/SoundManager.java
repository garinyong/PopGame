package com.haogame.popet;

import java.util.HashMap;
import java.util.Map;

 

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import com.haogame.popet.R;

public class SoundManager {
	private static SoundManager instance;
	public Map<String, MediaPlayer> mpGameSound;
	private Context mContext;
	private SoundPool sp;
	private int[] arrSound;
	private String[] arrBgName;
	public final static String SOUND_BG = "bgmusic";
	public final static String SOUND_BOMB = "bomb2";
	public final static String SOUND_GAME_OVER = "gameover";
	public final static String SOUND_IS_BOMUS = "isbonus";
	public final static String SOUND_LESS_THAN1 = "lessthan1";
	public final static String SOUND_NEW_LEVEL = "newlevle";
	public final static String SOUND_NO_BONUS = "nobonus";
	public final static String SOUND_TIME_OVERING = "timeovering";
	public static final String SOUND_WIN_LEVEL = "winlevel";
	public static final String SOUND_APPLAUSE = "applause";
	public static final String SOUND_WELCOME = "welcome";

	public static final int SOUND_POOL_CLEAR = 0;
	public static final int SOUND_POOL_SELECT = 1;
	public static final int SOUND_BTN_START = 2;

	private PopETApp app;

	private SoundManager(Context context) {
		this.mContext = context.getApplicationContext();
		app = (PopETApp) context.getApplicationContext();
		loadSoundRes(mContext);

	}

	public static SoundManager getInstance(Context context) {
		if (instance == null) {
			instance = new SoundManager(context);
		}
		return instance;
	}

	public int play(int sound) {
		if (app.getMusicState() && sp != null) {
			return sp.play(arrSound[sound], 1, 1, 0, 0, 1);
		}
		return 0;
	}

	public void loadSoundRes(Context context) {

		// 初始化游戏音效
		mpGameSound = new HashMap<String, MediaPlayer>();
		int[] arrBgRes = new int[] { R.raw.bgmusic, R.raw.gameover,
				R.raw.lessthan1, R.raw.newlevle, R.raw.winlevel,
				R.raw.applause, R.raw.welcome };
		arrBgName = new String[] { SOUND_BG, SOUND_GAME_OVER, SOUND_LESS_THAN1,
				SOUND_NEW_LEVEL, SOUND_WIN_LEVEL, SOUND_APPLAUSE, SOUND_WELCOME };
		for (int i = 0; i < arrBgRes.length; i++) {
			MediaPlayer mediaPlayer = MediaPlayer.create(mContext, arrBgRes[i]);
			if (mediaPlayer != null)
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			if (i == 0)
				mediaPlayer.setLooping(true);
			mpGameSound.put(arrBgName[i], mediaPlayer);
		}

		sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		int[] arrRes = { R.raw.pop_star, R.raw.select, R.raw.button_start };
		arrSound = new int[arrRes.length];
		int i = 0;
		for (int raw : arrRes) {
			arrSound[i] = sp.load(context, raw, 1);
			i++;
		}

	}

	public void pauseAllSound() {

		for (String soundName : arrBgName) {
			MediaPlayer mp = mpGameSound.get(soundName);
			if (mp != null && mp.isPlaying()) {
				mp.pause();
			}
		}
	}

	public void pauseGameSound(String soundName) {
		MediaPlayer mp = mpGameSound.get(soundName);
		if (mp != null && mp.isPlaying()) {
			mp.pause();
		}
	}

	public void playGameSound(String soundName) {
		MediaPlayer mp = mpGameSound.get(soundName);
		if (mp != null && app.getMusicState()) {
			mpGameSound.get(soundName).seekTo(0);
			mpGameSound.get(soundName).start();
		}

	}

	public void relase() {
		if (sp != null) {
			sp.release();
			sp = null;
		}

		for (String soundName : arrBgName) {
			MediaPlayer mp = mpGameSound.get(soundName);
			if (mp != null) {
				mp.release();
				mp = null;
			}
		}
		instance = null;
	}

}
