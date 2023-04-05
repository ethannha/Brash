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
 * Types supporting the dot-product operation must implement this interface.
 *
 * @author Raymond L. Rivera
 *
 * @param <T>
 *            A type that supports scalar product operations.
 */
interface ScalarProduct<T> {

    /**
     * Returns the scalar dot-product.
     * <p>
     * The dot-product, also known as the inner product, will be a scalar value
     * in the [-1, 1] range because it's the <i>cosine</i> of the {@link Angle
     * angle} between them, not the {@link Angle angle} itself. In other words,
     * for the scalar value to actually represent an angle, in radians, you must
     * calculate the inverse cosine of it.
     *
     * @param o
     *            The instance used to calculate the dot-product.
     * @return The scalar value of the dot-product.
     */
    float dot(T o);

}
