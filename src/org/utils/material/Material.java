package org.utils.material;

import org.utils.raytracing.RayTracing;
import org.utils.shape.Geometry;

/**
 * The class of material
 * 
 * @author kerry
 * 
 */
public class Material {
	/**
	 * Colors of the material.
	 */
	double[] ambientColor = new double[3];
	double[] diffuseColor = new double[3];
	double[] specularColor = new double[3];
	double specularPower;

	private double tempColor[] = new double[3];
	private double normalV[] = new double[3];
	private double tempV[] = new double[3];

	public Material() {
		for (int i = 0; i < 3; i++) {
			ambientColor[i] = 1.0;
			diffuseColor[i] = 1.0;
			specularColor[i] = 1.0;
		}
		specularPower = 10.0;
	}

	public void setMaterial(double ambient[], double diffuse[],
			double specular[], double power) {
		System.arraycopy(ambient, 0, this.ambientColor, 0, 3);
		System.arraycopy(diffuse, 0, this.diffuseColor, 0, 3);
		System.arraycopy(specular, 0, this.specularColor, 0, 3);
		this.specularPower = power;
	}

	/**
	 * Calculate the color by eye, light, normal and this material. Save the
	 * result in dst[]
	 * 
	 * @param l
	 * @param eye
	 * @param normal
	 * @param dst
	 */
	public void calcColor(Light l[], double eye[], double normal[], int dst[],
			RayTracing shadowRT, Geometry from) {
		double temp;
		double result[] = new double[3];
		for (int i = 0; i < 3; i++) {
			result[i] = this.ambientColor[i];
			normalV[i] = normal[i + 3];
		}
		if (shadowRT != null)
			shadowRT.setRayStartPoint(normal[0], normal[1], normal[2]);
		Geometry tempG;
		// loop over all the light to calculate the color.
		for (int i = 0; i < l.length; i++) {
			// test if in shadow. If in shadow, then do not add any diffuse
			// or specular color of this light
			if (shadowRT != null) {
				shadowRT.setRayVector(l[i].lDir[0] * 10 - normal[0],
						l[i].lDir[1] * 10 - normal[1], l[i].lDir[2] * 10
								- normal[2]);
				tempG = shadowRT.getNearestOtherObj(from);

				if (tempG != null) {
					continue;
				}
			}

			for (int j = 0; j < 3; j++) {
				tempColor[j] = 0;
			}
			temp = l[i].calcProduct(normalV);
			temp = (temp < 0) ? 0 : temp;
			for (int j = 0; j < 3; j++) {
				tempColor[j] += temp * this.diffuseColor[j];
			}

			l[i].calcReflection(normalV, tempV);
			temp = eye[0] * tempV[0] + eye[1] * tempV[1] + eye[2] * tempV[2];
			temp = temp < 0 ? 0 : temp;
			temp = Math.pow(temp, specularPower);
			for (int j = 0; j < 3; j++) {
				tempColor[j] += temp * this.specularColor[j];
			}

			for (int j = 0; j < 3; j++) {
				result[j] += tempColor[j] * l[i].lColor[j];
			}
		}
		for (int i = 0; i < 3; i++) {
			dst[i + 2] = (int) (Math.pow(result[i], 0.45) * 255);
		}
	}

}
