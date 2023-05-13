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
 * A <i>bufferable</i> object is one that supports making data available for a
 * buffer for more efficient processing.
 * <p>
 * Objects that need to store their data in OpenGL buffers should implement this
 * interface.
 *
 * @author Raymond L. Rivera
 *
 */
interface Bufferable {

    /**
     * Returns an array with a copy of the object's internal values.
     *
     * @return A copy of the object's internal values as an array.
     */
    float[] toFloatArray();

}
