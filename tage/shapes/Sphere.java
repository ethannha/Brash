package tage.shapes;

import tage.*;
import org.joml.*;
import static java.lang.Math.*;

/**
* Sphere is described in Chapter 6 of Computer Graphics Programming in OpenGL with Java.
* It builds a sphere out of a stack of circles (rings).
* The number of vertices is specified at the time of construction.
* This is based on a single precision value that determines both the number of
* vertices per ring, and the number of rings in the sphere.
* Texturing expects an image that has been pre-distorted at the poles (such as typical planetary textures).
* The winding order is CCW.
* <p>
* The recommended way of constructing a Sphere is to use
* the constructor that allows the caller to specify the precision.
* The higher the precision, the smoother the curvature, but the larger
* the number of vertices and more overhead.
* @author Scott Gordon
*/
public class Sphere extends ObjShape
{
	private int numIndices, prec;
	private int[] indices;
	private Vector3f[] vertices;
	private Vector2f[] texCoords;
	private Vector3f[] normals;
	private Vector3f[] tangents;

	/** The no-argument contructor specifies a default precision of 48. */
	public Sphere()
	{	super();
		prec = 48;
		initSphere();
		loadVertexArrays();
	}

	/** precision is the number of vertices per slice, and the number of slices. Total number of vertices is precision*precision. */
	public Sphere(int precision)
	{	super();
		prec = precision;
		initSphere();
		loadVertexArrays();
	}

	private void initSphere()
	{	int numVertices = (prec+1) * (prec+1);
		super.setNumVertices(numVertices);
		numIndices = prec * prec * 6;
		indices = new int[numIndices];
		vertices = new Vector3f[numVertices];
		texCoords = new Vector2f[numVertices];
		normals = new Vector3f[numVertices];
		tangents = new Vector3f[numVertices];
		
		for (int i=0; i<numVertices; i++)
		{	vertices[i] = new Vector3f();
			texCoords[i] = new Vector2f();
			normals[i] = new Vector3f();
			tangents[i] = new Vector3f();
		}

		// calculate triangle vertices
		for (int i=0; i<=prec; i++)
		{	for (int j=0; j<=prec; j++)
			{	float y = (float)cos(toRadians(180-i*180/prec));
				float x = -(float)cos(toRadians(j*360/(float)prec))*(float)abs(cos(asin(y)));
				float z = (float)sin(toRadians(j*360/(float)prec))*(float)abs(cos(asin(y)));
				vertices[i*(prec+1)+j].set(x,y,z);
				texCoords[i*(prec+1)+j].set((float)j/prec, (float)i/prec);
				normals[i*(prec+1)+j].set(x,y,z);

				// calculate tangent vector
				if (((x==0) && (y==1) && (z==0)) || ((x==0) && (y==-1) && (z==0)))
				{	tangents[i*(prec+1)+j].set(0.0f, 0.0f, -1.0f);
				}
				else
				{	tangents[i*(prec+1)+j] = (new Vector3f(0,1,0)).cross(new Vector3f(x,y,z));
		}	}	}
		
		// calculate triangle indices
		for(int i=0; i<prec; i++)
		{	for(int j=0; j<prec; j++)
			{	indices[6*(i*prec+j)+0] = i*(prec+1)+j;
				indices[6*(i*prec+j)+1] = i*(prec+1)+j+1;
				indices[6*(i*prec+j)+2] = (i+1)*(prec+1)+j;
				indices[6*(i*prec+j)+3] = i*(prec+1)+j+1;
				indices[6*(i*prec+j)+4] = (i+1)*(prec+1)+j+1;
				indices[6*(i*prec+j)+5] = (i+1)*(prec+1)+j;
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
	protected Vector3f[] getTangentsVector() { return tangents; }
}