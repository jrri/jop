package libs.junit;

import static org.junit.Assert.*;
import libs.safeutil.Iterator;
import libs.safeutil.SafeVector;
//import libs.safeutil.extras.AbstractPoolObject;
import libs.safeutil.extras.ObjectPool;
import libs.safeutil.extras.PoolObject;
import libs.safeutil.extras.PoolObjectFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SafeVectorJUnit {

	class MyPoolObject implements PoolObject {

		private boolean isFree = true;
		public int number = 0;
		private ObjectPool<?> pool;

		@Override
		public void initialize() {
			this.isFree = false;

		}

		@Override
		public boolean isFree() {
			return this.isFree;
		}

		@Override
		public void terminate() {
			this.isFree = true;
		}

		@Override
		public ObjectPool<?> getPool() {
			return pool;
		}

		@Override
		public void setPool(ObjectPool<PoolObject> pool) {
			this.pool = pool;
		}

	}

	class MyFactory implements PoolObjectFactory {

		private int count = 0;

		@Override
		public PoolObject createObject() {
			MyPoolObject temp = new MyPoolObject();
			temp.number = count;
			count++;
			return temp;
		}

	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEquals() {

		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>();
		ObjectPool<MyPoolObject> MyPool = new ObjectPool<MyPoolObject>(
				new MyFactory());

		SafeVector<MyPoolObject> fixture0 = new SafeVector<MyPoolObject>();
		ObjectPool<MyPoolObject> MyPool0 = new ObjectPool<MyPoolObject>(
				new MyFactory());

		/* The vector is equal to itself */
		assertTrue(fixture.equals(fixture));

		/* Two empty vectors are considered equal */
		assertTrue(fixture.equals(fixture0));

		/* Once an element is added, they should be different */
		MyPoolObject obj = MyPool.getPoolObject();
		fixture.add(obj);
		assertFalse(fixture.equals(fixture0));

		/* Adding the same element to the other vector makes them equal again */
		fixture0.add(obj);
		assertTrue(fixture.equals(fixture0));

	}

	@Test
	public void testHashCode() {

		ObjectPool<MyPoolObject> MyPool = new ObjectPool<MyPoolObject>(
				new MyFactory());
		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>(
				MyPool.maxObjects());

		MyPoolObject[] poolObjs = new MyPoolObject[MyPool.maxObjects()];
		for (int i = 0; i < MyPool.maxObjects(); i++) {
			poolObjs[i] = MyPool.getPoolObject();
			fixture.add(poolObjs[i]);
		}

		/* Hash code is calculated according to AbstractList hash */
		int hashCode = 1;
		for (int i = 0; i < poolObjs.length; i++) {
			hashCode = 31 * hashCode
					+ (poolObjs[i] == null ? 0 : poolObjs[i].hashCode());
		}
		
		assertEquals(hashCode, fixture.hashCode());

	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testSize() {

		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>();
		ObjectPool<MyPoolObject> MyPool = new ObjectPool<MyPoolObject>(
				new MyFactory());

		assertEquals(fixture.size(), 0);

		fixture.add(MyPool.getPoolObject());

		assertEquals(MyPool.usedObjects(), 1, MyPool.usedObjects());
		assertEquals(fixture.size(), 1);

	}

	@Test
	public void testIsEmp() {

		/**
		 * No element has been added to the Vector, hence it should be empty
		 */
		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>();
		assertTrue(fixture.isEmpty());

	}

	@Test
	public void testContains() {

		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>();
		ObjectPool<MyPoolObject> MyPool = new ObjectPool<MyPoolObject>(
				new MyFactory());

		MyPoolObject poolObj_000 = MyPool.getPoolObject();
		MyPoolObject poolObj_001 = MyPool.getPoolObject();
		MyPoolObject poolObj_002 = MyPool.getPoolObject();
		MyPoolObject poolObj_003 = MyPool.getPoolObject();
		MyPoolObject poolObj_004 = MyPool.getPoolObject();

		fixture.add(poolObj_002);
		fixture.add(poolObj_003);

		assertFalse(fixture.contains(poolObj_000));
		assertFalse(fixture.contains(poolObj_001));
		assertTrue(fixture.contains(poolObj_002));
		assertTrue(fixture.contains(poolObj_003));
		assertFalse(fixture.contains(poolObj_004));

	}

	@Test
	public void testToArray() {

		ObjectPool<MyPoolObject> MyPool = new ObjectPool<MyPoolObject>(
				new MyFactory());
		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>(
				MyPool.maxObjects());

		MyPoolObject[] poolObjs = new MyPoolObject[MyPool.maxObjects()];
		for (int i = 0; i < MyPool.maxObjects(); i++) {
			poolObjs[i] = MyPool.getPoolObject();
			fixture.add(poolObjs[i]);
		}

		Object[] obj = fixture.toArray();
		assertEquals(obj.length, MyPool.maxObjects());

		for (int i = 0; i < obj.length; i++) {
			assertTrue(obj[i].equals(poolObjs[i]));
		}

	}

	@Test
	public void testAddE() {

		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>();
		ObjectPool<MyPoolObject> MyPool = new ObjectPool<MyPoolObject>(
				new MyFactory());

		/*
		 * Pool created with default capacity
		 */
		assertEquals(16, MyPool.maxObjects());
		assertEquals(0, MyPool.usedObjects());

		for (int i = 0; i < fixture.capacity(); i++) {
			fixture.add(MyPool.getPoolObject());
			assertEquals(i + 1, MyPool.usedObjects());
			assertEquals(i + 1, fixture.size());
		}

		assertEquals(10, MyPool.usedObjects());
		assertEquals(10, fixture.size());

	}

	@Test
	public void testRemoveObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testClear() {
		
		ObjectPool<MyPoolObject> MyPool = new ObjectPool<MyPoolObject>(
				new MyFactory());
		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>(
				MyPool.maxObjects());

		MyPoolObject[] poolObjs = new MyPoolObject[MyPool.maxObjects()];
		for (int i = 0; i < MyPool.maxObjects(); i++) {
			poolObjs[i] = MyPool.getPoolObject();
			fixture.add(poolObjs[i]);
		}
		
		
		
		assertEquals(MyPool.maxObjects(), fixture.size());
		assertEquals(MyPool.maxObjects(),MyPool.usedObjects());
		
		fixture.clear();
		assertEquals(0, fixture.size());
		
		assertEquals(0, fixture.size());
		assertEquals(0, MyPool.usedObjects());
		
	}

	@Test
	public void testGet() {
		fail("Not yet implemented");
	}

	@Test
	public void testSet() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddIntE() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testLastIndexOfObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveRange() {
		fail("Not yet implemented");
	}

	@Test
	public void testVectorInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testVector() {
		fail("Not yet implemented");
	}

	@Test
	public void testCopyInto() {
		fail("Not yet implemented");
	}

	@Test
	public void testCapacity() {

		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>();

		/**
		 * Vector created with default capacity
		 */
		assertEquals(fixture.capacity(), 10);

	}

	@Test
	public void testElements() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfObjectInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testLastIndexOfObjectInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testElementAt() {
		fail("Not yet implemented");
	}

	@Test
	public void testFirstElement() {
		fail("Not yet implemented");
	}

	@Test
	public void testLastElement() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetElementAt() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveElementAt() {

		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>();
		ObjectPool<MyPoolObject> objectPool = new ObjectPool<MyPoolObject>(
				new MyFactory());

		MyPoolObject[] objectArray = new MyPoolObject[10];
		for (int i = 0; i < objectArray.length; i++) {
			objectArray[i] = objectPool.getPoolObject();
		}

		for (int i = 0; i < fixture.capacity(); i++) {
			fixture.add(objectArray[i]);
		}

		assertEquals(10, objectPool.usedObjects());
		assertEquals(10, fixture.size());

		/**
		 * Deletes the component at the specified index. Each component in this
		 * vector with an index greater or equal to the specified {@code index}
		 * is shifted downward to have an index one smaller than the value it
		 * had previously. The size of this vector is decreased by {@code 1} and
		 * the removed element is returned to its object pool.
		 */
		fixture.removeElementAt(5);
		assertEquals(4, fixture.elementAt(4).number);
		assertEquals(6, fixture.elementAt(5).number);
		assertEquals(9, fixture.size());
		assertEquals(9, objectPool.usedObjects());

		fixture.removeElementAt(5);
		assertEquals(4, fixture.elementAt(4).number);
		assertEquals(7, fixture.elementAt(5).number);
		assertEquals(8, fixture.size());
		assertEquals(8, objectPool.usedObjects());

		/**
		 * The index must be a value greater than or equal to {@code 0} and less
		 * than the current size of the vector.
		 */
		int size = fixture.size();
		try {
			fixture.removeElementAt(size + 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			assertNotNull("ArrayIndexOutOfBoundsException expected", e);
		}

		try {
			fixture.removeElementAt(-1);
		} catch (ArrayIndexOutOfBoundsException e) {
			assertNotNull("ArrayIndexOutOfBoundsException expected", e);
		}

	}

	@Test
	public void testInsertElementAt() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddElement() {

		fail("Not yet implemented");
	}

	@Test
	public void testRemoveElement() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveAllElements() {
		fail("Not yet implemented");
	}

}
