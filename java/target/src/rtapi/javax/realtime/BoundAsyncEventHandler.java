/*
This file is part of JOP, the Java Optimized Processor
  see <http://www.jopdesign.com/>

Copyright (C) 2008, Martin Schoeberl (martin@jopdesign.com)

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

/**
 * A bound asynchronous event handler is an instance of AsyncEventHandler that
 * is permanently bound to a dedicated real-time thread.
 * 
 * The BoundAsyncEventHandler class is not directly available to the safety
 * critical Java application developers. Hence none of its methods or
 * constructors are publicly available.
 * 
 * @author Martin Schoeberl
 * @note This class is empty - just a marker for SCJ RTSJ compatibility.
 * 
 */
@SCJAllowed
public class BoundAsyncEventHandler extends AsyncEventHandler {


}
