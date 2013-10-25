package javax.safetycritical;

import static javax.safetycritical.annotate.Level.LEVEL_1;

import java.util.Vector;

import javax.realtime.AffinitySet;
import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.annotate.MemoryAreaEncloses;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

import joprt.SwEvent;

import static javax.safetycritical.annotate.Phase.INITIALIZATION;

/**
 * This class permits the automatic execution of code that is bound to an
 * aperiodic event. It is abstract. Concrete subclasses must implement the
 * handleAsynEvent method and may override the default cleanup method.
 * 
 * Note, there is no programmer access to the RTSJ fireCount mechanisms, so the
 * associated methods are missing.
 * 
 * Note that the values in parameters classes passed to the constructors are
 * those that will be used by the infrastructure. Changing these values after
 * construction will have no impact on the created event handler.
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 * @note Trying to change the affinity of this AperiodicLongEventHandler after
 *       the register() method has been called has no effect.
 * 
 * 
 */
@SCJAllowed(LEVEL_1)
public abstract class AperiodicLongEventHandler extends ManagedLongEventHandler {

	// String name;
	StringBuffer name;
	SwEvent event;
	long _data;
	// Memory privMem;
	PrivateMemory privMem;
	RtThreadImpl rtt;

	/**
	 * Constructor to create an aperiodic event handler.
	 * <p>
	 * Does not perform memory allocation. Does not allow this to escape local
	 * scope. Builds links from this to priority and parameters, so those two
	 * arguments must reside in scopes that enclose this. Builds a link from
	 * ``this" to event, so event must reside in memory that encloses ``this".
	 * 
	 * @param priority
	 *            specifies the priority parameters for this periodic event
	 *            handler. Must not be null.
	 * 
	 * @param release_info
	 *            specifies the periodic release parameters, in particular the
	 *            start time, period and deadline miss and cost overrun
	 *            handlers. Note that a relative start time is not relative to
	 *            NOW but relative to the point in time when initialization is
	 *            finished and the timers are started. This argument must not be
	 *            null. TBD whether we support deadline misses and cost overrun
	 *            detection.
	 * 
	 * @param scp
	 *            The mem_info parameter describes the organization of memory
	 *            dedicated to execution of the underlying thread.
	 * 
	 * @param event
	 *            the aperiodic event that when fired should release this
	 *            handler.
	 * 
	 * @throws IllegalArgumentException
	 *             if priority, parameters or event is null.
	 */
	@MemoryAreaEncloses(inner = { "this", "this", "this" }, outer = {
			"priority", "release_info", "mem_info" })
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(phase = INITIALIZATION)
	public AperiodicLongEventHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage,
			long scopeSize) {
		this(priority, release, storage, scopeSize, "");
	}

	/**
	 * Constructor to create an aperiodic event handler.
	 * <p>
	 * Does not perform memory allocation. Does not allow this to escape local
	 * scope. Builds links from this to priority and parameters, so those two
	 * arguments must reside in scopes that enclose this. Builds a link from
	 * ``this" to event, so event must reside in memory that encloses ``this".
	 * 
	 * @param priority
	 *            specifies the priority parameters for this periodic event
	 *            handler. Must not be null.
	 * 
	 * @param release_info
	 *            specifies the periodic release parameters, in particular the
	 *            start time, period and deadline miss and cost overrun
	 *            handlers. Note that a relative start time is not relative to
	 *            NOW but relative to the point in time when initialization is
	 *            finished and the timers are started. This argument must not be
	 *            null. TBD whether we support deadline misses and cost overrun
	 *            detection.
	 * 
	 * @param scp
	 *            The mem_info parameter describes the organization of memory
	 *            dedicated to execution of the underlying thread.
	 * 
	 * @param name
	 *            the name by which this event handler is identified.
	 * 
	 * @param event
	 *            the aperiodic event that when fired should release this
	 *            handler.
	 * 
	 * @throws IllegalArgumentException
	 *             if priority, parameters or event is null.
	 */
	@MemoryAreaEncloses(inner = { "this", "this", "this", "this", "this" }, outer = {
			"priority", "release_info", "scp", "event", "name" })
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(phase = INITIALIZATION)
	public AperiodicLongEventHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage,
			long scopeSize, String name) {
		super(priority, release, storage, name);

		if (storage != null) {
			// Create private memory
			// privMem = new Memory((int) scopeSize, (int)
			// storage.getTotalBackingStoreSize());
			privMem = new PrivateMemory((int) scopeSize,
					(int) storage.getTotalBackingStoreSize());
		}

		final Runnable runner = new Runnable() {
			@Override
			public void run() {
				handleAsyncEvent(_data);
			}
		};

		this.name = new StringBuffer(name);
		// this.name = name;

		event = new SwEvent(priority.getPriority(), 0) {

			@Override
			public void handle() {
				if (!Mission.currentMission.terminationPending)
					privMem.enter(runner);
			}
		};

		rtt = event.thr;
	}

	/**
	 * @see javax.safetycritical.ManagedSchedulable#register() Registers this
	 *      event handler with the current mission and attaches this handler to
	 *      all the aperiodic events passed during construction. Registers all
	 *      the aperiodic events passed during constructions.
	 */
	@SCJAllowed
	@SCJRestricted(phase = INITIALIZATION)
	public final void register() {
		Mission m = Mission.getCurrentMission();
		if (!m.hasLongEventHandlers) {
			// System.out.println("creating MLEH vector...");
			m.longEventHandlersRef = Native.toInt(new Vector());
			m.hasLongEventHandlers = true;
		}

		((Vector) Native.toObject(m.longEventHandlersRef)).addElement(this);
		RtThreadImpl.register(rtt);

		/*
		 * Change the processor where the RtThread will run in case its affinity
		 * was changed from the default value. Note that trying to change the
		 * affinity after the register method has been called has no effect.
		 */
		AffinitySet set = AffinitySet.getAffinitySet(this);
		event.setProcessor(_rtsjHelper.getAffinitySetProcessor(set));

	}

	/**
	 * Release this aperiodic event handler
	 * 
	 * @param data
	 *            A long value that will be passed as argument when the handler
	 *            is released
	 */
	public final void release(long data) {
		_data = data;
		event.fire();
	}
}
