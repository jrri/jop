package libs.check;

import libs.safeutil.SafeVector;
import libs.safeutil.extras.AbstractPoolObject;
import libs.safeutil.extras.ObjectPool;
import libs.safeutil.extras.PoolObjectFactory;

public class Check {
	
	public static final int CAPACITY = 10;
	SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>();
	ObjectPool<MyPoolObject> myPool = new ObjectPool<MyPoolObject>(CAPACITY, new MyFactory());
	Object[] anArray = new Object[100];
	
	public Check() {
		// TODO Auto-generated constructor stub
	}

	class MyPoolObject extends AbstractPoolObject {

		private boolean isFree = true;
		public int number = 0;

		@Override
		public void initialize() {
			this.isFree = false;

		}

		@Override
		public boolean isFree() {
			return this.isFree;
		}

		@Override
		public void terminate() {
			this.isFree = true;
		}
		
		public boolean equals(Object o){
			return this == o;
			
		}

	}

	class MyFactory implements PoolObjectFactory {

		private int count = 0;

		@Override
		public AbstractPoolObject createObject() {
			MyPoolObject temp = new MyPoolObject();
			temp.number = count;
			count++;
			return temp;
		}

	}

	public static void main(String[] args) {
		
		System.out.println("Hello");
		
		char[] buf = new char[12];
		
		Long.getChars(274877906944L, 12, buf);
		
		System.out.println(buf);
		
		
//		Check app = new Check();

	}
	
	public void foo(){
		
		Object o = new Object();
	}
	
	public void measure1(){
		
//		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>();
		
//		Object[] array = new Object[10];
//		fixture.copyInto(array);
//		fixture.capacity();
//		fixture.size();
//		fixture.isEmpty();
//		fixture.elements();
//		fixture.removeAllElements();
//		ObjectPool<MyPoolObject> MyPool = new ObjectPool<MyPoolObject>(CAPACITY, new MyFactory());
		
//		fixture.add(MyPool.getPoolObject());
	}
	
	public void measure(){
		
//		MyPoolObject e = myPool.getPoolObject();
//		fixture.add(e);
//		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>(1000);
//		fixture.isEmpty();
//		fixture.lastElement();
//		fixture.copyInto(anArray);
//		fixture.indexOf(e, 5);
//		fixture.elements();
//		fixture.setElementAt(e, 10000);
		fixture.removeElementAt(100000000);
//		int x = 0;
//		for(int i = 0; i < 10; i++){
//			x++;
//		}
//		System.out.println("Hello");
	}
	
//	public void measure(){
		
//		measure2();
//	}
	
//	public void measure(){
//		
//		int j= 0;
//		System.out.println("Name" + j + "last name");
		
//		int j= 0;
//		
//		for(int i=0; i < 10; i++){
////			j++;
//		}
		
//	}

}
