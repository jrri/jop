package jopscj.enemy;

import jopscj.friend1.A;
import jopscj.friend1.Helper;

public class C {
	
	
	public static void oneMethod(){
		
		A a = new A();
		
		// Can not access other than public methods in class A
		A.baz();
		a.fubar();
		
		// Helper has package accessibility on its constructor,
		// thus the following object can't be created and package
		// methods of A are not exposed.
		// Helper h = new Helper();
		
	}

}
