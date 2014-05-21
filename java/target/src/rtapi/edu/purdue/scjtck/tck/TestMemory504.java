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
 *         Assertion: - mission memory is resizable
 */
public class TestMemory504 extends TestCase {

	/**
	 * mission repeat times
	 */
	private int _repeats = 2;

	public MissionSequencer getSequencer() {

		return new GeneralMissionSequencer() {

			GeneralMission mission = new GeneralMission() {

				private int _nObjects;

				private final long _memBaseSize = 1024 + 512;

				private final int _maxNumObjects = 100;

				@Override
				public void initialize() {

					Object obj = new Object();
					final ManagedMemory MissMem = ManagedMemory
							.getManagedMemory(obj);

					// debug: careful!!consume mission memory!
					// System.out.println("Resized mission memory");
					// System.out.println("Size: "+ MissMem.size());
					// System.out.println("Consumed: "+MissMem.memoryConsumed());
					// System.out.println("Remaining: "+MissMem.memoryRemaining());
					// System.out.println("Bs. remaining: "+MissMem.getRemainingBackingStore());

					new GeneralPeriodicEventHandler() {
						@Override
						public void handleAsyncEvent() {

							// use up all the mission memory
							try {
								for (int i = 0; i < _nObjects; i++) {
									new Object();
								}
								requestTermination();
							} catch (Throwable e) {
								fail("Failure in resizing mission memory size. Msg: "
										+ e.getMessage());
								requestTermination();
							}
						}

					}.register();

				}

				@Override
				public long missionMemorySize() {

					// randomly adjust the size of mission memory
					Random rnd = new Random();

					_nObjects = (int) (rnd.nextDouble() * _maxNumObjects)
							% (_maxNumObjects + 1);

					// debug:
					// SizeEstimator est = new SizeEstimator();
					// est.reserve(Object.class, 1);
					// System.out.println("No. Objects: " + _nObjects);
					// System.out.println("Object size: " + est.getEstimate());

					SizeEstimator estimator = new SizeEstimator();
					estimator.reserve(Object.class, _nObjects);

					long objsMem = estimator.getEstimate();
					long mm = objsMem + _memBaseSize;

					Terminal.getTerminal().writeln();
					Terminal.getTerminal().writeln("New mission memory: " + mm);
					Terminal.getTerminal().writeln(
							"Memory for Objects: " + objsMem);
					Terminal.getTerminal().writeln();

					return mm;
				}

				@Override
				protected void cleanUp() {
					teardown();
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

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize() {
		return 10000;
	}

	@Override
	protected String getArgs() {
		return "-L 1";
	}
}
