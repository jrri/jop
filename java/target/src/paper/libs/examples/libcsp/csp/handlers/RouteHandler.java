package libs.examples.libcsp.csp.handlers;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import libs.examples.libcsp.csp.CSPManager;
import libs.examples.libcsp.csp.ImmortalEntry;
import libs.examples.libcsp.csp.core.ConnectionCore;
import libs.examples.libcsp.csp.core.Node;
import libs.examples.libcsp.csp.core.PacketCore;
import libs.examples.libcsp.csp.core.Port;
import libs.examples.libcsp.csp.transportextentions.ITransportExtension;
import libs.examples.libcsp.csp.transportextentions.TransportUDP;

public class RouteHandler extends PeriodicEventHandler {

	// public static Node[] routeTable;
	// public static Port[] portTable;
	// public static Queue<PacketCore> packetsToBeProcessed;

	private TransportUDP transportExtensionUDP;

	public RouteHandler(PriorityParameters priority,
			PeriodicParameters parameters, StorageParameters scp, long scopeSize) {
		super(priority, parameters, scp, scopeSize, "[router]");

		transportExtensionUDP = new TransportUDP();

	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	public void handleAsyncEvent() {

		try {
			PacketCore packet = ImmortalEntry.packetsToBeProcessed
					.remove(0);
			// (ImmortalEntry.TIMEOUT_SINGLE_ATTEMPT);

			// if (packet != null) {
			byte packetDST = packet.getDST();
			System.out.println("[router] packet " + packet.getSRC() + ","
					+ packet.getSPORT() + "," + packet.getDST() + ","
					+ packet.getDPORT() + ":" + packet.readContent());

			// TODO: What if I'm sending a Broadcast packet? The code below wont
			// let it to be transmitted. How is a broadcast send with I2C
			// anyway.There is a general call address (0x00) to which every
			// slave should react
			/* The packet is for me */
			if (packetDST == CSPManager.nodeAddress
					|| packetDST == ImmortalEntry.ADDRESS_BROADCAST) {

				/*
				 * Check for an existing connection that should receive the
				 * packet
				 */
				int connectionIdentifier = ConnectionCore
						.getConnectionIdFromPacketHeader(packet);
				ConnectionCore packetConnection = ImmortalEntry.resourcePool
						.getGlobalConnection(connectionIdentifier);

				/* If its the first packet with no existing connection (server) */
				if (packetConnection == null) {

					/* Extract the port from the packet header */
					Port packetDPORT = ImmortalEntry.portTable[packet
							.getDPORT()];
					if (!packetDPORT.isOpen) {
						packetDPORT = ImmortalEntry.portTable[ImmortalEntry.PORT_ANY];
					}

					/* If a socket listens on the port (server) */
					if (packetDPORT.isOpen) {
						packetConnection = ImmortalEntry.resourcePool
								.getConnection(ImmortalEntry.TIMEOUT_SINGLE_ATTEMPT);
						if (packetConnection != null) {
							/*
							 * New connection established - Set connection id
							 * (reverse src, dst and sport, dport) and enqueue
							 * connection in the sockets connection queue
							 */
							try {
								packetConnection.setId(packet.getDST(),
										packet.getDPORT(), packet.getSRC(),
										packet.getSPORT());
								packetConnection.isOpen = true;
								packetDPORT.socket
										.processConnection(packetConnection);
							} catch (NullPointerException e) {
								packetConnection.dispose();
								packetConnection = null;
							}
						}
					}
				}

				/*
				 * Check if we have a connection - then deliver or drop the
				 * packet
				 */
				if (packetConnection != null) {
					ITransportExtension transportExtension = getTransportExtensionForPacket(packet);
					transportExtension.deliverPacket(packetConnection, packet);
				} else {
					ImmortalEntry.resourcePool.putPacket(packet);
					// packet.dispose();
				}
			} else {
				/*
				 * The packet is not for me - send it to the destination node
				 * through the correct interface
				 */
				Node packetDstNode = ImmortalEntry.routeTable[packetDST];
				packetDstNode.protocolInterface.transmitPacket(packet);
				
			}
		} catch (ArrayIndexOutOfBoundsException e) {

		} 
	}

	private ITransportExtension getTransportExtensionForPacket(PacketCore packet) {
		/*
		 * Inspect header and return the correct transport extension - UDP or
		 * RDP
		 */
		// TODO: Is this a todo thing?
		return transportExtensionUDP;
	}

	@Override
	@SCJAllowed(Level.SUPPORT)
	@SCJRestricted(phase = Phase.CLEANUP)
	public void cleanUp() {
		// TODO Auto-generated method stub
		System.out.println("cleanup router handler");
	}

}