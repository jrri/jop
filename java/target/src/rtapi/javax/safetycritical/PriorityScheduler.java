package javax.safetycritical;

import javax.safetycritical.annotate.BlockFree;
import javax.safetycritical.annotate.SCJAllowed;

import static javax.safetycritical.annotate.Level.LEVEL_0;
import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.LEVEL_2;

@SCJAllowed(LEVEL_1)
public class PriorityScheduler extends javax.realtime.PriorityScheduler {
	
	protected PriorityScheduler(){};
	
	private static PriorityScheduler instance = null;

	public static PriorityScheduler instance() {
		
		if(instance == null){
			instance = new PriorityScheduler();
		}
		return instance;
	}

	@BlockFree
	@SCJAllowed(LEVEL_1)
	public int getMaxHardwarePriority() {
		return 2000;
	}

	@BlockFree
	@SCJAllowed(LEVEL_1)
	public int getMinHardwarePriority() {
		return 1000;
	}
}
