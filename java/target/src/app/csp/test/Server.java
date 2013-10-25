package csp.test;

import com.jopdesign.io.I2Cport;

import joprt.RtThread;
import csp.Buffer;
import csp.Connection;
import csp.ImmortalEntry;
import csp.Services;

public class Server extends RtThread {



public Server(int prio, int us) {
		super(prio, us);
	}


	Connection conn;
	public int[] data;

	public void connBind(Connection conn){

		this.conn = conn;

	}

	@Override
	public void run() {

		for(;;){

			conn.rx_port.flushTXBuff();

		// Wait until we have valid data in the rx buffer
		while (((conn.rx_port.status & I2Cport.DATA_RDY)) == 0);

		// Get one free CSPbuffer
		Buffer buffer = ImmortalEntry.bufferPool.getCSPbuffer();

		// Read the data in the RX buffer
		Services.receivePacket(conn, buffer);

		// Process header and set missing connection parameters
		conn.prio = buffer.header[0] >>> 6;

		// Swap source and destination addresses
		conn.destination = (buffer.header[0] >>> 1) & (0x1F);
		conn.source = ( (buffer.header[0] << 4) & 0x10 ) | ((buffer.header[1] >>> 4) & 0x0F);

		// Swap source and destination ports
		conn.source_port = ( (buffer.header[1] << 2) & 0x3C ) | (buffer.header[2] >>> 6);
		conn.dest_port = buffer.header[2] & 0x3F;

		conn.res_flags = buffer.header[3];

		System.out.println("Received from " + conn.destination);

		// Change to master mode, flush buffers
//		conn.rx_port.flushFifo();
//		conn.rx_port.masterTX();

		// Send CSP packet
		Services.sendPacket(conn, buffer.data);

		while((conn.tx_port.status & I2Cport.BUS_BUSY) == 1);

		// Free RX buffer
		ImmortalEntry.bufferPool.freeCSPbuffer(buffer);

		// Can this instruction execute fast enough to avoid corruption of
		// data in the RX buffer?
		conn.rx_port.flushTXBuff();
		conn.rx_port.slaveMode();

		waitForNextPeriod();

	}

	// Should never reach this part

}


}
