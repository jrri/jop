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

import static javax.safetycritical.annotate.Level.SUPPORT;

import java.util.Vector;

import javax.realtime.AffinitySet;
import javax.safetycritical.annotate.Allocate;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Allocate.Area;

import com.jopdesign.io.IOFactory;
import com.jopdesign.sys.Native;

/**
 * A Safety Critical Java application is comprised of one or more missions. Each
 * mission is implemented as a subclass of this abstract Mission class. A
 * mission is comprised of one or more ManagedSchedulable objects, conceptually
 * running as independent threads of control, and the data that is shared
 * between them.
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.93
 * 
 */
@SCJAllowed
public abstract class Mission {

	/*
	 * Workaround to avoid illegal references: Store the address itself (a
	 * number) of the structure containing the handler's registered in this
	 * mission.
	 */
	int eventHandlersRef;
	boolean hasEventHandlers = false;

	/* See above... */
	int longEventHandlersRef;
	boolean hasLongEventHandlers = false;

	/* See above... */
	int managedInterruptRef;
	boolean hasManagedInterrupt = false;

	/* To keep track of the state of a mission */
	static final int INACTIVE = 0;
	static final int INITIIALIZATION = 1;
	static final int EXECUTION = 2;
	static final int CLEANUP = 3;

	public int phase = INACTIVE;

	MissionSequencer currentSequencer;

	static Mission currentMission;

	volatile boolean terminationPending = false;
	public volatile boolean execFinished = false;

	/**
	 * Allocate and initialize data structures associated with a Mission
	 * implementation.
	 * 
	 * The constructor may allocate additional infrastructure objects within the
	 * same MemoryArea that holds the implicit this argument.
	 * 
	 * The amount of data allocated in he same MemoryArea as this by the Mission
	 * constructor is implementation-defined. Application code will need to know
	 * the amount of this data to properly size the containing scope.
	 * 
	 * Memory behavior: This constructor may allocate objects within the same
	 * MemoryArea that holds the implicit this argument.
	 */
	@Allocate({ Area.THIS })
	@SCJAllowed
	public Mission() {
	}

	/**
	 * Method to clean data structures and machine state upon termination of
	 * this Mission’s execute phase. Infrastructure code running in the
	 * controlling Mission Sequencer’s bound thread invokes cleanUp after all
	 * ManagedSchedulables associated with this Mission have terminated, but
	 * before control leaves the corresponding MissionMemory area. The default
	 * implementation of cleanUp does nothing.
	 */
	@SCJAllowed(SUPPORT)
	protected void cleanUp() {
		// debug message
		 Terminal.getTerminal().writeln(
				"[SYSTEM]: Default Mission cleanup method");
	}

	/**
	 * Obtain the current mission.
	 * 
	 * @return the instance of the Mission to which the currently executing
	 *         ManagedSchedulable corresponds. The current Mission is known from
	 *         the moment when initialize has been invoked and continues to be
	 *         known until the mission’s last cleanUp method has been completed.
	 *         Otherwise, returns null.
	 */
	@SCJAllowed
	public static Mission getCurrentMission() {

		/*
		 * Who knows about the current mission? The current sequencer. Who knows
		 * about the current sequencer? The current mission. The only additional
		 * information here is the context from where the method is called.
		 * 
		 * 1. If it is called from Mission.initialize(), the current context is
		 * either the top-most sequencer or a nested sequencer. In both cases
		 * the returned object is the same as the one returned by
		 * getNextMission() method (as that objt's initialize() method is
		 * executing).
		 * 
		 * 2. If it is called from handleAsyncEvent() of a MEH
		 */
		/* Works only on L0 or L1 where only one mission executes at a time */
		return currentMission;
	}

	/**
	 * 
	 * @return the MissionSequencer that is overseeing execution of this
	 *         mission.
	 */
	public MissionSequencer<Mission> getSequencer() {
		return currentSequencer;
	}

	/**
	 * Perform initialization of this Mission. Infrastructure calls initialize
	 * after the Mission has been instantiated and the MissionMemory has been
	 * resized to match the size returned from Mission.missionMemorySize. Upon
	 * entry into the initialize method, the current allocation context is the
	 * MissionMemory area dedicated to this particular Mission.
	 * 
	 * The default implementation of initialize does nothing.
	 * 
	 * A typical implementation of initialize instantiates and registers all
	 * ManagedSchedulable objects that constitute this Mission. The
	 * infrastructure enforces that ManagedSchedulables can only be instantiated
	 * and registered if the currently executing ManagedSchedulable is running a
	 * Mission.initialize method under the direction of the SCJ infrastructure.
	 * The infrastructure arranges to begin executing the registered
	 * ManagedSchedulable objects associated with a particular Mission upon
	 * return from its initialize method.
	 * 
	 * Besides initiating the associated ManagedSchedulable objects, this method
	 * may also instantiate and/or initialize certain mission-level data
	 * structures. Note that objects shared between ManagedSchedulables
	 * typically reside within the corresponding MissionMemory scope, but may
	 * alternatively reside in outer-nested MissionMemory or ImmortalMemory
	 * areas. Individual ManagedSchedulables can gain access to these objects
	 * either by supplying their references to the ManagedSchedulable
	 * constructors or by obtaining a reference to the currently running mission
	 * (the value returned from Mission.getCurrentMission), coercing the
	 * reference to the known Mission subclass, and accessing the fields or
	 * methods of this subclass that represent the shared data objects.
	 */
	// why is this SUPPORT?
	@SCJAllowed(SUPPORT)
	protected abstract void initialize();

	/**
	 * This method must be implemented by a safety-critical application. It is
	 * invoked by the SCJ infrastructure to determine the desired size of this
	 * Mission’s MissionMemory area. When this method receives control, the
	 * MissionMemory area will include all of the backing store memory to be
	 * used for all memory areas. Therefore this method will not be able to
	 * create or call any methods that create any PrivateMemory areas. After
	 * this method returns, the SCJ infrastructure shall shrink the
	 * MissionMemory to a size based on the memory size returned by this method.
	 * This will make backing store memory available for the backing stores of
	 * the ManagedSchedulable objects that comprise this mission. Any attempt to
	 * introduce a new PrivateMemory area within this method will result in an
	 * OutOfMemoryError exception.
	 * 
	 * @return the desired size of this Mission’s MissionMemory area
	 * @todo Memory.java do not have any method for resizing memory instances
	 */
	@SCJAllowed
	abstract public long missionMemorySize();

	/**
	 * This method provides a standard interface for requesting termination of a
	 * mission.
	 * 
	 * Once this method is called during Mission execution, subsequent
	 * invocations of terminationPending shall return true, shall invoke this
	 * object’s terminationHook method, and shall invoke
	 * requestSequenceTermination on each inner-nested MissionSequencer object
	 * that is registered for execution within this mission. Additionally, this
	 * method has the effect of arranging to (1) disable all periodic event
	 * handlers associated with this Mission so that they will experience no
	 * further firings, (2) disable all AperiodicEventHandlers so that no
	 * further firings will be honored, (3) clear the pending event (if any) for
	 * each event handler so that the event handler can be effectively shut down
	 * following completion of any event handling that is currently active, (4)
	 * wait for all of the ManagedSchedulable objects associated with this
	 * mission to terminate their execution, (5) invoke the
	 * ManagedSchedulable.cleanUp methods for each of the ManagedSchedulable
	 * objects associated with this mission, and invoking the cleanUp method
	 * associated with this mission.
	 * 
	 * While many of these activities may be carried out asynchronously after
	 * returning from the requestTermination method, the implementation of
	 * requestTermination shall not return until after all of the
	 * ManagedEventHandler objects registered with this Mission have been
	 * disassociated from this Mission so they will receive no further releases.
	 * Before returning, or at least before initialize for this same mission is
	 * called in the case that it is subsequently started, the implementation
	 * shall clear all mission state.
	 */
	@SCJAllowed
	public final void requestTermination() {

		/*
		 * This variable is checked after returning from the handleAsync event in
		 * a PEH and checked before firing an AEH/ALEH
		 */
		terminationPending = true;
		terminationHook();

	}

	/**
	 * Used to call the cleanUp() method of every managed handler associated
	 * with the mission
	 * 
	 * TODO Probably should be part of sequencer and not of mission
	 */
	void terminate() {

		if (hasEventHandlers) {
			Vector eventHandlers = getHandlers();

			/*
			 * Run all cleanUp() methods of every MEH associated with the
			 * mission
			 */
			for (int i = 0; i < eventHandlers.size(); i++) {
				((ManagedEventHandler) eventHandlers.elementAt(i)).cleanUp();
			}

			/*
			 * The vector lives in mission memory. The removeAllElements()
			 * method sets all references to handlers to null. The handler
			 * objects are collected when the mission finishes (i.e. when
			 * mission memory is exited).
			 * 
			 */
			eventHandlers.removeAllElements();

			/*
			 * The following is needed only if mission objects live in immortal
			 * memory.
			 */
			hasEventHandlers = false;
			eventHandlersRef = 0;
		}

		if (hasLongEventHandlers) {
			Vector longEventHandlers = getLongHandlers();

			/*
			 * Run all cleanUp() methods of every MLEH associated with the
			 * mission
			 */
			for (int i = 0; i < longEventHandlers.size(); i++) {
				((ManagedLongEventHandler) longEventHandlers.elementAt(i)).cleanUp();
			}

			/*
			 * The vector lives in mission memory. The removeAllElements()
			 * method sets all references to handlers to null. The handler
			 * objects are collected when the mission finishes (i.e. when
			 * mission memory is exited).
			 */
//			longEventHandlers.removeAllElements();

			/*
			 * The following is needed only if mission objects live in immortal
			 * memory.
			 */
			longEventHandlersRef = 0;
			hasLongEventHandlers = false;
		}

		Vector managedInterrupt = getInterrupts();
		if (managedInterrupt != null) {

			/*
			 * Unregister interrupts
			 */
			for (int i = 0; i < managedInterrupt.size(); i++) {
				ManagedInterruptServiceRoutine mis = (ManagedInterruptServiceRoutine) managedInterrupt
						.elementAt(i);
				mis.unregister();
				// ((ManagedInterruptServiceRoutine)
				// managedInterrupt.elementAt(i))
				// .unregister();
			}

			managedInterrupt.removeAllElements();
			managedInterruptRef = 0;
			hasManagedInterrupt = false;
		}
	}

	/**
	 * This method shall be invoked by requestTermination().
	 * Application-specific subclasses of Mission may override the
	 * terminationHook method to supply application-specific mission shutdown
	 * code.
	 */
	@SCJAllowed(javax.safetycritical.annotate.Level.SUPPORT)
	protected void terminationHook() {
	}

	/**
	 * Implementation specific
	 */

	Vector getHandlers() {
		return (Vector) Native.toObject(eventHandlersRef);
	}

	Vector getLongHandlers() {
		return (Vector) Native.toObject(longEventHandlersRef);
	}

	Vector getInterrupts() {
		if (hasManagedInterrupt) {
			return (Vector) Native.toObject(managedInterruptRef);
		} else {
			return null;
		}
	}

	static void setCurrentMission(Mission m) {
		currentMission = m;
	}
}
