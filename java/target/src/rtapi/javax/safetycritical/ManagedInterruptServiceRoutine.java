package javax.safetycritical;

import java.util.Vector;

import javax.realtime.InterruptServiceRoutine;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.io.IOFactory;
import com.jopdesign.io.SysDevice;
import com.jopdesign.sys.Native;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;

/**
 * This class integrates the RTSJ interrupt handling mechanisms with the SCJ
 * mission structure
 * 
 */
public abstract class ManagedInterruptServiceRoutine extends
		InterruptServiceRoutine {

	static IOFactory factory;
	static SysDevice system;

	static {
		/* Make sure the sys device is created in ImmortalMemory */
		factory = IOFactory.getFactory();
		system = factory.getSysDevice();
	}

	/**
	 * Runnable to be registered as first level interrupt handler
	 */
	final Runnable firstLevelHandler;

	/**
	 * Implementation dependent interrupt ID
	 */
	int interrupt;

	StorageParameters _storage;
	private PrivateMemory privMem;

	/**
	 * Creates an interrupt service routine with the given name and associated
	 * with a given interrupt.
	 * 
	 * TODO: What is this? initialMemoryAreaSize — is the size of a private
	 * memory area which acts as the initial allocation context for the handle
	 * method. A size of 0 indicates that any use of the new operator within the
	 * initial allocation context will result in an OutOfMemoryException being
	 * thrown.
	 * 
	 * @param sizes
	 *            Defines the memory space required by the handle method.
	 */
	@SCJAllowed(LEVEL_1)
	public ManagedInterruptServiceRoutine(StorageParameters storage, String name) {

		_storage = storage;
		_name = new StringBuffer(name);

		privMem = new PrivateMemory((int) _storage.getMaxMemoryArea(),
				(int) _storage.getTotalBackingStoreSize());

		final Runnable isr = new Runnable() {
			@Override
			public void run() {
				try {
					handle();
				} catch (Exception e) {
					unhandledException(e);
				}
			}
		};

		firstLevelHandler = new Runnable() {
			@Override
			public void run() {
				privMem.enter(isr);
			}
		};

	}

	@SCJAllowed(LEVEL_1)
	public ManagedInterruptServiceRoutine(StorageParameters storage) {
		this(storage, "");
	}

	/**
	 * Equivalent to register(interrupt, highestInterruptCeilingPriority).
	 * 
	 * @param interrupt
	 *            Is the implementation-dependent id for the interrupt.
	 * @throws RegistrationException
	 */
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(phase = INITIALIZATION)
	public final void register(int interrupt) {// throws RegistrationException {
		this.register(interrupt, 33);
	}

	/**
	 * Registers the ISR for the given interrupt with the current mission, sets
	 * the ceiling priority of this. The filling of the associated interrupt
	 * vector is deferred until the end of the initialization phase.
	 * 
	 * @param interrupt
	 *            Is the implementation-dependent id for the interrupt.
	 * @param ceiling
	 *            Is the required ceiling priority.
	 * @throws RegistrationException
	 *             If the required ceiling is not as high or higher than this
	 *             interrupt priority.
	 */
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(phase = INITIALIZATION)
	public final void register(int interrupt, int ceiling) { // throws
																// RegistrationException
																// {
		this.interrupt = interrupt;

		// TODO Illegal array reference.
		// The array that holds the references to the interrupt handlers is a
		// static field in JVMHelp.java
		factory.registerInterruptHandler(interrupt, firstLevelHandler);

		// factory.registerInterruptHandler(interrupt, sw);

		final Mission m = Mission.getCurrentMission();

		if (!m.hasManagedInterrupt) {
			m.managedInterruptRef = Native.toInt(new Vector());
			m.hasManagedInterrupt = true;
		}

		((Vector) Native.toObject(m.managedInterruptRef)).addElement(this);

	}

	/**
	 * Called by the infrastructure if an exception propagates outside of the
	 * handle method.
	 * 
	 * @param except
	 *            Is the uncaught exception.
	 */
	@SCJAllowed(LEVEL_1)
	// @SCJRestricted(phase = INTERRUPT_SERVICE_ROUTINE)
	public void unhandledException(Exception except) {

	}

	/**
	 * Unregisters the ISR with the current mission.
	 */
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(phase = CLEANUP)
	public final void unregister() {
		factory.deregisterInterruptHandler(interrupt);
	}

	// ========== Implementation specific ============= //

	public int getInterrupt() {
		return interrupt;
	}

	public static InterruptServiceRoutine getInterruptServiceRoutine(
			int interrupt) {
		Mission m = Mission.getCurrentMission();
		Vector managedInterrupt = m.getInterrupts();
		ManagedInterruptServiceRoutine misr;
		InterruptServiceRoutine isr = null;

		if (managedInterrupt != null) {
			for (int i = 0; i < managedInterrupt.size(); i++) {
				misr = ((ManagedInterruptServiceRoutine) managedInterrupt
						.elementAt(i));
				if (interrupt == misr.getInterrupt())
					isr = misr;
			}
		}
		return isr;
	}
}
