package edu.purdue.scjtck.tck;

import javax.realtime.MemoryArea;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.MissionSequencer;
import javax.safetycritical.PrivateMemory;
import javax.safetycritical.ThrowBoundaryError;

/**
 * Level
 * 
 * - the ThrowBoundaryError exception behaves as if it is pre-allocated on a
 * per-schedulable object basis
 * 
 * - ThrowBoundaryError has following additional methods:
 * 
 * getPropagatedExceptionClass
 * 
 * getPropagatedMessage
 * 
 * getPropagatedStackTraceDepth
 * 
 * getPropagatedStackTrace
 */
public class TestException000 extends TestCase {

	public MissionSequencer getSequencer() {
		return new GeneralSingleMissionSequencer(new GeneralMission() {

			public void initialize() {
				new GeneralPeriodicEventHandler() {
					public void handleAsyncEvent() {
						final PrivateMemory enclosingScope = new PrivateMemory(
								256);
						enclosingScope.enter(new Runnable() {
							public void run() {
								final StackMaker stack = new StackMaker();
								PrivateMemory enclosedScope = new PrivateMemory(
										256);
								try {
									enclosedScope.enter(new Runnable() {
										public void run() {
											stack.func1();
										}
									});
								} catch (ThrowBoundaryError e) {
									
//									if (MemoryArea.getMemoryArea(e).equals(
//											enclosingScope))
									if (ManagedMemory.getManagedMemory(e).equals(
											enclosingScope))
										fail("ThrowBoundaryError should be pre-allocated");
									if (e.getPropagatedExceptionClass() != Error.class)
										fail("Error in ThrowBoundaryError.getPropagatedExceptionClass()");
									if (!e.getPropagatedMessage().toString()
											.equals("Original Exception"))
										fail("Error in ThrowBoundaryError.getPropagatedMessage()");
									// the stacktrace should at least contain
									// all functions in StackMaker
									if (e.getPropagatedStackTraceDepth() < stack.stackDepth)
										fail("Error in ThrowBoundaryError.getPropagatedStackTraceDepth()");

									// check the top "stack.stackDepth" elements
									// on the stacktrace. They should be
									// identical to that of the stacktrace
									// generated by an error cross no boundary
									// StackTraceElement[] elems1 = e
									// .getPropagatedStackTrace();
									// StackTraceElement[] elems2 = null;

									// try {
									// stack.func1();
									// } catch (Error e1) {
									// elems2 = e1.getStackTrace();
									// }

									boolean bad = false;

									// if (elems1 != null) {
									// for (int i = 0; i < elems1.length
									// && i < stack.stackDepth && !bad; i++)
									// if (!elems1[i].equals(elems2[i]))
									// bad = true;
									// } else
									// bad = true;

									if (bad)
										fail("Error in ThrowBoundaryError.getPropagatedStackTrace()");
								} catch (javax.realtime.ThrowBoundaryError e1) {
									fail("javax.realtime.ThrowBoundaryError is thrown (javax.safetycritical.ThrowBoundaryError expected)");
								} catch (Throwable t) {
									fail("Unexpected error occurred in entering enclosing scope");
								}
							};
						});
						requestTermination();
					}
				}.register();
			}

			class StackMaker {

				int stackDepth = 3;

				void func1() {
					func2();
				}

				void func2() {
					func3();
				}

				void func3() {
					throw new Error("Original Exception");
				}
			}

			@Override
			protected void cleanUp() {
				teardown();
			}
		});
	}

	@Override
	public long immortalMemorySize() {
		return 1000;
	}

	@Override
	protected String getArgs() {
		return "-L 1";
	}
}
