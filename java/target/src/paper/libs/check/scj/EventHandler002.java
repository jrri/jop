package libs.check.scj;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.SCJAllowed;

public class EventHandler002 extends PeriodicEventHandler{

	public EventHandler002(PriorityParameters priority,
			PeriodicParameters release, StorageParameters storage,
			long scopeSize, String name) {
		super(priority, release, storage, scopeSize, name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@SCJAllowed
	public void handleAsyncEvent() {
		
		System.out.println(getName());
		
	}

}
