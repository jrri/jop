package libs.check.scj.mem;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.SCJAllowed;

import libs.safeutil.SafeVector;
//import libs.safeutil.extras.AbstractPoolObject;
import libs.safeutil.extras.ObjectPool;
import libs.safeutil.extras.PoolObject;
import libs.safeutil.extras.PoolObjectFactory;

public class TestSafeVector extends PeriodicEventHandler {

	public TestSafeVector(PriorityParameters priority,
			PeriodicParameters release, StorageParameters storage,
			long scopeSize, String name) {
		super(priority, release, storage, scopeSize, name);

	}

	@Override
	@SCJAllowed
	public void handleAsyncEvent() {

		System.out.println("---- SafeVector tests ---");

		ObjectPool<CustomObject> pool = new ObjectPool<CustomObject>(
				new MyPoolObjectFactory());
		
		TestHelper testHelper = new TestHelper();
		testHelper.pool = pool;
		
		for (int i = 0; i < Properties.TSV_TESTS; i++) {
			testHelper.test = i;
			ManagedMemory.enterPrivateMemory(600, testHelper);
		}

		terminate();
	}

	void terminate() {

		Mission.getCurrentMission().requestTermination();
	}

	class TestHelper implements Runnable {

		public ObjectPool<CustomObject> pool;
		int test;

		@Override
		public void run() {

			Object dummy = new Object();
			Object objC = new Object();
			Object objD = new Object();

			int init, fin;

			ManagedMemory mm = ManagedMemory.getManagedMemory(dummy);
			SafeVector<CustomObject> fixture;

			switch (test) {

			case Properties.TSV_DEFAULT_CONSTRUCTOR:

				init = (int) mm.memoryConsumed();
				fixture = new SafeVector<TestSafeVector.CustomObject>();
				fin = (int) mm.memoryConsumed();

				assertEquals(25, fin - init, 0);

				break;

			case Properties.TSV_COSNTRUCTOR:
				int capacity = 100;

				init = (int) mm.memoryConsumed();
				fixture = new SafeVector<TestSafeVector.CustomObject>(capacity);
				fin = (int) mm.memoryConsumed();
				assertEquals(15 + capacity, fin - init, 1);

				break;
				
			case Properties.TSV_ELEMENTS:
				
				fixture = new SafeVector<TestSafeVector.CustomObject>();

				init = (int) mm.memoryConsumed();
				fixture.elements();
				fin = (int) mm.memoryConsumed();
				assertEquals(8, fin - init, 2);
				
				break;
				
			case Properties.TSV_ELEMENT_AT:
				
				fixture = new SafeVector<TestSafeVector.CustomObject>();
				fixture.add(pool.getPoolObject());
				
				init = (int) mm.memoryConsumed();
				
				try {
					fixture.elementAt(0);
					fixture.elementAt(100);
				} catch (ArrayIndexOutOfBoundsException e) {
					fin = (int) mm.memoryConsumed();
					
					/* Preallocated exceptions do not consume scope memory */
					assertEquals(0, fin - init, 3);
				}
				
				break;

			default:
				break;
			}

		}

		private void assertEquals(int expected, int actual, int testNr) {

			if (actual == expected) {
				System.out.println("[OK, "+testNr+"]");
			} else {
				System.out.println("[FAILED, "+testNr+" exp: "+expected+", act: "+actual+"]");
			}

		}

	}

	class CustomObject implements PoolObject {
		
		ObjectPool<?> pool;

		@Override
		public void initialize() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean isFree() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void terminate() {
			// TODO Auto-generated method stub

		}

		@Override
		public ObjectPool<?> getPool() {
			return pool;
		}

		@Override
		public void setPool(ObjectPool<PoolObject> pool) {
			// TODO Auto-generated method stub
			
		}

	}

	class MyPoolObjectFactory implements PoolObjectFactory {

		@Override
		public PoolObject createObject() {
			return new CustomObject();
		}

	}

}
