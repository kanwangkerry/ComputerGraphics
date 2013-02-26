package org.hw3;

import java.awt.Color;
import java.awt.Graphics;

import org.utils.BufferedApplet;
import org.utils.shape3d.Matrix;
import org.utils.shape3d.Projection;

@SuppressWarnings("serial")
public class Simple3D extends BufferedApplet {

	int w = 0, h = 0;
	
	double speedCirclePerSecond = 0.5;
	int framePerSec = 24;
	int frameCount = 0;
	double alpha;
	
	int timeInterval = 1000;
	long lastTime = 0;
	int caseAnimate = 0;
	
	double[][] vertices = { { -1, -1, -1 }, { 1, -1, -1 }, { -1, 1, -1 },
			{ 1, 1, -1 }, { -1, -1, 1 }, { 1, -1, 1 }, { -1, 1, 1 },
			{ 1, 1, 1 }, };

	int[][] edges = { { 0, 1 }, { 2, 3 }, { 4, 5 }, { 6, 7 }, // EDGES IN X
																// DIRECTION
			{ 0, 2 }, { 1, 3 }, { 4, 6 }, { 5, 7 }, // EDGES IN Y DIRECTION
			{ 0, 4 }, { 1, 5 }, { 2, 6 }, { 3, 7 }, // EDGES IN Z DIRECTION
	};
	Projection proj = new Projection();

	Matrix matrix = new Matrix();

	double[] point0 = new double[3];
	double[] point1 = new double[3];

	int[] a = new int[2];
	int[] b = new int[2];

	public void render(Graphics g) {
		if (w == 0) {
			w = getWidth();
			h = getHeight();
			proj = new Projection(w, h, 5.0);
			alpha = 0;
			lastTime = System.currentTimeMillis();
		}

		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.black);

		matrix.identity();

		long ctime = System.currentTimeMillis();
		if(ctime - lastTime > timeInterval){
			lastTime = ctime;
			caseAnimate = (caseAnimate+1)%7;
		}
		
		alpha = speedCirclePerSecond*Math.PI*((frameCount)/(double)framePerSec);
		frameCount = (frameCount+1+framePerSec*2+1)%(framePerSec*2+1);
		
		switch(caseAnimate){
		case 0:matrix.rotateY(alpha);break;
		case 1:matrix.rotateX(alpha);break;
		case 2:matrix.rotateZ(alpha);break;
		case 3:matrix.translate((double)frameCount/framePerSec, 0, 0);break;
		case 4:matrix.translate(0, (double)frameCount/framePerSec, 0);break;
		case 5:matrix.translate(0, 0, (double)frameCount/framePerSec);break;
		case 6:matrix.scale((double)frameCount/framePerSec, (double)frameCount/framePerSec, (double)frameCount/framePerSec);break;
		}
		

		for (int e = 0; e < edges.length; e++) {
			int i = edges[e][0];
			int j = edges[e][1];

			matrix.transform(vertices[i], point0);
			matrix.transform(vertices[j], point1);

			proj.projectPoint(point0, a);
			proj.projectPoint(point1, b);

			g.drawLine(a[0], a[1], b[0], b[1]);
		}
	}
}
