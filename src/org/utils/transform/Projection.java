package org.utils.transform;

public class Projection {
	int w=480, h=640;
	double FL=10.0;
	
	public Projection() {
	}
	
	public Projection(int width, int height, double fl)
	{
		this.w = width;
		this.h = height;
		this.FL = fl;		
	}
	
	/**
	 * Transform the vertice into a 2D point.
	 * @param xyz
	 * @param pxy
	 */
	public void projectPoint(double[] xyz, int[] pxy) {
		double x = xyz[0];
		double y = xyz[1];
		double z = xyz[2];

		pxy[0] = w / 2 + (int) (h * x / (FL - z));
		pxy[1] = h / 2 - (int) (h * y / (FL - z));
	}
	
	/**
	 * Get the z index of the vertice.
	 * @param xyz
	 * @return
	 */
	public double getZIndex(double[] xyz){
		double z = xyz[2];
		return FL * z /(FL -z);
	}

}
