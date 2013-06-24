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

import com.jopdesign.sys.Memory;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.SysHelper;

/**
 * This class represents immortal memory. Objects allocated in immortal memory
 * are never reclaimed during the lifetime of the application.
 * 
 * @author martin
 * @version SCJ 0.93
 * @note There is no ManagedImmortal class. How shall we now implement all the
 *       getter methods via package boundaries... We will transfer the
 *       information via a final class in javax.safetycritical, which has no
 *       public constructor, and call a final method in ImmortalMemory expecting
 *       this type. That is ugly, but should do the job.
 * 
 */
@SCJAllowed
public final class ImmortalMemory extends MemoryArea {

	private static ImmortalMemory instance;

	static SysHelper _sysHelper;

	public static void setHelper(SysHelper sysHelper) {
		_sysHelper = sysHelper;
	}
	
	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public static ImmortalMemory instance() {
		if (instance == null) {
			instance = new ImmortalMemory();
		}
		return instance;
	}

	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public long memoryConsumed() {
		return _sysHelper.memoryConsumed(getImmortalMemory());
	}

	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public long memoryRemaining() {
		return _sysHelper.memoryRemaining(getImmortalMemory());
	}

	@SCJAllowed
	@SCJRestricted(maySelfSuspend = false)
	public long size() {
		return _sysHelper.size(getImmortalMemory());
	}

	private Memory getImmortalMemory() {
		return _sysHelper.getImmortalMemory();
	}
	
	public long xx(){
		return _sysHelper.getRemainingBackingStore(getImmortalMemory());
	}
	
	void enterPrivateMemory(int size, Runnable logic){
		_sysHelper.enterPrivateMemory(_sysHelper.getImmortalMemory(), size, logic);
	}
}
