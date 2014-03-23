package test.level1;

import javax.realtime.AffinitySet;
import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;

public class TestALEH extends AperiodicLongEventHandler{

	public TestALEH(PriorityParameters priority, AperiodicParameters release,
			StorageParameters storage, String name) {
		super(priority, release, storage, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleAsyncEvent(long data) {

		Terminal.getTerminal().writeln(getName()+data);
//		System.out.println(data);
		
	}

}
