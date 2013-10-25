package libs.check.scj;

import javax.realtime.MemoryArea;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.realtime.ReleaseParameters;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.MissionMemory;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.SCJAllowed;

import com.jopdesign.sys.Memory;

import libs.safeutil.SafeVector;
import libs.safeutil.extras.AbstractPoolObject;
import libs.safeutil.extras.ObjectPool;
import libs.safeutil.extras.PoolObjectFactory;

public abstract class GenericMission extends Mission {

//	@Override
//	@SCJAllowed(Level.SUPPORT)
//	protected void initialize() {
//		
//		/* Shared data structures */
//		PacketFactory packetFactory = new PacketFactory();
//		ObjectPool<DataPacket> packetPool = new ObjectPool<DataPacket>(
//				packetFactory);
//
//		SafeVector<DataPacket> vector = new SafeVector<DataPacket>(100);
//		
//		/* Initialization of handlers */
//
//		PriorityParameters PRI001 = new PriorityParameters(10);
//		PriorityParameters PRI002 = new PriorityParameters(11);
//
//		PeriodicParameters PER001 = new PeriodicParameters(new RelativeTime(),
//				new RelativeTime(1000, 0));
//		PeriodicParameters PER002 = new PeriodicParameters(new RelativeTime(),
//				new RelativeTime(2000, 0));
//
//		StorageParameters STO001 = new StorageParameters(2048, null);
//		StorageParameters STO002 = new StorageParameters(512, null);
//
//		EventHandler001 EH001 = new EventHandler001(PRI001, PER001, STO001,
//				512, "EH001", packetPool, vector );
//		EventHandler002 EH002 = new EventHandler002(PRI002, PER002, STO002,
//				256, "EH002");
//
//		ServerHandler SRV001 = new ServerHandler(PRI001, PER002, STO002, 512, "SRV001");
//		ClientHandler CLI001 = new ClientHandler(PRI001, PER002, STO002, 512, "CLI001");
//		
//		EH001.register();
//		EH002.register();
//
//	}

	@Override
	@SCJAllowed
	public long missionMemorySize() {
		// TODO Auto-generated method stub
		return 4096;
	}

	class PacketFactory implements PoolObjectFactory {

		@Override
		public AbstractPoolObject createObject() {
			return new DataPacket();
		}

	}

}
