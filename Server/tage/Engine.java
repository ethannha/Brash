package tage;
import tage.input.*;

/**
* The Engine object holds references to the primary game engine components, and
* provides accessors for them.  It also maintains a link to the current game application.
* <p>
* The first thing that a game application should do is instantiate this engine class
* using the three-argument constructor. The arguments are a link back to the game application,
* and the desired window dimensions.
* @author Scott Gordon
*/
public class Engine
{
	private static Engine eng;

	/** returns a reference to the Engine object */
	public static Engine getEngine() { return eng; }

	private RenderSystem rs;
	private SceneGraph sg;
	private HUDmanager hm;
	private LightManager lm;
	private VariableFrameRateGame vfrg;
	private InputManager im;

	/** The game application should first call this constructor, supplying a pointer back to itself. */
	public Engine(VariableFrameRateGame v)
	{	vfrg = v;
		VariableFrameRateGame.setEngine(this);
		eng = this;
		rs = new RenderSystem(this);
		sg = new SceneGraph(this);
		hm = new HUDmanager(this);
		lm = new LightManager(this);
		im = new InputManager();
		Light.setEngine(this);
		rs.setUpCanvas();
		sg.buildSkyBox();
	}

	/** returns the RenderSystem object associated with this Engine */
	public RenderSystem getRenderSystem() { return rs; }

	/** returns the SceneGraph object associated with this Engine */
	public SceneGraph getSceneGraph() { return sg; }

	/** returns the HUDmanager object associated with this Engine */
	public HUDmanager getHUDmanager() { return hm; }

	/** returns the LightManager object associated with this Engine */
	public LightManager getLightManager() { return lm; }

	/** returns the InputManager object associated with this Engine */
	public InputManager getInputManager() { return im; }

	/** returns a reference to the game application. */
	public VariableFrameRateGame getGame() { return vfrg; }
}