package com.jopdesign.build;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.smartcardio.ATR;

import org.apache.bcel.classfile.JavaClass;

import com.jopdesign.sys.Const;

public class ClassObjectInfo {
//
//	/* 2 + (3 instance variables). See JOP's java.lang.Class */
//	static final int SIZE = 5;
//	static int javaLangClass;
//	static int address;
//
//	static final int IS_ANNOTATION = 0x01;
//	static final int IS_ARRAY = 0x02;
//	static final int IS_ENUM = 0x04;
//	static final int IS_INTERFACE = 0x08;
//	static final int IS_ABSTRACT = 0x10;
//
//	static int clinfo;
//	static int init;
//	static int attributes;
//	static int objAddress;
//
//	int cl;
//	int ini;
//	int att;
//
//	static Vector<ClassObjectInfo> usedClasses = new Vector<ClassObjectInfo>();
//	static Vector<String> names = new Vector<String>();
//
//	private ClassObjectInfo(int cl, int ini, int att) {
//		this.cl = cl;
//		this.ini = ini;
//		this.att = att;
//	}
//
//	@SuppressWarnings("deprecation")
//	static void work(JopClassInfo cli) {
//		
//		clinfo = cli.classRefAddress;
//
//		attributes = 0;
//		JavaClass klass = cli.clazz;
//		String value = cli.clazz.getClassName();
//
//		if (value.equals("java.lang.Class"))
//			javaLangClass = cli.classRefAddress + Const.CLASS_HEADR;
//
//		if (klass.isAnnotation()) {
//			attributes |= IS_ANNOTATION;
//		}
//
//		if (klass.isEnum()) {
//			attributes |= IS_ENUM;
//		}
//
//		if (klass.isInterface()) {
//			attributes |= IS_INTERFACE;
//		}
//
//		if (klass.isAbstract()) {
//			attributes |= IS_ABSTRACT;
//		}
//		
//		init = 0;
//		List<OldMethodInfo> methods = cli.getMethods();
//		for (int i = 0; i < methods.size(); i++) {
//			JopMethodInfo jopMethodInfo = (JopMethodInfo) methods.get(i);
//
//			if (jopMethodInfo.getMethod().getName().equals("<init>")) {
//				if (jopMethodInfo.getMethod().getSignature().startsWith("()")) {
//					init = jopMethodInfo.structAddress;
//				}
//
//			}
//		}
//
//		usedClasses.add(new ClassObjectInfo(clinfo, init, attributes));
//		names.add(value);
//		objAddress += SIZE;
//		cli.classObjectRef = objAddress;
//
//	}
//
//	public static void dump(PrintWriter out) {
//
//		out.println("\t//");
//		out.println("\t// "+usedClasses.size()+" Class objects ");
//		out.println("\t//");
//		
//		for (int i =0; i<usedClasses.size(); i++) {
//
//			ClassObjectInfo temp = usedClasses.elementAt(i);
//			String val = names.elementAt(i);
//			
//			out.println("//\t "+address+": "+val);
//			out.println("\t"+ (address + 2)+ ",\t//\tpointer to first instance variable");
//			out.println("\t" + javaLangClass+ ",\t//\tpointer to java.lang.Class mtab ");
//			out.println("\t" + temp.cl+ ",\t//\tpointer to class info structure");
//			out.println("\t" + temp.ini+ ",\t//\tpointer to empty <init> method");
//			out.println("\t" + temp.att + ",\t//\ttype attributes");
//			
//			address += SIZE;
//
//		}
//		
//		/* Create primitive type classes */
//		for(int i = 1; i < 10; i++){
//			
//			out.println("//\t "+address+": "+ getPrimName(i) + " primitive type");
//			out.println("\t"+ (address + 2)+ ",\t//\tpointer to first instance variable");
//			out.println("\t" + javaLangClass+ ",\t//\tpointer to java.lang.Class mtab ");
//			out.println("\t" + 0 + ",\t//\tpointer to class info structure");
//			out.println("\t" + 0+ ",\t//\tpointer to empty <init> method");
//			out.println("\t" + (i << 5) + ",\t//\ttype attributes");
//			
//			address += SIZE;
//
//		}
//
//	}
//
//	private static String getPrimName(int i) {
//		
//		switch (i) {
//		case 1:
//			return "boolean";
//		case 2:
//			return "byte";
//		case 3:
//			return "char";
//		case 4:
//			return "double";
//		case 5:
//			return "float";
//		case 6:
//			return "int";
//		case 7:
//			return "long";
//		case 8:
//			return "short";
//		case 9:
//			return "void";
//		default:
//			return "null";
//		}
//	}
}
