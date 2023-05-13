package client;

import java.awt.Color;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.joml.*;

import tage.*;

public class GhostManager
{
	private MyGame game;
	private Vector<GhostAvatar> ghostAvatars = new Vector<GhostAvatar>();
	private Map<String, Integer> ghostScoreList = new ConcurrentHashMap<>();

	public GhostManager(VariableFrameRateGame vfrg)
	{	
		game = (MyGame)vfrg;
	}
	
	public void createGhostAvatar(UUID id, Vector3f position, int score) throws IOException
	{	
		System.out.println("adding ghost with ID --> " + id);
		ObjShape s = game.getGhostShape();
		TextureImage t = game.getGhostTexture();
		GhostAvatar newAvatar = new GhostAvatar(id, s, t, position);
		Matrix4f initialScale = (new Matrix4f()).scaling(0.25f);
		newAvatar.setLocalScale(initialScale);
		ghostAvatars.add(newAvatar);
		addPlayer(id, score);
	}
	
	public void removeGhostAvatar(UUID id)
	{	
		GhostAvatar ghostAvatar = findAvatar(id);
		if(ghostAvatar != null)
		{	
			game.getEngine().getSceneGraph().removeGameObject(ghostAvatar);
			ghostAvatars.remove(ghostAvatar);
			ghostScoreList.remove(id.toString());
		}
		else
		{	
			System.out.println("tried to remove, but unable to find ghost in list");
		}
	}

	public void addPlayer(UUID clientID, int score)
    {
        System.out.println("=================== ADDING PLAYER: " + clientID.toString());
        ghostScoreList.put(clientID.toString(), score);
    }

	public Vector<GhostAvatar> getAllGhost()
	{
		return ghostAvatars;
	}

	public String getGhostScore(UUID clientID, String message)
	{
		if (ghostScoreList.isEmpty())
		{
			return message;
		}
		else
		{
			int counter = 1;
			for (Map.Entry<String, Integer> entry : ghostScoreList.entrySet())
			{
				String ghostID = entry.getKey();
				
				if (!ghostID.equals(clientID.toString()))
				{
					int score = entry.getValue();
					message += "Ghost " + counter + ": " + score + "   ||";
					counter++;
				}
			}
			return message;
		}
		
	}

	public int getGhostScore(UUID ghostID)
	{
		return ghostScoreList.get(ghostID.toString());
	}

	private GhostAvatar findAvatar(UUID id)
	{	
		GhostAvatar ghostAvatar;
		Iterator<GhostAvatar> it = ghostAvatars.iterator();
		while(it.hasNext())
		{	
			ghostAvatar = it.next();
			if(ghostAvatar.getID().compareTo(id) == 0)
			{	
				return ghostAvatar;
			}
		}		
		return null;
	}

	public void updateGhostScore(UUID clientID, int score)
    {
        ghostScoreList.replace(clientID.toString(), score);
    }
	
	public void updateGhostAvatar(UUID id, Vector3f position)
	{	
		GhostAvatar ghostAvatar = findAvatar(id);
		if (ghostAvatar != null)
		{	
			ghostAvatar.setPosition(position);
		}
		else
		{	
			System.out.println("tried to update ghost avatar position, but unable to find ghost in list");
		}
	}

	public void updateGhostAvatarRotation(UUID id, Matrix4f rotation)
	{
		GhostAvatar ghostAvatar = findAvatar(id);
		if (ghostAvatar != null)
		{	
			ghostAvatar.setLocalRotation(rotation);
		}
		else
		{	
			System.out.println("tried to update ghost avatar position, but unable to find ghost in list");
		}
	}
}
