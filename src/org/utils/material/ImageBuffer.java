package org.utils.material;

import java.net.*;

import java.awt.*;
import java.awt.image.*;

/*
 HOW TO USE THIS CLASS FROM AN APPLET:

 --- HERE'S WHAT YOU PUT INTO THE HTML FILE: ----

 &lt;applet code=myAmazingApplet.class width=500 height=500&gt;
 &lt;param name=image value=http://myAmazingUrl.com/myAmazingImage.jpg&gt;
 &lt;/applet&gt;

 --- HERE'S WHAT YOU PUT INTO THE APPLET JAVA CODE: ----

 String urlName = getParameter("image");
 buffer = new ImageBuffer(urlName, this);

 // HERE'S A REALLY TRIVIAL EXAMPLE OF SOMETHING TO DO WITH THE LOADED IMAGE.
 // YOU'LL PROBABLY WANT TO DO SOMETHING MORE INTERESTING - LIKE TEXTURE MAPPING.

 for (int y = 0 ; y &lt; buffer.height ; y++)
 for (int x = 0 ; x &lt; buffer.width  ; x++)
 pix[x + width * y] = buffer.get(x, y);
 */

public class ImageBuffer {
	public int pixels[] = null, width = 0, height = 0;

	// CONSTRUCT A NEW IMAGE BUFFER BY LOADING FROM A FILE OR URL

	public ImageBuffer(String fileName, java.applet.Applet applet) {

		// GRAB AN IMAGE FROM A URL ON THE WEB OR FROM A FILE ON DISK

		Image image = null;

		try {
			if (fileName.length() >= 4
					&& fileName.substring(0, 5).equals("http:"))
				image = applet.getImage(new URL(fileName));
			else
				image = Toolkit.getDefaultToolkit().getImage(
						new URL(applet.getCodeBase(), fileName));
		} catch (Exception e) {
			System.err.println(e);
		}

		// MOVE THE IMAGE INTO AN ARRAY OF PACKED R,G,B PIXELS

		try {
			MediaTracker mt = new MediaTracker(applet);
			mt.addImage(image, 1);
			mt.waitForAll();

			width = image.getWidth(applet);
			height = image.getHeight(applet);
			pixels = new int[width * height];
			System.out.println(width+" "+height);

			(new PixelGrabber(image, 0, 0, width, height, pixels, 0, width))
					.grabPixels();
		} catch (InterruptedException e) {
		}
	}

	// GET THE PACKED R,G,B VALUE AT ONE PIXEL OF THE IMAGE BUFFER

	public int get(int x, int y) {
		return pixels[x + width * y];
	}

	// GET A SINGLE COLOR COMPONENT - RED, GREEN OR BLUE - FROM A PIXEL

	public int get(int x, int y, int c) {
		return get(x, y) >> 16 - 8 * c & 255;
	}
}
