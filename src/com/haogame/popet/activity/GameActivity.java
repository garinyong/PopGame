package com.haogame.popet.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.haogame.popet.Constant;
import com.haogame.popet.PayUtil;
import com.haogame.popet.PayUtil.PayResultListener;
import com.haogame.popet.PopETApp;
import com.haogame.popet.R;
import com.haogame.popet.SoundManager;
import com.haogame.popet.adapter.GameAdapter;
import com.haogame.popet.entity.GameData;
import com.haogame.popet.entity.Item;
import com.haogame.popet.surfaceview.ParticleView;

public class GameActivity extends Activity {
	private Button btnMusic;
	private Button btnShowMenu;
	private Button btnHideMenu;
	private Button btnAddTools;
	private TextView tvChnageColorNum;
	private TextView tvLevel;
	private TextView tvEndAddScore;
	private TextView tvEndAddScore1;
	private TextView tvCurrScore;
	private TextView tvAddScore;
	private TextView tvTargetScore;
	private TextView tvToolNum; // tv_tool_num;
	private TextView tvHighScore;
	private ImageView imgvWin1;
	private ImageView imgvWin2;
	private ImageView imgvReachTartgetScore;
	private ImageView imgvReachTargetScoreDialog;
	private ImageView ivBonus;
	private ImageView imgvGmeover;
	private ImageView imgvGameoverWord;
	private View gameoverLayout;
	private Button btnRestart;
	private ParticleView pv;
	private GridView gdGame;
	private PopETApp app;
	public static int iGameState;
	private int currentLevel;
	private GameAdapter gameAdapter;
	private GameData gamedata;
	private ArrayList<Item> itemList;
	private static DisplayMetrics dm;
	public static final int MSG_ITEM_BOON = 0;
	public static final int MSG_INIT_LEVEL = 7;
	public static final int MSG_END_LEVEL = 8;
	public static final int MSG_NEXTLEVEL_ANIM_START = 9;
	private static final int MSG_GAMEOVER_ANIM_START = 10;
	private static final int MSG_REACH_TARGETSCORE = 11;
	private static final int MSG_REACH_TARGETSCORE_SOUND_END = 12;
	private static final int MSG_END_LEVEL_SOUND_START = 14;
	private static final int MSG_FUHUO_PROGRESS = 17;
	public static final int MSG_SHOW_FUHUO_DIALOG = 18;
	private static final int TAG_BTN_MUSIC = 15;
	private static final int TAG_BTN_RESTART = 16;
	 
	private static final int TAG_BTN_SHOW_MENU = 18;
	private static final int TAG_BTN_HIDE_MENU = 19;
	private static final int TAG_BTN_ADD_TOOLS = 20;
	private static final int TAG_BTN_FUHUO = 21;
	private static final int TAG_BTN_ET_START = 101;
	private static final int TAG_BTN_JIHUO = 22;
	private static int flagImgChange = -1;
	private SoundManager soundMgr;
	private AlphaAnimation mBonusAlphaAnim, mWinAlphaAnim, mAddEndScoreAnim;
	private ScaleAnimation mScaleAnim;
	private TranslateAnimation mGameoverAnim1, mGameoverAnim2,
			mShowTopMenuAnim, mHideTopMenuAnim;
	private float fXCoordinate;
	private float fYCoordinate;
	public static boolean bMusic;
	private static boolean mIsActivityResume;
	private int bgSrc[];
	private View gamelayout;
	private LinearLayout topMenuLayout;
	private LinearLayout etLayout;
	private boolean bFlag;
	private static boolean isGetAllItemView;
	private boolean mFlag; // 是否已经达到了目标分
	private boolean nextLevelSoundEnd = false;
	private ArrayList<ImageView> imgArr;

	private Dialog fuhuoDialog;
	private ProgressBar fuhuoProgressBar;
	public boolean bFuhuoDialogShow;
	public boolean bDialogShow;

	public int fuhuoValue;
	private boolean isBackground;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// EMPayManager.choose(this);
		// EMTotalPayManager.init(this);
		setContentView(R.layout.game_layout);
		isBackground = false;
		app = (PopETApp) getApplicationContext();
		dm = getApplicationContext().getResources().getDisplayMetrics();
		bMusic = app.getMusicState();
		mIsActivityResume = true;
		soundMgr = SoundManager.getInstance(this);
		bgSrc = new int[] { R.drawable.bg_play_ground1,
				R.drawable.bg_play_ground2, R.drawable.bg_play_ground3 };
		gamelayout = findViewById(R.id.game_view);
		imgArr = new ArrayList<ImageView>();

		initAnim();
		initView();
		initData();
	}

	public Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_INIT_LEVEL:
				GameActivity.iGameState = Constant.GAME_DEFALT;
				break;
			case MSG_ITEM_BOON:
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (HashMap<String, Object>) (msg.obj);
				ImageView img = (ImageView) map.get("imageView");
				int chipId = (Integer) map.get("chipId");
				img.setBackgroundResource(R.drawable.kongbai);
				pv.ps.add(
						5,
						0,
						(int) (msg.arg1 * 40 * dm.density + gdGame.getLeft() + 20 * dm.density),
						(int) (msg.arg2 * 40 * dm.density + gdGame.getTop() + 20 * dm.density),
						chipId);
				if (mIsActivityResume)
					soundMgr.play(SoundManager.SOUND_POOL_CLEAR);
				break;
			case MSG_END_LEVEL:
				gamedata.updateItemState(0);
				gamedata.endLevle();
				// 本关卡结束进入下一 关
				if (GameActivity.iGameState == Constant.GAME_NEXT_LEVEL) {
					if (bFlag) {
						gamedata.preNextLevel();
						tvEndAddScore.setVisibility(View.GONE);
						tvEndAddScore1.setVisibility(View.GONE);
						bFlag = false;
						gameAdapter.notifyDataSetChanged();

						tvCurrScore.setText(GameData.SCORE + "");
						if (gamedata.iRemainCount < 4) {
							switch (gamedata.iRemainCount) {
							case 0:
								tvEndAddScore.setText("+1000");
								tvEndAddScore1.setText("+1000");
								break;
							case 1:
								tvEndAddScore.setText("+500");
								tvEndAddScore1.setText("+500");
								break;
							case 2:
								tvEndAddScore.setText("+300");
								tvEndAddScore1.setText("+300");
								break;
							case 3:
								tvEndAddScore.setText("+100");
								tvEndAddScore1.setText("+100");
								break;

							}

							AnimationSet set = new AnimationSet(true);
							set.setFillAfter(true);

							TranslateAnimation transAnim = new TranslateAnimation(
									0, 0, 0, -100 * dm.density);
							transAnim.setDuration(1200);
							set.addAnimation(transAnim);
							set.addAnimation(mAddEndScoreAnim);
							tvEndAddScore.setVisibility(View.VISIBLE);
							tvEndAddScore1.setVisibility(View.VISIBLE);
							tvEndAddScore.startAnimation(set);
							tvEndAddScore1.startAnimation(set);
						}
					}

				}
				// 游戏结束
				if (GameActivity.iGameState == Constant.GAME_OVER) {

					if (bFlag) {

						gamedata.preNextLevel();
						tvEndAddScore.setVisibility(View.GONE);
						tvEndAddScore1.setVisibility(View.GONE);
						bFlag = false;
						gameAdapter.notifyDataSetChanged();
						tvCurrScore.setText(GameData.SCORE + "");
						if (gamedata.iRemainCount < 4) {
							switch (gamedata.iRemainCount) {
							case 0:
								tvEndAddScore.setText("+1000");
								tvEndAddScore1.setText("+1000");
								break;
							case 1:
								tvEndAddScore.setText("+500");
								tvEndAddScore1.setText("+500");
								break;
							case 2:
								tvEndAddScore.setText("+300");
								tvEndAddScore1.setText("+300");
								break;
							case 3:
								tvEndAddScore.setText("+100");
								tvEndAddScore1.setText("+100");
								break;

							}

							AnimationSet set = new AnimationSet(true);
							set.setFillAfter(true);

							TranslateAnimation transAnim = new TranslateAnimation(
									0, 0, 0, -100 * dm.density);
							transAnim.setDuration(1200);
							set.addAnimation(transAnim);
							set.addAnimation(mAddEndScoreAnim);
							tvEndAddScore.setVisibility(View.VISIBLE);
							tvEndAddScore1.setVisibility(View.VISIBLE);
							tvEndAddScore.startAnimation(set);
							tvEndAddScore1.startAnimation(set);
						}
					}

				}
				break;
			case MSG_REACH_TARGETSCORE: // 达到目标分
				if (mIsActivityResume)
					soundMgr.playGameSound(SoundManager.SOUND_APPLAUSE);
				mHandler.sendEmptyMessageDelayed(
						GameActivity.MSG_REACH_TARGETSCORE_SOUND_END,
						soundMgr.mpGameSound.get(SoundManager.SOUND_APPLAUSE)
								.getDuration());
				imgvReachTartgetScore.setVisibility(View.VISIBLE);
				imgvReachTargetScoreDialog.setVisibility(View.VISIBLE);
				int tempScore = GameData.SCORE - GameData.targetScore;
				if (tempScore > 200)
					imgvReachTargetScoreDialog
							.setBackgroundResource(R.drawable.bg_reach_targetscore_dialog3);
				else if (tempScore > 80)
					imgvReachTargetScoreDialog
							.setBackgroundResource(R.drawable.bg_reach_targetscore_dialog2);
				else if (tempScore > 0)
					imgvReachTargetScoreDialog
							.setBackgroundResource(R.drawable.bg_reach_targetscore_dialog1);
				break;

			case MSG_NEXTLEVEL_ANIM_START:
				if (mIsActivityResume && isGetAllItemView)
					soundMgr.playGameSound(SoundManager.SOUND_WIN_LEVEL);
				imgvWin2.setVisibility(View.VISIBLE);
				imgvWin1.setVisibility(View.VISIBLE);
				imgvWin1.startAnimation(mWinAlphaAnim);

				nextLevelSoundEnd = true;

				break;
			case MSG_GAMEOVER_ANIM_START:
				if (mIsActivityResume && isGetAllItemView)
					soundMgr.playGameSound(SoundManager.SOUND_GAME_OVER);
				gameoverLayout.setVisibility(View.VISIBLE);
				imgvGmeover.startAnimation(mGameoverAnim2);
				imgvGameoverWord.startAnimation(mGameoverAnim1);
				break;
			case MSG_REACH_TARGETSCORE_SOUND_END:
				imgvReachTartgetScore.setVisibility(View.GONE);
				imgvReachTargetScoreDialog.setVisibility(View.GONE);
				break;
			case MSG_END_LEVEL_SOUND_START:
				isGetAllItemView = true;
				break;

			case MSG_SHOW_FUHUO_DIALOG:
				showFuhuoDialog();
				break;

			case MSG_FUHUO_PROGRESS:
				if (fuhuoValue >= 0) {
					if (!isBackground) {
						fuhuoValue -= 10;
						fuhuoProgressBar.setProgress(fuhuoValue);
					}
					mHandler.sendEmptyMessageDelayed(MSG_FUHUO_PROGRESS, 100);
				} else {
					fuhuoDialog.cancel();
					mHandler.removeMessages(MSG_FUHUO_PROGRESS);
					mHandler.sendEmptyMessage(MSG_GAMEOVER_ANIM_START);

				}

				break;

			}
		}
	};

	private void initAnim() {

		// TODO Auto-generated method stub
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		// int height = metric.heightPixels; // 屏幕高度（像素）
		mAddEndScoreAnim = new AlphaAnimation(0, 1f);
		mAddEndScoreAnim.setDuration(1200);
		mAddEndScoreAnim.setRepeatCount(1);
		mAddEndScoreAnim.setRepeatMode(Animation.REVERSE);

		mBonusAlphaAnim = new AlphaAnimation(0, 1f);
		mBonusAlphaAnim.setDuration(1200);
		mBonusAlphaAnim.setRepeatCount(1);
		mBonusAlphaAnim.setRepeatMode(Animation.REVERSE);
		mScaleAnim = new ScaleAnimation(0, 1.0f, 0, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mScaleAnim.setFillAfter(true);
		mScaleAnim.setDuration(300);

		mGameoverAnim1 = new TranslateAnimation(-381, 0, 0, 0);
		mGameoverAnim1.setDuration(800);
		mGameoverAnim1.setFillAfter(true);

		mShowTopMenuAnim = new TranslateAnimation(width, 0, 0, 0);
		mShowTopMenuAnim.setDuration(200);
		mShowTopMenuAnim.setFillAfter(true);

		mHideTopMenuAnim = new TranslateAnimation(0, width, 0, 0);
		mHideTopMenuAnim.setDuration(200);
		mHideTopMenuAnim.setFillAfter(true);

		mHideTopMenuAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				// TODO Auto-generated method stub

				btnShowMenu.setVisibility(View.VISIBLE);
				btnHideMenu.setVisibility(View.GONE);
				btnAddTools.setVisibility(View.GONE);
				topMenuLayout.setVisibility(View.GONE);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

				// TODO Auto-generated method stub

			}

		});
		mGameoverAnim2 = new TranslateAnimation(480, 0, 0, 0);
		mGameoverAnim2.setDuration(800);
		mGameoverAnim2.setFillAfter(true);
		mGameoverAnim2.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				// TODO Auto-generated method stub
				btnRestart.setVisibility(View.VISIBLE);
				 
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

				// TODO Auto-generated method stub

			}

		});

		mWinAlphaAnim = new AlphaAnimation(1, 0);
		mWinAlphaAnim.setDuration(2000);
		mWinAlphaAnim.setInterpolator(this, R.anim.cycle1);

	}

	private void initData() {

		// TODO Auto-generated method stub
		mFlag = false;
		bFlag = true;
		bDialogShow = false;
		nextLevelSoundEnd = false;
		bFuhuoDialogShow = false;
		fuhuoValue = 500;
		etLayout.removeAllViews();
		imgArr.clear();
		gamedata = new GameData(currentLevel++);
		if (currentLevel >= 8) {
			if ((currentLevel) % 2 == 0) {
				app.saveChangeColorNum(app.getChangeColorNum() + 1);
				Toast.makeText(GameActivity.this, getString(R.string.get_tool),
						Toast.LENGTH_LONG).show();
			}
		}
		gamelayout.setBackgroundResource(bgSrc[(currentLevel - 1) % 3]);

		itemList = gamedata.getItems();
		gameAdapter = new GameAdapter(this, itemList, mHandler);
		gdGame.setAdapter(gameAdapter);
		if (mIsActivityResume) {
			soundMgr.playGameSound(SoundManager.SOUND_NEW_LEVEL);

		}

		iGameState = Constant.GAME_FALLING_INIT;
		tvChnageColorNum.setText(app.getChangeColorNum() + "");
		setTvToolNumBackImage();
		tvToolNum.setText(app.getChangeColorNum() + "");
		tvLevel.setText(String.valueOf(currentLevel));
		tvTargetScore.setText("TARGET: " + GameData.targetScore);
		GameData.endAcounts = 0;

		int len = gamedata.arrImgId.size();

		for (int i = 0; i < len; i++) {
			ImageView imgEt = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(3, 0, 0, 0);
			imgEt.setLayoutParams(params);
			imgEt.setBackgroundResource(gamedata.imgIds[gamedata.arrImgId
					.get(i)]);

			imgEt.setClickable(true);
			imgEt.setOnClickListener(mClick);
			imgEt.setTag(TAG_BTN_ET_START + i);
			imgArr.add(imgEt);
			etLayout.addView(imgEt);
		}
	}

	private void initView() {

		// TODO Auto-generated method stub

		topMenuLayout = (LinearLayout) findViewById(R.id.topmenu);
		etLayout = (LinearLayout) topMenuLayout.findViewById(R.id.et_layout);

		tvChnageColorNum = (TextView) topMenuLayout
				.findViewById(R.id.tv_change_color_num);
		tvChnageColorNum.setText(app.getChangeColorNum() + "");

		tvToolNum = (TextView) findViewById(R.id.tv_tool_num);
		setTvToolNumBackImage();
		tvToolNum.setText(app.getChangeColorNum() + "");

		tvLevel = (TextView) findViewById(R.id.tv_level);
		btnMusic = (Button) findViewById(R.id.btn_music);
		btnMusic.setTag(TAG_BTN_MUSIC);
		btnMusic.setOnClickListener(mClick);

		btnAddTools = (Button) topMenuLayout.findViewById(R.id.btn_add);
		btnAddTools.setTag(TAG_BTN_ADD_TOOLS);
		btnAddTools.setOnClickListener(mClick);

		btnShowMenu = (Button) findViewById(R.id.btn_show_menu);
		btnShowMenu.setTag(TAG_BTN_SHOW_MENU);
		btnShowMenu.setOnClickListener(mClick);

		btnHideMenu = (Button) topMenuLayout.findViewById(R.id.btn_hide_menu);
		btnHideMenu.setTag(TAG_BTN_HIDE_MENU);
		btnHideMenu.setOnClickListener(mClick);

		imgvWin1 = (ImageView) findViewById(R.id.img_win1);
		imgvWin2 = (ImageView) findViewById(R.id.img_win2);
		imgvGmeover = (ImageView) findViewById(R.id.img_gameover);
		imgvGameoverWord = (ImageView) findViewById(R.id.img_gameover_word);
		gameoverLayout = findViewById(R.id.gameover_layout);
		imgvReachTartgetScore = (ImageView) findViewById(R.id.img_reach_targetscore);
		imgvReachTargetScoreDialog = (ImageView) findViewById(R.id.img_reach_targetscore_dialog);
		btnRestart = (Button) findViewById(R.id.btn_restart);
		btnRestart.setTag(TAG_BTN_RESTART);
		btnRestart.setOnClickListener(mClick);
		gdGame = (GridView) findViewById(R.id.gd_game);
		gdGame.setAdapter(gameAdapter);
		gdGame.setOnTouchListener(mTouch);
		pv = (ParticleView) findViewById(100);
		tvCurrScore = (TextView) findViewById(R.id.tv_currentscore);
		tvHighScore = (TextView) findViewById(R.id.tv_maxscore);
		tvAddScore = (TextView) findViewById(R.id.tv_add_score);
		tvEndAddScore = (TextView) findViewById(R.id.tv_end_add_score);
		tvEndAddScore1 = (TextView) findViewById(R.id.tv_end_add_score1);
		Paint paint = ((TextView) findViewById(R.id.tv_end_add_score1))
				.getPaint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(8);

		tvTargetScore = (TextView) findViewById(R.id.tv_target_score);
		ivBonus = (ImageView) findViewById(R.id.imageView1);
		int maxScore = app.getHighestScore();
		if (maxScore == -1) {
			maxScore = 0;
		}
		tvHighScore.setText("HIGH: " + maxScore);

	}

	private void setTvToolNumBackImage() {

		if (app.getChangeColorNum() < 100) {
			tvToolNum.setBackgroundResource(R.drawable.bg_tool_num);
		} else {
			tvToolNum.setBackgroundResource(R.drawable.bg_tool_num1);
		}

	}

	private void removeImgSource(int index) {

		int len = imgArr.size();
		for (int i = 0; i < len; i++) {
			ImageView imgView = imgArr.get(i);
			imgView.setImageResource(R.drawable.kongbai);
		}
	}

	private OnClickListener mClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			// TODO Auto-generated method stub
			SoundManager.getInstance(GameActivity.this).play(
					SoundManager.SOUND_BTN_START);
			switch ((Integer) v.getTag()) {
			case TAG_BTN_JIHUO:

				payForJihuo();

				break;

			case TAG_BTN_MUSIC:
				if (bMusic) {
					bMusic = false;
					btnMusic.setBackgroundResource(R.drawable.bg_music_off);
					soundMgr.pauseAllSound();
				} else {

					bMusic = true;
					btnMusic.setBackgroundResource(R.drawable.bg_music_on);
					soundMgr.playGameSound(SoundManager.SOUND_BG);
				}
				app.saveMusicState(bMusic);
				break;
			case TAG_BTN_RESTART:
				restartGame();
				break;
			case TAG_BTN_ADD_TOOLS:

			int proIndex = 1;
				
				AlertDialog.Builder payDialog=new AlertDialog.Builder(GameActivity.this);  
				payDialog.setMessage("您确定要购买"+ PayUtil.PRO_COUNT[proIndex] + "个魔法棒吗？");
				  
				payDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
		              
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                // TODO Auto-generated method stub  
		            	
		            	
						PayUtil.payWithProductIndex(GameActivity.this,
								1, new PayResultListener() {

									@Override
									public void onResut(int state, int proIndex) {
										// TODO Auto-generated method stub
										switch (state) {
										case PayUtil.PAY_STATE_SUCCESS:
											app.saveChangeColorNum(app.getChangeColorNum()
													+ PayUtil.PRO_COUNT[proIndex]);
											break;
										case PayUtil.PAY_STATE_FAILED:

											break;
										}

									}

								}

						);


		                   
		            }  
		        });  
				payDialog.setNegativeButton("取消", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						
					}
					
				});
 
				payDialog.show();
				 
//				startActivity(new Intent(GameActivity.this, ShopActivity.class));
				break;

			case TAG_BTN_SHOW_MENU:
				btnShowMenu.setVisibility(View.GONE);
				btnHideMenu.setVisibility(View.VISIBLE);
				btnAddTools.setVisibility(View.VISIBLE);
				topMenuLayout.setVisibility(View.VISIBLE);
				topMenuLayout.startAnimation(mShowTopMenuAnim);
				break;
			case TAG_BTN_HIDE_MENU:
				removeImgSource(flagImgChange);
				topMenuLayout.startAnimation(mHideTopMenuAnim);
				break;

			case TAG_BTN_ET_START:
				flagImgChange = 0;
				soundMgr.play(SoundManager.SOUND_POOL_SELECT);
				removeImgSource(flagImgChange);
				ImageView img0 = imgArr.get(flagImgChange);
				img0.setImageResource(R.drawable.border);

				break;
			case TAG_BTN_FUHUO:
				Button button = (Button) v;
				button.setEnabled(false);
				if (fuhuoValue >= 0)
					payForFuhuo();

				break;

			case TAG_BTN_ET_START + 1:
				flagImgChange = 1;
				soundMgr.play(SoundManager.SOUND_POOL_SELECT);
				removeImgSource(flagImgChange);
				ImageView img1 = imgArr.get(flagImgChange);
				img1.setImageResource(R.drawable.border);
				break;
			case TAG_BTN_ET_START + 2:
				flagImgChange = 2;
				soundMgr.play(SoundManager.SOUND_POOL_SELECT);
				removeImgSource(flagImgChange);
				ImageView img2 = imgArr.get(flagImgChange);
				img2.setImageResource(R.drawable.border);
				break;
			case TAG_BTN_ET_START + 3:
				flagImgChange = 3;
				soundMgr.play(SoundManager.SOUND_POOL_SELECT);
				removeImgSource(flagImgChange);
				ImageView img3 = imgArr.get(flagImgChange);
				img3.setImageResource(R.drawable.border);
				break;
			case TAG_BTN_ET_START + 4:
				flagImgChange = 4;
				soundMgr.play(SoundManager.SOUND_POOL_SELECT);
				removeImgSource(flagImgChange);
				ImageView img4 = imgArr.get(flagImgChange);
				img4.setImageResource(R.drawable.border);
				break;
			case TAG_BTN_ET_START + 5:
				flagImgChange = 5;
				soundMgr.play(SoundManager.SOUND_POOL_SELECT);
				removeImgSource(flagImgChange);
				ImageView img5 = imgArr.get(flagImgChange);
				img5.setImageResource(R.drawable.border);
				break;
			}

		}

	};

	private OnTouchListener mTouch = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				removeImgSource(flagImgChange);
				int position = -1;
				int x = (int) event.getX();
				int y = (int) event.getY();
				int prevPos = gdGame.getChildCount();
				for (int i = 0; i < prevPos; i++) {
					Rect ButtonRect = new Rect();
					gdGame.getChildAt(i).getHitRect(ButtonRect);
					if (ButtonRect.contains(x, y)) {
						position = i;
						break;
					}
				}

				// TODO Auto-generated method stub
				if (position > -1) {
					if (GameActivity.iGameState == Constant.GAME_NEXT_LEVEL
							|| GameActivity.iGameState == Constant.GAME_OVER) {
						return false;
					}

					if (flagImgChange > -1
							&& itemList.get(position).imgId != R.drawable.kongbai) {
						int tempImagId = gamedata.imgIds[gamedata.arrImgId
								.get(flagImgChange)];
						Item item = itemList.get(position);
						if (app.getChangeColorNum() > 0) {
							if (topMenuLayout.getVisibility() == View.VISIBLE) {
								if (item.imgId != tempImagId) {
									soundMgr.play(SoundManager.SOUND_POOL_SELECT);
									item.imgId = tempImagId;
									app.saveChangeColorNum(app
											.getChangeColorNum() - 1);
									tvChnageColorNum.setText(app
											.getChangeColorNum() + "");
									setTvToolNumBackImage();
									tvToolNum.setText(app.getChangeColorNum()
											+ "");
									gamedata.initItemState();
									itemList = gamedata.getItems();
									gameAdapter.setData(itemList);
									gameAdapter.notifyDataSetChanged();
								}
								flagImgChange = -1;
								return false;

							}
						} else {
							Toast.makeText(GameActivity.this,
									getString(R.string.no_tools),
									Toast.LENGTH_LONG).show();
						}
						flagImgChange = -1;

						// return false;
					}

					gamedata.refreshData(position);
					if (itemList.get(position).imgId == R.drawable.kongbai) {
						return false;
					}
					itemList = gamedata.getItems();

					gameAdapter.setData(itemList);
					gameAdapter.notifyDataSetChanged();
					if (GameActivity.iGameState == Constant.GAME_SELECT
							&& GameData.iAccounts > 1) {
						soundMgr.play(SoundManager.SOUND_POOL_SELECT);
						int addScore = (int) (Math.pow(GameData.iAccounts, 2) * 20);
						tvAddScore.setText(GameData.iAccounts + " BLOCKS "
								+ addScore + " POINTS");
						tvAddScore.setVisibility(View.VISIBLE);
						tvAddScore.startAnimation(mScaleAnim);
					}
					if (GameActivity.iGameState == Constant.GAME_FALLING) {

						tvAddScore.setText("");
						gamedata.winLevel();
					}
					if (GameActivity.iGameState == Constant.GAME_REACH_TARGETSCORE
							&& !mFlag) {
						mHandler.sendEmptyMessage(MSG_REACH_TARGETSCORE);
						mFlag = true;
					}
					int delCount = GameData.iAccounts;
					if (delCount > 0
							&& GameActivity.iGameState == Constant.GAME_FALLING) {
						playBonusAnim(delCount);
					}
					tvCurrScore.setText(GameData.SCORE + "");
					// 当当前分数超过了保存的分数 就要从新更新 并且需要提交改为true
					if (app.isNeedRefreshScore(GameData.SCORE)) {
						app.saveHighestScore(GameData.SCORE);
						app.saveNeedSubmit(true);

					}

				}
			}
			if (event.getAction() == MotionEvent.ACTION_UP) {
				// TODO Auto-generated method stub
				fYCoordinate = event.getY() + gdGame.getTop();
				fXCoordinate = event.getX() - 150 * dm.density;
				if (imgvReachTartgetScore.getVisibility() == View.VISIBLE) {
					imgvReachTartgetScore.setVisibility(View.GONE);
					imgvReachTargetScoreDialog.setVisibility(View.GONE);
					soundMgr.pauseGameSound(SoundManager.SOUND_APPLAUSE);
				}

				if (iGameState == Constant.GAME_NEXT_LEVEL && nextLevelSoundEnd) {
					soundMgr.pauseGameSound(SoundManager.SOUND_WIN_LEVEL);
					imgvWin1.setVisibility(View.GONE);
					imgvWin2.setVisibility(View.GONE);
					imgvWin1.clearAnimation();
					if (currentLevel == 2 && !app.getJihuo()) {
						showJihuoDialog();
					  
					} else {
						initData();
					}
				}

			}

			return false;
		}

	};

	private void playBonusAnim(int delCount) {

		TranslateAnimation transAnim = new TranslateAnimation(fXCoordinate,
				fXCoordinate, fYCoordinate, fYCoordinate - 100 * dm.density);
		transAnim.setDuration(1200);
		if (delCount > 8)
			ivBonus.setImageResource(R.drawable.bonus9s);
		else if (delCount > 6)
			ivBonus.setImageResource(R.drawable.bonus7s);
		else if (delCount > 4)
			ivBonus.setImageResource(R.drawable.bonus5s);
		else if (delCount > 2)
			ivBonus.setImageResource(R.drawable.bonus3s);
		else
			ivBonus.setImageResource(0);

		AnimationSet set = new AnimationSet(true);
		set.setFillAfter(true);
		set.addAnimation(transAnim);
		set.addAnimation(mBonusAlphaAnim);
		ivBonus.setVisibility(View.VISIBLE);
		ivBonus.startAnimation(set);
	}

	@Override
	protected void onResume() {

		// TODO Auto-generated method stub
		super.onResume();
		// EMTotalPayManager.onResume(this);
		isBackground = false;
		mIsActivityResume = true;
		app = (PopETApp) getApplicationContext();
		soundMgr.loadSoundRes(this);
		tvChnageColorNum.setText(app.getChangeColorNum() + "");
		setTvToolNumBackImage();
		tvToolNum.setText(app.getChangeColorNum() + "");
		mHandler.sendEmptyMessageDelayed(MSG_END_LEVEL_SOUND_START, 5000);
		if (bMusic) {
			btnMusic.setBackgroundResource(R.drawable.bg_music_on);
			soundMgr.playGameSound(SoundManager.SOUND_BG);
		} else {
			btnMusic.setBackgroundResource(R.drawable.bg_music_off);
		}
	}

	@Override
	protected void onPause() {

		// TODO Auto-generated method stub
		super.onPause();
		// EMTotalPayManager.onPause(this);
		isBackground = true;
		isGetAllItemView = false;
		mIsActivityResume = false;

		 soundMgr.pauseAllSound();

	}

	@Override
	protected void onDestroy() {

		// TODO Auto-generated method stub
		super.onDestroy();
		GameData.SCORE = 0;
		pv.dt.flag = false;
		pv.dt = null;

	}

	private void restartGame() {
		currentLevel = 0;
		GameData.SCORE = 0;
		soundMgr.pauseGameSound(SoundManager.SOUND_GAME_OVER);
		tvCurrScore.setText(GameData.SCORE + "");
		gameoverLayout.setVisibility(View.GONE);
		btnRestart.setVisibility(View.INVISIBLE);
	 

		initData();

	}

	private void payForJihuo() {
		PayUtil.payWithProductIndex(GameActivity.this, 3,
				new PayResultListener() {

					@Override
					public void onResut(int state, int proIndex) {
						// TODO Auto-generated method stub
						switch (state) {
						case PayUtil.PAY_STATE_SUCCESS:
							dialog.dismiss();
							app.saveJihuo(true);
							initData();
							break;
						case PayUtil.PAY_STATE_FAILED:
							dialog.dismiss();
							restartGame();
							break;
						}

					}

				}

		);
	};

	private void showFuhuoDialog() {

		fuhuoDialog = new Dialog(this, R.style.dialog1);
		fuhuoDialog.setContentView(R.layout.dialog_fuhuo);
		fuhuoDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {

				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		fuhuoProgressBar = (ProgressBar) fuhuoDialog
				.findViewById(R.id.fuhuo_progress);
		fuhuoProgressBar.setMax(500);
		fuhuoProgressBar.setProgress(500);
		mHandler.sendEmptyMessageDelayed(MSG_FUHUO_PROGRESS, 100);
		Button btnFuhuo = (Button) fuhuoDialog.findViewById(R.id.btn_fuhuo);
		btnFuhuo.setTag(TAG_BTN_FUHUO);
		btnFuhuo.setOnClickListener(mClick);
		if (!fuhuoDialog.isShowing()) {
			fuhuoDialog.show();
			bFuhuoDialogShow = true;
		}

	}

	private void payForFuhuo() {

		bDialogShow = false;
		mHandler.removeMessages(MSG_FUHUO_PROGRESS);
		fuhuoDialog.cancel();

		PayUtil.payWithProductIndex(GameActivity.this, 4,
				new PayResultListener() {

					@Override
					public void onResut(int state, int proIndex) {
						// TODO Auto-generated method stub
						switch (state) {
						case PayUtil.PAY_STATE_SUCCESS:
							fuhuo();
							break;
						case PayUtil.PAY_STATE_FAILED:
							 if (mIsActivityResume && isGetAllItemView)
							 soundMgr.playGameSound(SoundManager.SOUND_GAME_OVER);
							gameoverLayout.setVisibility(View.VISIBLE);
							imgvGmeover.startAnimation(mGameoverAnim2);
							imgvGameoverWord.startAnimation(mGameoverAnim1);
							break;
						}

					}

				}

		);
	}

	private void fuhuo() {
		fuhuoDialog.cancel();
		mHandler.removeMessages(MSG_FUHUO_PROGRESS);
		bFuhuoDialogShow = false;
		bDialogShow = false;
		currentLevel--;
		initData();

	}

	private void showJihuoDialog() {
		if (dialog != null && dialog.isShowing())
			return;
		// 弹出激活页面
		dialog = new Dialog(GameActivity.this, R.style.dialog1);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_jihuo, null);
		dialog.setContentView(view, new LayoutParams(
				(int) Constant.SCREEN_WIDTH, (int) Constant.SCREEN_HEIGHT));

		Button btnActive = (Button) dialog.findViewById(R.id.btn_jihuo);
		btnActive.setTag(TAG_BTN_JIHUO);
		btnActive.setOnClickListener(mClick);
		dialog.setCancelable(false);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					finish();
					return false;
				} else {
					return false; // 默认返回 false
				}
			}
		});

		dialog.show();
	}

}
