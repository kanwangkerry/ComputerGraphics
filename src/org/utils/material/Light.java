package org.utils.material;

public class Light {
	double[] lDir = new double[3];
	public double[] lColor = new double[3];
	
	public void setLightDir(double x, double y, double z){
		lDir[0] = x;
		lDir[1] = y;
		lDir[2] = z;
	}
	
	public void setLightColor(double x, double y, double z){
		lColor[0] = x;
		lColor[1] = y;
		lColor[2] = z;
	}
	
	public void calcReflection(double normal[], double result[]){
		double temp = 2 * (normal[0]*lDir[0]+normal[1]*lDir[1]+normal[2]*lDir[2]);
		result[0] = temp*normal[0] - lDir[0];
		result[1] = temp*normal[1] - lDir[1];
		result[2] = temp*normal[2] - lDir[2];
	}
	
	public double calcProduct(double normal[]){
		return lDir[0] * normal[0] + lDir[1] * normal[1] + lDir[2] * normal[2];
	}

}
