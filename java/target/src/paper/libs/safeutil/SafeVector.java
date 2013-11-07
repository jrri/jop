/*
 * Copyright (c) 1994, 2007, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package libs.safeutil;
import libs.io.Serializable;
import libs.lang.Cloneable;
import libs.safeutil.extras.PoolObject;

/**
 * The {@code Vector} class implements a growable array of
 * objects. Like an array, it contains components that can be
 * accessed using an integer index. However, the size of a
 * {@code Vector} can grow or shrink as needed to accommodate
 * adding and removing items after the {@code Vector} has been created.
 *
 * <p>Each vector tries to optimize storage management by maintaining a
 * {@code capacity} and a {@code capacityIncrement}. The
 * {@code capacity} is always at least as large as the vector
 * size; it is usually larger because as components are added to the
 * vector, the vector's storage increases in chunks the size of
 * {@code capacityIncrement}. An application can increase the
 * capacity of a vector before inserting a large number of
 * components; this reduces the amount of incremental reallocation.
 *
 * <p><a name="fail-fast"/>
 * The iterators returned by this class's {@link #iterator() iterator} and
 * {@link #listIterator(int) listIterator} methods are <em>fail-fast</em>:
 * if the vector is structurally modified at any time after the iterator is
 * created, in any way except through the iterator's own
 * {@link ListIterator#remove() remove} or
 * {@link ListIterator#add(Object) add} methods, the iterator will throw a
 * {@link ConcurrentModificationException}.  Thus, in the face of
 * concurrent modification, the iterator fails quickly and cleanly, rather
 * than risking arbitrary, non-deterministic behavior at an undetermined
 * time in the future.  The {@link Enumeration Enumerations} returned by
 * the {@link #elements() elements} method are <em>not</em> fail-fast.
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw {@code ConcurrentModificationException} on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness:  <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 *
 * <p>As of the Java 2 platform v1.2, this class was retrofitted to
 * implement the {@link List} interface, making it a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html"> Java
 * Collections Framework</a>.  Unlike the new collection
 * implementations, {@code Vector} is synchronized.
 * 
 * This implementation is missing the following methods:
 * 
 * public Vector(int initialCapacity, int capacityIncrement)
 * public synchronized void ensureCapacity(int minCapacity)
 * private void ensureCapacityHelper(int minCapacity)
 * public synchronized void setSize(int newSize)
 * public synchronized void trimToSize()
 * 
 * public Vector(Collection<? extends E> c)
 * public synchronized boolean addAll(Collection<? extends E> c)
 * public synchronized boolean addAll(int index, Collection<? extends E> c)
 * public synchronized boolean containsAll(Collection<?> c)
 * public synchronized boolean removeAll(Collection<?> c)
 * public synchronized boolean retainAll(Collection<?> c)
 * 
 * public synchronized Object clone()
 * 
 * public synchronized List<E> subList(int fromIndex, int toIndex)
 * public synchronized <T> T[] toArray(T[] a)
 * 
 * private synchronized void writeObject(java.io.ObjectOutputStream s)
 *
 * @author  Lee Boynton
 * @author  Jonathan Payne
 * @see Collection
 * @see List
 * @see ArrayList
 * @see LinkedList
 * @since   JDK1.0
 */
public class SafeVector<E extends PoolObject> extends SafeAbstractList<E> implements
		List<E>, RandomAccess, Serializable, Cloneable
{
    /**
     * The array buffer into which the components of the vector are
     * stored. The capacity of the vector is the length of this array buffer,
     * and is large enough to contain all the vector's elements.
     *
     * <p>Any array elements following the last element in the Vector are null.
     *
     * @serial
     */
	protected final PoolObject[] elementData;

    /**
     * The number of valid components in this {@code Vector} object.
     * Components {@code elementData[0]} through
     * {@code elementData[elementCount-1]} are the actual items.
     *
     * @serial
     */
    protected int elementCount;

	/**
	 * Default capacity when the constructor is called with an empty argument.
	 * The capacity will not grow when trying to add more than
	 * <code>DEFAULT_CAPACITY</code> elements.
	 */
	protected static final int DEFAULT_CAPACITY = 10;
	
	static final ArrayIndexOutOfBoundsException biggerThanElemCntExc;
	static final IndexOutOfBoundsException indexExc;
	static final IllegalStateException maxCapExc;
	static final IllegalArgumentException initCapExc;
	
	static {
		biggerThanElemCntExc = new ArrayIndexOutOfBoundsException(
				"Index bigger or equal to the number of valid elemets");
		indexExc = new IndexOutOfBoundsException(
				"Index bigger or smaller than the number of valid elemets");
		maxCapExc = new IllegalStateException("Cannot add element");
		
		initCapExc = new IllegalArgumentException("Capacity smaller than zero");
		
	}
	
	/**
	 * Constructs an empty vector with the specified initial capacity.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the vector
	 * @throws IllegalArgumentException
	 *             if the specified initial capacity is negative
	 */
	// OK
	// WCMEM = 15 + initialCapacity
	// MOD
	public SafeVector(final int initialCapacity) {
		super();
		if (initialCapacity < 0)
			throw initCapExc;
		
		/* TODO: The field assignment has excessively high WCET */
		elementData = new PoolObject[initialCapacity];
	}

	/**
	 * Constructs an empty vector so that its internal data array
	 * has size {@code 10}. Once created with a specific capacity,
	 * the Vector internal array cannot be resized.
	 */
	// OK
	// WCMEM = 25
	public SafeVector() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Copies the components of this vector into the specified array.
	 * The item at index {@code k} in this vector is copied into
	 * component {@code k} of {@code anArray}.
	 *
	 * @param  anArray the array into which the components get copied
	 * @throws NullPointerException if the given array is null
	 * @throws IndexOutOfBoundsException if the specified array is not
	 *         large enough to hold all the components of this vector
	 * @throws ArrayStoreException if a component of this vector is not of
	 *         a runtime type that can be stored in the specified array
	 * @see #toArray(Object[])
	 */
	//TODO
	public synchronized void copyInto(Object[] anArray) {
		/* WCA tool can't find loop bounds on System.arraycopy */ 
		System.arraycopy(elementData, 0, anArray, 0, elementCount);
	}

	/**
	 * Returns the current capacity of this vector.
	 *
	 * @return  the current capacity (the length of its internal
	 *          data array, kept in the field {@code elementData}
	 *          of this vector)
	 */
	// OK
	// WCMEM = 0
	public synchronized int capacity() {
		return elementData.length;
	}

	/**
	 * Returns the number of components in this vector.
	 *
	 * @return  the number of components in this vector
	 */
	// OK
	// WCMEM = 0
	public synchronized int size() {
		return elementCount;
	}

	/**
	 * Tests if this vector has no components.
	 *
	 * @return  {@code true} if and only if this vector has
	 *          no components, that is, its size is zero;
	 *          {@code false} otherwise.
	 */
	// OK
	// WCMEM = 0
	public synchronized boolean isEmpty() {
		return elementCount == 0;
	}

	/**
	 * Returns an enumeration of the components of this vector. The
	 * returned {@code Enumeration} object will generate all items in
	 * this vector. The first item generated is the item at index {@code 0},
	 * then the item at index {@code 1}, and so on.
	 *
	 * @return  an enumeration of the components of this vector
	 * @see     Iterator
	 */
	// The returned enumeration object is located in the scope of the caller
	// TEST = OK
	// WCMEM = 8
	// MOD
	public Enumeration<E> elements() {
		
		return new Enumeration<E>() {
			int count = 0;

			public boolean hasMoreElements() {
				return count < elementCount;
			}

			public E nextElement() {
				synchronized (SafeVector.this) {
					if (count < elementCount) {
						return elementData(count++);
					}
				}
				throw noSuchElemExc;
			}
		};
	}

	/**
	 * Returns {@code true} if this vector contains the specified element.
	 * More formally, returns {@code true} if and only if this vector
	 * contains at least one element {@code e} such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
	 *
	 * @param o element whose presence in this vector is to be tested
	 * @return {@code true} if this vector contains the specified element
	 */
	// TEST = OK
	// WCET = 404 + 50*n, where n is the number of elements in the collection
	// WCMEM = 
	// MOD
	public boolean contains(E o) {
		return indexOf(o, 0) >= 0;
	}

	/**
	 * Returns the index of the first occurrence of the specified element
	 * in this vector, or -1 if this vector does not contain the element.
	 * More formally, returns the lowest index {@code i} such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 *
	 * @param o element to search for
	 * @return the index of the first occurrence of the specified element in
	 *         this vector, or -1 if this vector does not contain the element
	 */
	// WCET = 391 + 50*n, where n is the number of elements in the collection
	public int indexOf(E o) {
		return indexOf(o, 0);
	}

	/**
	 * Returns the index of the first occurrence of the specified element in
	 * this vector, searching forwards from {@code index}, or returns -1 if the
	 * element is not found. More formally, returns the lowest index {@code i}
	 * such that
	 * <tt>(i&nbsp;&gt;=&nbsp;index&nbsp;&amp;&amp;&nbsp;(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i))))</tt>
	 * , or -1 if there is no such index.
	 * 
	 * @param o
	 *            element to search for
	 * @param index
	 *            index to start searching from
	 * @return the index of the first occurrence of the element in this vector
	 *         at position {@code index} or later in the vector; {@code -1} if
	 *         the element is not found.
	 * @throws IndexOutOfBoundsException
	 *             if the specified index is negative
	 * @see Object#equals(Object)
	 */
	// MOD
	/* DFA can find bounds in the loops but "o.equals(elementData[i])" generates
	 * a call graph with cycles */ 
	public synchronized int indexOf(E o, int index) {
		if (o == null) {
			for (int i = index; i < elementData.length; i++)
				if (elementData[i] == null)
					return i;
		} else {
			for (int i = index; i < elementData.length; i++)
				if (o.equals(elementData[i]))
					return i;
		}
		return -1;
	}

	/**
	 * Returns the index of the last occurrence of the specified element
	 * in this vector, or -1 if this vector does not contain the element.
	 * More formally, returns the highest index {@code i} such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 *
	 * @param o element to search for
	 * @return the index of the last occurrence of the specified element in
	 *         this vector, or -1 if this vector does not contain the element
	 */
	// WCET = 481 + 39*n, where n is the number of elements in the collection 
	public synchronized int lastIndexOf(Object o) {
		return lastIndexOf(o, elementCount - 1);
	}

	/**
	 * Returns the index of the last occurrence of the specified element in
	 * this vector, searching backwards from {@code index}, or returns -1 if
	 * the element is not found.
	 * More formally, returns the highest index {@code i} such that
	 * <tt>(i&nbsp;&lt;=&nbsp;index&nbsp;&amp;&amp;&nbsp;(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i))))</tt>,
	 * or -1 if there is no such index.
	 *
	 * @param o element to search for
	 * @param index index to start searching backwards from
	 * @return the index of the last occurrence of the element at position
	 *         less than or equal to {@code index} in this vector;
	 *         -1 if the element is not found.
	 * @throws IndexOutOfBoundsException if the specified index is greater
	 *         than or equal to the current size of this vector
	 */
	/* DFA can find bounds in the loops but "o.equals(elementData[i])" generates
	 * a call graph with cycles */ 
	public synchronized int lastIndexOf(Object o, int index) {
		if (index >= elementCount)
			throw indexExc;

		if (o == null) {
			for (int i = index; i >= 0; i--)
				if (elementData[i] == null)
					return i;
		} else {
			for (int i = index; i >= 0; i--)
				if (o.equals(elementData[i]))
					return i;
		}
		return -1;
	}

	/**
	 * Returns the component at the specified index.
	 *
	 * <p>This method is identical in functionality to the {@link #get(int)}
	 * method (which is part of the {@link List} interface).
	 *
	 * @param      index   an index into this vector
	 * @return     the component at the specified index
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 *	       ({@code index < 0 || index >= size()})
	 */
	// WCMEM = 9
	// BCMEM = 0
	// WCET = 247
	// MOD
	public synchronized E elementAt(int index) {
		if (index >= elementCount) {
			throw biggerThanElemCntExc;
		}

		return elementData(index);
	}

	/**
	 * Returns the first component (the item at index {@code 0}) of
	 * this vector.
	 *
	 * @return     the first component of this vector
	 * @throws NoSuchElementException if this vector has no components
	 */
	// WCET = 238
	// MOD
	public synchronized E firstElement() {
		if (elementCount == 0) {
			throw noSuchElemExc;
		}
		return elementData(0);
	}

	/**
	 * Returns the last component of the vector.
	 *
	 * @return  the last component of the vector, i.e., the component at index
	 *          <code>size()&nbsp;-&nbsp;1</code>.
	 * @throws NoSuchElementException if this vector is empty
	 */
	// WCET = 250
	public synchronized E lastElement() {
		if (elementCount == 0) {
			throw noSuchElemExc;
		}
		return elementData(elementCount - 1);
	}

	/**
	 * Sets the component at the specified {@code index} of this
	 * vector to be the specified object. The previous component at that
	 * position is discarded.
	 *
	 * <p>The index must be a value greater than or equal to {@code 0}
	 * and less than the current size of the vector.
	 *
	 * <p>This method is identical in functionality to the
	 * {@link #set(int, Object) set(int, E)}
	 * method (which is part of the {@link List} interface). Note that the
	 * {@code set} method reverses the order of the parameters, to more closely
	 * match array usage.  Note also that the {@code set} method returns the
	 * old value that was stored at the specified position.
	 *
	 * @param      obj     what the component is to be set to
	 * @param      index   the specified index
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 *	       ({@code index < 0 || index >= size()})
	 */
	// WCET = 296 + WCET(terminate) + WCET(initialize) 
	// MOD
	// TODO review
	public synchronized void setElementAt(E obj, int index) {

		if (index >= elementCount)
			throw biggerThanElemCntExc;

		elementData[index].reset();
		elementData[index] = obj;
		obj.initialize();

	}

	/**
	 * Deletes the component at the specified index. Each component in
	 * this vector with an index greater or equal to the specified
	 * {@code index} is shifted downward to have an index one
	 * smaller than the value it had previously. The size of this vector
	 * is decreased by {@code 1}.
	 *
	 * <p>The index must be a value greater than or equal to {@code 0}
	 * and less than the current size of the vector. 
	 *
	 * <p>This method is identical in functionality to the {@link #remove(int)}
	 * method (which is part of the {@link List} interface).  Note that the
	 * {@code remove} method returns the old value that was stored at the
	 * specified position.
	 *
	 * @param      index   the index of the object to remove
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 *	       ({@code index < 0 || index >= size()})
	 */
	// WCET = 416 + WCET(returnToPool) + WCET(System.arrayCopy(n)
	// MOD
	public synchronized void removeElementAt(int index) {
		
		modCount++;
		if (index >= elementCount)
			throw biggerThanElemCntExc;

		returnToPool(elementData(index));

		int j = elementCount - index - 1;
		if (j > 0) {
			System.arraycopy(elementData, index + 1, elementData, index, j);
		}

		elementCount--;
		elementData[elementCount] = null;
	}

	/**
	 * Inserts the specified object as a component in this vector at the
	 * specified {@code index}. Each component in this vector with
	 * an index greater or equal to the specified {@code index} is
	 * shifted upward to have an index one greater than the value it had
	 * previously.
	 *
	 * <p>The index must be a value greater than or equal to {@code 0}
	 * and less than or equal to the current size of the vector. (If the
	 * index is equal to the current size of the vector, the new element
	 * is appended to the Vector.)
	 *
	 * <p>This method is identical in functionality to the
	 * {@link #add(int, Object) add(int, E)}
	 * method (which is part of the {@link List} interface).  Note that the
	 * {@code add} method reverses the order of the parameters, to more closely
	 * match array usage.
	 *
	 * @param      obj     the component to insert
	 * @param      index   where to insert the new component
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 *	       ({@code index < 0 || index > size()})
	 */
	// MOD
	public synchronized void insertElementAt(E obj, int index) {

		if (index > elementCount) {
			throw biggerThanElemCntExc;
		}

		if (elementCount < elementData.length) {
			modCount++;
			System.arraycopy(elementData, index, elementData, index + 1,
					elementCount - index);
			elementData[index] = obj;
			elementCount++;
		}
	}

	/**
	 * Adds the specified component to the end of this vector,
	 * increasing its size by one. The capacity of this vector is
	 * increased if its size becomes greater than its capacity.
	 *
	 * <p>This method is identical in functionality to the
	 * {@link #add(Object) add(E)}
	 * method (which is part of the {@link List} interface).
	 *
	 * @param   obj   the component to be added
	 */
	// MOD
	public synchronized void addElement(E obj) {
		add(obj);
	}

	/**
	 * Removes the first (lowest-indexed) occurrence of the argument
	 * from this vector. If the object is found in this vector, each
	 * component in the vector with an index greater or equal to the
	 * object's index is shifted downward to have an index one smaller
	 * than the value it had previously.
	 *
	 * <p>This method is identical in functionality to the
	 * {@link #remove(Object)} method (which is part of the
	 * {@link List} interface).
	 *
	 * @param   obj   the component to be removed
	 * @return  {@code true} if the argument was a component of this
	 *          vector; {@code false} otherwise.
	 */
	public synchronized boolean removeElement(Object obj) {
		modCount++;
		int i = indexOf((E) obj);
		if (i >= 0) {
			removeElementAt(i);
			return true;
		}
		return false;
	}

	/**
	 * Removes all components from this vector and sets its size to zero.
	 *
	 * <p>This method is identical in functionality to the {@link #clear}
	 * method (which is part of the {@link List} interface).
	 */
	// MOD
	public synchronized void removeAllElements() {
		modCount++;

		for (int i = 0; i < elementData.length; i++) {
			/* Restore entries into the pool */
			E element = elementData(i);
			if (element != null)
				returnToPool(elementData(i));
		}

		elementCount = 0;
	}

	/**
	 * Returns an array containing all of the elements in this Vector
	 * in the correct order.
	 *
	 * @since 1.2
	 */
	public synchronized Object[] toArray() {
		return SafeArrays.copyOf(elementData, elementCount);
	}

	/**
	 * Returns an array containing all of the elements in this Vector in the
	 * correct order; the runtime type of the returned array is that of the
	 * specified array.  If the Vector fits in the specified array, it is
	 * returned therein.  Otherwise, a new array is allocated with the runtime
	 * type of the specified array and the size of this Vector.
	 *
	 * <p>If the Vector fits in the specified array with room to spare
	 * (i.e., the array has more elements than the Vector),
	 * the element in the array immediately following the end of the
	 * Vector is set to null.  (This is useful in determining the length
	 * of the Vector <em>only</em> if the caller knows that the Vector
	 * does not contain any null elements.)
	 *
	 * @param a the array into which the elements of the Vector are to
	 *		be stored, if it is big enough; otherwise, a new array of the
	 * 		same runtime type is allocated for this purpose.
	 * @return an array containing the elements of the Vector
	 * @throws ArrayStoreException if the runtime type of a is not a supertype
	 * of the runtime type of every element in this Vector
	 * @throws NullPointerException if the given array is null
	 * @since 1.2
	 */
	//	public synchronized <T> T[] toArray(T[] a) {
	//		if (a.length < elementCount)
	//			return (T[]) Arrays.copyOf(elementData, elementCount, a.getClass());
	//
	//		System.arraycopy(elementData, 0, a, 0, elementCount);
	//
	//		if (a.length > elementCount)
	//			a[elementCount] = null;
	//
	//		return a;
	//	}

	// Positional Access Operations

	/**
	 * Returns the element at the specified position in this Vector.
	 *
	 * @param index index of the element to return
	 * @return object at the specified index
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 *            ({@code index < 0 || index >= size()})
	 * @since 1.2
	 */
	// MOD
	public synchronized E get(int index) {
		if (index >= elementCount)
			throw biggerThanElemCntExc;

		return elementData(index);
	}

	/**
	 * Replaces the element at the specified position in this Vector with the
	 * specified element.
	 *
	 * @param index index of the element to replace
	 * @param element element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 *	       ({@code index < 0 || index >= size()})
	 * @since 1.2
	 */
	// MOD
	public synchronized E set(int index, E element) {

		if (index >= elementCount)
			throw biggerThanElemCntExc;

		E oldValue = elementData(index);
		elementData[index] = element;
		oldValue.getPool().releasePoolObject(oldValue);

		return oldValue;
	}

	/**
	 * Appends the specified element to the end of this Vector.
	 * 
	 * @param e
	 *            element to be appended to this Vector
	 * @return {@code true} (as specified by {@link Collection#add})
	 * @throws IllegalStateException
	 *             if by adding the element the maximum capacity is exceeded
	 * @since 1.2
	 */
	// MOD
	public synchronized boolean add(E e) {

		if (elementCount < elementData.length) {
			modCount++;
			elementData[elementCount] = e;
			elementCount++;
			return true;
		} else {
			throw maxCapExc;
		}

	}

	/**
	 * Removes the first occurrence of the specified element in this Vector
	 * If the Vector does not contain the element, it is unchanged.  More
	 * formally, removes the element with the lowest index i such that
	 * {@code (o==null ? get(i)==null : o.equals(get(i)))} (if such
	 * an element exists).
	 *
	 * @param o element to be removed from this Vector, if present
	 * @return true if the Vector contained the specified element
	 * @since 1.2
	 */
	public boolean remove(Object o) {
		return removeElement(o);
	}

	/**
	 * Inserts the specified element at the specified position in this Vector.
	 * Shifts the element currently at that position (if any) and any
	 * subsequent elements to the right (adds one to their indices).
	 *
	 * @param index index at which the specified element is to be inserted
	 * @param element element to be inserted
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 *         ({@code index < 0 || index > size()})
	 * @since 1.2
	 */
	public void add(int index, E element) {
		insertElementAt(element, index);
	}

	/**
	 * Removes the element at the specified position in this Vector.
	 * Shifts any subsequent elements to the left (subtracts one from their
	 * indices).  Returns the element that was removed from the Vector.
	 *
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 *         ({@code index < 0 || index >= size()})
	 * @param index the index of the element to be removed
	 * @return element that was removed
	 * @since 1.2
	 */
	// MOD
	public synchronized E remove(int index) {

		modCount++;
		if (index >= elementCount)
			throw biggerThanElemCntExc;

		E oldValue = elementData(index);

		/* Return entry to pool */
		returnToPool(oldValue);

		int numMoved = elementCount - index - 1;
		if (numMoved > 0)
			System.arraycopy(elementData, index + 1, elementData, index,
					numMoved);
		elementCount--;
		elementData[elementCount] = null;

		return oldValue;
	}

	/**
	 * Removes all of the elements from this Vector.  The Vector will
	 * be empty after this call returns (unless it throws an exception).
	 *
	 * @since 1.2
	 */
	public void clear() {
		removeAllElements();
	}

	/**
	 * Compares the specified Object with this Vector for equality.  Returns
	 * true if and only if the specified Object is also a List, both Lists
	 * have the same size, and all corresponding pairs of elements in the two
	 * Lists are <em>equal</em>.  (Two elements {@code e1} and
	 * {@code e2} are <em>equal</em> if {@code (e1==null ? e2==null :
	 * e1.equals(e2))}.)  In other words, two Lists are defined to be
	 * equal if they contain the same elements in the same order.
	 *
	 * @param o the Object to be compared for equality with this Vector
	 * @return true if the specified Object is equal to this Vector
	 */
	// OK
	public synchronized boolean equals(Object o) {
		return super.equals(o);
	}

	/**
	 * Returns the hash code value for this Vector.
	 */
	public synchronized int hashCode() {
		return super.hashCode();
	}

	/**
	 * Returns a string representation of this Vector, containing
	 * the String representation of each element.
	 */
	// TODO: allocates an iterator
	public synchronized String toString() {
		return super.toString();
	}

	//    /**
	//     * Returns a view of the portion of this List between fromIndex,
	//     * inclusive, and toIndex, exclusive.  (If fromIndex and toIndex are
	//     * equal, the returned List is empty.)  The returned List is backed by this
	//     * List, so changes in the returned List are reflected in this List, and
	//     * vice-versa.  The returned List supports all of the optional List
	//     * operations supported by this List.
	//     *
	//     * <p>This method eliminates the need for explicit range operations (of
	//     * the sort that commonly exist for arrays).   Any operation that expects
	//     * a List can be used as a range operation by operating on a subList view
	//     * instead of a whole List.  For example, the following idiom
	//     * removes a range of elements from a List:
	//     * <pre>
	//     *	    list.subList(from, to).clear();
	//     * </pre>
	//     * Similar idioms may be constructed for indexOf and lastIndexOf,
	//     * and all of the algorithms in the Collections class can be applied to
	//     * a subList.
	//     *
	//     * <p>The semantics of the List returned by this method become undefined if
	//     * the backing list (i.e., this List) is <i>structurally modified</i> in
	//     * any way other than via the returned List.  (Structural modifications are
	//     * those that change the size of the List, or otherwise perturb it in such
	//     * a fashion that iterations in progress may yield incorrect results.)
	//     *
	//     * @param fromIndex low endpoint (inclusive) of the subList
	//     * @param toIndex high endpoint (exclusive) of the subList
	//     * @return a view of the specified range within this List
	//     * @throws IndexOutOfBoundsException if an endpoint index value is out of range
	//     *         {@code (fromIndex < 0 || toIndex > size)}
	//     * @throws IllegalArgumentException if the endpoint indices are out of order
	//     *	       {@code (fromIndex > toIndex)}
	//     */
	//    public synchronized List<E> subList(int fromIndex, int toIndex) {
	//        return Collections.synchronizedList(super.subList(fromIndex, toIndex),
	//                                            this);
	//    }

	/**
	 * Removes from this List all of the elements whose index is between
	 * fromIndex, inclusive and toIndex, exclusive.  Shifts any succeeding
	 * elements to the left (reduces their index).
	 * This call shortens the ArrayList by (toIndex - fromIndex) elements.  (If
	 * toIndex==fromIndex, this operation has no effect.)
	 *
	 * @param fromIndex index of first element to be removed
	 * @param toIndex index after last element to be removed
	 */
	// MOD
	protected synchronized void removeRange(int fromIndex, int toIndex) {
		modCount++;
		int numMoved = elementCount - toIndex;
		System.arraycopy(elementData, toIndex, elementData, fromIndex, numMoved);

		int newElementCount = elementCount - (toIndex - fromIndex);
		while (elementCount != newElementCount) {
			returnToPool(elementData(elementCount));
			--elementCount;
		}
	}

	private void returnToPool(E e) {
		e.getPool().releasePoolObject(e);
	}
	
	// WCMEM = 0
	E elementData(int index) {
		return (E) elementData[index];
	}
	
	
	/* Iterators */
	
    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be
     * returned by an initial call to {@link ListIterator#next next}.
     * An initial call to {@link ListIterator#previous previous} would
     * return the element with the specified index minus one.
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public synchronized ListIterator<E> listIterator(int index) {
        if (index < 0 || index > elementCount)
            throw indexExc;
        return new ListItr(index);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @see #listIterator(int)
     */
    public synchronized ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    public synchronized Iterator<E> iterator() {
        return new Itr();
    }
	
    /**
     * An optimized version of AbstractList.Itr
     */
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;

        public boolean hasNext() {
            // Racy but within spec, since modifications are checked
            // within or after synchronization in next/previous
            return cursor != elementCount;
        }

        public E next() {
            synchronized (SafeVector.this) {
                checkForComodification();
                int i = cursor;
                if (i >= elementCount)
                    throw noSuchElemExc;
                cursor = i + 1;
                return elementData(lastRet = i);
            }
        }

        public void remove() {
            if (lastRet == -1)
                throw illegalStateExc;;
            synchronized (SafeVector.this) {
                checkForComodification();
                SafeVector.this.remove(lastRet);
                expectedModCount = modCount;
            }
            cursor = lastRet;
            lastRet = -1;
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw concModExc;
        }
    }

    /**
     * An optimized version of AbstractList.ListItr
     */
    final class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public E previous() {
            synchronized (SafeVector.this) {
                checkForComodification();
                int i = cursor - 1;
                if (i < 0)
                    throw noSuchElemExc;
                cursor = i;
                return elementData(lastRet = i);
            }
        }

        public void set(E e) {
            if (lastRet == -1)
                throw illegalStateExc;;
            synchronized (SafeVector.this) {
                checkForComodification();
                SafeVector.this.set(lastRet, e);
            }
        }

        public void add(E e) {
            int i = cursor;
            synchronized (SafeVector.this) {
                checkForComodification();
                SafeVector.this.add(i, e);
                expectedModCount = modCount;
            }
            cursor = i + 1;
            lastRet = -1;
        }
    }

}
