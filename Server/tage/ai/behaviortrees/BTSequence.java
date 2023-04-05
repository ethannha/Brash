package tage.ai.behaviortrees;

import java.util.Iterator;

/**
 * A {@code BTSequence} node is a {@link BTComposite} that executes its child nodes from left to right. 
 * If a failure happens at any of the child nodes then the {@code BTSequence} fails. 
 * If all child nodes succeed then the {@code BTSequence} succeeds.
 * You are basically ANDing all of the return Status's of Behaviors together.
 * 
 * @author Kenneth Barnett
 * @author Alex J. Champandard
 * 
 * @see <a href="http://aigamedev.com/insider/tutorial/second-generation-bt/">Behavior Trees Tutorial Video</a><p>
 * 		<a href="https://github.com/aigamedev/btsk">Behavior Trees Starter Kit</a>
 *
 */
public class BTSequence extends BTComposite 
{
	protected Iterator<BTBehavior> currentChild;	// An iterator representing current child being processed. 
	
	/**
	 * Constructs a <code>BTSequence</code> with an associated ID.
	 * @param composteID The ID of the selector. 
	 */
	public BTSequence(int composteID) 
	{
		super(composteID);
	}

	/**
	 * Resets the current child iterator back to the beginning of the collection.
	 */
	protected void onInitialize()
	{
		currentChild = children.iterator();
	}
	
	/**
	 * Ticks each child and "ANDs" the return status's of the children
	 */
	protected BTStatus update(float elapsedTime) 
	{
		// Keep going until a child behavior says its running.
		while(true)
		{
			BTStatus s; 
			
			if(currentChild.hasNext())
				s = currentChild.next().tick(elapsedTime);
			else
				return BTStatus.BH_SUCCESS;		// This is for if the sequence has no children.
			
			// If the child fails, or keeps running, do the same. 
			if(s != BTStatus.BH_SUCCESS)
			{
				return s;
			}
			
			// Hit the end of the collection, job done!
			if(currentChild.hasNext() == false)
			{
				return BTStatus.BH_SUCCESS;
			}
		}
	}
}
