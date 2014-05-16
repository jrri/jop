package csp.scj.watchdog;

import javax.realtime.RawIntArray;
import javax.realtime.RawMemory;

import csp.IODevice;

public class I2CBusController_ implements IODevice{

	int BASE_ADD;

	static final int CONTROL_OFF = 0;
	static final int STATUS_OFF = 1;
	static final int DEV_ADD_OFF = 2;
	static final int MSG_SIZE_OFF = 3;
	static final int TX_FIFO_OFF = 4;
	static final int RX_FIFO_OFF = 5;
	static final int TH_OFF = 6;
	static final int TL_OFF = 7;
	static final int TX_OCCU_OFF = 8;
	static final int RX_OCCU_OFF = 9;

	// TX/RX buffer size in bytes. This size is the size of the FIFO's in
	// the hardware controller. To modify it you should modify the VHDL files.
	public static final int BUFFER_SIZE = 32;

	// Status constants
	public static final int TBUF_ERR = 0x00000040;
	public static final int RBUF_ERR = 0x00000020;
	public static final int ACK_ERR = 0x00000010;
	public static final int HDR_ERR = 0x00000008;
	public static final int BUS_BUSY = 0x00000004;
	public static final int STOP_STAT = 0x00000002;
	public static final int DATA_RDY = 0x00000001;

	public static final int OCCU_RD = 0xFFFF0000;
	public static final int OCCU_WR = 0x0000FFFF;

	// Control constants
	public static final int TX_FLUSH = 0x00000080;
	public static final int RX_FLUSH = 0x00000040;
	public static final int ACK = 0x00000020;
	public static final int STRT = 0x00000010;
	public static final int STOP = 0x00000008;
	public static final int MASL = 0x00000004;
	public static final int RSTA = 0x00000002;
	public static final int ENABLE = 0x00000001;

	public static final int CLEAR_STRT = 0xFFFFFFEF;

	// Configuration constants
	public static final int MASTER = ENABLE | MASL;
	public static final int SLAVE = ENABLE;

	public static final int NOT_FLUSH = 0xFFFFFF7F;

	// Count base for SCL timings, adjust according to
	// uP clock frequency.
	public static final int CNT_BASE = 10;

	// These constants define the timings of the SCL signal
	public static final int T_HOLD_START = 5 * CNT_BASE - 1;
	public static final int T_RSTART = 5 * CNT_BASE - 1;
	public static final int T_LOW = 5 * CNT_BASE - 1;
	public static final int T_HIGH = 5 * CNT_BASE - 1;

	public static final int T_HALF_HIGH = 2 * CNT_BASE - 1;
	public static final int T_SUSTO = 4 * CNT_BASE - 1;
	public static final int T_WAIT = 8 * CNT_BASE;

	// The following are the accessor objects
	
	RawIntArray i2cPort;

	public I2CBusController_(int baseAddress) {

		this.BASE_ADD = baseAddress;

		i2cPort = RawMemory.createRawIntArrayInstance(RawMemory.IO_MEM_MAPPED,
				BASE_ADD, 0);
		
	}

	public void setControl(int i) {
		i2cPort.put(i,CONTROL_OFF);
	}
	
	public int readControl() {
		return i2cPort.get(CONTROL_OFF);
	}

	public int readStatus() {
		return i2cPort.get(STATUS_OFF);
	}

	public int readBuff() {
		return i2cPort.get(RX_FIFO_OFF);
	}

	/**
	 * Load the initial configuration to the I2C device. It loads the device
	 * address and the timing constants.
	 * 
	 * @param devAdd
	 *            Device address. This is the address used when addressed as
	 *            slave.
	 * @param isMaster
	 *            When true, the device works in master mode, otherwise it works
	 *            in slave mode.
	 */
	final public void initialize(int devAdd, boolean isMaster) {

		i2cPort.put(devAdd, DEV_ADD_OFF);

		if (isMaster) {
			i2cPort.put(MASTER, CONTROL_OFF);
		} else {
			i2cPort.put(SLAVE, CONTROL_OFF);
		}

		int temp = (T_HOLD_START << 24) + (T_RSTART << 16) + (T_LOW << 8)
				+ (T_HIGH);
		i2cPort.put(temp, TH_OFF);

		temp = (T_HALF_HIGH << 24) + (T_SUSTO << 16) + (T_WAIT << 8) + (0);
		i2cPort.put(temp, TL_OFF);

	}
	
	/**
	 * Set device in slave mode without changing the device address
	 */
	public void slaveMode(){
		i2cPort.put(SLAVE,CONTROL_OFF);
	}

	/**
	 * Clear the transmit buffer.
	 */
	public void flushTXBuff() {

		int controlOld = i2cPort.get(CONTROL_OFF);
		i2cPort.put(controlOld | TX_FLUSH, CONTROL_OFF);
		i2cPort.put(controlOld, CONTROL_OFF);

	}

	/**
	 * Clear the receive buffer.
	 */
	public void flushRXBuff() {

		int controlOld = i2cPort.get(CONTROL_OFF);
		i2cPort.put(controlOld | RX_FLUSH, CONTROL_OFF);
		i2cPort.put(controlOld, CONTROL_OFF);

	}

	/**
	 * Write a single byte in the transmit buffer.
	 * 
	 * @param data
	 *            Byte to be written to the buffer.
	 */
	public void writeBuff(int data) {

		if (tBuffSpace(data)) {
			i2cPort.put(data, TX_FIFO_OFF);
		} else {
			System.out.println("Not enough space in buffer");
		}

	}

	/**
	 * Write the elements of the data array into the transmit buffer
	 * 
	 * @param dataArray
	 *            Array containing the elements to be written in the buffer.
	 */
	public void writeBuffer(int[] dataArray) {

		if (tBuffSpace(dataArray)) {
			for (int i = 0; i < dataArray.length; i++) {
				i2cPort.put(dataArray[i], TX_FIFO_OFF);
			}
		} else {
			System.out.println("Not enough space in buffer");
		}

	}

	/**
	 * Check that there is still space available in buffer.
	 * 
	 * @return True if there is space available to store the required data.
	 */
	public boolean tBuffSpace(int[] dataArray) {

		int occu = (i2cPort.get(TX_OCCU_OFF) & OCCU_WR);
		int space = BUFFER_SIZE - occu;

		return (dataArray.length <= space);

	}

	/**
	 * Check that there is still space available in buffer.
	 * 
	 * @return True if there is space available to store the required data.
	 */
	public boolean tBuffSpace(int data) {

		int occu = (i2cPort.get(TX_OCCU_OFF) & OCCU_WR);
		int space = BUFFER_SIZE - occu;

		return (space >= 1);

	}

	/**
	 * Read ALL the bytes in the transmit buffer. Data is stored in the data
	 * array. If buffer is empty, the array is filled with zeros.
	 * 
	 * @param data
	 */
	public void readBuffer(int[] data) {

		// Read all data in buffer
		int occu = (i2cPort.get(TX_OCCU_OFF) & OCCU_RD) >>> 16;

		for (int i = 0; i < occu; i++) {
			data[i] = i2cPort.get(RX_FIFO_OFF);
		}
	}

	/**
	 * Write "size" bytes to the slave identified by the address in the first
	 * byte of the transmit buffer.This method is particularly useful if you
	 * have previously written data to the transmit buffer. It assumes that the
	 * first seven bits of the byte to whom the read pointer in the transmit
	 * buffer points to is the slave address. The LSB of this same byte must be
	 * zero. This is a non-blocking operation. Once the transmission is started
	 * the hardware will take care of finishing it and clearing the BUS_BUSY
	 * flag in the status register.
	 * 
	 * @param size
	 *            How many bytes will be written to the slave.
	 */
	public void write(int size) {

		// Clear STRT bit in case there was a previous transaction
		i2cPort.put(i2cPort.get(CONTROL_OFF) & CLEAR_STRT, CONTROL_OFF);

		if ((i2cPort.get(STATUS_OFF) & BUS_BUSY) == 0) {
			// Set I2C to master
			i2cPort.put(MASTER, CONTROL_OFF);

			if (size > 1) {
				i2cPort.put(size + 1, MSG_SIZE_OFF);
			} else {
				i2cPort.put(1, MSG_SIZE_OFF);
			}

			// Initiate transmission, set STRT bit = 1
			i2cPort.put(i2cPort.get(CONTROL_OFF) | STRT, CONTROL_OFF);

		} else {
			System.out.println("Can't start transmission, bus busy");
		}

	}

	/**
	 * Write one byte to the slave identified with slAddress address. This is a
	 * non-blocking operation. Once the transmission is started the hardware
	 * will take care of finishing it and clearing the BUS_BUSY flag in the
	 * status register.
	 * 
	 * @param slAddress
	 *            Address of the slave target.
	 * @param data
	 *            Byte to be written.
	 * 
	 */
	public void write(int slAddress, int data) {

		// Clear STRT bit in case there was a previous transaction
		i2cPort.put(i2cPort.get(CONTROL_OFF) & CLEAR_STRT, CONTROL_OFF);
		
		// Start with an empty buffer
		flushTXBuff();

		if ((i2cPort.get(STATUS_OFF) & BUS_BUSY) == 0) {
			// To write, the LSB of the address is set to zero
			// and the first position of buffer is used to store the
			// address of the slave we wish to communicate with.
			i2cPort.put(slAddress * 2, TX_FIFO_OFF);

			// Write data to tx buffer
			i2cPort.put(data, TX_FIFO_OFF);
		}

		// Set I2C to master
		i2cPort.put(MASTER, CONTROL_OFF);

		i2cPort.put(1, MSG_SIZE_OFF);

		// Initiate transmission, set STRT bit = 1
		i2cPort.put(i2cPort.get(CONTROL_OFF) | STRT, CONTROL_OFF);

	}

	/**
	 * Write "N" bytes to the slave identified with slAddress address. "N" is
	 * the size of the "data" array. This is a non-blocking operation. Once the
	 * transmission is started the hardware will take care of finishing it and
	 * clearing the BUS_BUSY flag in the status register.
	 * 
	 * @param slAddress
	 *            Address of the slave target.
	 * @param data
	 *            Array of data to be written.
	 * 
	 */
	public void write(int slAddress, int[] data) {

		// Clear STRT bit in case there was a previous transaction
		i2cPort.put(i2cPort.get(CONTROL_OFF) & CLEAR_STRT, CONTROL_OFF);
		
		// Start with an empty buffer
		flushTXBuff();

		if ((i2cPort.get(STATUS_OFF) & BUS_BUSY) == 0) {
			// To write, the LSB of the address is set to zero
			// and the first position of buffer is used to store the
			// address of the slave we wish to communicate with.
			i2cPort.put(slAddress * 2, TX_FIFO_OFF);

			// Write data to tx buffer
			if (data.length > BUFFER_SIZE - 1) {
				System.out.println("Data bigger than buffer size");
			} else {
				for (int i = 0; i < data.length; i++) {
					i2cPort.put(data[i], TX_FIFO_OFF);
				}
			}

			// Set I2C to master
			i2cPort.put(MASTER,CONTROL_OFF);

			i2cPort.put(data.length + 1, MSG_SIZE_OFF);

			// Initiate transmission, set STRT bit = 1
			i2cPort.put(i2cPort.get(CONTROL_OFF) | STRT, CONTROL_OFF);

		} else {
			System.out.println("Can't start transmission, bus busy");
		}

	}

	/**
	 * This is a very common function in I2C devices, where the master writes
	 * one byte of data to a slave, usually to set a base address to read from,
	 * and then it performs a read operation. This is a non-blocking operation.
	 * The method will return after the read operation is initiated. The
	 * hardware will take care of finishing, clearing the BUS_BUSY flag, and
	 * setting the DATA_RDY flag in the status register.
	 * 
	 * @param slAddress
	 *            Address of the slave target.
	 * @param data
	 *            Base address of slave.
	 * 
	 * @param readSize
	 *            Size in bytes of the read transaction.
	 * 
	 */
	public void writeRead(int slAddress, int data, int readSize) {

		// Clear STRT bit in case there was a previous transaction
		i2cPort.put(i2cPort.get(CONTROL_OFF) & CLEAR_STRT, CONTROL_OFF);
		
		// Start with an empty buffer
		flushTXBuff();

		if ((i2cPort.get(STATUS_OFF) & BUS_BUSY) == 0) {
			// To write, the LSB of the address is set to zero
			// and the first position of buffer is used to store the
			// address of the slave we wish to communicate with.
			i2cPort.put(slAddress * 2, TX_FIFO_OFF);
			i2cPort.put(data, TX_FIFO_OFF);
			i2cPort.put(slAddress * 2 + 1, TX_FIFO_OFF);

			// Set I2C to master and initiate transmission
			i2cPort.put(MASTER | STRT, CONTROL_OFF);

			// It is safe to set the repeated start bit here since we wish
			// to transmit only one byte. Setting the repeated start bit
			// takes effect in the next WAIT_ACK or SEND_ACK state
			i2cPort.put(i2cPort.get(CONTROL_OFF) | RSTA, CONTROL_OFF);

			// Message size is reduced by one since byte 0 counts as first
			// received byte
			i2cPort.put(readSize - 1, MSG_SIZE_OFF);

			// Now we need to wait until the controller leaves the first
			// ACK_HEADER state to set the master rx mode

		} else {
			System.out.println("Can't start transmission, bus busy");
		}

	}

	/**
	 * Read "N" bytes from the slave identified with slAddress address. "N" is
	 * specified before starting the read operation. This is a non-blocking
	 * operation, once it is started, the hardware will take care of finishing
	 * it, clearing the BUS_BUSY flag and setting the DATA_RDY flag in the
	 * status register. Data in the receive buffer has to be moved explicitly to
	 * e.g. an array for its later use.
	 * 
	 * @param slAddress
	 *            Target slave address.
	 * @param readSize
	 *            Size in bytes of the read transaction.
	 */
	public void read(int slAddress, int readSize) {

		// Clear STRT bit in case there was a previous transaction
		i2cPort.put(i2cPort.get(CONTROL_OFF) & CLEAR_STRT, CONTROL_OFF);
		
		// Start with an empty buffer
		flushTXBuff();
		flushRXBuff();

		// A read operation starts by transmitting the slave address.
		// Add 1 to indicate a read transaction.
		i2cPort.put(slAddress * 2 + 1, TX_FIFO_OFF);

		i2cPort.put(MASTER, CONTROL_OFF);

		i2cPort.put(readSize - 1, MSG_SIZE_OFF);

		i2cPort.put(i2cPort.get(CONTROL_OFF) | STRT, CONTROL_OFF);

	}

}
