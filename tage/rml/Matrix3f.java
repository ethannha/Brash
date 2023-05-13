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
 * An immutable single-precision floating point column-major 3x3 square
 * {@link Matrix3 matrix}.
 * <p>
 * This means the following inputs:
 *
 * <pre>
 * float[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
 * float[][] {
 *     { 1, 2, 3 },
 *     { 4, 5, 6 },
 *     { 7, 8, 9 }
 * };
 * </pre>
 *
 * produce the following matrix layout:
 * <p>
 * <pre>
 * 1 4 7
 * 2 5 8
 * 3 6 9
 * </pre>
 * <p>
 *
 * The implementation assumes the input arrays have enough elements to build the
 * matrix. When the input array does not have enough elements, an
 * {@link IndexOutOfBoundsException} will be thrown. However, when the input
 * array has extra elements, the extra data are ignored.
 *
 * @author Raymond L. Rivera
 *
 */
public final class Matrix3f implements Matrix3 {

    private static final int      DIMENSIONS      = 3;

    private static final Matrix3f ZERO_MATRIX     = new Matrix3f(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
    private static final Matrix3f IDENTITY_MATRIX = new Matrix3f(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);

    private final float[][]       matrix          = new float[DIMENSIONS][DIMENSIONS];

    private Matrix3f(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21,
            float m22) {
        matrix[0][0] = m00;
        matrix[0][1] = m01;
        matrix[0][2] = m02;
        matrix[1][0] = m10;
        matrix[1][1] = m11;
        matrix[1][2] = m12;
        matrix[2][0] = m20;
        matrix[2][1] = m21;
        matrix[2][2] = m22;
    }

    private Matrix3f(final float[][] data) {
        // @formatter:off
        this(
            data[0][0], data[0][1], data[0][2],
            data[1][0], data[1][1], data[1][2],
            data[2][0], data[2][1], data[2][2]
        );
        // @formatter:on
    }

    private Matrix3f(final float[] data) {
        // @formatter:off
        this(
            data[0], data[1], data[2],
            data[3], data[4], data[5],
            data[6], data[7], data[8]
        );
        // @formatter:on
    }

    /**
     * Creates a new {@link Matrix3 matrix} with all values set to zero.
     *
     * @return A new zero-value {@link Matrix3 matrix}.
     */
    public static Matrix3 createZeroMatrix() {
        return ZERO_MATRIX;
    }

    /**
     * Creates a new identity {@link Matrix3 matrix}.
     * <p>
     * The identity {@link Matrix3 matrix} has values along the diagonal set to one
     * and all others set to zero.
     *
     * @return A new identity {@link Matrix3 matrix}.
     */
    public static Matrix3 createIdentityMatrix() {
        return IDENTITY_MATRIX;
    }

    /**
     * Creates a new {@link Matrix3 matrix} with the specified values.
     *
     * @param values
     *            An array of at least 9 values, in column-major order.
     * @return A new {@link Matrix3 matrix} with the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 9 elements.
     */
    public static Matrix3 createFrom(final float[] values) {
        return new Matrix3f(values);
    }

    /**
     * Creates a new {@link Matrix3 matrix} with the specified values.
     *
     * @param values
     *            A 3x3 array of values, in column-major order.
     * @return A new {@link Matrix3 matrix} with the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 3 elements per dimension.
     */
    public static Matrix3 createFrom(final float[][] values) {
        return new Matrix3f(values);
    }

    /**
     * Creates a new {@link Matrix3 matrix} from the specified column-{@link Vector3
     * vectors}.
     *
     * @param c0
     *            A {@link Vector3 vector} with values for the first column.
     * @param c1
     *            A {@link Vector3 vector} with values for the second column.
     * @param c2
     *            A {@link Vector3 vector} with values for the third column.
     * @return A new {@link Matrix3 matrix} with the specified values.
     */
    public static Matrix3 createFrom(final Vector3 c0, final Vector3 c1, final Vector3 c2) {
        // @formatter:off
        return new Matrix3f(
            c0.x(), c0.y(), c0.z(),
            c1.x(), c1.y(), c1.z(),
            c2.x(), c2.y(), c2.z()
        );
        // @formatter:on
    }

    /**
     * Creates a new {@link Matrix3 matrix} from the specified {@link Matrix2}
     * instance.
     * <p>
     * Values not present in the input {@link Matrix2 matrix} are set to zero.
     *
     * @param m
     *            The {@link Matrix2 matrix} from which this {@link Matrix3 matrix}
     *            will be built.
     * @return A new {@link Matrix3 matrix} from the specified sub-{@link Matrix3
     *         matrix}.
     */
    public static Matrix3 createFrom(final Matrix2 m) {
        Vector3 c0 = Vector3f.createFrom(m.column(0));
        Vector3 c1 = Vector3f.createFrom(m.column(1));
        Vector3 c2 = Vector3f.createZeroVector();
        return createFrom(c0, c1, c2);
    }

    /**
     * Creates a new inverse {@link Matrix3 matrix} from the specified values, if
     * possible.
     *
     * @param values
     *            An array of at least 9 values, in column-major order.
     * @return A new inverse {@link Matrix3 matrix} calculated from the specified
     *         values.
     * @throws ArithmeticException
     *             If the values represent a singular {@link Matrix3 matrix}. A
     *             singular {@link Matrix3 matrix} is non-invertible because its
     *             determinant is zero.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 9 elements.
     */
    public static Matrix3 createInverseFrom(final float[] values) {
        // @formatter:off
        return createInverseImpl(
            values[0], values[1], values[2],
            values[3], values[4], values[5],
            values[6], values[7], values[8]
        );
        // @formatter:on
    }

    /**
     * Creates a new inverse {@link Matrix3 matrix} from the specified values, if
     * possible.
     *
     * @param values
     *            A 3x3 array of values, in column-major order.
     * @return A new inverse {@link Matrix3 matrix} calculated from the specified
     *         values.
     * @throws ArithmeticException
     *             If the values represent a singular {@link Matrix3 matrix}. A
     *             singular {@link Matrix3 matrix} is non-invertible because its
     *             determinant is zero.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 3 elements per dimension.
     */
    public static Matrix3 createInverseFrom(final float[][] values) {
        // @formatter:off
        return createInverseImpl(
            values[0][0], values[0][1], values[0][2],
            values[1][0], values[1][1], values[1][2],
            values[2][0], values[2][1], values[2][2]
        );
        // @formatter:on
    }

    /**
     * Creates a new inverse {@link Matrix3 matrix} from the specified values, if
     * possible.
     *
     * @param c0
     *            A {@link Vector3 vector} with values for the first column.
     * @param c1
     *            A {@link Vector3 vector} with values for the second column.
     * @param c2
     *            A {@link Vector3 vector} with values for the third column.
     * @return A new inverse {@link Matrix3 matrix} calculated from the specified
     *         column-{@link Vector3 vectors}.
     * @throws RuntimeException
     *             If the values represent a singular {@link Matrix3 matrix}. A
     *             singular {@link Matrix3 matrix} is non-invertible because its
     *             determinant is zero.
     */
    public static Matrix3 createInverseFrom(final Vector3 c0, final Vector3 c1, final Vector3 c2) {
        // @formatter:off
        return createInverseImpl(
            c0.x(), c0.y(), c0.z(),
            c1.x(), c1.y(), c1.z(),
            c2.x(), c2.y(), c2.z()
        );
        // @formatter:on
    }

    /**
     * Creates a new transposed {@link Matrix3 matrix} from the specified values.
     *
     * @param values
     *            An array of at least 9 values, in column-major order.
     * @return A new transposed {@link Matrix3 matrix} calculated from the specified
     *         values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 9 elements.
     */
    public static Matrix3 createTransposeFrom(final float[] values) {
        // @formatter:off
        return new Matrix3f(
            values[0], values[3], values[6],
            values[1], values[4], values[7],
            values[2], values[5], values[8]
        );
        // @formatter:on
    }

    /**
     * Creates a new transposed {@link Matrix3 matrix} from the specified values.
     *
     * @param values
     *            A 3x3 array of values, in column-major order.
     * @return A new transposed {@link Matrix3 matrix} calculated from the specified
     *         values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 3 elements per dimension.
     */
    public static Matrix3 createTransposeFrom(final float[][] values) {
        // @formatter:off
        return new Matrix3f(
            values[0][0], values[1][0], values[2][0],
            values[0][1], values[1][1], values[2][1],
            values[0][2], values[1][2], values[2][2]
        );
        // @formatter:on
    }

    /**
     * Creates a new inverse {@link Matrix3 matrix} from the specified values.
     *
     * @param c0
     *            A {@link Vector3 vector} with values for the first column.
     * @param c1
     *            A {@link Vector3 vector} with values for the second column.
     * @param c2
     *            A {@link Vector3 vector} with values for the third column.
     * @return A new inverse {@link Matrix3 matrix} calculated from the specified
     *         column {@link Vector3 vectors}.
     */
    public static Matrix3 createTransposeFrom(final Vector3 c0, final Vector3 c1, final Vector3 c2) {
        // @formatter:off
        return new Matrix3f(
            c0.x(), c1.x(), c2.x(),
            c0.y(), c1.y(), c2.y(),
            c0.z(), c1.z(), c2.z()
        );
        // @formatter:on
    }

    /**
     * Creates a new {@link Matrix3 matrix} with the specified translation values
     * applied to the identity {@link Matrix3 matrix}.
     *
     * @param tx
     *            A scalar value by which to translate along the x axis.
     * @param ty
     *            A scalar value by which to translate along the y axis.
     * @return A new translation {@link Matrix3 matrix} based on the specified
     *         values.
     */
    public static Matrix3 createTranslationFrom(float tx, float ty) {
        return new Matrix3f(1f, 0f, 0f, 0f, 1f, 0f, tx, ty, 1f);
    }

    /**
     * Creates a new {@link Matrix3 matrix} with the specified translation
     * {@link Vector2 vector} applied to the identity {@link Matrix3 matrix}.
     *
     * @param tv
     *            The {@link Vector2 vector} used to create this translation.
     * @return A new translation {@link Matrix3 matrix} based on the specified
     *         column {@link Vector2 vector}.
     */
    public static Matrix3 createTranslationFrom(final Vector2 tv) {
        return createTranslationFrom(tv.x(), tv.y());
    }

    /**
     * Creates a new scaling {@link Matrix3 matrix} from the specified scalar
     * values.
     *
     * @param sx
     *            A scalar value used to scale the x component in the first column.
     * @param sy
     *            A scalar value used to scale the y component in the second column.
     * @param sz
     *            A scalar value used to scale the z component in the third column.
     * @return A new {@link Matrix3 matrix} that can be used to scale another
     *         {@link Matrix3 matrix} when multiplied together.
     */
    public static Matrix3 createScalingFrom(float sx, float sy, float sz) {
        return new Matrix3f(sx, 0f, 0f, 0f, sy, 0f, 0f, 0f, sz);
    }

    /**
     * Creates a new scaling {@link Matrix3 matrix} from the specified
     * column-{@link Vector3 vector}.
     *
     * @param sv
     *            A {@link Vector3 vector} with scaling values for the x, y, and z
     *            components.
     * @return A new scaling {@link Matrix3 matrix} based on the specified
     *         column-{@link Vector3 vector}.
     */
    public static Matrix3 createScalingFrom(final Vector3 sv) {
        return createScalingFrom(sv.x(), sv.y(), sv.z());
    }

    /**
     * Creates a new {@link Matrix3 matrix} rotated by the specified amount along
     * the specified axis.
     *
     * @param angle
     *            The {@link Angle angle} of rotation to be applied.
     * @param axis
     *            A {@link Vector3 vector} specifying the axis of rotation.
     * @return A new {@link Matrix3 matrix} with the specified rotation applied.
     */
    public static Matrix3 createRotationFrom(final Angle angle, final Vector3 axis) {
        final Vector3 nv = axis.normalize();
        final float cos = MathUtil.cos(angle.valueRadians());
        final float sin = MathUtil.sin(angle.valueRadians());
        final float t = 1.0f - cos;

        final float x = nv.x();
        final float y = nv.y();
        final float z = nv.z();

        final float x2 = x * x;
        final float y2 = y * y;
        final float z2 = z * z;
        final float xy = x * y;
        final float xz = x * z;
        final float yz = y * z;

        final float txy = t * xy;
        final float txz = t * xz;
        final float tyz = t * yz;

        final float xsin = x * sin;
        final float ysin = y * sin;
        final float zsin = z * sin;

        // @formatter:off
        return new Matrix3f(
            t * x2 + cos,  txy + zsin ,  txz - ysin,
             txy - zsin , t * y2 + cos,  tyz + xsin,
             txz + ysin ,  tyz - xsin , t * z2 + cos
        );
        // @formatter:on
    }

    @Override
    public float value(int row, int col) {
        return matrix[col][row];
    }

    @Override
    public Vector3 row(int row) {
        return Vector3f.createFrom(matrix[0][row], matrix[1][row], matrix[2][row]);
    }

    @Override
    public Vector3 column(int col) {
        return Vector3f.createFrom(matrix[col][0], matrix[col][1], matrix[col][2]);
    }

    @Override
    public Matrix3 add(final Matrix3 m) {
        final float m00 = matrix[0][0] + m.value(0, 0);
        final float m01 = matrix[0][1] + m.value(1, 0);
        final float m02 = matrix[0][2] + m.value(2, 0);
        final float m10 = matrix[1][0] + m.value(0, 1);
        final float m11 = matrix[1][1] + m.value(1, 1);
        final float m12 = matrix[1][2] + m.value(2, 1);
        final float m20 = matrix[2][0] + m.value(0, 2);
        final float m21 = matrix[2][1] + m.value(1, 2);
        final float m22 = matrix[2][2] + m.value(2, 2);
        return new Matrix3f(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Override
    public Matrix3 sub(final Matrix3 m) {
        final float m00 = matrix[0][0] - m.value(0, 0);
        final float m01 = matrix[0][1] - m.value(1, 0);
        final float m02 = matrix[0][2] - m.value(2, 0);
        final float m10 = matrix[1][0] - m.value(0, 1);
        final float m11 = matrix[1][1] - m.value(1, 1);
        final float m12 = matrix[1][2] - m.value(2, 1);
        final float m20 = matrix[2][0] - m.value(0, 2);
        final float m21 = matrix[2][1] - m.value(1, 2);
        final float m22 = matrix[2][2] - m.value(2, 2);
        return new Matrix3f(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Override
    public Matrix3 mult(float s) {
        final float m00 = matrix[0][0] * s;
        final float m01 = matrix[0][1] * s;
        final float m02 = matrix[0][2] * s;
        final float m10 = matrix[1][0] * s;
        final float m11 = matrix[1][1] * s;
        final float m12 = matrix[1][2] * s;
        final float m20 = matrix[2][0] * s;
        final float m21 = matrix[2][1] * s;
        final float m22 = matrix[2][2] * s;
        return new Matrix3f(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Override
    public Vector3 mult(final Vector3 v) {
        final float tx = matrix[0][0] * v.x() + matrix[1][0] * v.y() + matrix[2][0] * v.z();
        final float ty = matrix[0][1] * v.x() + matrix[1][1] * v.y() + matrix[2][1] * v.z();
        final float tz = matrix[0][2] * v.x() + matrix[1][2] * v.y() + matrix[2][2] * v.z();
        return Vector3f.createFrom(tx, ty, tz);
    }

    @Override
    public Matrix3 mult(final Matrix3 m) {
        final float m00 = value(0, 0) * m.value(0, 0) + value(0, 1) * m.value(1, 0) + value(0, 2) * m.value(2, 0);
        final float m10 = value(0, 0) * m.value(0, 1) + value(0, 1) * m.value(1, 1) + value(0, 2) * m.value(2, 1);
        final float m20 = value(0, 0) * m.value(0, 2) + value(0, 1) * m.value(1, 2) + value(0, 2) * m.value(2, 2);
        final float m01 = value(1, 0) * m.value(0, 0) + value(1, 1) * m.value(1, 0) + value(1, 2) * m.value(2, 0);
        final float m11 = value(1, 0) * m.value(0, 1) + value(1, 1) * m.value(1, 1) + value(1, 2) * m.value(2, 1);
        final float m21 = value(1, 0) * m.value(0, 2) + value(1, 1) * m.value(1, 2) + value(1, 2) * m.value(2, 2);
        final float m02 = value(2, 0) * m.value(0, 0) + value(2, 1) * m.value(1, 0) + value(2, 2) * m.value(2, 0);
        final float m12 = value(2, 0) * m.value(0, 1) + value(2, 1) * m.value(1, 1) + value(2, 2) * m.value(2, 1);
        final float m22 = value(2, 0) * m.value(0, 2) + value(2, 1) * m.value(1, 2) + value(2, 2) * m.value(2, 2);
        return new Matrix3f(m00, m01, m02, m10, m11, m12, m20, m21, m22);
    }

    @Override
    public Matrix3 rotate(final Angle x, final Angle y, final Angle z) {
        Matrix3 rx = createRotationFrom(x, Vector3f.createUnitVectorX());
        Matrix3 ry = createRotationFrom(y, Vector3f.createUnitVectorY());
        Matrix3 rz = createRotationFrom(z, Vector3f.createUnitVectorZ());
        return mult(rx).mult(ry).mult(rz);
    }

    @Override
    public Matrix3 rotate(final Angle angle, final Vector3 axis) {
        return mult(createRotationFrom(angle, axis));
    }

    @Override
    public Matrix3 translate(float tx, float ty) {
        return mult(createTranslationFrom(tx, ty));
    }

    @Override
    public Matrix3 translate(final Vector2 tv) {
        return translate(tv.x(), tv.y());
    }

    @Override
    public Matrix3 scale(float sx, float sy, float sz) {
        return mult(createScalingFrom(sx, sy, sz));
    }

    @Override
    public Matrix3 scale(final Vector3 v) {
        return scale(v.x(), v.y(), v.z());
    }

    @Override
    public float determinant() {
        // @formatter:off
        return getDeterminant(
            matrix[0][0], matrix[0][1], matrix[0][2],
            matrix[1][0], matrix[1][1], matrix[1][2],
            matrix[2][0], matrix[2][1], matrix[2][2]
        );
        // @formatter:on
    }

    @Override
    public Matrix3 inverse() {
        return createInverseFrom(matrix);
    }

    @Override
    public Matrix3 transpose() {
        return createTransposeFrom(matrix);
    }

    @Override
    public Matrix2 toMatrix2() {
        return Matrix2f.createFrom(matrix);
    }

    @Override
    public Quaternion toQuaternion() {
        final float m00 = matrix[0][0];
        final float m11 = matrix[1][1];
        final float m22 = matrix[2][2];
        final float trace = m00 + m11 + m22;

        float w, x, y, z;
        if (trace > 0.0f) {
            w = MathUtil.sqrt(trace + 1.0f) * 0.5f;
            final float f = 0.25f / w;
            x = (matrix[1][2] - matrix[2][1]) * f;
            y = (matrix[2][0] - matrix[0][2]) * f;
            z = (matrix[0][1] - matrix[1][0]) * f;
        } else if ((m00 > m11) && (m00 > m22)) {
            x = MathUtil.sqrt(m00 - m11 - m22 + 1.0f) * 0.5f;
            final float f = 0.25f / x;
            y = (matrix[0][1] + matrix[1][0]) * f;
            z = (matrix[2][0] + matrix[0][2]) * f;
            w = (matrix[1][2] - matrix[2][1]) * f;
        } else if (m11 > m22) {
            y = MathUtil.sqrt(m11 - m00 - m22 + 1.0f) * 0.5f;
            final float f = 0.25f / y;
            x = (matrix[0][1] + matrix[1][0]) * f;
            z = (matrix[1][2] + matrix[2][1]) * f;
            w = (matrix[2][0] - matrix[0][2]) * f;
        } else {
            z = MathUtil.sqrt(m22 - m00 - m11 + 1.0f) * 0.5f;
            final float f = 0.25f / z;
            x = (matrix[2][0] + matrix[0][2]) * f;
            y = (matrix[1][2] + matrix[2][1]) * f;
            w = (matrix[0][1] - matrix[1][0]) * f;
        }
        return Quaternionf.createNormalizedFrom(w, x, y, z);
    }

    /**
     * The values are ordered as documented {@link Matrix3f here}.
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
        if (!(obj instanceof Matrix3))
            return false;

        Matrix3 other = (Matrix3) obj;
        return MatrixUtil.areEqual(this, other, DIMENSIONS);
    }

    @Override
    public String toString() {
        // TODO: Improve formatting
        StringBuilder fmt = new StringBuilder();
        fmt.append(Matrix3f.class.getSimpleName() + " = [%9.5f | %9.5f | %9.5f]%n");
        fmt.append("           [%9.5f | %9.5f | %9.5f]%n");
        fmt.append("           [%9.5f | %9.5f | %9.5f]");

        // @formatter:off
        return String.format(fmt.toString(),
            matrix[0][0], matrix[1][0], matrix[2][0],
            matrix[0][1], matrix[1][1], matrix[2][1],
            matrix[0][2], matrix[1][2], matrix[2][2]
        );
        // @formatter:on
    }

    private static float[] getAdjugate(float m00, float m01, float m02, float m10, float m11, float m12, float m20,
            float m21, float m22) {
        // To calculate cofactor matrix, create a matrix of minors and, in a
        // single step, apply the alternate negation pattern on the proper
        // matrix elements. See:
        // https://www.khanacademy.org/math/precalculus/precalc-matrices/inverting_matrices/v/inverting-3x3-part-1-calculating-matrix-of-minors-and-cofactor-matrix
        final float c00 = +(m11 * m22 - m12 * m21);
        final float c01 = -(m10 * m22 - m12 * m20);
        final float c02 = +(m10 * m21 - m11 * m20);
        final float c10 = -(m01 * m22 - m02 * m21);
        final float c11 = +(m00 * m22 - m02 * m20);
        final float c12 = -(m00 * m21 - m01 * m20);
        final float c20 = +(m01 * m12 - m02 * m11);
        final float c21 = -(m00 * m12 - m02 * m10);
        final float c22 = +(m00 * m11 - m01 * m10);

        // @formatter:off
        // adjugate(M) = transpose(cofactor(M))
        return new float[] {
            c00, c10, c20,
            c01, c11, c21,
            c02, c12, c22
        };
        // @formatter:on
    }

    private static float getDeterminant(float m00, float m01, float m02, float m10, float m11, float m12, float m20,
            float m21, float m22) {
        // https://www.khanacademy.org/math/precalculus/precalc-matrices/inverting_matrices/v/finding-the-determinant-of-a-3x3-matrix-method-2
        // @formatter:off
        return +(m00 * (m11 * m22 - m21 * m12))
               -(m10 * (m01 * m22 - m21 * m02))
               +(m20 * (m01 * m12 - m11 * m02));
        // @formatter:on
    }

    private static Matrix3 createInverseImpl(float m00, float m01, float m02, float m10, float m11, float m12,
            float m20, float m21, float m22) {
        // @formatter:off
        float det = getDeterminant(
            m00, m01, m02,
            m10, m11, m12,
            m20, m21, m22
        );
        // @formatter:on

        if (FloatUtil.isZero(det))
            throw new ArithmeticException("Matrix determinant is zero: non-invertible matrix");

        // @formatter:off
        Matrix3 adj = createFrom(getAdjugate(
            m00, m01, m02,
            m10, m11, m12,
            m20, m21, m22
        ));
        // @formatter:on

        // inverse(M) = 1/determinant(M) * adjugate(M)
        return adj.mult(1.0f / det);
    }

}
