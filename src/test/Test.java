package test;

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
public class Test extends MISApplet {
	ArrayList<RenderPolygons> scene;
	Projection proj = new Projection(W, H, 15.0);

	Geometry joint[][] = new Geometry[4][3];
	Geometry dactylus[][] = new Geometry[4][3];
	Geometry root = new ShapeNull();
	Geometry hand = new ShapeCylinder();

	Geometry ball = new ShapeSphere();

	double speedCirclePerSecond = 0.1;
	int frameCount = 0;
	double alpha;

	double[] eye = new double[] { 0, 0, 1.0 };

	double[] AColor = { 0.2, 0, 0 };
	double[] DColor = { 0.8, 0, 0 };
	double[] SColor = { 1.0, 1.0, 1.0 };
	double power = 10.0;
	double[] AColor1 = { 0.1, 0.05, 0.05 };
	double[] DColor1 = { 0.82, 0.5, 0.44 };
	double[] SColor1 = { 0.3, 0.3, 0.3 };
	double power1 = 10.0;

	public void create() {
		root = new ShapeNull();
		hand = new ShapeCube();
		root.getMatrix().identity();
		hand.getMatrix().identity();
		ball.getMatrix().identity();

		root.addChild(hand);
		hand.globe(20, 20);
		ball.globe(20, 20);

		hand.getMatrix().scale(2, 2, 2);
		hand.getMatrix().translate(3, 0, 0);
	}

	@Override
	public void initialize() {

		proj = new Projection(W, H, 15.0);
		alpha = 0;
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
				zBuffer[j * W + i] = -15.0;
			}
		}

		root.getMatrix().rotateY(Math.PI/2);
		
		Geometry.renderFromRoot(root, scene, proj);
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
		int rgb[] = new int[3];
		int color = 0;
		int i = 0;
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) { // COMPUTE COLOR FOR EACH PIXEL
				setPixel(x, y, rgb);
				pix[i++] = pack(rgb[0], rgb[1], rgb[2]);
			}
		}
		color = pack(0xd2, 0xb1, 0x6f);
		for (int j = 0; j < scene.size(); j++) {
			
			scene.get(j).colorPolygon(pix, color, W, zBuffer);
		}
	}
}
