/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>

  Copyright (C) 2008-2011, Martin Schoeberl (martin@jopdesign.com)

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package javax.safetycritical;

import static javax.safetycritical.annotate.Level.LEVEL_1;

import java.util.Vector;

import javax.realtime.AbsoluteTime;
import javax.realtime.AffinitySet;
import javax.realtime.HighResolutionTime;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.annotate.MemoryAreaEncloses;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

import static javax.safetycritical.annotate.Phase.INITIALIZATION;

import joprt.RtThread;

/**
 * This class permits the automatic periodic execution of code. The
 * handleAsyncEvent method behaves as if the handler were attached to a periodic
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
 * The class that represents periodic activity. Should be used as the main
 * vehicle for real-time applications.
 * 
 * Now finally we have a class that the implementation can use.
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.93
 * @note Trying to change the affinity of this PeriodicEventHandler after the
 *       register() method has been called has no effect.
 * 
 */

@SCJAllowed
public abstract class PeriodicEventHandler extends ManagedEventHandler {

	HighResolutionTime start, period;
	PrivateMemory privMem;
	StorageParameters storage;
	PeriodicParameters release;

	RtThread thread;
	RtThreadImpl rtt;
	
	//ThrowBoundaryError tbe = new ThrowBoundaryError();

	// For test and debug purposes
	public int missCount = 0;

	/**
	 * Constructs a periodic event handler.
	 * 
	 * @param priority
	 *            specifies the priority parameters for this periodic event
	 *            handler. Must not be null.
	 * 
	 * @param release
	 *            specifies the periodic release parameters, in particular the
	 *            start time, period and deadline miss handler. Note that a
	 *            relative start time is not relative to now, rather it is
	 *            relative to the point in time when initialization is finished
	 *            and the timers are started. This argument must not be null.
	 * 
	 * @param storage
	 *            specifies the storage parameters for the periodic event
	 *            handler. It must not be null.
	 * 
	 * @param scopeSize
	 *            the size of the private memory that will be associated with
	 *            the handler
	 */
	@MemoryAreaEncloses(inner = { "this", "this", "this" }, outer = {
			"priority", "parameters", "scp" })
	@SCJAllowed
	@SCJRestricted(phase = INITIALIZATION)
	public PeriodicEventHandler(PriorityParameters priority,
			PeriodicParameters release, StorageParameters storage) {
		this(priority, release, storage, "");
	}

	/**
	 * Constructs a periodic event handler.
	 * 
	 * @param priority
	 *            specifies the priority parameters for this periodic event
	 *            handler. Must not be null.
	 * 
	 * @param release
	 *            specifies the periodic release parameters, in particular the
	 *            start time, period and deadline miss handler. Note that a
	 *            relative start time is not relative to now, rather it is
	 *            relative to the point in time when initialization is finished
	 *            and the timers are started. This argument must not be null.
	 * 
	 * @param storage
	 *            specifies the storage parameters for the periodic event
	 *            handler. It must not be null.
	 * 
	 * @param scopeSize
	 *            the size of the private memory that will be associated with
	 *            the handler
	 * @param name
	 *            A string representing the name of the handler
	 * 
	 * @hrows IllegalArgumentException if priority, release, or scp is null
	 */
	@MemoryAreaEncloses(inner = { "this", "this", "this", "this" }, outer = {
			"priority", "parameters", "scp", "name" })
	@SCJAllowed(LEVEL_1)
	public PeriodicEventHandler(PriorityParameters priority,
			PeriodicParameters release, StorageParameters storage, String name) {
		
		// TODO: what are we doing with this Managed thing?
		// For now, the MEH super class holds a reference to the mission to which
		// the handler belongs and performs the checks for null arguments.
		super(priority, release, storage, name);

		// preallocate a ThrowBoundaryError
		
		
		this.storage = storage;
		this.release = release;

		// start = (RelativeTime) release.getStart();
		// period = release.getPeriod();
		this.start = this.release.getStart();
		this.period = this.release.getPeriod();

		// TODO scp

		int p = ((int) period.getMilliseconds()) * 1000
				+ period.getNanoseconds() / 1000;
		if (p < 0) { // Overflow
			p = Integer.MAX_VALUE;
		}
		int off = ((int) start.getMilliseconds()) * 1000
				+ start.getNanoseconds() / 1000;
		if (off < 0) { // Overflow
			off = Integer.MAX_VALUE;
		}

		/*
		 * Mission should not be null at this point, as PEH's are created at
		 * mission initialization.
		 * FIXME
		 */
		m = Mission.getCurrentMission();

		/*
		 * No need to create runnables, RT threads, or handler's private
		 * memories. For cyclic executives handler's handleAsyncEvent method is
		 * called directly by the main thread (i.e. it does not go into the
		 * scheduler).
		 * 
		 * For cyclic executives a single private memory is reused for all
		 * handlers.
		 */
		if (!(m instanceof CyclicExecutive)) {
			privMem = new PrivateMemory((int) storage.maxMemoryArea,
					(int) storage.totalBackingStore);

			final Runnable runner = new Runnable() {
				@Override
				public void run() {
					handleAsyncEvent();
				}
			};

			thread = new RtThread(priority.getPriority(), p, off) {

				public void run() {
					
					// while (!MissionSequencer.terminationRequest) {
					for (;;) {
						privMem.enter(runner);

						// do not schedule this task to run again
						if (m.terminationPending) {
							// debug message
							// System.out.println("done");
							finished = true;
							break;
						}

						if (!waitForNextPeriod()) {
							// debug message
							// System.out.println("Deadline missed: " + getName());
							
							// For test and debug purposes
							missCount++;
							executeMissHandler();
						}
					}
				}
			};

			rtt = thread.thr;
		}
	}

	/**
	 * Registers the schedulable object with its mission.
	 */
	@SCJAllowed
	@SCJRestricted(phase = INITIALIZATION)
	public final void register() {

		final Mission m = Mission.getCurrentMission();

		if (!m.hasEventHandlers) {
			// System.out.println("creating MEH vector...");
			m.eventHandlersRef = Native.toInt(new Vector());
			m.hasEventHandlers = true;
		}

		((Vector) Native.toObject(m.eventHandlersRef)).addElement(this);

		/* L0 applications do not schedule event handlers */
		if (!(m instanceof CyclicExecutive)) {
			RtThreadImpl.register(rtt);
			_sysHelper.setSchedulable(rtt, this);

			/*
			 * Change the processor where the RtThread will run in case its
			 * affinity was changed from the default value. Note that trying to
			 * change the affinity after the register method has been called has
			 * no effect.
			 */
			AffinitySet set = AffinitySet.getAffinitySet(this);
			thread.setProcessor(_rtsjHelper.getAffinitySetProcessor(set));
		}
		
	}

	/**
	 * Get the actual start time of this handler. The actual start time of the
	 * handler is different from the requested start time (passed at
	 * construction time) when the requested start time is an absolute time that
	 * would occur before the mission has been started. In this case, the actual
	 * start time is the time the mission started. If the actual start time is
	 * equal to the effect start time, then the method behaves as if
	 * getResquestedStartTime() method has been called. If it is different, then
	 * a newly created time object is returned. The time value is associated
	 * with the same clock as that used with the original start time parameter.
	 * 
	 * @return a reference to a time parameter based on the clock used to start
	 *         the timer.
	 * @todo Not yet implemented
	 */
	@SCJAllowed(LEVEL_1)
	public HighResolutionTime getActualStartTime() {
		return null;

	}

	/**
	 * Get the effective start time of this handler. If the clock associated
	 * with the start time parameter and the interval parameter (that were
	 * passed at construction time) are the same, then the method behaves as if
	 * getActualStartTime() has been called. If the two clocks are different,
	 * then the method returns a newly created object whose time is the current
	 * time of the clock associated with the interval parameter (passed at
	 * construction time) when the handler is actually started.
	 * 
	 * @return a reference based on the clock associated with the interval
	 *         parameter.
	 * @todo Not yet implemented
	 */
	@SCJAllowed(LEVEL_1)
	public HighResolutionTime getEffectiveStartTime() {
		return null;

	}

	/**
	 * Get the last release time of this handler.
	 * 
	 * @return a reference to a newly-created javax.safetycritical.AbsoluteTime
	 *         object representing this handlers’s last release time, according
	 *         to the clock associated with the interval parameter used at
	 *         construction time.
	 * @throws IllegalStateException
	 *             if this timer has not been released since it was last
	 *             started.
	 * @todo Not yet implemented
	 */
	@SCJAllowed(LEVEL_1)
	public AbsoluteTime getLastReleaseTime() throws IllegalStateException {
		return null;

	}

	/**
	 * Get the time at which this handler is next expected to be released.
	 * 
	 * 
	 * @return The absolute time at which this handler is expected to be
	 *         released in a newly allocated javax.safetycritical.AbsoluteTime
	 *         object. The clock association of the returned time is the clock
	 *         on which interval parameter (passed at construction time) is
	 *         based.
	 * 
	 * @throws ArithmeticException
	 *             if the result does not fit in the normalized format. Throws
	 *             IllegalStateException Thrown if this handler has not been
	 *             started.
	 * @todo Not yet implemented
	 */
	@SCJAllowed(LEVEL_1)
	public AbsoluteTime getNextReleaseTime() throws ArithmeticException {
		return null;
	}

	/**
	 * Get the requested start time of this periodic handler. Note that the
	 * start time uses copy semantics, so changes made to the value returned by
	 * this method will not effect the requested start time of this handler if
	 * it has not already been started.
	 * 
	 * @return a reference to the start time parameter in the release parameters
	 *         used when constructing this handler.
	 */
	@SCJAllowed(LEVEL_1)
	public HighResolutionTime getRequestedStartTime() {
		
		/* The requested start time is measured relative to the start of the mission */
		return this.start;

	}

	/**
	 * Get the time at which this handler is next expected to be released.
	 * 
	 * @param dest
	 *            The instance of javax.safetycritical.AbsoluteTime which will
	 *            be updated in place and returned. The clock association of the
	 *            dest parameter is ignored. When dest is null a new object is
	 *            allocated for the result.
	 * 
	 * @return the instance of javax.safetycritical.AbsoluteTime passed as
	 *         parameter, with time values representing the absolute time at
	 *         which this handler is expected to be released. If the dest
	 *         parameter is null the result is returned in a newly allocated
	 *         object. The clock association of the returned time is the clock
	 *         on which the interval parameter (passed at construction time) is
	 *         based.
	 * 
	 * @throws ArithmeticException
	 *             If the result does not fit in the normalized format.
	 * 
	 * @throws IllegalStateException
	 *             If this handler has not been started.
	 * @todo Not yet implemented
	 */
	@SCJAllowed(LEVEL_1)
	public AbsoluteTime getnextReleaseTime(AbsoluteTime dest)
			throws ArithmeticException, IllegalStateException {
		return null;
	}

//	/**
//	 * Not on spec, implementation specific
//	 */
//	protected long getScopeSize() {
//		return this.storage.getMaxMemoryArea();
//	}
	
}
