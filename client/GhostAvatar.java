package client;

import java.util.UUID;

import tage.*;
import tage.shapes.AnimatedShape;

import org.joml.*;

// A ghost MUST be connected as a child of the root,
// so that it will be rendered, and for future removal.
// The ObjShape and TextureImage associated with the ghost
// must have already been created during loadShapes() and
// loadTextures(), before the game loop is started.

public class GhostAvatar extends GameObject
{
	UUID uuid;
	private boolean crown;
	private AnimatedShape animatedShape;

	public GhostAvatar(UUID id, AnimatedShape s, TextureImage t, Vector3f p, boolean crownOn) 
	{	
		super(GameObject.root(), s, t);
		animatedShape = s;
		uuid = id;
		setPosition(p);
		crown = crownOn;
	}

	public UUID getID() { return uuid; }
	public void setPosition(Vector3f m) { setLocalLocation(m); }
	public Vector3f getPosition() { return getWorldLocation(); }
	public void setCrown(boolean c) { crown = c; }
	public boolean getCrown() { return crown; }
	public void updateGhostAnimatedShape() { animatedShape.updateAnimation(); }

	public void playAnimation(String aniName)
	{
		animatedShape.playAnimation(aniName, 0.2f, AnimatedShape.EndType.LOOP, 0);
	}
}
