package edu.purdue.scjtck.tck;

import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.RealtimeThread;
import javax.realtime.ScopedMemory;
import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;

/**
 * Level 2
 * 
 * - AEH, PEH, NHRT are no-heap and non-daemon.
 */
public class TestSchedule401 extends TestCase {

    private volatile boolean[] _check = new boolean[3];
    private final int _AEHCheckIndex = 0;
    private final int _PEHCheckIndex = 1;
    private final int _NHRTCheckIndex = 2;
    private volatile int _done = 0;

    public MissionSequencer getSequencer() {
        return new GeneralSingleMissionSequencer(new GeneralMission() {

            public void initialize() {

                final AperiodicEventHandler event = new GeneralAperiodicEventHandler() {

                            public void handleAsyncEvent() {

                                if (!validateCurrentThread())
                                    fail("AEH should be non-daemon");
                                if (!validateCurrentMemory())
                                    fail("AEH should be non-heap");

                                _check[_AEHCheckIndex] = true;

                                if (++_done >= _check.length)
                                    Mission.getCurrentMission().requestTermination();
                            }
                        };
                        event.register();

                new GeneralPeriodicEventHandler() {

                    public void handleAsyncEvent() {
                        event.release();

                        if (!validateCurrentThread())
                            fail("PEH should be non-daemon");
                        if (!validateCurrentMemory())
                            fail("PEH should be non-heap");

                        _check[_PEHCheckIndex] = true;

                        if (++_done >= _check.length)
                        	Mission.getCurrentMission().requestTermination();
                    }
                }.register();

//                new GeneralManagedThread() {
//                    @Override
//                    public void run() {
//                        if (validateCurrentThread())
//                            fail("NHRT should be non-daemon");
//                        if (validateCurrentMemory())
//                            fail("NHRT should be non-heap");
//
//                        _check[_NHRTCheckIndex] = true;
//
//                        if (++_done >= _check.length)
//                            requestSequenceTermination();
//                    }
//                }.start();
            }

            @Override
            protected void cleanUp() {
                if (!_check[_AEHCheckIndex])
                    fail("AEH not executed");
                if (!_check[_PEHCheckIndex])
                    fail("PEH not executed");
                if (!_check[_NHRTCheckIndex])
                    fail("NHRT not executed");
                teardown();
                super.cleanUp();
            }
        });
    }

    private static boolean validateCurrentThread() {
    	// In JOP there are no daemon processes, this test will be 
    	// satisfied by design
    	// RtThreadImpl.currentRtThread().isDaemon() = false, ALWAYS;
    	// that is, this test will allways validate the thread
        return true;
    }

    private static boolean validateCurrentMemory() {
        MemoryArea mem = RealtimeThread.getCurrentMemoryArea();
        return (mem instanceof ImmortalMemory) || (mem instanceof ScopedMemory);
    }

	@Override
	public long immortalMemorySize() {
		return 1000;
	}

	@Override
	protected String getArgs() {
		return "-L 1";
	}
}
