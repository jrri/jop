package libs.junit;

import static org.junit.Assert.*;
import libs.safeutil.SafeHashMap;
import libs.safeutil.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SafeHashMapJUnit {
	
	SafeHashMap<String, String> fixture;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
		fixture = new SafeHashMap<String, String>();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSize() {
		assertEquals(0, fixture.size());
		
		fixture.put("A", "B");
		assertEquals(1, fixture.size());
		
		fixture.put("C", "D");
		assertEquals(2, fixture.size());

		fixture.put("E", "F");
		assertEquals(3, fixture.size());

		fixture.put("G", "H");
		assertEquals(4, fixture.size());

		fixture.put("I", "J");
		assertEquals(5, fixture.size());

		fixture.put("K", "L");
		assertEquals(6, fixture.size());

		fixture.put("M", "N");
		assertEquals(7, fixture.size());
		
		fixture.put("O", "P");
		assertEquals(8, fixture.size());

		fixture.put("Q", "R");
		assertEquals(9, fixture.size());

		fixture.put("S", "T");
		assertEquals(10, fixture.size());

		fixture.put("U", "V");
		assertEquals(11, fixture.size());

		fixture.put("W", "X");
		assertEquals(12, fixture.size());

		fixture.put("Y", "Z");
		assertEquals(13, fixture.size());

		fixture.put("AA", "BB");
		assertEquals(14, fixture.size());

		fixture.put("CC", "DD");
		assertEquals(15, fixture.size());

		fixture.put("EE", "FF");
		assertEquals(16, fixture.size());
		
	}

	@Test
	public void testIsEmpty() {
		
		assertEquals(true, fixture.isEmpty());
		
		fixture.put("AA", "BB");
		assertEquals(false, fixture.isEmpty());
		
		fixture.remove("AA");
		assertEquals(true, fixture.isEmpty());
	}

	@Test
	public void testContainsValue() {
		
		fixture.put("AA", "BB");
		assertEquals(true, fixture.containsValue("BB"));
		assertEquals(false, fixture.containsValue("DD"));
		
	}

	@Test
	public void testContainsKey() {
	
		fixture.put("AA", "BB");
		assertEquals(true, fixture.containsKey("AA"));
		assertEquals(false, fixture.containsKey("BB"));

	}

	@Test
	public void testGet() {
		
		fixture.put("AA", "BB");
		fixture.put("CC", "DD");
		assertEquals(true, fixture.get("AA").equals("BB"));
		assertEquals(true, fixture.get("CC").equals("DD"));
		assertEquals(true, (fixture.get("BB") == null));
	}

	@Test
	public void testPut() {
		
		/* It has already been tested :) */
		fixture.put("Juan", "Rios");
		assertEquals(1, fixture.size());
		assertEquals(false, fixture.isEmpty());
		assertEquals(true, fixture.containsValue("Rios"));
		assertEquals(true, fixture.containsKey("Juan"));
		assertEquals(true, fixture.get("Juan").equals("Rios"));
		
		/* There is a value associated with the key, the new put
		 * will remove the previous value and add the new one */
		fixture.put("Juan", "Rivas");
		assertEquals(1, fixture.size());
		assertEquals(false, fixture.isEmpty());
		assertEquals(false, fixture.containsValue("Rios"));
		assertEquals(true, fixture.containsKey("Juan"));
		assertEquals(false, fixture.get("Juan").equals("Rios"));
		
	}

	@Test
	public void testRemove() {
		
		fixture.put("AA", "BB");
		assertEquals(1, fixture.size());
		
		assertEquals(true, fixture.remove("AA").equals("BB"));
		assertEquals(true, fixture.isEmpty());
		
	}

	@Test
	public void testClear() {
		
		fixture.put("AA", "QQ");
		fixture.put("BB", "RR");
		fixture.put("CC", "SS");
		fixture.put("DD", "TT");
		fixture.put("EE", "UU");
		fixture.put("FF", "VV");
		fixture.put("GG", "WW");
		fixture.put("HH", "XX");
		fixture.put("II", "YY");
		fixture.put("JJ", "ZZ");
		fixture.put("KK", "AA");
		fixture.put("LL", "BB");
		fixture.put("MM", "CC");
		fixture.put("NN", "DD");
		fixture.put("OO", "EE");
		fixture.put("PP", "FF");
		assertEquals(16, fixture.size());
		
		fixture.clear();
		assertEquals(0,fixture.size());
		assertEquals(true, fixture.isEmpty());
		
		/* Test that we can add elements again */
		fixture.put("AA", "QQ");
		fixture.put("BB", "RR");
		fixture.put("CC", "SS");
		fixture.put("DD", "TT");
		fixture.put("EE", "UU");
		fixture.put("FF", "VV");
		fixture.put("GG", "WW");
		fixture.put("HH", "XX");
		fixture.put("II", "YY");
		fixture.put("JJ", "ZZ");
		fixture.put("KK", "AA");
		fixture.put("LL", "BB");
		fixture.put("MM", "CC");
		fixture.put("NN", "DD");
		fixture.put("OO", "EE");
		fixture.put("PP", "FF");
		assertEquals(16, fixture.size());
		
		fixture.clear();
		assertEquals(0,fixture.size());
		assertEquals(true, fixture.isEmpty());
		
	}

	@Test
	public void testKeySet() {
		
		fixture.put("AA", "QQ");
		fixture.put("BB", "RR");
		fixture.put("CC", "SS");
		fixture.put("DD", "TT");
		fixture.put("EE", "UU");
		fixture.put("FF", "VV");
		fixture.put("GG", "WW");
		fixture.put("HH", "XX");
		fixture.put("II", "YY");
		fixture.put("JJ", "ZZ");
		fixture.put("KK", "AA");
		fixture.put("LL", "BB");
		fixture.put("MM", "CC");
		fixture.put("NN", "DD");
		fixture.put("OO", "EE");
		fixture.put("PP", "FF");
		assertEquals(16, fixture.size());
		Set<String> set = fixture.keySet();
		
		assertEquals(true, set.remove("PP"));
		assertEquals(15, fixture.size());
		
		
	}

	@Test
	public void testValues() {
		fail("Not yet implemented");
	}

	@Test
	public void testSafeHashMapInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testSafeHashMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testSafeHashMapMapOfQextendsKQextendsV() {
		fail("Not yet implemented");
	}

	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	@Test
	public void testHash() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexFor() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEntry() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveEntryForKey() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveMapping() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddEntry() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewKeyIterator() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewValueIterator() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewEntryIterator() {
		fail("Not yet implemented");
	}

	@Test
	public void testEntrySet() {
		fail("Not yet implemented");
	}

	@Test
	public void testCapacity() {
		fail("Not yet implemented");
	}

	@Test
	public void testEquals() {
		fail("Not yet implemented");
	}

	@Test
	public void testHashCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testClone() {
		fail("Not yet implemented");
	}

	@Test
	public void testSafeAbstractMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testPutAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testEntrySet1() {
		fail("Not yet implemented");
	}

}
