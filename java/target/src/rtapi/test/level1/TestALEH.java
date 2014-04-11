package test.level1;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;

public class TestALEH extends AperiodicLongEventHandler{

	public TestALEH(PriorityParameters priority, AperiodicParameters release,
			StorageParameters storage, String name) {
		super(priority, release, storage, name);
	}

	@Override
	public void handleAsyncLongEvent(long data) {
		Terminal.getTerminal().writeln(getName()+data);
	}
	
	@Override
	public void cleanUp() {
		// Just a debug message
		System.out.println("Cleanup "+ getName());
		
	}

}
