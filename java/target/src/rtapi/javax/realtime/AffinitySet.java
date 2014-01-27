package javax.realtime;

import javax.safetycritical.annotate.SCJAllowed;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Level.LEVEL_2;

/**
 * This class is the API for all processor-affinity-related aspects of SCJ. It
 * includes a factory that generates AffinitySet objects, and methods that
 * control the default affinity sets used when affinity set inheritance does not
 * apply.
 * 
 * Affinity sets implement the concept of SCJ scheduling allocation domains.
 * They provide the mechanism by which the programmer can specify the processors
 * on which managed schedulable objects can execute.
 * 
 * The processor membership of an affinity set is immutable. SCJ constrains the
 * use of RTSJ affinity sets so that the affinity of a managed schedulable
 * object can only be set during the initialization phase.
 * 
 * The internal representation of a set of processors in an affinity set
 * instance is not specified. Each processor/core in the system is given a
 * unique logical number. The relationship between logical and physical
 * processors is implementation-defined.
 * 
 * The affinity set factory cannot create an affinity set with more than one
 * processor member, but such affinity sets are supported as predefined affinity
 * sets at Level 2.
 * 
 * A managed schedulable object inherits its creator’s affinity set. Every
 * managed schedulable object is associated with a processor affinity set
 * instance, either explicitly assigned, inherited, or defaulted.
 * 
 * See also Services.getSchedulingAllocationDoamins()
 * 
 * @author Juan Rios
 * @version SCJ 0.93
 * @note An AffinitySet at L0 and L1 has a single processor. At Level 0 there
 *       can be only one AffinitySet. At Level 1 there can be more than one
 *       AffinitySets, each with only one processor meaning that schedulable
 *       objects execute in only one processor (no task migration). L2 provides
 *       predefined AffinitySets with more than one processor members. Can two
 *       different AffinitySets at L2 have a processor in common? How are
 *       AffinitySets for L1 applications created (the generate() method is
 *       available only at L2)?
 * 
 * @note As the hierarchy of ManagedLongEventHandler is not a sub clas of
 *       BoundAsyncLongEventHandler, the setProcessorAffinity() and
 *       getAffinitySet() methods are modified to accept
 *       AbstractAsyncEventHandler instead.
 * 
 */
@SCJAllowed
public final class AffinitySet {

	/*
	 * Integer to keep track of the processors already assigned to AffinitySets.
	 * Probably it is useful only at L2 where AffinitySets can be created by the
	 * application.
	 * 
	 * 00...00000001 = Processor 1; 00...00000010 = Processor 2; 00...00000100 =
	 * Processor 3; ....
	 */
	static int globalBitSet = 0;

	/*
	 * For L0, L1 this is enough, as every AffinitySet can have only one
	 * processor
	 */
	int processorNumber = 0;

	/**
	 * 
	 * Generates an affinity set consisting of a single processor.
	 * 
	 * @param processorNumber
	 *            a processor in the set of processors allocated to the JVM.
	 * 
	 * @return An AffinitySet representing a single processor in the system. The
	 *         returned object may be dynamically created in the current memory
	 *         area or preallocated in immortal memory.
	 * 
	 * @throws IllegalArgumentException
	 *             if processorNumber is not a valid processor in the set of
	 *             processors allocated to the JVM.
	 * 
	 * @todo For level 2, but it is the way default AffinitySets are created in
	 *       javax.safetycritical.Services.java
	 */
	@SCJAllowed(LEVEL_2)
	public static AffinitySet generate(int processorNumber) {

		int mask = 0;
		mask = mask | (1 << processorNumber);

		if ((globalBitSet & mask) == 0) {
			AffinitySet affinitySet = new AffinitySet();
			affinitySet.processorNumber = processorNumber;
			globalBitSet = globalBitSet | mask;
			return affinitySet;
		} else {
			throw new IllegalArgumentException(
					"Processor already in a different AffinitySet");
		}

	}

	/**
	 * 
	 * @param thread
	 * 
	 * @return an AffinitySet representing the set of processors on which thread
	 *         can be scheduled. The returned object may be dynamically created
	 *         in the current memory area or preallocated in immortal memory.
	 * 
	 * @throws NullPointerException
	 *             if thread is null.
	 * 
	 * @todo For level 2, not yet implemented
	 */
	@SCJAllowed(LEVEL_2)
	public static final AffinitySet getAffinitySet(Thread thread) {
		return null;
	}

	/**
	 * 
	 * @param processorNumber
	 * 
	 * @return true if and only if the processorNumber is in this affinity set
	 * 
	 * @todo Works only on L0, L1 although it is supposed to be allowed only on
	 *       L2
	 * 
	 */
	@SCJAllowed(LEVEL_2)
	public final boolean isProcessorInSet(int processorNumber) {

		/* For L0, L1 this is enough */
		return (this.processorNumber == processorNumber) ? true : false;
	}

	/**
	 * Set the set of processors on which thread can be scheduled to that
	 * represented by set.
	 * 
	 * @param set
	 *            s the required affinity set
	 * 
	 * @param thread
	 *            is the target managed thread.
	 * 
	 * @throws ProcessorAffinityException
	 *             if set is not a valid processor set
	 * 
	 * @throws NullPointerException
	 *             if thread is null
	 * 
	 * @todo For level 2, not yet implemented
	 */
	@SCJAllowed(LEVEL_2)
	public static final void setProcessorAffinity(AffinitySet set, Thread thread) {
	}

	/**
	 * 
	 * @param handler
	 * 
	 * @return an AffinitySet representing the set of processors on which
	 *         handler can be scheduled. The returned object may be dynamically
	 *         created in the current memory area or preallocated in immortal
	 *         memory.
	 * 
	 * @throws NullPointerException
	 *             if handler is null.
	 */
	@SCJAllowed(LEVEL_1)
	public static final AffinitySet getAffinitySet(
			AbstractAsyncEventHandler handler) {
		return handler.getAffinitySet();
	}

	/**
	 * Set the set of processors on which aeh can be scheduled to that
	 * represented by set.
	 * 
	 * @param set
	 *            is the required affinity set
	 * 
	 * @param aeh
	 *            is the target BoundAsyncEventHandler
	 * 
	 * @throws ProcessorAffinityException
	 *             if set is not a valid processor set
	 * 
	 * @throws NullPointerException
	 *             if handler is null
	 */
	@SCJAllowed(LEVEL_1)
	public static final void setProcessorAffinity(AffinitySet set,
			AbstractAsyncEventHandler handler) {
		handler.setProcessorAffinity(set);
	}

}
