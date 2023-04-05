package tage.ai.behaviortrees;

/**
 * A <code>BTCondition</code> node is a leaf node that performs a boolean check on read-only
 * data gathered from the game world. If the check returns TRUE then the node
 * returns SUCCESS. If the check returns FALSE then the node returns FAILURE.
 * 
 * Users wanting to create condition nodes do so by extending this class
 * and implementing the <code>check()</code> method. 
 * 
 * @author Kenneth Barnett
 * @author Alex J. Champandard
 * 
 * @see <a href="http://aigamedev.com/insider/tutorial/second-generation-bt/">Behavior Trees Tutorial Video</a><p>
 * 		<a href="https://github.com/aigamedev/btsk">Behavior Trees Starter Kit</a>
 *
 */
public abstract class BTCondition extends BTBehavior
{
	protected boolean negate;		// A flag specifying if the check() should NOT its return value.
	
	/**
	 * Conducts the condition check. This method is meant to be overridden by a class that extends
	 * <code>BTCondition</code>. 
	 * 
	 * @return TRUE if the condition passes; FALSE otherwise. 
	 */
	protected abstract boolean check();
	
	/**
	 * Updates the <code>BTCondition</code> node. Calls the protected <code>check()</code> method.
	 * If the node is set to negation then the return value from <code>check()</code> will be NOTed.
	 * If the value from <code>check()</code> is TRUE then the node returns BH_SUCCESS. 
	 * If the value from <code>check()</code> is FALSE then the node returns BH_FAILURE. 
	 */
	protected BTStatus update(float elapsedTime)
	{
		boolean result = check();
		
		if(negate == true)
			result = !result;
			
		if(result == true)
		{
			return BTStatus.BH_SUCCESS;
		}
		else
		{
			return BTStatus.BH_FAILURE;
		}
	}
	
	/**
	 * Constructs a <code>BTCondition</code>. 
	 * @param toNegate TRUE if the value returned by <code>check()</code> should be NOTed; otherwise FALSE.
	 */
	public BTCondition(boolean toNegate)
	{
		negate = toNegate;
		
		this.setNodeType(BTNodeType.CONDITION);
	}
}