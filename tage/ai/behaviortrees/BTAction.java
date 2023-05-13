package tage.ai.behaviortrees;

/**
 * <code>BTAction</code> represents an action behavior node in a behavior tree.
 * All this class does is sets the nodes {@link BTNodeType} to ACTION. Users 
 * wanting to create their own action behaviors extend this class. 
 * 
 * @author Kenneth Barnett
 * @author Alex J. Champandard
 * 
 * @see <a href="http://aigamedev.com/insider/tutorial/second-generation-bt/">Behavior Trees Tutorial Video</a><p>
 * 		<a href="https://github.com/aigamedev/btsk">Behavior Trees Starter Kit</a>
 */
public abstract class BTAction extends BTBehavior
{
	/**
	 * Constructs a <code>BTAction</code>.  
	 */
	public BTAction()
	{
		super();
		this.setNodeType(BTNodeType.ACTION);
	}
}