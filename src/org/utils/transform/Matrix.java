package org.utils.transform;

public class Matrix implements IMatrix {

	private static Matrix tr = new Matrix();
	private static Matrix ro = new Matrix();
	private static Matrix sc = new Matrix();
	private static Matrix result = new Matrix();

	private double[][] matrix = new double[4][4];

	@Override
	public void identity() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				matrix[i][j] = (i == j ? 1 : 0);
			}
		}
	}

	@Override
	public void set(int col, int row, double value) {
		matrix[row][col] = value;
	}

	@Override
	public double get(int col, int row) {
		return matrix[row][col];
	}

	@Override
	synchronized public void translate(double x, double y, double z) {

		tr.identity();
		tr.set(3, 0, x);
		tr.set(3, 1, y);
		tr.set(3, 2, z);
		this.rightMultiply(tr);
	}

	@Override
	synchronized public void rotateX(double radians) {

		ro.identity();
		ro.set(1, 1, Math.cos(radians));
		ro.set(2, 1, -Math.sin(radians));
		ro.set(1, 2, Math.sin(radians));
		ro.set(2, 2, Math.cos(radians));
		this.rightMultiply(ro);
	}

	@Override
	synchronized public void rotateY(double radians) {
		ro.identity();
		ro.set(0, 0, Math.cos(radians));
		ro.set(2, 0, -Math.sin(radians));
		ro.set(0, 2, Math.sin(radians));
		ro.set(2, 2, Math.cos(radians));
		this.rightMultiply(ro);
	}

	@Override
	synchronized public void rotateZ(double radians) {
		ro.identity();
		ro.set(0, 0, Math.cos(radians));
		ro.set(1, 0, -Math.sin(radians));
		ro.set(0, 1, Math.sin(radians));
		ro.set(1, 1, Math.cos(radians));
		this.rightMultiply(ro);
	}

	@Override
	synchronized public void scale(double x, double y, double z) {

		sc.identity();
		sc.set(0, 0, x);
		sc.set(1, 1, y);
		sc.set(2, 2, z);
		this.rightMultiply(sc);
	}

	@Override
	synchronized public void leftMultiply(IMatrix other) {
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				double temp = 0;
				for (int k = 0; k < 4; k++) {
					temp += other.get(k, i) * this.get(j, k);
				}
				result.set(j, i, temp);
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.set(i, j, result.get(i, j));
			}
		}

	}

	@Override
	synchronized public void rightMultiply(IMatrix other) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				double temp = 0;
				for (int k = 0; k < 4; k++) {
					temp += this.get(k, i) * other.get(j, k);
				}
				result.set(j, i, temp);
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.set(i, j, result.get(i, j));
			}
		}

	}

	@Override
	public void transform(double[] src, double[] dst) {
		double[] tempS = new double[4];
		double[] tempD = new double[4];
		for (int i = 0; i < 3; i++) {
			tempS[i] = src[i];
		}
		tempS[3] = 1;
		for (int i = 0; i < 4; i++) {
			double temp = 0;
			for (int j = 0; j < 4; j++) {
				temp += this.get(j, i) * tempS[j];
			}
			tempD[i] = temp;
		}
		for (int i = 0; i < 3; i++) {
			dst[i] = tempD[i] / tempD[3];
		}
	}
}