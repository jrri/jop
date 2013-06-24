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
 * This is the base interface for all scoped memory areas. Scoped memory is a
 * region based memory management strategy that can only be cleared when no
 * thread is executing in the area.
 * 
 * @author martin
 * @version SCJ 0.93
 * @note Martin: Do we need this interface? I think no. Portals and resize is
 *       gone. Therefore, another empty interface....
 * 
 */
@SCJAllowed
public interface ScopedAllocationContext extends AllocationContext {
}
