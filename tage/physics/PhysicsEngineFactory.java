package tage.physics;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("unchecked")
public class PhysicsEngineFactory {
	private static HashMap<String, Class<?>> engines = new HashMap<String, Class<?>>();

	/**
	 * Static method to register a physics engine with this factory.
	 * 
	 * @param engineName
	 *            the full class name of the engine
	 * @param engineClass
	 *            the Class object of the engine
	 */
	public static void registerPhysicsEngine(String engineName,
			Class<?> engineClass) {
		engines.put(engineName, engineClass);
	}

	/**
	 * Creates a PhysicsEngine based on the param passed in.
	 * 
	 * The param expected is the full class name of the physics engine (i.e.
	 * sage.physics.JBullet.JBulletPhysicsEngine).
	 * 
	 * Other params that this method tries to account for: -the physics engine
	 * class name (i.e. JBulletPhysicsEngine, ODE4JPhysicsEngine...) -the
	 * physics engine name (i.e. JBullet, ODE4J...). this assumes that all
	 * engines follow the already established naming convention of post-fixing
	 * PhysicsEngine to the physics engine name as the class name.
	 * 
	 * The case of letters should not matter, however if the engine fails to
	 * load try camel casing the param string with acronyms in uppercase. There
	 * seems to be some information indicating that the solution used for case
	 * insensitivity is operating system based, meaning that it may fail
	 * depending on the operating system being used.
	 * 
	 * If anything other than the full class name is passed in, and an engine is
	 * found, there will be a "NOTE:" message to System.err stating what engine
	 * is being used.
	 * 
	 * @param engineClassName
	 * @return a physics engine
	 */
	public static PhysicsEngine createPhysicsEngine(String engineClassName) {
		/*
		 * ATTEMPTS to find the class by name passed in.
		 * 
		 * causes the static block that registers the class to get called if it
		 * exists
		 */
		try {
			Class.forName(engineClassName);
		} catch (ClassNotFoundException e1) {
			String postFix = "PhysicsEngine";
			String append = "";
			if (engineClassName.contains(postFix)) {
				append += engineClassName.substring(0, engineClassName.length()
						- postFix.length())
						+ "." + engineClassName;
			} else {
				append += engineClassName + "." + engineClassName
						+ "PhysicsEngine";
			}

			try {
				Class.forName("tage.physics." + append);
			} catch (ClassNotFoundException e2) {
			} catch (NoClassDefFoundError e3) {
				findClassByErrorMessage(e3.getMessage());
			}

		} catch (NoClassDefFoundError e3) {
			findClassByErrorMessage(e3.getMessage());
		}

		Class<PhysicsEngine> engineClass = (Class<PhysicsEngine>) engines
				.get(engineClassName);
		// engineClassName is not registered with this factory by that name
		if (engineClass == null) {
			// check if name is a substring of any key
			// ignores case when checking
			Set<String> keys = engines.keySet();
			String key = null;
			String temp = null;
			for (String s : keys) {
				temp = s.toLowerCase();

				if (temp.contains(engineClassName.toLowerCase())) {
					key = s;
					break;
				}
			}

			if (key == null) {
				throw new IllegalArgumentException(
						"Could not find Physics Engine given name: "
								+ engineClassName
								+ ". This engine may exist but is not properly"
								+ " registered with this factory, likely indicating it should not be used.");
			} else {
				engineClass = (Class<PhysicsEngine>) engines.get(key);

				System.err
						.println("NOTE: "
								+ engineClassName
								+ " is not recognized as"
								+ " a full class name. "
								+ "Attempted to find it which resulted in using engine: "
								+ key + ".");
			}
		}

		Constructor<PhysicsEngine> engineConstructor = null;
		try {
			engineConstructor = engineClass
					.getDeclaredConstructor(new Class[] {});

		} catch (SecurityException e) {
			throw new RuntimeException(
					"PhysicsEngineFactory.createPhysicsEngine(): "
							+ "SecurityException getting engine constructor:  \n"
							+ e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(
					"PhysicsEngineFactory.createPhysicsEngine(): "
							+ "NoSuchMethodException getting engine constructor: \n"
							+ e);
		}

		PhysicsEngine engine = null;
		try {
			engine = engineConstructor.newInstance(new Object[] {});

		} catch (IllegalArgumentException e) {
			throw new RuntimeException(
					"PhysicsEngineFactory.createPhysicsEngine(): "
							+ "IllegalArgumentException instantiating engine: \n"
							+ e);
		} catch (InstantiationException e) {
			throw new RuntimeException(
					"PhysicsEngineFactory.createPhysicsEngine(): "
							+ "InstantiationException instantiating engine: \n"
							+ e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(
					"PhysicsEngineFactory.createPhysicsEngine(): "
							+ "IllegalAccessException instantiating engine: \n"
							+ e);
		} catch (InvocationTargetException e) {
			System.out
					.println("PhysicsEngineFactory: Exception constructing engine '"
							+ engineClassName + "': " + e.getCause());
			throw new RuntimeException(
					"PhysicsEngineFactory.createPhysicsEngine(): "
							+ "InvocationTargetException instantiating engine: \n"
							+ e);
		}

		return engine;
	}

	/*
	 * found the class, but jvm could not load it?
	 * 
	 * happens when there is a case sensitivity issue. one last try to load the
	 * class based on the error message handed back.
	 * 
	 * the message handed back states "(wrong name: {className})".
	 */
	private static void findClassByErrorMessage(String message) {
		// parse out the name
		int i0 = message.indexOf("wrong name:");

		int i1 = -1, i2 = -1;
		if (i0 != -1) {
			i1 = message.indexOf("sage", i0);
			i2 = message.indexOf(")", i1);
		}

		if (i1 != -1 && i2 != -1) {
			message = message.substring(i1, i2);
			message = message.replace('/', '.');

			try {
				Class.forName(message);
			} catch (ClassNotFoundException e) {
			}
		}
	}
	
}
