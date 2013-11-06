package libcsp.csp.app.ping;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import libcsp.csp.CSPManager;
import libcsp.csp.Connection;
import libcsp.csp.ImmortalEntry;
import libcsp.csp.Packet;
import libcsp.csp.util.Const;

public class PingHandler extends PeriodicEventHandler {

	private CSPManager cspManager;
	private int data;
	int cnt = 0;

	public PingHandler(PriorityParameters priority,
			PeriodicParameters parameters, StorageParameters scp,
			long scopeSize, String name, CSPManager manager) {
		super(priority, parameters, scp, scopeSize, name);

		this.cspManager = manager;
		this.data = 0;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public void handleAsyncEvent() {
		System.out.println(getName());
		cnt++;
		if (cnt == 3){
			Mission.getCurrentMission().requestTermination();
		}
		Connection conn = cspManager.createConnection(1, Const.CSP_PING,
				ImmortalEntry.TIMEOUT_NONE, null);

		if (conn != null) {
			Packet p = cspManager.createPacket();
			p.setContent(data);
			data++;

			conn.send(p);
			
			Packet response = conn.read(400);

			if (response != null)
				System.out.println("Response: " + response.readContent());
			else
				System.out.println("Ping timeout");

			conn.close();
		}

	}
	
	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.CLEANUP)
	public void cleanUp() {
		// TODO Auto-generated method stub
		System.out.println("cleanup ping handler");
	}

}
