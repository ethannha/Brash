/**
 * Copyright (C) 2017 Raymond L. Rivera <ray.l.rivera@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package tage.rml;

/**
 * Utility class for single-precision floating-point comparisons that take into
 * account rounding errors.
 *
 * @author Raymond L. Rivera
 *
 */
final class FloatUtil {

    private FloatUtil() {}

    /**
     * The value of <a href=
     * "https://en.wikipedia.org/wiki/Machine_epsilon#Values_for_standard_hardware_floating_point_arithmetics">machine
     * epsilon</a>, set to <code>2e-23f</code>.
     */
    public static final float EPSILON   = 2e-23f;

    /**
     * A default amount of tolerance for floating-point rounding errors as a
     * more forgiving alternative to {@link #EPSILON}.
     */
    public static final float TOLERANCE = 1e-6f;

    /**
     * Compares the given values just like {@link Float#compareTo(Float)}, but
     * automatically taking a specified margin of error into account.
     * <p>
     * The margin of error is an arbitrarily small value that will be used to
     * determine whether the distance between the arguments being compared is
     * small enough to consider them equal.
     *
     * @param a
     *            The first value.
     * @param b
     *            The second value.
     * @param tolerance
     *            The margin of error.
     * @return
     *         <ul>
     *         <li><code>1 iff a > b</code></li>
     *         <li><code>0 iff abs(a - b) <= {@link #EPSILON}</code></li>
     *         <li><code>-1 iff a < b</code></li>
     *         </ul>
     */
    public static int compare(final float a, final float b, final float tolerance) {
        if (isEqual(a, b, tolerance))
            return 0;
        else if (a > b)
            return 1;
        else
            return -1;
    }

    /**
     * Compares the given values just like {@link Float#compareTo(Float)}, but
     * automatically taking {@link #EPSILON} into account.
     * <p>
     * The distance between the arguments being compared must be within
     * {@link #EPSILON} to be considered equal.
     *
     * @param a
     *            The first value.
     * @param b
     *            The second value.
     * @return
     *         <ul>
     *         <li><code>1 iff a > b</code></li>
     *         <li><code>0 iff abs(a - b) <= {@link #EPSILON}</code></li>
     *         <li><code>-1 iff a < b</code></li>
     *         </ul>
     */
    public static int compare(final float a, final float b) {
        return compare(a, b, EPSILON);
    }

    /**
     * Checks whether the arguments can be considered equal to each other,
     * within the given tolerance.
     *
     * @param a
     *            The first value.
     * @param b
     *            The second value.
     * @param tolerance
     *            The margin of error.
     * @return True if they're equal. Otherwise false.
     */
    public static boolean isEqual(final float a, final float b, final float tolerance) {
        if (Math.abs(a - b) <= tolerance)
            return true;

        return Float.floatToIntBits(a) == Float.floatToIntBits(b);
    }

    /**
     * Checks whether the arguments can be considered equal to each other, using
     * {@link #EPSILON} as the default tolerance.
     *
     * @param a
     *            The first value.
     * @param b
     *            The second value.
     * @return True if they're equal. Otherwise false.
     */
    public static boolean isEqual(final float a, final float b) {
        return isEqual(a, b, EPSILON);
    }

    /**
     * Checks whether the argument is zero, within the given tolerance.
     *
     * @param a
     *            The value to check.
     * @param tolerance
     *            The margin of error.
     * @return True if its distance from zero is within the margin of error.
     *         Otherwise false.
     */
    public static boolean isZero(final float a, final float tolerance) {
        return Math.abs(a) <= tolerance;
    }

    /**
     * Checks whether the argument is zero, using {@link #EPSILON} as the
     * default tolerance.
     *
     * @param a
     *            The value to check.
     * @return True if its distance from zero is within {@link #EPSILON}.
     *         Otherwise false.
     */
    public static boolean isZero(final float a) {
        return isZero(a, EPSILON);
    }

}
