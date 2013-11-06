package libs.examples.libcsp.csp.app.clientserver;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import libs.examples.libcsp.csp.CSPManager;
import libs.examples.libcsp.csp.Connection;
import libs.examples.libcsp.csp.ImmortalEntry;
import libs.examples.libcsp.csp.Packet;
import libs.examples.libcsp.csp.Socket;
import libs.examples.libcsp.csp.core.ConnectionCore;
import libs.examples.libcsp.csp.core.PacketCore;

public class ServerHandler extends PeriodicEventHandler {

	private CSPManager manager;
	private Socket socket;
	private Connection connection;
	
	public ServerHandler(PriorityParameters priority,
			PeriodicParameters parameters, StorageParameters scp, long scopeSize, CSPManager manager) {
		super(priority, parameters, scp, scopeSize);
		
		this.manager = manager;
		socket =  manager.createSocket(12, null);
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public void handleAsyncEvent() {
		connection = socket.accept(ImmortalEntry.TIMEOUT_SINGLE_ATTEMPT);
		
		if (connection != null) {
			Packet p = connection.read(ImmortalEntry.TIMEOUT_SINGLE_ATTEMPT);
			
			char data = (char) p.readContent();
			
			Packet response = manager.createPacket();

			switch(data) {
			case 'A':
				response.setContent((int)'X');
				break;
			case 'B':
				response.setContent((int)'Y'); 
				break;
			}
			
			connection.send(response);
			
			connection.close();
		}
	}
}
