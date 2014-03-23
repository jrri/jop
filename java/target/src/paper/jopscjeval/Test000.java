package jopscjeval;

import javax.realtime.AbsoluteTime;
import javax.realtime.PriorityScheduler;
import javax.realtime.RealtimeClock;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Memory;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

public class Test000 extends Mission {
	
	/* N = 2,5,8,11,14,17,20,23,26*/
	int N = 1;

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {

		/* About 1000 releases of the periodic handler */
		int testDuration = 140000;
		final RealtimeClock rtc = (RealtimeClock) RealtimeClock
				.getRealtimeClock();
		
		int MAX_PRIO = PriorityScheduler.instance().getMaxPriority();
		int MIN_PRIO = PriorityScheduler.instance().getMinPriority();

		/* High priority tasks should have the lowest release jitter. Lower priority tasks
		 * are affected by preemption */
		new GenericPEH(MAX_PRIO-1, 150, 0, 0, 0, 1024, 1000, "PEH") {
//		new GenericPEH(MIN_PRIO, 150, 0, 0, 0, 1024, 1000, "PEH") {

			@Override
			@SCJAllowed
			public void handleAsyncEvent() {

//				AbsoluteTime startMission = RtThreadImpl.getStartMissionTime();
//				AbsoluteTime now = rtc.getTime();
				
				long now = Native.rd(Const.IO_US_CNT);
				System.out.println(now - RtThreadImpl.startTime);
				
//				m = RtThreadImpl.startTime;
				// System.out.println(rtc.getTime());
				// System.out.println(rtc.getTime2());

			}
		}.register();
		
		/* Terminator handler */
		new GenericPEH(MAX_PRIO, 100000, 0, testDuration, 0, 256, 128, "T") {
			
			@Override
			@SCJAllowed
			public void handleAsyncEvent() {
				Mission.getCurrentMission().requestTermination();
			}
			
		}.register();
		

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
	@Override
	@SCJAllowed
	public long missionMemorySize() {
		return (N+2) * 1024;
	}

}
