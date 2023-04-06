package a3;

import tage.*;
import tage.shapes.*;

import java.lang.Math;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import org.joml.*;

import tage.input.*;
import tage.input.action.*;
import tage.networking.IGameConnection.ProtocolType;
import tage.nodeControllers.AttachController;
import tage.nodeControllers.RotationController;
import net.java.games.input.*;
import net.java.games.input.Component.Identifier.*;
import net.java.games.input.Event;

import javax.script.Invocable;
import javax.script.ScriptEngine;

public class MyGame extends VariableFrameRateGame
{
	private int score=0, itemHolding=0;
	private double lastFrameTime, currFrameTime, elapsTime;

	private static Engine engine;
	private GameObject cub, avatar, x, y, z, diamond, trapObj, worldPlane;
	private ObjShape dolS, cubS, linxS, linyS, linzS, diamondS, trapObjS, ghostS;
	private TextureImage doltx, blueWall, binCollector, bombSkin, oceanTexture, ghostT;
	private Light light1, light2;
	private InputManager im;
	private Camera leftCamera, rightCamera;
	private CameraOrbitController orbitController;
	private CameraOverviewController overviewController;
	private WorldPlane wPlane;
	private NodeController rotationNode;
	private AttachController attachNode, attachNode2, attachNode3;

	private int fluffyClouds; //skybox

	private LinkedList<GameObject> diamondList = new LinkedList<>();
	private LinkedList<GameObject> trapList = new LinkedList<>();

	private Random rand = new Random();

	// Ghost and Connection
	private GhostManager gm;
	private String serverAddress;
	private int serverPort;
	private ProtocolType serverProtocol;
	private ProtocolClient protClient;
	private boolean isClientConnected = false;

	// Script
	private File script1;
	private long fileLastModified = 0;
	ScriptEngine jsEngine;

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
	{	dolS = new ImportedModel("player.obj");
		ghostS = new ImportedModel("player.obj");
		cubS = new Cube();
		trapObjS = new Sphere();

		//Coordinate system
		linxS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(3f, 0f, 0f));
		linyS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 3f, 0f));
		linzS = new Line(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, -3f));

		//Diamond object
		diamondS = new ManualDiamond();

		//Plane object
		wPlane = new WorldPlane();

	}

	@Override
	public void loadTextures()
	{
		doltx = new TextureImage("player_uv.png");
		ghostT = new TextureImage("ghost_uv.png");
		blueWall = new TextureImage("BlueWall.png");
		binCollector = new TextureImage("CollectorLogo.png");
		bombSkin = new TextureImage("BombSkin.png");
		oceanTexture = new TextureImage("OceanFloor.png");
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
	{	Matrix4f initialTranslation, initialRotation, initialScale;

		// build dolphin in the center of the window
		avatar = new GameObject(GameObject.root(), dolS, doltx);
		initialTranslation = (new Matrix4f()).translation(-2.0f,0.0f, 2.0f);
		initialScale = (new Matrix4f()).scaling(0.5f);
		avatar.setLocalTranslation(initialTranslation);
		avatar.setLocalScale(initialScale);
		initialRotation = (new Matrix4f()).rotationY((float)java.lang.Math.toRadians(135.0f));
		avatar.setLocalRotation(initialRotation);

		//build cube at the right of the window
		cub = new GameObject(GameObject.root(), cubS, binCollector);
		initialTranslation = (new Matrix4f()).translation(2,0,-10);
		initialScale = (new Matrix4f()).scaling(2.0f);
		cub.setLocalTranslation(initialTranslation);
		cub.setLocalScale(initialScale);

		//build X, Y, -Z axis
		x = new GameObject(GameObject.root(), linxS);
		y = new GameObject(GameObject.root(), linyS);
		z = new GameObject(GameObject.root(), linzS);
		(x.getRenderStates()).setColor(new Vector3f(1f,0,0));
		(y.getRenderStates()).setColor(new Vector3f(0,1f,0));
		(z.getRenderStates()).setColor(new Vector3f(0,0,1f));


		// build diamond list
		for (int i = 0; i < 3; i++)
		{
			diamond = new GameObject(GameObject.root(), diamondS, blueWall);
			initialTranslation = (new Matrix4f().translation(rand.nextInt(14) + (-rand.nextInt(14)), 0.0f, rand.nextInt(14)));
			initialScale = (new Matrix4f()).scaling(0.25f);
			diamond.setLocalTranslation(initialTranslation);
			diamond.getRenderStates().hasLighting(true);
			diamond.setLocalScale(initialScale);
			diamondList.add(diamond);
		}

		//build trap list
		for (int i = 0; i < 7; i++)
		{
			trapObj = new GameObject(GameObject.root(), trapObjS, bombSkin);
			initialTranslation = (new Matrix4f()).translation(rand.nextInt(14) + (-rand.nextInt(14)), 0.0f, rand.nextInt(14));
			initialScale = (new Matrix4f()).scaling(0.25f);
			trapObj.setLocalTranslation(initialTranslation);
			trapObj.setLocalScale(initialScale);
			trapList.add(trapObj);
		}

		//building World Plane
		worldPlane = new GameObject(GameObject.root(), wPlane, oceanTexture);
		initialTranslation = (new Matrix4f()).translation(0.0f, -0.5f, 0.0f);
		initialScale = (new Matrix4f()).scaling(15.0f);
		worldPlane.setLocalTranslation(initialTranslation);
		worldPlane.setLocalScale(initialScale);
	}

	@Override
	public void initializeLights()
	{	Light.setGlobalAmbient(0.5f, 0.5f, 0.5f);
		light1 = new Light();
		light1.setLocation(new Vector3f(5.0f, 4.0f, 2.0f));
		(engine.getSceneGraph()).addLight(light1);
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

		// -------------- Node Controllers --------------------------
		rotationNode = new RotationController(engine, new Vector3f(0.0f, 1.0f, 0.0f), 0.001f);
		attachNode = new AttachController(avatar);
		attachNode2 = new AttachController(avatar);
		attachNode3 = new AttachController(avatar);
		for (int i = 0; i < trapList.size(); i++)
		{
			changeLocationByAvatar(trapList.get(i));
		}
		for (int i = 0; i < diamondList.size(); i++)
		{
			rotationNode.addTarget(diamondList.get(i));
			changeLocationByAvatar(diamondList.get(i));
			changeLocationByTrap(diamondList.get(i));
		}
		
		attachNode.addTarget(diamondList.get(0));
		attachNode2.addTarget(diamondList.get(1));
		attachNode3.addTarget(diamondList.get(2));

		(engine.getSceneGraph()).addNodeController(rotationNode);
		(engine.getSceneGraph()).addNodeController(attachNode);
		(engine.getSceneGraph()).addNodeController(attachNode2);
		(engine.getSceneGraph()).addNodeController(attachNode3);
		rotationNode.toggle();
		
		// ------------- Creating the cameras/viewports -------------
		createViewports();
		// --------------------- INPUT SECTION -----------------------
		im = engine.getInputManager();
		String gpName = im.getFirstGamepadName();
		
		orbitController = new CameraOrbitController(this, leftCamera, avatar, gpName, engine);
		overviewController = new CameraOverviewController(this, rightCamera, avatar, gpName, engine);

		FwdAction fwdAction = new FwdAction(this, protClient);
		BackAction backAction = new BackAction(this, protClient);
		TurnLeftAction turnLeftAction = new TurnLeftAction(this, protClient);
		TurnRightAction turnRightAction = new TurnRightAction(this, protClient);
		GamePadTurn gamePadTurn = new GamePadTurn(this);
		GamePadAction gamePadAction = new GamePadAction(this);
		ToggleAxis toggleAxis = new ToggleAxis(x, y, z);

		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.TAB, toggleAxis, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.W, fwdAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.S, backAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.A, turnLeftAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.D, turnRightAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.X, gamePadTurn, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		im.associateActionWithAllGamepads(net.java.games.input.Component.Identifier.Axis.Z, gamePadAction, InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
	}

	public void createViewports()
	{
		(engine.getRenderSystem()).addViewport("LEFT", 0.0f, 0.0f, 1.0f, 1.0f);
		(engine.getRenderSystem()).addViewport("RIGHT", 0.75f, 0.0f, 0.25f, 0.25f);

		Viewport leftVp = (engine.getRenderSystem()).getViewport("LEFT");
		Viewport rightVp = (engine.getRenderSystem()).getViewport("RIGHT");
		leftCamera = leftVp.getCamera();
		rightCamera = rightVp.getCamera();

		rightVp.setHasBorder(true);
		rightVp.setBorderWidth(4);
		rightVp.setBorderColor(0.0f, 1.0f, 0.0f);

		leftCamera.setLocation(new Vector3f(-2.0f, 0.0f, 2.0f));
		leftCamera.setU(new Vector3f(1.0f, 0.0f, 0.0f));
		leftCamera.setV(new Vector3f(0.0f, 1.0f, 0.0f));
		leftCamera.setN(new Vector3f(0.0f, 0.0f, -1.0f));

		rightCamera.setLocation(new Vector3f(0.0f, 4.0f, 0.0f));
		rightCamera.setU(new Vector3f(1.0f, 0.0f, 0.0f));
		rightCamera.setV(new Vector3f(0.0f, 0.0f, -1.0f));
		rightCamera.setN(new Vector3f(0.0f, -1.0f, 0.0f));

	}

	public GameObject getAvatar()
	{
		return avatar;
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

	public Engine getEngine()
	{
		return engine;
	}

	public Vector3f getPlayerPosition()
	{
		return avatar.getWorldLocation();
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

	public void changeLocationByTrap(GameObject go)
	{
		Matrix4f initialTranslation;
		
		for (int i = 0; i < trapList.size(); i++)
		{
			if (Math.abs(go.getLocalLocation().distance(trapList.get(i).getWorldLocation().x(), trapList.get(i).getWorldLocation().y(), 
			trapList.get(i).getWorldLocation().z())) <= 3.0)
			{
				initialTranslation = (new Matrix4f().translation(rand.nextInt(14) + (-rand.nextInt(14)), 0.0f,  rand.nextInt(14)));
				go.setLocalTranslation(initialTranslation);
				changeLocationByAvatar(go);
				changeLocationByTrap(go);
			}
		}

	}
	public void changeLocationByAvatar(GameObject go)
	{
		Matrix4f initialTranslation;
		if (Math.abs(avatar.getLocalLocation().distance(go.getWorldLocation().x(), go.getWorldLocation().y(), 
		go.getWorldLocation().z())) <= 4.0)
		{
			initialTranslation = (new Matrix4f().translation(rand.nextInt(14) + (-rand.nextInt(14)), 0.0f, rand.nextInt(14)));
			go.setLocalTranslation(initialTranslation);
			changeLocationByAvatar(go);
		}
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

	@Override
	public void update()
	{	
		lastFrameTime = currFrameTime;
		currFrameTime = System.currentTimeMillis();
		elapsTime += (currFrameTime - lastFrameTime) / 1000.0;

		// build and set HUD
		int elapsTimeSec = Math.round((float)elapsTime);
		String elapsTimeStr = Integer.toString(elapsTimeSec);
		String scorerStr = Integer.toString(score);
		String itemHoldStr = Integer.toString(itemHolding);
		String dispStr1 = "Time = " + elapsTimeStr + "      Score = " + scorerStr + "      Item Holding = " + itemHoldStr;
		String dispStr2 = "X = " + avatar.getWorldLocation().x() + "  Y = " + avatar.getWorldLocation().y() + "  Z = " + avatar.getWorldLocation().z();
		Vector3f hud1Color = new Vector3f(1,0,0);
		Vector3f hud2Color = new Vector3f(0,0,1);
		(engine.getHUDmanager()).setHUD1(dispStr1, hud1Color, 15, 15);
		(engine.getHUDmanager()).setHUD2(dispStr2, hud2Color, (int)((engine.getRenderSystem()).getSize().getWidth()-(engine.getRenderSystem()).getViewport("RIGHT").getActualWidth()), 15);


		//update inputs and camera
		im.update((float)elapsTime);
		orbitController.updateCameraPosition();
		overviewController.updateOverviewPosition();

		for (int i = 0; i < trapList.size(); i++)
		{
			if (isAvatarCollidingObj(trapList.get(i)))
			{
				GameObject.root().removeObj(trapList.get(i));
				score -= 1;
			}
		}

		if (isAvatarCollidingObj(diamondList.get(0)))
		{
			itemHolding += 1;
			attachNode.toggle();
		}
		if (isAvatarCollidingObj(diamondList.get(1)))
		{
			itemHolding += 1;
			attachNode2.toggle();
		}
		if (isAvatarCollidingObj(diamondList.get(2)))
		{
			itemHolding += 1;
			attachNode3.toggle();
		}
			
		if (Math.abs(avatar.getLocalLocation().distance(cub.getWorldLocation().x(), cub.getWorldLocation().y(), 
		cub.getWorldLocation().z())) <= 2)
		{
			if (itemHolding != 0)
			{
				removePrize(attachNode, diamondList.get(0));
				removePrize(attachNode2, diamondList.get(1));
				removePrize(attachNode3, diamondList.get(2));
				score++;
				itemHolding--;
			}
		}

		processNetworking((float)elapsTime);
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