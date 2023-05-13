package tage;
import java.awt.event.*;
import java.awt.event.KeyListener;

/**
* A game application should extend this class.
* Provides basic functionality for setting up OpenGL, starting and stopping a game,
* and capturing key and mouse strokes.
* The ESC key is configured to abort the game, and the EQUALS key toggles between windowed and full-screen.
* @author Scott Gordon
*/
public abstract class VariableFrameRateGame implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	static Engine eng;
	static void setEngine(Engine e) { eng = e; }
	static Engine getEngine() { return eng; }

	protected VariableFrameRateGame() { }

	protected void initializeSystem()
	{	System.out.println("****************************************************");
		System.out.println("*    Powered by TAGE - ANOTHER TINY GAME ENGINE    *");
		System.out.println("****************************************************");
		System.out.println("creating variable frame rate game");

		System.out.println("loading game shapes (models)");
		loadShapes();

		System.out.println("loading in textures");
		loadTextures();

		System.out.println("building game objects and scenegraph");
		buildObjects();

		System.out.println("building viewports");
		createViewports();

		System.out.println("initializing lights");
		initializeLights();

		System.out.println("initializing game");
		initializeGame();
	}

	/** Place instantiations of ObjShapes in this method when overriding. */
	public abstract void loadShapes();

	/** Place instantiations of TextureImages in this method when overriding. */
	public abstract void loadTextures();

	/** Place instantiations of GameObjects, and their initial configurations in this method when overriding. */
	public abstract void buildObjects();

	/**
	* Override this function if more viewports are desired.
	* Each viewport has a name, and a camera with a matching name.
	*/
	public void createViewports()
	{	(eng.getRenderSystem()).addViewport("MAIN",0,0,1,1);
	}

	/** Use this function to initialize global ambient, positional, and spot lights */
	public abstract void initializeLights();

	/** Use this function to initialize any other game stuff, node controllers, game logic, AI, etc. */
	public abstract void initializeGame();

	/**
	* The game application should override this function and build skyboxes in it.
	* This method is called by the RenderSystem from init(),
	* because the GL context must exist to build an OpenGL Cubemap.
	*/
	public void loadSkyBoxes() { }

	/**
	* Tells the render system to start the OpenGL Animator.
	* It should NOT be necessary for the game application to override this function.
	* Instead, the game application should call it when it is ready to start the game,
	* if a skybox is desired.
	*/
	public void game_loop()
	{	System.out.println("starting game loop");

		RenderSystem rs = getEngine().getRenderSystem();
		rs.getGLCanvas().addKeyListener(getEngine().getGame());
		rs.getGLCanvas().addMouseListener(getEngine().getGame());
		rs.getGLCanvas().addMouseMotionListener(getEngine().getGame());
		rs.getGLCanvas().addMouseWheelListener(getEngine().getGame());

		(eng.getSceneGraph()).updateAllObjectTransforms();
		(eng.getRenderSystem()).startGameLoop();
	}

	/**
	* Performs any functions necessary before shutting down the game.
	* The game application should override this function if there are particulars to do.
	* If overridden, the first line of the overridden function should be super.shutdown().
	*/
	public void shutdown()
	{	System.out.println("shutting down");
	}

	/**
	* Performs all game-specific per-frame processing.
	* This typically includes moving the objects, updating state information, etc.
	* The game application should override this function.  It is called by the engine.
	* It is not necessary to call super.update().
	*/
	public abstract void update();

	/**
	* Handles key and mouse listener tasks.
	* The game application should override this function, and the <i>last</i> line
	* of code in the function should be super.keyPressed(e) where "e" is the captured event.
	*/
	public void keyPressed(KeyEvent e)
	{	switch (e.getKeyCode())
		{	case KeyEvent.VK_ESCAPE:
				shutdown();
				System.exit(0);
				break;
			case KeyEvent.VK_EQUALS:
				(eng.getRenderSystem()).toggleFullScreenMode();
				break;
		}
	}

	/** override if desired. */
	public void keyReleased(KeyEvent e) {}

	/** override if desired. */
	public void keyTyped(KeyEvent e) {}

	/** override if desired. */
	public void mousePressed(MouseEvent e) {}

	/** override if desired. */
	public void mouseReleased(MouseEvent e) {}

	/** override if desired. */
	public void mouseEntered(MouseEvent e) {}

	/** override if desired. */
	public void mouseExited(MouseEvent e) {}

	/** override if desired. */
	public void mouseClicked(MouseEvent e) {}

	/** override if desired. */
	public void mouseMoved(MouseEvent e) {}

	/** override if desired. */
	public void mouseDragged(MouseEvent e) {}

	/** override if desired. */
	public void mouseWheelMoved(MouseWheelEvent e) {}
}