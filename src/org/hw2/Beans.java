package org.hw2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Beans {
	int x, y;
	
	Beans()
	{
		x = 0;
		y = 0;
	}
	
	void generate(Random r, int w, int h){
		x = r.nextInt(w-15);
		y = r.nextInt(h-15);
	}
	
	void plotBean(Graphics g)
	{
		
		g.setColor(new Color(0xC68FA0));
		g.fillOval(x, y, 15, 15);		
	}
	
	boolean eaten(int tx, int ty, int w, int h){
		if(x >= tx-8 && x <= tx+w+8 && y >= ty-8 && y <= ty+h+8)
			return true;
		else return false;
	}

}
