package org.hw4;

import java.awt.Color;
import java.awt.Graphics;

import org.utils.BufferedApplet;
import org.utils.transform.Projection;

@SuppressWarnings("serial")
public class SimplePersone extends BufferedApplet {

	int w = 0, h = 0;
	

	double speedCirclePerSecond = 0.5;
	int framePerSec = 24;
	int frameCount = 0;
	double alpha;
	
	int timeInterval = 1000;
	long lastTime = 0;
	int caseAnimate = 0;
	
	Projection proj = new Projection();
	Person p;
	
	@Override
	public void render(Graphics g) {
		if (w == 0) {
			w = getWidth();
			h = getHeight();
			proj = new Projection(w, h, 15.0);
			p = new Person();
			alpha = 0;
			lastTime = System.currentTimeMillis();
		}
		
		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.black);
//		Geometry cube = new ShapeCylinder();
//		cube.globe(20, 20);
//		//cube.getMatrix().scale(3, 3, 3);
//		cube.getMatrix().rotateX(-1);
//
//		cube.drawShape(g, proj);

		
		
		long ctime = System.currentTimeMillis();
		if(ctime - lastTime > timeInterval){
			lastTime = ctime;
			caseAnimate = (caseAnimate+1)%7;
		}
		
		alpha = speedCirclePerSecond*Math.PI*((frameCount)/(double)framePerSec);
		frameCount = (frameCount+1+framePerSec*2+1)%(framePerSec*2+1);

		p.move(frameCount/(double)framePerSec);
		p.drawPerson(g, proj);


	}

}
