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
 * A <i>negatable</i> object is one that supports negation.
 *
 * @author Raymond L. Rivera
 *
 */
interface Negatable<T> {

    /**
     * Returns a new instance that is the negation of <code>this</code>.
     * <p>
     * Negation is the mathematical equivalent of: <code>this * -1.0f</code>.
     *
     * @return A new instance with the result of this negation.
     */
    T negate();

}
