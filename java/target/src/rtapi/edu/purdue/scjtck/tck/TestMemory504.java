package edu.purdue.scjtck.tck;

import javax.realtime.SizeEstimator;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

/**
 * @author leizhao
 * 
 * Assertion:
 *  - mission memory is resizable
 */
public class TestMemory504 extends TestCase {

	/**
	 * mission repeat times
	 */
	private int _repeats = 10;

	public MissionSequencer getSequencer() {
		return new GeneralSingleMissionSequencer(new GeneralMission() {

			private int _nObjects;

			private final long _memBaseSize = 1;

			private final int _maxNumObjects = 1000;

			@Override
			public void initialize() {
				new GeneralPeriodicEventHandler() {
					@Override
					public void handleAsyncEvent() {
						// use up all the mission memory
						try {
							for (int i = 0; i < _nObjects; i++) {
								new Object();
							}
							if (_repeats-- == 0)
								requestTermination();
						} catch (Throwable e) {
							fail("Failure in resizing mission memory size. Msg: "
									+ e.getMessage());
							requestTermination();
						}
					}
				};
			}

			@Override
			public long missionMemorySize() {
				// randomly adjust the size of mission memory
				_nObjects = (int) (Math.random() * _maxNumObjects)
						% _maxNumObjects + 1;
				SizeEstimator estimator = new SizeEstimator();
				estimator.reserve(Object.class, _nObjects);
				return estimator.getEstimate() + _memBaseSize;
			}
		});
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize() {
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	protected String getArgs() {
		// TODO Auto-generated method stub
		return null;
	}
}
