package tage.ai.behaviortrees;

import java.util.Iterator;

/**
 * <code>BehaviorTree</code> is a class that allows for the easy construction
 * of a Beahvior Tree which is a technique used for creating Artificial 
 * Intelligence in video games. 
 * 
 * @author Kenneth Barnett
 *
 */
public class BehaviorTree 
{
	private BTComposite root;
	private int treeSize;
	
	/**
	 * Traverses the tree in a depth first fashion looking for a composite node
	 * with the specified ID to insert a new node into. 
	 * 
	 * @param id ID of the composite node. 
	 * @param node Current node being searched
	 * @param newNode The node to add to the composite. 
	 */
	private void preOrderHelper(int id, BTBehavior node, BTBehavior newNode)
	{
		if(node == null)
			return;
			
		switch(node.getType())
		{
		case COMPOSITE:
			BTComposite cNode;
			cNode = (BTComposite)node;
			
			if(cNode.getID() == id)
			{
				// Found the node!
				cNode.addChild(newNode);
				treeSize++;
			}
			else
			{
				// Traverse the child nodes of the composite
				Iterator<BTBehavior> it = cNode.getIterator();
				while(it.hasNext() == true)
				{
					preOrderHelper(id, it.next(), newNode);
				}
			}
			break;
		
		case DECORATOR:
			BTDecorator dNode;
			dNode = (BTDecorator)node;
			
			// Traverse the child node of the decorator (it could be a composite)
			preOrderHelper(id, dNode.getChild(), newNode);
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * Constructs a <code>BehaviorTree</code> with either a {@link BTSelector} or {@link BTSequence}
	 * as its root. The rode node has an ID of 0. 
	 * 
	 * @param type The type of composite node the root should be. 
	 */
	public BehaviorTree(BTCompositeType type)
	{
		root = null;
		treeSize = 0;
		
		switch(type)
		{
		case SEQUENCE:
			root = new BTSequence(0);
			treeSize = 1;
			break;
			
		case SELECTOR:
			root = new BTSelector(0);
			treeSize = 1;
			break;
			
		default:
			root = new BTSequence(0);
			treeSize = 1;
			break;
		}
	}
	
	/**
	 * @return The number of nodes in the tree.
	 */
	public int getTreeSize() { return treeSize; }
	
	/**
	 * Inserts a node at the root of the tree. 
	 * 
	 * @param node The {@link BTBehavior} node to insert. 
	 * 
	 * @return TRUE if node successfully inserted; FALSE otherwise. 
	 */
	public boolean insertAtRoot(BTBehavior node)
	{
		if(node == null)
			return false;
			
		root.addChild(node);
		treeSize++;
		
		return true;
	}
	
	/**
	 * Inserts a node into a composite node that already exists in the tree. 
	 * 
	 * @param id The ID of the composite node to insert the new node into. 
	 * 
	 * @param node The {@link BTBehavior} node to insert. 
	 * 
	 * @return TRUE if node successfully inserted; FALSE otherwise.
	 */
	public boolean insert(int id, BTBehavior node)
	{
		if(node == null)
			return false;
			
		if(id < 0)
			return false;
			
		// traverse tree to the node with matching id and add the child to it
		preOrderHelper(id, root, node);
		
		return true;
	}
	
	/**
	 * Updates the behavior tree by ticking through all of the nodes starting with the root node. 
	 * 
	 * @param elapsedTime Time since the last call to BaseGame.update()
	 */
	public void update(float elapsedTime)
	{
		root.tick(elapsedTime);
	}
}