package javax.safetycritical;

import javax.safetycritical.annotate.MemoryAreaEncloses;
import javax.safetycritical.annotate.SCJAllowed;

import static javax.safetycritical.annotate.Level.SUPPORT;

/**
 * A CyclicExecutive represents a Level 0 mission. Every mission in a Level 0
 * application must be a subclass of CyclicExecutive.
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 * 
 */
@SCJAllowed
public abstract class CyclicExecutive extends Mission {
	
	public int frameOverrun = 0;

	/**
	 * Construct a CyclicExecutive object.
	 */
	@SCJAllowed
	@MemoryAreaEncloses(inner = { "this" }, outer = { "sequencer" })
	public CyclicExecutive() {
	}

	/**
	 * Every CyclicExecutive shall provide its own cyclic schedule, which is
	 * represented by an instance of the CyclicSchedule class. Application
	 * programmers are expected to override the getSchedule method to provide a
	 * schedule that is appropriate for the mission.
	 * 
	 * Level 0 infrastructure code invokes the getSchedule method on the mission
	 * returned from MissionSequencer.getNextMission after invoking the
	 * mission’s initialize method in order to obtain the desired cyclic
	 * schedule. Upon entry into the getSchedule method, this mission’s
	 * MissionMemory area shall be the active allocation context. The value
	 * returned from getSchedule must reside in the current mission’s
	 * MissionMemory area or in some enclosing scope.
	 * 
	 * Infrastructure code shall check that all of the PeriodicEventHandler
	 * objects referenced from within the returned CyclicSchedule object have
	 * been registered for execution with this Mission. If not, the
	 * infrastructure shall immediately terminate execution of this mission
	 * without executing any event handlers.
	 * 
	 * Memory behavior: This constructor requires that the "handlers" argument
	 * reside in a scope that encloses the scope of the "this" argument.
	 * 
	 * @param handlers
	 *            represents all of the handlers that have been registered with
	 *            this Mission. The entries in the handlers array are sorted in
	 *            the same order in which they were registered by the
	 *            corresponding CyclicExecutive’s initialize method. The
	 *            infrastructure shall copy the information in the handlers
	 *            array into its private memory, so subsequent application
	 *            changes to the handlers array will have no effect.
	 * 
	 * @return the schedule to be used by the CyclicExecutive.
	 */
	@SCJAllowed(SUPPORT)
	@MemoryAreaEncloses(inner = { "this" }, outer = { "handlers" })
	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
		return null;
	}

}
