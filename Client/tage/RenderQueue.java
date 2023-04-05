package tage;
import java.util.*;

/**
* Builds a render queue by traversing the GameObjects as specified in the scenegraph tree.
* After building the queue as a Vector, it makes available an iterator for the queue.
* It is used by the engine before rendering each frame,
* and none of the functions should be called directly by the game application.
* <p>
* Eventually, the plan is to support transparency, such that transparent objects are
* moved to the end of the queue.  But this is not yet implemented.  As of now, all methods are protected.
* @author Scott Gordon
*/

public class RenderQueue
{
	private Vector<GameObject> queue;
	private GameObject root;

	protected RenderQueue(GameObject r)
	{	queue = new Vector<GameObject>();
		root = r;
	}

	// A standard queue includes all of the game objects.
	// It is built by starting at the root and traversing all of the
	// children and their descendents, adding them to the queue.

	protected Vector<GameObject> createStandardQueue()
	{	GameObject current = root;
		queue.clear();
		addToQueue(root.getChildrenIterator());
		return queue;
	}

	protected void addToQueue(GameObject g) { queue.add(g); }

	// Recursive traversal of the game objects

	protected void addToQueue(Iterator goIterator)
	{	while (goIterator.hasNext())
		{	GameObject go = (GameObject) goIterator.next();
			addToQueue(go);
			if (go.hasChildren()) addToQueue(go.getChildrenIterator());
		}
	}

	protected Iterator getIterator() { return queue.iterator(); }
}