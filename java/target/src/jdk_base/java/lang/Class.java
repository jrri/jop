/*
 * Copyright (c) 1994, 2012, Oracle and/or its affiliates. All rights reserved.
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

package java.lang;

import javax.safetycritical.annotate.SCJAllowed;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.GC;
import com.jopdesign.sys.JVMHelp;
import com.jopdesign.sys.Native;

public final class Class<T> implements java.io.Serializable {
	
	/** use serialVersionUID from JDK 1.1 for interoperability */
	private static final long serialVersionUID = 3206093459760846163L;

	// The memory address of the class info structure
	public int classRefAddress;

	public boolean _isArray = false;
	int arrayDimensions = 0;
	
	public boolean _isInterface = false;
	public int _interfaceNumber = 0;
	
	
	public boolean _isPrimitive = false;

	private final char _boolean = 'Z';
	private final char _char = 'C';
	private final char _byte = 'B';
	private final char _short = 'S';
	private final char _int = 'I';
	private final char _long = 'J';
	private final char _float = 'F';
	private final char _double = 'D';
	private final char _void = 'V';

	private final char _cl_int = 'L';

	public int primitiveType = 0;

	/**
	 * Package protected constructor to create Class objects at boot time.
	 */
	Class() {
	}

	/**
	 * Converts the object to a string. The string representation is the string
	 * "class" or "interface", followed by a number representing the reference
	 * of the class info structure the class represents
	 * 
	 * @return a string representation of this class object.
	 */
    @SCJAllowed
    public String toString() {
        return (isInterface() ? "interface " : (isPrimitive() ? "" : "class ")) + classRefAddress;
    }

	/**
	 * Creates a new instance of the class represented by this {@code Class}
	 * object. The class is instantiated as if by a {@code new} expression with
	 * an empty argument list.
	 * 
	 * @return a newly allocated instance of the class represented by this
	 *         object.
	 * @exception IllegalAccessException
	 *                if the class or its nullary constructor is not accessible.
	 * @exception InstantiationException
	 *                if this {@code Class} represents an abstract class, an
	 *                interface, an array class, a primitive type, or void; or
	 *                if the class has no nullary constructor; or if the
	 *                instantiation fails for some other reason.
	 */
    @SCJAllowed
	public T newInstance() throws InstantiationException,
			IllegalAccessException {
    	
    	// Pointer to <init> method of a no argument constructor
    	int initPtr = Native.rdMem(classRefAddress + Const.INIT_METH);
    	
    	int specialPointers = Native.rdMem(1);
    	int javaLangClass = Native.rdMem(specialPointers + Const.CLASS_CLASS_OFFSET);
    	
		if (classRefAddress == javaLangClass) {
			throw new IllegalAccessException(
					"Can not call newInstance() on the Class for java.lang.Class");
		}
    	
    	//TODO: Cannot see if it is abstract class
		if (_isInterface || _isPrimitive || (initPtr == 0))
			throw new InstantiationException(
					"Trying to instantiate an interface, primitive, or the class has no empty constructor");

		int newObj = GC.newObject(classRefAddress); 
		Native.invoke(newObj,initPtr);

		return (T) Native.toObject(newObj);
	}

    /**
     * Determines if the specified {@code Object} is assignment-compatible
     * with the object represented by this {@code Class}.  This method is
     * the dynamic equivalent of the Java language {@code instanceof}
     * operator. The method returns {@code true} if the specified
     * {@code Object} argument is non-null and can be cast to the
     * reference type represented by this {@code Class} object without
     * raising a {@code ClassCastException.} It returns {@code false}
     * otherwise.
     *
     * <p> Specifically, if this {@code Class} object represents a
     * declared class, this method returns {@code true} if the specified
     * {@code Object} argument is an instance of the represented class (or
     * of any of its subclasses); it returns {@code false} otherwise. If
     * this {@code Class} object represents an array class, this method
     * returns {@code true} if the specified {@code Object} argument
     * can be converted to an object of the array class by an identity
     * conversion or by a widening reference conversion; it returns
     * {@code false} otherwise. If this {@code Class} object
     * represents an interface, this method returns {@code true} if the
     * class or any superclass of the specified {@code Object} argument
     * implements this interface; it returns {@code false} otherwise. If
     * this {@code Class} object represents a primitive type, this method
     * returns {@code false}.
     *
     * @param   obj the object to check
     * @return  true if {@code obj} is an instance of this class
     *
     * @since JDK1.1
     */
	@SCJAllowed
	public boolean isInstance(Object obj) {

		if (_isPrimitive) {
			return false;
		}

		if (obj == null) {
			return false;
		}

		/*
		 * This is the same as the JVM.f_instanceof method. Such method is
		 * private so the code is repeated here
		 */

		// class info of the type this class represents
		int cons = this.classRefAddress;

		// start of class info
		int p = obj.getClass().classRefAddress;
		
		int res = 0;

		// check against interface
		int ifidx = Native.rdMem(cons + Const.CLASS_SUPER);

		if (ifidx < 0) {
			int iftab = Native.rdMem(p + Const.CLASS_IFTAB);
			if (iftab == 0) {
				// the class does not implement any interface
				return false;
			} else {
				// check if the appropriate bit is set
				int i = Native.rdMem(iftab - ((-ifidx + 31) >>> 5));
				res = (i >>> (~ifidx & 0x1f)) & 1;
				return res == 1 ? true : false;
			}
		}

		// search for superclass
		for (;;) {
			// always check this bound with TypeGraphTool!
			if (p == cons) { // @WCA loop <= 5
				return true;
			} else {
				p = Native.rdMem(p + Const.CLASS_SUPER);
				if (p == 0) {
					return false;
				}
			}
		}
	}

    /**
     * Determines if the specified {@code Class} object represents an
     * interface type.
     *
     * @return  {@code true} if this object represents an interface;
     *          {@code false} otherwise.
     */
    public boolean isInterface(){
    	return _isInterface;
    }


    /**
     * Determines if this {@code Class} object represents an array class.
     *
     * @return  {@code true} if this object represents an array class;
     *          {@code false} otherwise.
     * @since   JDK1.1
     */
    @SCJAllowed
    public boolean isArray(){
    	return _isArray;
    }


    /**
     * Determines if the specified {@code Class} object represents a
     * primitive type.
     *
     * <p> There are nine predefined {@code Class} objects to represent
     * the eight primitive types and void.  These are created by the Java
     * Virtual Machine, and have the same names as the primitive types that
     * they represent, namely {@code boolean}, {@code byte},
     * {@code char}, {@code short}, {@code int},
     * {@code long}, {@code float}, and {@code double}.
     *
     * <p> These objects may only be accessed via the following public static
     * final variables, and are the only {@code Class} objects for which
     * this method returns {@code true}.
     *
     * @return true if and only if this class represents a primitive type
     *
     * @see     java.lang.Boolean#TYPE
     * @see     java.lang.Character#TYPE
     * @see     java.lang.Byte#TYPE
     * @see     java.lang.Short#TYPE
     * @see     java.lang.Integer#TYPE
     * @see     java.lang.Long#TYPE
     * @see     java.lang.Float#TYPE
     * @see     java.lang.Double#TYPE
     * @see     java.lang.Void#TYPE
     * @since JDK1.1
     */
    @SCJAllowed
    public boolean isPrimitive(){
    	return _isPrimitive;
    }

    /**
     * Returns the {@code Class} representing the superclass of the entity
     * (class, interface, primitive type or void) represented by this
     * {@code Class}.  If this {@code Class} represents either the
     * {@code Object} class, an interface, a primitive type, or void, then
     * null is returned.  If this object represents an array class then the
     * {@code Class} object representing the {@code Object} class is
     * returned.
     *
     * @return the superclass of the class represented by this object.
     */
    @SCJAllowed
    public Class getSuperclass(){
    	
    	if(_isInterface || _isPrimitive || (this == JVMHelp.getClassHelper(Object.class)))
    		return null;
    	
    	// Reference to superclass
    	int sup = Native.rdMem(this.classRefAddress + Const.CLASS_SUPER);
    	sup = Native.rdMem(sup+5);
    	
    	return (Class) Native.toObject(sup);
    	
    };
    
    public char getType(){
    	
    	switch (primitiveType) {
		case 1:
			return _boolean;
		case 2:
			return _char;
		case 3:
			return _byte;
		case 4:
			return _short;
		case 5:
			return _int;
		case 6:
			return _long;
		case 7:
			return _float;
		case 8:
			return _double;
		case 9:
			return _void;
		default:
			return _cl_int;
		}
    	
    }


}