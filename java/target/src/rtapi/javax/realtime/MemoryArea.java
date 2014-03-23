/*
  This file is part of JOP, the Java Optimized Processor
    see <http://www.jopdesign.com/>
  This subset of javax.realtime is provided for the JSR 302
  Safety Critical Specification for Java

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

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

/**
 * All allocation contexts are implemented by memory areas. This is the base
 * level class for all memory areas.
 * 
 * @version SCJ 0.93
 * @note All methods will be overridden by SCJ classes. MemoryArea remains a
 *       dummy class, as all other RTSJ memory classes.
 * 
 */
@SCJAllowed
public abstract class MemoryArea implements AllocationContext {

	/**
	 * Returns the memory area in which object is allocated
	 * 
	 * @param object
	 *            the object to query its MemoryArea
	 * @return the memory area in which the object parameter is allocated
	 * 
	 * @note This method should be eliminated if we want to disallow obtaining
	 *       references to arbitrary memory areas
	 */
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public static MemoryArea getMemoryArea(Object object) {

		return null;
	}

	/**
	 * @return the memory consumed in this memory area.
	 */
	@Override
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public abstract long memoryConsumed();

	/**
	 * @return the memory remaining in this memory area.
	 */
	@Override
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public abstract long memoryRemaining();

	/**
	 * The size of a memory area is always equal to the {memoryConsumed() +
	 * memoryRemaining()}
	 * 
	 * @return the total size of this memory area.
	 */
	@Override
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public abstract long size();

	@Override
	@SCJAllowed
	public Object newInstance(Class type) throws InstantiationException,
			IllegalAccessException, OutOfMemoryError {

		return type.newInstance();
	}

}