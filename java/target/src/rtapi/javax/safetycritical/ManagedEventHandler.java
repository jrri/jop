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

import javax.realtime.BoundAsyncEventHandler;
import javax.realtime.HighResolutionTime;
import javax.realtime.PriorityParameters;
import javax.realtime.ReleaseParameters;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Level.INFRASTRUCTURE;

/**
 * In SCJ, all handlers must be registered with the enclosing mission, so SCJ
 * applications use classes that are based on the ManagedEventHandler and the
 * ManagedLongEventHandler class hierarchies.
 * 
 * Note that the values in parameter classes passed to the constructors are
 * those that will be used by the infrastructure. Changing these values after
 * construction will have no impact on the created event handler.
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.93
 * @note An almost empty class, just to add two methods.
 * 
 */
@SCJAllowed
public abstract class ManagedEventHandler extends BoundAsyncEventHandler
		implements ManagedSchedulable {

	/*
	 * Workaround to avoid illegal assignments when referring to constant
	 * strings. Constant strings in JOP have no associated memory area
	 */
	protected StringBuffer name;

	@SCJAllowed(INFRASTRUCTURE)
	@SCJRestricted(phase = INITIALIZATION)
	ManagedEventHandler(PriorityParameters priority, ReleaseParameters release,
			StorageParameters scp, String name) {
		this.name = new StringBuffer(name);
	}

	ManagedEventHandler(PriorityParameters priority, StorageParameters scp,
			String name) {
		this.name = new StringBuffer(name);
	}

	ManagedEventHandler(PriorityParameters priority, HighResolutionTime time,
			StorageParameters storage) {
		this(priority, time, storage, "");
	}

	ManagedEventHandler(PriorityParameters priority, HighResolutionTime time,
			StorageParameters storage, String name) {
		this.name = new StringBuffer(name);
	}

	/**
	 * Application developers override this method with code to be executed when
	 * this event handler’s execution is disabled (after termination of the
	 * enclosing mission has been requested).
	 * 
	 * MissionMemory is the current allocation context on entry into this
	 * method. When the cleanUp method is called, a private memory area shall be
	 * provided for its use, and shall be the current memory area. If desired,
	 * the cleanUp method may introduce a new PrivateMemory area. The memory
	 * allocated to ManagedSchedulables shall be available to be reclaimed when
	 * each Mission’s cleanUp method returns.
	 */
	@Override
	@SCJAllowed(SUPPORT)
	@SCJRestricted(phase = CLEANUP)
	public void cleanUp() {
		Terminal.getTerminal().writeln("[SYSTEM]: Default MEH cleanup");
	}

	/**
	 * 
	 * @return a string name of this event handler. The actual object returned
	 *         shall be the same object that was passed to the event handler
	 *         constructor.
	 */
	@SCJAllowed
	public String getName() {
		return name.toString();
	}

}
