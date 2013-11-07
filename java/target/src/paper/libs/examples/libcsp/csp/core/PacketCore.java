package libs.examples.libcsp.csp.core;

import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import libs.examples.libcsp.csp.ImmortalEntry;
import libs.examples.libcsp.csp.Packet;
import libs.examples.libcsp.csp.util.IDispose;
import libs.safeutil.extras.ObjectPool;
import libs.safeutil.extras.PoolObject;
import libs.safeutil.extras.PoolObjectFactory;

/**
 * Specific implementation of a CSP Packet. It contains operations to get and
 * set CSP packet fields.
 * 
 * The CSP header and data are stored in a 32 bit integer each. This is to
 * reduce the memory footprint as opposed to storing each element of a CSP
 * packet in a separate data type.
 * 
 * @author Mikkel Todberg, Jeppe Lund Andersen
 * 
 */
public class PacketCore implements PoolObject, Packet {

	/* Header masks */
	public final static int MASK_CRC = 0x00000001;
	public final static int MASK_RDP = 0x00000002;
	public final static int MASK_XTEA = 0x0000004;
	public final static int MASK_HMAC = 0x00000008;
	public final static int MASK_RES = 0x000000F0;
	public final static int MASK_SPORT = 0x00003F00;
	public final static int MASK_DPORT = 0x000FC000;
	public final static int MASK_DST = 0x01F00000;
	public final static int MASK_SRC = 0x3E000000;
	public final static int MASK_PRI = 0xC0000000;

	/* Packet */
	int header;
	int data;

	public PacketCore(int header, int data) {
		this.header = header;
		this.data = data;
	}

	/* Packet operations */
	public byte getCRC() {
		return (byte) (header & MASK_CRC);
	}

	public byte getRDP() {
		return (byte) ((header & MASK_RDP) >>> 1);
	}

	public byte getXTEA() {
		return (byte) ((header & MASK_XTEA) >>> 2);
	}

	public byte getHMAC() {
		return (byte) ((header & MASK_HMAC) >>> 3);
	}

	public byte getRES() {
		return (byte) ((header & MASK_RES) >>> 4);
	}

	public byte getSPORT() {
		return (byte) ((header & MASK_SPORT) >>> 8);
	}

	public byte getDPORT() {
		return (byte) ((header & MASK_DPORT) >>> 14);
	}

	public byte getDST() {
		return (byte) ((header & MASK_DST) >>> 20);
	}

	public byte getSRC() {
		return (byte) ((header & MASK_SRC) >>> 25);
	}

	public byte getPRI() {
		return (byte) ((header & MASK_PRI) >>> 30);
	}

	public void setCRC(byte value) {
		header &= ~(MASK_CRC);
		header |= (int) value;
	}

	public void setRDP(byte value) {
		header &= ~(MASK_RDP);
		header |= (int) value << 1;
	}

	public void setXTEA(byte value) {
		header &= ~(MASK_XTEA);
		header |= (int) value << 2;
	}

	public void setHMAC(byte value) {
		header &= ~(MASK_HMAC);
		header |= (int) value << 3;
	}

	public void setRES(byte value) {
		header &= ~(MASK_HMAC);
		header |= (int) value << 4;
	}

	public void setSPORT(byte value) {
		header &= ~(MASK_SPORT);
		header |= (int) value << 8;
	}

	public void setDPORT(byte value) {
		header &= ~(MASK_DPORT);
		header |= (int) value << 14;
	}

	public void setDST(byte value) {
		header &= ~(MASK_DST);
		header |= (int) value << 20;
	}

	public void setSRC(byte value) {
		header &= ~(MASK_SRC);
		header |= (int) value << 25;
	}

	public void setPRI(byte value) {
		header &= ~(MASK_PRI);
		header |= (int) value << 30;
	}

	/* Implemented methods from Packet interface */
	ObjectPool<PoolObject> pool;
	boolean isFree = true;

	@Override
	@SCJAllowed(Level.LEVEL_1)
	public void setContent(int data) {
		this.data = data;
	}
	
	public void setHeader(int data) {
		this.data = data;
	}

	@Override
	@SCJAllowed(Level.LEVEL_1)
	public int readContent() {
		return this.data;
	}
	
	@SCJAllowed(Level.LEVEL_1)
	public int readHeader() {
		return this.header;
	}

	/* Implemented methods from PoolObject interface */
	@Override
	public void initialize() {
		isFree = false;
	}

	@Override
	public boolean isFree() {
		return isFree;
	}

	@Override
	public void reset() {
		isFree = true;
	}

	@Override
	public ObjectPool<PoolObject> getPool() {
		return pool;
	}

	@Override
	public void setPool(ObjectPool<PoolObject> pool) {
		this.pool = pool;
	}
	
	/* A simple factory class that dictates how packet object are created */
	static class PacketCoreFactory implements PoolObjectFactory{

		@Override
		public PoolObject createObject() {
			return new PacketCore(0, 0);
		}
		
	}

}

//public class PacketCore implements IDispose, Packet {
//
//	/* Header masks */
//	public final static int MASK_CRC = 0x00000001;
//	public final static int MASK_RDP = 0x00000002;
//	public final static int MASK_XTEA = 0x0000004;
//	public final static int MASK_HMAC = 0x00000008;
//	public final static int MASK_RES = 0x000000F0;
//	public final static int MASK_SPORT = 0x00003F00;
//	public final static int MASK_DPORT = 0x000FC000;
//	public final static int MASK_DST = 0x01F00000;
//	public final static int MASK_SRC = 0x3E000000;
//	public final static int MASK_PRI = 0xC0000000;
//
//	/* Packet */
//	public int header;
//	public int data;
//
//	public PacketCore(int header, int data) {
//		this.header = header;
//		this.data = data;
//	}
//
//	public void setContent(int data) {
//		this.data = data;
//	}
//
//	public int readContent() {
//		return this.data;
//	}
//
//	public byte getCRC() {
//		return (byte) (header & MASK_CRC);
//	}
//
//	public byte getRDP() {
//		return (byte) ((header & MASK_RDP) >>> 1);
//	}
//
//	public byte getXTEA() {
//		return (byte) ((header & MASK_XTEA) >>> 2);
//	}
//
//	public byte getHMAC() {
//		return (byte) ((header & MASK_HMAC) >>> 3);
//	}
//
//	public byte getRES() {
//		return (byte) ((header & MASK_RES) >>> 4);
//	}
//
//	public byte getSPORT() {
//		return (byte) ((header & MASK_SPORT) >>> 8);
//	}
//
//	public byte getDPORT() {
//		return (byte) ((header & MASK_DPORT) >>> 14);
//	}
//
//	public byte getDST() {
//		return (byte) ((header & MASK_DST) >>> 20);
//	}
//
//	public byte getSRC() {
//		return (byte) ((header & MASK_SRC) >>> 25);
//	}
//
//	public byte getPRI() {
//		return (byte) ((header & MASK_PRI) >>> 30);
//	}
//
//	public void setCRC(byte value) {
//		header &= ~(MASK_CRC);
//		header |= (int) value;
//	}
//
//	public void setRDP(byte value) {
//		header &= ~(MASK_RDP);
//		header |= (int) value << 1;
//	}
//
//	public void setXTEA(byte value) {
//		header &= ~(MASK_XTEA);
//		header |= (int) value << 2;
//	}
//
//	public void setHMAC(byte value) {
//		header &= ~(MASK_HMAC);
//		header |= (int) value << 3;
//	}
//
//	public void setRES(byte value) {
//		header &= ~(MASK_HMAC);
//		header |= (int) value << 4;
//	}
//
//	public void setSPORT(byte value) {
//		header &= ~(MASK_SPORT);
//		header |= (int) value << 8;
//	}
//
//	public void setDPORT(byte value) {
//		header &= ~(MASK_DPORT);
//		header |= (int) value << 14;
//	}
//
//	public void setDST(byte value) {
//		header &= ~(MASK_DST);
//		header |= (int) value << 20;
//	}
//
//	public void setSRC(byte value) {
//		header &= ~(MASK_SRC);
//		header |= (int) value << 25;
//	}
//
//	public void setPRI(byte value) {
//		header &= ~(MASK_PRI);
//		header |= (int) value << 30;
//	}
//
//	@Override
//	public void dispose() {
//		this.header = 0;
//		this.data = 0;
//		ImmortalEntry.resourcePool.putPacket(this);
//	}
//}
