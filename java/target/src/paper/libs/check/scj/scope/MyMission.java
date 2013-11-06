package libs.check.scj.scope;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import libs.check.scj.generic.GenericMission;
import libs.io.DataInputStream;
import libs.io.IOException;
import libs.safeutil.SafeVector;
import libs.safeutil.extras.ObjectPool;
import libs.safeutil.extras.PoolObject;
import libs.safeutil.extras.PoolObjectFactory;

public class MyMission extends GenericMission {

	@Override
	@SCJAllowed(Level.SUPPORT)
	protected void initialize() {
		
		/* Shared data structures */
		PacketFactory packetFactory = new PacketFactory();
		ObjectPool<DataPacket> packetPool = new ObjectPool<DataPacket>(
				packetFactory);

		SafeVector<DataPacket> vector = new SafeVector<DataPacket>(100);
		
		try {
			DataInputStream dis = new DataInputStream(new I2cInputStream(0));
			dis.available();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/* Initialization of handlers */

		PriorityParameters PRI001 = new PriorityParameters(10);
		PriorityParameters PRI002 = new PriorityParameters(11);

		PeriodicParameters PER001 = new PeriodicParameters(new RelativeTime(),
				new RelativeTime(1000, 0));
		PeriodicParameters PER002 = new PeriodicParameters(new RelativeTime(),
				new RelativeTime(2000, 0));

		StorageParameters STO001 = new StorageParameters(2048, null);
		StorageParameters STO002 = new StorageParameters(512, null);

		EventHandler001 EH001 = new EventHandler001(PRI001, PER001, STO001,
				512, "EH001", packetPool, vector );
		EventHandler002 EH002 = new EventHandler002(PRI002, PER002, STO002,
				256, "EH002");

		ServerHandler SRV001 = new ServerHandler(PRI001, PER002, STO002, 512, "SRV001");
		ClientHandler CLI001 = new ClientHandler(PRI001, PER002, STO002, 512, "CLI001");
		
		EH001.register();
		EH002.register();
//		SRV001.register();
//		CLI001.register();

	}
	
	class PacketFactory implements PoolObjectFactory {

		@Override
		public PoolObject createObject() {
			return new DataPacket();
		}

	}

}
