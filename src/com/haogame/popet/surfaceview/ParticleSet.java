package com.haogame.popet.surfaceview;

import java.util.ArrayList;
public class ParticleSet
{
	public ArrayList<Particle> particleSet;
	
	public ParticleSet()
	{
		particleSet = new ArrayList<Particle>();
	}
	
	public void add(int count, double startTime, int tempX, int tempY, int chipId)
	{
		
		for (int i = 0; i < count; i++)
		{
			
			double tempv_v = -80 + 30 * (Math.random());
			double tempv_h = -40 + 80 * (Math.random());
			Particle particle = new Particle(tempv_v,
					tempv_h, tempX, tempY, startTime, chipId);
			particleSet.add(particle);
			
		}
	}
	

}
