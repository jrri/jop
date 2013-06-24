/*---------------------------------------------------------------------*\
 *
 * aicas GmbH, Karlsruhe, Germany 2010
 *
 * This code is provided to the JSR 302 group for evaluation purpose
 * under the LGPL 2 license from GNU.  This notice must appear in all
 * derived versions of the code and the source must be made available
 * with any binary version.  Viewing this code does not prejudice one
 * from writing an independent version of the classes within.
 *
 * $Source: /home/cvs/jsr302/scj/specsrc/javax/safetycritical/ManagedSchedulable.java,v $
 * $Revision: 1.4 $
 * $Author: jjh $
 * Contents: Java source of HIJA Safety Critical Java interface
 *           ManagedTask
 *
\*---------------------------------------------------------------------*/

package javax.safetycritical;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;

/**
 * In SCJ, all schedulable objects are managed by a mission.
 * 
 * This interface is implemented by all SCJ Schedulable classes. It defines the
 * mechanism by which the ManagedSchedulable is registered with the mission for
 * its management. This interface is used by SCJ classes. It is not intended for
 * direct use by applications classes.
 * 
 * @version SCJ 0.93
 * @todo This class specifies that: "It defines the mechanism by which the
 *       ManagedSchedulable is registered with the mission for its management"
 *       but the register() method was available until ver. 0.80 of the spec.
 *       The mentioned text should be removed from the description.
 */
@SCJAllowed
public interface ManagedSchedulable extends javax.realtime.Schedulable {

	/**
	 * Runs any end-of-mission clean up code associated with this schedulable
	 * object.
	 */
	@SCJAllowed(SUPPORT)
	@SCJRestricted(phase = CLEANUP)
	public void cleanUp();
}
