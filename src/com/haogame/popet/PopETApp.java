package com.haogame.popet;

import java.util.UUID;

import org.hj20150901.lib.HejuInit;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class PopETApp extends Application {
	public static final String PREFER_FILE = "duiduipeng";
	public static final String GAME_PLAYER = "name";
	public static final String GAME_MUSIC = "music";
	public static final String COLOR_CHANGE_NUM = "color_change_num";
	public static final String GAME_NEED_SUBMIT = "submit";
	public static final String PHONE_ID = "phoneid";
	public static final String GAME_HIGHEST_SCORE = "score";
	public static final String GAME_IS_FIRST_START = "isfirststart";
	public static final String URL_RANKING_GET = "http://rank.easymobi.cn/app/index.php?id=%s&difficulty=%d&sort=desc&game=%s&packages=%s";
	public static final String URL_RANKING_SET = "http://rank.easymobi.cn/service/setusernews.php?id=%s&name=%s&top=%d&difficulty=%d&game=%s";
	public static String userName = "userName";
	public String gamePlayer;
	public String mPhoneID;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		Constant.SCREEN_WIDTH = metrics.widthPixels;
		Constant.SCREEN_HEIGHT = metrics.heightPixels;
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		gamePlayer = pref.getString(GAME_PLAYER, "name");
		mPhoneID = pref.getString(PHONE_ID, "phoneid");
		if (mPhoneID.equals("phoneid")) {
			TelephonyManager tm = (TelephonyManager) getBaseContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			String tmDevice, tmSerial, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = ""
					+ android.provider.Settings.Secure.getString(
							getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);
			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			mPhoneID = deviceUuid.toString();
			gamePlayer = tmDevice;
			Editor edit = pref.edit();
			edit.putString(PHONE_ID, mPhoneID);
			edit.putString(GAME_PLAYER, gamePlayer);
			edit.commit();
			
			
			
		}
	}

	public boolean isNeedRefreshScore(int curScore) {
		SharedPreferences pref = getSharedPreferences(PREFER_FILE, 0);
		int gScore = pref.getInt(GAME_HIGHEST_SCORE, -1);
		if (curScore > gScore) {
			return true;
		}
		return false;
	}

	public void saveMusicState(boolean musicState) {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		Editor edit = pref.edit();
		edit.putBoolean(GAME_MUSIC, musicState);
		edit.commit();
	}

	public void saveNeedSubmit(boolean isNeed) {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		Editor edit = pref.edit();
		edit.putBoolean(GAME_NEED_SUBMIT, isNeed);
		edit.commit();
	}

	public boolean isNeedSubmit() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		return pref.getBoolean(GAME_NEED_SUBMIT, true);
	}

	public void saveHighestScore(int score) {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		Editor edit = pref.edit();
		edit.putInt(GAME_HIGHEST_SCORE, score);
		edit.commit();
	}

	public boolean getMusicState() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		return pref.getBoolean(GAME_MUSIC, true);
	}

	public int getHighestScore() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		return pref.getInt(GAME_HIGHEST_SCORE, -1);
	}

	public int getChangeColorNum() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		return pref.getInt(COLOR_CHANGE_NUM, 5);
	}

	public void saveChangeColorNum(int num) {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		Editor edit = pref.edit();
		edit.putInt(COLOR_CHANGE_NUM, num);
		edit.commit();

	}

	public void saveJihuo(boolean jihuo) {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		Editor edit = pref.edit();
		edit.putBoolean("is_jihuo", jihuo);
		edit.commit();
	}

	public boolean getJihuo() {
		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				PREFER_FILE, 0);
		return pref.getBoolean("is_jihuo", false);
	}

}
