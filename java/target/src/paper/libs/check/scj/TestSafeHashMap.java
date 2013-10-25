package libs.check.scj;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.SCJAllowed;

import libs.safeutil.Collection;
import libs.safeutil.Map.Entry;
import libs.safeutil.SafeHashMap;
import libs.safeutil.Set;

public class TestSafeHashMap extends PeriodicEventHandler {

	public TestSafeHashMap(PriorityParameters priority,
			PeriodicParameters release, StorageParameters storage,
			long scopeSize, String name) {
		super(priority, release, storage, scopeSize, name);
	}

	@Override
	@SCJAllowed
	public void handleAsyncEvent() {

		System.out.println("---- SafeHashMap tests ---");

		TestHelper testhelper = new TestHelper();
		
		for (int i = 0; i < Properties.TSHM_TESTS; i++) {
			testhelper.test = i;
			ManagedMemory.enterPrivateMemory(600, testhelper);
		}

		terminate();
	}

	void terminate() {

		Mission.getCurrentMission().requestTermination();
	}

	class TestHelper implements Runnable {

		int test;

		@Override
		public void run() {

			Object dummy = new Object();
			Object objC = new Object();
			Object objD = new Object();
			
			int init, fin;

			ManagedMemory mm = ManagedMemory.getManagedMemory(dummy);
			SafeHashMap<Object, Object> fixture;

			switch (test) {

			case Properties.TSHM_DEFAULT_CONSTRUCTOR:

				init = (int) mm.memoryConsumed();
				fixture = new SafeHashMap<Object, Object>();
				fin = (int) mm.memoryConsumed();
				assertEquals(254,fin-init,0);
				break;

			case Properties.TSHM_CONSTRUCTOR:

				int capacity = 18;
				init = (int) mm.memoryConsumed();

				fixture = new SafeHashMap<Object, Object>(capacity);

				fin = (int) mm.memoryConsumed();

				int totalCapacity = capacity;

				totalCapacity -= 1;
				totalCapacity |= totalCapacity >> 1;
				totalCapacity |= totalCapacity >> 2;
				totalCapacity |= totalCapacity >> 4;
				totalCapacity |= totalCapacity >> 8;
				totalCapacity |= totalCapacity >> 16;
				totalCapacity++;

				assertEquals((46 + 13 * totalCapacity),fin-init,1);

				break;

			case Properties.TSHM_PUT:
				
				/* Tests put(), putForNull(), and addEntry() */
				fixture = new SafeHashMap<Object, Object>();
				Object objA = new Object();
				Object objB = new Object();
				Object objAA = new Object();
				Object objBB = new Object();
				
				init = (int) mm.memoryConsumed();
				fixture.put(objA, objB);
				fixture.put(objAA, objBB);
				fixture.put(null, null);
				fin = (int) mm.memoryConsumed();

				assertEquals(0, fin-init, 2);
				
				break;
				
			case Properties.TSHM_NEW_KEY_ITERATOR:
				
				fixture = new SafeHashMap<Object, Object>();
				
				objC = new Object();
				objD = new Object();
				fixture.put(objC, objD);
				
				init = (int) mm.memoryConsumed();
				Set<Object> ks = fixture.keySet();
				ks.iterator();
				fin = (int) mm.memoryConsumed();

				assertEquals(12, fin-init, 3);
				
				break;
				
			case Properties.TSHM_NEW_VALUE_ITERATOR:
				
				fixture = new SafeHashMap<Object, Object>();
				
				objC = new Object();
				objD = new Object();
				fixture.put(objC, objD);
				
				init = (int) mm.memoryConsumed();
				Collection<Object> vm = fixture.values();
				vm.iterator();
				fin = (int) mm.memoryConsumed();

				assertEquals(12, fin-init, 4);
				
				break;

			case Properties.TSHM_NEW_ENTRY_ITERATOR:
				
				fixture = new SafeHashMap<Object, Object>();
				
				objC = new Object();
				objD = new Object();
				fixture.put(objC, objD);
				
				init = (int) mm.memoryConsumed();
				Set<Entry<Object, Object>> es = fixture.entrySet();
				es.iterator();
				fin = (int) mm.memoryConsumed();

				assertEquals(12, fin-init, 3);
				
				break;
				

			default:
				break;
			}

		}

		private void assertEquals(int expected, int actual, int testNr) {
			
			if (actual == expected) {
				System.out.println("[OK, "+testNr+"]");
			} else {
				System.out.println("[FAILED, "+testNr+"exp: "+expected+", act: "+actual+"]");
			}
			
		}

	}

}
