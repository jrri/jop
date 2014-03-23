package jopscjeval;

import javax.realtime.AbsoluteTime;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeClock;
import javax.realtime.RelativeTime;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.Services;
//import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

public class Test003 extends Mission {

	static int rel;
	static int serv;
	static int DLY = 40000;

	int N = 5;

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {

		final AbsoluteTime start = new AbsoluteTime();

		int MAX_PRIO = PriorityScheduler.instance().getMaxPriority();
		int MIN_PRIO = PriorityScheduler.instance().getMinPriority();
		int AEH_PRIO = MIN_PRIO + 14;

		DLY = DLY + 1;
		System.out.println("Delay: " + DLY);

		final AperiodicEventHandler tested = new GenericAEH(AEH_PRIO, 1024,
				512, "tested") {

			private int releases = 0;
			int limit = 10;
			
			int delay_ms = 2147;
			int delay_ns = delay_ms*1000*1000;
			
			RelativeTime inter = new RelativeTime();

			@Override
			public void handleAsyncEvent() {
				AbsoluteTime now = RealtimeClock.getRealtimeClock().getTime();
				serv++;
				releases++;

				now.subtract(start, inter);
				System.out.println(inter);

				/* Modify aperiodic execution time */
				Services.nanoSpin(delay_ns);

				if (!(releases <= limit))
					Mission.getCurrentMission().requestTermination();

			}

		};
		 tested.register();

		GenericPEH starter = new GenericPEH(MAX_PRIO, 100, 0, 2048, 1024,
				"tester") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {
				
				/*
				 * The whole block of code below should execute without
				 * interruption on every PEH release as it is the highest
				 * priority task
				 */
				AbsoluteTime now = RealtimeClock.getRealtimeClock().getTime();
				tested.release();
				start.set(now.getMilliseconds(), now.getNanoseconds());
				rel++;

			}

		};

		 starter.register();

		/* Low priority tasks */
		// TODO The for loop does not work for the next release of the MISSION!
		int i = 1;
//		for (int i = 0; i < N; i++) {
			new Worker(MIN_PRIO+i, 2000, 0, 1024, 512, 100, "PEH_LL")
					.register();
			
			new Worker(MIN_PRIO+(i+1), 2000, 0, 1024, 512, 100, "PEH_LH")
			.register();

//		}

		/* High priority tasks */
		// TODO The for loop does not work for the next release of the MISSION!
//		for (int i = 0; i < N; i++) {
			new Worker(AEH_PRIO + (i + 1), 1000, 0, 1024, 512, 100, "PEH_HH")
					.register();
			
			new Worker(AEH_PRIO + (i + 2), 1000, 0, 1024, 512, 100, "PEH_HL")
			.register();

//		}

	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		return 1024 * N + 2048;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void cleanUp() {
		// releases = 0;
		System.out.println(" Releases: " + rel);
		System.out.println(" Served: " + serv);

		rel = 0;
		serv = 0;

		// ;

		// Printer print = new Printer();
		//
		// RelativeTime s = new RelativeTime();
		// RelativeTime e = new RelativeTime();
		//
		// for (int i = 0; i < limit; i++) {
		// print.interval = e.subtract(s);
		// ManagedMemory.enterPrivateMemory(512, print);
		// }

	}

	// private class Printer implements Runnable {
	//
	// RelativeTime interval;
	//
	// @Override
	// public void run() {
	//
	// System.out.println(interval.getMilliseconds() + ":"
	// + interval.getNanoseconds());
	//
	// }
	//
	// }

}
