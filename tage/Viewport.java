package tage;

import com.jogamp.opengl.awt.GLCanvas;
import java.awt.Point;

/**
* One viewport covering a portion of the GLCanvas, and an associated camera.
* A viewport is defined by specifying its left, bottom, width, and height values.
* Each viewport is identified by a String name.
* By default, the engine creates a single viewport named "MAIN".
* <p>
* The game application should NOT instantiate viewports directly.
* Instead, it should use the addViewport() function in RenderSystem.
* @author Scott Gordon
*/

public class Viewport
{	
	private Camera camera;
	private Engine engine;

	// these are floats ranging from 0.0 to 1.0
	private float relativeLeft;
	private float relativeBottom;
	private float relativeWidth;
	private float relativeHeight;
	private boolean hasBorder = false;
	private int borderWidth = 0;
	private float borderColor[] = { 1.0f, 0.0f, 0.0f };

	protected Viewport(String n, Engine e, float left, float bottom, float width, float height)
	{	relativeLeft = left;
		relativeBottom = bottom;
		relativeWidth = width;
		relativeHeight = height;
		camera = new Camera();
		engine = e;
	}

	/** returns a reference to the camera assigned to this viewport. */
	public Camera getCamera() { return camera; }

	// these return values from 0.0 to 1.0 representing fraction of canvas space

	/** returns the left-side relative position of this viewport in the window, as a value between 0 and 1 */
	public float getRelativeLeft() { return relativeLeft; }

	/** returns the bottom relative position of this viewport in the window, as a value between 0 and 1 */
	public float getRelativeBottom() { return relativeBottom; }

	/** returns the relative width of this viewport in the window, as a value between 0 and 1 */
	public float getRelativeWidth() { return relativeWidth; }

	/** returns the relative height of this viewport in the window, as a value between 0 and 1 */
	public float getRelativeHeight() { return relativeHeight; }

	/** returns the actual left-side (x axis) position of this viewport */
	public float getActualLeft()
	{	RenderSystem rs = engine.getRenderSystem();
		GLCanvas canvas = rs.getGLCanvas();
		Point loc = canvas.getLocationOnScreen();
		float left = (float) loc.getX();
		float width = canvas.getWidth();
		return left + width * relativeLeft;
	}

	/** returns the actual bottom (y axis) position of this viewport */
	public float getActualBottom()
	{	RenderSystem rs = engine.getRenderSystem();
		GLCanvas canvas = rs.getGLCanvas();
		Point loc = canvas.getLocationOnScreen();
		float top = (float) loc.getY();
		float height = canvas.getHeight();
		return top + getActualHeight();
	}

	/** returns the actual width of this viewport, in pixels */
	public float getActualWidth()
	{	RenderSystem rs = engine.getRenderSystem();
		GLCanvas canvas = rs.getGLCanvas();
		return canvas.getWidth() * relativeWidth;
	}

	/** returns the actual height of this viewport, in pixels */
	public float getActualHeight()
	{	RenderSystem rs = engine.getRenderSystem();
		GLCanvas canvas = rs.getGLCanvas();
		return canvas.getHeight() * relativeHeight;
	}

	/** returns a boolean that is true if a border has been specified for this viewport */
	public boolean getHasBorder() { return hasBorder; }

	/** returns the border width for this viewport, if a border has been specified */
	public int getBorderWidth() { return borderWidth; }

	/** returns a reference to the float array containing the border color of this viewport, if a border has been specified */
	public float[] getBorderColor() { return borderColor; }

	/** sets whether or not this viewport should display a border */
	public void setHasBorder(boolean b) { hasBorder = b; }

	/** sets the width of a border for this viewport, if a border has been enabled */
	public void setBorderWidth(int w) { borderWidth = w; }

	/** sets RGB color values for the border of this viewport, if a border has been enabled */
	public void setBorderColor(float r, float g, float b)
	{	borderColor[0] = r;
		borderColor[1] = g;
		borderColor[2] = b;
	}
}