package org.example;

import org.utils.MISApplet;

@SuppressWarnings("serial")
public class MyMISApplet extends MISApplet {

	// ----- THESE TWO METHODS OVERRIDE METHODS IN THE BASE CLASS

	double t = 0;

	public void initFrame(double time) {

		t = 3 * time;
	}

	public void setPixel(int x, int y, int rgb[]) {

		double fx = ((double) x - W / 2) / W;
		double fy = ((double) y - H / 2) / H;
		for (int j = 0; j < 3; j++)
			rgb[j] = (int) (128 + 128
					* Math.sin(30 * fx + (3 - j)
							* ImprovedNoise.noise(8 * fx, 8 * fy, t))
					* Math.sin(30 * fy + (2 + j)
							* ImprovedNoise.noise(4 * fx, 4 * fy, t)));
	}
}
