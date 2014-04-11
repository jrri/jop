package jopscjeval;

import javax.realtime.AbsoluteTime;
import javax.realtime.RealtimeClock;
import javax.realtime.RelativeTime;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

public class Test000 extends Mission {
	
	/* N = 2,5,8,11,14,17,20,23,26*/
	int N = 1;
	int iterations = 1000;
	long periodMs = 150;
	
	final boolean USE_SCJ_API = true;
	
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {
		
		if (!USE_SCJ_API) {
			long[] longTimes = new long[iterations];
			useJopAPI(longTimes, periodMs);
		}else{
			RelativeTime[] relTimes = new RelativeTime[iterations];
			for(int i = 0; i<iterations; i++){
				relTimes[i] = new RelativeTime();
			}
			useScjAPI(relTimes, periodMs);
		}
		
		/* Uncomment to add noise */
//		for (int i = 0; i < N; i++) {
//			new GenericPEH(MIN_PRIO+i, 300, 0, 0, 0, 1024, 1000, "PEH") {
//				@Override
//				@SCJAllowed
//				public void handleAsyncEvent() {
//				}
//			}.register();
//		}		

	
	}
	
	private void useScjAPI(final RelativeTime[] relTimes, final long period) {

		new GenericPEH(ImmortalEntry.MAX_PRIO - 1, period, 0, 0, 0, 1024,
				1000, "PEH") {

			int releases = 0;

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

				AbsoluteTime now = RealtimeClock.getRealtimeClock().getTime();
				AbsoluteTime startMission = new AbsoluteTime(RtThreadImpl.startTime/1000, 
						(RtThreadImpl.startTime % 1000)*1000);
				
				RelativeTime expected = new RelativeTime(releases * period, 0);

				now.subtract(startMission, relTimes[releases]);
				relTimes[releases].subtract(expected, relTimes[releases]);

				releases++;
				if (releases > iterations - 1)
					Mission.getCurrentMission().requestTermination();

			}

			@Override
			public void cleanUp() {
				
				Utils.statisctics(relTimes);
				
//				Printer printer = new Printer();
//				for (int i = 0; i < 10; i++){
//					printer.arg = relTimes[i];
//					ManagedMemory.enterPrivateMemory(1024, printer);
//				}
//
			}

		}.register();
	}

	private void useJopAPI(final long[] longTimes, final long period) {
		
		/* High priority tasks should have the lowest release jitter. Lower priority tasks
		 * are affected by preemption */
		new GenericPEH(ImmortalEntry.MAX_PRIO - 1, period, 0, 0, 0, 1024, 1000,
				"PEH") {

			int releases = 0;

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

				long now = Native.rd(Const.IO_US_CNT);
				
				longTimes[releases] = now - RtThreadImpl.startTime
						- (releases * period * 1000);

				releases++;
				if (releases > iterations - 1)
					Mission.getCurrentMission().requestTermination();

			}

			@Override
			public void cleanUp() {
				
				Utils.statistics(null, longTimes);
				
//				ManagedMemory.enterPrivateMemory(1024, new Runnable() {
//					
//					@Override
//					public void run() {
//						for(int i = 0; i < 10; i++)
//							System.out.println(longTimes[i]);
//					}
//				});
				
			}
			
			
		}.register();
		
	}
	
	@Override
	@SCJAllowed
	public long missionMemorySize() {
		return (N+2) * 8192;
	}
	
	class Printer implements Runnable {
		
		Object arg;
		
		@Override
		public void run() {
			System.out.println(arg.toString());
		}
		
	}
	
}
