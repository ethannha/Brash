package tage.shapes;

import tage.*;
import org.joml.*;
import static java.lang.Math.*;

/**
* Torus is described in Chapter 6 of Computer Graphics Programming in OpenGL with Java.
* It is based heavily on an algorithm by Paul Baker.
* It builds a torus out of a series of rings that are placed at various rotations around the origin.
* The number of vertices is specified at the time of construction.
* This is based on a single precision value that determines both the number of
* vertices per ring, and the number of rings in the torus.
* The size of the inner hole, and radius of the Torus, are also specified at the time of construction.
* Texturing is done by evenly distributing the image on both halves of the Torus,
* and requires tiling to be enabled as 1 = GL_REPEAT to work properly.
* <p>
* The recommended way of constructing a Torus is to use
* the constructor that allows the caller to specify the precision,
* the size of the inner hole, and the radius of the Torus itself.
* The higher the precision, the smoother the curvature, but the larger
* the number of vertices.
* @author Scott Gordon
*/
public class Torus extends ObjShape
{
	private int numIndices, prec=48;
	private int[] indices;
	private Vector3f[] vertices;
	private Vector2f[] texCoords;
	private Vector3f[] normals;
	private float inner = 0.6f, outer = 0.25f;
	private Vector3f[] sTangents, tTangents;

	/** The no-parameter contructor specifies a default precision of 48, inner radius=0.6, outer radius=0.25. */
	public Torus()
	{	prec = 48;
		initTorus();
		loadVertexArrays();
	}

	/** parameters are inner radius, outer radius, and precision=48. */
	public Torus(float in, float out, int p)
	{	inner=in; outer=out; prec=p;
		initTorus();
		loadVertexArrays();
	}

	private void initTorus()
	{	int numVertices = (prec+1)*(prec+1);
		super.setNumVertices(numVertices);
		numIndices = prec * prec * 6;
		indices = new int[numIndices];
		vertices = new Vector3f[numVertices];
		texCoords = new Vector2f[numVertices];
		normals = new Vector3f[numVertices];
		sTangents = new Vector3f[numVertices];
		tTangents = new Vector3f[numVertices];
		for (int i=0; i<numVertices; i++)
		{	vertices[i] = new Vector3f();
			texCoords[i] = new Vector2f();
			normals[i] = new Vector3f();
			sTangents[i] = new Vector3f();
			tTangents[i] = new Vector3f();
		}

		// calculate first ring.
		for (int i=0; i<prec+1; i++)
		{	float amt = (float) toRadians(i*360.0f/prec);
		
			Vector3f initPos = new Vector3f(0.0f, outer, 0.0f);
			initPos.rotateAxis(amt, 0.0f, 0.0f, 1.0f);
			initPos.add(new Vector3f(inner, 0.0f, 0.0f));
			vertices[i].set(initPos);
			
			texCoords[i].set(0.0f, ((float)i)/((float)prec));

			tTangents[i] = new Vector3f(0.0f, -1.0f, 0.0f);
			tTangents[i].rotateAxis(amt+(3.14159f/2.0f), 0.0f, 0.0f, 1.0f);
			sTangents[i].set(0.0f, 0.0f, -1.0f);
			
			normals[i] = tTangents[i].cross(sTangents[i]);
		}

		//  rotate the first ring about Y to get the other rings
		for (int ring=1; ring<prec+1; ring++)
		{	for (int i=0; i<prec+1; i++)
			{	float amt = (float) toRadians((float)ring*360.0f/(prec));
				Vector3f vp = new Vector3f(vertices[i]);
				vp.rotateAxis(amt, 0.0f, 1.0f, 0.0f);
				vertices[ring*(prec+1)+i].set(vp);

				texCoords[ring*(prec+1)+i].set((float)ring*2.0f/(float)prec, texCoords[i].y());

				sTangents[ring*(prec+1)+i].set(sTangents[i]);
				sTangents[ring*(prec+1)+i].rotateAxis(amt, 0.0f, 1.0f, 0.0f);
				tTangents[ring*(prec+1)+i].set(tTangents[i]);
				tTangents[ring*(prec+1)+i].rotateAxis(amt, 0.0f, 1.0f, 0.0f);

				normals[ring*(prec+1)+i].set(normals[i]);
				normals[ring*(prec+1)+i].rotateAxis(amt, 0.0f, 1.0f, 0.0f);
			}
		}

		// calculate triangle indices
		for(int ring=0; ring<prec; ring++)
		{	for(int i=0; i<prec; i++)
			{	indices[((ring*prec+i)*2)  *3+0]= ring*(prec+1)+i;
				indices[((ring*prec+i)*2)  *3+1]=(ring+1)*(prec+1)+i;
				indices[((ring*prec+i)*2)  *3+2]= ring*(prec+1)+i+1;
				indices[((ring*prec+i)*2+1)*3+0]= ring*(prec+1)+i+1;
				indices[((ring*prec+i)*2+1)*3+1]=(ring+1)*(prec+1)+i;
				indices[((ring*prec+i)*2+1)*3+2]=(ring+1)*(prec+1)+i+1;
			}
		}
	}

	protected int getNumIndices() { return numIndices; }
	protected int[] getIndices() { return indices; }
	protected Vector3f[] getVerticesVector() { return vertices; }
	protected Vector2f[] getTexCoordsVector() { return texCoords; }
	protected Vector3f[] getNormalsVector() { return normals; }
	protected Vector3f[] getStangentsVector() { return sTangents; }
	protected Vector3f[] getTtangentsVector() { return tTangents; }

	private void loadVertexArrays()
	{	setNumVertices(this.getNumIndices());
		setVerticesIndexed(this.getIndices(), this.getVerticesVector());
		setTexCoordsIndexed(this.getIndices(), this.getTexCoordsVector());
		setNormalsIndexed(this.getIndices(), this.getNormalsVector());
		setMatAmb(Utils.goldAmbient());
		setMatDif(Utils.goldDiffuse());
		setMatSpe(Utils.goldSpecular());
		setMatShi(Utils.goldShininess());
		setWindingOrderCCW(true);
	}
}
