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
import javax.safetycritical.annotate.SCJProtected;

import static javax.safetycritical.annotate.Level.LEVEL_0;

/**
 * In SCJ, all asynchronous events must have their handlers bound to a thread
 * when they are created (during the initialization phase). The binding is
 * permanent. Thus, the AsyncEventHandler constructors are hidden from public
 * view in the SCJ specification.
 * 
 * @author Martin Schoeberl
 * @version SCJ 0.93
 * @note An almost empty class.
 * 
 */
@SCJAllowed(LEVEL_0)
public class AsyncEventHandler extends AbstractAsyncEventHandler {

	/**
	 * This is an old RTSJ issue. It is used by the RTSJ implementation and shall
	 * not be called from user code. But it is visible!
	 */
	@Override
	@SCJProtected
	public final void run() {
	}

	/**
	 * This method must be overridden by the application to provide the handling
	 * code.
	 */
	@SCJAllowed
	public void handleAsyncEvent() {
	}
}
