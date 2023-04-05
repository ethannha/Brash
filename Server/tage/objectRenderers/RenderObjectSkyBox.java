package tage.objectRenderers;
import java.nio.*;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.common.nio.Buffers;
import org.joml.*;
import tage.shapes.*;
import tage.*;

/**
* Includes a single method render() for rendering a 6-image OpenGL SkyBox (Cubemap).
* The skybox images are assumed to be in the assets/skyboxes folder.
* Skyboxes are rendered without lighting.
* <p>
* Follows closely the method described in Chapter 4
* of Computer Graphics Programming in OpenGL with Java.
* <p>
* Used by the engine, should not be used directly by the game application.
* @author Scott Gordon
*/

public class RenderObjectSkyBox
{	private GLCanvas myCanvas;
	private Engine engine;

	// allocate variables for display() function
	private FloatBuffer vals = Buffers.newDirectFloatBuffer(16);
	private Matrix4f pMat = new Matrix4f();  // perspective matrix
	private Matrix4f vMat = new Matrix4f();  // view matrix
	private Matrix4f mMat = new Matrix4f();  // model matrix
	private int mLoc, vLoc, pLoc;
	private int activeSkyBoxTexture;

	/** for engine use only. */
	public RenderObjectSkyBox(Engine e)
	{	engine = e;
	}

	/** for engine use only. */
	public void render(GameObject go, int skyboxProgram, Matrix4f pMat, Matrix4f vMat)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();

		gl.glUseProgram(skyboxProgram);

		vLoc = gl.glGetUniformLocation(skyboxProgram, "v_matrix");
		pLoc = gl.glGetUniformLocation(skyboxProgram, "p_matrix");
		gl.glUniformMatrix4fv(vLoc, 1, false, vMat.get(vals));
		gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));

		gl.glBindBuffer(GL_ARRAY_BUFFER, go.getShape().getVertexBuffer());
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);

		activeSkyBoxTexture = (engine.getSceneGraph()).getActiveSkyBoxTexture();
		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_CUBE_MAP, activeSkyBoxTexture);

		gl.glEnable(GL_TEXTURE_CUBE_MAP_SEAMLESS);	
		gl.glEnable(GL_CULL_FACE);
		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		gl.glFrontFace(GL_CCW);	     // cube is CW, but we are viewing the inside
		gl.glDisable(GL_DEPTH_TEST);
		gl.glDrawArrays(GL_TRIANGLES, 0, 36);
		gl.glEnable(GL_DEPTH_TEST);
	}
}