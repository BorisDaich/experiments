/**
 *
 */
package biz.daich.experiments.boot.metrics.test;

/**
 * generic interface that generates an Item of <TYPE> usually for testing
 *
 * the implementations can be as different as simple random values generator to something complicated as needed - interface to make those implementations interchangeable in tests
 * and experiments
 *
 * @author boris
 *
 */
@FunctionalInterface
public interface IObjGen<TYPE>
{
	/**
	 * Just return us an instance
	 *
	 * @param objects
	 *            - what ever parameters that might be needed
	 */
	TYPE get(Object... objects);
}
