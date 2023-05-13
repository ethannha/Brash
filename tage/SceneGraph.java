package tage;
import java.util.*;
import tage.shapes.*;
import tage.nodeControllers.*;
import org.joml.*;

/**
* Tools for building a scene graph tree, and building and applying the associated node controllers.
* A game application should use the tools here for adding game objects, lights, and node controllers.
* The game objects and node controllers are stored in ArrayLists.
* The renderer also uses methods here for applying node controllers at each frame.
* <p>
* The functions here that are useful for the game application are:
* <ul>
* <li> addLight()
* <li> addNodeController()
* <li> getRoot()
* <li> loadCubeMap()
* <li> removeGameObject()
* <li> getNumGameObjects()
* </ul>
* <p>
* It is important to understand that adding a game object doesn't require calling addGameObject().
* That function is called by the engine.  All that is necessary is to use one of the GameObject constructors.
* They will call addGameObject().  Similarly it isn't necessary to call buildSkyBox(), that happens automatically.
* However, adding a Light or a Node Controller does necessitate calling addLight() and addNodeController().
* <p>
* Loading a skybox texture into an OpenGL CubeMap using loadCubeMap() is done as follows.
* The game application needs to supply the name of a folder containing the 6 textures.
* The folder is assumed to be in the assets/skyboxes folder.
* The resulting OpenGL CubeMap is referenced by the returned integer.
* The game application should store that integer to refer to the cubemap if it wishes to swap
* between multiple cubemaps.  That can be done by calling setActiveSkyBoxTexture() with the integer
* skybox reference provided as a parameter. All cubemaps should be loaded before starting the game loop.
* @author Scott Gordon
*/

public class SceneGraph
{	private static GameObject root;
	private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	private ArrayList<NodeController> nodeControllers = new ArrayList<NodeController>();
	private Engine engine;

	private RenderSystem rs;
	private NodeController nc, nci;
	private GameObject skybox;
	private boolean skyboxEnabled = false;
	private int activeSkyBoxTexture;

	protected SceneGraph(Engine e)
	{	engine = e;
		root = GameObject.createRoot();
	}

	// -------------- LIGHT SECTION ------------------------

	/** adds the specified Light object to the LightManager for rendering. */
	public void addLight(Light light) { (engine.getLightManager()).addLight(light); }

	// -------------- NODE CONTROLLER SECTION -------------------

	/** adds the specified node controller for use in the game. */
	public void addNodeController(NodeController nc) { nodeControllers.add(nc); }

	// Apply the node controllers to their attached objects - for engine use only.
	// Called by RenderSystem, should not be called by the game application directly.

	protected void applyNodeControllers()
	{	for (int i = 0; i < nodeControllers.size(); i++)
		{	nci = nodeControllers.get(i);
			if (nci.isEnabled()) nci.applyController();
		}
	}

	// -------------- GAME OBJECT SECTION ---------------------

	/** returns the current number of GameObjects. */
	public int getNumGameObjects() { return gameObjects.size(); }

	/** returns a reference to the entire ArrayList of GameObjects - not likely to be useful to the game application. */
	public ArrayList<GameObject> getGameObjects() { return gameObjects; }

	protected GameObject getGameObject(int i) { return gameObjects.get(i); }
	protected GameObject getRoot() { return root; }
	protected void updateAllObjectTransforms() { root.update(); }

	/** removes the specified GameObject from the scenegraph. */
	public void removeGameObject(GameObject go)
	{	if (go.hasChildren())
		{	System.out.println("attempted deletion of game object with children");
		}
		else
		{	if (go.getParent() != null) (go.getParent()).removeChild(go);
			if (gameObjects.contains(go)) gameObjects.remove(go);
		}
	}

	protected void addGameObject(GameObject g) { gameObjects.add(g); }

	//------------- SKYBOX SECTION ---------------------

	/** loads a set of six skybox images into an OpenGL cubemap so that it can be used in an OpenGL skybox. */
	public int loadCubeMap(String foldername)
	{	int skyboxTexture = Utils.loadCubeMap("assets/skyboxes/"+foldername);
		return skyboxTexture;
	}

	/** returns a boolean that is true if skybox rendering has been enabled */
	public boolean isSkyboxEnabled() { return skyboxEnabled; }

	/** sets whether or not to render a skybox */
	public void setSkyBoxEnabled(boolean sbe) { skyboxEnabled = sbe; }

	/** specifies which loaded skybox should be rendered */
	public void setActiveSkyBoxTexture(int tex) { activeSkyBoxTexture = tex; }

	/** returns an integer reference to the current active skybox */
	public int getActiveSkyBoxTexture() { return activeSkyBoxTexture; }

	protected GameObject getSkyBoxObject() { return skybox; }

	protected void buildSkyBox()
	{	skybox = new GameObject(new SkyBoxShape());
	}
}