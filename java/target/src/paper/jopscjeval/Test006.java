package jopscjeval;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.RealtimeClock;
import javax.safetycritical.Mission;
import javax.safetycritical.Services;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

/**
 * This class is used to test synchronization while accessing shared resources.
 * Three periodic event handlers (PEH), with high, medium, and low priorities.
 * The low priority PEH is released at an offset zero, the medium priority PEH
 * is released at an offset OFF_MED, and the high priority PEH is released at an
 * offset off OFF_HIG, where OFF_MED < OFF_HIG. The low priority PEH immediately
 * after being released locks a resource shared with the high priority PEH thus
 * potentially causing a priority inversion.
 * 
 * A proper priority inversion avoidance protocol should minimize the response
 * time of the high priority PEH.
 * 
 * @author Juan Rios
 * 
 */
public class Test006 extends Mission {

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {

		final SharedObject shared = new SharedObject();

		final int PRIO_HIG = 20;
		final int PRIO_MED = 19;
		final int PRIO_LOW = 18;

		final int OFF_HIG = 400;
		final int OFF_MED = 300;
		final int OFF_LOW = 0;

		final int WORK_HIG = 200;
		final int WORK_MED = 400;
		final int WORK_LOW = 100;

		final int SHARED_HIG = 100;
		final int SHARED_MED = 0;
		final int SHARED_LOW = 500;

		final Clock rtc = RealtimeClock.getRealtimeClock();

		final AbsoluteTime hi_start = new AbsoluteTime();
		final AbsoluteTime hi_end = new AbsoluteTime();

		final AbsoluteTime me_start = new AbsoluteTime();
		final AbsoluteTime me_end = new AbsoluteTime();

		final AbsoluteTime lo_start = new AbsoluteTime();
		final AbsoluteTime lo_end = new AbsoluteTime();

		/* High priority PEH */
		new GenericPEH(PRIO_HIG, 3800, 0, OFF_HIG, 0, 1024, 512, "H") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {
				rtc.getTime(hi_start);
				
				/* Attempt to lock shared resource */
				shared.work(SHARED_HIG);

				/* Simulate work */
				// Services.nanoSpin(WORK_HIG * 1000000);
				for (int j = 0; j < WORK_HIG; j++)
					for (int i = 0; i < 3748; i++)
						; // ~ 1 ms

				

				rtc.getTime(hi_end);
			}

		}.register();

		/* Medium priority PEH */
		new GenericPEH(PRIO_MED, 4000, 0, OFF_MED, 0, 1024, 512, "M") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

				rtc.getTime(me_start);

				/* Simulate work and do not attempt to lock shared resources */
				for (int j = 0; j < WORK_MED; j++)
					for (int i = 0; i < 3748; i++)
						; // ~ 1 ms

				// Services.nanoSpin(WORK_MED * 1000000);
				rtc.getTime(me_end);

			}

		}.register();

		/* Low priority PEH */
		new GenericPEH(PRIO_LOW, 3800, 0, OFF_LOW, 0, 1024, 512, "L") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {
				rtc.getTime(lo_start);

				/* Simulate work */
				// Services.nanoSpin(WORK_LOW * 1000000);

				/* Attempt to lock shared resource */
				shared.work(SHARED_LOW);
				
				/* Simulate work */
				for (int j = 0; j < WORK_LOW; j++)
					for (int i = 0; i < 3748; i++)
						; // ~ 1 ms

				rtc.getTime(lo_end);
			}

		}.register();

		/* Terminator thread */
		new GenericPEH(10, 2000, 0, 2000, 0, 1024, 512, "T") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

				Mission.getCurrentMission().requestTermination();

			}

			@Override
			@SCJAllowed(Level.SUPPORT)
			@SCJRestricted(phase = Phase.CLEANUP)
			public void cleanUp() {

				AbsoluteTime st_miss = RtThreadImpl.getStartMissionTime();

				AbsoluteTime st_h = st_miss.add(OFF_HIG, 0);
				AbsoluteTime st_m = st_miss.add(OFF_MED, 0);
				AbsoluteTime st_l = st_miss.add(OFF_LOW, 0);

				System.out.println("Hi jitter: " + hi_start.subtract(st_miss));
				System.out.println("Hi exec: " + hi_end.subtract(hi_start));
				System.out.println("Hi resp: " + hi_end.subtract(st_h));

				System.out.println("Me jitter: " + me_start.subtract(st_miss));
				System.out.println("Me exec: " + me_end.subtract(me_start));
				System.out.println("Me resp: " + me_end.subtract(st_m));

				System.out.println("Lo jitter: " + lo_start.subtract(st_miss));
				System.out.println("Lo exec: " + lo_end.subtract(lo_start));
				System.out.println("Lo resp: " + lo_end.subtract(st_l));

			}

		}.register();

	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		return 4 * 2048;
	}

	class SharedObject {

		/**
		 * Simulate work by doing spin-wait.
		 * 
		 * @param delayMs
		 *            Time in milliseconds to simulate work. The time parameter
		 *            has to be less than 2,147 ms, otherwise the value
		 *            overflows creating a negative value. Negative time values
		 *            make the method to return immediately.
		 */
		public synchronized void work(int delayMs) {

			for (int j = 0; j < delayMs; j++)
				for (int i = 0; i < 3748; i++)
					; // ~ 1 ms
			// Services.nanoSpin(time * 1000000);

		}

	}

}
