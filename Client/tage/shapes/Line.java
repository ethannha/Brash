package tage.shapes;
import tage.*;
import org.joml.*;

/**
* Draws a line segment connecting two specified start and end locations.
* By default it has solid color and no lighting.
* The color can be specified in the associated RenderStates.
* @author Scott Gordon
*/
public class Line extends ObjShape
{
	float[] vertices = new float[6];
	float[] texCoords = new float[]	{ 0f, 0f, 0f, 0f, 0f, 0f };
	float[] normals = new float[] { 0f, 0f, 0f, 0f, 0f, 0f };

	/** Creates a line segment connecting specified start and end locations. */
	public Line(Vector3f start, Vector3f end)
	{	super();
		setNumVertices(2);
		setPrimitiveType(2);
		vertices[0] = start.x();
		vertices[1] = start.y();
		vertices[2] = start.z();
		vertices[3] = end.x();
		vertices[4] = end.y();
		vertices[5] = end.z();
		setVertices(vertices);
		setTexCoords(texCoords);
		setNormals(normals);
	}
}