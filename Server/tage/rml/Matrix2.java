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
 * This interface defines the operations for a square 2x2 {@link Matrix matrix}.
 * <p>
 * Implementations are meant to use a column-major layout. This is the standard
 * convention followed by mathematical texts and OpenGL/GLSL.
 * <p>
 * This interface implies this convention by the overloaded
 * <code>#mult(Vector2)</code> method expecting a {@link Vector2 vector}, where
 * the {@link Vector2 vector} being multiplied is on the right side of the
 * {@link Matrix2 matrix}. In other words, a {@link Matrix2 matrix}
 * <code>M</code> and a {@link Vector2 vector} <code>v</code> must be multiplied
 * as <code>v' = Mv</code> to produce a new column-{@link Vector2 vector}
 * <code>v'</code> as the output.
 *
 * @author Raymond L. Rivera
 *
 */
public interface Matrix2 extends Matrix<Matrix2, Vector2>, AngleRotatable<Matrix2> {

    /**
     * Returns the scalar value at the specified row and column.
     *
     * @param row
     *            A zero-based index specifying the desired {@link Matrix2
     *            matrix} row.
     * @param col
     *            A zero-based index specifying the desired {@link Matrix2
     *            matrix} column.
     * @return The scalar value found at the specified location.
     * @throws IndexOutOfBoundsException
     *             If either index is outside the [0, 1] range.
     */
    @Override
    float value(int row, int col);

    /**
     * Returns the specified {@link Matrix2 matrix} row as a {@link Vector2
     * vector}.
     *
     * @param row
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} row.
     * @return A {@link Vector2 vector} with data from the specified row.
     * @throws IndexOutOfBoundsException
     *             If the row index is outside the [0, 1] range.
     */
    @Override
    Vector2 row(int row);

    /**
     * Returns the specified {@link Matrix2 matrix} column as a {@link Vector2
     * vector}.
     *
     * @param col
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} column.
     * @return A {@link Vector2 vector} with data from the specified column.
     * @throws IndexOutOfBoundsException
     *             If the column index is outside the [0, 1] range.
     */
    @Override
    Vector2 column(int col);

    /**
     * Add the specified {@link Matrix2 matrix} to this {@link Matrix2 matrix}.
     *
     * @param m
     *            The {@link Matrix2 matrix} that will be added to this
     *            {@link Matrix2 matrix}.
     * @return A new {@link Matrix2 matrix} with the result of this addition.
     */
    @Override
    Matrix2 add(final Matrix2 m);

    /**
     * Subtract the specified {@link Matrix2 matrix} from this {@link Matrix
     * matrix}.
     *
     * @param m
     *            The {@link Matrix2 matrix} that will be subtracted from this
     *            {@link Matrix2 matrix}.
     * @return A new {@link Matrix2 matrix} with the result of this subtraction.
     */
    @Override
    Matrix2 sub(final Matrix2 m);

    /**
     * Multiply this {@link Matrix2 matrix} by the specified scalar value.
     *
     * @param scalar
     *            The scalar value by which this {@link Matrix2 matrix} will be
     *            multiplied or scaled.
     * @return A new {@link Matrix2 matrix} with the result of this
     *         multiplication.
     */
    @Override
    Matrix2 mult(float scalar);

    /**
     * Multiply this {@link Matrix2 matrix} by the specified column vector.
     *
     * @param v
     *            The {@link Vector2 vector} by which this {@link Matrix2
     *            matrix} will be multiplied or scaled.
     * @return A new {@link Matrix2 matrix} with the result of this
     *         multiplication.
     */
    @Override
    Vector2 mult(final Vector2 v);

    /**
     * Multiply this {@link Matrix2 matrix} by the specified {@link Matrix
     * matrix}. Also known as concatenation.
     *
     * @param m
     *            The {@link Matrix2 matrix} that will be multiplied by, or
     *            concatenated to, this {@link Matrix2 matrix}.
     * @return A new {@link Matrix2 matrix} with the result of this
     *         multiplication.
     */
    @Override
    Matrix2 mult(final Matrix2 m);

    /**
     * Rotate this {@link Matrix2 matrix} by the specified {@link Angle angle}.
     *
     * @param angle
     *            The {@link Angle angle} of rotation to be applied.
     * @return A new {@link Matrix2 matrix} with the specified rotation applied.
     */
    @Override
    Matrix2 rotate(final Angle angle);

    /**
     * Scale this {@link Matrix2 matrix} by the specified scalar values.
     *
     * @param sx
     *            A value used to scale the x component in the first column.
     * @param sy
     *            A value used to scale the y component in the second column.
     * @return A new {@link Matrix2 matrix} scaled by the specified values.
     */
    Matrix2 scale(float sx, float sy);

    /**
     * Scale this {@link Matrix2 matrix} by the specified column vector.
     *
     * @param v
     *            A {@link Vector2 vector} used to scale this {@link Matrix
     *            matrix}.
     * @return A new {@link Matrix2 matrix} scaled by the specified column
     *         vector.
     */
    @Override
    Matrix2 scale(final Vector2 v);

    /**
     * Returns the determinant of this {@link Matrix2 matrix}.
     * <p>
     * Note that if the determinant is zero, then this {@link Matrix2 matrix} is
     * singular and cannot be inverted.
     *
     * @return The determinant of this {@link Matrix2 matrix}.
     */
    @Override
    float determinant();

    /**
     * Returns a new {@link Matrix2 matrix} that is the inverse of this
     * {@link Matrix2 matrix}, if possible.
     * <p>
     * If this {@link Matrix2 matrix} is a singular {@link Matrix2 matrix}, then
     * it's not possible to calculate its inverse.
     *
     * @return The inverse of this {@link Matrix2 matrix}.
     * @throws RuntimeException
     *             If this is a non-invertible {@link Matrix2 matrix}.
     */
    @Override
    Matrix2 inverse();

    /**
     * Returns a new {@link Matrix2 matrix} that is the transpose of this
     * {@link Matrix2 matrix}.
     * <p>
     * Transposing a {@link Matrix2 matrix} means swapping the columns with the
     * rows.
     *
     * @return The transpose of this {@link Matrix2 matrix}.
     */
    @Override
    Matrix2 transpose();

}
