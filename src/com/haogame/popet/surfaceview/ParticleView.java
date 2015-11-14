package com.haogame.popet.surfaceview;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet; 
import android.view.View;


public class ParticleView extends View
{
	public DrawThread dt;
	public ParticleSet ps;
	
	Bitmap bmp;
	private Paint mPaint;
	
	public ParticleView( Context context , AttributeSet attr )
	{
		super(context);
		dt = new DrawThread(this);
		ps = new ParticleSet();
		mPaint = new Paint();
		setId(100);
		setBackgroundColor(Color.TRANSPARENT);
		startThread();
		
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		ArrayList<Particle> particleSet = ps.particleSet;
		for (int i = 0; i < particleSet.size(); i++)
		{
			Particle p = particleSet.get(i);
			int tempX = p.x; // 获得粒子X坐标
			int tempY = p.y; // 获得粒子Y坐标
			int ibmpId = p.chipId;			 
			bmp = ((BitmapDrawable) this.getResources().getDrawable(ibmpId)).getBitmap();
			canvas.drawBitmap(bmp, tempX, tempY, mPaint);
		}
		

		int count = ps.particleSet.size();
		for (int i = 0; i < count; i++)
		{
			Particle particle = ps.particleSet.get(i);
			double timeSpan = particle.time - particle.startTime;
			int tempx = (int) (particle.startX + particle.horizontal_v
					* timeSpan);
			int tempy = (int) (particle.startY + 4.9 * timeSpan * timeSpan + particle.vertical_v
					* timeSpan);
			if (tempy > 1280)
			{ // 300
				ps.particleSet.remove(particle);
				count = ps.particleSet.size();
				
			}
			particle.x = tempx;
			particle.y = tempy;
			particle.time += 1;
		}
		
	}
	
	
	private void startThread()
	{
		if (!dt.isAlive())
		{
			dt.start();
		}
	}
	
}
