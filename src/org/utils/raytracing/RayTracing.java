package org.utils.raytracing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.utils.render.RenderPolygons;
import org.utils.shape.Geometry;
import org.utils.shape.IGeometry;

public class RayTracing {
	Map<Geometry, ColorPainter> objPainter = new HashMap<Geometry, ColorPainter>();
	double w[] = new double[3];
	double v[] = new double[3];
	double r;
	double s[] = new double[3];
	double s_v[] = new double[3];
	double result[] = new double[2];
	
	private double point[] = new double[3];
	public void rayTraceRender(int x, int y, int W, int H, double F, int pixel[]){
		w[0] = (x - 0.5*W)/H;
		w[1] = (0.5*H - y)/H;
		w[2] = -F;
		double normal = w[0]*w[0]+w[1]*w[1]+w[2]*w[2];
		normal = Math.sqrt(normal);
		for(int i = 0 ; i < 3; i++)
			w[i] = w[i]/normal;
		boolean flag;
		double T = Integer.MAX_VALUE;
		Geometry tempResult = null;
		
		for(Geometry obj : objPainter.keySet()){
			System.arraycopy(obj.getVertex(), 0, this.point, 0, 3);
			System.arraycopy(obj.getCenter(), 0, this.s, 0, 3);
			this.r = this.calcRadius(this.s, this.point);
			flag = this.raytrace();
			if(flag){
				if(result[0] < T){
					T = result[0];
					tempResult = obj;
				}
			}
		}
		
		if(tempResult != null){
			pixel[y*W+x] = objPainter.get(tempResult).getColor(x, y);
		}
	}
	
	private double calcRadius(double center[], double point[]){
		double result = 0;
		for(int i = 0 ; i < 3; i++){
			result += (center[i]-point[i])*(center[i] - point[i]);
		}
		return Math.sqrt(result);
	}
	
	void diff_sv() {
		for (int i = 0; i < 3; i++)
			s_v[i] = v[i] - s[i];
	}

	private double dot(double x[], double y[]) {
		double result = 0;
		for (int i = 0; i < x.length; i++) {
			result += x[i] * y[i];
		}
		return result;
	}

	boolean raytrace() {

		diff_sv();

		double A = 1.0;
		double B = 2 * dot(w, s_v);
		double C = dot(s_v, s_v) - r*r;

		return solveQuadraticEquation(A, B, C, result);
	}

	boolean solveQuadraticEquation(double A, double B, double C, double[] t) {

		double discriminant = B * B - 4 * A * C;
		if (discriminant < 0)
			return false;

		double d = Math.sqrt(discriminant);
		t[0] = (-B - d) / (2 * A);
		t[1] = (-B + d) / (2 * A);
		return true;
	}

	/**
	 * Build up a painter for a geometry object.
	 * 
	 * @param root
	 * @param scene
	 * @param W
	 * @param H
	 * @param F
	 */
	public void buildPainter(Geometry root, ArrayList<RenderPolygons> scene,
			int W, int H, double F) {
		this.v = new double[] {0, 0, F};
		Geometry pointer = root;
		LinkedList<IGeometry> q = new LinkedList<IGeometry>();
		q.add(pointer);
		ColorPainter painter;
		double zBuffer[] = new double[W * H];
		while (!q.isEmpty()) {
			pointer = (Geometry) q.pollFirst();
			painter = new ColorPainter(W, H);
			// initial zbuffer
			for (int i = 0; i < W; i++) {
				for (int j = 0; j < H; j++) {
					zBuffer[j * W + i] = -F;
				}
			}
			// set color of this Geometry
			for (int j = 0; j < scene.size(); j++) {
				if (scene.get(j).isInGeometry(pointer))
					scene.get(j)
							.colorPolygon(painter.getPixel(), 0, W, zBuffer);
			}
			this.objPainter.put(pointer, painter);
			for (int i = 0; i < pointer.getNumChild(); i++) {
				q.add(pointer.getChild(i));
			}
		}
		this.objPainter.remove(root);
	}
}
