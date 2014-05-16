package test;

import javax.realtime.RawMemory;

import csp.scj.watchdog.IOMemMappedFactory;
import csp.scj.watchdog.I2CBusController;

import com.jopdesign.sys.Const;

public class TestRawMemoryAccess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * The SCJ infrastructure creates a factory to allow access to the raw
		 * memory areas the factory supports. GeneralIOFactory supports access
		 * only to IO_MEM_MAPPED raw memory areas.
		 */
		IOMemMappedFactory factory = new IOMemMappedFactory();

		/**
		 * The created factory is registered with the raw memory manager i.e.,
		 * the RawMemory final class. During the registration process the
		 * manager gets the name from the factory and checks that no factory has
		 * already been registered with that name. Otherwise an
		 * IllegalArgumentException is thrown.
		 */
		RawMemory.registerAccessFactory(factory);

		/**
		 * The application during one of the mission phases is then able to
		 * request (from the raw memory manager) access to a particular type of
		 * raw memory. The manager finds the appropriate factory and requests
		 * that it create an accessor object. This object is then returned to
		 * the mission.
		 * 
		 * In this example, the I2CBusController requests the creation of an
		 * accessor object to access the registers of the peripheral device.
		 * This action is done in the constructor method and the returned
		 * accessor objects are assigned to fields in the I2CBusController
		 * object.
		 */
		I2CBusController i2c_a = new I2CBusController(Const.I2C_A_BASE);
		I2CBusController i2c_b = new I2CBusController(Const.I2C_B_BASE);

		/**
		 * Read and write to the device registers is delegated to the
		 * I2CBusController object that in turn uses the put and get methods
		 * defined by the RawMemory model
		 */
		i2c_a.initialize(100, true);
		i2c_b.initialize(75, false);

		i2c_b.writeBuffer(new int[] { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 });

		i2c_a.writeRead(75, 100, 5);

	}

}
