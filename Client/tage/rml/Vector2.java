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
 * This interface defines the operations for a 2-component {@link Vector
 * vector}.
 * <p>
 * Implementations are meant to use a column-major layout. This is the standard
 * convention followed by mathematical texts and OpenGL/GLSL.
 * <p>
 * This interface implies this convention by the lack of a <code>mult</code>
 * method expecting a {@link Matrix2 matrix}, where the {@link Vector2 vector}
 * being multiplied is on the left side of the matrix. In other words, it's not
 * possible to take a matrix <code>M</code> and a {@link Vector2 vector}
 * <code>v</code> and multiply them as <code>v' = vM</code> to produce a new row
 * {@link Vector2 vector} <code>v'</code> as the output.
 * <p>
 * For those with a Microsoft Direct3D background, note that they follow a
 * convention opposite to the standard described above.
 *
 * @see Matrix2
 *
 * @author Raymond L. Rivera
 *
 */
public interface Vector2 extends Vector<Vector2>, TwoDimensional, AngleRotatable<Vector2> {

    /**
     * Adds the specified scalar values to the respective components of
     * <code>this</code> {@link Vector2 vector}.
     *
     * @param x
     *            The scalar to add to <code>this</code> x component.
     * @param y
     *            The scalar to add to <code>this</code> y component.
     * @return A new {@link Vector2 vector} with the result of this addition.
     */
    Vector2 add(float x, float y);

    /**
     * Subtracts the specified scalar values from the respective components of
     * <code>this</code> {@link Vector2 vector}.
     *
     * @param x
     *            The scalar to subtract from <code>this</code> x component.
     * @param y
     *            The scalar to subtract from <code>this</code> y component.
     * @return A new {@link Vector2 vector} with the result of this subtraction.
     */
    Vector2 sub(float x, float y);

}
