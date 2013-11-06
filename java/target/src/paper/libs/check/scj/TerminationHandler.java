package libs.check.scj;

import javax.realtime.AperiodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;

public class TerminationHandler extends AperiodicEventHandler {

	public TerminationHandler(PriorityParameters priority,
			AperiodicParameters release, StorageParameters storage,
			long scopeSize, String name) {
		super(priority, release, storage, scopeSize, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleAsyncEvent() {
		
		Mission.getCurrentMission().requestTermination();
		
	}

}
