package com.haogame.popet.surfaceview;

public class Particle
{
	double vertical_v;
	double horizontal_v;
	int startX;
	int startY;
	int x;
	int y;
	double startTime;
	double time = 0;
	int chipId;
	
	public Particle( double vertical_v , double horizontal_v ,
			int x , int y , double startTime , int chipId )
	{
		
		this.vertical_v = vertical_v; // 离子竖直方向速度（向下为正）
		this.horizontal_v = horizontal_v; // 离子水平方向速度（向右为正）
		this.startX = x;
		this.startY = y;
		this.x = x;
		this.y = y;
		this.startTime = startTime;
		this.chipId = chipId;
	}
	
}
