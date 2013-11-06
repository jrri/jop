package libs.check.scj.generic;

import javax.realtime.PriorityParameters;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import libs.check.scj.mem.TestSafeHashMapMission;
import libs.check.scj.mem.TestSafeStringBuilderMission;
import libs.check.scj.mem.TestSafeVectorMission;
import libs.check.scj.scope.MyMission;

public class GenericSequencer extends MissionSequencer<GenericMission>{
	
	private int next = 0;
	private GenericMission[] missions;

	public GenericSequencer(PriorityParameters priority,
			StorageParameters storage, String name)
			throws IllegalStateException {
		super(priority, storage, name);
		
		this.missions = new GenericMission[1];
//		missions[0] = new TestSafeHashMapMission();
//		missions[1] = new TestSafeVectorMission();
//		missions[2] = new TestSafeStringBuilderMission();
		missions[0] = new MyMission();
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected GenericMission getNextMission() {
		
		if(next < missions.length)
			return missions[next++]; 
		
		return null;
			
	}

}
