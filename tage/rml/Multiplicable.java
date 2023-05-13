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
 * A <i>multiplicable</i> object is one that supports multiplication.
 *
 * @author Raymond L. Rivera
 *
 * @param <T>
 *            The object type that supports multiplication.
 */
interface Multiplicable<T> {

    /**
     * Returns a new instance with the product of <code>this</code> object multiplied
     * by another object.
     *
     * @param o
     *            The object that will be multiplied by <code>this</code> object.
     * @return A new instance with the product of this multiplication.
     */
    T mult(final T o);

    /**
     * Returns a new instance with the product of <code>this</code> object multiplied
     * by a scalar value.
     *
     * @param s
     *            The scalar value that will be multiplied by <code>this</code>
     *            instance.
     * @return A new instance with the product of this multiplication.
     */
    T mult(float s);

}
