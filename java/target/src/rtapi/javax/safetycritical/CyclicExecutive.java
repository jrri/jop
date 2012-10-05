package javax.safetycritical;

import javax.realtime.PriorityParameters;

import javax.safetycritical.annotate.MemoryAreaEncloses;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;

/**
 * TBD: An earlier version of CyclicExecutive extended Mission. In the current
 * design, CyclicExecutive produces a MissionSequencer which has the ability to
 * run a sequence of Missions. There's been some back and forth on this. Many of
 * our earlier design choices were based on the assumption that a Level0 Safelet
 * consists of only one Mission, but we subsequently reversed that choice
 * without fixing the relevant libraries. I understand a fundamental desire that
 * "simple things be simple". But there's some question in my mind as to what is
 * simple. The current draft document pursues option 2.
 * 
 * Option 1: CyclicExecutive extends Level0Mission and implements Safelet, with
 * the following consequences:
 * 
 * a. The application developer extends CyclicExecutive b. We need a variant of
 * CyclicExecutive that doesn't extend Level0Mission, because some Level0
 * applications are going to be sequences of Missions rather than a single
 * mission. c. CyclicExecutive can define a default getSequencer method which
 * returns a SingleMissionSequencer with a "normal" priority and a
 * "reasonably conservative" StorageParameters object, with the single mission
 * represented by "this" CyclicExecutive. d. The user overrides the initialize()
 * and getSchedule() methods, and optionally, the cleanup method. e. I don't
 * like the name CyclicExecutive for this. I'd rather call it CyclicApplication
 * as it is both a Safelet and a Mission.
 * 
 * Option 2: Cyclet is a concrete class that implements Safelet, but does not
 * extend Level0Mission, with the following consequences:
 * 
 * a. Configuraton of the SCJ run-time specifies both the name of the Cyclet
 * subclass (or Cyclet itself) and an optional name of the primordial mission.
 * Infrastructure invokes in sequence the setUp(), getSequencer(),
 * "sequencer.run()", and tearDown(). b. The default implementation of
 * getSequencer returns a SingleMissionSequencer with a normal priority and a
 * reasonably conservative StorageParameters object, representing the single
 * mission that is obtained by invoking the static method of Cyclet that is
 * declared as:
 * 
 * public static Level0Mission getPrimordialMission();
 * 
 * The vendor is required to implement this method in a vendor-specific way. It
 * could, for example, obtain this mission from a command-line argument, or from
 * a configuration choice specified at build time. c. The application developer
 * extends Level0Mission and overrides the initialize() and getSchedule()
 * methods.
 */

@SCJAllowed
public abstract class CyclicExecutive extends Mission {
	/**
	 * Constructor for a Cyclic Executive. Level 0 Applications need to extend
	 * CyclicExecutive and define a getSchedule() method. Level 1 and Level 2
	 * applications should not extend CyclicExecutive, but rather should
	 * implement Safelet more directly.
	 * 
	 * @param storage
	 */


//	@SCJAllowed
//	@MemoryAreaEncloses(inner = { "this" }, outer = { "sequencer" })
//	public CyclicExecutive(MissionSequencer<CyclicExecutive> sequencer) {
//	}

	// jrri: According to v.0.90 of spec, the constructor has no arguments
	@SCJAllowed
	@MemoryAreaEncloses(inner = { "this" }, outer = { "sequencer" })
	public CyclicExecutive() {
	}

	/**
	 * @return the schedule to be used by for the CyclicExecutive The cyclic
	 *         schedule is typically generated by vendor-specific tools. The
	 *         returned object is expected to reside within the MissionMemory of
	 *         the CyclicExecutive.
	 */
	@SCJAllowed(SUPPORT)
	@MemoryAreaEncloses(inner = { "this" }, outer = { "handlers" })
	public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
		return null;
	}
	
	protected void startCycle(){ 
		
		
		
	}
}
