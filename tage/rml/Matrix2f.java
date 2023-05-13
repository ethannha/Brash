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
 * An immutable single-precision floating point column-major 2x2 square matrix.
 * <p>
 * This means the following inputs:
 *
 * <pre>
 * float[] { 1, 2, 3, 4 };
 * float[][] {
 *     { 1, 2 },
 *     { 3, 4 }
 * };
 * </pre>
 *
 * produce the following matrix layout:
 * <p>
 * 1 3
 * 2 4
 * <p>
 *
 * The implementation assumes the input arrays have enough elements to build the
 * matrix. When the input array does not have enough elements, an
 * IndexOutOfBoundsException will be thrown. However, when the input
 * array has extra elements, the extra data are ignored.
 *
 * @author Raymond L. Rivera
 *
 */
public final class Matrix2f implements Matrix2 {

    private static final int     DIMENSIONS      = 2;

    private static final Matrix2 ZERO_MATRIX     = new Matrix2f(0f, 0f, 0f, 0f);
    private static final Matrix2 IDENTITY_MATRIX = new Matrix2f(1f, 0f, 0f, 1f);

    private final float[][]      matrix          = new float[DIMENSIONS][DIMENSIONS];

    private Matrix2f(float m00, float m01, float m10, float m11) {
        matrix[0][0] = m00;
        matrix[0][1] = m01;
        matrix[1][0] = m10;
        matrix[1][1] = m11;
    }

    private Matrix2f(final float[][] data) {
        this(data[0][0], data[0][1], data[1][0], data[1][1]);
    }

    private Matrix2f(final float[] data) {
        this(data[0], data[1], data[2], data[3]);
    }

    /**
     * Creates a new {@link Matrix2 matrix} with all values set to zero.
     *
     * @return A new zero-value {@link Matrix2 matrix}.
     */
    public static Matrix2 createZeroMatrix() {
        return ZERO_MATRIX;
    }

    /**
     * Creates a new identity matrix.
     * <p>
     * The identity matrix has values along the diagonal set to one and all others
     * set to zero.
     *
     * @return A new identity matrix.
     */
    public static Matrix2 createIdentityMatrix() {
        return IDENTITY_MATRIX;
    }

    /**
     * Creates a new matrix from the specified values.
     *
     * @param values
     *            An array of at least 4 values, in column-major order.
     * @return A new matrix from the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 elements.
     */
    public static Matrix2 createFrom(final float[] values) {
        return new Matrix2f(values);
    }

    /**
     * Creates a new matrix from the specified values.
     *
     * @param values
     *            A 2x2 array of values, in column-major order.
     * @return A new matrix from the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 2 elements per dimension.
     */
    public static Matrix2 createFrom(final float[][] values) {
        return new Matrix2f(values);
    }

    /**
     * Creates a new matrix from the specified column vectors.
     *
     * @param c0
     *            A Vector2 with values for the first column.
     * @param c1
     *            A Vector2 with values for the second column.
     * @return A new matrix from the specified values.
     */
    public static Matrix2 createFrom(final Vector2 c0, final Vector2 c1) {
        return new Matrix2f(c0.x(), c0.y(), c1.x(), c1.y());
    }

    /**
     * Creates a new inverse matrix from the specified values, if
     * possible.
     *
     * @param values
     *            An array of at least 4 values, in column-major order.
     * @return A new inverse matrix calculated from the specified
     *         values.
     * @throws ArithmeticException
     *             If the values represent a singular matrix. A singular matrix is
     *             non-invertible because its determinant is zero.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 elements.
     */
    public static Matrix2 createInverseFrom(final float[] values) {
        return createInverseImpl(values[0], values[1], values[2], values[3]);
    }

    /**
     * Creates a new inverse {@link Matrix2 matrix} from the specified values, if
     * possible.
     *
     * @param values
     *            A 2x2 array of values, in column-major order.
     * @return A new inverse matrix calculated from the specified
     *         values.
     * @throws ArithmeticException
     *             If the values represent a singular matrix. A singular matrix is
     *             non-invertible because its determinant is zero.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 2 elements per dimension.
     */
    public static Matrix2 createInverseFrom(final float[][] values) {
        return createInverseImpl(values[0][0], values[0][1], values[1][0], values[1][1]);
    }

    /**
     * Creates a new inverse matrix from the specified values, if
     * possible.
     *
     * @param c0
     *            A Vector2 with values for the first column.
     * @param c1
     *            A Vector2 with values for the second column.
     * @return A new inverse matrix calculated from the specified
     *         column vectors.
     * @throws ArithmeticException
     *             If the values represent a singular matrix. A singular matrix is
     *             non-invertible because its determinant is zero.
     */
    public static Matrix2 createInverseFrom(final Vector2 c0, final Vector2 c1) {
        return createInverseImpl(c0.x(), c0.y(), c1.x(), c1.y());
    }

    /**
     * Creates a new transposed matrix from the specified values.
     *
     * @param data
     *            An array of at least 4 values, in column-major order.
     * @return A new transposed matrix calculated from the specified
     *         values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 values.
     */
    public static Matrix2 createTransposeFrom(final float[] data) {
        return new Matrix2f(data[0], data[2], data[1], data[3]);
    }

    /**
     * Creates a new transposed matrix from the specified values.
     *
     * @param data
     *            A 2x2 array of values, in column-major order.
     * @return A new transposed matrix calculated from the specified
     *         values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 2 elements per dimension.
     */
    public static Matrix2 createTransposeFrom(final float[][] data) {
        return new Matrix2f(data[0][0], data[1][0], data[0][1], data[1][1]);
    }

    /**
     * Creates a new transposed matrix from the specified values.
     *
     * @param c0
     *            A Vector2 with values for the first column.
     * @param c1
     *            A Vector2 with values for the second column.
     * @return A new transposed matrix calculated from the specified
     *         column vectors.
     */
    public static Matrix2 createTransposeFrom(final Vector2 c0, final Vector2 c1) {
        return new Matrix2f(c0.x(), c1.x(), c0.y(), c1.y());
    }

    /**
     * Creates a new matrix from the specified scaling values.
     *
     * @param sx
     *            A scalar value used to scale the x component in the first column.
     * @param sy
     *            A scalar value used to scale the y component in the second column.
     * @return A new matrix that can be used to scale another
     *         matrix when multiplied together.
     */
    public static Matrix2 createScalingFrom(float sx, float sy) {
        return new Matrix2f(sx, 0f, 0f, sy);
    }

    /**
     * Creates a new matrix from the specified scaling values.
     *
     * @param sv
     *            A Vector2 with scaling values for the x and y components.
     * @return A new matrix that can be used to scale another
     *         matrix when multiplied together.
     */
    public static Matrix2 createScalingFrom(final Vector2 sv) {
        return createScalingFrom(sv.x(), sv.y());
    }

    /**
     * Creates a new {@link Matrix2 matrix} rotated by the specified {@link Angle
     * angle}.
     *
     * @param angle
     *            The {@link Angle angle} of rotation to be applied.
     * @return A new {@link Matrix2 matrix} with the specified rotation applied.
     */
    public static Matrix2 createRotationFrom(final Angle angle) {
        final float theta = angle.valueRadians();
        final float sint = MathUtil.sin(theta);
        final float cost = MathUtil.cos(theta);

        return new Matrix2f(cost, sint, -sint, cost);
    }

    @Override
    public float value(int row, int col) {
        return matrix[col][row];
    }

    @Override
    public Vector2 row(int row) {
        return Vector2f.createFrom(matrix[0][row], matrix[1][row]);
    }

    @Override
    public Vector2 column(int col) {
        return Vector2f.createFrom(matrix[col][0], matrix[col][1]);
    }

    @Override
    public Matrix2 add(final Matrix2 m) {
        final float m00 = matrix[0][0] + m.value(0, 0);
        final float m01 = matrix[0][1] + m.value(1, 0);
        final float m10 = matrix[1][0] + m.value(0, 1);
        final float m11 = matrix[1][1] + m.value(1, 1);
        return new Matrix2f(m00, m01, m10, m11);
    }

    @Override
    public Matrix2 sub(final Matrix2 m) {
        final float m00 = matrix[0][0] - m.value(0, 0);
        final float m01 = matrix[0][1] - m.value(1, 0);
        final float m10 = matrix[1][0] - m.value(0, 1);
        final float m11 = matrix[1][1] - m.value(1, 1);
        return new Matrix2f(m00, m01, m10, m11);
    }

    @Override
    public Matrix2 mult(float s) {
        final float m00 = matrix[0][0] * s;
        final float m01 = matrix[0][1] * s;
        final float m10 = matrix[1][0] * s;
        final float m11 = matrix[1][1] * s;
        return new Matrix2f(m00, m01, m10, m11);
    }

    @Override
    public Vector2 mult(final Vector2 v) {
        final float tx = matrix[0][0] * v.x() + matrix[1][0] * v.y();
        final float ty = matrix[0][1] * v.x() + matrix[1][1] * v.y();
        return Vector2f.createFrom(tx, ty);
    }

    @Override
    public Matrix2 mult(final Matrix2 m) {
        final float m00 = value(0, 0) * m.value(0, 0) + value(0, 1) * m.value(1, 0);
        final float m10 = value(0, 0) * m.value(0, 1) + value(0, 1) * m.value(1, 1);
        final float m01 = value(1, 0) * m.value(0, 0) + value(1, 1) * m.value(1, 0);
        final float m11 = value(1, 0) * m.value(0, 1) + value(1, 1) * m.value(1, 1);
        return new Matrix2f(m00, m01, m10, m11);
    }

    @Override
    public Matrix2 rotate(final Angle angle) {
        return mult(createRotationFrom(angle));
    }

    @Override
    public Matrix2 scale(float sx, float sy) {
        return mult(createScalingFrom(sx, sy));
    }

    @Override
    public Matrix2 scale(final Vector2 v) {
        return scale(v.x(), v.y());
    }

    @Override
    public float determinant() {
        return getDeterminant(matrix[0][0], matrix[0][1], matrix[1][0], matrix[1][1]);
    }

    @Override
    public Matrix2 inverse() {
        return createInverseFrom(matrix);
    }

    @Override
    public Matrix2 transpose() {
        return createTransposeFrom(matrix);
    }

    /**
     * The values are ordered as documented {@link Matrix2f here}.
     *
     * @see Bufferable#toFloatArray()
     */
    @Override
    public float[] toFloatArray() {
        return MatrixUtil.toFlatArray(matrix);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int result = 3;
        return prime * result + matrix.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Matrix2))
            return false;

        Matrix2 other = (Matrix2) obj;
        return MatrixUtil.areEqual(this, other, DIMENSIONS);
    }

    @Override
    public String toString() {
        // TODO: Improve formatting
        StringBuilder fmt = new StringBuilder();
        fmt.append(Matrix2f.class.getSimpleName() + " = [%9.5f | %9.5f]%n");
        fmt.append("           [%9.5f | %9.5f]");

        // @formatter:off
        return String.format(fmt.toString(),
            matrix[0][0], matrix[1][0],
            matrix[0][1], matrix[1][1]
        );
        // @formatter:on
    }

    private static float getDeterminant(float m00, float m01, float m10, float m11) {
        return m00 * m11 - m01 * m10;
    }

    private static Matrix2 createInverseImpl(float m00, float m01, float m10, float m11) {
        float det = getDeterminant(m00, m01, m10, m11);

        if (FloatUtil.isZero(det))
            throw new ArithmeticException("Matrix determinant is zero: non-invertible matrix");

        det = 1.0f / det;

        // https://www.khanacademy.org/math/precalculus/precalc-matrices/inverting_matrices/v/inverse-of-a-2x2-matrix
        // calculate determinant and then multiply 1/det * adjugate in one step
        return new Matrix2f(m11 * det, -m01 * det, -m10 * det, m00 * det);
    }

}
