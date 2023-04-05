package tage.shapes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import org.joml.*;
import tage.*;

/**
* Supports importing OBJ model files into the game.
* <p>
* There are tight restrictions on which OBJ files are supported.
* They must have the following characteristics:
* <ul>
* <li> vertices must be triangulated (this importer doesn't support quads)
* <li> texture coordinates must be present (i.e., must be UV-unwrapped)
* <li> normal vectors must be present
* <li> must be a SINGLE object, not a composite of multiple objects
* <li> only v, vt, vn, and f tags are read - all other tags are ignored
* <li> all f tags must be of the form f xxx/xxx/xxx xxx/xxx/xxx xxx/xxx/xxx
* <li> associated material file is ignored (use the material accessor functions instead)
* </ul>
* <p>
* If you have a model that fails one of the above restrictions, you may need to load it into
* a tool such as Blender, and export it as an OBJ file that meets all of the above.
* <p>
* This tool is described in Chapter 6 of Computer Graphics Programming in OpenGL with Java.
* @author Scott Gordon
*/
public class ImportedModel extends ObjShape
{
	private Vector3f[] verticesV;
	private Vector2f[] texCoordsV;
	private Vector3f[] normalsV;
	private int numVerts;

	/** Use this constructor to read in an OBJ file with the specified file name. */
	public ImportedModel(String filename)
	{	super();
		ModelImporter modelImporter = new ModelImporter();
		try
		{	modelImporter.parseOBJ("assets/models/" + filename);
			numVerts      = modelImporter.getNumVertices();
			super.setNumVertices(numVerts);
			float[] verts = modelImporter.getVertices();
			float[] tcs   = modelImporter.getTextureCoordinates();
			float[] norm  = modelImporter.getNormals();

			verticesV  = new Vector3f[numVerts];
			texCoordsV = new Vector2f[numVerts];
			normalsV   = new Vector3f[numVerts];
			
			for(int i=0; i<verticesV.length; i++)
			{	verticesV[i] = new Vector3f();
				verticesV[i].set(verts[i*3], verts[i*3+1], verts[i*3+2]);
				texCoordsV[i] = new Vector2f();
				texCoordsV[i].set(tcs[i*2], tcs[i*2+1]);
				normalsV[i] = new Vector3f();
				normalsV[i].set(norm[i*3], norm[i*3+1], norm[i*3+2]);
			}
		} catch (IOException e)
		{ e.printStackTrace();
		}
		setNumVertices(this.getNumVertices());
		setVertices(this.getVerticesVector());
		setTexCoords(this.getTexCoordsVector());
		setNormals(this.getNormalsVector());
		setWindingOrderCCW(true);
	}

	// these methods are for engine use only
	protected Vector3f[] getVerticesVector() { return verticesV; }
	protected Vector2f[] getTexCoordsVector() { return texCoordsV; }
	protected Vector3f[] getNormalsVector() { return normalsV; }	

	private class ModelImporter
	{	// values as read from OBJ file
		private ArrayList<Float> vertVals = new ArrayList<Float>();
		private ArrayList<Float> triangleVerts = new ArrayList<Float>(); 
		private ArrayList<Float> textureCoords = new ArrayList<Float>();

		// values stored for later use as vertex attributes
		private ArrayList<Float> stVals = new ArrayList<Float>();
		private ArrayList<Float> normals = new ArrayList<Float>();
		private ArrayList<Float> normVals = new ArrayList<Float>();

		protected void parseOBJ(String filename) throws IOException
		{	InputStream input = new FileInputStream(new File(filename));
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String line;
			while ((line = br.readLine()) != null)
			{	if(line.startsWith("v "))			// vertex position ("v" case)
				{	for(String s : (line.substring(2)).split(" "))
					{	vertVals.add(Float.valueOf(s));
				}	}
				else if(line.startsWith("vt"))			// texture coordinates ("vt" case)
				{	for(String s : (line.substring(3)).split(" "))
					{	stVals.add(Float.valueOf(s));
				}	}
				else if(line.startsWith("vn"))			// vertex normals ("vn" case)
				{	for(String s : (line.substring(3)).split(" "))
					{	normVals.add(Float.valueOf(s));
				}	}
				else if(line.startsWith("f"))			// triangle faces ("f" case)
				{	for(String s : (line.substring(2)).split(" "))
					{	String v = s.split("/")[0];
						String vt = s.split("/")[1];
						String vn = s.split("/")[2];
	
						int vertRef = (Integer.valueOf(v)-1)*3;
						int tcRef   = (Integer.valueOf(vt)-1)*2;
						int normRef = (Integer.valueOf(vn)-1)*3;
	
						triangleVerts.add(vertVals.get(vertRef));
						triangleVerts.add(vertVals.get((vertRef)+1));
						triangleVerts.add(vertVals.get((vertRef)+2));

						textureCoords.add(stVals.get(tcRef));
						textureCoords.add(stVals.get(tcRef+1));
	
						normals.add(normVals.get(normRef));
						normals.add(normVals.get(normRef+1));
						normals.add(normVals.get(normRef+2));
			}	}	}
			input.close();
		}

		protected int getNumVertices() { return (triangleVerts.size()/3); }

		protected float[] getVertices()
		{	float[] p = new float[triangleVerts.size()];
			for(int i = 0; i < triangleVerts.size(); i++)
			{	p[i] = triangleVerts.get(i);
			}
			return p;
		}

		protected float[] getTextureCoordinates()
		{	float[] t = new float[(textureCoords.size())];
			for(int i = 0; i < textureCoords.size(); i++)
			{	t[i] = textureCoords.get(i);
			}
			return t;
		}
	
		protected float[] getNormals()
		{	float[] n = new float[(normals.size())];
			for(int i = 0; i < normals.size(); i++)
			{	n[i] = normals.get(i);
			}
			return n;
		}	
	}
}
