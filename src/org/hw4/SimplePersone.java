package org.hw4;

import java.awt.Color;
import java.awt.Graphics;

import org.utils.BufferedApplet;
import org.utils.shape.Geometry;
import org.utils.shape.ShapeCube;
import org.utils.shape.ShapeSphere;
import org.utils.transform.Projection;

@SuppressWarnings("serial")
public class SimplePersone extends BufferedApplet {

	int w = 0, h = 0;
	Projection proj = new Projection();
	
	@Override
	public void render(Graphics g) {
		if (w == 0) {
			w = getWidth();
			h = getHeight();
			proj = new Projection(w, h, 10.0);
		}
		
		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.black);
		Geometry cube = new ShapeCube();
		cube.globe(20, 20);
		cube.drawShape(g, proj);

	}

}
