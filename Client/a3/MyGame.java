package a3;

import tage.*;
import tage.shapes.*;

import java.lang.Math;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.awt.event.*;
import java.io.*;

import org.joml.*;

import tage.CameraOrbitController;
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
	private int score=0, itemHolding=0;
	private double lastFrameTime, currFrameTime, elapsTime;

	private static Engine engine;
	private GameObject avatar, prizeItem, worldTerrain, boxObject;
	private ObjShape avatarS, prizeItemS, ghostS, terrainS, boxS;
	private TextureImage avatarT, ghostT, hills, grass, prizeTexture, boxTexture;
	private Light light1;
	private InputManager im;
	private Camera mainCamera;
	private CameraOrbitController orbitController;
	private NodeController rotationNode;
	private AttachController attachNode;
	private IAudioManager audioMgr;
	private Sound bgmSound, grassSound, collectSound, hitSound, jumpSound, deathSound;

	private int fluffyClouds; //skybox

	private float currHeight, prevHeight;

	// Ghost and Connection
	private GhostManager gm;
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private ProtocolClient protClient;
	private boolean isClientConnected = false;

	// Script
	private File script1, script2;
	private long fileLastModified = 0;
	ScriptEngine jsEngine;
	Invocable invocableEngine;

	// Player
	private double player1WinCounter;
	private double player2WinCounter;

	// Physics
	private PhysicsEngine physicsEngine;
	private PhysicsObject avatarP, planeP;
	private boolean running = false;
	private float vals[] = new float[16];

	// Animation
	private AnimatedShape avatarAnimatedShape;

	// NPC/AI
	private ObjShape npcShape;
	private TextureImage npcTex;


	public MyGame(String serverAddress, int serverPort, String protocol) 
	{ 
		super(); 
		gm = new GhostManager(this);
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
		//avatarS = new ImportedModel("player.obj");
		avatarAnimatedShape = new AnimatedShape("player.rkm", "player.rks");
		avatarAnimatedShape.loadAnimation("RUN", "player_run.rka");
		avatarAnimatedShape.loadAnimation("IDLE", "player_idle.rka");
		avatarAnimatedShape.loadAnimation("BACK", "player_backwalk.rka");
		avatarAnimatedShape.loadAnimation("JUMP", "player_jump.rka");
		avatarAnimatedShape.loadAnimation("PUNCHL", "player_punchL.rka");
		avatarAnimatedShape.loadAnimation("PUNCHR", "player_punchR.rka");
		ghostS = new ImportedModel("player.obj");
		npcShape = new ImportedModel("player.obj");
		terrainS = new TerrainPlane(1000);

		//Diamond object
		prizeItemS = new ImportedModel("crown.obj");
		boxS = new Cube();

	}

	@Override
	public void loadTextures()
	{
		avatarT = new TextureImage("player_uv.png");
		ghostT = new TextureImage("ghost_uv.png");
		prizeTexture = new TextureImage("crown2_texture.png");
		hills = new TextureImage("hill2.png");
		grass = new TextureImage("grass2.png");
		boxTexture = new TextureImage("BlueWall.png");
		npcTex = new TextureImage("player_uv.png");

	}

	@Override
	public void loadSkyBoxes()
	{
		fluffyClouds = (engine.getSceneGraph()).loadCubeMap("fluffyClouds");
		(engine.getSceneGraph()).setActiveSkyBoxTexture(fluffyClouds);
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
		avatar = new GameObject(GameObject.root(), avatarAnimatedShape, avatarT);
		//avatar = new GameObject(GameObject.root(), avatarS, avatarT);
		initialTranslation = (new Matrix4f()).translation((float)((double)jsEngine.get("avatarPosX")), (float)((double)jsEngine.get("avatarPosY")), 
		(float)((double)jsEngine.get("avatarPosZ")));

		initialScale = (new Matrix4f()).scaling(0.2f);
		avatar.setLocalTranslation(initialTranslation);
		avatar.setLocalScale(initialScale);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(135.0f));
		avatar.setLocalRotation(initialRotation);

		// build prize
		prizeItem = new GameObject(GameObject.root(), prizeItemS, prizeTexture);
		initialTranslation = (new Matrix4f().translation(3, 1, -3));
		initialScale = (new Matrix4f()).scaling(0.25f);
		prizeItem.setLocalTranslation(initialTranslation);
		prizeItem.getRenderStates().hasLighting(true);
		prizeItem.setLocalScale(initialScale);

		// build world terrain object
		worldTerrain = new GameObject(GameObject.root(), terrainS, grass);
		initialTranslation = (new Matrix4f()).translation(0f,0f,0f);
		worldTerrain.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(20.0f, 1.0f, 20.0f);
		worldTerrain.setLocalScale((initialScale));
		worldTerrain.setHeightMap(hills);

		// Box
		boxObject = new GameObject(GameObject.root(), boxS, boxTexture);
		initialTranslation =  (new Matrix4f().translation(6, 1, 2));
		initialScale = (new Matrix4f()).scaling(0.5f);
		boxObject.setLocalTranslation(initialTranslation);
		initialScale = (new Matrix4f()).scaling(1.0f);
		boxObject.setLocalScale(initialScale);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(135.0f));
		boxObject.setLocalRotation(initialRotation);

	}

	@Override
	public void initializeLights()
	{	Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
		light1 = new Light();
		light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
		(engine.getSceneGraph()).addLight(light1);
	}

	public void initAudio()
	{ 
		AudioResource resource1, resource2, resource3, resource4, resource5, resource6;
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

		bgmSound = new Sound(resource1, SoundType.SOUND_EFFECT, 50, true);
		grassSound = new Sound(resource2, SoundType.SOUND_EFFECT, 50, true);
		collectSound = new Sound(resource3, SoundType.SOUND_EFFECT, 100, false);
		hitSound = new Sound(resource4, SoundType.SOUND_EFFECT, 100, false);
		jumpSound = new Sound(resource5, SoundType.SOUND_EFFECT, 100, false);
		deathSound = new Sound(resource6, SoundType.SOUND_EFFECT, 100, false);
		
		bgmSound.initialize(audioMgr);

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

		jumpSound.initialize(audioMgr);
		jumpSound.setLocation(avatar.getWorldLocation());

		deathSound.initialize(audioMgr);
		deathSound.setLocation(avatar.getWorldLocation());
		
		collectSound.initialize(audioMgr);
		collectSound.setMaxDistance(10.0f);
		collectSound.setMinDistance(0.5f);
		collectSound.setRollOff(5.0f);
		collectSound.setLocation(prizeItem.getWorldLocation());

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
		rotationNode.addTarget(prizeItem);
		attachNode.addTarget(prizeItem);

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
		player1WinCounter = ((int)jsEngine.get("p1Win"));
		player2WinCounter = ((int)jsEngine.get("p2Win"));

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
		avatarP.setBounciness(1.0f);
		avatar.setPhysicsObject(avatarP);

		translation = new Matrix4f(worldTerrain.getLocalTranslation());
		tempTransform = toDoubleArray(translation.get(vals));
		planeP = physicsEngine.addStaticPlaneObject(physicsEngine.nextUID(), tempTransform, up, 0.0f);
		planeP.setBounciness(1.0f);
		worldTerrain.setPhysicsObject(planeP);

		// --------------------- INPUT SECTION -----------------------
		im = engine.getInputManager();
		String gpName = im.getFirstGamepadName();
		
		orbitController = new CameraOrbitController(mainCamera, avatar, gpName, engine);

		FwdAction fwdAction = new FwdAction(this, protClient);
		BackAction backAction = new BackAction(this, protClient);
		TurnLeftAction turnLeftAction = new TurnLeftAction(this, protClient);
		TurnRightAction turnRightAction = new TurnRightAction(this, protClient);
		GamePadTurn gamePadTurn = new GamePadTurn(this);
		GamePadAction gamePadAction = new GamePadAction(this);
		//ShutDownAction shutDownAction = new ShutDownAction(this, protClient);
		JumpAction jumpAction = new JumpAction(this, physicsEngine, avatarP);

		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.W, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.S, backAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.A, turnLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.D, turnRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		//im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.ESCAPE, shutDownAction, INPUT_ACTION_TYPE.ON_PRESS_AND_RELEASE);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.SPACE, jumpAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.X, gamePadTurn, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Z, gamePadAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
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
				if (!avatarAnimatedShape.isPlayingAnimation("PUNCHL")) {
					avatarAnimatedShape.stopAnimation();
					avatarAnimatedShape.playAnimation("PUNCHL", 0.5f, AnimatedShape.EndType.LOOP, 0);
				}
				hitSound.play();
				break;
			case KeyEvent.VK_H:
				deathSound.play();
				break;
			case KeyEvent.VK_SPACE:
				if (!avatarAnimatedShape.isPlayingAnimation("JUMP")) {
					avatarAnimatedShape.stopAnimation();
					avatarAnimatedShape.playAnimation("JUMP", 0.2f, AnimatedShape.EndType.LOOP, 0);
				}
				jumpSound.play();
				break;
			case KeyEvent.VK_ESCAPE:
				System.out.println("CLIENT SEND BYE MESSAGE");
				protClient.sendByeMessage();
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
					running = false;
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

	public ObjShape getGhostShape()
	{
		return ghostS;
	}

	public TextureImage getGhostTexture()
	{
		return ghostT;
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

	public float getElapseTime()
	{
		return (float)((currFrameTime - lastFrameTime) / 1000.0);
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

	@Override
	public void update()
	{	
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		elapsTime += (currFrameTime - lastFrameTime) / 1000.0;

		// update sound
		grassSound.setLocation(avatar.getWorldLocation());
		hitSound.setLocation(avatar.getWorldLocation());
		jumpSound.setLocation(avatar.getWorldLocation());
		deathSound.setLocation(avatar.getWorldLocation());
		collectSound.setLocation(prizeItem.getWorldLocation());
		setEarParameters();

		// build and set HUD
		int elapsTimeSec = Math.round((float)elapsTime);
		String elapsTimeStr = Integer.toString(elapsTimeSec);
		String scorerStr = Integer.toString(score);
		String itemHoldStr = Integer.toString(itemHolding);
		String dispStr1 = "Time = " + elapsTimeStr + "      Score = " + scorerStr + "      Item Holding = " + itemHoldStr + 
		"            P1: " + (int)player1WinCounter + "     P2: " + (int)player2WinCounter;
		String dispStr2 = "X = " + avatar.getWorldLocation().x() + "  Y = " + avatar.getWorldLocation().y() + "  Z = " + avatar.getWorldLocation().z();
		Vector3f hud1Color = new Vector3f(1,0,0);
		(engine.getHUDmanager()).setHUD1(dispStr1 + "         " + dispStr2, hud1Color, 15, 15);

		// update inputs and camera
		im.update((float)elapsTime);

		orbitController.updateCameraPosition();

		// Animation
		avatarAnimatedShape.updateAnimation();

		// update altitude of avatar based on height mapping
		Vector3f loc = avatar.getWorldLocation();
		currHeight = worldTerrain.getHeight(loc.x(), loc.z());

		if (currHeight != prevHeight)
		{
			avatar.setLocalLocation(new Vector3f(loc.x(), currHeight, loc.z()));
		}

		prevHeight = currHeight;

		if (isAvatarCollidingObj(prizeItem))
		{
			itemHolding += 1;
			attachNode.toggle();
			collectSound.play();
		}

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

		}
		//System.out.println("AVATARP TRANSFORM: " + toFloatArray(avatarP.getTransform())[12] + ", " + toFloatArray(avatarP.getTransform())[13] + ", " + toFloatArray(avatarP.getTransform())[14]);

		if (Math.abs(avatar.getLocalLocation().distance(boxObject.getWorldLocation().x(), boxObject.getWorldLocation().y(), 
		boxObject.getWorldLocation().z())) <= 2)
		{
			if (itemHolding != 0)
			{
				removePrize(attachNode, prizeItem);
				score++;
				try
				{
					player1WinCounter =  (double)invocableEngine.invokeFunction("updateWinCount", player1WinCounter);
					//player2WinCounter = (double)invocableEngine.invokeFunction("updateWinCount", player2WinCounter);
					
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
				itemHolding--;
			}
		}
		protClient.processPackets();
		processNetworking((float)elapsTime);
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
				protClient.sendByeMessage();
			}
		}
	}
}