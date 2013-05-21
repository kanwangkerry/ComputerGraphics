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

	public ImageBuffer buffer;

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

	private double lightColor[] = new double[3];

	private void calcLightColor(Light l, double vertex[]) {
		double d = 0;
		for (int i = 0; i < 3; i++)
			d += (vertex[i] - l.lSource[i]) * (vertex[i] - l.lSource[i]);
		d = Math.sqrt(d);
		for (int i = 0; i < 3; i++) {
			lightColor[i] = l.lColor[i] / Math.pow(d, 1);
		}
	}

	public int[] calColorOnBallWithImage(double vertice[]) {
		double n = vertice[0] * vertice[0] + vertice[1] * vertice[1]
				+ vertice[2] * vertice[2];
		n = Math.sqrt(n);
		double u = Math.atan2((vertice[1]+0.0001)/ n, vertice[0]/n)/ (2 * Math.PI);
		double v = (1 + vertice[2] / n) / 2;
		int x = (int) (u * (buffer.width-1));
		int y = (int) (v * (buffer.height-1));
		int color[] = new int[3];
		color[0] = buffer.get(x, y, 0);
		color[1] = buffer.get(x, y, 1);
		color[2] = buffer.get(x, y, 2);
		return color;
	}

	public void setImage(ImageBuffer b) {
		this.buffer = b;
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
				shadowRT.setRayVector(l[i].lSource[0] - normal[0],
						l[i].lSource[1] - normal[1], l[i].lSource[2]
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

			this.calcLightColor(l[i], normal);
			for (int j = 0; j < 3; j++) {
				result[j] += tempColor[j] * lightColor[j];
			}
		}
		if (buffer != null) {
			int color[] = this.calColorOnBallWithImage(normal);
			for (int i = 0; i < 3; i++) {
				// dst[i + 2] = (int) (Math.pow(result[i], 0.45) * 255);
				dst[i + 2] = color[i];
			}
		} else {
			for (int i = 0; i < 3; i++) {
				dst[i + 2] = (int) (Math.pow(result[i], 0.45) * 255);
			}

		}
	}

}
