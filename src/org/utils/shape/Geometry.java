package org.utils.shape;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;

import org.utils.render.RenderPolygons;
import org.utils.transform.Matrix;
import org.utils.transform.Projection;

public abstract class Geometry implements IGeometry{
	double vertices[][];
	int faces[][];
	Matrix m = new Matrix();
	
	private double dst1[] = new double[3];
	private double dst2[] = new double[3];
	private int end1[] = new int[2];
	private int end2[] = new int[2];
	private int end[][] = new int[4][5];
	
	private ArrayList<IGeometry> child = new ArrayList<IGeometry>();
	
	public abstract void globe(int M, int N);
	//v is in the latitude, u is in the longitude 
	abstract double plotX(double u, double v);
	abstract double plotY(double u, double v);
	abstract double plotZ(double u, double v);
	
	public static void drawFromRoot(Geometry root, Graphics g, Projection proj){
		Geometry pointer = root;
		LinkedList<IGeometry> q = new LinkedList<IGeometry>();
		q.add(pointer);
		while(!q.isEmpty()){
			pointer = (Geometry) q.pollFirst();
			pointer.drawShape(g, proj);
			for(int i = 0 ; i < pointer.getNumChild() ;i++){
				pointer.getChild(i).getMatrix().leftMultiply(pointer.getMatrix());
				q.add(pointer.getChild(i));
			}			
		}
	}
	
	public static void renderFromRoot(Geometry root, ArrayList<RenderPolygons> s, Projection proj){
		Geometry pointer = root;
		LinkedList<IGeometry> q = new LinkedList<IGeometry>();
		q.add(pointer);
		while(!q.isEmpty()){
			pointer = (Geometry) q.pollFirst();
			s.addAll(pointer.renderShapeWithColor(proj));
			for(int i = 0 ; i < pointer.getNumChild() ;i++){
				pointer.getChild(i).getMatrix().leftMultiply(pointer.getMatrix());
				q.add(pointer.getChild(i));
			}			
		}
	}
	
	public Matrix getMatrix(){
		return m;
	}
	
	public abstract void drawShape(Graphics g, Projection p);
	
	
	public ArrayList<RenderPolygons> renderShapeWithColor(Projection p){
		double zIndex[] = new double[4];
		ArrayList<RenderPolygons> result = new ArrayList<RenderPolygons>();
		if(faces == null) return result;
		RenderPolygons temp;
		for(int i = 0 ; i<faces.length ;i++){
			for(int j = 0 ;j <4 ; j++){
				m.transform(vertices[faces[i][j]], dst1);
				p.projectPoint(dst1, end[j]);
				this.setColor(dst1, end[j]);
				zIndex[j] = p.getZIndex(dst1);
			}
			temp = new RenderPolygons();
			temp.renderToTrapezoidWithColor(end, zIndex);
			result.add(temp);			
		}
		return result;
	}
	
	private void setColor(double[] vertice, int point[]){
		point[2] = (int) ((vertice[3]+1.0)/2 * 255);
		point[3] = (int) ((vertice[4]+1.0)/2 * 255);
		point[4] = (int) ((vertice[5]+1.0)/2 * 255);
	}
	
	
	void drawEdge(double[] point1, double[] point2, Graphics g, Projection proj){
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
	 * this is used to remove rChild
	 * Note: this will only be removed when they are exactly the same
	 */
	@Override
	public void removeChild(IGeometry rChild) {
		for(int i = 0 ; i < child.size() ;i++){
			if(child.get(i) == rChild)
				child.remove(i);
		}
	}
}
