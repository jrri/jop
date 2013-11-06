package libs.examples.libcsp.csp.transportextentions;

import libs.examples.libcsp.csp.core.ConnectionCore;
import libs.examples.libcsp.csp.core.PacketCore;

public interface ITransportExtension {
	public void deliverPacket(ConnectionCore connection, PacketCore packet);
}
