package libs.examples.libcsp.csp;

import javax.realtime.AbsoluteTime;
import javax.realtime.Clock;
import javax.realtime.RawMemory;
import javax.safetycritical.Terminal;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import libs.examples.libcsp.csp.core.Node;
import libs.examples.libcsp.csp.core.PacketCore;
import libs.examples.libcsp.csp.core.Port;
import libs.examples.libcsp.csp.core.ResourcePool;
import libs.examples.libcsp.csp.handlers.RouteHandler;
import libs.examples.libcsp.csp.interfaces.InterfaceI2C_A;
import libs.examples.libcsp.csp.interfaces.InterfaceI2C_B;
import libs.examples.libcsp.csp.interfaces.InterfaceLoopback;
import libs.examples.libcsp.csp.util.Const;
import libs.examples.libcsp.csp.util.Queue;
import libs.safeutil.SafeVector;

import com.jopdesign.io.I2CFactory;

import csp.CRC32;

/**
 * This class defines the mandatory shared structures needed for the CSP
 * implementation. Additional shared entries that may be application specific
 * can be added by extending this class.
 * 
 * @author Juan Rios
 * 
 */
public abstract class ImmortalEntry {

	/* Real time clock object*/
	static AbsoluteTime clk = null;
	
	/* Functionality to log events with time stamps */
	public static Logger log = null;
	
	/* Number of currently logged events */
	public static int eventsLogged = 0;
	
	/* Provides functionality to select which log entry to display */
	public static DumpLog dumpLog = null;

	/* The terminal object (stdout) */
	public static Terminal term = null;
	
	/* Local CSP node address */ 
	public static final int NODE_ADDRESS = 0xA;
	
	/* Timeouts */
	public static final byte TIMEOUT_NONE = -1;
	public static final byte TIMEOUT_SINGLE_ATTEMPT = 0;
	
	/* Special addresses and ports */
	public static final byte ADDRESS_BROADCAST = 31;
	public static final byte PORT_ANY = Const.MAX_BIND_PORTS;

	/* Resource pool containing Packet, Connection and Socket pools */
	public static ResourcePool resourcePool;
	
	/* Structure to store the number of used outgoing ports */
	public static short outgoingPorts;

	public static Node[] routeTable;
	public static Port[] portTable;
	public static SafeVector<PacketCore> packetsToBeProcessed;
	
//	public static Queue<PacketCore> packetsToBeProcessed;
	
	public static CRC32 crc32 = null;
	
	public static InterfaceI2C_A interfaceI2C_A = null;
	public static InterfaceI2C_B interfaceI2C_B = null;
	public static InterfaceLoopback interfaceLoopback = null;
	
	public static CSPManager manager = null;


	public static void setup() {
		
		clk = Clock.getRealtimeClock().getTime();
		log = new Logger();
		dumpLog = new DumpLog();

		term = Terminal.getTerminal();
		term.writeln("Startup...");

		/* Initialize resource pools */
		initPools();
		
		/* Initialize route table */
		initializeRouteTable();
		
		/* Initialize port table*/
		initializePortTable();

		/* Initialize the router's packet queue */
//		packetsToBeProcessed = new Queue<PacketCore>(Const.DEFAULT_PACKET_QUEUE_SIZE_ROUTING);
		packetsToBeProcessed = new SafeVector<PacketCore>(Const.DEFAULT_PACKET_QUEUE_SIZE_ROUTING);
		
		/* If CRC is used, create a new CRC object to provide the functionality */
		if (Const.CSP_USE_CRC32) {
			crc32 = new CRC32();
		}
		
		/* Get the loopback interface object */
		interfaceLoopback = InterfaceLoopback.getInterface();
		
		/* Get the I2C interface objects */
		interfaceI2C_A = InterfaceI2C_A.getInterface();
		interfaceI2C_B = InterfaceI2C_B.getInterface();
		
		manager = new CSPManager();

		/* Just a message to display the initial parameters of the application */ 
		dumpInitialParameters();
		
		term.writeln("Setup ok...");

	}
	
	/**
	 * Initializes socket, connection and packet resources to default sizes.
	 * These sizes cannot be changed at run-time.
	 */
	@SCJAllowed(Level.LEVEL_1)
	public static void initPools() {
		initPools(Const.DEFAULT_MAX_SOCKETS,
				Const.DEFAULT_MAX_CONNECTION_PER_SOCKET,
				Const.DEFAULT_MAX_CONNECTIONS,
				Const.DEFAULT_PACKET_QUEUE_SIZE_PER_CONNECTION,
				Const.DEFAULT_MAX_PACKETS);
	}

	/**
	 * Initializes socket, connection and packet resources to specified sizes.
	 * These sizes cannot be changed at run-time.
	 * 
	 * @param socketsCapacity
	 *            Maximum amount of sockets
	 * @param connectionsPerSocketCapacity
	 *            Maximum amount of connections per socket (must be less than
	 *            connectionsCapacity)
	 * @param connectionsCapacity
	 *            Maximum amount of connections
	 * @param packetsPerConnectionCapacity
	 *            Maximum amount of packets per connection (must be less than
	 *            packetsCapacity)
	 * @param packetsCapacity
	 *            Maximum amount of packets
	 */
	@SCJAllowed(Level.LEVEL_1)
	private static void initPools(int socketsCapacity,
			int connectionsPerSocketCapacity, int connectionsCapacity,
			int packetsPerConnectionCapacity, int packetsCapacity) {

		ImmortalEntry.resourcePool = new ResourcePool((byte) socketsCapacity,
				(byte) connectionsPerSocketCapacity,
				(byte) connectionsCapacity,
				(byte) packetsPerConnectionCapacity, (byte) packetsCapacity);
	}
	
	private static void initializeRouteTable() {
		routeTable = new Node[Const.MAX_NETWORK_HOSTS];
		for (byte i = 0; i < Const.MAX_NETWORK_HOSTS; i++) {
			routeTable[i] = new Node();
		}
	}

	private static void initializePortTable() {
		portTable = new Port[Const.MAX_PORTS];
		for (byte i = 0; i < Const.MAX_PORTS; i++) {
			portTable[i] = new Port();
		}
	}

	public static class DumpLog implements Runnable {

		public int logEntry = 0;
		public int selector = 0;

		@Override
		public void run() {
			
			switch (selector) {
			case 0:
				log.printEntry(logEntry);
				break;
				
			case 1:
				break;

			default:
				break;
			}
			
		}

	}
	
	public static void dumpInitialParameters(){
		term.writeln("Available packets: "+ ImmortalEntry.resourcePool.packets.maxObjects());
		term.writeln("Available Connections: "+ ImmortalEntry.resourcePool.connections.capacity);
		term.writeln("Available Sockets: "+ ImmortalEntry.resourcePool.sockets.capacity);
		term.writeln("Maximum packet payload (bytes): "+ Const.MAX_PAYLOAD_SIZE_IN_BYTES);
	}

}