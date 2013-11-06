package libs.check.scj.scope;

import libs.lang.SafeStringBuilder;
import libs.safeutil.extras.ObjectPool;
import libs.safeutil.extras.PoolObject;

public class DataPacket implements PoolObject{
	
	private boolean isFree = true;
	
	int header;
	int payload;
	public int data;
	
	// WCMEM = 10
	public DataPacket() {
		
	}

	@Override
	public void initialize() {
		isFree = false;
	}

	@Override
	public boolean isFree() {
		return isFree;
	}

	@Override
	public void terminate() {
		data = 0;
		isFree = true;
	}
	
	// WCMEM = 355
	@Override
	public String toString() {
		
		// Size of Integer.toString(int)
		//  6 + (String handler)
		//  3   (String instance size)
		//  6   (String char array handler)
		//  M   (digits in int argument, 33 in worst case if a binary value is returned)
		//  6   (Char array handler created inside method)
		// 33	(Chars in previous array, worst case length)
		//------
		// 54+M
		String s1 = Integer.toString(header);
		String s2 = Integer.toString(data);
		
		// Size of SafeStringBuilder:
		//  6 + (SafeStringBuilder handler)
		//  2   (SafeStringBuilder instance)
		//  6   (SafeStringBuilder char array handler)
		//  N   (Number of elements in char array, 75 in worst case if a binary value is returned)
		//-------
		// 14+ N
		SafeStringBuilder sb = new SafeStringBuilder(s1.length()+s2.length()+11);		

		sb.append("Packet: [");
		sb.append(s1);
		sb.append(":");
		sb.append(s2);
		sb.append("]");

		// Returning string size:
		//  6 + (String handler)
		//  3   (String instance size)
		//  6   (String char array handler)
		//  Q   (used positions in sb, 75 in worst case if a binary value is returned)
		//------
		// 15+Q
		String s = sb.toString(); 

		// Memory consumed: (14+N)+(54+M)+(54+P)+(15+Q) = 137+N+M+P+Q
		// In this case: 212+M+P+Q
		return s;
		
		// The compiler generates StringBuilder for the expression below:
		// return "Packet: "+header+"-"+data;
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
