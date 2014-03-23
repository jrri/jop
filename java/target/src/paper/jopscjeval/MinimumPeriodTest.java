package jopscjeval;

import javax.safetycritical.Mission;
import javax.safetycritical.annotate.SCJAllowed;

public class MinimumPeriodTest extends GenericPEH {

	int runs = 0;

	public MinimumPeriodTest(int prio, long periodMs, int periodNs, int bsSize,
			int memSizestorage, String name) {
		super(prio, periodMs, periodNs, bsSize, memSizestorage, name);
		// TODO Auto-generated constructor stub
	}

	@Override
	@SCJAllowed
	public void handleAsyncEvent() {

		runs++;

		if (runs == 10)
			Mission.getCurrentMission().requestTermination();

	}

}
