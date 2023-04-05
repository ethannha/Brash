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
 * A <i>translatable</i> object is one that can be positioned and moved around
 * in 3D space.
 *
 * @author Raymond L. Rivera
 *
 * @param <T>
 *            The type that supports translation.
 * @param <V>
 *            The {@link Vector vector} type.
 */
interface Translatable<T, V extends Vector<V>> {

    /**
     * Translate <code>this</code> instance by the specified vector.
     *
     * @param v
     *            The {@link Vector vector} by which to translate <code>this</code>
     *            instance, where each vector component provides the translation
     *            value for each axis.
     *
     * @return A new instance with the translation applied.
     */
    T translate(final V v);

}
