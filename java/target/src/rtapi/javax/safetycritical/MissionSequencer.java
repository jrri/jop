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

import static javax.safetycritical.annotate.Level.LEVEL_2;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Level.INFRASTRUCTURE;

import java.util.Vector;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.PriorityParameters;
import javax.safetycritical.JopSystem.TerminationHelper;
import javax.safetycritical.annotate.MemoryAreaEncloses;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import joprt.RtThread;
import joprt.SwEvent;

import static javax.safetycritical.annotate.Phase.INITIALIZATION;

/**
 * A MissionSequencer oversees a sequence of Mission executions. The sequence
 * may include interleaved execution of independent missions and repeated
 * executions of missions.
 * 
 * As a subclass of ManagedEventHandler, MissionSequencer is bound to an event
 * handling thread. The bound thread’s execution priority and memory budget are
 * specified by constructor parameters.
 * 
 * This MissionSequencer executes vendor-supplied infrastructure code which
 * invokes user-defined implementations of MissionSequencer.getNextMission,
 * Mission.initialize, and Mission.cleanUp. During execution of an inner-nested
 * mission, the MissionSequencer’s thread remains blocked waiting for the
 * mission to terminate. An invocation of
 * MissionSequencer.requestSequenceTermination will unblock this waiting thread
 * so that it can perform an invocation of the running mission’s
 * requestTermination method if the mission is still running and its termination
 * has not already been requested.
 * 
 * Note that if a MissionSequencer object is preallocated by the application, it
 * must be allocated in the same scope as its corresponding Mission.
 * 
 * That's the root of (all evil ;-), no the main startup logic...
 * 
 * @author Martin Schoeberl, Juan Rios
 * @version SCJ 0.93
 * 
 * @param <SpecificMission>
 * 
 */
@SCJAllowed
public abstract class MissionSequencer<SpecificMission extends Mission> extends
		ManagedEventHandler {

	private SwEvent clean;
	// private boolean cleanupDidRun;
	// public static boolean cleanupDidRun;
	StorageParameters storage;

	TerminationHelper terminationHelper;
	Mission currMission;

	// why is this static?
	// ok, in level 1 we have only one mission.
	// But it is ugly that Mission does not know about its
	// sequencer and therefore cannot call the termination on
	// a specific one.
	static boolean terminationRequest = false;

	/**
	 * Construct a MissionSequencer object to oversee a sequence of mission
	 * executions.
	 * 
	 * Memory behavior: This constructor requires that the "priority" argument
	 * reside in a scope that encloses the scope of the "this" argument. This
	 * constructor requires that the "storage" argument reside in a scope that
	 * encloses the scope of the "this" argument. This constructor requires that
	 * the "name" argument reside in a scope that encloses the scope of the
	 * "this" argument.
	 * 
	 * @param priority
	 *            The priority at which the MissionSequencer's bound thread
	 *            executes.
	 * @param storage
	 *            The memory resources to be dedicated to execution of this
	 *            MissionSequencer's bound thread.
	 * @param name
	 *            The name by which this MissionSequencer will be identified.
	 * 
	 * @throws IllegalStateException
	 *             if invoked at an inappropriate time. The only appropriate
	 *             times for instantiation of a new MissionSequencer are (a)
	 *             during execution of Safelet.getSequencer by SCJ
	 *             infrastructure during startup of an SCJ application, and (b)
	 *             during execution of Mission.initialize by SCJ infrastructure
	 *             during initialization of a new mission in a Level 2
	 *             configuration of the SCJ run-time environment.
	 * 
	 *             Note that the static checker for SCJ forbids instantiation of
	 *             MissionSequencer objects outside of mission initialization,
	 *             but it does not prevent Mission.initialize in a Level 1
	 *             application from attempting to instantiate a
	 *             MissionSequencer.
	 * 
	 * 
	 * @note The constructor just sets the initial state.
	 * 
	 * @todo throwing of IllegalStateException
	 */
	@SCJAllowed
	@MemoryAreaEncloses(inner = { "this", "this", "this" }, outer = {
			"priority", "storage", "name" })
	@SCJRestricted(phase = INITIALIZATION)
	public MissionSequencer(PriorityParameters priority,
			StorageParameters storage, String name)
			throws IllegalStateException {

		// MS: just to make the system happy, but we don't want to
		// really extend the handler.... We want to run in the
		// plain Java thread!
		// in Level 1 we can simply ignore the priority
		super(priority, null, null, storage);

		this.storage = storage;

		// just an idle thread that watches the termination request
		// We should use the initial main thread to watch for termination...
		// new RtThread(0, 10000) {
		// public void run() {
		// while (!MissionSequencer.terminationRequest) {
		// System.out.println("seq");
		// waitForNextPeriod();
		// }
		// // Why do we need to call the cleanUp() method of the next
		// // mission after a termination request in the current mission?
		// //getNextMission().cleanUp();
		//
		// // Current mission cleanup method
		// current_mission.cleanUp();
		//
		// // MEH cleanUp method
		// cleanUp();
		//
		// cleanupDidRun = true;
		//
		// }
		//
		// };

		// final Runnable runner = new Runnable() {
		//
		// public void run() {
		// handleAsyncEvent();
		//
		// }
		// };
		//
		// new RtThread(0, 10000){
		//
		// public void run() {
		// while (!MissionSequencer.terminationRequest) {
		//
		// // This should be the mission memory
		// privMem.enter(runner);
		// block();
		// // waitForNextPeriod();
		// }
		// // Current mission cleanup method
		// current_mission.cleanUp();
		//
		// // MEH cleanUp method
		// cleanUp();
		// }
		// };

		// clean = new SwEvent(0, 100) {
		// public void handle() {
		// if (!cleanupDidRun && terminationRequest) {
		// cleanup();
		// }
		// }
		// };
	}

	/**
	 * Construct a MissionSequencer object to oversee a sequence of mission
	 * executions.
	 * 
	 * Memory behavior: This constructor requires that the "priority" argument
	 * reside in a scope that encloses the scope of the "this" argument. This
	 * constructor requires that the "storage" argument reside in a scope that
	 * encloses the scope of the "this" argument.
	 * 
	 * @param priority
	 *            The priority at which the MissionSequencer's bound thread
	 *            executes.
	 * @param storage
	 *            The memory resources to be dedicated to execution of this
	 *            MissionSequencer's bound thread.
	 * 
	 * @throws IllegalStateException
	 *             if invoked at an inappropriate time. The only appropriate
	 *             times for instantiation of a new MissionSequencer are (a)
	 *             during execution of Safelet.getSequencer by SCJ
	 *             infrastructure during startup of an SCJ application, and (b)
	 *             during execution of Mission.initialize by SCJ infrastructure
	 *             during initialization of a new mission in a Level 2
	 *             configuration of the SCJ run-time environment.
	 * 
	 *             Note that the static checker for SCJ forbids instantiation of
	 *             MissionSequencer objects outside of mission initialization,
	 *             but it does not prevent Mission.initialize in a Level 1
	 *             application from attempting to instantiate a
	 *             MissionSequencer.
	 * 
	 * 
	 * @note The constructor just sets the initial state.
	 * 
	 * @todo throwing of IllegalStateException
	 */
	@SCJAllowed
	@MemoryAreaEncloses(inner = { "this" }, outer = { "priority" })
	@SCJRestricted(phase = INITIALIZATION)
	public MissionSequencer(PriorityParameters priority,
			StorageParameters storage) throws IllegalStateException {
		this(priority, storage, "");
	}

	/**
	 * This method is called by infrastructure to select the initial mission to
	 * execute, and subsequently, each time one mission terminates, to determine
	 * the next mission to execute.
	 * 
	 * Prior to each invocation of getNextMission, infrastructure instantiates
	 * and enters the MissionMemory allocation area. The getNextMission method
	 * may allocate the returned mission within this newly instantiated
	 * MissionMemory allocation area, or it may return a reference to a Mission
	 * object that was allocated in some outer-nested MissionMemory area or in
	 * the ImmortalMemory area.
	 * 
	 * @return the next mission to run, or null if no further missions are to
	 *         run under the control of this MissionSequencer.
	 */
	@SCJAllowed(SUPPORT)
	protected abstract SpecificMission getNextMission();

	/**
	 * Initiate mission termination by invoking the currently running mission’s
	 * requestTermination method. Upon completion of the currently running
	 * mission, this MissionSequencer shall return from its handleAsyncEvent
	 * method without invoking getNextMission and without starting any
	 * additional missions. Its handleAsyncEvent method will not be invoked
	 * again.
	 * 
	 * Note that requestSequenceTermination does not force the sequence to
	 * terminate because the currently running mission must voluntarily
	 * relinquish its resources.
	 * 
	 * Control shall not return from requestSequenceTermination until after the
	 * requestTermination method for this mission sequencer’s currently running
	 * mission has been invoked and control has returned from that invocation.
	 * The running mission’s requestTermination method is invoked by the
	 * requestSequenceTermination method.
	 * 
	 * It is implementation-defined whether Mission.requestTermination has been
	 * called when requestSequenceTermination returns.
	 */
	@SCJAllowed(LEVEL_2)
	public final void requestSequenceTermination() {
		Mission.currentMission.requestTermination();
		terminationRequest = true;
	}

	/**
	 * 
	 * @return true if and only if the requestSequenceTermination method has
	 *         been invoked for this MissionSequencer object.
	 */
	@SCJAllowed(LEVEL_2)
	public final boolean sequenceTerminationPending() {
		return terminationRequest;
	}

	/**
	 * This method implements the mission life cycle for a L1 application.
	 * 
	 * @note Inherited because we extend MEH although we could use composition
	 */
	@Override
	@SCJAllowed(INFRASTRUCTURE)
	public final void handleAsyncEvent() {

		Mission m = getNextMission();

		if (m != null) {
			
			m.currentSequencer = this;
			currMission = m;
			myMission = m;

			// ! @todo Illegal reference when the mission object is allocated in
			// ! Immortal memory, e.g. with the Linear and Repeating sequencers
			Mission.setCurrentMission(m);
			m.terminationPending = false;

			Terminal.getTerminal().writeln("[SEQ]: Got new mission");
			ManagedMemory.setSize((int) m.missionMemorySize());
			m.initialize();
			
			if (!m.isCyclicExecutive) {
				executeMission(m);
			} else {
				executeCycle((CyclicExecutive) m);
			}

			m.terminate();
			m.cleanUp();

		} else {
			Terminal.getTerminal().writeln(
					"[SEQ]: No more missions to execute");
			terminationHelper.nextMission = false;
		}
	}

	/**
	 * @note Inherited because we extend MEH although we could use composition
	 */
	@SCJAllowed
	@SCJRestricted(phase = INITIALIZATION)
	public final void register() {
		// NOOP in Level 1
	}

	void executeMission(Mission m) {
		
		Terminal.getTerminal().writeln("[SEQ]: SCJ Start L1 mission on JOP");
		RtThread.startMission();

		/*
		 * Used to avoid leaving the MissionMemory. This is part of the main
		 * thread and it should block to avoid it being executed.
		 */
		while (!m.terminationPending) {
		}

	}

	void executeCycle(CyclicExecutive ce) {

		Terminal.getTerminal().writeln("[SEQ]: SCJ Start L0 mission on JOP");

		/*
		 * Upon return from initialize(), the infrastructure invokes the
		 * mission's getSchedule method in a Level 0 run-time environment. The
		 * infrastructure creates an array representing all of the
		 * ManagedSchedulable objects that were registered by the initialize
		 * method and passes this array as an argument to the mission's
		 * getSchedule method
		 */
		Vector v = ce.getHandlers();
		PeriodicEventHandler[] peHandlers = new PeriodicEventHandler[v.size()];
		v.copyInto(peHandlers);

		ce.phase = Mission.EXECUTION;
		CyclicSchedule schedule = ce.getSchedule(peHandlers);
		Frame[] frames = schedule.getFrames();

		/*
		 * The total size required can be the maximum of the backing store sizes
		 * needed for each handler's private memories.
		 */
		long maxScopeSize = 0;
		long maxBsSize = 0;
		for (int i = 0; i < frames.length; i++) {
			for (int j = 0; j < frames[i].handlers_.length; j++) {
				long k = frames[i].handlers_[j].getScopeSize();
				long l = frames[i].handlers_[j].storage
						.getTotalBackingStoreSize();

				maxScopeSize = (k > maxScopeSize) ? k : maxScopeSize;
				maxBsSize = (l > maxBsSize) ? l : maxBsSize;

			}
		}

		PrivateMemory handlerPrivMemory = new PrivateMemory((int) maxScopeSize,
				(int) maxBsSize);

		HandlerExecutor handlerExecutor = new HandlerExecutor();

		AbsoluteTime now = new AbsoluteTime();

		AbsoluteTime next = new AbsoluteTime();
		next = Clock.getRealtimeClock().getTime(next).add(1000, 0);

		while (!ce.terminationPending) {

			for (int i = 0; i < frames.length; i++) {

				while (Clock.getRealtimeClock().getTime(now).compareTo(next) < 0) {
					;
				}

				for (int j = 0; j < frames[i].handlers_.length; j++) {

					handlerExecutor.handler = frames[i].handlers_[j];

					/*
					 * Since no two PeriodicEventHandlers in a Level 0
					 * application are permitted to execute simultaneously, the
					 * backing store for the private memories may be reused. In
					 * order for this to be achieved, the implementation may
					 * revoke the backing store reservation for the private
					 * memory of a periodic event handler at the end of its
					 * release.
					 */
					handlerPrivMemory.enter(handlerExecutor);
				}

				next = next.add(frames[i].duration_, next);

				if (Clock.getRealtimeClock().getTime(now).compareTo(next) > 0) {
					/* Frame overrun */
					Terminal.getTerminal().writeln("[SEQ]: Frame overrun");
				}
			}
		}

	}

	class HandlerExecutor implements Runnable {

		PeriodicEventHandler handler;

		@Override
		public void run() {
			handler.handleAsyncEvent();
		}

	}
}
