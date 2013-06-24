package test.level1;

import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

public class CustomMissionSequencer extends MissionSequencer<Mission>{
	
	private boolean served = false;

	public CustomMissionSequencer(PriorityParameters priority,
			StorageParameters storage, String name)
			throws IllegalStateException {
		super(priority, storage, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected Mission getNextMission() {
		// TODO Auto-generated method stub
		if(!served){
			served = true;
			return new MyMission(2);
		}
		return null;
	}
}
