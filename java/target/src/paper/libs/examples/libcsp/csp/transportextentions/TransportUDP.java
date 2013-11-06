package libs.examples.libcsp.csp.transportextentions;

import libs.examples.libcsp.csp.core.ConnectionCore;
import libs.examples.libcsp.csp.core.PacketCore;

public class TransportUDP implements ITransportExtension {

	@Override
	public void deliverPacket(ConnectionCore connection, PacketCore packet) {
		connection.processPacket(packet);
	}
}