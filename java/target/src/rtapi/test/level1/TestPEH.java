package test.level1;

import java.util.Random;

import javax.realtime.AffinitySet;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.Schedulable;
import javax.realtime.Scheduler;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.ManagedEventHandler;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.PrivateMemory;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;
import javax.safetycritical.Services;

public class TestPEH extends PeriodicEventHandler {

	int fireCount = 0;
	AperiodicEventHandler aeh;
	AperiodicLongEventHandler aleh;

	Random rnd = new Random();

	public TestPEH(PriorityParameters priority, PeriodicParameters release,
			StorageParameters storage, long scopeSize, String name, AperiodicEventHandler aeh,
			AperiodicLongEventHandler aleh) {
		super(priority, release, storage, scopeSize, name);
		this.aeh = aeh;
		this.aleh = aleh;
	}

	@Override
	public void handleAsyncEvent() {
		
		/* One way to get the currently executing schedulable object */
		Schedulable s = Scheduler.getCurrentSO();
		ManagedEventHandler meh = (ManagedEventHandler) s;
		
		Terminal.getTerminal().writeln(meh.getName()+"Hello!");
//		Terminal.getTerminal().writeln(getName()+"Hello!");
		
		PrivateMemory.enterPrivateMemory(256, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Terminal.getTerminal().writeln(getName()+"Hello from nested!");
			}
		});

		if (rnd.nextInt(3) == 1) {
			aeh.release();
			aleh.release(666);
			fireCount++;
		}

		if (fireCount > 2) {
			Mission.getCurrentMission().requestTermination();
		}

	}

}
