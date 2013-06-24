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

package javax.realtime;

import javax.safetycritical.annotate.BlockFree;
import javax.safetycritical.annotate.SCJAllowed;

/**
 * This class is restricted relative to the RTSJ so that it allows the priority
 * to be created and queried, but not changed.
 * 
 * In SCJ the range of priorities is separated into software priorities and
 * hardware priorities. Hardware priorities have higher values than software
 * priorities. Schedulable objects can be assigned only software priorities.
 * Ceiling priorities can be either software or hardware priorities.
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.93
 * 
 */
@SCJAllowed
public class PriorityParameters extends SchedulingParameters {

	int priority;

	/**
	 * Create a PriorityParameters object specifying the given priority.
	 * 
	 * @param priority
	 *            is the integer value of the specified priority.
	 * 
	 * @throws IllegalArgumentException
	 *             if priority is not in the range of supported priorities.
	 * 
	 * @todo Throwing of IllegalArgumentException.
	 */
	@BlockFree
	@SCJAllowed
	public PriorityParameters(int priority) {
		this.priority = priority;
	}

	/**
	 * 
	 * @return the integer priority value that was specified at construction
	 *         time
	 */
	@BlockFree
	@SCJAllowed
	public int getPriority() {
		return this.priority;
	}

}
