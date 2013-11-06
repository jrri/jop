package libs.examples.libcsp.csp.interfaces;

import libs.examples.libcsp.csp.core.PacketCore;

public interface IMACProtocol {
	
	public void initialize(int MACAddress);
	
	public void transmitPacket(PacketCore packet);
	
	public void receiveFrame();
}
