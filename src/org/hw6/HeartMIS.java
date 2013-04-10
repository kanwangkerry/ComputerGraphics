package org.hw6;

import org.utils.MISApplet;

@SuppressWarnings("serial")
public class HeartMIS extends MISApplet {
	double wSize, hSize;
	@Override
	public void initFrame(double time) {
		time = time*33;
		hSize = 100 + time%50;
		wSize = 100 + time%50;
		wSize = wSize*1.5;
	} 

	@Override
	public void setPixel(int x, int y, int rgb[]) {
		double px = (x-W/2)/wSize;
		double py = -(y-H/2)/hSize;
		if(Math.pow(px, 2)+Math.pow((py-Math.pow(Math.pow(px, 2),1/(double)3)), 2) < 1){
			rgb[0] = 0xFF;
			rgb[1] = 0x40;
			rgb[2] = 0x40;
		}
		else{
			rgb[0] = 0x99;
			rgb[1] = 0x26;
			rgb[2] = 0x67;
		}
	} 

}
