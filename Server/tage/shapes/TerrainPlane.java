package tage.shapes;

import tage.*;
import org.joml.*;
import static java.lang.Math.*;

/**
* TerrainPlane is a plane with a large number of vertices, intended for height mapping.
* Height mapping is low-precision because it is done in the vertex shader.
* <p>
* The number of vertices in the plane is based on the precision value specified
* in the constructor, where the number of vertices = precision * precision.
* If the no-argument constructor is called, precision is set to 100 (leading to 10,000 vertices).
* The higher the precision, the better the terrain quality, but also more overhead if the
* number of vertices is large.
* <p>
* If a flat plane is desired, use Plane instead.
* The winding order is CCW.
* @author Scott Gordon
*/
public class TerrainPlane extends ObjShape
{
	private int numIndices, prec;
	private int[] indices;
	private Vector3f[] vertices;
	private Vector2f[] texCoords;
	private Vector3f[] normals;

	/** sets default precision of 100, or 10,000 vertices. */
	public TerrainPlane()
	{	super();
		prec = 100;
		initTerrainPlane();
		loadVertexArrays();
	}

	/** precision is number of vertices along each axis. Total number of vertices is precision*precision. */
	public TerrainPlane(int precision)
	{	super();
		prec = precision;
		initTerrainPlane();
		loadVertexArrays();
	}

	private void initTerrainPlane()
	{	int numVertices = prec * prec;
		super.setNumVertices(numVertices);
		numIndices = (prec-1) * (prec-1) * 6;
		indices = new int[numIndices];
		vertices = new Vector3f[numVertices];
		texCoords = new Vector2f[numVertices];
		normals = new Vector3f[numVertices];
		
		for (int i=0; i<numVertices; i++)
		{	vertices[i] = new Vector3f();
			texCoords[i] = new Vector2f();
			normals[i] = new Vector3f();
		}

		// calculate triangle vertices
		float spacing = 1.0f/(float)(prec-1);
		for (int i=0; i<prec; i++)
		{	for (int j=0; j<prec; j++)
			{	vertices[i*prec+j].set(i*2.0f*spacing-1.0f, 0f, j*2.0f*spacing-1.0f);
				texCoords[i*prec+j].set(i*spacing, 1.0-j*spacing);
				normals[i*prec+j].set(0,1,0);
		}	}
		
		// calculate triangle indices
		for(int i=0; i<prec-1; i++)
		{	for(int j=0; j<prec-1; j++)
			{	indices[6*(i*(prec-1)+j)+0] = i*prec+j;
				indices[6*(i*(prec-1)+j)+1] = i*prec+j+1;
				indices[6*(i*(prec-1)+j)+2] = (i+1)*prec+j;
				indices[6*(i*(prec-1)+j)+3] = i*prec+j+1;
				indices[6*(i*(prec-1)+j)+4] = (i+1)*prec+j+1;
				indices[6*(i*(prec-1)+j)+5] = (i+1)*prec+j;
	}	}	}

	private void loadVertexArrays()
	{	setNumVertices(this.getNumIndices());
		setVerticesIndexed(this.getIndices(), this.getVerticesVector());
		setTexCoordsIndexed(this.getIndices(), this.getTexCoordsVector());
		setNormalsIndexed(this.getIndices(), this.getNormalsVector());
		setWindingOrderCCW(true);
	}

	protected int getNumIndices() { return numIndices; }
	protected int[] getIndices() { return indices; }
	protected Vector3f[] getVerticesVector() { return vertices; }
	protected Vector2f[] getTexCoordsVector() { return texCoords; }
	protected Vector3f[] getNormalsVector() { return normals; }
}