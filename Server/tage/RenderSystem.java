package tage;
import java.nio.*;
import javax.swing.*;
import java.lang.Math;
import java.util.*;
import java.awt.*;
import static com.jogamp.opengl.GL4.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.common.nio.Buffers;
import org.joml.*;
import tage.shapes.*;
import tage.objectRenderers.*;

/**
* Manages the OpenGL setup and rendering for each frame.
* Closely follows the method described in Computer Graphics Programming in OpenGL with Java.
* <p>
* The only methods that the game application is likely to utilize are:
* <ul>
* <li> setTitle() to set the title in the bar at the top of the render window
* <li> addViewport() if setting up multiple viewports
* <li> getViewport() mainly to get that viewport's camera
* </ul>
* <p>
* This class includes the init() and display() methods used by the JOGL animator.
* @author Scott Gordon
*/
public class RenderSystem extends JFrame implements GLEventListener
{	private GLCanvas myCanvas;
	private Engine engine;
	private RenderQueue rq;
	private RenderObjectStandard objectRendererStandard;
	private RenderObjectSkyBox objectRendererSkyBox;
	private RenderObjectLine objectRendererLine;
	private RenderObjectAnimation objectRendererAnimation;

	private float fov = 60.0f;
	private float nearClip = 0.1f;
	private float farClip = 1000.0f;

	private int renderingProgram, hudColorProgram, skyboxProgram, lineProgram;
	private int heightProgram, skelProgram;
	private int[] vao = new int[1];
	private int[] vbo = new int[3];

	private int defaultSkyBox;
	
	// allocate variables for display() function
	private Matrix4f pMat = new Matrix4f();  // perspective matrix
	private Matrix4f vMat = new Matrix4f();  // view matrix
	private int xLoc, zLoc;
	private float aspect;
	private int defaultTexture;
	private String defaultTitle = "default title", title;
	private int screenSizeX, screenSizeY;

	private ArrayList<TextureImage> textures = new ArrayList<TextureImage>();
	private ArrayList<ObjShape> shapes = new ArrayList<ObjShape>();
	private LinkedHashMap<String, Viewport> viewportList = new LinkedHashMap<String, Viewport>();

	private int canvasWidth, canvasHeight;
	private boolean isInFullScreenMode = false;
	GraphicsEnvironment ge;
	GraphicsDevice gd;

	private int buffer[] = new int[1];
	private float res[] = new float[1];

	protected RenderSystem(Engine e)
	{	engine = e;

		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();
		DisplaySettingsDialog dsd = new DisplaySettingsDialog(gd);
		dsd.showIt();

		DisplayMode dm = dsd.getSelectedDisplayMode();
		this.setSize(dm.getWidth(), dm.getHeight());
		if (dsd.isFullScreenModeSelected()) tryFullScreenMode(gd, dm);
	}

	protected void setUpCanvas()
	{	myCanvas = new GLCanvas();
		myCanvas.addGLEventListener(this);
		this.getContentPane().add(myCanvas, BorderLayout.CENTER);
		this.setVisible(true);
		(engine.getHUDmanager()).setGLcanvas(myCanvas);
	}

	/** The game application can use this to set the window dimensions if in windowed mode. */
	public void setWindowDimensions(int ssX, int ssY)
	{	title = defaultTitle;
		screenSizeX = ssX;
		screenSizeY = ssY;
		if (!isInFullScreenMode) setSize(screenSizeX, screenSizeY);
	}

	/** gets a reference to the current OpenGL canvas used by the engine */
	public GLCanvas getGLCanvas() { return myCanvas; }

	/** sets the title at the top of the window if in windowed mode */
	public void setTitle(String t) { title = t; }

	private void tryFullScreenMode(GraphicsDevice gd, DisplayMode dispMode)
	{	isInFullScreenMode = false;
		if (gd.isFullScreenSupported())
		{	this.setUndecorated(true);
			this.setResizable(false);
			this.setIgnoreRepaint(true); // AWT repaint events ignored for active rendering
			gd.setFullScreenWindow(this);
			if (gd.isDisplayChangeSupported())
			{	try
				{	gd.setDisplayMode(dispMode);
					this.setSize(dispMode.getWidth(), dispMode.getHeight());
					isInFullScreenMode = true;
				}
				catch (IllegalArgumentException e)
				{	System.out.println(e.getLocalizedMessage());
					this.setUndecorated(false);
					this.setResizable(true);
				}
			}
			else
			{	System.out.println("FSEM not supported");
			}
		}
		else
		{	this.setUndecorated(false);
			this.setResizable(true);
			this.setSize(dispMode.getWidth(), dispMode.getHeight());
			this.setLocationRelativeTo(null);
		}
	}

	/** incomplete - see comments in the code. */
	public void toggleFullScreenMode()
	{	// Note that toggling out of fullscreen mode is incomplete.
		// It basically just makes the window smaller - it cannot be resized or minimized.
		// The only way to completely exit fullscreen mode is to first dispose the frame.
		// But that causes a new frame to be created, resulting in another call to JOGL init().
		// So although this doesn't fully return to windowed mode, at least you can access other windows.
		// And toggling back to fullscreen mode, if initially was in fullscreen mode, should work.

		if (isInFullScreenMode)
		{	gd.setFullScreenWindow(null);
			this.setSize(screenSizeX, screenSizeY);
			isInFullScreenMode = false;
		}
		else
		{	gd.setFullScreenWindow(this);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			isInFullScreenMode = true;
		}
	}

	/** Adds a viewport at (bottom, left) with dimensions width and height. */
	public Viewport addViewport(String name, float bottom, float left, float width, float height)
	{	Viewport vp = new Viewport(name, engine, bottom, left, width, height);
		viewportList.put(name, vp);
		return vp;
	}

	/** gets a reference to the viewport with the specified name. */
	public Viewport getViewport(String name) { return viewportList.get(name); }

	protected void startGameLoop()
	{	setTitle(title);
		Animator animator = new Animator(myCanvas);
		animator.start();
	}

	/**
	* Displays the current frame - for Engine use only.
	* This method is called automatically by the JOGL Animator, once per frame.
	* It renders every object in the scene, considering all factors such as lights, etc.
	* The game application should NOT call this function directly.
	*/
	public void display(GLAutoDrawable drawable)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glClear(GL_COLOR_BUFFER_BIT);
		gl.glClear(GL_DEPTH_BUFFER_BIT);

		(engine.getGame()).update();
		(engine.getSceneGraph()).applyNodeControllers();

		engine.getLightManager().updateSSBO();

		canvasWidth = myCanvas.getWidth();
		canvasHeight = myCanvas.getHeight();

		for (Viewport vp : viewportList.values())
		{	vMat = vp.getCamera().getViewMatrix();

			aspect = ((float)myCanvas.getWidth() * vp.getRelativeWidth()) / ((float) myCanvas.getHeight() * vp.getRelativeHeight());
			pMat.setPerspective((float) Math.toRadians(fov), aspect, nearClip, farClip);

			constructViewport(vp);

			if ((engine.getSceneGraph()).isSkyboxEnabled())
			{	objectRendererSkyBox.render((engine.getSceneGraph()).getSkyBoxObject(), skyboxProgram, pMat, vMat);
			}

			(engine.getHUDmanager()).drawHUDs(hudColorProgram);

			rq = new RenderQueue((engine.getSceneGraph()).getRoot());
			Vector<GameObject> q = rq.createStandardQueue();

			for (int i = 0; i < q.size(); i++)
			{	GameObject go = q.get(i);
				if ((go.getRenderStates()).renderingEnabled())
				{	if ((go.getShape()).getPrimitiveType() < 3)
					{	objectRendererLine.render(go, lineProgram, pMat, vMat);
					}
					else if (go.getShape() instanceof AnimatedShape)
					{	objectRendererAnimation.render(go, skelProgram, pMat, vMat);
					}
					else
					{	objectRendererStandard.render(go, renderingProgram, pMat, vMat);
						// if hidden faces are rendered, render a second time with opposite winding order
						if ((go.getRenderStates()).willRenderHiddenFaces()) 
						{	(go.getShape()).toggleWindingOrder();
							objectRendererStandard.render(go, renderingProgram, pMat, vMat);
							(go.getShape()).toggleWindingOrder();
						}
					}
				}
			}
		}
	}

	private void constructViewport(Viewport vp)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();

		gl.glEnable(GL_SCISSOR_TEST);
		gl.glScissor((int)(vp.getRelativeLeft()*canvasWidth),
			(int)(vp.getRelativeBottom()*canvasHeight),
			(int)vp.getActualWidth(),
			(int)vp.getActualHeight());
		gl.glClear(GL_COLOR_BUFFER_BIT);
		gl.glClear(GL_DEPTH_BUFFER_BIT);

		if (vp.getHasBorder())
		{	int borderWidth = vp.getBorderWidth();
			float[] color = vp.getBorderColor();
			gl.glEnable(GL_SCISSOR_TEST);
			gl.glScissor((int)(vp.getRelativeLeft()*canvasWidth),
				(int)(vp.getRelativeBottom()*canvasHeight),
				(int)vp.getActualWidth(),
				(int)vp.getActualHeight());
			gl.glClearColor(color[0], color[1], color[2], 1.0f);
			gl.glClear(GL_COLOR_BUFFER_BIT);
			gl.glScissor((int)(vp.getRelativeLeft()*canvasWidth)+borderWidth,
				(int)(vp.getRelativeBottom()*canvasHeight)+borderWidth,
				(int)vp.getActualWidth()-borderWidth*2,
				(int)vp.getActualHeight()-borderWidth*2);
			gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			gl.glClear(GL_COLOR_BUFFER_BIT);
			gl.glDisable(GL_SCISSOR_TEST);

			gl.glViewport((int)(vp.getRelativeLeft()*canvasWidth)+borderWidth,
				(int)(vp.getRelativeBottom()*canvasHeight)+borderWidth,
				(int)(vp.getRelativeWidth()*canvasWidth)-borderWidth*2,
				(int)(vp.getRelativeHeight()*canvasHeight)-borderWidth*2);
		}
		else
		{	gl.glViewport((int)(vp.getRelativeLeft()*canvasWidth),
				(int)(vp.getRelativeBottom()*canvasHeight),
				(int)(vp.getRelativeWidth()*canvasWidth),
				(int)(vp.getRelativeHeight()*canvasHeight));
		}
	}

	/**
	* Initializes the elements needed for rendering - for Engine use only.
	* This method is called one time, automatically, by the JOGL Animator.
	* The game application should NOT call this function directly.
	*/
	public void init(GLAutoDrawable drawable)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
	
		renderingProgram = Utils.createShaderProgram("assets/shaders/StandardVert.glsl",
			"assets/shaders/StandardFrag.glsl");

		hudColorProgram = Utils.createShaderProgram("assets/shaders/HUDcolorVert.glsl",
			"assets/shaders/HUDcolorFrag.glsl");

		skyboxProgram = Utils.createShaderProgram("assets/shaders/skyboxVert.glsl",
			"assets/shaders/skyboxFrag.glsl");

		lineProgram = Utils.createShaderProgram("assets/shaders/LineVert.glsl",
			"assets/shaders/LineFrag.glsl");

		skelProgram = Utils.createShaderProgram("assets/shaders/skeletalVert.glsl",
			"assets/shaders/StandardFrag.glsl");

		objectRendererStandard = new RenderObjectStandard(engine);
		objectRendererSkyBox = new RenderObjectSkyBox(engine);
		objectRendererLine = new RenderObjectLine(engine);
		objectRendererAnimation = new RenderObjectAnimation(engine);

		aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
		pMat.setPerspective((float) Math.toRadians(fov), aspect, nearClip, farClip);

		System.out.println("loading skyboxes");
		defaultTexture = Utils.loadTexture("assets/defaultAssets/checkerboardSmall.JPG");
		defaultSkyBox = Utils.loadCubeMap("assets/defaultAssets/lakeIslands");

		loadVBOs();


		loadTexturesIntoOpenGL();
		(engine.getGame()).loadSkyBoxes();

		// prepare buffer for extracting height from height map
		heightProgram = Utils.createShaderProgram("assets/shaders/heightCompute.glsl");
		gl.glGenBuffers(1, buffer, 0);
		gl.glBindBuffer(GL_SHADER_STORAGE_BUFFER, buffer[0]);
		FloatBuffer resBuf = Buffers.newDirectFloatBuffer(res.length);
		gl.glBufferData(GL_SHADER_STORAGE_BUFFER, resBuf.limit()*4, null, GL_STATIC_READ);

		(engine.getLightManager()).loadLightsSSBOinitial();
	}

	protected int getDefaultSkyBox() { return defaultSkyBox; }

	/** for engine use only. */
	public int getDefaultTexture() { return defaultTexture; }

	// ----------------------- SHAPES SECTION ----------------------

	protected void addShape(ObjShape s) { shapes.add(s); }

	// loads the vertices, tex coords, and normals into three VBOs.
	private void loadVBOs()
	{	GL4 gl = (GL4) GLContext.getCurrentGL();

		gl.glGenVertexArrays(vao.length, vao, 0);
		gl.glBindVertexArray(vao[0]);

		for (ObjShape shape:shapes)
		{	gl.glGenBuffers(3, vbo, 0);

			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
			FloatBuffer vertBuf = Buffers.newDirectFloatBuffer(shape.getVertices());
			gl.glBufferData(GL_ARRAY_BUFFER, vertBuf.limit()*4, vertBuf, GL_STATIC_DRAW);

			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
			FloatBuffer texBuf = Buffers.newDirectFloatBuffer(shape.getTexCoords());
			gl.glBufferData(GL_ARRAY_BUFFER, texBuf.limit()*4, texBuf, GL_STATIC_DRAW);

			gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[2]);
			FloatBuffer norBuf = Buffers.newDirectFloatBuffer(shape.getNormals());
			gl.glBufferData(GL_ARRAY_BUFFER, norBuf.limit()*4, norBuf, GL_STATIC_DRAW);

			shape.setVertexBuffer(vbo[0]);
			shape.setTexCoordBuffer(vbo[1]);
			shape.setNormalBuffer(vbo[2]);

			if (shape instanceof AnimatedShape)
			{	gl.glGenBuffers(2, vbo, 0);

				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
				FloatBuffer boneBuf = Buffers.newDirectFloatBuffer(shape.getBoneWeights());
				gl.glBufferData(GL_ARRAY_BUFFER, boneBuf.limit()*4, boneBuf, GL_STATIC_DRAW);

				gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[1]);
				FloatBuffer binBuf = Buffers.newDirectFloatBuffer(shape.getBoneIndices());
				gl.glBufferData(GL_ARRAY_BUFFER, binBuf.limit()*4, binBuf, GL_STATIC_DRAW);

				shape.setBoneWeightBuffer(vbo[0]);
				shape.setBoneIndicesBuffer(vbo[1]);
			}
		}

		// load skybox into vbo
		gl.glGenBuffers(1, vbo, 0);
		GameObject go = (engine.getSceneGraph()).getSkyBoxObject();
		gl.glBindBuffer(GL_ARRAY_BUFFER, vbo[0]);
		FloatBuffer vertBuf = Buffers.newDirectFloatBuffer(go.getShape().getVertices());
		gl.glBufferData(GL_ARRAY_BUFFER, vertBuf.limit()*4, vertBuf, GL_STATIC_DRAW);
		go.getShape().setVertexBuffer(vbo[0]);
	}

	// ------------------ TEXTURE SECTION ---------------------

	protected void addTexture(TextureImage t) { textures.add(t); }

	private void loadTexturesIntoOpenGL()
	{	int thisTexture;
		for (int i = 0; i < textures.size(); i++)
		{	TextureImage t = textures.get(i);
			thisTexture = Utils.loadTexture(t.getTextureFile());
			t.setTexture(thisTexture);
		}
		engine.getSceneGraph().setActiveSkyBoxTexture(defaultSkyBox);
	}

	/** get height map height at the specified texture coordinate (x,z). */
	public float getHeightAt(int texture, float x, float z)
	{	GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glUseProgram(heightProgram);
		gl.glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, buffer[0]);

		xLoc = gl.glGetUniformLocation(heightProgram, "x");
		zLoc = gl.glGetUniformLocation(heightProgram, "z");
		gl.glUniform1f(xLoc, x);
		gl.glUniform1f(zLoc, z);

		gl.glActiveTexture(GL_TEXTURE0);
		gl.glBindTexture(GL_TEXTURE_2D, texture);

		gl.glDispatchCompute(1, 1, 1);
		gl.glFinish();

		gl.glBindBuffer(GL_SHADER_STORAGE_BUFFER, buffer[0]);
		FloatBuffer resBuf = Buffers.newDirectFloatBuffer(res.length);
		gl.glGetBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, resBuf.limit()*4, resBuf);
		
		float res = resBuf.get();
		return res;
	}

	// ---------------------------------------------------------
	/** for engine use, called by JOGL. */
	public void dispose(GLAutoDrawable drawable) {}

	/** for engine use, called by JOGL. */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
	{	aspect = (float) myCanvas.getWidth() / (float) myCanvas.getHeight();
		pMat.setPerspective((float) Math.toRadians(fov), aspect, nearClip, farClip);
	}
}