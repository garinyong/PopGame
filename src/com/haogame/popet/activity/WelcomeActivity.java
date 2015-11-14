package com.haogame.popet.activity;

 

import com.haogame.popet.SoundManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import com.haogame.popet.R;

public class WelcomeActivity extends Activity {
	private static final int TAG_BTN_MORE = 2;
	private static final int TAG_BTN_RANK = 1;
	private static final int TAG_BTN_START = 0;
	private static final int TAG_BTN_FORUM = 3;
	protected static final int MSG_REFRESH_UI = 0;
	private static final int MSG_START_ANIM = 2;
	private Button btnStart;
	private Button btnRank;
	private Button btnMore;
	private Button btnForum;
	private SoundManager soundMgr;
	private float density;
	private ImageView imgvTop;
	private ImageView imgvBottom;
	private View menuLayout;
	private TranslateAnimation mTranslateAnimationTop;
	private TranslateAnimation mTranslateAnimationBottom1;
	private TranslateAnimation mTranslateAnimationBottom2;
	private Animation shake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		density = getResources().getDisplayMetrics().density;
		Thread loadAudioThread = new Thread("loadAudio") {
			public void run() {
				mHandler.sendEmptyMessage(MSG_REFRESH_UI);
			};
		};
		loadAudioThread.start();
		

		 
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REFRESH_UI:
				initView();
				startAnim();
				 
				break;

			case MSG_START_ANIM:
				imgvTop.startAnimation(shake);
				break;

			}
		};
	};

	private void initView() {
		imgvTop = (ImageView) findViewById(R.id.img_top);
		imgvBottom = (ImageView) findViewById(R.id.img_bottom);
		menuLayout = findViewById(R.id.menu_layout);
		btnStart = (Button) findViewById(R.id.btn_play);
		btnRank = (Button) findViewById(R.id.btn_rank);
		btnRank.setVisibility(View.GONE);
		btnMore = (Button) findViewById(R.id.btn_more);
		btnForum = (Button) findViewById(R.id.btn_forum);

		btnStart.setOnClickListener(mBtnWelcomListener);
		btnRank.setOnClickListener(mBtnWelcomListener);
		btnMore.setOnClickListener(mBtnWelcomListener);
		btnForum.setOnClickListener(mBtnWelcomListener);

		btnStart.setTag(TAG_BTN_START);
		btnRank.setTag(TAG_BTN_RANK);
		btnMore.setTag(TAG_BTN_MORE);
		btnForum.setTag(TAG_BTN_FORUM);

	}

	private void startAnim() {
		// TODO Auto-generated method stub
		shake = AnimationUtils.loadAnimation(this, R.anim.shake);

		mTranslateAnimationTop = new TranslateAnimation(0, 0, -333 * density, 0);
		mTranslateAnimationTop.setFillAfter(true);
		mTranslateAnimationTop.setDuration(1000);

		mTranslateAnimationBottom1 = new TranslateAnimation(0, 0,
				100 * density, 0);
		mTranslateAnimationBottom1.setFillAfter(true);
		mTranslateAnimationBottom1.setDuration(1000);

		mTranslateAnimationBottom2 = new TranslateAnimation(0, 0,
				400 * density, 0);
		mTranslateAnimationBottom2.setFillAfter(true);
		mTranslateAnimationBottom2.setDuration(1000);

		imgvTop.startAnimation(mTranslateAnimationTop);
		imgvBottom.startAnimation(mTranslateAnimationBottom1);
		menuLayout.startAnimation(mTranslateAnimationBottom2);

		Message msg = new Message();
		msg.what = MSG_START_ANIM;
		mHandler.sendMessageDelayed(msg, 1000);

	}

	OnClickListener mBtnWelcomListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;
			soundMgr.pauseGameSound(SoundManager.SOUND_WELCOME);
			soundMgr.play(SoundManager.SOUND_BTN_START);
			switch ((Integer) v.getTag()) {
			case TAG_BTN_START:
				intent = new Intent(WelcomeActivity.this, GameActivity.class);
				startActivity(intent);
				break;
			case TAG_BTN_RANK:
				intent = new Intent(WelcomeActivity.this, RankActivity.class);
				startActivity(intent);
				break;
			case TAG_BTN_MORE:
				Uri uri = Uri.parse("http://ad.plat56.com/");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				break;
			case TAG_BTN_FORUM:
				Uri uri1 = Uri.parse("http://m.uugames.cn/");
				intent = new Intent(Intent.ACTION_VIEW, uri1);
				startActivity(intent);
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		soundMgr = SoundManager.getInstance(this);
		soundMgr.playGameSound(SoundManager.SOUND_WELCOME);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		soundMgr.relase();
	}
}
