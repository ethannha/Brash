package tage;
import tage.Light.*;
import java.nio.*;
import java.util.*;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.common.nio.Buffers;

/**
* Manages storage of Lights to facilitate sending them to shaders.
* <p>
* Handling an arbitrary number of lights is complicated because all lights need to be sent to
* each shader invocation, each time an object is rendered.  This is facilitated using an OpenGL SSBO
* (Shader Storage Buffer Object). Converting the array of light objects to an SSBO requires
* first converting the array of Light objects to a float array, then to a java direct FloatBuffer,
* and then finally to an SSBO.
* <p>
* This class manages all four of those storages.
* Each time a light is modified, the FloatBuffer needs to be updated.
* At each frame, the FloatBuffer is reloaded into the SSBO.
* <p>
* Although a few accessors have been made public, there shouldn't be any reason for a game
* application to interact with the LightManager.  A game application should instatiate and
* modify Light objects directly, and insert them into the game via the SceneGraph addLight() method.
* @author Scott Gordon
*/
public class LightManager
{	private GLCanvas myCanvas;
	private Engine engine;

	private ArrayList<Light> lights = new ArrayList<Light>();
	private float[] lightArray;
	private FloatBuffer lightBuf;
	private int[] lightSSBO = new int[1];

	private int fieldsPerLight = 22;

	protected LightManager(Engine e)
	{	engine = e;
	}

	protected void addLight(Light light)
	{	lights.add(light);
		light.setIndex(lights.size()-1);
	}

	/** returns a reference to the ith Light - not likely to be useful in the game application. */
	public Light getLight(int i) { return lights.get(i); }

	/** returns the number of lights currently in the game */
	public int getNumLights() { return lights.size(); }

	/** Used by the renderer - not likely to be useful in the game application. */
	public int getFieldsPerLight() { return fieldsPerLight; }

	protected FloatBuffer getLightBuffer() { return lightBuf; }
	protected float[] getLightArray() { return lightArray; }

	/** for engine use only, returns a reference to the SSBO containing the data for all of the lights  */
	public int getLightSSBO() { return lightSSBO[0]; }

	// These functions update the light information in both the FloatArray and the FloatBuffer
	protected void updateLightLocation(int which, float x, float y, float z)
	{	lightArray[which * fieldsPerLight + 0] = x;
		lightArray[which * fieldsPerLight + 1] = y;
		lightArray[which * fieldsPerLight + 2] = z;
		lightBuf.put(which * fieldsPerLight + 0, x);
		lightBuf.put(which * fieldsPerLight + 1, y);
		lightBuf.put(which * fieldsPerLight + 2, z);
	}
	protected void updateLightAmbient(int which, float r, float g, float b)
	{	lightArray[which * fieldsPerLight + 3] = r;
		lightArray[which * fieldsPerLight + 4] = g;
		lightArray[which * fieldsPerLight + 5] = b;
		lightBuf.put(which * fieldsPerLight + 3, r);
		lightBuf.put(which * fieldsPerLight + 4, g);
		lightBuf.put(which * fieldsPerLight + 5, b);
	}
	protected void updateLightDiffuse(int which, float r, float g, float b)
	{	lightArray[which * fieldsPerLight + 6] = r;
		lightArray[which * fieldsPerLight + 7] = g;
		lightArray[which * fieldsPerLight + 8] = b;
		lightBuf.put(which * fieldsPerLight + 6, r);
		lightBuf.put(which * fieldsPerLight + 7, g);
		lightBuf.put(which * fieldsPerLight + 8, b);
	}
	protected void updateLightSpecular(int which, float r, float g, float b)
	{	lightArray[which * fieldsPerLight + 9] = r;
		lightArray[which * fieldsPerLight + 10] = g;
		lightArray[which * fieldsPerLight + 11] = b;
		lightBuf.put(which * fieldsPerLight + 9, r);
		lightBuf.put(which * fieldsPerLight + 10, g);
		lightBuf.put(which * fieldsPerLight + 11, b);
	}
	protected void updateConstantAttenuation(int which, float ca)
	{	lightArray[which * fieldsPerLight + 12] = ca;
		lightBuf.put(which * fieldsPerLight + 12, ca);
	}
	protected void updateLinearAttenuation(int which, float la)
	{	lightArray[which * fieldsPerLight + 13] = la;
		lightBuf.put(which * fieldsPerLight + 13, la);
	}
	protected void updateQuadraticAttenuation(int which, float qa)
	{	lightArray[which * fieldsPerLight + 14] = qa;
		lightBuf.put(which * fieldsPerLight + 14, qa);
	}
	protected void updateRange(int which, float r)
	{	lightArray[which * fieldsPerLight + 15] = r;
		lightBuf.put(which * fieldsPerLight + 15, r);
	}
	protected void updateDirection(int which, float x, float y, float z)
	{	lightArray[which * fieldsPerLight + 16] = x;
		lightArray[which * fieldsPerLight + 17] = y;
		lightArray[which * fieldsPerLight + 18] = z;
		lightBuf.put(which * fieldsPerLight + 16, x);
		lightBuf.put(which * fieldsPerLight + 17, y);
		lightBuf.put(which * fieldsPerLight + 18, z);
	}
	protected void updateCutoffAngle(int which, float coa)
	{	lightArray[which * fieldsPerLight + 19] = coa;
		lightBuf.put(which * fieldsPerLight + 19, coa);
	}
	protected void updateOffAxisExponent(int which, float oae)
	{	lightArray[which * fieldsPerLight + 20] = oae;
		lightBuf.put(which * fieldsPerLight + 20, oae);
	}
	protected void updateType(int which, float t)
	{	lightArray[which * fieldsPerLight + 21] = t;
		lightBuf.put(which * fieldsPerLight + 21, t);
	}
	protected void updateSSBO()
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glBindBuffer(GL_SHADER_STORAGE_BUFFER, lightSSBO[0]);
		gl.glBufferData(GL_SHADER_STORAGE_BUFFER, lightBuf.limit()*4, lightBuf, GL_STATIC_DRAW);
	}

	// This function is called once, from init() in the renderer.
	// It loads information from the Light array into the FloatArray,
	// then from the FloatArray into the FloatBuffer, and finally into the SSBO.

	protected void loadLightsSSBOinitial()
	{	GL4 gl = (GL4) GLContext.getCurrentGL();

		int numLights = getNumLights();
		lightArray = new float[numLights*fieldsPerLight];
		for (int i=0; i < numLights; i++)
		{	lightArray[i*fieldsPerLight + 0] = (getLight(i).getLocation())[0];
			lightArray[i*fieldsPerLight + 1] = (getLight(i).getLocation())[1];
			lightArray[i*fieldsPerLight + 2] = (getLight(i).getLocation())[2];
			lightArray[i*fieldsPerLight + 3] = (getLight(i).getAmbient())[0];
			lightArray[i*fieldsPerLight + 4] = (getLight(i).getAmbient())[1];
			lightArray[i*fieldsPerLight + 5] = (getLight(i).getAmbient())[2];
			lightArray[i*fieldsPerLight + 6] = (getLight(i).getDiffuse())[0];
			lightArray[i*fieldsPerLight + 7] = (getLight(i).getDiffuse())[1];
			lightArray[i*fieldsPerLight + 8] = (getLight(i).getDiffuse())[2];
			lightArray[i*fieldsPerLight + 9] = (getLight(i).getSpecular())[0];
			lightArray[i*fieldsPerLight + 10] = (getLight(i).getSpecular())[1];
			lightArray[i*fieldsPerLight + 11] = (getLight(i).getSpecular())[2];
			lightArray[i*fieldsPerLight + 12] = (getLight(i).getConstantAttenuation());
			lightArray[i*fieldsPerLight + 13] = (getLight(i).getLinearAttenuation());
			lightArray[i*fieldsPerLight + 14] = (getLight(i).getQuadraticAttenuation());
			lightArray[i*fieldsPerLight + 15] = (getLight(i).getRange());
			lightArray[i*fieldsPerLight + 16] = (getLight(i).getDirection())[0];
			lightArray[i*fieldsPerLight + 17] = (getLight(i).getDirection())[1];
			lightArray[i*fieldsPerLight + 18] = (getLight(i).getDirection())[2];
			lightArray[i*fieldsPerLight + 19] = (getLight(i).getCutoffAngle());
			lightArray[i*fieldsPerLight + 20] = (getLight(i).getOffAxisExponent());
			float type;
			LightType lightType = getLight(i).getLightType();
			if (lightType == LightType.POSITIONAL) type = 0.0f; else type = 1.0f;
			lightArray[i*fieldsPerLight + 21] = type;
		}
		gl.glGenBuffers(1, lightSSBO, 0);
		gl.glBindBuffer(GL_SHADER_STORAGE_BUFFER, lightSSBO[0]);
		lightBuf = Buffers.newDirectFloatBuffer(lightArray);
		gl.glBufferData(GL_SHADER_STORAGE_BUFFER, lightBuf.limit()*4, lightBuf, GL_STATIC_DRAW);
	}
}