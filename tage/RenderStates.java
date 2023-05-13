package tage;
import org.joml.*;

/**
* Holds all render states for a given object.
* Each game object should have an instantiated RenderStates object associated with it.
* <ul>
* <li> enable rendering this object (or not)
* <li> render this object with or without lighting
* <li> render this object with or without depth testing
* <li> render this object with transparency (not yet implemented)
* <li> utilize OpenGL texture tiling - options are:  0=none, 1=repeat, 2=mirroredRepeat, 3=clampToEdge
* <li> set primitive -- 1=point, 2=line, 3=triangle (default)
* <li> render this object with a solid, specified color
* <li> render this object in wireframe
* <li> render hidden faces (if need to view from the inside)
* <li> enable environment mapping (to make a "chrome-like" object)
* <li> adjust for an incorrectly-aligned OBJ or RKM model
* </ul>
* @author Scott Gordon
*/
public class RenderStates
{
	private boolean enableRendering = true;
	private boolean hasLighting = true;
	private boolean hasDepthTesting = true;
	private boolean isTransparent = false;
	private int tiling = 0; // 0=none, 1=repeat, 2=mirroredRepeat, 3=clampToEdge
	private int primitive = 3;  // 1=point, 2=line, 3=triangle
	private boolean solidColor = false;
	private Vector3f color = new Vector3f(1f,0f,0f);
	private boolean wireframe = false;
	private boolean renderHiddenFaces = false;
	private boolean isEnvironmentMapped = false;
	private Matrix4f modelOrientationCorrection = new Matrix4f();

	//---------------- ACCESSORS ---------------------

	/** enables rendering this object */
	public void enableRendering() { enableRendering = true; }

	/** disables rendering this object */
	public void disableRendering() { enableRendering = false; }

	/** sets whether or not this object responds to lighting */
	public void hasLighting(boolean h) { hasLighting = h; }

	/** sets whether or not this object participates in depth testing */
	public void hasDepthTesting(boolean h) { hasDepthTesting = h; }

	/** sets whether or not this object is transparent (not yet implemented) */
	public void isTransparent(boolean i) { isTransparent = i; }

	/** sets whether or not this object is environment mapped (simulates chrome) */
	public void isEnvironmentMapped(boolean i) { isEnvironmentMapped = i; }

	/** sets whether or not this object is rendered in wireframe mode */
	public void setWireframe(boolean w) { wireframe = w; }

	/** specifies how to texture when texcoords are outside [0 to 1] -- 0=none, 1=repeat, 2=mirroredRepeat, 3=clampToEdge */
	public void setTiling(int t) { tiling = t; }

	/** 1=point, 2=line, 3=triangle (default) -- set by the engine. */
	public void setPrimitive(int p) { primitive = p; }

	/** most useful for lines and wireframe. */
	public void setHasSolidColor(boolean sc) { solidColor = true; }

	/** the color to use when solidColor also set. */
	public void setColor(Vector3f c) { color = new Vector3f(c); }

	/** useful if camera can go inside the object. */
	public void setRenderHiddenFaces(boolean r) { renderHiddenFaces = r; }

	/** apply a rotation without including it in the local or world transforms */
	public void setModelOrientationCorrection(Matrix4f r) { modelOrientationCorrection = new Matrix4f(r); }

	/** returns a boolean that is true if rendering is enabled for this object */
	public boolean renderingEnabled() { return enableRendering; }

	/** returns a boolean that is true if this object responds to lighting */
	public boolean hasLighting() { return hasLighting; }

	/** returns a boolean that is true if depth testing is enabled for this object - mostly for skyboxes */
	public boolean hasDepthTesting() { return hasDepthTesting; }

	/** returns a boolean that is true if this object is transparent (not yet implemented) */
	public boolean isTransparent() { return isTransparent; }

	/** returns a boolean that is true if this object is environment mapped (simulated chrome) */	
	public boolean isEnvironmentMapped() { return isEnvironmentMapped; }

	/** returns an int specifying the texture behavior when texcoords exceed the range [0 to 1] -- 0=none, 1=repeat, 2=mirroredRepeat, 3=clampToEdge */
	public int getTiling() { return(tiling); }

	/** returns an int specifying the primitives type for this object -- 1=point, 2=line, 3=triangle */
	public int getPrimitive() { return(primitive); }

	/** returns a boolean that is true if this object is rendered with a solid specified color */
	public boolean hasSolidColor() { return solidColor; }

	/** returns a reference to the Vector3f that contains RGB values for this object's color if it is solid color */
	public Vector3f getColor() { return color; }

	/** returns a boolean that is true if the object has been specified to render in wireframe */
	public boolean isWireframe() { return wireframe; }

	/** returns a boolen that is true if the object has been specified to render hidden faces */
	public boolean willRenderHiddenFaces() { return renderHiddenFaces; }

	/** returns a copy of the matrix that contains the model orientation correction, if one has been specified */
	public Matrix4f getModelOrientationCorrection() { return new Matrix4f(modelOrientationCorrection); }
}