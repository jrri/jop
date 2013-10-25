package libs.check.scj;

import javax.realtime.PriorityParameters;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

public class GenericSequencer extends MissionSequencer<GenericMission>{
	
	private int next = 0;
	private GenericMission[] missions;

	public GenericSequencer(PriorityParameters priority,
			StorageParameters storage, String name)
			throws IllegalStateException {
		super(priority, storage, name);
		
		this.missions = new GenericMission[3];
		missions[0] = new TestSafeHashMapMission();
		missions[1] = new TestSafeVectorMission();
		missions[2] = new TestSafeStringBuilderMission();
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected GenericMission getNextMission() {
		
		if(next < missions.length)
			return missions[next++]; 
		
		return null;
			
	}

}
