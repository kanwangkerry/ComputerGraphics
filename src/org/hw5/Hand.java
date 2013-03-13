package org.hw5;

import java.awt.Color;
import java.awt.Graphics;

import org.utils.BufferedApplet;
import org.utils.shape.Geometry;
import org.utils.shape.ShapeCube;
import org.utils.shape.ShapeCylinder;
import org.utils.shape.ShapeNull;
import org.utils.transform.Projection;

@SuppressWarnings("serial")
public class Hand extends BufferedApplet {

	int w = 0, h = 0;

	double speedCirclePerSecond = 0.1;
	int frameCount = 0;
	double alpha;

	Geometry joint[][] = new Geometry[4][3];
	Geometry dactylus[][] = new Geometry[4][3];
	Geometry root = new ShapeNull();
	Geometry hand = new ShapeCube();

	Projection proj = new Projection();

	public void create() {
		root = new ShapeNull();
		hand = new ShapeCube();
		root.getMatrix().identity();
		hand.getMatrix().identity();

		root.addChild(hand);
		hand.globe(20, 20);

		hand.getMatrix().scale(1, 1, 0.3);

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
	public void render(Graphics g) {
		if (w == 0) {
			w = getWidth();
			h = getHeight();
			proj = new Projection(w, h, 15.0);
			alpha = 0;
		}

		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		Color c = new Color(0xd2b16f);
		g.setColor(c);
		
		create();
		alpha = speedCirclePerSecond * 2 * Math.PI
				* ((frameCount) / (double) 20);
		frameCount = (frameCount + 1 + 200) % 200;

		if (alpha < Math.PI) {
			for (int i = 0; i < 4; i++) {
				joint[i][0].getMatrix().rotateX(alpha / 2);
				joint[i][1].getMatrix().rotateX(alpha / 2);
				joint[i][2].getMatrix().rotateX(alpha / 2);
			}
		} else {
			for (int i = 0; i < 4; i++) {
				joint[i][0].getMatrix().rotateX((2 * Math.PI - alpha) / 2);
				joint[i][1].getMatrix().rotateX((2 * Math.PI - alpha) / 2);
				joint[i][2].getMatrix().rotateX((2 * Math.PI - alpha) / 2);
			}
		}
		
		root.getMatrix().translate(0, -2, 0);
		root.getMatrix().rotateY(alpha);
		root.getMatrix().scale(1.8, 1.8, 1.8);

		Geometry.drawFromRoot(root, g, proj);
	}

}
