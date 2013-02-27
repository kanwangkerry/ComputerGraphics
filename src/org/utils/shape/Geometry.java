package org.utils.shape;

import java.awt.Graphics;

import org.utils.transform.Matrix;
import org.utils.transform.Projection;

public abstract class Geometry {
	double vertices[][];
	int faces[][];
	Matrix m = new Matrix();
	
	private double dst1[] = new double[3];
	private double dst2[] = new double[3];
	private int end1[] = new int[2];
	private int end2[] = new int[2];
	
	public abstract void globe(int M, int N);
	//v is in the latitude, u is in the longitude 
	abstract double plotX(double u, double v);
	abstract double plotY(double u, double v);
	abstract double plotZ(double u, double v);
	
	public Matrix getMatrix(){
		return m;
	}
	
	public abstract void drawShape(Graphics g, Projection p);
	
	
	void drawEdge(double[] point1, double[] point2, Graphics g, Projection proj){
		m.transform(point1, dst1);
		m.transform(point2, dst2);
		
		proj.projectPoint(dst1, end1);
		proj.projectPoint(dst2, end2);
		g.drawLine(end1[0], end1[1], end2[0], end2[1]);		
	}
}
