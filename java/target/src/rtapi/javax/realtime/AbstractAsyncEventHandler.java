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

import static javax.safetycritical.annotate.Level.LEVEL_0;

/**
 * 
 * This is the base class for all asynchronous event handlers. In SCJ, this is
 * an empty class.
 * 
 * @author Martin Schoeberl
 * 
 */
@SCJAllowed(LEVEL_0)
public abstract class AbstractAsyncEventHandler implements Schedulable {
	
	AffinitySet affintySet;
	
	void setProcessorAffinity(AffinitySet set){
		this.affintySet = set;
	}
	
	AffinitySet getAffinitySet(){
		return this.affintySet;
	}

}
