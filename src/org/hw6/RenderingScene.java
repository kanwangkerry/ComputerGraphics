package org.hw6;

import java.util.ArrayList;

import org.utils.MISApplet;
import org.utils.render.RenderPolygons;
import org.utils.shape.Geometry;
import org.utils.shape.ShapeCube;
import org.utils.shape.ShapeCylinder;
import org.utils.shape.ShapeNull;
import org.utils.shape.ShapeSphere;
import org.utils.transform.Projection;

@SuppressWarnings("serial")
public class RenderingScene extends MISApplet {
	ArrayList<RenderPolygons> scene;
	Projection proj = new Projection(W, H, 15.0);
	
	Geometry joint[][] = new Geometry[4][3];
	Geometry dactylus[][] = new Geometry[4][3];
	Geometry root = new ShapeNull();
	Geometry hand = new ShapeCube();
	
	Geometry ball = new ShapeSphere();
	
	double speedCirclePerSecond = 0.1;
	int frameCount = 0;
	double alpha;



	public void create() {
		root = new ShapeNull();
		hand = new ShapeCube();
		root.getMatrix().identity();
		hand.getMatrix().identity();
		ball.getMatrix().identity();

		root.addChild(hand);
		root.addChild(ball);
		hand.globe(20, 20);
		ball.globe(20, 20);

		hand.getMatrix().scale(1, 1, 0.3);
		ball.getMatrix().translate(-3, 0, 0);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				joint[i][j] = new ShapeNull();
				dactylus[i][j] = new ShapeCylinder();
			}
		}
		for (int i = 0; i < 4; i++) {
			root.addChild(joint[i][0]);
			joint[i][0].addChild(joint[i][1]);
			joint[i][0].addChild(dactylus[i][0]);
			joint[i][1].addChild(joint[i][2]);
			joint[i][1].addChild(dactylus[i][1]);
			joint[i][2].addChild(dactylus[i][2]);
		}
		double translate[] = { -.8, -.3, .3, .8 };
		double length[][] = { { .6, .5, .3 }, { .7, .5, .3 }, { .5, .4, .3 },
				{ .3, .3, .2 } };
		for (int i = 0; i < 4; i++) {
			joint[i][0].getMatrix().identity();
			joint[i][0].getMatrix().translate(translate[i], 1, 0);
			joint[i][0].getMatrix().rotateX(-Math.PI / 2);
			joint[i][0].getMatrix().scale(.15, .15, 1);
			dactylus[i][0].globe(20, 20);
			dactylus[i][0].getMatrix().translate(0, 0, length[i][0]);
			dactylus[i][0].getMatrix().scale(1, 1, length[i][0]);

			joint[i][1].getMatrix().identity();
			joint[i][1].getMatrix().translate(0, 0, 2 * length[i][0]);
			dactylus[i][1].globe(20, 20);
			dactylus[i][1].getMatrix().translate(0, 0, length[i][1]);
			dactylus[i][1].getMatrix().scale(1, 1, length[i][1]);

			joint[i][2].getMatrix().identity();
			joint[i][2].getMatrix().translate(0, 0, 2 * length[i][1]);
			dactylus[i][2].globe(20, 20);
			dactylus[i][2].getMatrix().translate(0, 0, length[i][2]);
			dactylus[i][2].getMatrix().scale(1, 1, length[i][2]);
		}
	}
	
	@Override
	public void initialize(){
		scene = new ArrayList<RenderPolygons>();
		proj = new Projection(W, H, 15.0);		
		alpha = 0;
	}
	
	@Override
	public void initFrame(double time) {
		this.create();
		frameCount = (frameCount + 1 + 200) % 200;
		root.getMatrix().translate(0, -2, 0);
		root.getMatrix().scale(1.8, 1.8, 1.8);
		ball.getMatrix().rotateY(Math.PI/4);
		Geometry.renderFromRoot(root, scene, proj);
	} 

	@Override
	public void setPixel(int x, int y, int rgb[]) {
		for(int i = 0 ; i < scene.size(); i++){
			if(scene.get(i).isRendering(x, y)){
				rgb[0] = 0xd2b16f;
				rgb[1] = 0xb1;
				rgb[2] = 0x6f;
				return ;
			}
		}
		rgb[0] = 0x99;
		rgb[1] = 0x26;
		rgb[2] = 0x67;
	} 
}
