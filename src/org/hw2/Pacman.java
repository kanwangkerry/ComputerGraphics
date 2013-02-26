package org.hw2;

import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;

import org.utils.BufferedApplet;

import java.util.Random;

@SuppressWarnings("serial")
public class Pacman extends BufferedApplet {
	int w = 0, h = 0;
	double startTime = System.currentTimeMillis() / 1000.0;
	int speed = 4;
	int tx, ty;
	int direction[][] = { { 1, 0 }, { 0, -1 }, { -1, 0 }, { 0, 1 } };
	int currDir;
	final int radius = 15;
	Random r = new Random(System.currentTimeMillis());
	Color myColor = new Color(0xCEC781);
	Color bgColor = new Color(0x663366);
	Color eyeColor = new Color(0x00CCFF);

	int mouthCount = 1;
	int xPoints[] = { 0, 0, 0 };
	int yPoints[] = { 0, 0, 0 };
	
	Beans b[] = new Beans[10];

	public boolean keyUp(Event e, int key) {
		switch (key) {
		case Event.UP:
			currDir = 1;
			break;
		case Event.RIGHT:
			currDir = 0;
			break;
		case Event.LEFT:
			currDir = 2;
			break;
		case Event.DOWN:
			currDir = 3;
			break;
		}
		return true;
	}

	public void render(Graphics g) {
		if (w == 0) {
			w = getWidth();
			h = getHeight();
			tx = w / 2;
			ty = h / 2;
			currDir = r.nextInt(4);
			for(int i = 0 ; i < 10 ; i++){
				b[i] = new Beans();
				b[i].generate(r, w, h);
			}
		}
		g.setColor(bgColor);
		g.fillRect(0, 0, w, h);

		tx += direction[currDir][0] * speed;
		ty += direction[currDir][1] * speed;
		tx = (tx + (w - 2 * radius)) % (w - 2 * radius);
		ty = (ty + (h - 2 * radius)) % (h - 2 * radius);
		// fill pacman
		g.setColor(myColor);
		g.fillOval(tx - radius, ty - radius, 2 * radius, 2 * radius);
		// fill eye
		g.setColor(eyeColor);
		g.fillOval(tx + direction[(currDir + 5) % 4][0] * 4 - 3, ty
				+ direction[currDir!=2?(currDir + 5) % 4:(currDir+3)%4][1] * 4 - 3, 5, 5);
		// fill mouth
		g.setColor(bgColor);
		if (mouthCount > 0) {
			this.mouthCount += 1;
			if (mouthCount == 5)
				mouthCount = -1;
			xPoints[0] = tx;
			xPoints[1] = (int) ((direction[currDir][0] == 0) ? (tx + radius / 2)
					: (tx + direction[currDir][0] * radius));
			xPoints[2] = (int) ((direction[currDir][0] == 0) ? (tx - radius / 2)
					: (tx + direction[currDir][0] * radius));

			yPoints[0] = ty;
			yPoints[1] = (int) ((direction[currDir][1] == 0) ? (ty + radius / 2)
					: (ty + direction[currDir][1] * radius));
			yPoints[2] = (int) ((direction[currDir][1] == 0) ? (ty - radius / 2)
					: (ty + direction[currDir][1] * radius));
			g.fillPolygon(xPoints, yPoints, 3);
		} else {
			this.mouthCount -= 1;
			if (mouthCount == -5)
				mouthCount = 1;
		}
		for(int i = 0 ; i < b.length ; i++){
			if(b[i].eaten(tx - radius, ty-radius, 2*radius, 2*radius))
				b[i].generate(r, w, h);
			b[i].plotBean(g);
		}

	}
}
