package org.hw4;

import java.awt.Graphics;

import org.utils.shape.Geometry;
import org.utils.shape.ShapeCube;
import org.utils.shape.ShapeCylinder;
import org.utils.shape.ShapeSphere;
import org.utils.transform.Projection;

public class Person {
	Geometry part[];
	Geometry head = new ShapeSphere();
	Geometry leftUpperArm = new ShapeCylinder();
	Geometry rightUpperArm = new ShapeCylinder();
	Geometry leftForeArm = new ShapeCylinder();
	Geometry rightForeArm = new ShapeCylinder();
	Geometry leftShank = new ShapeCylinder();
	Geometry rightShank = new ShapeCylinder();
	Geometry leftThigh = new ShapeCylinder();
	Geometry rightThigh = new ShapeCylinder();
	Geometry Body = new ShapeCube();

	public Person() {
		part = new Geometry[] { this.head, this.leftForeArm, this.rightForeArm,
				this.leftShank, this.rightShank, this.leftThigh,
				this.rightThigh, this.leftUpperArm, this.rightUpperArm,
				this.Body, };
		for(int i = 0 ; i < part.length ; i++)
			part[i].globe(20, 20);
		
	}
	
	private void identity()
	{
		for(int i = 0 ; i < part.length ;i++){
			part[i].getMatrix().identity();
		}
		
	}
	private void origin()
	{
		for(int i = 0 ; i < part.length ; i++)
			part[i].getMatrix().rotateX(1);
		this.head.getMatrix().translate(0, 3, 0);
		
//		this.leftUpperArm.getMatrix().rotateX(1);
		this.leftUpperArm.getMatrix().translate(1.2, 0, 0);
		this.leftUpperArm.getMatrix().scale(.2, .2, 1);
		
//		this.rightUpperArm.getMatrix().rotateX(1);
		this.rightUpperArm.getMatrix().translate(-1.2, 0, 0);
		this.rightUpperArm.getMatrix().scale(.2, .2, 1);
	
		
//		this.leftForeArm.getMatrix().rotateX(1);
		this.leftForeArm.getMatrix().translate(1.5, -4, 0);
		this.leftForeArm.getMatrix().scale(.2, .2, 1);
		
//		this.rightForeArm.getMatrix().rotateX(1);
		this.rightForeArm.getMatrix().translate(-1.5, -4, 0);
		this.rightForeArm.getMatrix().scale(.2, .2, 1);
		
		this.leftThigh.getMatrix().translate(1, -8, 0);
		this.leftThigh.getMatrix().scale(.3, .3, 1.5);
		
		this.rightThigh.getMatrix().translate(-1, -8, 0);
		this.rightThigh.getMatrix().scale(.3, .3, 1.5);
		
		this.leftShank.getMatrix().translate(1.4, -17, 0);
		this.leftShank.getMatrix().scale(.3, .3, 1.5);
		
		this.rightShank.getMatrix().translate(-1.4, -17, 0);
		this.rightShank.getMatrix().scale(.3, .3, 1.5);
		
		this.Body.getMatrix().translate(0, -1, .5);
		this.Body.getMatrix().scale(2, 6, .8);
		
	}
	
	public void drawPerson(Graphics g, Projection p){
		for(int i = 0 ; i < part.length ; i++){
			part[i].drawShape(g, p);
		}
		
	}
	
	public void armup(double alpha){
		this.leftForeArm.getMatrix().identity();
		this.leftForeArm.getMatrix().rotateZ(alpha);
		this.leftForeArm.getMatrix().rotateX(1);
		this.leftForeArm.getMatrix().translate(1.5, -4, 0);
		this.leftForeArm.getMatrix().scale(.2, .2, 1);
		
	}
	
	public void move(double x){
		this.identity();
		for(int i = 0 ; i < part.length ; i++){
			part[i].getMatrix().translate(x, 0, 0);
		}
		this.origin();
	}
}
