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
 * <i>Invertible</i> objects can be inverted.
 * <p>
 * {@link Matrix Matrices} are good candidates for implementing this interface,
 * though they could be found to be non-invertible (i.e. singular) at run-time.
 *
 * @author Raymond L. Rivera
 *
 * @param <T>
 *            The object type that can be inverted.
 */
interface Invertible<T> {

    /**
     * Returns a new instance that's the inverse of <code>this</code>, if
     * possible.
     * <p>
     * Checks to determine whether <code>this</code> can be successfully
     * inverted. Inversion will not be possible if a division by zero takes
     * place.
     *
     * @return The inverse of <code>this</code> instance.
     * @throws ArithmeticException
     *             If <code>this</code> is non-invertible.
     */
    T inverse();

}
