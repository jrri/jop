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

import javax.safetycritical.annotate.SCJAllowed;

/**
 * StorageParameters provide storage size parameters for ISRs and schedulable
 * objects in a ManagedSchedulable: event handlers, threads, and sequencers. A
 * StorageParameters object is passed as a parameter to the constructor of
 * mission sequencers and other SCJ schedulable objects.
 * 
 * This class might get renamed to MemoryParameters (back again).
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.94
 * 
 */
@SCJAllowed
public final class StorageParameters {

	long totalBackingStore;
	long maxMemoryArea;
	long maxImmortal;

	/**
	 * This is the primary constructor for a StorageParameters object,
	 * permitting specification of all settable values.
	 * 
	 * @param totalBackingStore
	 *            size of the backing store reservation for worst-case scope
	 *            usage by the associated ManagedSchedulable: object, in bytes.
	 * @param sizes
	 *            is an array of parameters for configuring VM resources such as
	 *            native stack or Java stack size. The meanings of the entries
	 *            in the array are vendor specific. The array passed is not
	 *            stored in the object.
	 * @param messageLength
	 *            memory space in bytes dedicated to the message associated with
	 *            this ManagedSchedulable object’s ThrowBoundaryError exception,
	 *            plus references to the method names/identifiers in the stack
	 *            backtrace.
	 * @param stackTraceLength
	 *            is the number of elements in the StackTraceElement array
	 *            dedicated to stack backtrace associated with this
	 *            StorageParameters object’s Throw- BoundaryError exception.
	 * @param maxMemoryArea
	 *            is the maximum amount of memory in the per-release private
	 *            memory area.
	 * @param maxImmortal
	 *            is the maximum amount of memory in the immortal memory area
	 *            required by the associated schedulable object.
	 * @param maxMissionMemory
	 *            is the maximum amount of memory in the mission memory area
	 *            required by the associated schedulable object.
	 * 
	 * @throws IllegalArgumentException
	 *             if any value other than positive. zero, or NO_MAX is passed
	 *             as the value of maxMemoryArea or maxImmortal.
	 */
	@SCJAllowed
	public StorageParameters(long totalBackingStore, long[] sizes,
			int messageLength, int stackTraceLength, long maxMemoryArea,
			long maxImmortal, long maxMissionMemory) {

		this.totalBackingStore = totalBackingStore;
		this.maxMemoryArea = maxMemoryArea;
		this.maxImmortal = maxImmortal;

	}

	@SCJAllowed
	public StorageParameters(long totalBackingStore, long[] sizes,
			long maxMemoryArea, long maxImmortal, long maxMissionMemory) {
		this(totalBackingStore, sizes, 0, 0, maxMemoryArea, maxImmortal,
				maxMissionMemory);

	}

//	/* Implementation specific */
//	long getTotalBackingStoreSize() {
//		return totalBackingStore;
//	}
//	
//	long getMaxMemoryArea(){
//		return maxMemoryArea;
//	}

}
