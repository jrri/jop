package test.level1;

import java.util.Random;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.PrivateMemory;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;

import com.jopdesign.sys.Memory;

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

		Terminal.getTerminal().writeln(getName()+"Hello!");
//		Object object = new Object();
//		Memory m = Memory.getMemoryArea(object);
//		Terminal.getTerminal().writeln("Level: "+m.level);
		
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
