package test.level1;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.Services;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

public class DebugPEH extends PeriodicEventHandler{

	public DebugPEH(PriorityParameters priority, PeriodicParameters release,
			StorageParameters storage, String name) {
		super(priority, release, storage, name);
	}
	
	@Override
	@SCJAllowed
	public void handleAsyncEvent() {
		
		System.out.println(getName());
		
		Mission.getCurrentMission().requestTermination();
		
	}
	
	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.CLEANUP)
	public void cleanUp() {
		
		ManagedMemory.enterPrivateMemory(256, new Runnable() {
			
			@Override
			public void run() {
				
				System.out.println("cleanup in nested");
				
			}
		});
		
	}

	@Override
	public void executeMissHandler() {
		// TODO Auto-generated method stub
		
	}

}
