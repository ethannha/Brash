package tage.ai.behaviortrees;

/**
 * <code>BTBehavior</code> is an abstract base class that represents
 * a node in a behavior tree. <code>BTBehavior</code> is the base
 * class for other behavior tree nodes such as actions, conditions,
 * sequences, and selectors.  
 * 
 * @author Kenneth Barnett
 * @author Alex J. Champandard
 * 
 * @see <a href="http://aigamedev.com/insider/tutorial/second-generation-bt/">Behavior Trees Tutorial Video</a><p>
 * 		<a href="https://github.com/aigamedev/btsk">Behavior Trees Starter Kit</a>
 */
public abstract class BTBehavior 
{
	private BTStatus status;	// The return status of the node
	private BTNodeType type;	// The type of node
	
	/**
	 * Updates the node. 
	 * 
	 * @param elapsedTime Time since the last call to BaseGame.update()
	 * @return The return {@link BTStatus} of the node.
	 */
	protected abstract BTStatus update(float elapsedTime);
	
	/**
	 * Performs initialization on the node. By default this method doesn't do anything, but
	 * users can override this method to add initialization that is specific to the behavior
	 * they are creating. 
	 */
	protected void onInitialize() {}
	
	/**
	 * Performs any termination that needs to be done on the node when it finishes processing.
	 * 
	 * @param status The return {@link BTStatus} of the node. 
	 */
	protected void onTerminate(BTStatus status) {}
	
	/**
	 * Sets the value of the node's {@link BTNodeType} variable. 
	 * 
	 * @param newType The nodes new {@link BTNodeType}
	 */
	protected void setNodeType(BTNodeType newType) { type = newType; }
	
	/**
	 * Constructs a <code>BTBehavior</code>. By default the return status is set to 
	 * BH_INVALID and node type is set to GENERIC. 
	 */
	public BTBehavior()
	{
		status = BTStatus.BH_INVALID;
		type = BTNodeType.GENERIC;
	}
	
	/**
	 * A helper/wrapper method. Tick makes sure that the sequence of BTBehavior
	 * methods are called correctly. The proper sequence is <code>onInitialize()</code> 
	 * -- <code>update()</code> -- <code>onTerminate()</code>
	 * -- <code>return status</code>. Note that before <code>onInitialize()</code> and
	 * <code>onTerminate()</code> are called the return status is check to make sure that it is NOT
	 * currently equal to <code>BH_RUNNING</code>.
	 * 
	 * @return The return {@link BTStatus} of the node. 
	 */
	public BTStatus tick(float elapsedTime)
	{
		if(status != BTStatus.BH_RUNNING)
		{
			onInitialize();
		}
		
		status = update(elapsedTime);
		
		if(status != BTStatus.BH_RUNNING)
		{
			onTerminate(status);
		}
		
		return status;
	}
	
	/**
	 * Resets the return status of the node to BH_INVALID.
	 */
	public void reset()
	{
		status = BTStatus.BH_INVALID;
	}
	
	/**
	 * Aborts the nodes. Calls the <code>onTerminate()</code> method and sets the return {@link BTStatus}
	 * to BH_ABORTED. 
	 */
	public void abort()
	{
		onTerminate(BTStatus.BH_ABORTED);
		status = BTStatus.BH_ABORTED;
	}
	
	/**
	 * @return TRUE if the {@link BTStatus} of the Behavior is BH_SUCCESS or BH_FAILURE; FALSE otherwise.
	 */
	public boolean isTerminated()
	{
		return (status == BTStatus.BH_SUCCESS || status == BTStatus.BH_FAILURE);
	}
	
	/**
	 * @return TRUE if the {@link BTStatus} of the Behavior is BH_RUNNING; FALSE otherwise. 
	 */
	public boolean isRunning()
	{
		return (status == BTStatus.BH_RUNNING);
	}
	
	/**
	 * @return {@link BTStatus} of the Behavior node.
	 */
	public BTStatus getStatus() { return status; }
	
	/**
	 * @return {@link BTNodeType} of the Behavior node. 
	 */
	public BTNodeType getType() { return type; }
}
