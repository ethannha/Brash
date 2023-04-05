package tage.shapes;
import tage.*;

/**
* Plane is a rectangular object located on the XZ plane.
* It is comprised of two triangles, and therefore requires only 6 vertices.
* Its normal vectors all point upwards, and it has CCW winding order.
* It is suitable for a flat ground plane or floor (i.e., without height mapping) or can be rotated for other uses.
* <p>
* Plane is not suitable for terrain, because there are an insufficient
* number of vertices for height-mapping.  If terrain is desired, use TerrainPlane.
* @author Scott Gordon
*/
public class Plane extends ObjShape
{
	float[] vertices =
	{	-1.0f, 0.0f, 1.0f,
		1.0f, 0.0f, 1.0f,
		-1.0f, 0.0f, -1.0f,

		-1.0f, 0.0f, -1.0f,
		1.0f, 0.0f, 1.0f,
		1.0f, 0.0f, -1.0f
	};
	float[] texCoords = new float[]
	{	0.0f, 0.0f,  1.0f, 0.0f,  0.0f, 1.0f,
		0.0f, 1.0f,  1.0f, 0.0f,  1.0f, 1.0f
	};
	float[] normals = new float[]
	{	0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,
		0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f
	};

	/** Creates a flat rectangular object that defaults to the XZ plane. */ 
	public Plane()
	{	setNumVertices(6);
		setVertices(vertices);
		setTexCoords(texCoords);
		setNormals(normals);
		setMatAmb(Utils.silverAmbient());
		setMatDif(Utils.silverDiffuse());
		setMatSpe(Utils.silverSpecular());
		setMatShi(Utils.silverShininess());
		setWindingOrderCCW(true);
	}
}