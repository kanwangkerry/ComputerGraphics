package org.utils.material;

/**
 * Class of Light. Should contain color and direction of light.
 * @author kerry
 *
 */
public class Light {
	double[] lDir = new double[3];
	public double[] lColor = new double[3];
	
	public void setLightDir(double x, double y, double z){
		double normal = Math.sqrt(x*x+y*y+z*z);
		lDir[0] = x/normal;
		lDir[1] = y/normal;
		lDir[2] = z/normal;
	}
	
	public void setLightColor(double x, double y, double z){
		lColor[0] = x;
		lColor[1] = y;
		lColor[2] = z;
	}
	
	/**
	 * Get the reflection of the light. normal should be the normal of the surface, and 
	 * result should save the result of the reflection
	 * @param normal
	 * @param result
	 */
	public void calcReflection(double normal[], double result[]){
		double temp = 2 * (normal[0]*lDir[0]+normal[1]*lDir[1]+normal[2]*lDir[2]);
		result[0] = temp*normal[0] - lDir[0];
		result[1] = temp*normal[1] - lDir[1];
		result[2] = temp*normal[2] - lDir[2];
	}
	
	/**
	 * calculate the point production of light and normal
	 * @param normal
	 * @return
	 */
	public double calcProduct(double normal[]){
		return lDir[0] * normal[0] + lDir[1] * normal[1] + lDir[2] * normal[2];
	}

}
