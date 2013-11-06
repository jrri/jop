package libs.check.scj.mem;

import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.Mission;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.SCJAllowed;

import com.jopdesign.sys.GC;
import com.jopdesign.sys.Native;

import libs.lang.SafeStringBuilder;

public class TestSafeStringBuilder extends PeriodicEventHandler {

	public TestSafeStringBuilder(PriorityParameters priority,
			PeriodicParameters release, StorageParameters storage,
			long scopeSize, String name) {
		super(priority, release, storage, scopeSize, name);
	}

	@Override
	@SCJAllowed
	public void handleAsyncEvent() {

		System.out.println("---- SafeStringBuilder tests ---");

		TestHelper testHelper = new TestHelper();

		for (int i = 0; i < 9; i++) {
			testHelper.test = i;
			ManagedMemory.enterPrivateMemory(700, testHelper);
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
			SafeStringBuilder fixture;

			switch (test) {

			case 0:

				init = (int) mm.memoryConsumed();
				fixture = new SafeStringBuilder();
				fin = (int) mm.memoryConsumed();

				assertEquals(30, fin - init, 0);

				break;

			case 1:

				String s = "Hello World";

				init = (int) mm.memoryConsumed();
				fixture = new SafeStringBuilder(s);
				fin = (int) mm.memoryConsumed();

				assertEquals(30 + s.length(), fin - init, 1);

				break;

			case 2:

				int capacity = 115;

				init = (int) mm.memoryConsumed();
				fixture = new SafeStringBuilder(capacity);
				fin = (int) mm.memoryConsumed();

				assertEquals(14 + capacity, fin - init, 2);

				break;

			case 3:

				SafeStringBuilder sb = new SafeStringBuilder("Hello");

				init = (int) mm.memoryConsumed();
				fixture = new SafeStringBuilder(sb);
				fin = (int) mm.memoryConsumed();

				assertEquals(30 + sb.length(), fin - init, 3);

				break;

			case 4:

				fixture = new SafeStringBuilder();
				String test = "Hello World";
				String extra = "Hello to this new wonderful world";

				init = (int) mm.memoryConsumed();
				fixture.append(test);

				try {
					fixture.append(extra);
				} catch (IllegalStateException e) {
					fin = (int) mm.memoryConsumed();
					
					/* Preallocated exceptions do not consume scope memory */
					assertEquals(0, fin - init, 4);
				}

				break;

			case 5:

				fixture = new SafeStringBuilder(300);

				init = (int) mm.memoryConsumed();
				fixture.append(100);
				fin = (int) mm.memoryConsumed();

				assertEquals(212, fin - init, 5);

				break;

			case 6:

				fixture = new SafeStringBuilder(300);

				init = (int) mm.memoryConsumed();
				fixture.append(9223372036854775000L);
				fin = (int) mm.memoryConsumed();

				assertEquals(212, fin - init, 6);

				break;

			case 7:

				fixture = new SafeStringBuilder("Hello World");
				String find = "World";

				init = (int) mm.memoryConsumed();
				int i = fixture.indexOf(find);
				fin = (int) mm.memoryConsumed();

				assertEquals(6 + find.length(), fin - init, 7);

				break;

			case 8:

				String hello = new String("Hello");
				String world = new String("World");

				fixture = new SafeStringBuilder(10);

				init = (int) mm.memoryConsumed();
				fixture.insert(0, hello);
				fixture.insert(5, world);
				fin = (int) mm.memoryConsumed();
				
				assertEquals(0, fin - init, 8);

				break;

			}

		}

		private void assertEquals(int expected, int actual, int testNr) {

			if (actual == expected) {
				System.out.println("[OK, " + testNr + "]");
			} else {
				System.out.println("[FAILED, " + testNr + ", exp: " + expected
						+ ", act: " + actual + "]");
			}

		}

	}

}
