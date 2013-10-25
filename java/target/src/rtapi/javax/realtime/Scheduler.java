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
import com.jopdesign.sys.SysHelper;

/**
 * The RTSJ supports generic on-line feasibility analysis via the Scheduler
 * class. SCJ supports off-line analysis; hence most of the methods in this
 * class are omitted. Only the static method getCurrentSO is provided.
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 * 
 */
@SCJAllowed
public abstract class Scheduler {

	static SysHelper _sysHelper;

	public static void setSysHelper(SysHelper sysHelper) {
		_sysHelper = sysHelper;
	}

	/**
	 * 
	 * @return the current asynchronous event handler or real-time thread of the
	 *         caller.
	 */
	@SCJAllowed
	public static Schedulable getCurrentSO() {
		return _sysHelper.getSchedulable();
	}
}
