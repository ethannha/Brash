package tage.ai.behaviortrees;

/**
 * <code>BTDecorator</code> is an abstract behavior node that contains a single 
 * {@link BTBehavior} child. A <code>BTDecorator</code> node is meant to enforce
 * how its child node executes or enforce a specific return status. 
 * 
 * @author Kenneth Barnett
 * @author Alex J. Champandard
 * 
 * @see <a href="http://aigamedev.com/insider/tutorial/second-generation-bt/">Behavior Trees Tutorial Video</a><p>
 * 		<a href="https://github.com/aigamedev/btsk">Behavior Trees Starter Kit</a>
 *
 */
public abstract class BTDecorator extends BTBehavior
{
	protected BTBehavior child;		// The child node.
	
	/**
	 * Constructs a <code>BTDecorator</code> node encapsulating a child node. 
	 * @param child The child node. 
	 */
	public BTDecorator(BTBehavior child)
	{
		super();
		this.child = child;
		this.setNodeType(BTNodeType.DECORATOR);
	}
	
	/**
	 * @return The <code>BTDecorator</code>'s child node.
	 */
	public BTBehavior getChild() { return child; }
}