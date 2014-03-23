package test.mixed;

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

import test.cyclic.CyclicMission;
import test.cyclic.ImmortalEntry;
import test.level1.CustomMissionSequencer;
import test.level1.MyMission;

public class MixedLevelSafelet implements Safelet{
	
	static final int sequencerSelector = 0;

	@Override
	public MissionSequencer<Mission> getSequencer() {
		
		PriorityParameters seq_prio = new PriorityParameters(13);
		
		StorageParameters seq_storage = new StorageParameters(16384, null, 0L, 0L, 0L);

		Mission[] missions = new Mission[ImmortalEntry.missions+1];
		missions[0] = new CyclicMission();
		
		for(int i = 0; i < ImmortalEntry.missions; i++){
			missions[i+1] = new MyMission(i+1);
		}
		
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
		ImmortalEntry.setup();
	}

}
