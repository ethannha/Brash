package tage.ai.behaviortrees;

/**
 * <code>BTRepeat</code> is a <code>BTDecorator</code> node that processes its child nodes a specific
 * number of times before returning a return status. If during one of these repeat the child node 
 * returns BH_FAILURE then the repeat node returns BH_FAILURE. <code>BTRepeat</code> is analogous to
 * adding a loop to a behavior tree. 
 * 
 * @author Kenneth Barnett
 * @author Alex J. Champandard
 * 
 * @see <a href="http://aigamedev.com/insider/tutorial/second-generation-bt/">Behavior Trees Tutorial Video</a><p>
 * 		<a href="https://github.com/aigamedev/btsk">Behavior Trees Starter Kit</a>
 *
 */
public class BTRepeat extends BTDecorator
{
	protected int limit;
	protected int counter;
	
	/**
	 * Resets the nodes repeat counter to zero. 
	 */
	protected void onInitialize()
	{
		counter = 0;
	}
	
	/**
	 * Updates the <code>BTRepeat</code> node. Continually executes its child node
	 * until either 1) the child returns BH_FAILURE in which case the repeat node
	 * returns BH_FAILURE, 2) the child returns BH_RUNNING in which case the repeat
	 * node returns BH_INVALID, or 3) the child has executed the specified number 
	 * times in which case BH_SUCCESS is returned. 
	 */
	protected BTStatus update(float elapsedTime)
	{
		while(true)
		{
			child.tick(elapsedTime);
			
			if(child.getStatus() == BTStatus.BH_RUNNING)
			{
				break;
			}
			
			if(child.getStatus() == BTStatus.BH_FAILURE)
			{
				return BTStatus.BH_FAILURE;
			}
			
			if(++counter == limit)
			{
				return BTStatus.BH_SUCCESS;
			}
			
			child.reset();
		}
		
		return BTStatus.BH_INVALID;
	}
	
	/**
	 * Constructs a <code>BTRepeat</code> node encapsulating a child node and setting the 
	 * repeat count. 
	 * @param child The child node. 
	 * @param repeatCount The number of times the child node should be processed before the repeat node returns. 
	 */
	public BTRepeat(BTBehavior child, int repeatCount)
	{
		super(child);
		limit = repeatCount;
	}
}