package libcsp.csp.app.ping;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.LinearMissionSequencer;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.Safelet;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import libcsp.csp.ImmortalEntry;
import libcsp.csp.app.CSPGenericMission;
import libcsp.csp.interfaces.InterfaceI2C_A;
import libcsp.csp.interfaces.InterfaceI2C_B;

public class PingMission extends CSPGenericMission implements Safelet<Mission> {

	/* Safelet methods */
	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.INITIALIZATION)
	public void initializeApplication() {
		ImmortalEntry.setup();
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.INITIALIZATION)
	public MissionSequencer<Mission> getSequencer() {
		return new LinearMissionSequencer<Mission>(new PriorityParameters(1),
				new StorageParameters(16000, null), false, this);
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public long immortalMemorySize() {
		return 10000;
	}

	/* Mission methods */
	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {

		/* I2C MAC address is the device address */
		ImmortalEntry.interfaceI2C_A.initialize(ImmortalEntry.NODE_ADDRESS);
		ImmortalEntry.interfaceI2C_B.initialize(0);

		/* Initialize router task, interrupt tasks, and Loopback route */
		init((byte) ImmortalEntry.NODE_ADDRESS, 2, ImmortalEntry.interfaceI2C_B);
		
		/* Add a route to a node through I2C interface */
		ImmortalEntry.manager.routeSet(1, ImmortalEntry.interfaceI2C_A, 1);
		
		/* Initialize application specific handlers */
		initializePingHandler(15);

	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		// TODO Auto-generated method stub
		return 8000;
	}
	
	private void initializePingHandler(int priority) {
		
		final int PING_HANDLER_BACKING_STORE_SIZE_IN_BYTES = 1024;
		final int PING_HANDLER_SCOPE_SIZE_IN_BYTES = 800;
		final int PING_HANDLER_RELEASE_PERIOD_IN_MS = 1000;
		final int PING_HANDLER_PRIORITY = priority;

		PriorityParameters clientHandlerPriorityParameters = new PriorityParameters(
				PING_HANDLER_PRIORITY);
		PeriodicParameters clientHandlerPeriodicParameters = new PeriodicParameters(
				new RelativeTime(200, 0), new RelativeTime(
						PING_HANDLER_RELEASE_PERIOD_IN_MS, 0));
		StorageParameters clientHandlerStorageParameters = new StorageParameters(
				PING_HANDLER_BACKING_STORE_SIZE_IN_BYTES, null, 0, 0);

		PingHandler ping = new PingHandler(
				clientHandlerPriorityParameters,
				clientHandlerPeriodicParameters,
				clientHandlerStorageParameters,
				PING_HANDLER_SCOPE_SIZE_IN_BYTES, 
				"[ping]: ",
				ImmortalEntry.manager);

		ping.register();
	}

}
