/**
 * Copyright (C) 2015 Raymond L. Rivera <ray.l.rivera@gmail.com>
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
 * A <i>normalizable</i> object is one that can be normalized.
 * <p>
 * Types that can be normalized include {@link Vector vectors} and
 * {@link Quaternion quaternions}. Since calculating the length (or magnitude)
 * of an instance is necessary to normalize it, these methods are also included
 * in this interface.
 *
 * @author Raymond L. Rivera
 *
 * @param <T>
 *            The type that supports normalization.
 */
interface Normalizable<T> {

    /**
     * Returns a new normalized instance from <code>this</code> instance.
     * <p>
     *
     * @return A new normalized (unit-length) instance with a length of 1.
     *
     * @throws ArithmeticException
     *             If the length of <code>this</code> instance is zero.
     * @see #length()
     * @see #lengthSquared()
     */
    T normalize();

    /**
     * Returns the measured length (or magnitude) of <code>this</code> instance.
     *
     * @return The length of <code>this</code> instance.
     * @see #lengthSquared()
     */
    float length();

    /**
     * Returns the measured length of <code>this</code> instance without taking
     * the square root.
     * <p>
     * This method is provided for performance reasons. It avoids expensive
     * square root operations for simple comparisons (e.g. sorting).
     *
     * @return The squared length of <code>this</code> instance.
     * @see #length()
     */
    float lengthSquared();

    /**
     * Determines whether <code>this</code> instance has a length of zero or
     * not.
     * <p>
     * Zero-length instances should avoid calling {@link #normalize()}. Doing so
     * will result in an attempt to divide by zero.
     *
     * @return True if <code>this</code> instance is zero-length. False
     *         otherwise.
     * @see #length()
     * @see #lengthSquared()
     */
    boolean isZeroLength();

}
