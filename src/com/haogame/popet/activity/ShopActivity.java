package com.haogame.popet.activity;

import java.util.HashMap;

import org.hj20150901.lib.HejuHuafeiCallback;
import org.hj20150901.lib.HejuInit;
import org.hj20150901.lib.HejuInstance;
import org.json.JSONException;
import org.json.JSONObject;

import com.haogame.popet.Constant;
import com.haogame.popet.PayUtil.PayResultListener;
import com.haogame.popet.PopETApp;
import com.haogame.popet.SoundManager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haogame.popet.R;
import com.haogame.popet.PayUtil;

public class ShopActivity extends Activity {
	private ListView shopList;

	private PopETApp app;

	protected static final int MSG_PAY_SUCCESS = 1;
	protected static final int MSG_PAY_FAILED = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// EMPayManager.choose(this);
		// EMTotalPayManager.init(this);
		setContentView(R.layout.shop_activity);
		app = (PopETApp) getApplicationContext();
		initData();
		initView();
		HejuInit mHejuInit = new HejuInit(this);
		mHejuInit.start();
	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	private void initView() {
		// TODO Auto-generated method stub
		shopList = (ListView) findViewById(R.id.lv_shop);
		shopList.setAdapter(new ShopAdapter());
	}

	private OnClickListener onClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SoundManager.getInstance(ShopActivity.this).play(
					SoundManager.SOUND_BTN_START);
			PayUtil.payWithProductIndex(ShopActivity.this,
					(Integer) v.getTag(), new PayResultListener() {

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
	};

	private class ShopAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(ShopActivity.this).inflate(
						R.layout.shop_item, null);
				holder.tvNum = (TextView) convertView.findViewById(R.id.tv_num);
				holder.tvPrice = (TextView) convertView
						.findViewById(R.id.tv_price);
				holder.btnBuy = (Button) convertView.findViewById(R.id.btn_buy);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.btnBuy.setTag(position);
			holder.btnBuy.setOnClickListener(onClick);
			holder.tvNum.setText("x" + PayUtil.PRO_COUNT[position]);
			holder.tvPrice.setText("￥" + PayUtil.PRO_PRICE[position]);
			return convertView;
		}

	}

	class ViewHolder {
		TextView tvNum;
		TextView tvPrice;
		Button btnBuy;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// EMTotalPayManager.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// EMTotalPayManager.onPause(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// EMTotalPayManager.onDestroy(this);
	}

}
