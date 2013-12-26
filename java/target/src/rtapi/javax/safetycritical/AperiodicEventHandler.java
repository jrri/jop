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
 * handleAsyncEvent method and may override the default cleanup method.
 * 
 * Note, there is no programmer access to the RTSJ fireCount mechanisms, so the
 * associated methods are missing.
 * 
 * Note that the values in parameters passed to the constructors are those that
 * will be used by the infrastructure. Changing these values after construction
 * will have no impact on the created event handler.
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.93
 * @note Trying to change the affinity of this AperiodicEventHandler after the
 *       register() method has been called has no effect.
 * 
 */
@SCJAllowed(LEVEL_1)
public abstract class AperiodicEventHandler extends ManagedEventHandler {

	// String name;
	SwEvent event;
	// Memory privMem;
	PrivateMemory privMem;
	RtThreadImpl rtt;

	/**
	 * Constructs an aperiodic event handler that can be explicitly released.
	 * 
	 * @param priority
	 *            specifies the priority parameters for this aperiodic event
	 *            handler. Must not be null.
	 * 
	 * @param release
	 *            specifies the release parameters for this aperiodic event
	 *            handler; it must not be null.
	 * 
	 * @param storage
	 *            specifies the StorageParameters for this aperiodic event
	 *            handler
	 * 
	 * @param scopeSize
	 *            the size of the private memory that will be associated with
	 *            the handler
	 */
	@MemoryAreaEncloses(inner = { "this", "this", "this", "this" }, outer = {
			"priority", "release_info", "mem_info", "event" })
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(phase = INITIALIZATION)
	public AperiodicEventHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage,
			long scopeSize) {
		this(priority, release, storage, scopeSize, "");
	}

	/**
	 * Constructs an aperiodic event handler that can be explicitly released.
	 * 
	 * @param priority
	 *            specifies the priority parameters for this aperiodic event
	 *            handler. Must not be null.
	 * 
	 * @param release
	 *            specifies the release parameters for this aperiodic event
	 *            handler; it must not be null.
	 * 
	 * @param storage
	 *            specifies the StorageParameters for this aperiodic event
	 *            handler
	 * 
	 * @param scopeSize
	 *            the size of the private memory that will be associated with
	 *            the handler
	 * @param name
	 *            A string representing the name of the handler
	 */
	@MemoryAreaEncloses(inner = { "this", "this", "this", "this", "this" }, outer = {
			"priority", "release_info", "scp", "event", "name" })
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(phase = INITIALIZATION)
	public AperiodicEventHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage,
			long scopeSize, String name) {
		super(priority, null, release, storage, name);

		if (storage != null) {
			// privMem = new Memory((int) scopeSize, (int)
			// storage.getTotalBackingStoreSize());
			privMem = new PrivateMemory((int) scopeSize,
					(int) storage.getTotalBackingStoreSize());
		}

		final Runnable runner = new Runnable() {
			@Override
			public void run() {
				handleAsyncEvent();
			}
		};

		// Aperiodic = Sporadic with minimum inter-arrival time set to zero
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
	 * Registers the schedulable object with its mission.
	 */
	@SCJAllowed
	@SCJRestricted(phase = INITIALIZATION)
	public final void register() {
		Mission m = Mission.getCurrentMission();
		if (!m.hasEventHandlers) {
			// System.out.println("creating MEH vector...");
			m.eventHandlersRef = Native.toInt(new Vector());
			m.hasEventHandlers = true;
		}

		((Vector) Native.toObject(m.eventHandlersRef)).addElement(this);
		RtThreadImpl.register(rtt);
		_sysHelper.setSchedulable(rtt, this);

		/*
		 * Change the processor where the RtThread will run in case its affinity
		 * was changed from the default value. Note that trying to change the
		 * affinity after the register method has been called has no effect.
		 */
		AffinitySet set = AffinitySet.getAffinitySet(this);
		event.setProcessor(_rtsjHelper.getAffinitySetProcessor(set));

	}

	/**
	 * This method is concrete in the RTSJ superclass, but now it is abstract.
	 */
	public abstract void handleAsyncEvent();

	/**
	 * An internal method to unblock the handler.
	 */
	void unblock() {
		// TODO Auto-generated method stub
	}

	/**
	 * Release this aperiodic event handler
	 */
	public final void release() {
		event.fire();
	}
}
