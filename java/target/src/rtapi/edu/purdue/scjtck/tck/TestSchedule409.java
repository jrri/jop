package edu.purdue.scjtck.tck;

import javax.safetycritical.AperiodicEventHandler;
import javax.safetycritical.InterruptHandler;
import javax.safetycritical.ManagedInterruptServiceRoutine;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.StorageParameters;

import com.jopdesign.io.IOFactory;
import com.jopdesign.io.SysDevice;

/**
 * Level 1?
 * 
 * This tests "Interrupt Handling" section
 */
public class TestSchedule409 extends TestCase {

	private boolean[] _check = new boolean[3];
	private final int _singalExternalEventCheckIndex = 0;
	private final int _arrayExternalEventCheckIndex = 1;
	private final int _interruptHandlerCheckIndex = 2;

	public MissionSequencer getSequencer() {
		return new GeneralSingleMissionSequencer(new GeneralMission() {

			// TODO: interrupt ID is VM implementation dependent
			private int _interruptID = 1;

			public void initialize() {

				// Originally this was triggered by an external happening
				AperiodicEventHandler aeh1 = new GeneralAperiodicEventHandler() {
					public void handleAsyncEvent() {
						_check[_singalExternalEventCheckIndex] = true;
						requestTermination();
					}
				};
				
				aeh1.register();

				// Originally this was triggered by an external happening
				AperiodicEventHandler aeh2 = new GeneralAperiodicEventHandler() {
					@Override
					public void handleAsyncEvent() {
						_check[_arrayExternalEventCheckIndex] = true;
						requestTermination();
					}
				};
				
				aeh2.register();

				MyInterruptHandler mih = new MyInterruptHandler(
						new StorageParameters(512, null, 256, 0, 0));
				mih.register(_interruptID);

				InterruptHandler.enableGlobalInterrupts();
				ManagedInterruptServiceRoutine.getInterruptPriority(_interruptID);

				new Terminator().register();
				
				new GeneralPeriodicEventHandler() {
					
					@Override
					public void handleAsyncEvent() {
						
						// Trigger the interrupt by SW
						SysDevice sys = IOFactory.getFactory().getSysDevice();
						sys.intNr = _interruptID;
						
					}
					
				}.register();
			}

			@Override
			protected void cleanUp() {
				if (!_check[_singalExternalEventCheckIndex])
					fail("Error occurred in ExternalEvent (single handler)");
				if (!_check[_arrayExternalEventCheckIndex])
					fail("Error occurred in ExternalEvent (multiple handlers)");
				if (!_check[_interruptHandlerCheckIndex])
					fail("Error occurred in InterruptHandler");
				super.cleanUp();
				
				teardown();
			}

		});
	}

	// TODO: how can we use this class?
	class MyInterruptHandler extends ManagedInterruptServiceRoutine {

		public MyInterruptHandler(StorageParameters storage) {
			super(storage);
		}

		@Override
		protected synchronized void handle() {
			_check[_interruptHandlerCheckIndex] = true;
		}
	}

	@Override
	public long immortalMemorySize() {
		return 10000;
	}

	@Override
	protected String getArgs() {
		return "-L 1";
	}
}
