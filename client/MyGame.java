package client;

import tage.*;
import tage.shapes.*;

import java.lang.Math;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.*;
import java.io.*;

import org.joml.*;

import tage.CameraOrbitController;
import tage.Light.LightType;
import tage.audio.*;
import tage.input.*;
import tage.input.action.*;
import tage.networking.IGameConnection.ProtocolType;
import tage.nodeControllers.AttachController;
import tage.nodeControllers.RotationController;
import tage.physics.PhysicsEngine;
import tage.physics.PhysicsEngineFactory;
import tage.physics.PhysicsObject;
import tage.physics.JBullet.JBulletPhysicsEngine;
import tage.physics.JBullet.JBulletPhysicsObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MyGame extends VariableFrameRateGame
{
	private double lastFrameTime, currFrameTime, elapsTime;

	private static Engine engine;
	private GameObject avatar, crown, worldTerrain, jukeBoxObject;
	private ObjShape crownS, ghostS, terrainS, jukeBoxS, boxS;
	private TextureImage avatarTex, ghostTex, hillsTex, grassTex, crownTex, jukeBoxTex, boxTex;
	private Light light1, spotLight;
	private InputManager im;
	private Camera mainCamera;
	private CameraOrbitController orbitController;
	private NodeController rotationNode;
	private AttachController attachNode;
	private IAudioManager audioMgr;
	private Sound bgmSound, grassSound, collectSound, hitSound, jumpSound, deathSound, breakSound;

	private int sereneClouds; //skybox

	private float currHeight, prevHeight;

	// Ghost and Connection
	private GhostManager gm;
	private BoxManager bm;
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private ProtocolClient protClient;
	private boolean isClientConnected = false, crownAttach = false;


	// Script
	private File script1, script2;
	private long fileLastModified = 0;
	ScriptEngine jsEngine;
	Invocable invocableEngine;

	// Script initialize
	private float avatarPosX, avatarPosY, avatarPosZ, jukeBoxPosX, jukeBoxPosY, jukeBoxPosZ;
	private int hud1Height;

	// Player
	private int playerScore;
	private boolean isAlive = true;

	// Physics
	private PhysicsEngine physicsEngine;
	private PhysicsObject avatarP, planeP;
	private boolean running = true, canJump = false;
	private float vals[] = new float[16];

	// Animation
	private AnimatedShape avatarAnimatedShape, ghostAnimatedShape, npcAnimatedShape;
	private String aniName;

	// NPC/AI
	private ObjShape npcShape;
	private TextureImage npcTex;

	public MyGame(String serverAddress, int serverPort, String protocol) 
	{ 
		super(); 
		gm = new GhostManager(this);
		bm = new BoxManager(this);
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		if (protocol.toUpperCase().compareTo("TCP") == 0)
		{
			this.serverProtocol = ProtocolType.TCP;
		}
		else
		{
			this.serverProtocol = ProtocolType.UDP;
		}
	}

	public static void main(String[] args)
	{	
		MyGame game = new MyGame(args[0], Integer.parseInt(args[1]), args[2]);
		engine = new Engine(game);
		game.initializeSystem();
		game.game_loop();
	}

	@Override
	public void loadShapes()
	{	
		avatarAnimatedShape = new AnimatedShape("player.rkm", "player.rks");
		avatarAnimatedShape.loadAnimation("RUN", "player_run.rka");
		avatarAnimatedShape.loadAnimation("IDLE", "player_idle.rka");
		avatarAnimatedShape.loadAnimation("BACK", "player_backwalk.rka");
		avatarAnimatedShape.loadAnimation("JUMP", "player_jump.rka");
		avatarAnimatedShape.loadAnimation("PUNCHL", "player_punchL.rka");
		avatarAnimatedShape.loadAnimation("PUNCHR", "player_punchR.rka");

		ghostAnimatedShape = new AnimatedShape("player.rkm", "player.rks");
		ghostAnimatedShape.loadAnimation("RUN", "player_run.rka");
		ghostAnimatedShape.loadAnimation("IDLE", "player_idle.rka");
		ghostAnimatedShape.loadAnimation("BACK", "player_backwalk.rka");
		ghostAnimatedShape.loadAnimation("JUMP", "player_jump.rka");
		ghostAnimatedShape.loadAnimation("PUNCHL", "player_punchL.rka");
		ghostAnimatedShape.loadAnimation("PUNCHR", "player_punchR.rka");

		npcAnimatedShape = new AnimatedShape("duck.rkm", "duck.rks");
		npcAnimatedShape.loadAnimation("WALK", "duck_walk.rka");
		npcAnimatedShape.loadAnimation("CHASE", "duck_chase.rka");

		ghostS = new ImportedModel("player.obj");
		npcShape = new ImportedModel("duck.obj");
		terrainS = new TerrainPlane(1000);

		//Diamond object
		crownS = new ImportedModel("crown.obj");
		jukeBoxS = new Cube();
		boxS = new Cube();

	}

	@Override
	public void loadTextures()
	{
		avatarTex = new TextureImage("player_uv.png");
		ghostTex = new TextureImage("ghost_uv.png");
		crownTex = new TextureImage("crown_texture.png");
		hillsTex = new TextureImage("hill.png");
		grassTex = new TextureImage("grass.png");
		npcTex = new TextureImage("duck_uv.png");
		jukeBoxTex = new TextureImage("jukebox.png");
		boxTex = new TextureImage("crate.png");

	}

	@Override
	public void loadSkyBoxes()
	{
		sereneClouds = (engine.getSceneGraph()).loadCubeMap("sereneClouds");
		(engine.getSceneGraph()).setActiveSkyBoxTexture(sereneClouds);
		(engine.getSceneGraph()).setSkyBoxEnabled(true);
	}

	@Override
	public void buildObjects()
	{	
		// initialize scripting engine
		ScriptEngineManager factory = new ScriptEngineManager();
		jsEngine = factory.getEngineByName("js");
		script1 = new File("assets/scripts/initParams.js");
		this.runScript(script1);

		Matrix4f initialTranslation, initialRotation, initialScale;

		// build avatar in the center of the window
		avatar = new GameObject(GameObject.root(), avatarAnimatedShape, avatarTex);
		//avatar = new GameObject(GameObject.root(), avatarS, avatarT);
		avatarPosX = (float)((double)jsEngine.get("avatarPosX"));
		avatarPosY = (float)((double)jsEngine.get("avatarPosY"));
		avatarPosZ = (float)((double)jsEngine.get("avatarPosZ"));
		initialTranslation = (new Matrix4f()).translation(avatarPosX, avatarPosY, avatarPosZ);

		initialScale = (new Matrix4f()).scaling(0.2f);
		avatar.setLocalTranslation(initialTranslation);
		avatar.setLocalScale(initialScale);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(135.0f));
		avatar.setLocalRotation(initialRotation);

		// build crown
		crown = new GameObject(GameObject.root(), crownS, crownTex);
		initialTranslation = (new Matrix4f().translation(0.0f, -10.0f, 0.0f));
		initialScale = (new Matrix4f()).scaling(0.15f);
		crown.setLocalTranslation(initialTranslation);
		crown.getRenderStates().hasLighting(true);
		crown.setLocalScale(initialScale);

		// build world terrain object
		worldTerrain = new GameObject(GameObject.root(), terrainS, grassTex);
		initialTranslation = (new Matrix4f()).translation(0f,0f,0f);
		worldTerrain.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(30.0f, 10.0f, 30.0f);
		worldTerrain.setLocalScale((initialScale));
		worldTerrain.setHeightMap(hillsTex);

		// Box
		jukeBoxObject = new GameObject(GameObject.root(), jukeBoxS, jukeBoxTex);
		jukeBoxPosX = (float)((double)jsEngine.get("b1X"));
		jukeBoxPosY = (float)((double)jsEngine.get("b1Y"));
		jukeBoxPosZ = (float)((double)jsEngine.get("b1Z"));
		initialTranslation =  (new Matrix4f().translation(jukeBoxPosX, jukeBoxPosY, jukeBoxPosZ));
		initialScale = (new Matrix4f()).scaling(0.5f);
		jukeBoxObject.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(0.5f);
		jukeBoxObject.setLocalScale(initialScale);
		// initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(135.0f));
		jukeBoxObject.setLocalRotation(initialRotation);

		
	}

	@Override
	public void initializeLights()
	{	Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
		light1 = new Light();
		light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
		(engine.getSceneGraph()).addLight(light1);

		spotLight = new Light();
		spotLight.setType(LightType.SPOTLIGHT);
		spotLight.setLocation(jukeBoxObject.getWorldLocation());
		(engine.getSceneGraph()).addLight(spotLight);
	}

	public void initAudio()
	{ 
		AudioResource resource1, resource2, resource3, resource4, resource5, resource6, resource7;
		audioMgr = AudioManagerFactory.createAudioManager("tage.audio.joal.JOALAudioManager");
		if (!audioMgr.initialize()) { 
			System.out.println("Audio Manager failed to initialize!");
			return;
		}
		resource1 = audioMgr.createAudioResource("assets/sounds/bgm.wav", AudioResourceType.AUDIO_SAMPLE);
		resource2 = audioMgr.createAudioResource("assets/sounds/grass.wav", AudioResourceType.AUDIO_SAMPLE);
		resource3 = audioMgr.createAudioResource("assets/sounds/collect.wav", AudioResourceType.AUDIO_SAMPLE);
		resource4 = audioMgr.createAudioResource("assets/sounds/hit.wav", AudioResourceType.AUDIO_SAMPLE);
		resource5 = audioMgr.createAudioResource("assets/sounds/jump.wav", AudioResourceType.AUDIO_SAMPLE);
		resource6 = audioMgr.createAudioResource("assets/sounds/death.wav", AudioResourceType.AUDIO_SAMPLE);
		resource7 = audioMgr.createAudioResource("assets/sounds/break.wav", AudioResourceType.AUDIO_SAMPLE);

		bgmSound = new Sound(resource1, SoundType.SOUND_EFFECT, 100, true);
		grassSound = new Sound(resource2, SoundType.SOUND_EFFECT, 100, true);
		collectSound = new Sound(resource3, SoundType.SOUND_EFFECT, 100, false);
		hitSound = new Sound(resource4, SoundType.SOUND_EFFECT, 75, false);
		jumpSound = new Sound(resource5, SoundType.SOUND_EFFECT, 100, false);
		deathSound = new Sound(resource6, SoundType.SOUND_EFFECT, 100, false);
		breakSound = new Sound(resource7, SoundType.SOUND_EFFECT, 100, false);
		
		bgmSound.initialize(audioMgr);
		bgmSound.setMaxDistance(5.0f);
		bgmSound.setMinDistance(3.0f);
		bgmSound.setRollOff(5.0f);
		bgmSound.setLocation(jukeBoxObject.getWorldLocation());

		grassSound.initialize(audioMgr);
		grassSound.setMaxDistance(10.0f);
		grassSound.setMinDistance(0.5f);
		grassSound.setRollOff(5.0f);
		grassSound.setLocation(avatar.getWorldLocation());
		
		hitSound.initialize(audioMgr);
		hitSound.setMaxDistance(10.0f);
		hitSound.setMinDistance(0.5f);
		hitSound.setRollOff(5.0f);
		hitSound.setLocation(avatar.getWorldLocation());

		breakSound.initialize(audioMgr);
		breakSound.setLocation(avatar.getWorldLocation());

		jumpSound.initialize(audioMgr);
		jumpSound.setLocation(avatar.getWorldLocation());

		deathSound.initialize(audioMgr);
		deathSound.setLocation(avatar.getWorldLocation());
		
		collectSound.initialize(audioMgr);
		collectSound.setMaxDistance(10.0f);
		collectSound.setMinDistance(0.5f);
		collectSound.setRollOff(5.0f);
		collectSound.setLocation(crown.getWorldLocation());

		setEarParameters();
		bgmSound.play();
	}

	public void setEarParameters()
	{
		Camera camera = (engine.getRenderSystem()).getViewport("MAIN").getCamera();
		audioMgr.getEar().setLocation(avatar.getWorldLocation());
		audioMgr.getEar().setOrientation(camera.getN(), new Vector3f(0.0f, 1.0f, 0.0f));
	}


	@Override
	public void initializeGame()
	{	
		lastFrameTime = System.currentTimeMillis();
		currFrameTime = System.currentTimeMillis();
		elapsTime = 0.0;
		
		(engine.getRenderSystem()).setWindowDimensions(1900,1000);

		// Setting up network
		setupNetworking();

		// initialize scripting engine
		ScriptEngineManager factory = new ScriptEngineManager();
		jsEngine = factory.getEngineByName("js");

		invocableEngine = (Invocable)jsEngine;

		// initialize sounds
		initAudio();

		// -------------- Node Controllers --------------------------
		rotationNode = new RotationController(engine, new Vector3f(0.0f, 1.0f, 0.0f), 0.001f);
		attachNode = new AttachController(avatar);
		rotationNode.addTarget(crown);
		attachNode.addTarget(crown);

		(engine.getSceneGraph()).addNodeController(rotationNode);
		(engine.getSceneGraph()).addNodeController(attachNode);
		rotationNode.toggle();
		
		// ------------- Creating the cameras/viewports -------------
		createViewports();

		// ------------- Terrain Height ------------------------------
		currHeight = 0.0f;
		prevHeight = 0.0f;

		// -------------- Initialize Players Win --------------------
		script1 = new File("assets/scripts/initParams.js");
		this.runScript(script1);
		playerScore = (int)(jsEngine.get("p1Win"));

		hud1Height = ((int)jsEngine.get("hud1Height"));

		// ------------- Initialize Update win counter --------------
		script2 = new File("assets/scripts/updateWinCount.js");
		this.runScript(script2);

		// ------------ Physics initializing ------------------------
		String physEngineString = "tage.physics.JBullet.JBulletPhysicsEngine";
		float[] gravity = {0.0f, (float)((double)jsEngine.get("gravityY")), 0.0f};
		physicsEngine = PhysicsEngineFactory.createPhysicsEngine(physEngineString);
		physicsEngine.initSystem();
		physicsEngine.setGravity(gravity);
		
		// -------------- Create physics world ------------------
		float mass = 1.0f;
		float up[] = {0, 1, 0};
		float size[] = {avatar.getLocalUpVector().x(), avatar.getLocalUpVector().y(), avatar.getLocalUpVector().z()};
		double[] tempTransform;

		Matrix4f translation = new Matrix4f(avatar.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		avatarP = physicsEngine.addBoxObject(physicsEngine.nextUID(), mass, tempTransform, size);
		avatarP.setBounciness(0.01f);
		avatar.setPhysicsObject(avatarP);

		translation = new Matrix4f(worldTerrain.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		planeP = physicsEngine.addStaticPlaneObject(physicsEngine.nextUID(), tempTransform, up, 0.0f);
		planeP.setBounciness(1.0f);
		worldTerrain.setPhysicsObject(planeP);

		// --------------------- Animation ---------------------
		aniName = "IDLE";
		// --------------------- INPUT SECTION -----------------------
		im = engine.getInputManager();
		String gpName = im.getFirstGamepadName();
		
		orbitController = new CameraOrbitController(mainCamera, avatar, gpName, engine, this);

		FwdAction fwdAction = new FwdAction(this, protClient);
		BackAction backAction = new BackAction(this, protClient);
		TurnLeftAction turnLeftAction = new TurnLeftAction(this, protClient);
		TurnRightAction turnRightAction = new TurnRightAction(this, protClient);
		GamePadTurn gamePadTurn = new GamePadTurn(this, protClient);
		GamePadAction gamePadAction = new GamePadAction(this, protClient);
		ShutDownAction shutDownAction = new ShutDownAction(this, protClient);
		JumpAction jumpAction = new JumpAction(this, avatarP, protClient);
		PunchAction punchAction = new PunchAction(this, protClient);

		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.W, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.S, backAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.A, turnLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.D, turnRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.F, punchAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.ESCAPE, shutDownAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.SPACE, jumpAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.X, gamePadTurn, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Z, gamePadAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._0, jumpAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._2, punchAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Button._7, shutDownAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

	}

	@Override
	public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode())
		{
			case KeyEvent.VK_W:
				avatarAnimatedShape.stopAnimation();
				grassSound.stop();
				break;
			case KeyEvent.VK_S:
				avatarAnimatedShape.stopAnimation();
				grassSound.stop();
				break;
			case KeyEvent.VK_F:
				avatarAnimatedShape.stopAnimation();
				break;
			case KeyEvent.VK_SPACE:
				avatarAnimatedShape.stopAnimation();
				break;
		}
    }

	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_W:
				if (!avatarAnimatedShape.isPlayingAnimation("RUN")) {
					grassSound.play();
					avatarAnimatedShape.stopAnimation();
					avatarAnimatedShape.playAnimation("RUN", 0.2f, AnimatedShape.EndType.LOOP, 0);
				}
				break;
			case KeyEvent.VK_S:
				if (!avatarAnimatedShape.isPlayingAnimation("BACK")) {
					grassSound.play();
					avatarAnimatedShape.stopAnimation();
					avatarAnimatedShape.playAnimation("BACK", 0.2f, AnimatedShape.EndType.LOOP, 0);
				}
				break;
			case KeyEvent.VK_F:
				if (!avatarAnimatedShape.isPlayingAnimation("PUNCHR")) {
					avatarAnimatedShape.playAnimation("PUNCHR", 0.5f, AnimatedShape.EndType.NONE, 0);
				}
				hitSound.play();
				breakSound.play();
				break;
			case KeyEvent.VK_H:
				deathSound.play();
				break;
			case KeyEvent.VK_SPACE:
				if (!avatarAnimatedShape.isPlayingAnimation("JUMP")) {
					avatarAnimatedShape.playAnimation("JUMP", 0.2f, AnimatedShape.EndType.LOOP, 0);
				}
				jumpSound.play();
				break;
			case KeyEvent.VK_ESCAPE:
				System.out.println("CLIENT SEND BYE MESSAGE");
				protClient.sendByeMessage(playerScore);
				this.shutdown();
				System.exit(0);
				break;
		}
	}

	//Networking methods
	private void setupNetworking()
	{
		isClientConnected = false;
		try
		{
			protClient = new ProtocolClient(InetAddress.getByName(serverAddress), serverPort, serverProtocol, this);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (protClient == null)
		{
			System.out.println("Missing protocol host");
		}
		else
		{
			// ask client protocol to send initial join message
			// to server, with a unique identifier for the client
			protClient.sendJoinMessage();
			protClient.sendNeedBoxObject();
		}
	}

	private void checkForCollisions()
	{
		com.bulletphysics.dynamics.DynamicsWorld dynamicsWorld;
		com.bulletphysics.collision.broadphase.Dispatcher dispatcher;
		com.bulletphysics.collision.narrowphase.PersistentManifold manifold;
		com.bulletphysics.dynamics.RigidBody object1, object2;
		com.bulletphysics.collision.narrowphase.ManifoldPoint contacPoint;

		dynamicsWorld = ((JBulletPhysicsEngine)physicsEngine).getDynamicsWorld();
		dispatcher = dynamicsWorld.getDispatcher();
		int manifoldCount =  dispatcher.getNumManifolds();
		for (int i = 0; i < manifoldCount; i++)
		{
			manifold = dispatcher.getManifoldByIndexInternal(i);
			object1 = (com.bulletphysics.dynamics.RigidBody)manifold.getBody0();
			object2 = (com.bulletphysics.dynamics.RigidBody)manifold.getBody1();
			JBulletPhysicsObject obj1 = JBulletPhysicsObject.getJBulletPhysicsObject(object1);
			JBulletPhysicsObject obj2 = JBulletPhysicsObject.getJBulletPhysicsObject(object2);

			for (int j = 0; j < manifold.getNumContacts(); j++)
			{
				contacPoint = manifold.getContactPoint(j);
				System.out.println("DISTANCE: " + contacPoint.getDistance());
				if (contacPoint.getDistance() < 0.0f)
				{
					System.out.println("---- hit between " + obj1 + " and " + obj2);
					if (avatarP.getUID() == obj1.getUID() || avatarP.getUID() == obj2.getUID())
					{
						canJump = true;
					}
					// running = false;
					break;
				}
			}
		}
	}

	public void updateAvatarPhysicsObject()
	 {
		double[] tempTransform;
		Matrix4f translation = new Matrix4f(avatar.getWorldTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		avatarP.setTransform(tempTransform);
	}

	// UTILITY FUNCITON used by physics
	private float[] toFloatArray(double[] arr)
	{
		if (arr == null)
		{
			return null;
		}
		int n = arr.length;
		float[] ret = new float[n];
		for (int i = 0; i < n; i++)
		{
			ret[i] = (float)arr[i];
		}
		return ret;
	}

	private double[] toDoubleArray(float[] arr)
	{
		if (arr == null)
		{
			return null;
		}
		int n = arr.length;
		double[] ret = new double[n];
		for (int i = 0; i < n; i++)
		{
			ret[i] = (double)arr[i];
		}
		return ret;
	}

	public PhysicsEngine getPhysicsEngine()
	{
		return physicsEngine;
	}

	public void playGrassSound()
	{
		grassSound.play();
	}

	public void playHitSound()
	{
		hitSound.play();
	}

	public void playCollectSound()
	{
		collectSound.play();
	}

	public void createViewports()
	{
		(engine.getRenderSystem()).addViewport("MAIN", 0.0f, 0.0f, 1.0f, 1.0f);

		Viewport mainVp = (engine.getRenderSystem()).getViewport("MAIN");
		mainCamera = mainVp.getCamera();

		mainCamera.setLocation(new Vector3f(-5.0f, 0.0f, 5.0f));
		mainCamera.setU(new Vector3f(1.0f, 0.0f, 0.0f));
		mainCamera.setV(new Vector3f(0.0f, 1.0f, 0.0f));
		mainCamera.setN(new Vector3f(0.0f, 0.0f, -1.0f));
	}

	private void runScript(File scriptFile)
	{
		try
		{
			FileReader fileReader = new FileReader(scriptFile);
			jsEngine.eval(fileReader);
			fileReader.close();
		}
		catch (FileNotFoundException e1)
		{
			System.out.println(scriptFile + " not found " + e1);
		}
		catch (IOException e2)
		{
			System.out.println("IO problem with " + scriptFile + "; " + e2);
		}
		catch (ScriptException e3)
		{
			System.out.println("ScriptException in " + scriptFile + "; " + e3);
		}
		catch (NullPointerException e4)
		{
			System.out.println("Null ptr exception reading " + scriptFile + "; " + e4);
		}
	}

	public GameObject getAvatar()
	{
		return avatar;
	}

	public void setRunning(boolean check)
	{
		running = check;
	}

	public ObjShape getBoxShape()
	{
		return boxS;
	}

	public TextureImage getBoxTexture()
	{
		return boxTex;
	}

	public BoxManager getBoxManager()
	{
		return bm;
	}

	public AnimatedShape getGhostShape()
	{
		return ghostAnimatedShape;
	}

	public TextureImage getGhostTexture()
	{
		return ghostTex;
	}

	public GhostManager getGhostManager()
	{
		return gm;
	}

	public ObjShape getNPCShape()
	{
		return npcShape;
	}

	public TextureImage getNPCTexture()
	{
		return npcTex;
	}

	public Vector3f getNPCDefaultPosition()
	{
		Vector3f ghostPos = new Vector3f((float)((double)jsEngine.get("npcPosX")), (float)((double)jsEngine.get("npcPosY")), 
		(float)((double)jsEngine.get("npcPosZ")));

		return ghostPos;
	}

	public Engine getEngine()
	{
		return engine;
	}

	public Vector3f getPlayerPosition()
	{
		return avatar.getWorldLocation();
	}

	public Vector3f getGhostDefaultPosition()
	{
		Vector3f ghostPos = new Vector3f((float)((double)jsEngine.get("ghostPosX")), (float)((double)jsEngine.get("ghostPosY")), 
		(float)((double)jsEngine.get("ghostPosZ")));

		return ghostPos;
	}

	public boolean isCrownAttach()
	{
		return crownAttach;
	}

	public void setCrownAttach(boolean c)
	{
		crownAttach = c;
	}

	public float getElapseTime()
	{
		return (float)((currFrameTime - lastFrameTime) / 1000.0);
	}

	public int getPlayerScore()
	{
		return playerScore;
	}

	public void increasePlayerScore()
	{
		try
		{
			playerScore = (int)((double)invocableEngine.invokeFunction("updateWinCount", playerScore));
			protClient.sendUpdatePlayerScore(playerScore);
		}
		catch (ScriptException e1)
		{
			System.out.println("ScriptException in " + script2 + "; " + e1);
		}
		catch (NoSuchMethodException e2)
		{
			System.out.println("No such function/method in " + script2 + "; " + e2);
		}
		catch (NullPointerException e3)
		{
			System.out.println("Null ptr exception in " + script2 + "; " + e3);
		}
	}

	public boolean getAvatarCanJump()
	{
		return canJump;
	}

	public void stopJumpAction()
	{
		canJump = false;
	}

	public void toggleAttachController()
	{
		attachNode.toggle();
	}

	public void setIsConnected(boolean b)
	{
		isClientConnected = b;
	}

	public boolean isAvatarCollidingObj(GameObject go)
	{
		return Math.abs(avatar.getLocalLocation().distance(go.getWorldLocation().x(), go.getWorldLocation().y(), 
		go.getWorldLocation().z())) < 1.25 && GameObject.root().isObjAlive(go);
	}

	public void removePrize(NodeController c, GameObject go)
	{
		if (c.isEnabled())
		{
			c.toggle();
			GameObject.root().removeObj(go);
			engine.getSceneGraph().removeGameObject(go);
		}
	}

	// Animation methods
	public AnimatedShape getAvatarAnimatedShape()
	{
		return avatarAnimatedShape;
	}

	public String getAnimationName()
	{
		return aniName;
	}

	public void setAnimationName(String a)
	{
		aniName = a;
	}

	@Override
	public void update()
	{	
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		elapsTime += (currFrameTime - lastFrameTime) / 1000.0;

		// update sound
		bgmSound.setLocation(jukeBoxObject.getWorldLocation());
		grassSound.setLocation(avatar.getWorldLocation());
		hitSound.setLocation(avatar.getWorldLocation());
		breakSound.setLocation(avatar.getWorldLocation());
		jumpSound.setLocation(avatar.getWorldLocation());
		deathSound.setLocation(avatar.getWorldLocation());
		collectSound.setLocation(crown.getWorldLocation());
		setEarParameters();

		// build and set HUD
		int elapsTimeSec = Math.round((float)elapsTime);
		String elapsTimeStr = Integer.toString(elapsTimeSec);
		String dispStr1 = "|| You: " + playerScore + "             ||   "; 
		dispStr1 += elapsTimeStr + "   ||   ";
		dispStr1 = gm.getGhostScore(protClient.getID(), dispStr1);
		
		String dispStr2 = "X = " + avatar.getWorldLocation().x() + "  Y = " + avatar.getWorldLocation().y() + "  Z = " + avatar.getWorldLocation().z();
		Vector3f hud1Color = new Vector3f(1,0,0);
		(engine.getHUDmanager()).setHUD1(dispStr1 + "         " + dispStr2, hud1Color, (engine.getRenderSystem()).getWidth()/3, (engine.getRenderSystem()).getHeight()-hud1Height);

		// update inputs and camera
		im.update((float)elapsTime);

		orbitController.updateCameraPosition();

		// Animation
		avatarAnimatedShape.updateAnimation();

		if (gm.hasGhosts())
		{
			gm.updateGhostAnimation();
		}

		// update altitude of avatar based on height mapping
		Vector3f loc = avatar.getWorldLocation();
		currHeight = worldTerrain.getHeight(loc.x(), loc.z());

		if (currHeight != prevHeight)
		{
			avatar.setLocalLocation(new Vector3f(loc.x(), currHeight, loc.z()));
			running = false;
			stopJumpAction();
			updateAvatarPhysicsObject();
		}

		prevHeight = currHeight;

		// if (isAvatarCollidingObj(crown))
		// {
		// 	attachNode.toggle();
		// 	collectSound.play();
		// }

		// update physics
		if (running)
		{
			Matrix4f mat = new Matrix4f();
			Matrix4f mat2 = new Matrix4f().identity();
			checkForCollisions();
			physicsEngine.update((float)elapsTime);
			for (GameObject go:engine.getSceneGraph().getGameObjects())
			{
				if (go.getPhysicsObject() != null)
				{
					mat.set(toFloatArray(go.getPhysicsObject().getTransform()));
					mat2.set(3, 0, mat.m30());
					mat2.set(3, 1, mat.m31());
					mat2.set(3, 2, mat.m32());
					go.setLocalTranslation(mat2);
				}
			}
		}

		// Script checking if modified
		long modTime = script1.lastModified();
		if (modTime > fileLastModified)
		{
			fileLastModified =  modTime;
			this.runScript(script1);
			Matrix4f initialTranslation = (new Matrix4f()).translation((float)((double)jsEngine.get("avatarPosX")), (float)((double)jsEngine.get("avatarPosY")), 
			(float)((double)jsEngine.get("avatarPosZ")));
			avatar.setLocalTranslation(initialTranslation);

			hud1Height = (int)jsEngine.get("hud1Height");

		}

		if (!isAlive)
		{
			gameOver();
		}


		protClient.processPackets();
		processNetworking((float)elapsTime);
	}

	public void gameOver()
	{
		System.out.println("\n\nYOU WERE KILLED BY THE NPC!!! \nYOUR TOTAL POINTS: " + playerScore);
		protClient.sendByeMessage(playerScore);
		this.shutdown();
		System.exit(0);
	}

	public void setIsAlive(boolean c)
	{
		isAlive = c;
	}

	protected void processNetworking(float elapsTime)
	{
		//Process packets received by the client from the server
		if (protClient != null)
		{
			protClient.processPackets();
		}
	}

	private class SendCloseConnectionPacketAction extends AbstractInputAction
	{
		// for leaving the game...need to attach to an input device
		@Override
		public void performAction(float time, net.java.games.input.Event evt) 
		{
			if (protClient != null && isClientConnected == true)
			{
				protClient.sendByeMessage(playerScore);
			}
		}
	}
}