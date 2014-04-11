package test.level1;

import javax.realtime.PriorityParameters;
import javax.safetycritical.LinearMissionSequencer;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.RepeatingMissionSequencer;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import test.cyclic.ImmortalEntry;

public class Level1Safelet implements Safelet{
	
	static final int sequencerSelector = 0;

	@Override
	public MissionSequencer<Mission> getSequencer() {
		
		PriorityParameters seq_prio = new PriorityParameters(13);
		
		StorageParameters seq_storage = new StorageParameters(16384, null, 0, 0, 0);

		MyMission m0 = new MyMission(0);
		MyMission m1 = new MyMission(1);
		
		Mission[] missions = new Mission[2];
		missions[0] = m0;
		missions[1] = m1;
		
		MissionSequencer[] ms = new MissionSequencer[3];

		// The Linear and Repeating sequencers return mission objects residing
		// in Immortal
		MissionSequencer<Mission> ms0 = new LinearMissionSequencer<Mission>(
				seq_prio, seq_storage, missions, false);
		MissionSequencer<Mission> ms1 = new RepeatingMissionSequencer<Mission>(
				seq_prio, seq_storage, missions);
		MissionSequencer<Mission> ms2 = new CustomMissionSequencer(seq_prio,
				seq_storage, "a");
		
		ms[0] = ms0;
		ms[1] = ms1;
		ms[2] = ms2;
		
		return ms[sequencerSelector];
		
	}

	@Override
	public long immortalMemorySize() {
		return 10000;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.INITIALIZATION)
	public void initializeApplication() {
		// TODO Auto-generated method stub
		ImmortalEntry.setup();
		
	}
	
	

}
