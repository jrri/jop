package test.level1;

import javax.realtime.AbsoluteTime;
import javax.realtime.AffinitySet;
import javax.realtime.AperiodicParameters;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.Services;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.Terminal;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import test.cyclic.ImmortalEntry;

public class MyMission extends Mission {

	private final boolean DBG_MODE = false;

	int number;
	int totalPeriodicHandlers = 1;
	int totalAperiodicHandlers = 1;
	int totalAperiodicLongHandlers = 1;

	public MyMission(int number) {

		this.number = number;

	}

	@Override
	protected void initialize() {

		if (DBG_MODE) {
			PriorityParameters dbgPrio = new PriorityParameters(14);
			AbsoluteTime start = new AbsoluteTime(0, 0);
			RelativeTime period = new RelativeTime(1000, 0);
			PeriodicParameters dbgPparams = new PeriodicParameters(start,
					period);
			StorageParameters dbgStorage = new StorageParameters(1024, null,
					512L, 0L, 0L);
			DebugPEH dbgPeh = new DebugPEH(dbgPrio, dbgPparams, dbgStorage,
					"DBG_PEH");
			dbgPeh.register();
		} else {
			initialize0();
		}

	}

	private void initialize0() {

		TestPEH peh;
		TestPEH peh0;
		TestAEH aeh;
		TestALEH aleh;

		Terminal.getTerminal().writeln(
				"[MISSION " + number + "] start initialization");

		// peHandlerCount = totalPeriodicHandlers;
		// aeHandlerCount = totalAperiodicHandlers;
		// aleHandlerCount = totalAperiodicLongHandlers;

		PriorityParameters eh1_prio = new PriorityParameters(14);
		AperiodicParameters eh1_pparams = new AperiodicParameters(null, null);

		StorageParameters eh1_storage = new StorageParameters(1024, null, 512L,
				0L, 0L);
		aeh = new TestAEH(eh1_prio, eh1_pparams, eh1_storage, "[AEH]: ", number);
		AffinitySet.setProcessorAffinity(
				Services.getSchedulingAllocationDoamins()[number], aeh);

		aleh = new TestALEH(eh1_prio, eh1_pparams, eh1_storage, "[ALEH]: ");

		PriorityParameters eh0_prio = new PriorityParameters(13);
		RelativeTime eh0_start = new RelativeTime(0, 0);
		RelativeTime eh0_period = new RelativeTime(1000, 0);
		PeriodicParameters eh0_pparams = new PeriodicParameters(eh0_start,
				eh0_period);

		StorageParameters eh0_storage = new StorageParameters(1024, null, 512L,
				0L, 0L);
		peh = new TestPEH(eh0_prio, eh0_pparams, eh0_storage, "[PEH0]: ", aeh,
				aleh, 0);

		peh0 = new TestPEH(eh0_prio, eh0_pparams, eh0_storage, "[PEH1]: ", aeh,
				aleh, 1);

		aeh.register();
		aleh.register();
		peh.register();
		peh0.register();
		
		Terminal.getTerminal().writeln(
				"[MISSION " + number + "] finished initialization");

	}

	@Override
	public long missionMemorySize() {
		return 7800;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void cleanUp() {

		class Helper implements Runnable {

			public int coreNr;
			public int missionNr;

			@Override
			public void run() {
				System.out.println("AEH firings core " + coreNr + ", mission "
						+ number + ": "
						+ ImmortalEntry.shared[coreNr][missionNr]);
			}

		}

		Helper h = new Helper();
		h.missionNr = number;

		synchronized (this) {

			System.out.println("Clean [MISSION " + number + "]");
			for (int i = 0; i < ImmortalEntry.sys.nrCpu; i++) {
				h.coreNr = i;
				ManagedMemory.enterPrivateMemory(1500, h);
			}
		}
	}
}
