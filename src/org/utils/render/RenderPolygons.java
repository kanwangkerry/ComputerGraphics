package org.utils.render;

import java.util.ArrayList;

public class RenderPolygons {

	ArrayList<Triangle> render = new ArrayList<Triangle>();
	
	public void renderToTrapezoid(int point[][]) {
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		Triangle temp;
		if(point[0][0] == point[1][0] && point[0][0] == point[2][0])
			return;
		if(point[0][1] == point[1][1] && point[0][1] == point[2][1])
			return;
		if((point[0][1] - point[1][1])/((double)point[0][0] - point[1][0]) ==
				(point[0][1] - point[2][1])/((double)point[0][0] - point[2][0])	)
			return;
		if (point.length == 3) {
				temp = new Triangle(point[0], point[1], point[2]); 
				triangles.add(temp);
		}
		else{
			temp = new Triangle(point[0], point[1], point[2]); 
			triangles.add(temp);
			temp = new Triangle(point[0], point[2], point[3]); 
			triangles.add(temp);
		}
		
		for(int i = 0 ; i< triangles.size() ;i++){
			temp = triangles.get(i);
			ArrayList<Triangle> x= Triangle.splitTriangle(temp);
			render.addAll(x);
		}
	}
		
	public void colorPolygon(int pix[], int color, int W){
		for(int i = 0 ; i < this.render.size() ;i++){
			if(render.get(i).area >0){
			render.get(i).renderTriangle(pix, color, W);
			}
		}
	}
}
