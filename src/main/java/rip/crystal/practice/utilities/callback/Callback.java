package rip.crystal.practice.utilities.callback;

import java.io.Serializable;

public interface Callback extends Serializable {

	/**
	 * A callback for running a task on a set of data.
	 */
	void callback();

}
