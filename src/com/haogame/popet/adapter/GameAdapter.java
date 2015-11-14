package com.haogame.popet.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.haogame.popet.Constant;
import com.haogame.popet.activity.GameActivity;
import com.haogame.popet.entity.GameData;
import com.haogame.popet.entity.Item;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.haogame.popet.R;

public class GameAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Item> data;
	private Animation translateAnimation;
	private DisplayMetrics dm;
	private Handler mHandler;
	private int iCount;
	private boolean mFlag;
	private int timeSpan;

	public GameAdapter(Context context, ArrayList<Item> data, Handler mHandler) {
		this.context = context;
		this.mHandler = mHandler;
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		this.data = data;
		initAnim();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Item item = data.get(position);
		ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new GridView.LayoutParams(
				(int) (40 * dm.density), (int) (40 * dm.density)));// 设置Gallery中每一个图片的大小为80*80。
		imageView.setScaleType(ImageView.ScaleType.CENTER);
		imageView.setBackgroundResource(item.imgId);

		if (GameActivity.iGameState == Constant.GAME_FALLING_INIT) {
			TranslateAnimation animation = new TranslateAnimation(0, 0, -200
					* dm.density, 0);
			animation.setDuration(600);
			imageView.startAnimation(animation);
			if (position == (GameData.rowNum * GameData.colNum - 1)) {
				mHandler.sendEmptyMessage(GameActivity.MSG_INIT_LEVEL);
			}
		}
		if (GameActivity.iGameState == Constant.GAME_NEXT_LEVEL
				|| GameActivity.iGameState == Constant.GAME_OVER) {
			timeSpan = 150;
		} else {
			timeSpan = 50;
		}

		if (GameData.iAccounts > 1 && item.imgId != R.drawable.kongbai) {
			switch (item.state) {
			case Constant.ITEM_STATE_SELECTED:

				imageView.setImageResource(R.drawable.border);
				imageView.startAnimation(translateAnimation);
				break;
			case Constant.ITEM_STATE_SELECTED_SECEND:
				Message msg = new Message();
				msg.arg1 = position % GameData.colNum;
				msg.arg2 = position / GameData.colNum;
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("imageView", imageView);
				map.put("chipId", item.chipId);
				msg.obj = map;
				msg.what = GameActivity.MSG_ITEM_BOON;
				item.imgId = R.drawable.kongbai;
				item.chipId = R.drawable.kongbai;
				if (position == 0 && mFlag) {
					mHandler.sendMessageDelayed(msg, (timeSpan * iCount++));
					mFlag = false;
				} else if (position != 0)
					mHandler.sendMessageDelayed(msg, (timeSpan * iCount++));
				break;
			default:
				if (item.toX < 0) {
					TranslateAnimation animation = new TranslateAnimation(0,
							(float) ((item.toX) * 40 * dm.density) - 1, 0,
							(float) ((item.toY) * 40 * dm.density) + 1);
					animation.setFillAfter(true);
					animation.setDuration(300);
					imageView.startAnimation(animation);
				}
				if (item.toY > 0) {
					TranslateAnimation animation = new TranslateAnimation(0,
							(float) ((item.toX) * 40 * dm.density), -20,
							(float) ((item.toY) * 40 * dm.density) + 1);
					animation.setFillAfter(true);
					animation.setDuration(300);
					imageView.startAnimation(animation);
				}

			}

		}
		if (position == GameData.rowNum * GameData.colNum - 1) {
			if (GameActivity.iGameState == Constant.GAME_FALLING
					|| GameActivity.iGameState == Constant.GAME_REACH_TARGETSCORE)
				mHandler.sendEmptyMessage(GameActivity.MSG_END_LEVEL);
			if (GameActivity.iGameState == Constant.GAME_NEXT_LEVEL)
				mHandler.sendEmptyMessageDelayed(
						GameActivity.MSG_NEXTLEVEL_ANIM_START, timeSpan
								* iCount + 500);
			if (GameActivity.iGameState == Constant.GAME_OVER)
				mHandler.sendEmptyMessageDelayed(
						GameActivity.MSG_SHOW_FUHUO_DIALOG, timeSpan * iCount
								+ 500);
		}
		return imageView;
	}

	private void initAnim() {
		// TODO Auto-generated method stub
		translateAnimation = new TranslateAnimation(0, 0, 0, -10.0f
				* dm.density);
		translateAnimation.setDuration(1);

	}

	public void setData(ArrayList<Item> data) {
		this.data = data;
		iCount = 0;
		mFlag = true;
	}

}
