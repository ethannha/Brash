package tage.audio;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * This class is a Factory for creating <I>audio manager</i> objects -- that is, objects which
 * implement the {@link IAudioManager} interface.  
 * Audio Manager implementation classes can register themselves as being available by invoking
 * {@link #registerAudioManager(String, Class)}.  Client classes can obtain a AudioManager implementation
 * instance by invoking {@link #createAudioManager(String)}, providing a {@link String} giving the
 * particular AudioManager's class name. 
 * 
 * The code for this class comes from the RendererFactory class in sage.renderer created by John Clevenger, and
 * it has been reworked by Kenneth Barnett to create audio managers.  
 *
 * @author John Clevenger
 * @author Kenneth Barnett
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 *
 */
@SuppressWarnings("unchecked")
public class AudioManagerFactory 
{
	//a table mapping audio manager name strings to audio manager class objects
	static private HashMap<String,Class<?>> registeredAudioManagers = new HashMap<String,Class<?>>();

	/**
	 * Registers a audio manager class (that is, a class implementing the {@link IAudioManager} interface)
	 * and making the audio manager available to clients.
	 * 
	 * @param audioMgrName - a String giving the AudioManager class name
	 * @param audioMgrClassObject - a Class object defining the AudioManager implementation
	 */
	static public void registerAudioManager(String audioMgrName, Class<?> audioMgrClassObject) 
	{
		//System.out.println("RendererFactory.registerRenderer(): registering '" + rendererName + "' (" + rendererClassObject + ")");
		registeredAudioManagers.put(audioMgrName, audioMgrClassObject);
	}

	/**
	 * A factory method (see the <I>Factory</i> Design Pattern) for creating instances of a 
	 * particular AudioManager (that is, an object which implements the {@link IAudioManager} interface.
	 * 
	 * @param audioMgrClassName - a String giving the desired AudioManagers's class name (including
	 * 					any prefixes defining the AudioManagers's subpackage(s)
	 * 
	 * @return - an instance of the specified AudioManager class
	 */
	static public IAudioManager createAudioManager(String audioMgrClassName) 
	{
		//see if the requested audio manager class has been registered
		Class<IAudioManager> audioMgrClass = (Class<IAudioManager>) registeredAudioManagers.get(audioMgrClassName);
		
		//check if the above found the requested audio manager already registered
		if (audioMgrClass == null) 
		{
			//System.out.println("RendererFactory.createRenderer(): didn't find '" + rendererClassName + "' in registered renderers");
			
			//audio manager class hasn't been registered; see if we can find the class
			try 
			{	
				//Class.forName() attempts to load (and initialize) the specified class; try that
				audioMgrClass = (Class<IAudioManager>) Class.forName(audioMgrClassName);
				
				//check to see if we were able to find the class
				if (audioMgrClass != null) 
				{
					//we found and loaded the requested class
					//check to see if the class is now registered (the class may have registered 
					// itself during loading/initialization); if not, register it now
					Class<IAudioManager> checkClass = (Class<IAudioManager>) registeredAudioManagers.get(audioMgrClassName);
					
					if (checkClass == null) 
					{
						//class loaded but didn't register itself; register it manually
						registerAudioManager(audioMgrClassName, audioMgrClass);
					}
				}
			} 
			catch (ClassNotFoundException e) 
			{
				//System.out.println("RendererFactory.createRenderer(): ClassNotFoundException " + "returned from 'Class.forName(" + rendererClassName + ")'");
				
				//not found by the specified name; try adding subpackage prefix
				try 
				{	
					audioMgrClass = (Class<IAudioManager>) Class.forName("tage.audio." + audioMgrClassName);
					
					if (audioMgrClass != null) 
					{
						System.out.println("WARNING: unable to find requested audio manager '" + audioMgrClassName
								+ "'; attempting to use audio manager '" + audioMgrClass.getName() + "' instead.");
					}
					
				} 
				catch (ClassNotFoundException e1) 
				{	
					throw new RuntimeException("AudioManagerFactory.createAudioManager(): cannot find class '"
											+ audioMgrClassName 
											+ "' (missing/incorrect subpackage specification?) \n" + e1);
				}
			}
		}
		
		//if we get here we know the requested audio manager class (or a substitute) has been found; 
		// see if we can get a no-argument constructor for the class
		Constructor<IAudioManager> audioMgrConstructor = null;
		
		try 
		{
			//get a constructor which expects the specified number of arguments (here, that number is zero
			// as indicated by the empty array passed as a parameter
			//If instead we wanted to get a constructor expecting one argument of type String we could
			// do that as follows:   getDeclaredConstructor(new Class[] {String.class});
			audioMgrConstructor = audioMgrClass.getDeclaredConstructor(new Class[] {});
			
		} 
		catch (SecurityException e) 
		{
			throw new RuntimeException("AudioManagerFactory.createAudioManager(): " 
									+ "SecurityException getting audio manager constructor:  \n" + e);
		} 
		catch (NoSuchMethodException e) 
		{
			throw new RuntimeException("AudioManagerFactory.createAudioManager(): " 
									+ "NoSuchMethodException getting audio manager constructor: \n" + e);
		}
		
		//if we get here we know we have a constructor for the requested renderer; try constructing
		// an instance of the audio manager
		IAudioManager audioMgr = null ;
		
		try 
		{
			//get an instance of the requested audio manager by invoking the no-arg constructor of the class
			// (here, the no-arg constructor is indicated by passing a empty array of arguments)
			audioMgr = audioMgrConstructor.newInstance(new Object[] {});
			
		} 
		catch (IllegalArgumentException e) 
		{
			throw new RuntimeException("AudioManagerFactory.createAudioManager: "
									+ "IllegalArgumentException instantiating audio manager: \n" + e );
		} 
		catch (InstantiationException e) 
		{
			throw new RuntimeException("AudioManagerFactory.createAudioManager: "
									+ "InstantiationException instantiating audio manager: \n" + e );
		} 
		catch (IllegalAccessException e) 
		{
			throw new RuntimeException("AudioManagerFactory.createAudioManager: "
									+ "IllegalAccessException instantiating audio manager: \n" + e );
		} 
		catch (InvocationTargetException e) 
		{
			System.out.println("AudioManagerFactory: Exception createAudioManager audio manager '" 
							+ audioMgrClassName + "': " + e.getCause());
			
			throw new RuntimeException("AudioManagerFactory.createAudioManager: "
									+ "InvocationTargetException instantiating audio manager: \n" + e );
		}
		
		//if we get here we have successfully constructed the requested audio manager -- return it
		return audioMgr ;
	}
}
