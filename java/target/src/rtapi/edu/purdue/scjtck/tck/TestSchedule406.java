package edu.purdue.scjtck.tck;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.PriorityScheduler;
import javax.realtime.RelativeTime;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PeriodicEventHandler;
//import javax.safetycritical.StorageConfigurationParameters;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

/**
 * Level 1
 * 
 * - A preempted schedulable object must be placed at the front of the run queue
 * for its active priority level.
 */
public class TestSchedule406 extends TestCase {

	@Override
	protected String getArgs() {
		return "-L 1 -I 100";
	}

	public MissionSequencer getSequencer() {
		return new GeneralSingleMissionSequencer(new GeneralMission() {

			private volatile int _curRunner;
			private volatile boolean _done;
			private volatile boolean _preempted;
			private long _iter = _prop._iterations;

			public void initialize() {

				int NOR_PRIORITY = PriorityScheduler.instance()
						.getNormPriority();
				int MIN_PRIORITY = PriorityScheduler.instance()
						.getMinPriority();

				PriorityParameters high = new PriorityParameters(NOR_PRIORITY);
				PriorityParameters low = new PriorityParameters(NOR_PRIORITY
						- (NOR_PRIORITY - MIN_PRIORITY) / 2);

				for (int i = 0; i < 5; i++) {
					final int index = i;

					// final String s = index + " back to run";
					new PeriodicEventHandler(low, new PeriodicParameters(null,
							new RelativeTime(Long.MAX_VALUE, 0)),
							new StorageParameters(_prop._schedObjBackStoreSize,
									null, _prop._schedObjScopeSize,0,0)) {

						@Override
						public void handleAsyncEvent() {
							while (!_done) {
								if (_preempted) {
									int curRunner = _curRunner;
									_preempted = false;

									if (curRunner != index)
										fail("Thread being preempted should be placed in front of the run queue");

									// Terminal.getTerminal().writeln(s);
									Thread.yield();

								} else
									_curRunner = index;
							}
						}
					}.register();
				}

				new PeriodicEventHandler(high, new PeriodicParameters(
						new RelativeTime(20, 0), new RelativeTime(100, 0)),
						new StorageParameters(_prop._schedObjBackStoreSize,
								null, _prop._schedObjScopeSize,0,0)) {
					
					@Override
					public void handleAsyncEvent() {
						_preempted = true;
						if (--_iter <= 0) {
							_done = true;
							requestTermination();
						}
						// Terminal.getTerminal().writeln(
						// _curRunner + " ------------- being preempted");
					}
					
					@Override
					@SCJAllowed(Level.SUPPORT)
					@SCJRestricted(phase = Phase.CLEANUP)
					public void cleanUp() {
						teardown();
					}
					
				}.register();
			}
		});
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize() {
		// TODO Auto-generated method stub
		return 0;
	}
}
