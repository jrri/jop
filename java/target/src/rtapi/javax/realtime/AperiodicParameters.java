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

/**
 * SCJ supports no detection of minimum inter-arrival time violations, therefore
 * only aperiodic parameters are needed. Hence the RTSJ SporadicParameters class
 * is absent. Deadline miss detection is supported.
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.93
 * @note The following text, still in the specification, is not valid anymore
 *       since there are no events in SCJ: The RTSJ supports a queue for storing
 *       the arrival of release events is order to enable bursts of events to be
 *       handled. This queue is of length 1 in SCJ. The RTSJ also enables
 *       different responses to the queue overflowing. In SCJ the overflow
 *       behavior is to overwrite the pending release event if there is one.
 * 
 */
@SCJAllowed(LEVEL_1)
public class AperiodicParameters extends ReleaseParameters {

	/**
	 * Construct a new AperiodicParameters object within the current memory area
	 * with no deadline detection facility.
	 */
	@SCJAllowed(LEVEL_1)
	public AperiodicParameters() {
		this.deadline = null;
		this.missHandler = null;
	}

	/**
	 * Construct a new object within the current memory area.
	 * 
	 * @parameter deadline is an offset from the release time by which the
	 *            release should finish. A null deadline indicates that there is
	 *            no deadline.
	 * @parameter handler is the AsynchronousEventHandler to be release if the
	 *            associated schedulable object misses its deadline. A null
	 *            parameter indicates that no handler should be release.
	 */
	@SCJAllowed(LEVEL_1)
	public AperiodicParameters(RelativeTime deadline,
			AsyncEventHandler missHandler) {
		this.deadline = deadline;
		this.missHandler = missHandler;
	}
}
