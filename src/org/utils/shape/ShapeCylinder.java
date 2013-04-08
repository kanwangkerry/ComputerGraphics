package org.utils.shape;

import java.awt.Graphics;

import org.utils.transform.Projection;

public class ShapeCylinder extends Geometry {

	@Override
	// only M is used in cylinder;
	public void globe(int M, int N) {
		double u;
		vertices = new double[M*2+M*2+2][6];
		faces = new int[M*3][4];
		vertices[M*4] = new double[] {0, 0, 1, 0, 0, 1};
		vertices[M*4+1] = new double[] {0, 0, -1, 0, 0, -1};
		for(int i = 0 ; i < M ; i++){
			u = i/(double) M;
			vertices[i*2] = new double[] {plotX(u, 1), plotY(u, 1), 1, plotX(u, 1), plotY(u, 1), 1};
			vertices[i*2+1] = new double[] {plotX(u, 1), plotY(u, 1), -1, plotX(u, 1), plotY(u, 1), -1};
			faces[i] = new int[] {2*i, 2*i+1, 2*(i+1)+1, 2*(i+1)};
			
			vertices[M*2+i*2] = new double[] {plotX(u, 1), plotY(u, 1), 1, plotX(u, 1), plotY(u, 1), 1};
			vertices[M*2+i*2+1] = new double[] {plotX(u, 1), plotY(u, 1), -1, plotX(u, 1), plotY(u, 1), 1};
			faces[M+i] = new int[] {M*4, M*4, M*2+i*2, M*2+(i+1)*2};
			faces[2*M+i] = new int[] {M*4+1, M*4+1, M*2+i*2+1, M*2+(i+1)*2+1};
		}
		this.m.identity();
	}

	@Override
	double plotX(double u, double v) {
		return Math.cos(u*2*Math.PI);
	}

	@Override
	double plotY(double u, double v) {
		return Math.sin(u*2*Math.PI);
	}

	@Override
	double plotZ(double u, double v) {
		return 0;
	}

	public void drawShape(Graphics g, Projection p) {
		for (int i = 0; i < faces.length; i++) {
			drawEdge(vertices[faces[i][0]], vertices[faces[i][1]], g, p);
			drawEdge(vertices[faces[i][1]], vertices[faces[i][2]], g, p);
			drawEdge(vertices[faces[i][2]], vertices[faces[i][3]], g, p);
			drawEdge(vertices[faces[i][0]], vertices[faces[i][3]], g, p);
		}
	}

}
