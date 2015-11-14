package com.haogame.popet.activity;

import java.util.ArrayList;

import com.haogame.popet.PopETApp;

import android.os.Bundle;
import cn.easymobi.reward.OnGetRewardListener;
import cn.easymobi.reward.RewardActivity;
import cn.easymobi.reward.RewardItem;

public class GetToolsActivity extends RewardActivity
{
	
	private PopETApp app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		app = (PopETApp) getApplicationContext();
		setOnGetRewardListener(mReward);
		
	}
	
	private OnGetRewardListener mReward = new OnGetRewardListener()
	{
		
		@Override
		public void onGetReward(ArrayList<RewardItem> arg0)
		{
			// TODO Auto-generated method stub
			for (RewardItem item : arg0)
			{
				app.saveChangeColorNum(app.getChangeColorNum() + item.getCount());
				 
			}
			

		}
	};
	
	 

}
