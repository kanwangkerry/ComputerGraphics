package org.utils;

import java.awt.*;

@SuppressWarnings("serial")
public abstract class BufferedApplet extends java.applet.Applet implements
		Runnable {
	// YOU MUST DEFINE A METHOD TO RENDER THE APPLET

	public abstract void render(Graphics g);

	// A BACKGROUND THREAD CALLS REPAINT EVERY 30 MILLISECONDS,

	public void start() {
		if (t == null)
			(t = new Thread(this)).start();
	}

	public void run() {
		try {
			while (true) {
				repaint();
				Thread.sleep(30);
			}
		} catch (Exception e) {
		}
		;
	}


	public void update(Graphics g) {
		if (width != getWidth() || height != getHeight()) {
			image = createImage(width = getWidth(), height = getHeight());
			buffer = image.getGraphics();
		}
		render(buffer);
		g.drawImage(image, 0, 0, this);
	}

	private Thread t;
	private Image image;
	private Graphics buffer;
	private int width = 0, height = 0;
}
