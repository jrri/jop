package csp.test;

import joprt.RtThread;

import com.jopdesign.io.I2Cport;

import csp.Buffer;
import csp.Connection;
import csp.ImmortalEntry;
import csp.Services;

public class Watchdog extends RtThread {

	public Watchdog(int prio, int us) {
		super(prio, us);
	}

	Connection conn;
	public int[] slaves;
	int source;

	public void connBind(Connection conn) {

		this.conn = conn;

	}

	@Override
	public void run() {

		for (;;) {

			for (int i = 0; i < TestWatchdog.NUM_SLAVES; i++) {

				conn.destination = slaves[i];

//				conn.tx_port.masterTX();

				// Send CSP ping packet
				Services.sendPacket(conn, null);

				// Can this instruction execute fast enough to avoid corruption
				// of data in the RX buffer?
				conn.tx_port.slaveMode();

				sleepMs(TestWatchdog.TIMEOUT);

				if (((conn.tx_port.status & I2Cport.DATA_RDY)) == 0) {

					System.out.println("Timeout " + conn.destination);
					// Slave not responding, take actions

				} else {

					// Get one free CSPbuffer
					Buffer buffer = ImmortalEntry.bufferPool.getCSPbuffer();

					// Read the data in the RX buffer
					Services.receivePacket(conn, buffer);

					// Process header
					conn.prio = buffer.header[0] >>> 6;

					source = (buffer.header[0] >>> 1) & (0x1F);

					// destination = ( (buffer.header[0] << 4) & 0x10 ) |
					// ((buffer.header[1] >>> 4) & 0x0F);

					// dest_port = ( (buffer.header[1] << 2) & 0x3C ) |
					// (buffer.header[2] >>> 6);

					// source_port = buffer.header[2] & 0x3F;

					// res_flags = buffer.header[3];

					// Print received data
					System.out.println("Reply from " + source);

					ImmortalEntry.bufferPool.freeCSPbuffer(buffer);

				}
			}

			waitForNextPeriod();

		}

		// Should never reach this part

	}

}
