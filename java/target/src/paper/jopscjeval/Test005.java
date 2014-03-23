package jopscjeval;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeClock;
import javax.realtime.RelativeTime;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.AperiodicLongEventHandler;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.Services;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

/**
 * Aperiodic tests 
 * 
 * @author jrri
 *
 */
public class Test005 extends Mission {

	private static final long TEST_LENGTH_MS = 50000;
	/* Missions are created in immortal memory, so all the fields below are in immortal memory */
	private static int rel = 0;
	private static int serv = 0;

	private int NUM_HI_PEH = 4;
	private int NUM_ALEH = 1;
	
	private int MAX_PRIO = PriorityScheduler.instance().getMaxPriority();
	private int MIN_PRIO = PriorityScheduler.instance().getMinPriority();
	
	int TERMINATOR_PRIO = MAX_PRIO;
	int STARTER_PRIO = MAX_PRIO - 1;

	private int PEH_TOP_PRIO = STARTER_PRIO - NUM_ALEH - 1;
	private int ALEH_BASE_PRIO = PEH_TOP_PRIO - NUM_HI_PEH;
	
	int[] relTimes;

	// long[] rels;
	// long[] servs;

	AbsoluteTime start = new AbsoluteTime();

	//TODO illegal array references 
	final AperiodicWorker[] aleh = new AperiodicWorker[NUM_ALEH];
	

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {
		
		message();

		final Clock rtc = RealtimeClock.getRealtimeClock();

		relTimes = genReleaseTimes200();

		/* Create all the ALEH's that will be used */
		for (int i = 0; i < aleh.length; i++) {
			aleh[i] = new AperiodicWorker(ALEH_BASE_PRIO+i, 1024, 512, "ALEH_" + i,
					relTimes.length);
			aleh[i].register();
		}

		// rels = new long[relTimes.length];
		// servs = new long[relTimes.length];

		// final AperiodicLongEventHandler aleh = new GenericALEH(MAX_PRIO,
		// 1024,
		// 512, "tested") {
		//
		// @Override
		// @SCJAllowed
		// public void handleAsyncEvent(long data) {
		//
		// for (int j = 0; j < 15; j++)
		// for (int i = 0; i < 3749; i++)
		// ;
		//
		// AbsoluteTime end = rtc.getTime();
		// servs[(int) data] = end.getMilliseconds();
		// serv++;
		//
		// }
		//
		// };
		// aleh.register();

		/* Starter thread. This thread fires the ALEHs. It runs at the second highest priority */
		GenericPEH starter = new GenericPEH(STARTER_PRIO, 10, 0, 4048, 1024, "starter") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

				//int a,b;
				//a = Native.rd(Const.IO_US_CNT);
				
				AbsoluteTime now = rtc.getTime();
				AbsoluteTime stm = RtThreadImpl.getStartMissionTime();
				AperiodicWorker aw = aleh[rel % NUM_ALEH];

				if (nextReleaseTime(rel) <= now.subtract(stm).getMilliseconds()) {
					aw.rels[rel] = now.getMilliseconds();
					rel++;
					aw.rel++;
					aw.release(rel - 1);

					// rels[rel] = now.getMilliseconds();
					// rel++;
					// aleh.release(rel - 1);
				}
				
				//b = Native.rd(Const.IO_US_CNT);
				//long d = (long)b - (long)a;
				//System.out.println(d);

			}

		};

		starter.register();

		/* Terminator thread */
		new GenericPEH(TERMINATOR_PRIO, 100000, 0, TEST_LENGTH_MS, 0, 1024, 512, "T") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {
				Mission.getCurrentMission().requestTermination();
			}

		}.register();

		periodicLoad88();
	}

	private void message() {
		
		ImmortalEntry.term.writeln();
		ImmortalEntry.term.writeln("**************************************");
		ImmortalEntry.term.writeln();
		ImmortalEntry.term.writeln("Terminator priority: "+TERMINATOR_PRIO);
		ImmortalEntry.term.writeln("Starter pririty    : "+STARTER_PRIO);
		ImmortalEntry.term.writeln("PEH top priority   : " +PEH_TOP_PRIO);
		ImmortalEntry.term.writeln("ALEH base priority : "+ ALEH_BASE_PRIO);
		ImmortalEntry.term.writeln();
		ImmortalEntry.term.writeln("The test will run for "+TEST_LENGTH_MS+" ms");
		ImmortalEntry.term.writeln();
		ImmortalEntry.term.writeln("**************************************");
		ImmortalEntry.term.writeln();
	}

	/**
	 * Generate release times for a Poisson distribution with an average minimum
	 * inter-arrival rate of 50 ms for an interval of 5 s.
	 * 
	 * TODO
	 * 
	 * @return
	 */

	int[] genReleaseTimes50() {

		int[] timesA = { 29, 47, 90, 99, 114, 165, 205, 252, 265, 280, 323,
				341, 344, 356, 373, 484, 531, 557, 596, 746, 819, 829, 1036,
				1044, 1171, 1191, 1226, 1302, 1330, 1435, 1455, 1481, 1625,
				1769, 1863, 2059, 2101, 2110, 2134, 2167, 2174, 2290, 2295,
				2407, 2440, 2537, 2566, 2835, 2848, 2857, 2861, 2862, 2896,
				2961, 3076, 3110, 3136, 3150, 3274, 3295, 3328, 3416, 3419,
				3446, 3487, 3490, 3511, 3551, 3559, 3591, 3620, 3640, 3690,
				3761, 3789, 3796, 3841, 3950, 3990, 4051, 4096, 4105, 4151,
				4198, 4249, 4347, 4414, 4537, 4579, 4647, 4707, 4750, 4856,
				4892, 4909, 4980, 4992, 5122, };

		return timesA;
	}

	/**
	 * Generate release times for a Poisson distribution with an average minimum
	 * inter-arrival rate of 100 ms for an interval of 5 s.
	 * 
	 * TODO
	 * 
	 * @return
	 */
	int[] genReleaseTimes100() {

		int[] timesA = { 90, 219, 252, 378, 389, 408, 502, 572, 609, 627, 676,
				732, 844, 922, 956, 968, 1001, 1399, 1438, 1521, 1604, 1818,
				1839, 1951, 2091, 2198, 2296, 2357, 2414, 2507, 2599, 2665,
				2707, 2712, 2745, 2836, 2855, 3055, 3336, 3583, 3764, 3877,
				3997, 4442, 4503, 4738, 4930, 4976, 4992, 4994, 5050, };

		return timesA;

	}

	/**
	 * Generate release times for a Poisson distribution with an average minimum
	 * inter-arrival rate of 200 ms for an interval of 50 s.
	 * 
	 * @return
	 */

	int[] genReleaseTimes200() {

		int[] timesA = { 310, 432, 502, 511, 674, 1166, 1737, 1830, 1876, 1949,
				2162, 2173, 2304, 2313, 2835, 3150, 3201, 3219, 3268, 3512,
				3889, 3922, 3970, 4231, 4527, 4755, 4792, 4831, 4943, 5055,
				5305, 5377, 5423, 5586, 5747, 5900, 6156, 6234, 6254, 6273,
				6332, 6601, 6675, 7080, 7499, 7830, 8215, 8322, 8845, 8884,
				8949, 8964, 9106, 9190, 9214, 9337, 9590, 9595, 10258, 10482,
				10487, 10689, 10924, 11346, 11364, 11764, 11984, 12006, 12144,
				12242, 12349, 12421, 13127, 13255, 13943, 13981, 14197, 14230,
				14510, 14619, 14632, 15240, 15824, 16600, 16677, 16779, 17213,
				17259, 17355, 17887, 18421, 18820, 18867, 19343, 19631, 19913,
				20364, 20395, 20466, 20528, 20614, 20747, 20970, 21053, 21481,
				21864, 22648, 22656, 22662, 23079, 23231, 23316, 23563, 23619,
				23736, 23906, 24170, 24226, 24248, 24311, 24491, 24504, 24777,
				24902, 24912, 25175, 25452, 25467, 26003, 26245, 26350, 26668,
				26759, 26804, 26942, 27028, 27073, 27364, 27466, 27903, 28036,
				28071, 28088, 28227, 28483, 28569, 28586, 28721, 28726, 29050,
				29490, 29732, 29917, 30090, 30324, 30397, 30874, 31057, 31301,
				31537, 31987, 32091, 32344, 32716, 34181, 34433, 34553, 34580,
				35213, 35233, 35640, 35676, 35721, 35738, 36135, 36272, 36452,
				36803, 36913, 37013, 37321, 37452, 37454, 37597, 37670, 37847,
				38519, 38765, 38809, 39021, 39518, 39652, 39853, 39913, 40042,
				40086, 40126, 40459, 40877, 40916, 41006, 41832, 41854, 41986,
				42108, 42208, 42263, 42294, 42486, 42980, 43042, 43262, 43297,
				43495, 43533, 43880, 44288, 44314, 44938, 45013, 45075, 45241,
				45434, 45439, 45622, 45786, 46157, 46381, 46613, 46635, 46915,
				47149, 47327, 47396, 47785, 47812, 48309, 48464, 49163, 49219,
				49291, 49599, 49676, 49793, 49825, 49942, 49962, 50136 };

		return timesA;
	}

	private int nextReleaseTime(int index) {

		if (index < relTimes.length) {
			return relTimes[index];
		} else {
			return 100000000;
		}

	}

	private void periodicLoad69() {

		new Worker(PEH_TOP_PRIO, 200, 0, 512, 256, 5, "PEH_0").register();
		new Worker(PEH_TOP_PRIO-1, 250, 0, 512, 256, 5, "PEH_1").register();
		new Worker(PEH_TOP_PRIO-2, 300, 0, 512, 256, 15, "PEH_2").register();
		new Worker(PEH_TOP_PRIO-3, 500, 0, 512, 256, 15, "PEH_3").register();
		new Worker(ALEH_BASE_PRIO-1, 750, 0, 512, 256, 20, "PEH_4").register();
		new Worker(ALEH_BASE_PRIO-2, 1125, 0, 512, 256, 20, "PEH_5").register();
		new Worker(ALEH_BASE_PRIO-3, 1250, 0, 512, 256, 25, "PEH_6").register();
		new Worker(ALEH_BASE_PRIO-4, 2000, 0, 512, 256, 45, "PEH_7").register();
		new Worker(ALEH_BASE_PRIO-5, 2500, 0, 512, 256, 50, "PEH_8").register();
		new Worker(ALEH_BASE_PRIO-6, 2525, 0, 512, 256, 100, "PEH_9").register();

		// System.out.println(PEH_TOP_PRIO);
		// System.out.println(PEH_TOP_PRIO-1);
		// System.out.println(PEH_TOP_PRIO-2);
		// System.out.println(PEH_TOP_PRIO-3);
		// System.out.println(ALEH_BASE_PRIO-1);
		// System.out.println(ALEH_BASE_PRIO-2);
		// System.out.println(ALEH_BASE_PRIO-3);
		// System.out.println(ALEH_BASE_PRIO-4);
		// System.out.println(ALEH_BASE_PRIO-5);
		// System.out.println(ALEH_BASE_PRIO-6);
		
		/* This task makes the set non-schedulable */
		// new Worker(MAX_PRIO - 1, 100, 0, 512, 256, 60000, "ST")
		// .register();

	}

	private void periodicLoad88() {

		new Worker(PEH_TOP_PRIO, 150, 0, 512, 256, 5, "PEH_0").register();
		new Worker(PEH_TOP_PRIO-1, 155, 0, 512, 256, 5, "PEH_1").register();
		new Worker(PEH_TOP_PRIO-2, 300, 0, 512, 256, 15, "PEH_2").register();
		new Worker(PEH_TOP_PRIO-3, 500, 0, 512, 256, 25, "PEH_3").register();
		new Worker(ALEH_BASE_PRIO-1, 550, 0, 512, 256, 25, "PEH_4").register();
		new Worker(ALEH_BASE_PRIO-2, 1000, 0, 512, 256, 30, "PEH_5").register();
		new Worker(ALEH_BASE_PRIO-3, 1250, 0, 512, 256, 50, "PEH_6").register();
		new Worker(ALEH_BASE_PRIO-4, 1500, 0, 512, 256, 110, "PEH_7").register();
		new Worker(ALEH_BASE_PRIO-5, 1750, 0, 512, 256, 110, "PEH_8").register();
		new Worker(ALEH_BASE_PRIO-6, 2000, 0, 512, 256, 100, "PEH_9").register();

	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		return (NUM_ALEH+12) * 1024;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void cleanUp() {

		long sum;
		int data;
		int N;
		int max, min;

		double avg;
		double stdev_sum;
		double stdev;

		Printer printer = new Printer();
		AperiodicWorker aw;

		for (int i = 0; i < NUM_ALEH; i++) {

			aw = aleh[i];

			sum = 0;
			data = 0;
			N = 0;
			max = Integer.MIN_VALUE;
			min = Integer.MAX_VALUE;

//			 System.out.println(aw.rel);
			// served
			System.out.print(rel + "\t");
			System.out.print(aw.serv);

			for (int j = 0; j < relTimes.length; j++) {

				data = (int) (aw.servs[j] - aw.rels[j]);
				if (data > 0) {
					if (data > max)
						max = data;

					if (data < min)
						min = data;

					sum += data;
					N++;
				}

			}

			avg = ((double) sum) / N;
			stdev_sum = 0;
			stdev = 0;

			for (int j = 0; j < relTimes.length; j++) {
				data = (int) (aw.servs[j] - aw.rels[j]);
				if (data > 0) {
					stdev_sum += ((double) data - avg) * ((double) data - avg);
				}
			}

			stdev = Math.sqrt(stdev_sum / (N - 1));

			printer.avg = avg;
			printer.max = max;
			printer.min = min;
			printer.stdev = stdev;

			ManagedMemory.enterPrivateMemory(2048, printer);

		}

		System.out.println();

	}

	class Printer implements Runnable {

		double stdev;
		double avg;
		int max;
		int min;

		@Override
		public void run() {

			// min
			System.out.print("\t" + min);
			// max
			System.out.print("\t" + max);
			// avg
			System.out.print("\t" + avg);
			// stdev
			System.out.print("\t" + stdev);
			
			System.out.print("\t");
		}

	}
}
