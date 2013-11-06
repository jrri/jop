package libs.examples.libcsp.csp.interfaces;

import libs.examples.libcsp.csp.ImmortalEntry;
import libs.examples.libcsp.csp.core.PacketCore;

public class InterfaceLoopback implements IMACProtocol {

	private static InterfaceLoopback instance;
	
	private InterfaceLoopback() { }
	
	public static InterfaceLoopback getInterface() {
		if(instance == null) {
			instance = new InterfaceLoopback();
		}
		
		return instance;
	}
	
	@Override
	public void initialize(int MACAddress) { }

	@Override
	public void transmitPacket(PacketCore packet) {
//		ImmortalEntry.packetsToBeProcessed.enqueue(packet);
		ImmortalEntry.packetsToBeProcessed.add(packet);
	}

	@Override
	public void receiveFrame() { }
}