package tage;
import org.joml.*;
import java.util.*;
import tage.shapes.*;

/**
* ObjShape holds the raw vertex data for an object.
* <br>
* Specifically, it includes the following:
* <ul>
* <li> vertices, texture coordinates, and normals as float arrays
* <li> integer references to the OpenGL VBOs for vertices, texture coordinates, and normals
* <li> a boolean indicating whether the vertices are oriented in the standard CCW configuration
* <li> a boolean indicating whether this is an animated model
* <li> a primitive type, usually 3=triangle (1=point, 2=line, 3=triangle)
* <li> material ambient, diffuse, specular, and shininess properties as float arrays
* </ul>
* <p>
* Each GameObject as one associated ObjShape object.
* Several specific types of shapes extend ObjShape, such as Cube, Sphere, Imported Models, etc.
* The Game Application can also define Manual Objects that also extend ObjShape.
* All ObjShapes are loaded into VBOs during init().
* However, the building of GameObjects and assignments of ObjShapes to GameObjects can be done later.
* @author Scott Gordon
*/
public abstract class ObjShape
{
	private int numVertices;
	private float[] vertices, texCoords, normals, boneWeights, boneIndices;
	private int vertexBuffer, texCoordBuffer, normalBuffer, boneWeightBuffer, boneIndicesBuffer;
	private boolean hasWindingOrderCCW;
	private boolean isAnimated = false;
	private int primitiveType = 3; // 1=point, 2=line, 3=triangle(default)
	private float[] matAmb, matDif, matSpe;
	private float matShi;

	/** Instantiates an ObjShape and automatically adds it to the render system's list of shapes. */
	public ObjShape()
	{	matAmb = new float[4];
		matDif = new float[4];
		matSpe = new float[4];
		setMatAmb(Utils.defAmbient());
		setMatDif(Utils.defDiffuse());
		setMatSpe(Utils.defSpecular());
		setMatShi(Utils.defShininess());
		hasWindingOrderCCW = true;
		Engine.getEngine().getRenderSystem().addShape(this);
	}

	protected void setNumVertices(int n) { numVertices = n; }

	//------------- SETTERS FOR NON-INDEXED MODELS--------------------

	// Loads vertex data for non-indexed models.
	// Data is provided in an array of floats.

	protected void setVertices(float[] v)
	{	vertices = new float[v.length];
		for (int i=0; i<v.length; i++) { vertices[i] = v[i]; }
	}

	// Loads vertex data for non-indexed models.
	// Data is provided in an array of Vector3f.

	protected void setVertices(Vector3f[] v)
	{	vertices = new float[v.length*3];
		for (int i=0; i<numVertices; i++)
		{	vertices[i*3]   = (float) v[i].x();
			vertices[i*3+1] = (float) v[i].y();
			vertices[i*3+2] = (float) v[i].z();
		}
	}

	// Loads texture coordinates for non-indexed models.
	// Data is provided in an array of floats.

	protected void setTexCoords(float[] t)
	{	texCoords = new float[t.length];
		for (int i=0; i<t.length; i++) { texCoords[i] = t[i]; }
	}

	// Loads texture coordinates for non-indexed models.
	// Data is provided in an array of Vector3f.

	protected void setTexCoords(Vector2f[] t)
	{	texCoords = new float[t.length*2];
		for (int i=0; i<numVertices; i++)
		{	texCoords[i*2]   = (float) t[i].x();
			texCoords[i*2+1] = (float) t[i].y();
		}
	}

	// Loads normal vectors for non-indexed models.
	// Data is provided in an array of floats.

	protected void setNormals(float[] n)
	{	normals = new float[n.length];
		for (int i=0; i<n.length; i++) { normals[i] = n[i]; }
	}

	// Loads normal vectors for non-indexed models.
	// Data is provided in an array of Vector3f.

	protected void setNormals(Vector3f[] n)
	{	normals = new float[n.length*3];
		for (int i=0; i<numVertices; i++)
		{	normals[i*3]   = (float) n[i].x();
			normals[i*3+1] = (float) n[i].y();
			normals[i*3+2] = (float) n[i].z();
		}
	}

	// Loads bone weight vectors for non-indexed models.
	// Data is provided in an array of Vector3f.

	protected void setBoneWeights(Vector3f[] b)
	{	boneWeights = new float[b.length*3];
		for (int i=0; i<numVertices; i++)
		{	boneWeights[i*3]   = (float) b[i].x;
			boneWeights[i*3+1] = (float) b[i].y;
			boneWeights[i*3+2] = (float) b[i].z;
		}
	}

	// Loads bone index vectors for non-indexed models.
	// Data is provided in an array of Vector3f.

	protected void setBoneIndices(Vector3f[] b)
	{	boneIndices = new float[b.length*3];
		for (int i=0; i<numVertices; i++)
		{	boneIndices[i*3]   = (float) b[i].x;
			boneIndices[i*3+1] = (float) b[i].y;
			boneIndices[i*3+2] = (float) b[i].z;
		}
	}

	// -------------SETTERS FOR INDEXED MODELS -----------------

	// Loads vertex data for indexed models.
	// Data is provided in an array of Vector3f.
	// Data is then converted and stored as non-indexed.

	protected void setVerticesIndexed(int indices[], Vector3f[] v)
	{	vertices = new float[indices.length*3];
		for (int i=0; i<numVertices; i++)
		{	vertices[i*3]   = (float) (v[indices[i]]).x;
			vertices[i*3+1] = (float) (v[indices[i]]).y;
			vertices[i*3+2] = (float) (v[indices[i]]).z;
		}
	}

	// Loads texture coordinates for indexed models.
	// Data is provided in an array of Vector3f.
	// Data is then converted and stored as non-indexed.

	protected void setTexCoordsIndexed(int indices[], Vector2f[] t)
	{	texCoords = new float[indices.length*2];
		for (int i=0; i<numVertices; i++)
		{	texCoords[i*2]   = (float) (t[indices[i]]).x;
			texCoords[i*2+1] = (float) (t[indices[i]]).y;
		}
	}

	// Loads normal vectors for indexed models.
	// Data is provided in an array of Vector3f.
	// Data is then converted and stored as non-indexed.

	protected void setNormalsIndexed(int indices[], Vector3f[] n)
	{	normals = new float[indices.length*3];
		for (int i=0; i<numVertices; i++)
		{	normals[i*3]   = (float) (n[indices[i]]).x;
			normals[i*3+1] = (float) (n[indices[i]]).y;
			normals[i*3+2] = (float) (n[indices[i]]).z;
		}
	}
	
	// ------------------  ACCESSORS ---------------------

	// Setters with 3-element float array for R, G, B.

	/** sets the material ambient RGB characteristic for this ObjShape */
	public void setMatAmb(float[] ma) { matAmb[0]=ma[0]; matAmb[1]=ma[1]; matAmb[2]=ma[2]; matAmb[3]=1.0f; }

	/** sets the material diffuse RGB characteristic for this ObjShape */
	public void setMatDif(float[] md) { matDif[0]=md[0]; matDif[1]=md[1]; matDif[2]=md[2]; matDif[3]=1.0f; }

	/** sets the material specular RGB characteristic for this ObjShape */
	public void setMatSpe(float[] ms) { matSpe[0]=ms[0]; matSpe[1]=ms[1]; matSpe[2]=ms[2]; matSpe[3]=1.0f; }

	/** sets the material shininess for this ObjShape */
	public void setMatShi(float s) { matShi = s; }

	/** sets the winding order for this ObjShape - true for CCW, false for CW. */
	public void setWindingOrderCCW(boolean wo) { hasWindingOrderCCW = wo; }

	/** toggles the winding order for this ObjShape - if CCW changes to CW - if CW changes to CCW */
	public void toggleWindingOrder() { hasWindingOrderCCW = !hasWindingOrderCCW; }

	/** sets this ObjShape primitive type -- 1=point 2=line 3=triangle -- probably engine use only. */
	public void setPrimitiveType(int p) { primitiveType = p; }

	protected void setAnimated(boolean b) { isAnimated = b; }

	protected void setVertexBuffer(int b) { vertexBuffer = b; }
	protected void setTexCoordBuffer(int b) { texCoordBuffer = b; }
	protected void setNormalBuffer(int b) { normalBuffer = b; }
	protected void setBoneWeightBuffer(int b) { boneWeightBuffer = b; }
	protected void setBoneIndicesBuffer(int b) { boneIndicesBuffer = b; }

	/** engine use only. */
	public int getVertexBuffer() { return vertexBuffer; }
	/** engine use only. */
	public int getTexCoordBuffer() { return texCoordBuffer; }
	/** engine use only. */
	public int getNormalBuffer() { return normalBuffer; }
	/** engine use only. */
	public int getBoneWeightBuffer() { return boneWeightBuffer; }
	/** engine use only. */
	public int getBoneIndicesBuffer() { return boneIndicesBuffer; }

	// These are available for general use (and are also used by the engine)

	/** returns the number of vertices in this ObjShape */
	public int getNumVertices() { return numVertices; }

	/** returns a reference to the float array containing all of the vertices of this ObjShape */
	public float[] getVertices() { return vertices; }

	/** returns a reference to the float array containing all of the texture coordinates for this ObjShape */
	public float[] getTexCoords() { return texCoords; }

	/** returns a reference to the float array containing all of the normal vectors for this ObjShape */
	public float[] getNormals() { return normals; }

	/** returns a reference to the float array containing all of the bone weights for this ObjShape */
	public float[] getBoneWeights() { return boneWeights; }

	/** returns a reference to the float array containing all of the bone indices for this ObjShape */
	public float[] getBoneIndices() { return boneIndices; }

	/** returns a reference to the float array containing the material ambient RGB characteristic values for this ObjShape */
	public float[] getMatAmb() { return matAmb; }

	/** returns a reference to the float array containing the material diffuse RGB characteristic values for this ObjShape */
	public float[] getMatDif() { return matDif; }

	/** returns a reference to the float array containing the material specular RGB characteristic values for this ObjShape */
	public float[] getMatSpe() { return matSpe; }

	/** returns the material shininess for this ObjShape */
	public float getMatShi() { return matShi; }

	/** returns true if winding order is CCW */
	public boolean isWindingOrderCCW() { return hasWindingOrderCCW; }

	/** gets this ObjShape's primitive type -- 1=point 2=line 3=triangle -- probably engine use only */
	public int getPrimitiveType() { return primitiveType; }

	protected boolean isAnimated() { return isAnimated; }
}