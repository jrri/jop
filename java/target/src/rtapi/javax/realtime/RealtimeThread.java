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

import static javax.safetycritical.annotate.Level.LEVEL_1;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.RtThreadImpl;
import com.jopdesign.sys.SysHelper;

/**
 * 
 * Real-time threads cannot be directly created by an SCJ application. However,
 * they are needed by the infrastructure to support ManagedThreads. The
 * getCurrentMemoryArea method can be used at Level 1, hence the class is
 * visible at Level 1.
 * 
 * What is the real usage of RealtimeThread in SCJ? It is *just* the static
 * method to get the current allocation context!!!
 * 
 * @author martin
 * 
 */
@SCJAllowed(LEVEL_1)
public class RealtimeThread extends Thread implements Schedulable {
	
	static SysHelper _sysHelper;
	
	public static void setSysHelper(SysHelper sysHelper) {
		_sysHelper = sysHelper;
	}

	// no instances allowed in level 0/1
	private RealtimeThread() {
		super(null);
	}

	/**
	 * Allocates no memory. The returned object may reside in scoped memory,
	 * within a scope that encloses the current execution context.
	 * 
	 * @return a reference to the current allocation context.
	 */
	@SCJAllowed(LEVEL_1)
	@SCJRestricted(maySelfSuspend = false)
	public static MemoryArea getCurrentMemoryArea() {
		
		return _sysHelper.getCurrentManagedMemory();
		
//		throw new Error("implement me");
	}


}
