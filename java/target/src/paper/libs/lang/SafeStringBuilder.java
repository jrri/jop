package libs.lang;

//import java.io.IOException;

import javax.safetycritical.annotate.Phase;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

/*
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/**
 * A mutable sequence of characters. This class provides an API compatible with
 * <code>StringBuffer</code>, but with no guarantee of synchronization. This
 * class is designed for use as a drop-in replacement for
 * <code>StringBuffer</code> in places where the string buffer was being used by
 * a single thread (as is generally the case). Where possible, it is recommended
 * that this class be used in preference to <code>StringBuffer</code> as it will
 * be faster under most implementations.
 * 
 * <p>
 * The principal operations on a <code>SafeAbstractStringBuilder</code> are the
 * <code>append</code> and <code>insert</code> methods, which are overloaded so
 * as to accept data of any type. Each effectively converts a given datum to a
 * string and then appends or inserts the characters of that string to the
 * string builder. The <code>append</code> method always adds these characters
 * at the end of the builder; the <code>insert</code> method adds the characters
 * at a specified point.
 * <p>
 * For example, if <code>z</code> refers to a string builder object whose
 * current contents are "<code>start</code>", then the method call
 * <code>z.append("le")</code> would cause the string builder to contain "
 * <code>startle</code>", whereas <code>z.insert(4, "le")</code> would alter the
 * string builder to contain "<code>starlet</code>".
 * <p>
 * In general, if sb refers to an instance of a <code>SafeAbstractStringBuilder</code>, then
 * <code>sb.append(x)</code> has the same effect as
 * <code>sb.insert(sb.length(),&nbsp;x)</code>.
 * 
 * Every string builder has a capacity. As long as the length of the character
 * sequence contained in the string builder does not exceed the capacity, it is
 * not necessary to allocate a new internal buffer. If the internal buffer
 * overflows, it is automatically made larger.
 * 
 * <p>
 * Instances of <code>SafeAbstractStringBuilder</code> are not safe for use by multiple
 * threads. If such synchronization is required then it is recommended that
 * {@link java.lang.StringBuffer} be used.
 * 
 * @author Michael McCloskey
 * @version %I%, %G%
 * @see java.lang.StringBuffer
 * @see java.lang.String
 * @since 1.5
 */
@SCJAllowed
public final class SafeStringBuilder extends SafeAbstractStringBuilder implements
		java.io.Serializable, CharSequence {

	/** use serialVersionUID for interoperability */
	static final long serialVersionUID = 4383685877147921099L;

	/**
	 * Constructs a string builder with no characters in it and an initial
	 * capacity of 16 characters.
	 * 
	 * Does not allow "this" to escape local variables. Allocates internal
	 * structure of sufficient size to represent 16 characters in the scope of
	 * "this".
	 * 
	 * Memory behavior: This constructor may allocate objects within the same
	 * MemoryArea that holds the implicit this argument
	 * 
	 */
	// WCMEM = 30
	@SCJAllowed
	@SCJRestricted(phase = Phase.ALL, maySelfSuspend = true, mayAllocate = true)
	public SafeStringBuilder() {
		super(16);
	}

	/**
	 * Constructs a string builder with no characters in it and an initial
	 * capacity specified by the <code>capacity</code> argument.
	 * 
	 * Does not allow "this" to escape local variables. Allocates internal
	 * structure of sufficient size to represent "capacity" characters in the
	 * scope of "this".
	 * 
	 * Memory behavior: This constructor may allocate objects within the same
	 * MemoryArea that holds the implicit this argument
	 * 
	 * @param capacity
	 *            the initial capacity.
	 * @throws NegativeArraySizeException
	 *             if the <code>capacity</code> argument is less than
	 *             <code>0</code>.
	 */
	// WCMEM = 14 + capacity
	@SCJAllowed
	@SCJRestricted(phase = Phase.ALL, maySelfSuspend = true, mayAllocate = true)
	public SafeStringBuilder(int capacity) {
		super(capacity);
	}

	/**
	 * Constructs a string builder initialized to the contents of the specified
	 * string. The initial capacity of the string builder is <code>16</code>
	 * plus the length of the string argument.
	 * 
	 * Does not allow "this" to escape local variables. Allocates a character
	 * internal structure of sufficient size to represent str.length() + 16
	 * characters within the scope of "this".
	 * 
	 * Memory behavior: This constructor may allocate objects within the same
	 * MemoryArea that holds the implicit this argument
	 * 
	 * @param str
	 *            the initial contents of the buffer.
	 * @throws NullPointerException
	 *             if <code>str</code> is <code>null</code>
	 */
	// WCMEM = 30 + str.length(), append cannot fail, char array is long enough when created
	@SCJAllowed
	@SCJRestricted(phase = Phase.ALL, maySelfSuspend = true, mayAllocate = true)
	public SafeStringBuilder(String str) {
		super(str.length() + 16);
		append(str);
	}

	/**
	 * Constructs a string builder that contains the same characters as the
	 * specified <code>CharSequence</code>. The initial capacity of the string
	 * builder is <code>16</code> plus the length of the
	 * <code>CharSequence</code> argument.
	 * 
	 * Does not allow "this" to escape local variables. Allocates a character
	 * internal structure of sufficient size to represent seq.length() + 16
	 * characters within the scope of "this".
	 * 
	 * Memory behavior: This constructor may allocate objects within the same
	 * MemoryArea that holds the implicit this argument
	 * 
	 * @param seq
	 *            the sequence to copy.
	 * @throws NullPointerException
	 *             if <code>seq</code> is <code>null</code>
	 */
	// WCMEM = 30 + seq.length(), append cannot fail at construction
	@SCJAllowed
	@SCJRestricted(phase = Phase.ALL, maySelfSuspend = true, mayAllocate = true)
	public SafeStringBuilder(CharSequence seq) {
		this(seq.length() + 16);
		append(seq);
	}

	/**
	 * @see java.lang.String#valueOf(java.lang.Object)
	 * @see #append(java.lang.String)
	 */
	// Does not work on JOP
	@SCJAllowed
	@SCJRestricted(phase = Phase.ALL, maySelfSuspend = true, mayAllocate = true)
	public SafeStringBuilder append(Object obj) {
		return append(String.valueOf((char[]) obj));
	}

	// WCMEM = 9 (from exception)
	// BCMEM = 0
	public SafeStringBuilder append(String str) {
		super.append(str);
		return this;
	}

	// Appends the specified string builder to this sequence.
	// MOD
	// WCMEM = 9 (from exception)
	// BCMEM = 0
	private SafeAbstractStringBuilder append(SafeAbstractStringBuilder sb) {
		
		if (sb == null)
			return append("null");
		
		int len = sb.length();
		int newcount = count + len;
		
		if (newcount > value.length)
			throw new IllegalStateException("Interal character array size exceeded");
		// expandCapacity(newcount);
		
		sb.getChars(0, len, value, count);
		count = newcount;
		return this;
	}

//	/**
//	 * Appends the specified <tt>StringBuffer</tt> to this sequence.
//	 * <p>
//	 * The characters of the <tt>StringBuffer</tt> argument are appended, in
//	 * order, to this sequence, increasing the length of this sequence by the
//	 * length of the argument. If <tt>sb</tt> is <tt>null</tt>, then the four
//	 * characters <tt>"null"</tt> are appended to this sequence.
//	 * <p>
//	 * Let <i>n</i> be the length of this character sequence just prior to
//	 * execution of the <tt>append</tt> method. Then the character at index
//	 * <i>k</i> in the new character sequence is equal to the character at index
//	 * <i>k</i> in the old character sequence, if <i>k</i> is less than
//	 * <i>n</i>; otherwise, it is equal to the character at index <i>k-n</i> in
//	 * the argument <code>sb</code>.
//	 * 
//	 * @param sb
//	 *            the <tt>StringBuffer</tt> to append.
//	 * @return a reference to this object.
//	 */
//	public SafeAbstractStringBuilder append(StringBuffer sb) {
//		super.append(sb);
//		return this;
//	}

	/**
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	public SafeAbstractStringBuilder append(CharSequence s) {
		if (s == null)
			s = "null";
		
		if (s instanceof String)
			return this.append((String) s);
		
		// StringBuffer not part of SCJ spec.
		// if (s instanceof StringBuffer)
		// return this.append((StringBuffer) s);
		
		if (s instanceof SafeAbstractStringBuilder)
			return this.append((SafeAbstractStringBuilder) s);
		
		return this.append(s, 0, s.length());
	}

	/**
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	public SafeAbstractStringBuilder append(CharSequence s, int start, int end) {
		super.append(s, start, end);
		return this;
	}

	public SafeAbstractStringBuilder append(char str[]) {
		super.append(str);
		return this;
	}

	public SafeAbstractStringBuilder append(char str[], int offset, int len) {
		super.append(str, offset, len);
		return this;
	}

	/**
	 * @see java.lang.String#valueOf(boolean)
	 * @see #append(java.lang.String)
	 */
	// WCMEM = 9, if exception
	// BCMEM = 0
	public SafeAbstractStringBuilder append(boolean b) {
		super.append(b);
		return this;
	}

	// WCMEM = 9, if exception
	// BCMEM = 0
	public SafeAbstractStringBuilder append(char c) {
		super.append(c);
		return this;
	}

	/**
	 * @see java.lang.String#valueOf(int)
	 * @see #append(java.lang.String)
	 */
	// WCMEM = 212
	public SafeAbstractStringBuilder append(int i) {
		super.append(i);
		return this;
	}

	/**
	 * @see java.lang.String#valueOf(long)
	 * @see #append(java.lang.String)
	 */
	// WCMEM = 212
	public SafeAbstractStringBuilder append(long lng) {
		super.append(lng);
		return this;
	}

	/**
	 * @see java.lang.String#valueOf(float)
	 * @see #append(java.lang.String)
	 */
	public SafeAbstractStringBuilder append(float f) {
		super.append(f);
		return this;
	}

	/**
	 * @see java.lang.String#valueOf(double)
	 * @see #append(java.lang.String)
	 */
	public SafeAbstractStringBuilder append(double d) {
		super.append(d);
		return this;
	}

//	/**
//	 * @since 1.5
//	 */
//	public SafeAbstractStringBuilder appendCodePoint(int codePoint) {
//		super.appendCodePoint(codePoint);
//		return this;
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 */
//	public SafeAbstractStringBuilder delete(int start, int end) {
//		super.delete(start, end);
//		return this;
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 */
//	public SafeAbstractStringBuilder deleteCharAt(int index) {
//		super.deleteCharAt(index);
//		return this;
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 */
//	public SafeAbstractStringBuilder replace(int start, int end, String str) {
//		super.replace(start, end, str);
//		return this;
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 */
//	public SafeAbstractStringBuilder insert(int index, char str[], int offset, int len) {
//		super.insert(index, str, offset, len);
//		return this;
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 * @see java.lang.String#valueOf(java.lang.Object)
//	 * @see #insert(int, java.lang.String)
//	 * @see #length()
//	 */
//	public SafeAbstractStringBuilder insert(int offset, Object obj) {
//		return insert(offset, String.valueOf(obj));
//	}

	/**
	 * @throws StringIndexOutOfBoundsException
	 *             {@inheritDoc}
	 * @see #length()
	 */
	public SafeAbstractStringBuilder insert(int offset, String str) {
		super.insert(offset, str);
		return this;
	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 */
//	public SafeAbstractStringBuilder insert(int offset, char str[]) {
//		super.insert(offset, str);
//		return this;
//	}

//	/**
//	 * @throws IndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 */
//	public SafeAbstractStringBuilder insert(int dstOffset, CharSequence s) {
//		if (s == null)
//			s = "null";
//		if (s instanceof String)
//			return this.insert(dstOffset, (String) s);
//		return this.insert(dstOffset, s, 0, s.length());
//	}

//	/**
//	 * @throws IndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 */
//	public SafeAbstractStringBuilder insert(int dstOffset, CharSequence s, int start,
//			int end) {
//		super.insert(dstOffset, s, start, end);
//		return this;
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 * @see java.lang.String#valueOf(boolean)
//	 * @see #insert(int, java.lang.String)
//	 * @see #length()
//	 */
//	public SafeAbstractStringBuilder insert(int offset, boolean b) {
//		super.insert(offset, b);
//		return this;
//	}

//	/**
//	 * @throws IndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 * @see #length()
//	 */
//	public SafeAbstractStringBuilder insert(int offset, char c) {
//		super.insert(offset, c);
//		return this;
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 * @see java.lang.String#valueOf(int)
//	 * @see #insert(int, java.lang.String)
//	 * @see #length()
//	 */
//	public SafeAbstractStringBuilder insert(int offset, int i) {
//		return insert(offset, String.valueOf(i));
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 * @see java.lang.String#valueOf(long)
//	 * @see #insert(int, java.lang.String)
//	 * @see #length()
//	 */
//	public SafeAbstractStringBuilder insert(int offset, long l) {
//		return insert(offset, String.valueOf(l));
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 * @see java.lang.String#valueOf(float)
//	 * @see #insert(int, java.lang.String)
//	 * @see #length()
//	 */
//	public SafeAbstractStringBuilder insert(int offset, float f) {
//		return insert(offset, String.valueOf(f));
//	}

//	/**
//	 * @throws StringIndexOutOfBoundsException
//	 *             {@inheritDoc}
//	 * @see java.lang.String#valueOf(double)
//	 * @see #insert(int, java.lang.String)
//	 * @see #length()
//	 */
//	public SafeAbstractStringBuilder insert(int offset, double d) {
//		return insert(offset, String.valueOf(d));
//	}

	/**
	 * @throws NullPointerException
	 *             {@inheritDoc}
	 */
	// WCMEM = 6 + value.length 
	public int indexOf(String str) {
		return indexOf(str, 0);
	}

	/**
	 * @throws NullPointerException
	 *             {@inheritDoc}
	 */
	// WCMEM = 6 + value.length 
	public int indexOf(String str, int fromIndex) {
		return String.indexOf(value, 0, count, str.toCharArray(), 0,
				str.length(), fromIndex);
	}

	/**
	 * @throws NullPointerException
	 *             {@inheritDoc}
	 */
	// WCMEM = 6 + value.length 
	public int lastIndexOf(String str) {
		return lastIndexOf(str, count);
	}

	/**
	 * @throws NullPointerException
	 *             {@inheritDoc}
	 */
	// WCMEM = 6 + value.length
	public int lastIndexOf(String str, int fromIndex) {
		return String.lastIndexOf(value, 0, count, str.toCharArray(), 0,
				str.length(), fromIndex);
	}

//	public SafeAbstractStringBuilder reverse() {
//		super.reverse();
//		return this;
//	}

	public String toString() {
		// Create a copy, don't share the array
		return new String(value, 0, count);
	}

//	/**
//	 * Save the state of the <tt>SafeAbstractStringBuilder</tt> instance to a stream (that
//	 * is, serialize it).
//	 * 
//	 * @serialData the number of characters currently stored in the string
//	 *             builder (<tt>int</tt>), followed by the characters in the
//	 *             string builder (<tt>char[]</tt>). The length of the
//	 *             <tt>char</tt> array may be greater than the number of
//	 *             characters currently stored in the string builder, in which
//	 *             case extra characters are ignored.
//	 */
//	private void writeObject(java.io.ObjectOutputStream s)
//			throws java.io.IOException {
//		s.defaultWriteObject();
//		s.writeInt(count);
//		s.writeObject(value);
//	}
//
//	/**
//	 * readObject is called to restore the state of the StringBuffer from a
//	 * stream.
//	 */
//	private void readObject(java.io.ObjectInputStream s)
//			throws java.io.IOException, ClassNotFoundException {
//		s.defaultReadObject();
//		count = s.readInt();
//		value = (char[]) s.readObject();
//	}

//	@Override
//	public Appendable append(java.lang.CharSequence csq) throws IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	public Appendable append(java.lang.CharSequence csq, int start, int end)
//			throws IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
