package libs.safeutil;

import libs.io.Serializable;

/*
 *  %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

//import java.io.*;

//import javax.safetycritical.ManagedMemory;

import libs.lang.Cloneable;

/**
 * Hash table based implementation of the <tt>Map</tt> interface.  This
 * implementation provides all of the optional map operations, and permits
 * <tt>null</tt> values and the <tt>null</tt> key.  (The <tt>HashMap</tt>
 * class is roughly equivalent to <tt>Hashtable</tt>, except that it is
 * unsynchronized and permits nulls.)  This class makes no guarantees as to
 * the order of the map; in particular, it does not guarantee that the order
 * will remain constant over time.
 *
 * <p>This implementation provides constant-time performance for the basic
 * operations (<tt>get</tt> and <tt>put</tt>), assuming the hash function
 * disperses the elements properly among the buckets.  Iteration over
 * collection views requires time proportional to the "capacity" of the
 * <tt>HashMap</tt> instance (the number of buckets) plus its size (the number
 * of key-value mappings).  Thus, it's very important not to set the initial
 * capacity too high (or the load factor too low) if iteration performance is
 * important.
 *
 * <p>An instance of <tt>HashMap</tt> has two parameters that affect its
 * performance: <i>initial capacity</i> and <i>load factor</i>.  The
 * <i>capacity</i> is the number of buckets in the hash table, and the initial
 * capacity is simply the capacity at the time the hash table is created.  The
 * <i>load factor</i> is a measure of how full the hash table is allowed to
 * get before its capacity is automatically increased.  When the number of
 * entries in the hash table exceeds the product of the load factor and the
 * current capacity, the hash table is <i>rehashed</i> (that is, internal data
 * structures are rebuilt) so that the hash table has approximately twice the
 * number of buckets.
 *
 * <p>As a general rule, the default load factor (.75) offers a good tradeoff
 * between time and space costs.  Higher values decrease the space overhead
 * but increase the lookup cost (reflected in most of the operations of the
 * <tt>HashMap</tt> class, including <tt>get</tt> and <tt>put</tt>).  The
 * expected number of entries in the map and its load factor should be taken
 * into account when setting its initial capacity, so as to minimize the
 * number of rehash operations.  If the initial capacity is greater
 * than the maximum number of entries divided by the load factor, no
 * rehash operations will ever occur.
 *
 * <p>If many mappings are to be stored in a <tt>HashMap</tt> instance,
 * creating it with a sufficiently large capacity will allow the mappings to
 * be stored more efficiently than letting it perform automatic rehashing as
 * needed to grow the table.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a hash map concurrently, and at least one of
 * the threads modifies the map structurally, it <i>must</i> be
 * synchronized externally.  (A structural modification is any operation
 * that adds or deletes one or more mappings; merely changing the value
 * associated with a key that an instance already contains is not a
 * structural modification.)  This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the map.
 *
 * If no such object exists, the map should be "wrapped" using the
 * {@link Collections#synchronizedMap Collections.synchronizedMap}
 * method.  This is best done at creation time, to prevent accidental
 * unsynchronized access to the map:<pre>
 *   Map m = Collections.synchronizedMap(new HashMap(...));</pre>
 *
 * <p>The iterators returned by all of this class's "collection view methods"
 * are <i>fail-fast</i>: if the map is structurally modified at any time after
 * the iterator is created, in any way except through the iterator's own
 * <tt>remove</tt> method, the iterator will throw a
 * {@link ConcurrentModificationException}.  Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than risking
 * arbitrary, non-deterministic behavior at an undetermined time in the
 * future.
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw <tt>ConcurrentModificationException</tt> on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness: <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @author  Doug Lea
 * @author  Josh Bloch
 * @author  Arthur van Hoff
 * @author  Neal Gafter
 * @version %I%, %G%
 * @see     Object#hashCode()
 * @see     Collection
 * @see	    Map
 * @see	    TreeMap
 * @see	    Hashtable
 * @since   1.2
 */

public class SafeHashMap<K, V> extends SafeAbstractMap<K, V> implements
		Map<K, V> , Cloneable , Serializable
{

    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_CAPACITY = 16;
    
    static final int DEFAULT_ENTRIES = 16; 

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

	/**
	 * The table holding entries indexed by the hash function. Length MUST
	 * Always be a power of two.
	 */
    final Entry<K,V>[] table;
    
	/**
	 * An array representing pre-allocated Entry objects. The number of
	 * pre-allocated Entry objects is equal to the capacity of the SafeHashMap
	 */
    final Entry<K,V>[] entries;

    /**
     * The number of key-value mappings contained in this map.
     */
    int size;

	/**
	 * The number of times this SafeHashMap has been structurally modified.
	 * Structural modifications are those that change the number of mappings in
	 * the SafeHashMap. This field is used to make iterators on Collection-views
	 * of the SafeHashMap fail-fast. (See ConcurrentModificationException).
	 */
    volatile int modCount;
    
    static final IllegalArgumentException negInitCapacityExc;
    static final IllegalStateException noFreeEntriesExc;
    static final ConcurrentModificationException concModExc;
    static final NoSuchElementException noSuchElemExc;
    static final IllegalStateException illegalStateExc;
    
	static {
		negInitCapacityExc = new IllegalArgumentException(
				"Negative or not power of two initial capacity");
		noFreeEntriesExc = new IllegalStateException("No more free entries");
		concModExc = new ConcurrentModificationException();
		noSuchElemExc = new NoSuchElementException();
		illegalStateExc = new IllegalStateException();
	}

    // WCMEM = 46 + 13*entriesSize
    // MOD
    public SafeHashMap(final int tableSize, final int entriesSize) {
    	
        if (tableSize <= 0)
            throw negInitCapacityExc;
        
		/* Force initial capacity to be power of two */
		if ((tableSize & (tableSize - 1)) != 0)
			throw negInitCapacityExc;

		if (tableSize > MAXIMUM_CAPACITY) {
			table = new Entry[MAXIMUM_CAPACITY];
		} else {
			table = new Entry[tableSize];
		}
        
       	/* Initialize Entry pool */
    	entries = new Entry[entriesSize];

    	/* Preallocate Entries into the entry pool */
    	for(int i=0; i<tableSize; i++){
        	entries[i] = new Entry<K, V>();
    	}
    	
    	/* Initialize the views of the HashMap */
    	
    	// WCMEM = 7
		entrySet = new EntrySet();
		// WCMEM = 7
		keySet = new KeySet();
		// WCMEM = 7
		values = new Values();
    }

    /**
     * Constructs an empty <tt>HashMap</tt> with the default initial capacity
     * (16).
     */
    // WCMEM = 254
    // MOD
    public SafeHashMap() {
    	this(DEFAULT_CAPACITY, DEFAULT_ENTRIES);
        init();
    }

//	/**
//	 * Constructs a new <tt>HashMap</tt> with the same mappings as the specified
//	 * <tt>Map</tt>. The <tt>HashMap</tt> is created with capacity sufficient to
//	 * hold the mappings in the specified <tt>Map</tt>.
//	 * 
//	 * @param m
//	 *            the map whose mappings are to be placed in this map
//	 * @throws NullPointerException
//	 *             if the specified map is null
//	 */
//    // MOD
//    public SafeHashMap(Map<? extends K, ? extends V> m) {
//    	
//        this(Math.max(m.size(), DEFAULT_INITIAL_CAPACITY));
//        putAllForCreate(m);
//    }

    /* internal utilities */

    /**
     * Initialization hook for subclasses. This method is called
     * in all constructors and pseudo-constructors (clone, readObject)
     * after HashMap has been initialized but before any entries have
     * been inserted.  (In the absence of this method, readObject would
     * require explicit knowledge of subclasses.)
     */
    // WCMEM = 
    // MOD
    void init() {

    }
    
	/**
	 * Applies a supplemental hash function to a given hashCode, which defends
	 * against poor quality hash functions. This is critical because HashMap
	 * uses power-of-two length hash tables, that otherwise encounter collisions
	 * for hashCodes that do not differ in lower bits. Note: Null keys always
	 * map to hash 0, thus index 0.
	 */
    // WCMEM = 0
	@MemSafe(risk = { MemoryRisk.NONE })
	static int hash(int h) {
		/*
		 * This function ensures that hashCodes that differ only by constant
		 * multiples at each bit position have a bounded number of collisions
		 * (approximately 8 at default load factor).
		 */
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

    /**
     * Returns index for hash code h.
     */
    // WCMEM = 0
    @MemSafe(risk = {MemoryRisk.NONE})
    static int indexFor(int h, int length) {
    	
    	/* Equivalent to h mod length */
        return h & (length-1);
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    // WCMEM = 0
    @MemSafe(risk = {MemoryRisk.NONE})
    public int size() {
        return size;
    }
    
    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    // WCMEM = 0
    @MemSafe(risk = {MemoryRisk.NONE})
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     *
     * <p>A return value of {@code null} does not <i>necessarily</i>
     * indicate that the map contains no mapping for the key; it's also
     * possible that the map explicitly maps the key to {@code null}.
     * The {@link #containsKey containsKey} operation may be used to
     * distinguish these two cases.
     *
     * @see #put(Object, Object)
     */
    // MOD
    // WCMEM = 0
    @MemSafe(risk ={MemoryRisk.NONE})
	public V get(Object key) {
    	
		if (key == null)
			return getForNullKey();
		
		int hash = hash(key.hashCode());
		
		/*
		 * The bound for this loop will be equal to the number of free entries
		 * of the SafeHashMap. In the worst case, for the worst hash function in
		 * the world, all elements will collide into the same bucket table. To
		 * make the search is made WCET analyzable like this:
		 */
		Entry<K, V> e = table[indexFor(hash, table.length)];
		for (int i = 0; i < entries.length; i++) {
			if (e != null) {
				Object k;
				if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
					return e.value;
				e = e.next;
			}
		}
		
		// Original code:
		//
		//	for (Entry<K, V> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
		//		Object k;
		//
		//		if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
		//			return e.value;
		//	}

		return null;
	}

    /**
     * Offloaded version of get() to look up null keys.  Null keys map
     * to index 0.  This null case is split out into separate methods
     * for the sake of performance in the two most commonly used
     * operations (get and put), but incorporated with conditionals in
     * others.
     */
    // MOD
    // WCMEM = 0
    @MemSafe(risk ={MemoryRisk.NONE})
    private V getForNullKey() {
    	
		/*
		 * The bound for this loop will be equal to the number of free entries
		 * of the SafeHashMap. In the worst case, for the worst hash function in
		 * the world, all elements will collide into the same bucket table. To
		 * make the search is made WCET analyzable like this:
		 */
		Entry<K, V> e = table[0];
		for (int i = 0; i < entries.length; i++) {
			if (e != null) {
				if (e.key == null)
					return e.value;

				e = e.next;
			}
		}
        
        return null;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the
     * specified key.
     *
     * @param   key   The key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key.
     */
    // WCMEM = 0
    @MemSafe(risk ={MemoryRisk.NONE})
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    /**
     * Returns the entry associated with the specified key in the
     * HashMap.  Returns null if the HashMap contains no mapping
     * for the key.
     */
    // MOD
    // WCMEM = 0
    @MemSafe(risk ={MemoryRisk.NONE})
	final Entry<K, V> getEntry(Object key) {
    	
		int hash = (key == null) ? 0 : hash(key.hashCode());
		
		/*
		 * The bound for this loop will be equal to the capacity of the
		 * SafeHashMap, as in the worst case, for the worst hash function, all
		 * elements will collide into the same bucket table
		 */
		Entry<K, V> e = table[indexFor(hash, table.length)];
		for (int i = 0; i < entries.length; i++) {
			if (e != null) {
				Object k;
				if (e.hash == hash && ((k = e.key) == key || 
						(key != null && key.equals(k))))
					return e;
				
				e = e.next;
			}
		}
		
		return null;
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for the key, the old value is
	 * replaced.
	 * 
	 * @param key
	 *            key with which the specified value is to be associated
	 * @param value
	 *            value to be associated with the specified key
	 * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
	 *         if there was no mapping for <tt>key</tt>. (A <tt>null</tt> return
	 *         can also indicate that the map previously associated
	 *         <tt>null</tt> with <tt>key</tt>.)
	 */
    // WCMEM = 0
	@MemSafe(risk = { MemoryRisk.MIXED_CONTEXT, MemoryRisk.UNREFERENCED_OBJ })
	public V put(K key, V value) {
		
		if (key == null)
			return putForNullKey(value);
		
		int hash = hash(key.hashCode());
		int i = indexFor(hash, table.length);
		
		/* If the entry already exists */
		Entry<K, V> e = table[i];
		for (int j = 0; j < entries.length; j++) {
			if (e != null) {
				Object k;
				if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
					V oldValue = e.value;
					e.value = value;
					e.recordAccess(this);
					return oldValue;
				}

				e = e.next;
			}
		}

		modCount++;
		addEntry(hash, key, value, i);

		return null;
	}

    /**
     * Offloaded version of put for null keys
     */
	// MOD
	// WCMEM = 9
    @MemSafe(risk = {MemoryRisk.UNREFERENCED_OBJ})
    private V putForNullKey(V value) {
    	
		/*
		 * The bound for this loop will be equal to the number of free entries
		 * of the SafeHashMap. In the worst case, for the worst hash function in
		 * the world, all elements will collide into the same bucket table. To
		 * make the search is made WCET analyzable like this:
		 */
		Entry<K, V> e = table[0];
		for (int i = 0; i < entries.length; i++) {
			if (e != null) {
				if (e.key == null) {
					V oldValue = e.value;
					e.value = value;
					e.recordAccess(this);
					return oldValue;
				}
				e = e.next;
			}
		}
        
        // Original code:
        //
        //   for (Entry<K,V> e = table[0]; e != null; e = e.next) {
        //    	if (e.key == null) {
        //        V oldValue = e.value;
        //        e.value = value;
        //        e.recordAccess(this);
        //        return oldValue;
        //      }
        //    }
        
        modCount++;
        addEntry(0, null, value, 0);
        return null;
    }

//    /**
//     * This method is used instead of put by constructors and
//     * pseudoconstructors (clone, readObject).  It does not resize the table,
//     * check for comodification, etc.
//     */
//    @MemSafe(risk = {MemoryRisk.MIXED_CONTEXT, MemoryRisk.UNREFERENCED_OBJ})
//    private void putForCreate(K key, V value) {
//    	
//        int hash = (key == null) ? 0 : hash(key.hashCode());
//        int i = indexFor(hash, table.length);
//
//        /**
//         * Look for preexisting entry for key.  This will never happen for
//         * clone or deserialize.  It will only happen for construction if the
//         * input Map is a sorted map whose ordering is inconsistent w/ equals.
//         * 
//         * The bound for this loop will be equal to the capacity of the
//		 * SafeHashMap, as in the worst case, for the worst hash function, all
//		 * elements will collide into the same bucket table
//         */
//        for (Entry<K,V> e = table[i]; e != null; e = e.next) {
//            Object k;
//            if (e.hash == hash &&
//                ((k = e.key) == key || (key != null && key.equals(k)))) {
//                e.value = value;
//                return;
//            }
//        }
//
//    }

//    //MOD
//	@MemSafe(risk = { MemoryRisk.MIXED_CONTEXT, MemoryRisk.UNREFERENCED_OBJ })
//	private void putAllForCreate(Map<? extends K, ? extends V> m) {
//		
//		for (Iterator<? extends Map.Entry<? extends K, ? extends V>> i = m
//				.entrySet().iterator(); i.hasNext();) {
//			Map.Entry<? extends K, ? extends V> e = i.next();
//			putForCreate(e.getKey(), e.getValue());
//		}
//	}

    /**
     * Removes the mapping for the specified key from this map if present.
     *
     * @param  key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
	//MOD
	// WCMEM = 0
    @MemSafe(risk = {MemoryRisk.UNREFERENCED_OBJ})
	public V remove(Object key) {
		
    	Entry<K, V> e = removeEntryForKey(key);
		V tempValue;
    	
		if (e == null) {
			return null;
		} else {
			tempValue = e.value;
			
			/* Return the entry to the entry pool */
			e.isFree = true;
			e.hash = 0;
			
			/*
			 * Key and Value objects should also be returned 
			 * to their respective pools
			 */
			e.key = null;
			e.value = null;
			e.next = null;
			return tempValue;
		}

	}

	/**
	 * Removes and returns the entry associated with the specified key in the
	 * SafeHashMap. Returns null if the SafeHashMap contains no mapping for this
	 * key.
	 */
    // MOD
	// WCMEM = 0
    @MemSafe(risk = {MemoryRisk.UNREFERENCED_OBJ})
	final Entry<K, V> removeEntryForKey(Object key) {
    	
		int hash = (key == null) ? 0 : hash(key.hashCode());
		int i = indexFor(hash, table.length);
		
		Entry<K, V> prev = table[i];
		Entry<K, V> e = prev;

		/*
		 * The bound for this loop will be equal to the capacity of the
		 * SafeHashMap, as in the worst case, for the worst hash function, all
		 * elements will collide into the same bucket table
		 */
		for (int j = 0; j < entries.length; j++) {
			if (e != null) {
				Entry<K, V> next = e.next;
				Object k;
				if (e.hash == hash
						&& ((k = e.key) == key || (key != null && key.equals(k)))) {
					modCount++;
					size--;
					if (prev == e)
						table[i] = next;
					else
						prev.next = next;
					e.recordRemoval(this);
					return e;
				}
				prev = e;
				e = next;
			}
		}

		return e;
	}

    /**
     * Special version of remove for EntrySet.
     */
    // MOD
	// WCMEM = 0
	final Entry<K, V> removeMapping(Object o) {

		if (!(o instanceof Map.Entry))
			return null;

		Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
		Object key = entry.getKey();

		int hash = (key == null) ? 0 : hash(key.hashCode());
		int i = indexFor(hash, table.length);

		Entry<K, V> prev = table[i];
		Entry<K, V> e = prev;

		/*
		 * The bound for this loop will be equal to the capacity of the
		 * SafeHashMap, as in the worst case, for the worst hash function, all
		 * elements will collide into the same bucket table
		 */
		for (int j = 0; j < entries.length; j++) {
			if (e != null) {
			Entry<K, V> next = e.next;
			if (e.hash == hash && e.equals(entry)) {
				modCount++;
				size--;
				if (prev == e)
					table[i] = next;
				else
					prev.next = next;
				e.recordRemoval(this);
				return e;
			}
			prev = e;
			e = next;
			}
		}

		/* Entry is returned to pool in the EntrySet.remove method */
		return e;
	}

	/**
	 * Removes all of the mappings from this map. The map will be empty after
	 * this call returns.
	 */
	// MOD
	// WCMEM = 0
	@MemSafe(risk = { MemoryRisk.OBJ_REF_TO_NULL })
	public void clear() {

		modCount++;
		for (int i = 0; i < table.length; i++) {
			Entry<K, V> e = table[i];

			/*
			 * The bound for this loop will be equal to the capacity of the
			 * SafeHashMap, as in the worst case, for the worst hash function,
			 * all elements will collide into the same index in the table
			 */
			for (int j = 0; j < entries.length; j++) {
				if (e != null) {
					Entry<K, V> next = e.next;
					e.clear();
					e = next;
				}
			}
		}
		size = 0;
	}

	/**
	 * Returns <tt>true</tt> if this map maps one or more keys to the specified
	 * value.
	 * 
	 * @param value
	 *            value whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map maps one or more keys to the specified
	 *         value
	 */
	// MOD
	// WCMEM = 0
	@MemSafe(risk = { MemoryRisk.NONE })
	public boolean containsValue(Object value) {
		
		if (value == null)
			return containsNullValue();

		Entry[] tab = table;
		for (int i = 0; i < tab.length; i++) {

			/*
			 * The bound for this loop will be equal to the capacity of the
			 * SafeHashMap, as in the worst case, for the worst hash function,
			 * all elements will collide into the same index in the table
			 */
			Entry e = tab[i];
			for (int j = 0; j < entries.length; j++) {
				if (e != null) {
					if (value.equals(e.value))
						return true;
					e = e.next;
				}
			}
		}
		
		return false;
	}

	/**
	 * Special-case code for containsValue with null argument
	 */
	// MOD
	// WCMEM = 0
	@MemSafe(risk = { MemoryRisk.NONE })
	private boolean containsNullValue() {

		Entry[] tab = table;
		for (int i = 0; i < tab.length; i++) {

			/*
			 * The bound for this loop will be equal to the capacity of the
			 * SafeHashMap, as in the worst case, for the worst hash function,
			 * all elements will collide into the same index in the table
			 */
			Entry e = tab[i];
			for (int j = 0; j < entries.length; j++) {
				if (e != null) {
					if (e.value == null)
						return true;
					e = e.next;
				}
			}
		}

		return false;
	}

	/**
	 * This inner class is the equivalent of SimpleEntry in SafeAbstractMap, both
	 * implement the inner class Map.Entry.
	 * 
	 * An Entry in a SafeHashMap is a structure that holds references to a
	 * key-value mapping.
	 * 
	 * @param <K>
	 * @param <V>
	 */
	// MOD
	static class Entry<K, V> implements Map.Entry<K, V> {
		// final K key;
		K key;
		V value;
		Entry<K, V> next;
		// final int hash;
		int hash;

		/*
		 * Additional field to indicate if the Entry has a key-value
		 * mapping. Only checking for value==null, key==null, hash==0 is not
		 * enough as those are the values of a null key-value mapping added
		 * to the SafeHashMap
		 */
		boolean isFree = true;

		/**
		 * Creates new entry.
		 */
        // WCMEM = 0
		Entry(int h, K k, V v, Entry<K, V> n) {
			value = v;
			next = n;
			key = k;
			hash = h;
		}

        // WCMEM = 0
		Entry() {
			value = null;
			next = null;
			key = null;
			hash = 0;
		}

        // WCMEM = 0
		public final K getKey() {
			return key;
		}

        // WCMEM = 0
		public final V getValue() {
			return value;
		}

        // WCMEM = 0
		@MemSafe(risk = { MemoryRisk.UNREFERENCED_OBJ })
		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		// WCMEM depends on the implementation of key's equals method
		public final boolean equals(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
			Map.Entry e = (Map.Entry) o;
			Object k1 = getKey();
			Object k2 = e.getKey();
			if (k1 == k2 || (k1 != null && k1.equals(k2))) {
				Object v1 = getValue();
				Object v2 = e.getValue();
				if (v1 == v2 || (v1 != null && v1.equals(v2)))
					return true;
			}
			return false;
		}

        // WCMEM = 0
		public final int hashCode() {
			return (key == null ? 0 : key.hashCode())
					^ (value == null ? 0 : value.hashCode());
		}

		public final String toString() {
			return getKey() + "=" + getValue();
		}

		/**
		 * This method is invoked whenever the value in an entry is overwritten
		 * by an invocation of put(k,v) for a key k that's already in the
		 * HashMap.
		 */
	    // WCMEM = 0
		void recordAccess(SafeHashMap<K, V> m) {
		}

		/**
		 * This method is invoked whenever the entry is removed from the table.
		 */
	    // WCMEM = 0
		void recordRemoval(SafeHashMap<K, V> m) {

		}

		/**
		 * Additional method that resets the state of an entry
		 */
		// MOD
        // WCMEM = 0
		void clear() {
			value = null;
			next = null;
			key = null;
			hash = 0;
			isFree = true;
		}
	}

    /**
     * Adds a new entry with the specified key, value and hash code to
     * the specified bucket.  It is the responsibility of this
     * method to resize the table if appropriate.
     *
     * Subclass overrides this to alter the behavior of put method.
     */
	// MOD
	// WCMEM = 9
	@MemSafe(risk = { MemoryRisk.MIXED_CONTEXT, MemoryRisk.RESIZE })
	void addEntry(int hash, K key, V value, int tableIndex) {

		Entry<K, V> e = table[tableIndex];

		if (entries.length < size)
			throw noFreeEntriesExc;

		/* Get a free entry from the pool of entries */
		for (int i = 0; i < entries.length; i++) {
			if (entries[i].isFree) {
				table[tableIndex] = entries[i];
				// System.out.println("Found free entry");
				break;
			}
		}

		table[tableIndex].hash = hash;
		table[tableIndex].key = key;
		table[tableIndex].value = value;
		table[tableIndex].isFree = false;
		table[tableIndex].next = e;

		/* Increase size only if we find a free entry */
		size++;

	}
    
	/**
	 * 
	 * Iterates over the non-empty hash buckets. It is used base for the Value,
	 * Key, and Entry iterators whom only implement the next() method according
	 * to their particular needs.
	 * 
	 * @param <E>
	 */
	private abstract class HashIterator<E> implements Iterator<E> {
		
		/* Next entry to return */
		Entry<K, V> next;

		/* For fast-fail */
		int expectedModCount;
		
		/* Current slot in the bucket table */
		int index;
		
		/* Current entry */
		Entry<K, V> current;
		
		HashIterator() {
			
			expectedModCount = modCount;
			if (size > 0) {
				/* Advance to first non-null entry */
				Entry[] t = table;
				while (index < t.length && (next = t[index++]) == null)
					;
			}
		}

		public final boolean hasNext() {
			return next != null;
		}

		final Entry<K, V> nextEntry() {
			
			if (modCount != expectedModCount){
				throw concModExc;
			}

			Entry<K, V> e = next;
			
			if (e == null){
				throw noSuchElemExc; 
			}
			
			if ((next = e.next) == null) {
				/* Advance to the next non-null entry */
				Entry[] t = table;
				while (index < t.length && (next = t[index++]) == null)
					;
			}
			current = e;
			
			return e;
		}

		//MOD
		public void remove() {
			
			if (current == null)
				throw illegalStateExc;
			
			if (modCount != expectedModCount)
				throw concModExc;
			
			Object k = current.key;
			current = null;
			SafeHashMap.this.removeEntryForKey(k).clear();
			
			expectedModCount = modCount;
		}

	}

    private final class ValueIterator extends HashIterator<V> {
        public V next() {
            return nextEntry().value;
        }
    }

    private final class KeyIterator extends HashIterator<K> {
        public K next() {
            return nextEntry().getKey();
        }
    }

    private final class EntryIterator extends HashIterator<Map.Entry<K,V>> {
        public Map.Entry<K,V> next() {
            return nextEntry();
        }
    }

    // Subclass overrides these to alter behavior of views' iterator() method
    // WCMEM = 12
    Iterator<K> newKeyIterator()   {
        return new KeyIterator();
    }
    // WCMEM = 12
    Iterator<V> newValueIterator()   {
        return new ValueIterator();
    }
    // WCMEM = 12
    Iterator<Map.Entry<K,V>> newEntryIterator()   {
        return new EntryIterator();
    }


    // Views

    private volatile Set<Map.Entry<K,V>> entrySet;

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation), the results of
     * the iteration are undefined.  The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
     * operations.  It does not support the <tt>add</tt> or <tt>addAll</tt>
     * operations.
     */
    // MOD
    @MemSafe(risk = {MemoryRisk.LAZY})
    public Set<K> keySet() {
    	return keySet;
    }

    private final class KeySet extends SafeAbstractSet<K> {
    	
    	// WCMEM = 12
        public Iterator<K> iterator() {
            return newKeyIterator();
        }
        
        // WCMEM = 0
        public int size() {
            return size;
        }
        
        // WCMEM = 0
        public boolean contains(Object o) {
            return containsKey(o);
        }
        
        // MOD
        // WCMEM = 0
        public boolean remove(Object o) {
        	
        	Entry<K,V> e = SafeHashMap.this.removeEntryForKey(o);
        	
    		if (e == null) {
    			return false;
    		} else {
    			/* Return the entry to the entry pool */
    			e.isFree = true;
    			e.hash = 0;
    			/*
    			 * Key and Value objects should also be returned 
    			 * to their respective pools
    			 */
    			e.key = null;
    			e.value = null;
    			e.next = null;
    			return true;
    		}
        }
        
        // WCMEM = 0
        public void clear() {
            SafeHashMap.this.clear();
        }
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are
     * reflected in the collection, and vice-versa.  If the map is
     * modified while an iteration over the collection is in progress
     * (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined.  The collection
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
     * support the <tt>add</tt> or <tt>addAll</tt> operations.
     */
    // MOD
    // WCMEM = 0
    @MemSafe(risk = {MemoryRisk.LAZY})
    public Collection<V> values() {
    	return values;
    }

    private final class Values extends SafeAbstractCollection<V> {
    	
    	// WCMEM = 12
        public Iterator<V> iterator() {
            return newValueIterator();
        }
        
        // WCMEM = 0
        public int size() {
            return size;
        }
        
        // WCMEM = 0
        public boolean contains(Object o) {
            return containsValue(o);
        }
        
        // WCMEM = 0
        public void clear() {
            SafeHashMap.this.clear();
        }
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map.
     * The set is backed by the map, so changes to the map are
     * reflected in the set, and vice-versa.  If the map is modified
     * while an iteration over the set is in progress (except through
     * the iterator's own <tt>remove</tt> operation, or through the
     * <tt>setValue</tt> operation on a map entry returned by the
     * iterator) the results of the iteration are undefined.  The set
     * supports element removal, which removes the corresponding
     * mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations.  It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    // MOD
    // WCMEM = 0
    @MemSafe(risk = {MemoryRisk.LAZY})
	public Set<Map.Entry<K, V>> entrySet() {
    	return entrySet;
	}

    private final class EntrySet extends SafeAbstractSet<Map.Entry<K,V>> {
    	
        // WCMEM = 12
        public Iterator<Map.Entry<K,V>> iterator() {
            return newEntryIterator();
        }
        
        public boolean contains(Object o) {
        	
            if (!(o instanceof Map.Entry))
                return false;
            
            Map.Entry<K,V> e = (Map.Entry<K,V>) o;
            Entry<K,V> candidate = getEntry(e.getKey());
            
            return candidate != null && candidate.equals(e);
        }
        
        // MOD
        // WCMEM = 0
        public boolean remove(Object o) {
			Entry<K, V> e = removeMapping(o);
			if (e != null)
				e.clear();
			return e != null;
		}
        
        // WCMEM = 0
        public int size() {
            return size;
        }
        
        // WCMEM = 0
        public void clear() {
            SafeHashMap.this.clear();
        }
    }

    
    // Only used for tests, it should disappear
    public int   capacity()     { return table.length; }
    public double getLoadFactor() { return (size*100)/table.length; }

}
