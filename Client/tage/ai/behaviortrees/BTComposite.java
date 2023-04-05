package tage.ai.behaviortrees;

import java.util.Iterator;
import java.util.Vector;

/**
 * A {@code BTComposite} node is an inner node that maintains a collection of {@code BTBehavior} child nodes.
 * <code>BTComposite</code> is implemented as an abstract class. 
 * <code>BTComposite</code> nodes contain an ID variable so that it can be distinguished between other composite
 * nodes. This helps when inserting nodes into a behavior tree. 
 * 
 * @author Kenneth Barnett
 * @author Alex J. Champandard
 * 
 * @see <a href="http://aigamedev.com/insider/tutorial/second-generation-bt/">Behavior Trees Tutorial Video</a><p>
 * 		<a href="https://github.com/aigamedev/btsk">Behavior Trees Starter Kit</a>
 */
public abstract class BTComposite extends BTBehavior 
{
	protected Vector<BTBehavior> children;		// The collection of child nodes. 
	protected int id;							// The ID of the composite node. 
	
	/**
	 * Constructs a <code>BTComposite</code> with an associated ID. 
	 * @param id The ID of the composite node. 
	 */
	public BTComposite(int id) 
	{
		super();
		this.id = id;
		this.setNodeType(BTNodeType.COMPOSITE);
		this.children = new Vector<BTBehavior>();
	}
	
	/**
	 * Adds a new {@link BTBehavior} to the composite's collection of child nodes if the child isn't null. 
	 * @param child The child node to add. 
	 */
	public void addChild(BTBehavior child)
	{
		if(child == null)
			return;
		
		children.add(child);
	}
	
	/**
	 * Removes the specified {@link BTBehavior} node from the composite's collection of child nodes. 
	 * @param child The child node to remove. 
	 */
	public void removeChild(BTBehavior child)
	{
		if(child == null)
			return;
		
		children.remove(child);
	}
	
	/**
	 * Removes all nodes from the composite's collection of child nodes. 
	 */
	public void ClearChildren()
	{
		children.clear();
	}
	
	/**
	 * @return The number of child nodes the composite has. 
	 */
	public int getNumberOfChildren() { return children.size(); }
	
	/**
	 * @return The ID of the <code>BTComposite</code>
	 */
	public int getID() { return id; }
	
	/**
	 * @return An iterator for the composite's collection of {@link BTBehavior} child nodes. 
	 */
	public Iterator<BTBehavior> getIterator() { return children.iterator(); }
}
