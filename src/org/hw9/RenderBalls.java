package org.hw9;

import java.util.ArrayList;

import org.utils.MISApplet;
import org.utils.material.Light;
import org.utils.raytracing.RayTracing;
import org.utils.render.RenderPolygons;
import org.utils.shape.Geometry;
import org.utils.shape.ShapeCube;
import org.utils.shape.ShapeNull;
import org.utils.shape.ShapeSphere;
import org.utils.transform.Projection;

@SuppressWarnings("serial")
public class RenderBalls extends MISApplet {
	ArrayList<RenderPolygons> scene;
	Projection proj;
	Geometry root = new ShapeNull();

	Geometry ball = new ShapeSphere();
	Geometry ball1 = new ShapeSphere();
	Geometry ball2 = new ShapeSphere();
	
	double speedCirclePerSecond = 0.1;
	int frameCount = 0;
	double alpha;

	Light[] l = new Light[2];
	double[] eye = new double[] { 0, 0, 1.0 };

	double[] AColor = { 0.2, 0, 0 };
	double[] DColor = { 0.8, 0, 0 };
	double[] SColor = { 1.0, 1.0, 1.0 };
	double power = 10.0;
	double[] AColor1 = { 0.1, 0.05, 0.05 };
	double[] DColor1 = { 0.82, 0.5, 0.44 };
	double[] SColor1 = { 0.3, 0.3, 0.3 };
	double power1 = 10.0;

	double F;

	public void create() {
		root = new ShapeNull();
		root.getMatrix().identity();
		root.addChild(ball);
		root.addChild(ball1);
		root.addChild(ball2);
		
		ball.getMatrix().identity();
		ball.globe(30, 30);

		ball.getMatrix().translate(0, 0, 0);
		ball.getMatrix().scale(.1, .1, .1);
		
		ball1.getMatrix().identity();
		ball1.globe(30, 30);

		ball1.getMatrix().translate(0, .15, .15);
		ball1.getMatrix().scale(.1, .1, .1);
		
		ball2.getMatrix().identity();
		ball2.globe(30, 30);

		ball2.getMatrix().translate(0, -.15, -.15);
		ball2.getMatrix().scale(.1, .1, .1);

		Geometry.setMaterialFromRoot(root, AColor1, DColor1, SColor1, power);
		Geometry.setMaterialFromRoot(ball, AColor, DColor, SColor, power);
	}

	@Override
	public void initialize() {
		F = 1;

		proj = new Projection(W, H, F);
		alpha = 0;

		l[0] = new Light();
		l[0].setLightDir(0.2, 1.0, .2);
		l[0].setLightColor(1.0, 1.0, 1.0);
		l[1] = new Light();
		l[1].setLightDir(-0.2, -1.0, -.2);
		l[1].setLightColor(1.0, 1.0, 1.0);
	}

	@Override
	public void initFrame(double time) {

		scene = new ArrayList<RenderPolygons>();
		alpha = speedCirclePerSecond * 2 * Math.PI
				* ((frameCount) / (double) 20);
		frameCount = (frameCount + 1 + 200) % 200;
		this.create();

		for (int i = 0; i < W; i++) {
			for (int j = 0; j < H; j++) {
				zBuffer[j * W + i] = -F;
			}
		}

		 root.getMatrix().translate(0, 0, 0);
		// root.getMatrix().scale(1.8, 1.8, 1.8);
		root.getMatrix().rotateY(alpha);
		Geometry.renderFromRoot(root, scene, proj, l, eye);
	}

	@Override
	public void setPixel(int x, int y, int rgb[]) {
		rgb[0] = 0x99;
		rgb[1] = 0x26;
		rgb[2] = 0x67;
	}

	@Override
	public void computeImage(double time) {
		initFrame(time); // INITIALIZE COMPUTATION FOR FRAME
		int rgb[] = new int[]{0x99, 0x26, 0x67};
		int i = 0;
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) { // COMPUTE COLOR FOR EACH PIXEL
				pix[i++] = pack(rgb[0], rgb[1], rgb[2]);
			}
		}

		RayTracing rt = new RayTracing();
		rt.buildPainter(root, scene, W, H, F);
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				rt.rayTraceRender(x, y, W, H, F, pix);
			}
		}
//		int color;
//		color = pack(0xd2, 0xb1, 0x6f);
//		for (int j = 0; j < scene.size(); j++) {
//			scene.get(j).colorPolygon(pix, color, W, zBuffer);
//		}
	}
}