package org.utils.render;

import java.util.ArrayList;

import org.utils.shape.Geometry;

public class RenderPolygons {

	public ArrayList<Triangle> render = new ArrayList<Triangle>();
	
	public String toString(){
		return render.toString();
	}
	
	private Geometry from;
	public boolean isInGeometry(Geometry object){
		return from == object;
	}
	public void setGeometry(Geometry obj){
		this.from = obj;
	}
	
	/**
	 * make a trapezoid into triangles. save all the triangles into the render array.
	 * @param point
	 * @param zIndex
	 */
	public void renderToTrapezoidWithColor(int point[][], double zIndex[]) {
		ArrayList<Triangle> triangles = new ArrayList<Triangle>();
		Triangle temp;
//		if (point[0][0] == point[1][0] && point[0][0] == point[2][0])
//			return;
//		if (point[0][1] == point[1][1] && point[0][1] == point[2][1])
//			return;
//		if ((point[0][1] - point[1][1]) / ((double) point[0][0] - point[1][0]) == (point[0][1] - point[2][1])
//				/ ((double) point[0][0] - point[2][0]))
//			return;
		if (point.length == 3) {
			temp = new Triangle(point[0], point[1], point[2], zIndex[0],
					zIndex[1], zIndex[2]);
			triangles.add(temp);
		} else {
			if ((point[0][0] != point[1][0] || point[0][1] != point[1][1])
					&& (point[0][0] != point[2][0] || point[0][1] != point[2][1])
					&& (point[2][0] != point[1][0] || point[2][1] != point[1][1])) {
				temp = new Triangle(point[0], point[1], point[2], zIndex[0],
						zIndex[1], zIndex[2]);
				triangles.add(temp);
			}
			if ((point[0][0] != point[3][0] || point[0][1] != point[3][1])
					&& (point[0][0] != point[2][0] || point[0][1] != point[2][1])
					&& (point[2][0] != point[3][0] || point[2][1] != point[3][1])) {
				temp = new Triangle(point[0], point[2], point[3], zIndex[0],
						zIndex[2], zIndex[3]);
				triangles.add(temp);
			}
		}

		for (int i = 0; i < triangles.size(); i++) {
			temp = triangles.get(i);
			ArrayList<Triangle> x = Triangle.splitTriangle(temp);
			render.addAll(x);
		}
	}
	
	/**
	 * Color a polygon. We only need to color every triangle.
	 * @param pix
	 * @param color
	 * @param W
	 * @param zBuffer
	 */
	public void colorPolygon(int pix[], int color, int W, double zBuffer[]){
		for(int i = 0 ; i < this.render.size() ;i++){
			render.get(i).renderTriangle(pix, color, W, zBuffer);
		}
	}
}
