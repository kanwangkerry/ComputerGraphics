package org.utils.shape;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import org.utils.material.Light;
import org.utils.material.Material;
import org.utils.raytracing.RayTracing;
import org.utils.render.RenderPolygons;
import org.utils.transform.Matrix;
import org.utils.transform.Projection;

public abstract class Geometry implements IGeometry {
	double vertices[][];
	int faces[][];
	Matrix m = new Matrix();

	private double dst1[] = new double[6];
	private double dst2[] = new double[3];
	private int end1[] = new int[2];
	private int end2[] = new int[2];
	private int end[][] = new int[4][5];

	/**
	 * Maintian the child list.
	 */
	private ArrayList<IGeometry> child = new ArrayList<IGeometry>();

	Material material = new Material();

	/**
	 * abstract functions, need to be implemented by the subclass.
	 * 
	 * @param M
	 * @param N
	 */
	public abstract void globe(int M, int N);

	// v is in the latitude, u is in the longitude
	abstract double plotX(double u, double v);

	abstract double plotY(double u, double v);

	abstract double plotZ(double u, double v);

	/**
	 * Used in hw4, draw the edge of the geometry from root.
	 * 
	 * @param root
	 * @param g
	 * @param proj
	 */
	public static void drawFromRoot(Geometry root, Graphics g, Projection proj) {

		Geometry pointer = root;
		LinkedList<IGeometry> q = new LinkedList<IGeometry>();
		q.add(pointer);
		while (!q.isEmpty()) {
			pointer = (Geometry) q.pollFirst();
			pointer.drawShape(g, proj);
			for (int i = 0; i < pointer.getNumChild(); i++) {
				pointer.getChild(i).getMatrix()
						.leftMultiply(pointer.getMatrix());
				q.add(pointer.getChild(i));
			}
		}
	}

	/**
	 * Used in and after hw5: used to render the faces of a geometry into 2d
	 * faces so we can render it in Z-buffer method. This function mainly focus
	 * on traverse the geometry tree structure, and call renderShapWithColor to
	 * do actual render jobs.
	 * 
	 * @param root
	 * @param s
	 * @param proj
	 */

	public static void transformGeometryFromRoot(Geometry root) {
		Geometry pointer = root;
		LinkedList<IGeometry> q = new LinkedList<IGeometry>();
		q.add(pointer);
		while (!q.isEmpty()) {
			pointer = (Geometry) q.pollFirst();
			for (int i = 0; i < pointer.getNumChild(); i++) {
				pointer.getChild(i).getMatrix()
						.leftMultiply(pointer.getMatrix());
				q.add(pointer.getChild(i));
			}
		}
	}

	public static void renderFromRoot(Geometry root,
			ArrayList<RenderPolygons> s, Projection proj, Light l[],
			double[] eye, RayTracing rt) {
		Geometry pointer = root;
		LinkedList<IGeometry> q = new LinkedList<IGeometry>();
		q.add(pointer);
		while (!q.isEmpty()) {
			pointer = (Geometry) q.pollFirst();
			s.addAll(pointer.renderShapeWithColor(proj, l, eye, rt));
			for (int i = 0; i < pointer.getNumChild(); i++) {
				q.add(pointer.getChild(i));
			}
		}
	}

	public Matrix getMatrix() {
		return m;
	}

	public abstract void drawShape(Graphics g, Projection p);

	/**
	 * Actual render jobs. This is the important function for hw5 and later HWs.
	 * For each face of the geometry, it firstly transform it into a 2d face,
	 * and at the same time figure out the color and z-index of each vertex of
	 * this face. Then, we render it to trapezoids and then into triangles. At
	 * last, we will get a list of RenderPolygons, and each polygon should
	 * contains some triangles and should be able to render use a z-buffer
	 * method.
	 * 
	 * @param p
	 * @param l
	 * @param eye
	 * @return
	 */
	public ArrayList<RenderPolygons> renderShapeWithColor(Projection p,
			Light l[], double eye[], RayTracing rt) {
		double zIndex[] = new double[4];
		ArrayList<RenderPolygons> result = new ArrayList<RenderPolygons>();
		if (faces == null)
			return result;
		RenderPolygons temp;
		for (int i = 0; i < faces.length; i++) {
			for (int j = 0; j < 4; j++) {

				m.transform(vertices[faces[i][j]], dst1);
				p.projectPoint(dst1, end[j]);
				this.setColor(dst1, end[j], l, eye, rt);
				zIndex[j] = p.getZIndex(dst1);
			}
			temp = new RenderPolygons();
			temp.renderToTrapezoidWithColor(end, zIndex);
			temp.setGeometry(this);
			result.add(temp);
		}
		return result;
	}

	/**
	 * set color for hw7: it should use the normal, light, eye and material to
	 * figure out the color.
	 * 
	 * @param vertice
	 * @param point
	 * @param l
	 * @param eye
	 */
	private void setColor(double[] vertice, int point[], Light l[],
			double[] eye, RayTracing rt) {
		material.calcColor(l, eye, vertice, point, rt, this);
	}

	/**
	 * Set the material from the root
	 * 
	 * @param root
	 * @param ambient
	 * @param diffuse
	 * @param specular
	 * @param power
	 */
	public static void setMaterialFromRoot(Geometry root, double ambient[],
			double diffuse[], double specular[], double power) {
		Geometry pointer = root;
		LinkedList<IGeometry> q = new LinkedList<IGeometry>();
		q.add(pointer);
		while (!q.isEmpty()) {
			pointer = (Geometry) q.pollFirst();
			pointer.setMaterial(ambient, diffuse, specular, power);
			for (int i = 0; i < pointer.getNumChild(); i++) {
				q.add(pointer.getChild(i));
			}
		}
	}

	private void setMaterial(double ambient[], double diffuse[],
			double specular[], double power) {
		material.setMaterial(ambient, diffuse, specular, power);
	}

	void drawEdge(double[] point1, double[] point2, Graphics g, Projection proj) {
		m.transform(point1, dst1);
		m.transform(point2, dst2);

		proj.projectPoint(dst1, end1);
		proj.projectPoint(dst2, end2);
		g.drawLine(end1[0], end1[1], end2[0], end2[1]);
	}

	@Override
	public void addChild(IGeometry child) {
		this.child.add(child);
	}

	@Override
	public IGeometry getChild(int i) {
		return this.child.get(i);
	}

	@Override
	public int getNumChild() {
		return this.child.size();
	}

	/**
	 * this is used to remove rChild Note: this will only be removed when they
	 * are exactly the same
	 */
	@Override
	public void removeChild(IGeometry rChild) {
		for (int i = 0; i < child.size(); i++) {
			if (child.get(i) == rChild)
				child.remove(i);
		}
	}

	public double[] getVertex() {
		this.m.transform(this.vertices[0], dst1);
		for (int i = 0; i < 3; i++) {
			dst2[i] = dst1[i];
		}
		return dst2;
	}

	public double[] getCenter() {
		this.m.transform(new double[] { 0, 0, 0, 0, 0, 0 }, dst1);
		for (int i = 0; i < 3; i++) {
			dst2[i] = dst1[i];
		}
		return dst2;
	}
}
