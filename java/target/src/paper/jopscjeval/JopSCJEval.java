package jopscjeval;

import javax.realtime.PriorityParameters;
import javax.realtime.RealtimeClock;
import javax.safetycritical.JopSystem;
import javax.safetycritical.LinearMissionSequencer;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.Native;
import com.jopdesign.sys.RtThreadImpl;

public class JopSCJEval implements Safelet {

	static JopSCJEval app;

	/* Global data structures */
	int[] times;
	int runs = 100;
	
//	Mission[] tests = {new Test001(), new Test002(), new Test003(),new Test004()};
//	Mission[] tests = {new Test001()};
//	Mission[] tests = {new Test002()};
//	Mission[] tests = {new Test004(), new Test004()};
	Mission[] tests = {new Test000()};
//	Mission[] tests = {new Test007()};
	
	
	
	public static void main(String[] args) {

		RealtimeClock.getRealtimeClock().getTime();
		
		app = new JopSCJEval();
		
		JopSystem.startMission(app);

	}

	/* Safelet methods */

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.INITIALIZATION)
	public void initializeApplication() {
		times = new int[runs];
		ImmortalEntry.setup();
		
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.INITIALIZATION)
	public MissionSequencer<Mission> getSequencer() {

		return new LinearMissionSequencer<Mission>(new PriorityParameters(10),
				new StorageParameters(1024, null, 512, 0, 0), tests, false, "Sequencer");

	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize() {
		return 10000;
	}

	/* Mission methods */

//	@Override
//	@SCJAllowed(Level.SUPPORT)
//	protected void initialize() {
		
//		new MemoryTest(15, this.period, 262140, 262140, "PEH").register();

		
//		new GenericPEH(15, this.period, 1024, 512, "PEH") {
//			
//			int i = 0;
//
//			public void handleAsyncEvent() {
//				times[i] = Native.rd(Const.IO_US_CNT);
//				i++;
//
//				if (i > times.length - 1)
//					Mission.getCurrentMission().requestTermination();
//			};
//
//			
//		}.register();


//	}

//	@Override
//	@SCJAllowed
//	public long missionMemorySize() {
//		return MISSION_MEMORY;
//	}

//	@Override
//	@SCJAllowed(Level.SUPPORT)
//	protected void cleanUp() {
//
//		PrintHelper ph = new PrintHelper();
//		int initial, expected;
//
////		initial = times[0];
//		initial = RtThreadImpl.startTime;
//
//		for (int i = 0; i < times.length; i++) {
//
////			if (i != 0) {
//				expected = initial + i * periodMs * 1000;
//				ph.diff = times[i] - expected;
//				ManagedMemory.enterPrivateMemory(512, ph);
////			}
//		}
//	}

	class PrintHelper implements Runnable {

		int diff = 0;

		@Override
		public void run() {
			System.out.println(diff);
		}

	}

}
