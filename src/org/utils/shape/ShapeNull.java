package org.utils.shape;

import java.awt.Graphics;

import org.utils.transform.Projection;

public class ShapeNull extends Geometry {

	@Override
	public void globe(int M, int N) {

	}

	@Override
	double plotX(double u, double v) {
		return 0;
	}

	@Override
	double plotY(double u, double v) {
		return 0;
	}

	@Override
	double plotZ(double u, double v) {
		return 0;
	}

	@Override
	public void drawShape(Graphics g, Projection p) {
	}

}
