package libs.examples.libcsp.csp.core;

import libs.examples.libcsp.csp.ImmortalEntry;
import libs.examples.libcsp.csp.Socket;
import libs.examples.libcsp.csp.util.ConnectionQueue;
import libs.examples.libcsp.csp.util.IDispose;

public class SocketCore implements IDispose, Socket {
	
	public byte port;
	public ConnectionQueue connections;
	
	public SocketCore(byte connectionsCapacity) {
		this.connections = new ConnectionQueue(connectionsCapacity);
	}

	public ConnectionCore accept(int timeout) {
		return connections.dequeue(timeout);
	}
	
	public synchronized void processConnection(ConnectionCore connection) {
		if(port != -1) {
			connections.enqueue(connection);
		}
	}
	
	public synchronized void close() {
		if(ImmortalEntry.portTable[port].isOpen) {
			ImmortalEntry.portTable[port].isOpen = false;
			ImmortalEntry.portTable[port].socket = null;
			dispose();
		}	
	}
	
	@Override
	public void dispose() {
		this.port = -1;
		this.connections.reset();
		ImmortalEntry.resourcePool.putSocket(this);
	}
}