package tage.objectRenderers;
import java.nio.*;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.common.nio.Buffers;
import org.joml.*;
import tage.*;

/**
* Includes a single method render() for rendering a Line.
* <p>
* Used by the game engine, should not be used directly by the game application.
* @author Scott Gordon
*/
public class RenderObjectLine
{	private GLCanvas myCanvas;
	private Engine engine;

	// allocate variables for display() function
	private FloatBuffer vals = Buffers.newDirectFloatBuffer(16);
	private Matrix4f pMat = new Matrix4f();  // perspective matrix
	private Matrix4f vMat = new Matrix4f();  // view matrix
	private Matrix4f mMat = new Matrix4f();  // model matrix
	private int mLoc, vLoc, pLoc, cLoc;

	/** for engine use only. */
	public RenderObjectLine(Engine e)
	{	engine = e;
	}

	/** for engine use only. */
	public void render(GameObject go, int lineProgram, Matrix4f pMat, Matrix4f vMat)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();

		gl.glUseProgram(lineProgram);

		mLoc = gl.glGetUniformLocation(lineProgram, "m_matrix");
		vLoc = gl.glGetUniformLocation(lineProgram, "v_matrix");
		pLoc = gl.glGetUniformLocation(lineProgram, "p_matrix");
		cLoc = gl.glGetUniformLocation(lineProgram, "lineColor");
		
		mMat.identity();
		mMat.mul(go.getWorldTranslation());
		mMat.mul(go.getWorldRotation());
		mMat.mul(go.getWorldScale());
		
		gl.glUniformMatrix4fv(mLoc, 1, false, mMat.get(vals));
		gl.glUniformMatrix4fv(vLoc, 1, false, vMat.get(vals));
		gl.glUniformMatrix4fv(pLoc, 1, false, pMat.get(vals));
		gl.glUniform3fv(cLoc, 1, ((go.getRenderStates()).getColor()).get(vals));

		gl.glBindBuffer(GL_ARRAY_BUFFER, go.getShape().getVertexBuffer());
		gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		gl.glEnableVertexAttribArray(0);
	
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);

		gl.glDrawArrays(GL_LINES, 0, go.getShape().getNumVertices());
	}
}