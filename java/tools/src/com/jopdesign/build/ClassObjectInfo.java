package com.jopdesign.build;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.bcel.classfile.JavaClass;

public class ClassObjectInfo {

	/**
	 * The size of a Class class instance. Equal to 2 + (3 instance variables).
	 * See JOP's java.lang.Class
	 */
	static final int SIZE = 5;

	/**
	 * Address of the Class class cli
	 */
	public static JopClassInfo cli;

	/**
	 * The start address of the class objects table. The first position has the
	 * size of the table (the total number of class objects created) without
	 * including the primitive type objects.
	 */
	static int classObjectsTableAddress = 0;
	
	/**
	 * Address of this Class object, relative to the start address of the class
	 * objects table
	 */
	int objAddressOffset;
	
	/**
	 * Class info structure of the type represented by this Class object
	 * instance
	 */
	int clinfo;

	/**
	 * Reference to the empty constructor of the type represented by this Class
	 * object instance.
	 */
	int initMethod;

	/**
	 * This field represent different attributes coded in the bit positions
	 * listed below:
	 * 
	 * 0: Annotation 1: Array 2: Enumeration 3: Interface 4: Abstract 5-8:
	 * Primitive type
	 * 
	 */
	int attributes;

	private String name;
	private static int nextClassObject = 0;

	/* Attribute masks */
	private static final int IS_ANNOTATION = 0x01;
	private static final int IS_ARRAY = 0x02;
	private static final int IS_ENUM = 0x04;
	private static final int IS_INTERFACE = 0x08;
	private static final int IS_ABSTRACT = 0x10;

	/**
	 * A hash table to store the classes used in the application
	 */
	static HashMap<String, ClassObjectInfo> usedClasses = new HashMap<String, ClassObjectInfo>();
	static List<ClassObjectInfo> list = new LinkedList<ClassObjectInfo>();

	/**
	 * Total length of the class objects table, including the 9 primitive
	 * classes
	 */
	static int length = SIZE * 9;

	static int lastAddress;

	private ClassObjectInfo(String name, int att) {
		this.attributes = att;
		this.name = name;
		this.objAddressOffset += nextClassObject;

		nextClassObject += SIZE;
	}
	
	int getAddress(){
		return classObjectsTableAddress + objAddressOffset + 1 ;
	}
	
	static void addClass(JavaClass clazz) {

		String className = clazz.getClassName();
		int attributes = 0;

		if (usedClasses.containsKey(className))
			return;

		/* Process attributes */
		if (clazz.isAnnotation()) {
			attributes |= IS_ANNOTATION;
		}

		if (clazz.isEnum()) {
			attributes |= IS_ENUM;
		}

		if (clazz.isInterface()) {
			attributes |= IS_INTERFACE;
		}

		if (clazz.isAbstract()) {
			attributes |= IS_ABSTRACT;
		}

		ClassObjectInfo cloi = new ClassObjectInfo(className, attributes);
		usedClasses.put(className, cloi);
		list.add(cloi);

		length += SIZE;

	}

	@SuppressWarnings("deprecation")
	static void setAddresses(JOPizer jz) {

		String className;
		ClassObjectInfo cloi;

		Iterator<? extends OldClassInfo> iter = jz.cliMap.values().iterator();
		while (iter.hasNext()) {
			JopClassInfo cli = (JopClassInfo) iter.next();
			className = cli.clazz.getClassName();

			cloi = usedClasses.get(className);
			cloi.clinfo = cli.classRefAddress;

			cloi.initMethod = 0;
			List<OldMethodInfo> methods = cli.getMethods();
			for (int i = 0; i < methods.size(); i++) {
				JopMethodInfo jopMethodInfo = (JopMethodInfo) methods.get(i);

				if (jopMethodInfo.getMethod().getName().equals("<init>")) {
					if (jopMethodInfo.getMethod().getSignature()
							.startsWith("()")) {
						cloi.initMethod = jopMethodInfo.structAddress;
					}

				}
			}

		}

	}

	public void dump(PrintWriter out, JopClassInfo classCli, int address) {

		out.println("//\t " + address + ": " + name);
		out.println("\t" + (address + 2) + ",\t//\tpointer to first instance variable");
		out.println("\t" + classCli.methodsAddress + ",\t//\tpointer to java.lang.Class mtab ");
		out.println("\t" + clinfo + ",\t//\tpointer to class info structure");
		out.println("\t" + initMethod + ",\t//\tpointer to empty <init> method");
		out.println("\t" + attributes + ",\t//\ttype attributes");

		objAddressOffset = address;
	}

	private static String getPrimName(int i) {

		switch (i) {
		case 1:
			return "boolean";
		case 2:
			return "byte";
		case 3:
			return "char";
		case 4:
			return "double";
		case 5:
			return "float";
		case 6:
			return "int";
		case 7:
			return "long";
		case 8:
			return "short";
		case 9:
			return "void";
		default:
			return "null";
		}
	}

	static void dumpPrimitive(PrintWriter out, JopClassInfo classCli,
			int address, int index) {

		out.println("//\t " + address + ": " + getPrimName(index) + " primitive type");
		out.println("\t" + (address + 2) + ",\t//\tpointer to first instance variable");
		out.println("\t" + classCli.methodsAddress + ",\t//\tpointer to java.lang.Class mtab ");
		out.println("\t" + 0 + ",\t//\tpointer to class info structure");
		out.println("\t" + 0 + ",\t//\tpointer to empty <init> method");
		out.println("\t" + (index << 5) + ",\t//\ttype attributes");

	}

	static ClassObjectInfo getClass(JavaClass clazz) {
		return usedClasses.get(clazz.getClassName());
	}

	static ClassObjectInfo getClass(String clname) {
		return usedClasses.get(clname);
	}

	void dump(PrintWriter out, JopClassInfo classCli) {
		
		int address = classObjectsTableAddress + objAddressOffset + 1;
		lastAddress = address + SIZE;
		
		out.println("//\t " + address + ": " + name);
		out.println("\t" + (address + 2) + ",\t//\tpointer to first instance variable");
		out.println("\t" + classCli.methodsAddress + ",\t//\tpointer to java.lang.Class mtab ");
		out.println("\t" + clinfo + ",\t//\tpointer to class info structure");
		out.println("\t" + initMethod + ",\t//\tpointer to empty <init> method");
		out.println("\t" + attributes + ",\t//\ttype attributes");
		
	}
}
