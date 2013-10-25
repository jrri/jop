package libs.check.scj;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.SCJAllowed;

import libs.lang.SafeStringBuilder;
import libs.safeutil.SafeHashMap;
import libs.safeutil.SafeVector;
import libs.safeutil.extras.ObjectPool;

public class EventHandler001 extends PeriodicEventHandler{
	
	ObjectPool<DataPacket> pool;
	SafeVector<DataPacket> v;

	public EventHandler001(PriorityParameters priority,
			PeriodicParameters release, StorageParameters storage,
			long scopeSize, String name, ObjectPool<DataPacket> pool, SafeVector<DataPacket> v) {
		super(priority, release, storage, scopeSize, name);
		
		this.pool = pool;
		this.v = v;
		
	}
	
	@Override
	@SCJAllowed
	public void handleAsyncEvent() {
		
		System.out.println(getName());
		Object obj = new Object();
		int init, fin;
		SafeStringBuilder sb;
//		String s1;
		/* Get a packet */
		
		/* Get a pool object */
		DataPacket dp = pool.getPoolObject();
		dp.data = Integer.MIN_VALUE;
		dp.header = Integer.MIN_VALUE;
		
		/* Add the object to the vector */
		
		ManagedMemory mm = ManagedMemory.getManagedMemory(obj); 
		init = (int) mm.memoryConsumed();
//		v.add(dp);
//		v.remove(dp);
		SafeHashMap<Object, Object> hm = new SafeHashMap<Object, Object>();
//		String s1 = dp.toString();
//		sb = new SafeStringBuilder(75);
//		s1 = Integer.toString(0);
//		char[] ch = new char[33];
		fin = (int) mm.memoryConsumed();
		
		System.out.println(fin-init);
		
//		System.out.println(s1);
		
		
	}

}
