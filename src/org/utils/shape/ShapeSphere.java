package org.utils.shape;

import java.awt.Graphics;

import org.utils.transform.Projection;

public class ShapeSphere extends Geometry {

	@Override
	public void globe(int M, int N) {
		this.faces = new int[M * N][4];
		this.vertices = new double[(M + 1) * (N + 1)][6];
		double v0, v1, u0, u1;
		for (int n = 0; n < N; n++) {
			for (int m = 0; m < M; m++) {
				v0 = ((double) n) / N;
				v1 = ((double) (n + 1)) / N;
				u0 = ((double) m) / M;
				u1 = ((double) (m + 1)) / M;

				vertices[m + n * (M + 1)] = new double[] { plotX(u0, v0),
						plotY(u0, v0), plotZ(u0, v0), plotX(u0, v0),
						plotY(u0, v0), plotZ(u0, v0) };
				vertices[m + 1 + n * (M + 1)] = new double[] { plotX(u1, v0),
						plotY(u1, v0), plotZ(u1, v0), plotX(u1, v0),
						plotY(u1, v0), plotZ(u1, v0) };
				vertices[m + (n + 1) * (M + 1)] = new double[] { plotX(u0, v1),
						plotY(u0, v1), plotZ(u0, v1), plotX(u0, v1),
						plotY(u0, v1), plotZ(u0, v1) };
				vertices[m + 1 + (n + 1) * (M + 1)] = new double[] {
						plotX(u1, v1), plotY(u1, v1), plotZ(u1, v1),
						plotX(u1, v1), plotY(u1, v1), plotZ(u1, v1) };

				faces[m + M * n] = new int[] { m + n * (M + 1),
						m + 1 + n * (M + 1), m + 1 + (n + 1) * (M + 1),
						m + (n + 1) * (M + 1) };
			}
		}

		this.m.identity();
	}

	@Override
	public double plotX(double u, double v) {
		return Math.cos(2 * Math.PI * u) * Math.sin(Math.PI * v);
	}

	@Override
	public double plotY(double u, double v) {
		return Math.sin(2 * Math.PI * u) * Math.sin(Math.PI * v);
	}

	@Override
	public double plotZ(double u, double v) {
		return -Math.cos(Math.PI * v);
	}

	@Override
	public void drawShape(Graphics g, Projection p) {
		for (int i = 0; i < faces.length; i++) {
			drawEdge(vertices[faces[i][0]], vertices[faces[i][1]], g, p);
			drawEdge(vertices[faces[i][0]], vertices[faces[i][3]], g, p);
		}
	}
}
