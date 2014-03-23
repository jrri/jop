package javax.safetycritical;

import static javax.safetycritical.annotate.Level.LEVEL_0;
import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.LEVEL_2;

import javax.realtime.AffinitySet;
import javax.realtime.HighResolutionTime;
import javax.realtime.ProcessorAffinityException;
import javax.realtime.Scheduler;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.io.IOFactory;
import com.jopdesign.io.SysDevice;

import static javax.safetycritical.annotate.Phase.INITIALIZATION;

/**
 * This class provides a collection of static helper methods.
 */
@SCJAllowed
public class Services {

	static AffinitySet[] schedullingDomains;

	static SysDevice sys = IOFactory.getFactory().getSysDevice();
	
	static {
		/* Generate an affinity set for each available cpu.
		 * 
		 * schedullingDomains[0] => processor 0,
		 * schedullingDomains[1] => processor 1,
		 * ...
		 *  */
		schedullingDomains = new AffinitySet[sys.nrCpu];
		for (int i = 0; i < schedullingDomains.length; i++) {
			schedullingDomains[i] = AffinitySet.generate(i);
		}
		
	}

	/**
	 * Captures the stack back trace for the current thread into its
	 * thread-local stack back trace buffer and remembers that the current
	 * contents of the stack back trace buffer is associated with the object
	 * represented by the association argument. The size of the stack back trace
	 * buffer is determined by the StorageParameters object that is passed as an
	 * argument to the constructor of the corresponding Schedulable. If the
	 * stack back trace buffer is not large enough to capture all of the stack
	 * back trace information, the information is truncated in an implementation
	 * dependent manner.
	 * 
	 * @todo: Not implemented
	 */
	@SCJAllowed
	public static void captureBackTrace(Throwable association) {
	}

	/**
	 * 
	 * @return a reference to the currently executed ManagedEventHandler or
	 *         ManagedThread.
	 */
	@SCJAllowed
	public static javax.safetycritical.ManagedSchedulable currentManagedSchedulable() {
		return (ManagedSchedulable) Scheduler.getCurrentSO();
	}
	
	/**
	 * This is like sleep except that it is not interruptible and it uses
	 * nanoseconds instead of milliseconds.
	 * 
	 * @param delay
	 *            is the number of nanoseconds to suspend
	 * 
	 *            TBD: should this be called suspend or deepSleep to no have a
	 *            ridiculously long name?
	 * 
	 *            TBD: should not be a long nanoseconds?
	 */
	@SCJAllowed(LEVEL_2)
	@SCJRestricted(maySelfSuspend = true)
	public static void delay(int ns_delay) {
	}
	
	/**
	 * This is like sleep except that it is not interruptible and it uses
	 * nanoseconds instead of milliseconds.
	 * 
	 * @param delay
	 *            is the number of nanoseconds to suspend
	 * 
	 *            TBD: should this be called suspend or deepSleep to no have a
	 *            ridiculously long name?
	 * 
	 *            TBD: should not be a long nanoseconds?
	 */
	@SCJAllowed(LEVEL_2)
	@SCJRestricted(maySelfSuspend = true)
	public static void delay(HighResolutionTime delay) {
	}

	/**
	 * @return the default ceiling priority The value is the highest software
	 *         priority.
	 */
	@SCJAllowed(LEVEL_1)
	public static int getDefaultCeiling() {
		return 39;
	}

	/**
	 * sets the ceiling priority of object O The priority can be in the software
	 * or hardware priority range.
	 * 
	 * @throws IllegalThreadState
	 *             if called outside the mission phase
	 */
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(phase = INITIALIZATION)
	public static void setCeiling(Object O, int pri) {
	}

//	/**
//	 * This method is invoked by infrastructure to change the association for
//	 * the thread-local stack back trace buffer to the Class that represents a
//	 * Throwable that has crossed its scope boundary, at the time that Throwable
//	 * is replaced with a ThrowBoundaryError.
//	 */
//	static void overwriteBackTraceAssociation(Class _class) {
//	}
//
//	/**
//	 * Every interrupt has an implementation-defined integer id.
//	 * 
//	 * @return The priority of the code that the first-level interrupts code
//	 *         executes. The returned value is always greater than
//	 *         PriorityScheduler.getMaxPriority().
//	 * @throws IllegalArgument
//	 *             if unsupported InterruptId
//	 */
//	@SCJAllowed(LEVEL_1)
//	public static int getInterruptPriority(int InterruptId) {
//		return 33;
//	}
//
//	/**
//	 * Registers an interrupt handler.
//	 * 
//	 * @throws IllegalArgument
//	 *             if unsupported InterruptId IllegalStateException if handler
//	 *             already registered
//	 */
//	@SCJAllowed(LEVEL_1)
//	public static void registerInterruptHandler(int InterruptId,
//			InterruptHandler IH) {
//	}

	/*
	 * The deployment level
	 */
	// @SCJAllowed
	// public static Level getDeploymentLevel() { return LEVEL_0; }

	/**
	 * Busy wait spinning loop (now plus delay).
	 * 
	 * @param delay
	 */
	// @ICS
	public static void spin(HighResolutionTime delay) {
	}

	/**
	 * Busy wait in nano seconds.
	 * 
	 * @param nanos
	 */
	@SCJAllowed(javax.safetycritical.annotate.Level.LEVEL_0)
	@SCJRestricted(maySelfSuspend = false)
	public static void nanoSpin(int nanos) {
	}

	@SCJAllowed(LEVEL_0)
	@SCJRestricted(maySelfSuspend = false)
	public static javax.realtime.AffinitySet[] getSchedulingAllocationDoamins() {
		return schedullingDomains;
	}
}
