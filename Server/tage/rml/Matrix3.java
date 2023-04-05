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
 * This interface defines the operations for a square 3x3 {@link Matrix matrix}.
 * <p>
 * Implementations are meant to use a column-major layout. This is the standard
 * convention followed by mathematical texts and OpenGL/GLSL.
 * <p>
 * This interface implies this convention by the overloaded
 * <code>#mult(Vector3)</code> method expecting a {@link Vector3 vector}, where
 * the {@link Vector3 vector} being multiplied is on the right side of the
 * {@link Matrix3 matrix}. In other words, a {@link Matrix3 matrix}
 * <code>M</code> and a {@link Vector3 vector} <code>v</code> must be multiplied
 * as <code>v' = Mv</code> to produce a new column-{@link Vector3 vector}
 * <code>v'</code> as the output.
 *
 * @author Raymond L. Rivera
 *
 */
public interface Matrix3 extends Matrix<Matrix3, Vector3>, AngleAxisRotatable<Matrix3, Vector3>, Translatable<Matrix3, Vector2> {

    /**
     * Returns the scalar value at the specified row and column.
     *
     * @param row
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} row.
     * @param col
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} column.
     * @return The scalar value found at the specified location.
     * @throws IndexOutOfBoundsException
     *             If either index is outside the [0, 2] range.
     */
    @Override
    float value(int row, int col);

    /**
     * Returns the specified {@link Matrix3 matrix} row as a {@link Vector3
     * vector}.
     *
     * @param row
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} row.
     * @return A {@link Vector3 vector} with data from the specified row.
     * @throws IndexOutOfBoundsException
     *             If the row index is outside the [0, 2] range.
     */
    @Override
    Vector3 row(int row);

    /**
     * Returns the specified {@link Matrix3 matrix} column as a {@link Vector3
     * vector}.
     *
     * @param col
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} column.
     * @return A {@link Vector3 vector} with data from the specified column.
     * @throws IndexOutOfBoundsException
     *             If the column index is outside the [0, 2] range.
     */
    @Override
    Vector3 column(int col);

    /**
     * Add the specified {@link Matrix3 matrix} to this {@link Matrix3 matrix}.
     *
     * @param m
     *            The {@link Matrix3 matrix} that will be added to this
     *            {@link Matrix3 matrix}.
     * @return A new {@link Matrix3 matrix} with the result of this addition.
     */
    @Override
    Matrix3 add(final Matrix3 m);

    /**
     * Subtract the specified {@link Matrix3 matrix} from this {@link Matrix3
     * matrix}.
     *
     * @param m
     *            The {@link Matrix3 matrix} that will be subtracted from this
     *            {@link Matrix3 matrix}.
     * @return A new {@link Matrix3 matrix} with the result of this subtraction.
     */
    @Override
    Matrix3 sub(final Matrix3 m);

    /**
     * Multiply this {@link Matrix3 matrix} by the specified scalar value.
     *
     * @param scalar
     *            The scalar value by which this {@link Matrix3 matrix} will be
     *            multiplied or scaled.
     * @return A new {@link Matrix3 matrix} with the result of this
     *         multiplication.
     */
    @Override
    Matrix3 mult(float scalar);

    /**
     * Multiply this {@link Matrix3 matrix} by the specified column vector.
     *
     * @param v
     *            The {@link Vector3 vector} by which this {@link Matrix3
     *            matrix} will be multiplied or scaled.
     * @return A new {@link Matrix3 matrix} with the result of this
     *         multiplication.
     */
    @Override
    Vector3 mult(final Vector3 v);

    /**
     * Multiply this {@link Matrix3 matrix} by the specified {@link Matrix3
     * matrix}. Also known as concatenation.
     *
     * @param m
     *            The {@link Matrix3 matrix} that will be multiplied by, or
     *            concatenated to, this {@link Matrix3 matrix}.
     * @return A new {@link Matrix3 matrix} with the result of this
     *         multiplication.
     */
    @Override
    Matrix3 mult(final Matrix3 m);

    /**
     * Rotate this {@link Matrix3 matrix} by the specified amounts.
     *
     * @param rotationX
     *            The {@link Angle} of rotation for the x axis.
     * @param rotationY
     *            The {@link Angle} of rotation for the y axis.
     * @param rotationZ
     *            The {@link Angle} of rotation for the z axis.
     * @return A new {@link Matrix3 matrix} with the specified rotation applied.
     */
    Matrix3 rotate(final Angle rotationX, final Angle rotationY, final Angle rotationZ);

    /**
     * Rotate this {@link Matrix3 matrix} by the specified angle along the
     * specified axis.
     *
     * @param angle
     *            The {@link Angle} of rotation to be applied.
     * @param axis
     *            A {@link Vector3 vector} specifying the axis of rotation.
     * @return A new {@link Matrix3 matrix} with the specified rotation applied.
     */
    @Override
    Matrix3 rotate(final Angle angle, final Vector3 axis);

    /**
     * Translate this {@link Matrix3 matrix} by the specified amounts.
     *
     * @param tx
     *            A scalar value by which to translate this {@link Matrix3
     *            matrix} on the x axis.
     * @param ty
     *            A scalar value by which to translate this {@link Matrix3
     *            matrix} on the y axis.
     * @return A new {@link Matrix3 matrix} with the translation applied.
     */
    Matrix3 translate(float tx, float ty);

    /**
     * Translate this {@link Matrix3 matrix} by the specified column vector.
     *
     * @param tv
     *            A {@link Vector2 vector} by which to translate this
     *            {@link Matrix3 matrix}.
     * @return A new {@link Matrix3 matrix} with the translation applied.
     */
    @Override
    Matrix3 translate(final Vector2 tv);

    /**
     * Scale this {@link Matrix3 matrix} by the specified amounts.
     *
     * @param sx
     *            A scalar value by which to scale this {@link Matrix3 matrix}
     *            on the x axis.
     * @param sy
     *            A scalar value by which to scale this {@link Matrix3 matrix}
     *            on the y axis.
     * @param sz
     *            A scalar value by which to scale this {@link Matrix3 matrix}
     *            on the z axis.
     * @return A new {@link Matrix3 matrix} with the scaling applied.
     */
    Matrix3 scale(float sx, float sy, float sz);

    /**
     * Scale this {@link Matrix3 matrix} by the specified amounts.
     *
     * @param v
     *            A {@link Vector3 vector} by which to scale this {@link Matrix3
     *            matrix}.
     * @return A new {@link Matrix3 matrix} with the scaling applied.
     */
    @Override
    Matrix3 scale(final Vector3 v);

    /**
     * Returns the determinant of this {@link Matrix3 matrix}.
     * <p>
     * Note that if the determinant is zero, then this {@link Matrix3 matrix} is
     * singular and cannot be inverted.
     *
     * @return The determinant of this {@link Matrix3 matrix}.
     */
    @Override
    float determinant();

    /**
     * Returns a new {@link Matrix3 matrix} that is the inverse of this
     * {@link Matrix3 matrix}, if possible.
     * <p>
     * If this {@link Matrix3 matrix} is a singular {@link Matrix3 matrix}, then
     * it's not possible to calculate its inverse.
     *
     * @return The inverse of this {@link Matrix3 matrix}.
     * @throws RuntimeException
     *             If this is a non-invertible {@link Matrix3 matrix}.
     */
    @Override
    Matrix3 inverse();

    /**
     * Returns a new {@link Matrix3 matrix} that is the transpose of this
     * {@link Matrix3 matrix}.
     * <p>
     * Transposing a {@link Matrix3 matrix} means swapping the columns with the
     * rows.
     *
     * @return The transpose of this {@link Matrix3 matrix}.
     */
    @Override
    Matrix3 transpose();

    /**
     * Extract the upper left 2x2 sub-{@link Matrix2 matrix} from this
     * {@link Matrix3 matrix}.
     *
     * @return A new {@link Matrix2} instance from the upper left values of this
     *         {@link Matrix3 matrix}.
     */
    Matrix2 toMatrix2();

    /**
     * Extracts a {@link Quaternion quaternion} with the orientation represented
     * by this rotation {@link Matrix3 matrix}.
     *
     * @return The rotation of this {@link Matrix3 matrix} as a
     *         {@link Quaternion quaternion}.
     */
    Quaternion toQuaternion();

}
