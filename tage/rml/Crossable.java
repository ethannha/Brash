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
 * <i>Crossable</i> objects support calculating the cross-product, such as
 * {@link Vector vectors}.
 * <p>
 * A cross-product, also called cartesian product, produces a {@link Vector
 * vector} that is perpendicular to the plane of the two original vectors and is
 * useful for calculating normal (not normalized) vectors.
 *
 * @author Raymond L. Rivera
 *
 * @param <V>
 *            A more specialized {@link Vector vector} type that supports the
 *            calculation of a cross-product.
 */
interface Crossable<V extends Vector<V>> {

    /**
     * Returns the cross-product between <code>this</code> and the specified
     * {@link Vector vectors}.
     * <p>
     * The {@link Vector vector} returned is orthogonal to the plane defined by
     * <code>this</code> {@link Vector vector} and the one specified.
     *
     * @param v
     *            The {@link Vector vector} used to calculate the cross-product.
     * @return A new {@link Vector vector} perpendicular to the plane of
     *         <code>this</code> and other {@link Vector vector}.
     */
    V cross(final V v);

}
