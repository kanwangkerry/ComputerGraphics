package org.utils.raytracing;

public class ColorPainter {
	int pixel[];
	int W, H;
	
	public ColorPainter(int w, int h){
		W = w;
		H = h;
		pixel = new int[W*H];
	}
	
	public int[] getPixel(){
		return pixel;
	}
	
	public void setColor(int x, int y, int color){
		pixel[y*W+x] = color;
	}
	
	public int getColor(int x, int y){
		return pixel[y*W+x];
	}

}
