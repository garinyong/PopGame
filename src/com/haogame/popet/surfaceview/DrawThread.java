package com.haogame.popet.surfaceview;


//继承自Thread的子类，负责刷新屏幕
public class DrawThread extends Thread
{
	ParticleView pv; // ParticleView对象引用
	public boolean flag; // 线程执行标志位
	int sleepSpan = 50; // 睡眠时间
	double span = 1;
	
	public DrawThread( ParticleView pv )
	{
		this.pv = pv;
		this.flag = true; // 设置线程执行标志位为true
	}
	
	// 线程执行方法
	public void run()
	{
		while (flag)
		{
			
			long lBeforeDraw = System.currentTimeMillis();
			// pv.onDraw(canvas);
			pv.postInvalidate();
			long iDuration = System.currentTimeMillis() - lBeforeDraw;
			

			try
			{
				if (iDuration < sleepSpan) Thread.sleep(sleepSpan - iDuration);
			}
			catch (Exception e)
			{
				e.printStackTrace(); // 捕获并打印异常
			}
			
		}
	}
	

}
