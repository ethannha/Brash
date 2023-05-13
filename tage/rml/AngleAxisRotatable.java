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
 * An <i>angle-axis rotatable</i> object is one that can be rotated by an
 * {@link Angle angle} about an arbitrary {@link Vector axis}.
 *
 * @author Raymond L. Rivera
 *
 * @param <R>
 *            The type supporting rotation.
 * @param <V>
 *            The {@link Vector vector} type for the rotation axis.
 */
interface AngleAxisRotatable<R, V extends Vector<V>> {

    /**
     * Rotate this instance by the specified {@link Angle angle} around the
     * specified {@link Vector axis}.
     *
     * @param angle
     *            The {@link Angle angle} of rotation to be applied.
     * @param axis
     *            The {@link Vector axis} around which to rotate.
     * @return A new instance with the specified rotation applied.
     */
    R rotate(final Angle angle, final V axis);

}
