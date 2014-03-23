package edu.purdue.scjtck.tck;

import java.util.Random;

import javax.realtime.SizeEstimator;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionMemory;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Terminal;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import edu.purdue.scjtck.Properties;

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
	private int _repeats = 1;

	public MissionSequencer getSequencer() {
		
		return new GeneralMissionSequencer() {

			GeneralMission mission = new GeneralMission() {

				private int _nObjects;

				private final long _memBaseSize = 1024+512;

				private final int _maxNumObjects = 500;

				@Override
				public void initialize() {
					
					Object obj = new Object();
					final ManagedMemory MissMem = ManagedMemory.getManagedMemory(obj);
					
					new GeneralPeriodicEventHandler() {
						@Override
						public void handleAsyncEvent() {
							
//							System.out.println(MissMem.memoryConsumed());
							System.out.println(MissMem.memoryRemaining());
//							System.out.println(MissMem.getRemainingBackingStore());
							
							// use up all the mission memory
							try {
								for (int i = 0; i < _nObjects; i++) {
									new Object();
								}
								
//								System.out.println(MissMem.memoryConsumed());
								
//								if (_repeats-- == 0)
									requestTermination();
							} catch (Throwable e) {
								fail("Failure in resizing mission memory size. Msg: "
										+ e.getMessage());
								requestTermination();
							}
						}

						@Override
						@SCJAllowed(Level.SUPPORT)
						@SCJRestricted(phase = Phase.CLEANUP)
						public void cleanUp() {
							teardown();
						}

					}.register();
					
					
//					System.out.println(MissMem.memoryConsumed());
				}

				@Override
				public long missionMemorySize() {
					// randomly adjust the size of mission memory
					Random rnd = new Random();

					_nObjects = (int) (rnd.nextDouble() * _maxNumObjects)
							% _maxNumObjects + 1;
					SizeEstimator estimator = new SizeEstimator();
					estimator.reserve(Object.class, _nObjects);

					long objs = estimator.getEstimate();
					long mm = objs + _memBaseSize;
					
					Terminal.getTerminal().writeln();
					Terminal.getTerminal().writeln("Mission memory: " + mm);
					Terminal.getTerminal().writeln("Objects: " + objs);

					return mm;
				}

			};

			@Override
			@SCJAllowed(Level.SUPPORT)
			protected Mission getNextMission() {

				if (_repeats-- > 0)
					return mission;
				else
					return null;

			}
		};
	}
		
//		return new GeneralMissionSequencer(new GeneralMission() {
//
//			private int _nObjects;
//
//			private final long _memBaseSize = 1024;
//
//			private final int _maxNumObjects = 1000;
//
//			@Override
//			public void initialize() {
//				new GeneralPeriodicEventHandler() {
//					@Override
//					public void handleAsyncEvent() {
//						// use up all the mission memory
//						try {
//							for (int i = 0; i < _nObjects; i++) {
//								new Object();
//							}
//							if (_repeats-- == 0)
//								requestTermination();
//						} catch (Throwable e) {
//							fail("Failure in resizing mission memory size. Msg: "
//									+ e.getMessage());
//							requestTermination();
//						}
//					}
//					
//					@Override
//					@SCJAllowed(Level.SUPPORT)
//					@SCJRestricted(phase = Phase.CLEANUP)
//					public void cleanUp() {
//						teardown();
//					}
//					
//				}.register();
//			}
//
//			@Override
//			public long missionMemorySize() {
//				// randomly adjust the size of mission memory
//				Random rnd = new Random();
//				
//				_nObjects = (int) (rnd.nextDouble() * _maxNumObjects)
//						% _maxNumObjects + 1;
//				SizeEstimator estimator = new SizeEstimator();
//				estimator.reserve(Object.class, _nObjects);
//				
//				long mm = estimator.getEstimate() + _memBaseSize; 
//				Terminal.getTerminal().writeln("Mission memory: "+ mm);
//				
//				return mm;
//			}
//			
//		});
//	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize() {
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	protected String getArgs() {
		return "-L 1";
	}
}
