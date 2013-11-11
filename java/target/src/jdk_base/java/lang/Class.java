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

import static javax.safetycritical.annotate.Phase.ALL;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import com.jopdesign.sys.Const;
import com.jopdesign.sys.GC;
import com.jopdesign.sys.JVMHelp;
import com.jopdesign.sys.Native;

public final class Class<T> {
	
	/* Masks */
	static final int IS_ANNOTATION = 0x01;
	static final int IS_ARRAY = 0x02;
	static final int IS_ENUM = 0x04;
	static final int IS_INTERFACE = 0x08;
	static final int IS_ABSTRACT = 0x10;
	static final int PRIM_TYPE = 0x1E0;
	
	/* Class info structure of the type represented by this instance */
	 private int clinfo;
	
	/* Reference to the empty constructor of the type represented by this instance. */
	 private int init;
	
	 /* This field represent different attributes coded in several bits:
	  * 
	  * 0: Annotation
	  * 1: Array
	  * 2: Enumeration
	  * 3: Interface
	  * 4: Abstract
	  * 5-8: Primitive type
	  * 
	  * */
	 private int attributes;
	
	/**
	 * Package protected constructor to create Class objects at boot time.
	 */
	Class() {
	}
	
	@SCJAllowed
	@SCJRestricted(phase = ALL, maySelfSuspend = false, mayAllocate = true)
	public boolean desiredAssertionStatus( ){
		throw new Error("not yet implemented");
	}
	
	/**
	 * Returns the Class representing the component type of an array. If this
	 * class does not represent an array class this method returns null.
	 * 
	 * @return the Class representing the component type of this class if this
	 *         class is an array
	 */
	@SCJAllowed
	@SCJRestricted(phase = ALL, maySelfSuspend = false, mayAllocate = true)
	public Class<?> getComponentType() {
		throw new Error("not yet implemented");
	}
	
	/**
	 * If the class or interface represented by this Class object is a member of
	 * another class, returns the Class object representing the class in which
	 * it was declared. This method returns null if this class or interface is
	 * not a member of any other class. If this Class object represents an array
	 * class, a primitive type, or void,then this method returns null.
	 * 
	 * @return the declaring class for this class
	 */
	@SCJAllowed
	@SCJRestricted(phase = ALL,	maySelfSuspend = false, mayAllocate = true)
	public Class<?> getDeclaringClass(){
		throw new Error("not yet implemented");
	}
	
	/**
	 * Returns the elements of this enum class or null if this Class object does
	 * not represent an enum type.
	 * 
	 * @return an array containing the values comprising the enum class
	 *         represented by this Class object in the order they're declared,
	 *         or null if this Class object does not represent an enum type
	 */
	@SCJAllowed
	@SCJRestricted(phase = ALL,	maySelfSuspend = false, mayAllocate = true)
	public T[] getEnumConstants(){
		throw new Error("not yet implemented");
	}
	
	/**
	 * 
	 * Returns the name of the entity (class, interface, array class, primitive
	 * type, or void) represented by this Class object, as a String.
	 * 
	 * If this class object represents a reference type that is not an array
	 * type then the binary name of the class is returned, as specified by the
	 * Java Language Specification, Second Edition.
	 * 
	 * If this class object represents a primitive type or void, then the name
	 * returned is a String equal to the Java language keyword corresponding to
	 * the primitive type or void.
	 * 
	 * If this class object represents a class of arrays, then the internal form
	 * of the name consists of the name of the element type preceded by one or
	 * more '[' characters representing the depth of the array nesting. The
	 * encoding of element type names is as follows:
	 * 
	 * 			Element Type 			Encoding 
	 * 				boolean 				Z 
	 * 				byte		 			B 
	 * 				char 					C 
	 * 			class or interface 		Lclassname; 
	 * 				double 					D 
	 * 				float 					F 
	 * 				int 					I	 
	 * 				long 					J 
	 * 				short 					S
	 * 
	 * The class or interface name classname is the binary name of the class
	 * specified above.
	 * 
	 * Examples:
	 * 
	 * String.class.getName() returns "java.lang.String" 
	 * byte.class.getName()	 returns "byte" 
	 * (new Object[3]).getClass().getName() returns "[Ljava.lang.Object;" 
	 * (new int[3][4][5][6][7][8][9]).getClass().getName() returns "[[[[[[[I"
	 * 
	 * @return the name of the class or interface represented by this object.
	 * 
	 */
	@SCJAllowed
	@SCJRestricted(phase = ALL, maySelfSuspend = false, mayAllocate = true)
	public String getName(){
		throw new Error("not yet implemented");
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
    public Class<?> getSuperclass(){
    	
    	if( isInterface() || isPrimitive() || (this == Object.class))
    		return null;
    	
    	// Reference to superclass
    	int sup = Native.rdMem(this.clinfo + Const.CLASS_SUPER);
    	sup = Native.rdMem(sup+5);
    	
    	return (Class) Native.toObject(sup);
    	
    };

	/**
	 * Returns true if this Class object represents an annotation type. Note
	 * that if this method returns true, isInterface() would also return true,
	 * as all annotation types are also interfaces.
	 * 
	 * @return true if this class object represents an annotation type; false
	 *         otherwise
	 */
    @SCJAllowed
    @SCJRestricted(phase = ALL, maySelfSuspend = false, mayAllocate = true)
    public boolean isAnnotation(){
    	return ((attributes & IS_ANNOTATION) != 0);
    }
    
    /**
     * Determines if this {@code Class} object represents an array class.
     *
     * @return  {@code true} if this object represents an array class;
     *          {@code false} otherwise.
     * @since   JDK1.1
     */
    @SCJAllowed
    @SCJRestricted(phase = ALL, maySelfSuspend = false, 	mayAllocate = true)
    public boolean isArray(){
    	return ((attributes & IS_ARRAY) != 0);
    }
    
	/**
	 * Determines if the class or interface represented by this Class object is
	 * either the same as, or is a superclass or superinterface of, the class or
	 * interface represented by the specified Class parameter. It returns true
	 * if so; otherwise it returns false. If this Class object represents a
	 * primitive type, this method returns true if the specified Class parameter
	 * is exactly this Class object; otherwise it returns false.
	 * 
	 * Specifically, this method tests whether the type represented by the
	 * specified Class parameter can be converted to the type represented by
	 * this Class object via an identity conversion or via a widening reference
	 * conversion. See The Java Language Specification, sections 5.1.1 and 5.1.4
	 * , for details.
	 * 
	 * @param cls
	 *            - the Class object to be checked
	 * @return the boolean value indicating whether objects of the type cls can
	 *         be assigned to objects of this class
	 * @throws NullPointerException
	 *             - if the specified Class parameter is null.
	 * 
	 */
    @SCJAllowed
    @SCJRestricted(phase = ALL, maySelfSuspend = false, mayAllocate = true)
    public boolean isAssignableFrom(Class<?> cls){
    	
    	if(this.getClass().isPrimitive())
    		return (this.getClass() == cls ? true: false);
    	
    	int cons = this.clinfo;
		int p = cls.clinfo;
    	return isInstance(cons, p);
    	
    }
    
    
	/**
	 * Returns true if and only if this class was declared as an enum in the
	 * source code.
	 * 
	 * @return true if and only if this class was declared as an enum in the
	 *         source code
	 * 
	 */
    @SCJAllowed
    @SCJRestricted(phase = ALL, maySelfSuspend = false, mayAllocate = true)
    public boolean isEnum(){
    	return ((attributes & IS_ENUM) != 0);
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
    @SCJRestricted(phase = ALL, maySelfSuspend = false, mayAllocate = true)
	public boolean isInstance(Object obj) {

		if (isPrimitive()) {
			return false;
		}

		if (obj == null) {
			return false;
		}

		// class info of the type this class represents
		int cons = this.clinfo;

		// start of class info
		int p = obj.getClass().clinfo;
		
		return isInstance(cons, p);
		
	}
    
    private boolean isInstance(int cons, int p){
    	
		/*
		 * This is the same as the JVM.f_instanceof method. Such method is
		 * private so the code is repeated here
		 */

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
    	return ((attributes & IS_INTERFACE) != 0);
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
    @SCJRestricted(phase = ALL, maySelfSuspend = false, mayAllocate = true)
    public boolean isPrimitive(){
    	return ((attributes & PRIM_TYPE) != 0);
    }
    
    private boolean isAbstract(){
    	return ((attributes & IS_ABSTRACT) != 0);
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
    	
		if (this == Class.class) {
			throw new IllegalAccessException(
					"Can not call newInstance() on the Class for java.lang.Class");
		}
    	
		if (isInterface() || isPrimitive() || init == 0 || isAbstract())
			throw new InstantiationException(
					"Trying to instantiate an interface, primitive, or the class has no empty constructor");

		int newObj = GC.newObject(clinfo); 
		Native.invoke(newObj,init);

		return (T) Native.toObject(newObj);
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
        return (isInterface() ? "interface " : (isPrimitive() ? "" : "class ")) + clinfo;
    }
    
    static Class<?> getPrimitiveClass(char c){
    	
    	/* Primitive objects are the last Class objects */
    	int p = Native.rdMem(0) - 9*Const.CLASS_INST_SIZE;
    	int offset = 0;
    	
    	switch (c) {
		case 'Z':
			offset = 0;
			break;
		case 'B':
			offset = 1;
			break;
		case 'C':
			offset = 2;
			break;
		case 'D':
			offset = 3;
			break;
		case 'F':
			offset = 4;
			break;
		case 'I':
			offset = 5;
			break;
		case 'J':
			offset = 6;
			break;
		case 'S':
			offset = 7;
			break;
		case 'V':
			offset = 8;
			break;
		default:
			break;
		}
    	
    	int i = p + 5*offset;
    	return (Class<?>) Native.toObject(i);
    }

}