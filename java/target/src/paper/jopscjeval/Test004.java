package jopscjeval;

import java.util.Random;

import javax.realtime.AbsoluteTime;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeClock;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.Terminal;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.io.SysDevice;
import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;

/**
 * Interrupt dispatch latency test
 * 
 * @author jrri
 * 
 */
public class Test004 extends Mission {

	SysDevice sys;
	Terminal term;

	int N = 5;

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {

		sys = ImmortalEntry.sys;
		term = ImmortalEntry.term;

		Random rnd = new Random();

		// final AbsoluteTime start = new AbsoluteTime();
		final int winner = rnd.nextInt(N);

		int MAX_PRIO = PriorityScheduler.instance().getMaxPriority();

		final SharedData shared = new SharedData(ImmortalEntry.relLimit);

		term.writeln("---- ISR will be triggered from PEH: " + winner);

		/* The managed ISR */
		new GenericISR(512, 256) {

			@Override
			@SCJAllowed(Level.LEVEL_1)
			protected synchronized void handle() {

				/* Using the jop's microsecond counter */
				int now = Native.rd(Const.IO_US_CNT);
				shared.endTimes[ImmortalEntry.relNo] = (long) now;

				/* A nested interrupt */
				// Native.wr(1, Const.IO_INT_ENA);
				// ImmortalEntry.sys.intNr = 1;

				/* Using SCJ's time API */
				// AbsoluteTime end = ImmortalEntry.rtc.getTime();
				// shared.end[ImmortalEntry.relNo].set(end.getMilliseconds(),
				// end.getNanoseconds());

				System.out.println("Finished isr 2");

			}

		}.register(2);

		/* The managed ISR */
		new GenericISR(512, 256) {

			@Override
			@SCJAllowed(Level.LEVEL_1)
			protected synchronized void handle() {

				System.out.println("Nested Interrupt!");

			}
		}.register(1);

		/* Terminator thread */
		new GenericPEH(MAX_PRIO, 100000, 0, 80000, 0, 1024, 512, "T") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {
				Mission.getCurrentMission().requestTermination();
			}

			@Override
			@SCJAllowed(Level.SUPPORT)
			@SCJRestricted(phase = Phase.CLEANUP)
			public void cleanUp() {

				Utils.statistics(shared.startTimes, shared.endTimes);

				// /* Using the jop's microsecond counter */
				// long value = 0;
				//
				// /* Using SCJ's time API */
				// // RelativeTime value;
				//
				// for (int i = 0; i < limit; i++) {
				//
				// /* Using the jop's microsecond counter */
				// value = shared.endTimes[i] - shared.startTimes[i];
				// term.writeln(Long.toString(value));
				//
				// /* Using SCJ's time API */
				// // value = shared.end[i].subtract(shared.start[i]);
				// // term.writeln(value.toString());
				//
				// }

				ImmortalEntry.relNo = -1;

			}

		}.register();

		for (int i = 0; i < N; i++) {
			new InterruptTrigger(20 + i, 150, 0, 512, 256, "", i, winner,
					shared).register();
		}

		// int i = 1;
		// final int idd = i;
		// new GenericPEH(MAX_PRIO - (i + 1), 1000, 0, 512, 256, null) {
		//
		// int id = idd;
		//
		// // AbsoluteTime start;
		//
		// @Override
		// @SCJAllowed
		// public void handleAsyncEvent() {
		//
		// /*
		// * Only the "winner" thread fires the interrupt, the others do a
		// * busy loop
		// */
		// if (id == winner) {
		// fireInterrupt();
		// } else {
		// int delay = 20000;
		// for (int i = 0; i < delay; i++)
		// ;
		// }
		//
		// }
		//
		// void fireInterrupt() {
		//
		// // AbsoluteTime now = ImmortalEntry.rtc.getTime();
		// // start.set(now.getMilliseconds(), now.getNanoseconds());
		//
		// shared.startTimes[ImmortalEntry.relNo] = Native.rd(Const.IO_US_CNT);
		// sys.intNr = 1;
		//
		// }
		//
		// }.register();
		// }

	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		return 1024 * N + 16000;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void cleanUp() {

	}

}
