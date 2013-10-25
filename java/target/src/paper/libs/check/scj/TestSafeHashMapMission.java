package libs.check.scj;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

public class TestSafeHashMapMission extends GenericMission {

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {
		
		PriorityParameters PRI001 = new PriorityParameters(10);
		PeriodicParameters PER001 = new PeriodicParameters(new RelativeTime(),
				new RelativeTime(1000, 0));
		StorageParameters STO001 = new StorageParameters(2048, null);
		
		TestSafeHashMap TSHM = new TestSafeHashMap(PRI001, PER001, STO001, 1024, "TSHM");
		TSHM.register();
		
	}

}
