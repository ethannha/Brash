package tage;
import org.joml.*;
import java.util.*;
import tage.shapes.*;

/**
* TextureImage holds all raw data associated with a particular texture image.
* <br>
* Specifically, it includes the following:
* <ul>
* <li> a String containing the associated texture file pathname
* <li> an integer reference to the associated OpenGL texture object
* </ul>
* <p>
* Each GameObject typically is associated with one TextureImage.
* More than one GameObject can use the same TextureImage.
* All texture images must be read in during init().
* However, assignments of texture images to game objects can be made or changed later.
* @author Scott Gordon
*/
public class TextureImage
{
	private String textureFile;
	private int texture;

	public TextureImage()
	{	
	}

	/** Loads a texture image file and uses it to build a new TextureImage object. */
	public TextureImage(String texFile)
	{	textureFile = "assets/textures/" + texFile;
		Engine.getEngine().getRenderSystem().addTexture(this);		
	}

	protected void setTexture(int t) { texture = t; }

	/** for engine use */
	public void setTextureFile(String t) { textureFile = t; }
	/** for engine use */
	public String getTextureFile() { return textureFile; }
	/** for engine use */
	public int getTexture() { return texture; }
}