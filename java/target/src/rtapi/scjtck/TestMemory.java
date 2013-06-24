package scjtck;

import javax.realtime.ImmortalMemory;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PrivateMemory;
import javax.safetycritical.Safelet;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

public class TestMemory extends TestCase implements Safelet {
	
	public String getName() {
		return "Test memory";
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.INITIALIZATION)
	public void initializeApplication() {
		
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize() {
		return 1000L;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {
		
		ImmortalMemory im = ImmortalMemory.instance();
		System.out.println(im.size());
		System.out.println(im.memoryConsumed());
		System.out.println(im.memoryRemaining());
		System.out.println(im.xx());
				
	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		// TODO Auto-generated method stub
		return 0;
	}

}
