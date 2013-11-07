package libs.check;

import libs.safeutil.SafeHashMap;
import libs.safeutil.SafeVector;
import libs.safeutil.extras.ObjectPool;
import libs.safeutil.extras.PoolObject;
import libs.safeutil.extras.PoolObjectFactory;

public class Check {
	
	public static final int CAPACITY = 10;
	
	public final int lim;
	
	public Check(int limit) {
		lim = limit;
	}

	class MyPoolObject implements PoolObject {

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
		public void reset() {
			this.isFree = true;
		}
		
		public boolean equals(Object o){
			return this == o;
			
		}

		@Override
		public ObjectPool<?> getPool() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setPool(ObjectPool<PoolObject> pool) {
			// TODO Auto-generated method stub
			
		}

	}

	class MyFactory implements PoolObjectFactory {

		private int count = 0;

		@Override
		public PoolObject createObject() {
			MyPoolObject temp = new MyPoolObject();
			temp.number = count;
			count++;
			return temp;
		}

	}

	public static void main(String[] args) {
		
		Check app = new Check(100);
		app.measure();

	}
	
	public void measurer(){
		int x=0;
		int y=0;
		int z=0;
		
		for(int i = 0; i<lim; i++)
			z = x+y;
		
		measureX(11);
		
	}
	
	public void measureX(int l){
		int x =0;
		for(int i =0; i<l;i++)
			x++;
		
	}
	
	public void measure1(){

		/* Test one method at a time with WCA tool */
		SafeVector<MyPoolObject> fixture = new SafeVector<MyPoolObject>(5);
		ObjectPool<MyPoolObject> MyPool = new ObjectPool<MyPoolObject>(CAPACITY, new MyFactory());
		
		Object[] array = new Object[10];
		fixture.copyInto(array);
		fixture.add(MyPool.getPoolObject());
		fixture.capacity();
		fixture.size();
		fixture.isEmpty();
		fixture.elements();
		fixture.contains(null);
		fixture.lastIndexOf(null, 4);
		fixture.removeAllElements();
		
	}
	
	public void measure(){

		/* Test one method at a time with WCA tool */
		SafeHashMap<String, String> fixture = new SafeHashMap<String, String>();
		
		String key = "key";
		String value = "value";
		
		fixture.put(key, value);
		
		String s = fixture.get(key);
		
		fixture.remove(key);
		fixture.clear();
		
		Object[] array = new Object[10];
		fixture.capacity();
		fixture.size();
		fixture.isEmpty();

	}
	
}
