package jopscjeval;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

public class Test002 extends Mission {

	int memSize = 1024;
	int bsSize = 1024;
	int n = 5;
	int periodMs = 150;
	int periodNs = 470000;
	long MISSION_MEMORY = 2048 * n;

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {

		System.out.println(" --- Minimum period test start --- ");
		System.out.println(" Settings: ");
		System.out.println(" Handler bsSize: " + bsSize);
		System.out.println(" Handler memSize: " + memSize);
		System.out.println(" Number of handlers: " + n);

		for (int i = 1; i < n + 1; i++)
			new MinimumPeriodTest(15, this.periodMs, this.periodNs, memSize,
					bsSize, "PEH" + i).register();

	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		return MISSION_MEMORY;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void cleanUp() {
		System.out.println(" --- Minimum period test end --- ");
	}

}
