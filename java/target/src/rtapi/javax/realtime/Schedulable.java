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

/**
 * 
 * In keeping with the RTSJ, SCJ event handlers are schedulable objects.
 * However, the Schedulable interface in the RTSJ is mainly concerned with
 * on-line feasibility analysis and the getting and setting of the parameter
 * classes. On the contrary, in SCJ, it provides no extra functionality over the
 * Runnable interface.
 * 
 * For SCJ this is an empty interface, which just introduces the issue of
 * Runnable into event handlers where run() shall not be invoked.
 * 
 * @author Martin Schoeberl
 * @version SCJ 0.93
 * 
 */
@SCJAllowed
public interface Schedulable extends Runnable {
}
