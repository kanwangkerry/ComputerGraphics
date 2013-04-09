package org.utils.material;

public class Material {
	double[] ambientColor = new double[3];
	double[] diffuseColor = new double[3];
	double[] specularColor = new double[3];
	double specularPower;
	
	private double tempColor[] = new double[3];
	private double normalV[] = new double[3];
	private double tempV[] = new double[3];
	
	public Material(){
		for(int i = 0 ; i < 3 ; i ++){
			ambientColor[i] = 1.0;
			diffuseColor[i] = 1.0;
			specularColor[i] = 1.0;
		}
		specularPower = 10.0;
	}
	
	public void setMaterial(double ambient[], double diffuse[], double specular[], double power){
		System.arraycopy(ambient, 0, this.ambientColor, 0, 3);
		System.arraycopy(diffuse, 0, this.diffuseColor, 0, 3);
		System.arraycopy(specular, 0, this.specularColor, 0, 3);
		this.specularPower = power;
	}
	
	public void calcColor(Light l[], double eye[], double normal[], int dst[]){
		double temp;
		double result[] = new double[3];
		for(int i = 0 ;i < 3; i++){
			result[i] = this.ambientColor[i];
			normalV[i] = normal[i+3];
		}
		for(int i = 0 ; i < l.length ; i++){
			for(int j = 0 ; j < 3; j++){
				tempColor[j] = 0;
			}
			temp = l[i].calcProduct(normalV);
			temp = (temp<0)? 0 : temp;
			for(int j = 0 ; j < 3; j++){
				tempColor[j] += temp*this.diffuseColor[j];
			}
			
			l[i].calcReflection(normalV, tempV);
			temp = eye[0]*tempV[0] + eye[1] * tempV[1] + eye[2]*tempV[2];
			temp = temp<0?0:temp;
			temp = Math.pow(temp, specularPower);
			for(int j = 0 ; j < 3; j++){
				tempColor[j] += temp*this.specularColor[j];
			}
			
			for(int j = 0 ; j < 3; j++){
				result[j] += tempColor[j] * l[i].lColor[j]; 
			}
		}
		for(int i = 0 ; i < 3; i++){
			dst[i+2] = (int) (Math.pow(result[i], 0.45)*255);
		}
	}

}
