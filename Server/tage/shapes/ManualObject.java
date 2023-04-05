package tage.shapes;

import tage.*;
import org.joml.*;

/**
* Abstract class for user-defined manual objects.
* <p>
* A manual object is one in which the user has manually specified the
* vertex locations, texture coordinates, and normal vectors for the object.
* When defining a custom manual object, the class must extend ManualObject, and
* provide a constructor that does the following steps, in this order:
* <OL>
* <LI> call super()
* <LI> set the number of vertices using setVertices()
* <LI> generate fields for vertices, texture coordinates, and normal vectors (either float arrays or arrays of Vector3f)
* <LI> load them into the Shape arrays using the appropriate setters
* <LI> set the winding order if other than CCW, and primitive type if other than 3 (triangles)
* </OL>
* @author Scott Gordon
*/
public abstract class ManualObject extends ObjShape
{
	public ManualObject()
	{	super();
	}

	//------------- PUBLIC SETTERS FOR NON-INDEXED MODELS--------------------

	/** Loads vertex data for non-indexed models, where data is provided in an array of floats. */
	public void setVertices(float[] v) { super.setVertices(v); }

	/** Loads vertex data for non-indexed models, where data is provided in an array of Vector3f. */
	public void setVertices(Vector3f[] v) { super.setVertices(v); }

	/** Loads texture coordinates for non-indexed models, where data is provided in an array of floats. */
	public void setTexCoords(float[] t) { super.setTexCoords(t); }

	/** Loads texture coordinates for non-indexed models, where data is provided in an array of Vector3f. */
	public void setTexCoords(Vector2f[] t) { super.setTexCoords(t); }

	/** Loads normal vectors for non-indexed models, where data is provided in an array of floats. */
	public void setNormals(float[] n) { super.setNormals(n); }

	/** Loads normal vectors for non-indexed models, where data is provided in an array of Vector3f. */
	public void setNormals(Vector3f[] n) { super.setNormals(n); }

	// ------------- PUBLIC SETTERS FOR INDEXED MODELS -----------------

	/** Loads vertex data for indexed models, where data is provided in an array of Vector3f.
	* Data is then converted and stored as non-indexed.
	*/
	public void setVerticesIndexed(int indices[], Vector3f[] v)
	{	super.setVerticesIndexed(indices, v);
	}

	/** Loads texture coordinates for indexed models, where data is provided in an array of Vector3f.
	* Data is then converted and stored as non-indexed.
	*/
	public void setTexCoordsIndexed(int indices[], Vector2f[] t)
	{	super.setTexCoordsIndexed(indices, t);
	}

	/** Loads normal vectors for indexed models, where data is provided in an array of Vector3f.
	* Data is then converted and stored as non-indexed.
	*/
	public void setNormalsIndexed(int indices[], Vector3f[] n)
	{	super.setNormalsIndexed(indices, n);
	}
}