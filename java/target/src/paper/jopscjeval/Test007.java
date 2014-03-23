package jopscjeval;

import javax.realtime.AbsoluteTime;
import javax.realtime.PriorityScheduler;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;

public class Test007 extends Mission {

	// long thh;
	long tll;

	double sum = 0;
	double sumSq = 0;
	double N = 0;

	long max = Long.MIN_VALUE;
	long min = Long.MAX_VALUE;

	boolean finished = false;

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {

		final AbsoluteTime th = new AbsoluteTime();
		final AbsoluteTime tl = new AbsoluteTime();

		final int MAX_PRIO = PriorityScheduler.instance().getMaxPriority();
		final int MIN_PRIO = PriorityScheduler.instance().getMinPriority();

		new GenericPEH(MAX_PRIO - 1, 20, 0, 10, 0, 512, 512, "H") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

				int now = Native.rd(Const.IO_US_CNT);
				// thh = now;

				// ImmortalEntry.rtc.getTime(th);

				// System.out.println(th.subtract(tl));
				// System.out.println(thh-tll);

				long data = (now - tll);
				sum += data;
				sumSq += data * data;
				N++;

				if (data > max)
					max = data;

				if (data < min)
					min = data;

			}

		}.register();

		new GenericPEH(MIN_PRIO, 10000, 0, 512, 512, "L") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

				int now;

				for (;;) {
					now = Native.rd(Const.IO_US_CNT);
					tll = now;
					// ImmortalEntry.rtc.getTime(tl);

					if (finished)
						break;
				}

			}

		}.register();

		new GenericPEH(MAX_PRIO, 10, 0, 20000, 0, 1024, 1024, "T") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

				double avg = sum / N;
				double a = sumSq - N * avg * avg;

				double stdev = Math.sqrt(a / (N - 1));

				System.out.println("avg: " + avg);
				System.out.println("stdev: " + stdev);
				System.out.println("min: " + min);
				System.out.println("max: " + max);
				
				System.out.println("N: " + N);

				finished = true;

				Mission.getCurrentMission().requestTermination();

			}

		}.register();

		/* 1 to 27 or 0 to 26 */
		for (int i = 1; i < 27; i++)
			new GenericPEH(MIN_PRIO + i, 10, 0, 20000, 0, 1024, 1024, "O") {

				@Override
				@SCJAllowed
				public void handleAsyncEvent() {

				}

			}.register();

	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		// TODO Auto-generated method stub
		return 28 * 1024;
	}

}
