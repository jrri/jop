package test;

import com.jopdesign.sys.JVMHelp;

public class TestClass {

//	 Vector<VectorElement> vector;
	
	public TestClass() {
		// this.vector = new Vector<VectorElement>();
	}

	/**
	 * @param args
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(String[] args) {
		
//		Class klazz;
//		
//		klazz = Boolean.TYPE;
//		System.out.println(Native.toInt(klazz));
//		
//		klazz = Character.TYPE;
//		System.out.println(Native.toInt(klazz));
//		
//		klazz = Byte.TYPE;
//		System.out.println(Native.toInt(klazz));
//		
//		klazz = Short.TYPE;
//		System.out.println(Native.toInt(klazz));
//		
//		klazz = Integer.TYPE;
//		System.out.println(Native.toInt(klazz));
//		
//		klazz = Long.TYPE;
//		System.out.println(klazz.classRefAddress);
//		
//		klazz = Float.TYPE;
//		System.out.println(Native.toInt(klazz));
//		
//		klazz = Double.TYPE;
//		System.out.println(Native.toInt(klazz));
//		
//		klazz = Void.TYPE;
//		System.out.println(Native.toInt(klazz));
//		
//		System.out.println(Native.toInt(Runnable.class));
//		Class<Runnable> klazz = JVMHelp.getClassHelper(Runnable.class);
//		System.out.println(klazz.clCassRefAddress);
//
//		
//		try {
//			Runnable newRunnable = (Runnable) klazz.newInstance();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		boolean ok = true;

		// Obtain class objects for primitive wrapper types
		ok = ok & testPrimitives();

		// Use .class notation for for interfaces
		ok = ok & testForInterface();
		
		// Use .class notation for non interfaces
		ok = ok & testForNonInterface();
		
		// Use Objet.getClass()
		ok = ok & testGetClass();
		
		// Misc tests
		ok = ok & miscTests();
		
		System.out.println("----------------------");
		System.out.println("Global test ok: "+ok);
		System.out.println("----------------------");

		
	}

	private static boolean miscTests() {
		
		boolean ok = true;
		
		Class klass = Object.class.getSuperclass();
		
		/*
		 * Should not be able to return the superclass of a Class object
		 * representing java.lang.Object
		 */
		if(klass != null){
			System.out.println("Error! Object does not have super class!");
			ok = false;
		}
		
		/*
		 * Should not be able to create Class objects from an object
		 * representing java.lang.Class
		 */
		klass = Class.class;
		try {
			Class newClass = (Class) klass.newInstance();
			ok = false;
		} catch (InstantiationException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		/* Check that <init> method is called correctly */
		ClassWithEmptyConstructor cwec = new ClassWithEmptyConstructor();
		klass = cwec.getClass();

		try {
			ClassWithEmptyConstructor newCwec =  (ClassWithEmptyConstructor) klass.newInstance();
			ok = ok & newCwec.surname.equals("Johnsson");
			ok = ok & newCwec.name.equals("John");
		} catch (InstantiationException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			ok = false;
		} catch (IllegalAccessException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			ok = false;
		}
		
		/* Check that objects with non-empty constructor cannot be created */
		ClassWithNonEmptyConstructor cwnec = new ClassWithNonEmptyConstructor("Alice");
		klass = cwnec.getClass();

		try {
			ClassWithNonEmptyConstructor newCwnec =  (ClassWithNonEmptyConstructor) klass.newInstance();
			ok = false;
		} catch (InstantiationException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("Misc test ok: "+ok);
		return ok;
	}

	private static boolean testGetClass() {
		
		VectorElementExtended vee = new VectorElementExtended();
		VectorElement ve = new VectorElement();
		
		Class<VectorElementExtended> klazz = (Class<VectorElementExtended>) vee.getClass();
		Class<VectorElement> superClass = (Class<VectorElement>) ve.getClass();
		
		boolean ok = true;
		
		ok = ok & (klazz.isPrimitive() == false);
//		ok = ok & (klazz.primitiveType == 0);
//		ok = ok & (klazz.getType() == 'L');
		ok = ok & (klazz.getSuperclass() == superClass);
		ok = ok & (klazz.isArray() == false);
		ok = ok & (superClass.isInstance(vee));
		ok = ok & (klazz.isInterface() == false);
		ok = ok & (klazz.isPrimitive() == false);

		VectorElementExtended newVee;

		try {
			newVee = (VectorElementExtended) klazz.newInstance();
			ok = ok & (newVee instanceof VectorElement);
			ok = ok & (newVee instanceof VectorElementExtended);
			
			newVee.name = "Bob";
			ok = ok & (newVee.getName().equals("Bob"));
		} catch (InstantiationException e) {
			e.printStackTrace();
			ok = false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			ok = false;
		}

		System.out.println("Get class ok : " + ok);
		
		return ok;
		
		
	}

	private static boolean testForNonInterface() {
		
		Class<String> klazz = String.class;
		String testString = "Hello";
		
		// Get a Class object for java.lang.Object
		Class objClass = Object.class;
		
		boolean ok = true;
		
		ok = ok & (klazz.isPrimitive() == false);
//		ok = ok & (klazz.primitiveType == 0);
//		ok = ok & (klazz.getType() == 'L');
		ok = ok & (klazz.getSuperclass() == objClass);
		ok = ok & (klazz.isArray() == false);
		ok = ok & (klazz.isInstance(testString));
		ok = ok & (klazz.isInterface() == false);
		ok = ok & (klazz.isPrimitive() == false);

		String newString;

		try {
			newString = (String) klazz.newInstance();
			ok = ok & (newString instanceof String);
			newString = "Hello again!";
			ok = ok & (newString.equals("Hello again!"));
		} catch (InstantiationException e) {
			e.printStackTrace();
			ok = false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			ok = false;
		}

		System.out.println("String ok : " + ok);
		
		return ok;
		
		
	}

	private static boolean testForInterface() {
		
		
		Class<Runnable> klazz = Runnable.class;
		
		boolean ok = true;
		
		// These objects implement Runnable
		MyClass myClass = new MyClass();
		Implementer myImplemener = new Implementer();

			ok = ok & (klazz.isPrimitive() == false);
//			ok = ok & (klazz.primitiveType == 0);
//			ok = ok & (klazz.getType() == 'L');
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(myClass));
			ok = ok & (klazz.isInstance(myImplemener));
			
			ok = ok & (klazz.isInterface());
			ok = ok & (klazz.isPrimitive() == false);

			Runnable newRunnable;

			try {
				newRunnable = (Runnable) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

			System.out.println("Interface ok : " + ok);
			
			return ok;
	}

	private static boolean testPrimitives() {

		boolean ok = true;
		Object testObj = new Object();

		Class<?> klazz;

		klazz = Boolean.TYPE;
		for (int i = 0; i < 2; i++) {
			ok = ok & klazz.isPrimitive();
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(testObj) == false);
			ok = ok & (klazz.isInterface() == false);
			ok = ok & (klazz.isPrimitive());

			Boolean newBoolean;

			try {
				newBoolean = (Boolean) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println("Boolean ok, pass " + i + " : " + ok);
			klazz = boolean.class;
		}

		// *******************************************************
		klazz = Character.TYPE;
		for (int i = 0; i < 2; i++) {
			ok = ok & klazz.isPrimitive();
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(testObj) == false);
			ok = ok & (klazz.isInterface() == false);
			ok = ok & (klazz.isPrimitive());

			Character newCharacter;

			try {
				newCharacter = (Character) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println("Character ok, pass " + i + " : " + ok);
			klazz = char.class;
		}

		// *******************************************************
		klazz = Byte.TYPE;
		for (int i = 0; i < 2; i++) {
			ok = ok & klazz.isPrimitive();
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(testObj) == false);
			ok = ok & (klazz.isInterface() == false);
			ok = ok & (klazz.isPrimitive());

			Byte newByte;

			try {
				newByte = (Byte) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println("Byte ok, pass " + i + " : " + ok);
			klazz = byte.class;
		}

		// *******************************************************
		klazz = Short.TYPE;
		for (int i = 0; i < 2; i++) {
			ok = ok & klazz.isPrimitive();
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(testObj) == false);
			ok = ok & (klazz.isInterface() == false);
			ok = ok & (klazz.isPrimitive());

			Short newShort;

			try {
				newShort = (Short) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println("Short ok, pass " + i + " : " + ok);
			klazz = short.class;
		}

		// *******************************************************
		klazz = Integer.TYPE;
		for (int i = 0; i < 2; i++) {
			ok = ok & klazz.isPrimitive();
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(testObj) == false);
			ok = ok & (klazz.isInterface() == false);
			ok = ok & (klazz.isPrimitive());

			Integer newInteger;

			try {
				newInteger = (Integer) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println("Integer ok, pass " + i + " : " + ok);
			klazz = int.class;
		}

		// *******************************************************
		klazz = Long.TYPE;
		for (int i = 0; i < 2; i++) {
			ok = ok & klazz.isPrimitive();
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(testObj) == false);
			ok = ok & (klazz.isInterface() == false);
			ok = ok & (klazz.isPrimitive());

			Long newLong;

			try {
				newLong = (Long) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println("Long ok, pass " + i + " : " + ok);
			klazz = long.class;
		}

		// *******************************************************
		klazz = Float.TYPE;
		for (int i = 0; i < 2; i++) {
			ok = ok & klazz.isPrimitive();
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(testObj) == false);
			ok = ok & (klazz.isInterface() == false);
			ok = ok & (klazz.isPrimitive());

			Float newFloat;

			try {
				newFloat = (Float) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println("Float ok, pass " + i + " : " + ok);
			klazz = float.class;
		}

		// *******************************************************
		klazz = Double.TYPE;
		for (int i = 0; i < 2; i++) {
			ok = ok & klazz.isPrimitive();
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(testObj) == false);
			ok = ok & (klazz.isInterface() == false);
			ok = ok & (klazz.isPrimitive());

			Double newDouble;

			try {
				newDouble = (Double) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println("Double ok, pass " + i + " : " + ok);
			klazz = double.class;
		}

		// *******************************************************
		klazz = Void.TYPE;
		for (int i = 0; i < 2; i++) {
			ok = ok & klazz.isPrimitive();
			ok = ok & (klazz.getSuperclass() == null);
			ok = ok & (klazz.isArray() == false);
			ok = ok & (klazz.isInstance(testObj) == false);
			ok = ok & (klazz.isInterface() == false);
			ok = ok & (klazz.isPrimitive());

			Void newVoid;

			try {
				newVoid = (Void) klazz.newInstance();
				ok = false;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			System.out.println("Void ok, pass " + i + " : " + ok);
			klazz = void.class;
		}
		
		return ok;

	}

	static class VectorElement implements Runnable {

		String name;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

	}

	static class VectorElementExtended extends VectorElement {
		
		public String name;
		
		public String getName(){
			return name;
		}

	}

	static abstract class AnAbstractClass {

		public void foo() {
			System.out.println("My foo");
		}

	}

	static public interface MyInterface extends Runnable {

		public void anotherRun();

	}
	
	static public class MyClass implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	}
	
	static public class Implementer implements MyInterface{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void anotherRun() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	static public class ClassWithNonEmptyConstructor {
		
		public String name;
		public String surname = "Johnsson";
		
		public ClassWithNonEmptyConstructor(String name) {
			this.name = name;
		}
		
	}
	
	static public class ClassWithEmptyConstructor {
		
		public String name;
		public String surname = "Johnsson";
		
		public ClassWithEmptyConstructor() {
			this.name = "John";
		}
		
	}
	
	static public abstract class MyAbstractClass{
		
		public String aString;
		
		public void foo(){
			System.out.println("My abstract class: foo");
		}
		
		public abstract void bar();
		
	}

}
