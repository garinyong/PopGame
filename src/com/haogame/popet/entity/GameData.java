
package com.haogame.popet.entity;

import java.util.ArrayList;
import java.util.Random;

import com.haogame.popet.Constant;
import com.haogame.popet.activity.GameActivity;

import android.util.Log;
import com.haogame.popet.R;

public class GameData
{

	private ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Integer> arrImgId = new ArrayList<Integer>();
	public final static int rowNum = 8;
	public final static int colNum = 7;
	public int level;
	private int imgNum;
	public static int iAccounts;
	public static int SCORE;
	public static int targetScore;
	public static int endAcounts;
	private int lastScore;
	public int iRemainCount;

	private final int[] levleImgNum = new int[] { 3, 4, 5, 6 };
	public final static int[] targetScoreArray = new int[] { 7000, 11000,
	        14500, 17000 };

	private int[] chipIds = new int[] { R.drawable.chip1, R.drawable.chip2,
	        R.drawable.chip3, R.drawable.chip4, R.drawable.chip5,
	        R.drawable.chip6, };

	public int[] imgIds = new int[6];
	private int[][] allImgId = new int[][] {
	        { R.drawable.cartoon1, R.drawable.cartoon7 },
	        { R.drawable.cartoon2, R.drawable.cartoon8 },
	        { R.drawable.cartoon3, R.drawable.cartoon9 },
	        { R.drawable.cartoon4, R.drawable.cartoon10 },
	        { R.drawable.cartoon5, R.drawable.cartoon11 },
	        { R.drawable.cartoon6, R.drawable.cartoon12 } };

	private void initImgIds()
	{

		Random r = new Random();

		for (int i = 0; i < imgIds.length; i++)
		{
			imgIds[i] = allImgId[i][r.nextInt(2)];

		}

	}

	public GameData(int level  )
	{

		this.level = level;
		
		if (this.level > 3)
		{
			this.imgNum = 6;
			targetScore = targetScore + 2000 + (level - 3) * 200;
		}
		else
		{
			this.imgNum = levleImgNum[level];
			targetScore = targetScoreArray[level];
		}
		initImgIds();
		initItems();
	}

	private void initItems()
	{

		// TODO Auto-generated method stub
		Random random = new Random();
		arrImgId.clear();
		for (int i = 0; i < 56; i++)
		{
			int rdNum = random.nextInt(imgNum);

			if (!arrImgId.contains(rdNum))
				arrImgId.add(rdNum);
			Item item = new Item();
			item.toX = 0;
			item.toY = 0;
			item.imgId = imgIds[rdNum];
			item.chipId = chipIds[rdNum];
			item.state = Constant.ITEM_STATE_DEFAULT;
			item.checked = false;
			items.add(item);

		}

	}

	public void refreshData(int position)
	{

		Log.v("TagEm", "----------------> refreshData");
		updateItemState(position);
		check(position / colNum, position % colNum);
		if (GameActivity.iGameState == Constant.GAME_FALLING && iAccounts > 1)
		{
			// 计算item.toY
			getItemToY();
			// 计算Item.toX
			getItemToX();
			// 得分
			if (items.get(position).imgId != R.drawable.kongbai)
			{
				if (iAccounts > 1)
				{
					lastScore = SCORE;
					SCORE += Math.pow(iAccounts, 2) * 20;
				}
			}

		}

	}

	public void initItemState()
	{
		 
		for(Item item :items)
		{
			item.toX = 0;
			item.toY = 0;
			item.state = Constant.ITEM_STATE_DEFAULT;
			item.checked = false;
			
		}
	}
	 
	public void updateItemState(int position)
	{

		iAccounts = 0;
		GameActivity.iGameState = Constant.GAME_DEFALT;
		for (int i = rowNum - 1; i > -1; i--)
		{
			for (int j = colNum - 1; j > -1; j--)
			{
				Item preItem = items.get(i * colNum + j);
				if (preItem.state == Constant.ITEM_STATE_FALL_DOWN
				        || preItem.state == Constant.ITEM_STATE_SELECTED_SECEND)
				{
					for (int k = i - 1; k > -1; k--)
					{
						Item item = items.get(k * colNum + j);
						if ((k + item.toY) == i)
						{
							preItem.imgId = item.imgId;
							item.imgId = R.drawable.kongbai;
							preItem.chipId = item.chipId;
							item.chipId = R.drawable.kongbai;
							preItem.state = Constant.ITEM_STATE_DEFAULT;
							break;
						}
					}
				}

			}
		}

		for (int i = 0; i < rowNum; i++)
		{
			for (int j = 0; j < colNum; j++)
			{
				Item item = items.get(i * colNum + j);
				int toX = item.toX;
				if (toX < 0)
				{
					int m = j + toX;
					if (m >= 0)
					{
						items.get(i * colNum + m).imgId = items.get(i * colNum
						        + j).imgId;
						items.get(i * colNum + j).imgId = R.drawable.kongbai;
						items.get(i * colNum + m).chipId = items.get(i * colNum
						        + j).chipId;
						items.get(i * colNum + j).chipId = R.drawable.kongbai;
					}
				}
				item.checked = false;
				item.toX = 0;
				item.toY = 0;
				if (items.get(position).state == Constant.ITEM_STATE_DEFAULT)
					item.state = Constant.ITEM_STATE_DEFAULT;

			}
		}

	}

	private void check(int i, int j)
	{

		// TODO Auto-generated method stub
		if (items.get(i * colNum + j).checked)
			return;
		items.get(i * colNum + j).checked = true;
		if (items.get(i * colNum + j).state == Constant.ITEM_STATE_DEFAULT)
		{
			items.get(i * colNum + j).state = Constant.ITEM_STATE_SELECTED;
			GameActivity.iGameState = Constant.GAME_SELECT;
		}
		else if (items.get(i * colNum + j).state == Constant.ITEM_STATE_SELECTED)
		{
			items.get(i * colNum + j).state = Constant.ITEM_STATE_SELECTED_SECEND;
			GameActivity.iGameState = Constant.GAME_FALLING;

		}
		if (j > 0
		        && items.get(i * colNum + (j - 1)).imgId == items.get(i
		                * colNum + j).imgId)
		{
			check(i, j - 1);
		}
		if (i > 0
		        && items.get((i - 1) * colNum + j).imgId == items.get(i
		                * colNum + j).imgId)
		{
			check(i - 1, j);
		}
		if (j < colNum - 1
		        && items.get(i * colNum + (j + 1)).imgId == items.get(i
		                * colNum + j).imgId)
		{
			check(i, j + 1);
		}
		if (i < rowNum - 1
		        && items.get((i + 1) * colNum + j).imgId == items.get(i
		                * colNum + j).imgId)
		{
			check(i + 1, j);
		}
		iAccounts++;

	}

	// 计算item.toY
	private void getItemToY()
	{

		for (int m = rowNum - 1; m >= 0; m--)
		{
			for (int n = colNum - 1; n >= 0; n--)
			{
				if (items.get(m * colNum + n).checked)
				{
					for (int p = m - 1; p >= 0; p--)
					{
						if (!items.get(p * colNum + n).checked)
						{
							items.get(p * colNum + n).checked = true;
							int iDistance = m - p;
							if (iDistance > 0)
							{
								Item item = items.get(p * colNum + n);
								item.state = Constant.ITEM_STATE_FALL_DOWN;
								item.toY = m - p;
							}
							break;
						}
					}

				}

			}
		}

	}

	private void getItemToX()
	{

		// TODO Auto-generated method stub

		int tempCount = 0; // 计算空白的列数
		for (int col = 0; col < colNum; col++)
		{
			for (int i = rowNum - 1; i > 0; i--)
			{
				int state = items.get(i * colNum + col).state;
				int imgId = items.get(i * colNum + col).imgId;
				if (state != Constant.ITEM_STATE_SELECTED_SECEND
				        && imgId != R.drawable.kongbai)
				{
					break;
				}

				else
				{
					tempCount++;

				}

			}

			// 从空白列的右边一列依次计算item.toX
			if (tempCount > 0)
			{
				for (int n = col + 1; n < colNum; n++)
				{
					for (int m = rowNum - 1; m > -1; m--)
					{
						items.get(m * colNum + n).toX = (-tempCount / colNum);
					}

				}
			}

		}
	}

	public ArrayList<Item> getItems()
	{

		return items;
	}

	public void endLevle()
	{

		iRemainCount = 0;
		for (int i = 0; i < rowNum; i++)
		{
			for (int j = 0; j < colNum; j++)
			{
				if (items.get(i * colNum + j).imgId == R.drawable.kongbai)
					continue;
				else
				{
					iRemainCount++;
					if (j > 0
					        && items.get(i * colNum + (j - 1)).imgId == items
					                .get(i * colNum + j).imgId)
					{

						return;
					}
					else if (i > 0
					        && items.get((i - 1) * colNum + j).imgId == items
					                .get(i * colNum + j).imgId)
					{

						return;
					}
					else if (j < colNum - 1
					        && items.get(i * colNum + (j + 1)).imgId == items
					                .get(i * colNum + j).imgId)
					{

						return;
					}
					else if (i < rowNum - 1
					        && items.get((i + 1) * colNum + j).imgId == items
					                .get(i * colNum + j).imgId)
					{

						return;
					}

				}

			}
		}
		switch (iRemainCount)
		{
			case 0:
				SCORE += 1000;
				break;
			case 1:
				SCORE += 500;
				break;
			case 2:
				SCORE += 300;
				break;
			case 3:
				SCORE += 100;
				break;
		}
		if (SCORE >= targetScore)
		{
			GameActivity.iGameState = Constant.GAME_NEXT_LEVEL;
			return;
		}
		else
		{
			GameActivity.iGameState = Constant.GAME_OVER;
			return;
		}

	}

	public void winLevel()
	{

		if (lastScore < targetScore && SCORE >= targetScore)
		{
			GameActivity.iGameState = Constant.GAME_REACH_TARGETSCORE;
		}
	}

	public void preNextLevel()
	{

		for (int i = 0; i < GameData.rowNum; i++)
		{
			for (int j = 0; j < GameData.colNum; j++)
			{
				Item item = items.get(i * GameData.colNum + j);
				if (item.imgId != R.drawable.kongbai)
				{
					endAcounts++;
					iAccounts++;
					item.state = Constant.ITEM_STATE_SELECTED_SECEND;
				}

			}

		}
		if (iAccounts == 1)
			iAccounts++;
	}

}
