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
 * A <i>scalable</i> object is one that can be scaled to change its size.
 *
 * @author Raymond L. Rivera
 *
 * @param <S>
 *            A type that supports scaling.
 * @param <V>
 *            A type representing the scaling {@link Vector vector}.
 */
interface Scalable<S, V extends Vector<V>> {

    /**
     * Returns a new instance with the scaling operation applied.
     *
     * @param v
     *            The instance that will be used to scale <code>this</code> object.
     * @return A new scaled instance.
     */
    S scale(final V v);

}
