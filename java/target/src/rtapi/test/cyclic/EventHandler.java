package test.cyclic;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

public class EventHandler extends PeriodicEventHandler {

	int count = 0;

	public EventHandler(PriorityParameters priority,
			PeriodicParameters release, StorageParameters scp, long scopeSize,
			String name) {
		super(priority, release, scp, scopeSize, name);
	}

	@Override
	public void handleAsyncEvent() {

		ImmortalEntry.term.writeln(getName());
		
		count++;
		if (count == 2) {
			System.out.println("Termination requested...");
			Mission.getCurrentMission().requestTermination();
		}
		Terminal.getTerminal().writeln("exec term");

	}
	
	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.CLEANUP)
	public void cleanUp() {
		Terminal.getTerminal().writeln("Handler " + getName() + " cleanup...");
	}

}
