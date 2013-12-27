package test.level1;

import javax.realtime.AbsoluteTime;
import javax.realtime.AperiodicParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.Mission;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

public class MyMission extends Mission{
	
	private final boolean DBG_MODE = true; 
	
	int number;
	int totalPeriodicHandlers = 1;
	int totalAperiodicHandlers = 1;
	int totalAperiodicLongHandlers = 1;
	
	public MyMission(int number){
		
		this.number = number;
		
	}
	
	@Override
	protected void initialize(){
		
		if(DBG_MODE){
			PriorityParameters dbgPrio = new PriorityParameters(14);
			AbsoluteTime start = new AbsoluteTime(0,0);
			RelativeTime period = new RelativeTime(1000, 0);
			PeriodicParameters dbgPparams = new PeriodicParameters(start, period);
			StorageParameters dbgStorage = new StorageParameters(1024, null, 0, 0);
			DebugPEH dbgPeh = new DebugPEH(dbgPrio, dbgPparams, dbgStorage, 512, "DBG_PEH");
			dbgPeh.register();
		}else{
			initialize0();
		}
		
	}

	
	private void initialize0() {
		
		TestPEH peh;
		TestAEH aeh;
		TestALEH aleh;
		
		Terminal.getTerminal().writeln("[MISSION " +number+ "] start initialization");

		// peHandlerCount = totalPeriodicHandlers;
		// aeHandlerCount = totalAperiodicHandlers;
		// aleHandlerCount = totalAperiodicLongHandlers;
		
		PriorityParameters eh1_prio = new PriorityParameters(14);
		AperiodicParameters eh1_pparams = new AperiodicParameters(null, null);
		
		StorageParameters eh1_storage = new StorageParameters(1024, null, 0, 0);
		aeh = new TestAEH(eh1_prio, eh1_pparams, eh1_storage, 512, "[AEH]: ");
		
		aeh.register();
		aleh = new TestALEH(eh1_prio, eh1_pparams, eh1_storage, 512, "[ALEH]: ");
		aleh.register();
		
		PriorityParameters eh0_prio = new PriorityParameters(13);
		RelativeTime eh0_start = new RelativeTime(0,0);
		RelativeTime eh0_period = new RelativeTime(1000, 0);
		PeriodicParameters eh0_pparams = new PeriodicParameters(eh0_start, eh0_period);
		
		
		StorageParameters eh0_storage = new StorageParameters(1024, null, 0, 0);
		
		peh = new TestPEH(eh0_prio, eh0_pparams, eh0_storage, 512, "[PEH]: ", aeh,aleh);
		
		peh.register();
		
		Terminal.getTerminal().writeln("[MISSION " +number+ "] finished initialization");
		
	}

	@Override
	public long missionMemorySize() {
		return 4800;
	}
	
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void cleanUp() {
		System.out.println("Custom clean");
	}

}
