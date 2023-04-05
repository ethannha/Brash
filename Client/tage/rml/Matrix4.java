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
 * This interface defines the operations for a square 4x4 {@link Matrix matrix}.
 * <p>
 * Implementations are meant to use a column-major layout. This is the standard
 * convention followed by mathematical texts and OpenGL/GLSL.
 * <p>
 * This interface implies this convention by the overloaded
 * mult(Vector4) method expecting a {@link Vector4 vector}, where
 * the {@link Vector4 vector} being multiplied is on the right side of the
 * {@link Matrix4 matrix}. In other words, a {@link Matrix4 matrix}
 * <code>M</code> and a {@link Vector4 vector}
 * <code>v</code>} must be multiplied as <code>v' = Mv</code> to produce a new
 * column-{@link Vector4 vector} <code>v'</code> as the output.
 *
 * @author Raymond L. Rivera
 *
 */
public interface Matrix4 extends Matrix<Matrix4, Vector4>, AngleAxisRotatable<Matrix4, Vector4>, Translatable<Matrix4, Vector4> {

    /**
     * Returns the scalar value at the specified row and column.
     *
     * @param row
     *            A zero-based index specifying the desired {@link Matrix4
     *            matrix} row.
     * @param col
     *            A zero-based index specifying the desired {@link Matrix4
     *            matrix} column.
     * @return The scalar value found at the specified location.
     * @throws IndexOutOfBoundsException
     *             If either index is outside the [0, 2] range.
     */
    @Override
    float value(int row, int col);

    /**
     * Returns the specified {@link Matrix4 matrix} row as a {@link Vector4
     * vector} instance.
     *
     * @param row
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} row.
     * @return A {@link Vector4 vector} with data from the specified row.
     * @throws IndexOutOfBoundsException
     *             If the row index is outside the [0, 3] range.
     */
    @Override
    Vector4 row(int row);

    /**
     * Returns the specified {@link Matrix4 matrix} column as a {@link Vector4
     * vector} instance.
     *
     * @param col
     *            A zero-based index specifying the desired {@link Matrix4
     *            matrix} column.
     * @return A {@link Vector4 vector} with data from the specified column.
     * @throws IndexOutOfBoundsException
     *             If the column index is outside the [0, 3] range.
     */
    @Override
    Vector4 column(int col);

    /**
     * Add the specified {@link Matrix4 matrix} to this {@link Matrix4 matrix}.
     *
     * @param m
     *            The {@link Matrix4 matrix} that will be added to this
     *            {@link Matrix4 matrix}.
     * @return A new {@link Matrix4 matrix} with the result of this addition.
     */
    @Override
    Matrix4 add(final Matrix4 m);

    /**
     * Subtract the specified {@link Matrix4 matrix} from this {@link Matrix4
     * matrix}.
     *
     * @param m
     *            The {@link Matrix4 matrix} that will be subtracted from this
     *            {@link Matrix4 matrix}.
     * @return A new {@link Matrix4 matrix} with the result of this subtraction.
     */
    @Override
    Matrix4 sub(final Matrix4 m);

    /**
     * Multiply this {@link Matrix4 matrix} by the specified scalar value.
     *
     * @param scalar
     *            The scalar value by which this {@link Matrix4 matrix} will be
     *            multiplied or scaled.
     * @return A new {@link Matrix4 matrix} with the result of this
     *         multiplication.
     */
    @Override
    Matrix4 mult(float scalar);

    /**
     * Multiply this {@link Matrix4 matrix} by the specified column vector.
     *
     * @param v
     *            The {@link Vector3 vector} by which this {@link Matrix4
     *            matrix} will be multiplied or scaled.
     * @return A new {@link Matrix4 matrix} with the result of this
     *         multiplication.
     */
    @Override
    Vector4 mult(final Vector4 v);

    /**
     * Multiply this {@link Matrix4 matrix} by the specified {@link Matrix4
     * matrix}. Also known as concatenation.
     *
     * @param m
     *            The {@link Matrix4 matrix} that will be multiplied by, or
     *            concatenated to, this {@link Matrix4 matrix}.
     * @return A new {@link Matrix4 matrix} with the result of this
     *         multiplication.
     */
    @Override
    Matrix4 mult(final Matrix4 m);

    /**
     * Rotate this {@link Matrix4 matrix} by the specified angles.
     *
     * @param angleX
     *            The {@link Angle angle} of rotation for the x axis.
     * @param angleY
     *            The {@link Angle angle} of rotation for the y axis.
     * @param angleZ
     *            The {@link Angle angle} of rotation for the z axis.
     * @return A new {@link Matrix4 matrix} with the specified rotation applied.
     */
    Matrix4 rotate(final Angle angleX, final Angle angleY, final Angle angleZ);

    /**
     * Rotate this {@link Matrix4 matrix} by the specified angle along the
     * specified {@link Vector3 axis}.
     *
     * @param angle
     *            The {@link Angle angle} of rotation to be applied.
     * @param axis
     *            A {@link Vector3 vector} specifying the axis of rotation.
     * @return A new {@link Matrix4 matrix} with the specified rotation applied.
     */
    Matrix4 rotate(final Angle angle, final Vector3 axis);

    /**
     * Rotate this {@link Matrix4 matrix} by the specified angle along the
     * specified {@link Vector4 axis}.
     *
     * @param angle
     *            The {@link Angle angle} of rotation to be applied.
     * @param axis
     *            A {@link Vector4 vector} instance specifying the axis of
     *            rotation.
     * @return A new {@link Matrix4 matrix} with the specified rotation applied.
     */
    @Override
    Matrix4 rotate(final Angle angle, final Vector4 axis);

    /**
     * Translate this {@link Matrix4 matrix} by the specified amounts.
     *
     * @param tx
     *            A scalar value by which to translate this {@link Matrix4
     *            matrix} on the x-axis.
     * @param ty
     *            A scalar value by which to translate this {@link Matrix4
     *            matrix} on the y-axis.
     * @param tz
     *            A scalar value by which to translate this {@link Matrix4
     *            matrix} on the z-axis.
     * @return A new {@link Matrix4 matrix} with the translation applied.
     */
    Matrix4 translate(float tx, float ty, float tz);

    /**
     * Translate this {@link Matrix4 matrix} by the specified column
     * {@link Vector3 vector} .
     *
     * @param tv
     *            A {@link Vector3 vector} by which to translate this
     *            {@link Matrix4 matrix}.
     * @return A new {@link Matrix4 matrix} with the translation applied.
     */
    Matrix4 translate(final Vector3 tv);

    /**
     * Translate this {@link Matrix4 matrix} by the specified column vector.
     *
     * @param tv
     *            A {@link Vector4 vector} instance by which to translate this
     *            {@link Matrix4 matrix}.
     * @return A new {@link Matrix4 matrix} with the translation applied.
     */
    @Override
    Matrix4 translate(final Vector4 tv);

    /**
     * Scale this {@link Matrix4 matrix} by the specified amounts.
     *
     * @param sx
     *            A scalar value by which to scale this {@link Matrix4 matrix}
     *            on the x axis.
     * @param sy
     *            A scalar value by which to scale this {@link Matrix4 matrix}
     *            on the y axis.
     * @param sz
     *            A scalar value by which to scale this {@link Matrix4 matrix}
     *            on the z axis.
     * @return A new {@link Matrix4 matrix} with the scaling applied.
     */
    Matrix4 scale(float sx, float sy, float sz);

    /**
     * Scale this {@link Matrix4 matrix} by the specified amounts.
     *
     * @param sv
     *            A {@link Vector3 vector} by which to scale this {@link Matrix4
     *            matrix}.
     * @return A new {@link Matrix4 matrix} with the scaling applied.
     */
    Matrix4 scale(final Vector3 sv);

    /**
     * Scale this {@link Matrix4 matrix} by the specified amounts.
     *
     * @param sv
     *            A {@link Vector4 vector} instance by which to scale this
     *            {@link Matrix4 matrix}.
     * @return A new {@link Matrix4 matrix} with the scaling applied.
     */
    @Override
    Matrix4 scale(final Vector4 sv);

    /**
     * Returns the determinant of this {@link Matrix4 matrix}.
     * <p>
     * Note that if the determinant is zero, then this {@link Matrix4 matrix} is
     * singular and cannot be inverted.
     *
     * @return The determinant of this {@link Matrix4 matrix}.
     */
    @Override
    float determinant();

    /**
     * Returns a new {@link Matrix4 matrix} that is the inverse of this matrix,
     * if possible.
     * <p>
     * If this {@link Matrix4 matrix} is a singular matrix, then it's not
     * possible to calculate its inverse.
     *
     * @return The inverse of this {@link Matrix4 matrix}.
     * @throws RuntimeException
     *             If this is a non-invertible {@link Matrix4 matrix}.
     */
    @Override
    Matrix4 inverse();

    /**
     * Returns a new {@link Matrix4 matrix} that is the transpose of this
     * {@link Matrix4 matrix}.
     * <p>
     * Transposing a {@link Matrix4 matrix} means swapping the columns with the
     * rows.
     *
     * @return The transpose of this {@link Matrix4 matrix}.
     */
    @Override
    Matrix4 transpose();

    /**
     * Extract the upper left 3x3 sub-{@link Matrix3 matrix} from this
     * {@link Matrix4 matrix}.
     *
     * @return A new {@link Matrix3 matrix} instance from the upper left values
     *         of this {@link Matrix4 matrix}.
     */
    Matrix3 toMatrix3();

    /**
     * Extracts a {@link Quaternion quaternion} with the orientation represented
     * by the upper 3x3 rotation {@link Matrix3 matrix} within this
     * {@link Matrix4 matrix}.
     *
     * @return The rotation in this {@link Matrix4 matrix} as a
     *         {@link Quaternion quaternion}.
     */
    Quaternion toQuaternion();

}
