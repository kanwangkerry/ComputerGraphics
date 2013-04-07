package org.utils.render;

import java.util.ArrayList;

public class Triangle {
	int point[][] = new int[3][5];
	int area;
	private int yt, yb, xlt, xrt, xlb, xrb;

	Triangle(int point1[], int point2[], int point3[], double zIndex1,
			double zIndex2, double zIndex3) {
		System.arraycopy(point1, 0, point[0], 0, 5);
		System.arraycopy(point2, 0, point[1], 0, 5);
		System.arraycopy(point3, 0, point[2], 0, 5);
		int temp[] = new int[5];
		if (point[0][1] > point[1][1]) {
			temp[0] = point[0][0];
			temp[1] = point[0][1];
			point[0][0] = point[1][0];
			point[0][1] = point[1][1];
			point[1][0] = temp[0];
			point[1][1] = temp[1];
		}
		if (point[1][1] > point[2][1]) {
			temp[0] = point[1][0];
			temp[1] = point[1][1];
			point[1][0] = point[2][0];
			point[1][1] = point[2][1];
			point[2][0] = temp[0];
			point[2][1] = temp[1];
		}
		if (point[0][1] > point[1][1]) {
			temp[0] = point[0][0];
			temp[1] = point[0][1];
			point[0][0] = point[1][0];
			point[0][1] = point[1][1];
			point[1][0] = temp[0];
			point[1][1] = temp[1];
		}
		this.area = this.area();
		this.updatePoints();
	}

	static public ArrayList<Triangle> splitTriangle(Triangle src) {
		ArrayList<Triangle> result = new ArrayList<Triangle>();
		if (src.point[1][1] == src.point[0][1]
				|| src.point[1][1] == src.point[2][1])
			result.add(src);
		else {
			Triangle temp;
			int newPoint[] = new int[2];
			double k = (src.point[0][0] - src.point[2][0])
					/ ((double) src.point[0][1] - src.point[2][1]);
			newPoint[1] = src.point[1][1];
			newPoint[0] = (int) ((src.point[1][1] - src.point[2][1]) * k + src.point[2][0]);
			temp = new Triangle(src.point[0], src.point[1], newPoint);
			result.add(temp);
			temp = new Triangle(src.point[2], src.point[1], newPoint);
			result.add(temp);
		}
		return result;
	}

	private int area() {
		double area = 0;
		area = (point[0][0] - point[1][0]) * (point[0][1] + point[1][1])
				/ ((double) 2);
		area += (point[0][0] - point[2][0]) * (point[0][1] + point[2][1])
				/ ((double) 2);
		area += (point[2][0] - point[1][0]) * (point[2][1] + point[1][1])
				/ ((double) 2);

		return (int) area;
	}

	private void updatePoints() {
		if (point[0][1] < point[1][1]) {
			this.yt = point[0][1];
			this.yb = point[1][1];
			this.xrt = point[0][0];
			this.xlt = point[0][0];
			this.xrb = (point[1][0] > point[2][0] ? point[1][0] : point[2][0]);
			this.xlb = (point[1][0] < point[2][0] ? point[1][0] : point[2][0]);
		} else {
			this.yb = point[2][1];
			this.yt = point[1][1];
			this.xrb = point[2][0];
			this.xlb = point[2][0];
			this.xrt = (point[1][0] > point[0][0] ? point[1][0] : point[0][0]);
			this.xlt = (point[1][0] < point[0][0] ? point[1][0] : point[0][0]);
		}
	}

	private int getXL(int y) {
		double t = (y - yt) / ((double) yb - yt);
		return (int) (xlt + t * (xlb - xlt));
	}

	private int getXR(int y) {
		double t = (y - yt) / ((double) yb - yt);
		return (int) (xrt + t * (xrb - xrt));
	}

	public boolean isInTriangle(int x, int y) {
		if (y > yb || y < yt)
			return false;
		int rx = this.getXR(y);
		int lx = this.getXL(y);
		if (x >= lx && x <= rx) {
			return true;
		} else
			return false;
	}

	public void renderTriangle(int pix[], int color, int W) {
		for (int y = yt; y <= yb; y++) {
			int rx = this.getXR(y);
			int rl = this.getXL(y);
			for (int x = rl; x <= rx; x++) {
				pix[y * W + x] = color;
			}
		}
	}

}
