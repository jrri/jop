package libs.safeutil.extras;

public interface PoolObject {

	/**
	 * Initialization hook. Needs to be implemented by the application developer
	 * accordingly to initialize the object state and mark it as in use.
	 */
	public void initialize();

	/**
	 * Check if the pool object is in use.
	 * 
	 * @return true if the pool element is not in use, false otherwise.
	 */
	public boolean isFree();

	/**
	 * Used when returning the object into the pool e.g. to reset its state and
	 * to mark it as free.
	 */
	public void reset();
	
	
	public ObjectPool<?> getPool();
	
	public void setPool(ObjectPool<PoolObject> pool);

}
