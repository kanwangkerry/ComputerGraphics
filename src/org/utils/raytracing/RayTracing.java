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
	double result[] = new double[2];
	public int bgColor = 0;
	Map<Geometry, ObjectInfo> objInfo = new HashMap<Geometry, ObjectInfo>();

	private void initRayTraceEnd(int x, int y, int W, int H, double F) {
		this.setRayVector((x - 0.5 * W) * F / H, (0.5 * H - y) * F / H, -F);
	}

	private Geometry getNearestObj() {
		boolean flag;
		double T = Integer.MAX_VALUE;
		Geometry tempResult = null;

		for (Geometry obj : objPainter.keySet()) {
			flag = this.raytrace(obj);
			if (flag) {
				if (result[0] < T) {
					T = result[0];
					tempResult = obj;
				}
			}
		}

		return tempResult;
	}
	
	public void setRayStartPoint(double x, double y, double z){
		v[0] = x;
		v[1] = y;
		v[2] = z;
	}
	
	public void setRayVector(double x, double y, double z){
		w[0] = x;
		w[1] = y;
		w[2] = z;
		double normal = w[0] * w[0] + w[1] * w[1] + w[2] * w[2];
		normal = Math.sqrt(normal);
		for (int i = 0; i < 3; i++)
			w[i] = w[i] / normal;
	}

	//speeded up ray tracing
	public void rayTraceRender(int x, int y, int W, int H, double F,
			int pixel[]) {
		this.initRayTraceEnd(x + 2, y + 2, W, H, F);
		Geometry tempResult = this.getNearestObj();
		int color = (tempResult == null ? bgColor : objPainter.get(tempResult)
				.getColor(x + 2, y + 2));
		int i = 0;
		for (i = 0; i < 3; i++) {
			int tempColor = this.unpack(color, i);
			if (tempColor != this.unpack(pixel[(y - 1) * W + x - 1], i)) {
				break;
			}
			if (tempColor != this.unpack(pixel[(y+2) * W + x - 1], i)) {
				break;
			}
			if (tempColor != this.unpack(pixel[(y - 1) * W + x+2], i)) {
				break;
			}
		}
		if (i == 3) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					pixel[(y + j) * W + x + k] = color;
				}
			}
		} else {
//			int rSum = 0, gSum = 0, bSum = 0;
			int tempColor;
//			int r[] = new int[16];
//			int g[] = new int[16];
//			int b[] = new int[16];
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					this.initRayTraceEnd(x + k, y + j, W, H, F);
					tempResult = this.getNearestObj();
					tempColor = (tempResult == null ? bgColor : objPainter.get(
							tempResult).getColor(x + k, y + j));
					pixel[(y + j) * W + x + k] = tempColor;
//					rSum += this.unpack(tempColor, 0);
//					r[j * 4 + k] = this.unpack(tempColor, 0);
//					gSum += this.unpack(tempColor, 1);
//					g[j * 4 + k] = this.unpack(tempColor, 1);
//					bSum += this.unpack(tempColor, 2);
//					b[j * 4 + k] = this.unpack(tempColor, 2);
				}
			}
//			color = MISApplet.pack(rSum / 16, gSum / 16, bSum / 16);
//			for (int j = 0; j < 4; j++) {
//				for (int k = 0; k < 4; k++) {
//					// pixel[(y + j) * W + x + k] = color;
//					pixel[(y + j) * W + x + k] = MISApplet.pack(
//							(r[j * 4 + k] + rSum / 16) / 2,
//							(g[j * 4 + k] + gSum / 16) / 2,
//							(b[j * 4 + k] + bSum / 16) / 2);
//				}
//			}
		}

	}

	private double calcRadius(double center[], double point[]) {
		double result = 0;
		for (int i = 0; i < 3; i++) {
			result += (center[i] - point[i]) * (center[i] - point[i]);
		}
		return Math.sqrt(result);
	}

	void diff_sv(double s[], double v[], double s_v[]) {
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

	boolean raytrace(Geometry objIndex) {
		ObjectInfo tempInfo = objInfo.get(objIndex);
		

		double A = 1.0;
		double B = 2 * dot(w, tempInfo.s_v);
		double C = dot(tempInfo.s_v, tempInfo.s_v) - tempInfo.r * tempInfo.r;

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
		Geometry pointer = root;
		LinkedList<IGeometry> q = new LinkedList<IGeometry>();
		q.add(pointer);
		ColorPainter painter;
		ObjectInfo tempInfo;
		double zBuffer[] = new double[W * H];
		v[0] = 0;
		v[1] = 0;
		v[2] = F;
		while (!q.isEmpty()) {

			pointer = (Geometry) q.pollFirst();
			// set object info
			if (pointer != root) {
				tempInfo = new ObjectInfo();
				System.arraycopy(pointer.getVertex(), 0, tempInfo.point, 0, 3);
				System.arraycopy(pointer.getCenter(), 0, tempInfo.s, 0, 3);
				tempInfo.r = this.calcRadius(tempInfo.s, tempInfo.point);
				diff_sv(tempInfo.s, this.v, tempInfo.s_v);
				objInfo.put(pointer, tempInfo);
			}

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

	private int unpack(int packedRGB, int component) {
		return packedRGB >> 8 * (2 - component) & 255;
	}

}
