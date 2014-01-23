package javax.safetycritical;

import javax.realtime.AbsoluteTime;
import javax.realtime.AperiodicParameters;
import javax.realtime.HighResolutionTime;
import javax.realtime.PriorityParameters;

import javax.safetycritical.annotate.SCJAllowed;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import javax.safetycritical.annotate.SCJRestricted;
import static javax.safetycritical.annotate.Level.LEVEL_1;

/**
 * This class permits the automatic execution of time-triggered code. The
 * handleAsyncEvent method behaves as if the handler were attached to a one-shot
 * timer asynchronous event.
 * 
 * This class is abstract, non-abstract sub-classes must implement the method
 * handleAsyncEvent and may override the default cleanup method.
 * 
 * Note that the values in parameters passed to the constructors are those that
 * will be used by the infrastructure. Changing these values after construction
 * will have no impact on the created event handler.
 * 
 * Note: all time-triggered events are subject to release jitter.
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 * @todo Not yet implemented
 * 
 */
@SCJAllowed(LEVEL_1)
public abstract class OneShotEventHandler extends ManagedEventHandler {

	/**
	 * Constructs a one-shot event handler.
	 * 
	 * Memory behavior: Does not perform memory allocation. Does not allow this
	 * to escape local scope. Builds links from this to priority and parameters,
	 * so those two arguments must reside in scopes that enclose this.
	 * 
	 * 
	 * @param priority
	 *            specifies the priority parameters for this event handler. Must
	 *            not be null.
	 * 
	 * @param time
	 *            specifies the time at which the handler should be release. A
	 *            relative time is relative to the start of the associated
	 *            mission. An absolute time that is before the mission is
	 *            started is equivalent to a relative time of 0. A null
	 *            parameter is equivalent to a relative time of 0.
	 * 
	 * @param release
	 *            specifies the aperiodic release parameters, in particular the
	 *            deadline miss handler. A null parameters indicates that there
	 *            is no deadline associated with this handler.
	 * 
	 * @param storage
	 *            specifies the storage parameters; it must not be null
	 * @throws IllegalArgumentException
	 *             if priority, release or memory is null.
	 */
	@SCJAllowed
	@SCJRestricted(phase = INITIALIZATION)
	public OneShotEventHandler(PriorityParameters priority,
			HighResolutionTime time, AperiodicParameters release,
			StorageParameters storage) {
		this(priority, time, release, storage,"");

	}

	/**
	 * Constructs a one-shot event handler.
	 * 
	 * Memory behavior: Does not perform memory allocation. Does not allow this
	 * to escape local scope. Builds links from this to priority and parameters,
	 * so those two arguments must reside in scopes that enclose this.
	 * 
	 * 
	 * @param priority
	 *            specifies the priority parameters for this event handler. Must
	 *            not be null.
	 * 
	 * @param time
	 *            specifies the time at which the handler should be release. A
	 *            relative time is relative to the start of the associated
	 *            mission. An absolute time that is before the mission is
	 *            started is equivalent to a relative time of 0. A null
	 *            parameter is equivalent to a relative time of 0.
	 * 
	 * @param release
	 *            specifies the aperiodic release parameters, in particular the
	 *            deadline miss handler. A null parameters indicates that there
	 *            is no deadline associated with this handler.
	 * 
	 * @param storage
	 *            specifies the storage parameters; it must not be null.
	 * 
	 * @param name
	 *            a name provided by the application to be attached to this
	 *            event handler
	 * @throws IllegalArgumentException
	 *             if priority, release, or scp is null.
	 */
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(phase = INITIALIZATION)
	public OneShotEventHandler(PriorityParameters priority,
			HighResolutionTime time, AperiodicParameters release,
			StorageParameters storage, String name) {
		super(priority, release, storage, name);
	}

	/**
	 * Deschedules the next release of the handler.
	 * 
	 * @return true if the handler was scheduled to be released false otherwise.
	 */
	@SCJAllowed(LEVEL_1)
	public boolean deschedule() {
		// dummy return
		return false;
	}

	/**
	 * Get the time at which this handler is next expected to be released.
	 * 
	 * @param dest
	 *            The instance of javax.safetycritical.AbsoluteTime which will
	 *            be updated in place and returned. The clock association of the
	 *            dest parameter is ignored. When dest is null a new object is
	 *            allocated for the result
	 * 
	 * @return An instance of an javax.safetycritical.AbsoluteTime representing
	 *         the absolute time at which this handler is expected to be
	 *         released. If the dest parameter is null the result is returned in
	 *         a newly allocated object. The clock association of the returned
	 *         time is the clock on which the interval parameter (passed at
	 *         construction time) is based.
	 * 
	 * @throws IllegalStateException
	 *             if this handler has not been started.
	 */
	@SCJAllowed(LEVEL_1)
	public AbsoluteTime getNextReleaseTime(AbsoluteTime dest) {
		// dummy return
		return null;
	}

	@SCJAllowed
	@SCJRestricted(phase = INITIALIZATION)
	public final void register() {

	}

	/**
	 * Change the next scheduled release time for this handler. This method can
	 * take either an AbsoluteTime or a RelativeTime for its argument, and the
	 * handler will be released as if it was created using that type for its
	 * time parameter. An absolute time in the past is equivalent to a relative
	 * time of (0,0). The rescheduling will take place between the invocation
	 * and the return of the method.
	 * 
	 * @param time
	 *            Time at which the handler will be released
	 * 
	 * @throws IllegalArgumentException
	 *             Thrown if time is a negative RelativeTime value or null
	 * 
	 */
	@SCJAllowed(LEVEL_1)
	public void scheduleNextReleaseTime(HighResolutionTime time) {

	}

}
