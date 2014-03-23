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

import test.cyclic.ImmortalEntry;

import com.jopdesign.io.IOFactory;
import com.jopdesign.io.LedSwitch;
import com.jopdesign.io.LedSwitchFactory;
import com.jopdesign.io.SysDevice;

public class TestPEH extends PeriodicEventHandler {

	int fireCount = 0;
	int number;
	AperiodicEventHandler aeh;
	AperiodicLongEventHandler aleh;
	

	Random rnd = new Random();

	public TestPEH(PriorityParameters priority, PeriodicParameters release,
			StorageParameters storage, String name, AperiodicEventHandler aeh,
			AperiodicLongEventHandler aleh, int number) {
		super(priority, release, storage, name);
		this.aeh = aeh;
		this.aleh = aleh;
		this.number = number;
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

//		if (rnd.nextInt(3) == 1) {
		System.out.println(getName()+ "fire aeh's");
			aeh.release();
			aleh.release(number);
			fireCount++;
//		}
			
			if (number == 1) {
				int foo = 0;
				Terminal.getTerminal().writeln("** Long computation **");
				for(int i = 0; i < 10000000; i++)
					foo++;
			}

		// Only PEH 0 can finish the mission
		if (fireCount > 2 & number == 0) {
			int dummy = 0;
			Terminal.getTerminal().writeln("------> Requesting mission termination");
			Mission.getCurrentMission().requestTermination();
			Terminal.getTerminal().writeln("** Doing a long final computation **");
			for(int i = 0; i < 10000000; i++)
				dummy++;
		}

	}

}
