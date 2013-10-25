package javax.safetycritical;

import javax.realtime.AffinitySet;
import javax.realtime.BoundAsyncLongEventHandler;
import javax.realtime.PriorityParameters;
import javax.realtime.ReleaseParameters;
import javax.realtime.RtsjHelper;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Level.SUPPORT;

/**
 * In SCJ, all handlers must be registered with the enclosing mission, so
 * applications use classes that are based on the ManagedEventHandler and the
 * ManagedLongEventHandler class hierarchies. These class hierarchies allow a
 * mission to manage all the handlers that are created during its initialization
 * phase. They set up the initial memory area of each managed handler to be a
 * private memory that is entered before a call to handleAsyncEvent and that is
 * left on return. The size of the private memory area allocated is the maximum
 * available to the infrastructure for this handler.
 * 
 * Note that the values in parameters classes passed to the constructors are
 * those that will be used by the infrastructure. Changing these values after
 * construction will have no impact on the created event handler.
 * 
 * This class differs from ManagedEventHandler in that when it is fired, a long
 * integer is provided for use by the released event handler(s).
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 * 
 */
@SCJAllowed
public abstract class ManagedLongEventHandler extends
		BoundAsyncLongEventHandler implements ManagedSchedulable {

	/*
	 * Workaround to avoid illegal assignments when referring to constant
	 * strings. Constant strings in JOP have no associated memory area
	 */
	private StringBuffer name;
	
	static RtsjHelper _rtsjHelper;

	public static void setRtsjHelper(RtsjHelper rtsjHelper) {
		_rtsjHelper = rtsjHelper;
	}

	/**
	 * Constructor to create an event handler.
	 * <p>
	 * Does not perform memory allocation. Does not allow this to escape local
	 * scope. Builds links from this to priority, parameters, and name so those
	 * three arguments must reside in scopes that enclose this.
	 * 
	 * @param priority
	 *            specifies the priority parameters for this periodic event
	 *            handler. Must not be null.
	 * 
	 * @param release
	 *            specifies the periodic release parameters, in particular the
	 *            start time and period. Note that a relative start time is not
	 *            relative to NOW but relative to the point in time when
	 *            initialization is finished and the timers are started. This
	 *            argument must not be null.
	 * 
	 * @param storage
	 *            specifies the non-null maximum memory demands for this event
	 *            handler.
	 * 
	 * @throws IllegalArgumentException
	 *             if priority, release or memory parameters are null.
	 */
	@SCJAllowed
	@SCJRestricted(phase = INITIALIZATION)
	ManagedLongEventHandler(PriorityParameters priority,
			ReleaseParameters release, StorageParameters storage, String name) {

		if (priority == null | release == null | storage == null) {
			throw new IllegalArgumentException();
		}

		this.name = new StringBuffer(name);
		
		/* Default affinity set, can be overridden only at initialization phase */
		AffinitySet defaultAffinity = Services.getSchedulingAllocationDoamins()[0];
		AffinitySet.setProcessorAffinity(defaultAffinity, this);
	}

	/**
	 * Application developers override this method with code to be executed when
	 * this event handler's execution is disabled (upon termination of the
	 * enclosing mission).
	 * 
	 */
	@Override
	@SCJAllowed(SUPPORT)
	public void cleanUp() {
		Terminal.getTerminal().writeln("[SYSTEM]: Default MLEH cleanup");
	}

	/**
	 * Application developers override this method with code to be executed
	 * whenever the event(s) to which this event handler is bound is fired.
	 */
	@SCJAllowed
	public abstract void handleAsyncEvent(long data);

	/**
	 * @return the name of this event handler.
	 */
	@SCJAllowed
	public String getName() {
		return name.toString();
	}

}
