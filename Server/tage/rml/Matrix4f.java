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
 * An immutable single-precision floating point column-major 4x4 square
 * {@link Matrix4 matrix}.
 * <p>
 * This means the following inputs:
 *
 * <pre>
 * float[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
 * float[][] {
 *     {  1,  2,  3,  4 },
 *     {  5,  6,  7,  8 },
 *     {  9, 10, 11, 12 },
 *     { 13, 14, 15, 16 }
 * };
 * </pre>
 *
 * produce the following matrix layout:
 * <p>
 * 1 5 9 13
 * 2 6 10 14
 * 3 7 11 15
 * 4 8 12 16
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
public final class Matrix4f implements Matrix4 {

    private static final int      DIMENSIONS      = 4;

    // @formatter:off
    private static final Matrix4f ZERO_MATRIX     = new Matrix4f(
        0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f,
        0f, 0f, 0f, 0f
    );
    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f(
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f
    );
    // @formatter:on

    private final float[][]       matrix          = new float[DIMENSIONS][DIMENSIONS];

    private Matrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20,
            float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
        matrix[0][0] = m00;
        matrix[0][1] = m01;
        matrix[0][2] = m02;
        matrix[0][3] = m03;
        matrix[1][0] = m10;
        matrix[1][1] = m11;
        matrix[1][2] = m12;
        matrix[1][3] = m13;
        matrix[2][0] = m20;
        matrix[2][1] = m21;
        matrix[2][2] = m22;
        matrix[2][3] = m23;
        matrix[3][0] = m30;
        matrix[3][1] = m31;
        matrix[3][2] = m32;
        matrix[3][3] = m33;
    }

    private Matrix4f(final float[][] data) {
        // @formatter:off
        this(
            data[0][0], data[0][1], data[0][2], data[0][3],
            data[1][0], data[1][1], data[1][2], data[1][3],
            data[2][0], data[2][1], data[2][2], data[2][3],
            data[3][0], data[3][1], data[3][2], data[3][3]
        );
        // @formatter:on
    }

    private Matrix4f(final float[] data) {
        // @formatter:off
        this(
            data[0] , data[1] , data[2] , data[3] ,
            data[4] , data[5] , data[6] , data[7] ,
            data[8] , data[9] , data[10], data[11],
            data[12], data[13], data[14], data[15]
        );
        // @formatter:on
    }

    /**
     * Creates a new {@link Matrix4 matrix} with all values set to zero.
     *
     * @return A new zero-value {@link Matrix4 matrix}.
     */
    public static Matrix4 createZeroMatrix() {
        return ZERO_MATRIX;
    }

    /**
     * Creates a new {@link Matrix4 matrix} instance of the identity {@link Matrix4
     * matrix}.
     * <p>
     * The identity {@link Matrix4 matrix} has values along the diagonal set to one
     * and all others set to zero.
     *
     * @return A new identity {@link Matrix4 matrix}.
     */
    public static Matrix4 createIdentityMatrix() {
        return IDENTITY_MATRIX;
    }

    /**
     * Creates a new perspective projection {@link Matrix4 matrix}.
     *
     * @param fovy
     *            The {@link Angle angle} specifying the vertical field of view.
     * @param aspect
     *            The width to height aspect ratio.
     * @param near
     *            The near clipping plane. This must be a positive value.
     * @param far
     *            The far clipping plane. This must be a positive value greater than
     *            <code>near</code>.
     * @return A new {@link Matrix4 matrix} with a perspective projection.
     * @throws IllegalArgumentException
     *             If any of the following conditions holds true:
     *             <pre>fovy LTE 0, near LTE 0, far LTE 0, near GTE far, aspect LTE 0</pre>
     */
    public static Matrix4 createPerspectiveMatrix(final Angle fovy, float aspect, float near, float far) {
        float degs = fovy.valueDegrees();
        if (degs <= 0f)
            throw new IllegalArgumentException("Vertical field of view must be > 0");
        if (near <= 0f)
            throw new IllegalArgumentException("Near must be > 0");
        if (far <= 0f)
            throw new IllegalArgumentException("Far must be > 0");
        if (near >= far)
            throw new IllegalArgumentException("Near must be < Far");
        if (aspect <= 0f)
            throw new IllegalArgumentException("Aspect ratio must be > 0");

        float q = (float) (1.0f / Math.tan(Math.toRadians(0.5f * degs)));
        float A = q / aspect;
        float B = (near + far) / (near - far);
        float C = (2.0f * near * far) / (near - far);

        // @formatter:off
        Matrix4 x = new Matrix4f(
            A , 0f, 0f,  0f,
            0f, q , 0f,  0f,
            0f, 0f, B , -1f,
            0f, 0f, C ,  0f
        );
        return x;
        // @formatter:on
    }

    /**
     * Creates a new view {@link Matrix4 matrix}.
     *
     * @param u
     *            The side {@link Vector4 vector} pointing "right", in world-space.
     * @param v
     *            The vertical {@link Vector4 vector} pointing "up", in world-space.
     * @param n
     *            The front {@link Vector4 vector} pointing "forward", in
     *            world-space.
     * @param p
     *            The position {@link Vector4 vector}, in world-space.
     * @return A new {@link Matrix4 matrix} with the viewing transform.
     * @see #createViewMatrix(Vector3, Vector3, Vector3, Vector3)
     */
    public static Matrix4 createViewMatrix(Vector4 u, Vector4 v, Vector4 n, Vector4 p) {
        // @formatter:off
    	
    	Matrix4 objOrient = new Matrix4f(
                u.x(), u.y(), u.z(), 0f,
                v.x(), v.y(), v.z(), 0f,
                -n.x(), -n.y(), -n.z(), 0f,
                p.x(), p.y(), p.z(), 1f
            );
    	Matrix4 view = objOrient.inverse();
    	return (view);
        // @formatter:on
    }

    /**
     * Creates a new view {@link Matrix4 matrix}.
     *
     * @param u
     *            The side {@link Vector3 vector} pointing "right", in world-space.
     * @param v
     *            The vertical {@link Vector3 vector} pointing "up", in world-space.
     * @param n
     *            The front {@link Vector3 vector} pointing "forward", in
     *            world-space.
     * @param p
     *            The position {@link Vector3 vector}, in world-space.
     * @return A new {@link Matrix4 matrix} with the viewing transform.
     * @see #createViewMatrix(Vector4, Vector4, Vector4, Vector4)
     */
    public static Matrix4 createViewMatrix(Vector3 u, Vector3 v, Vector3 n, Vector3 p) {
        // @formatter:off
        return createViewMatrix(
            Vector4f.createFrom(u),
            Vector4f.createFrom(v),
            Vector4f.createFrom(n),
            Vector4f.createFrom(p)
        );
        // @formatter:on
    }

    /**
     * Create a new look-at {@link Matrix4 matrix}.
     *
     * @param eyePosition
     *            A {@link Vector3 vector} specifying the viewer's position, in
     *            world-space.
     * @param targetPosition
     *            A {@link Vector3 vector} specifying the target's position, in
     *            world-space.
     * @param upDirection
     *            A {@link Vector3 vector} specifying the viewer's up axis, in
     *            world-space.
     * @return A new look-at {@link Matrix4 matrix}.
     * @see #createLookAtMatrix(Vector4, Vector4, Vector4)
     */
    public static Matrix4 createLookAtMatrix(Vector3 eyePosition, Vector3 targetPosition, Vector3 upDirection) {
        // @formatter:off
        return createLookAtMatrix(
            Vector4f.createFrom(eyePosition),
            Vector4f.createFrom(targetPosition),
            Vector4f.createFrom(upDirection)
        );
        // @formatter:on
    }

    /**
     * Create a new look-at {@link Matrix4 matrix}.
     *
     * @param eyePosition
     *            A {@link Vector4 vector} specifying the viewer's position, in
     *            world-space.
     * @param targetPosition
     *            A {@link Vector4 vector} specifying the target's position, in
     *            world-space.
     * @param upDirection
     *            A {@link Vector4 vector} specifying the world's up axis, in
     *            world-space.
     * @return A new look-at {@link Matrix4 matrix}.
     * @see #createLookAtMatrix(Vector3, Vector3, Vector3)
     */
    public static Matrix4 createLookAtMatrix(Vector4 eyePosition, Vector4 targetPosition, Vector4 upDirection) {
        // turn camera towards the target to calculate forward vector f,
        // then calculate side vector s and new up vector u (relative to camera)
        // so that all axes are perpendicular to each other
        Vector4 f = targetPosition.sub(eyePosition).normalize();
        Vector4 s = upDirection.cross(f).normalize();
        Vector4 u = f.cross(s).normalize();
        
        // transpose the values
        // @formatter:off
        return new Matrix4f(
                s.x()      ,         s.y()      ,        s.z()      , 0f,
                u.x()      ,         u.y()      ,        u.z()      , 0f,
                f.x()      ,         f.y()      ,        f.z()      , 0f,
                0f, 0f, 0f, 1f);
        // @formatter:on
    }

    /**
     * Create a new look-at {@link Matrix4 matrix}.
     *
     * @param position
     *            A {@link Vector3 vector} with the position of the camera, in
     *            world-space.
     * @param orientation
     *            A {@link Quaternion quaternion} with the orientation of the
     *            camera, in world-space.
     * @return A new look-at {@link Matrix4 matrix}.
     */
    public static Matrix4 createLookAtMatrix(final Vector3 position, final Quaternion orientation) {
        // This code is adapted from the OGRE3D engine

        // Make the translation relative to new axes
        Matrix3 rmT = orientation.toMatrix3().transpose();
        Vector3 xlate = rmT.mult(position).negate();

        return createFrom(rmT, xlate);
    }

    /**
     * Create a new look-at {@link Matrix4 matrix}.
     *
     * @param position
     *            A {@link Vector4 vector} with the position of the camera, in
     *            world-space.
     * @param orientation
     *            A {@link Quaternion quaternion} with the orientation of the
     *            camera, in world-space.
     * @return A new look-at {@link Matrix4 matrix}.
     */
    public static Matrix4 createLookAtMatrix(final Vector4 position, final Quaternion orientation) {
        return createLookAtMatrix(position.toVector3(), orientation);
    }

    /**
     * Creates a new {@link Matrix4 matrix} with the specified values.
     *
     * @param values
     *            An array of at least 16 values, in column-major order.
     * @return A new {@link Matrix4 matrix} with the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 16 elements.
     */
    public static Matrix4 createFrom(final float[] values) {
        return new Matrix4f(values);
    }

    /**
     * Creates a new {@link Matrix4 matrix} with the specified values.
     *
     * @param values
     *            A 4x4 array of values, in column-major order.
     * @return A new {@link Matrix4 matrix} with the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 elements per dimension.
     */
    public static Matrix4 createFrom(final float[][] values) {
        return new Matrix4f(values);
    }

    /**
     * Creates a new {@link Matrix4 matrix} from the specified column {@link Vector3
     * vectors}.
     * <p>
     * The added <code>w</code> component of each {@link Vector3 vector} is set to
     * <code>0</code> by default, except for the last column, which is set to
     * <code>1</code>.
     *
     * @param c0
     *            A {@link Vector3 vector} with values for the first column, with
     *            <code>w = 0</code>.
     * @param c1
     *            A {@link Vector3 vector} with values for the second column, with
     *            <code>w = 0</code>.
     * @param c2
     *            A {@link Vector3 vector} with values for the third column, with
     *            <code>w = 0</code>.
     * @param c3
     *            A {@link Vector3 vector} with values for the fourth column, with
     *            <code>w = 1</code>.
     * @return A new {@link Matrix4 matrix} with the specified values.
     */
    public static Matrix4 createFrom(final Vector3 c0, final Vector3 c1, final Vector3 c2, final Vector3 c3) {
        // @formatter:off
        return new Matrix4f(
            c0.x(), c0.y(), c0.z(), 0f,
            c1.x(), c1.y(), c1.z(), 0f,
            c2.x(), c2.y(), c2.z(), 0f,
            c3.x(), c3.y(), c3.z(), 1f
        );
        // @formatter:on
    }

    /**
     * Creates a new {@link Matrix4 matrix} from the specified column-{@link Vector4
     * vectors}.
     *
     * @param c0
     *            A {@link Vector4 vector} with values for the first column.
     * @param c1
     *            A {@link Vector4 vector} with values for the second column.
     * @param c2
     *            A {@link Vector4 vector} with values for the third column.
     * @param c3
     *            A {@link Vector4 vector} with values for the fourth column.
     * @return A new {@link Matrix4 matrix} with the specified values.
     */
    public static Matrix4 createFrom(final Vector4 c0, final Vector4 c1, final Vector4 c2, final Vector4 c3) {
        // @formatter:off
        return new Matrix4f(
            c0.x(), c0.y(), c0.z(), c0.w(),
            c1.x(), c1.y(), c1.z(), c1.w(),
            c2.x(), c2.y(), c2.z(), c2.w(),
            c3.x(), c3.y(), c3.z(), c3.w()
        );
        // @formatter:on
    }

    /**
     * Creates a new {@link Matrix4 matrix} from the specified {@link Matrix3
     * matrix}.
     *
     * @param m
     *            The {@link Matrix3 matrix} from which this {@link Matrix4 matrix}
     *            will be built.
     * @return A new {@link Matrix4 matrix} from the specified sub-{@link Matrix4
     *         matrix}.
     */
    public static Matrix4 createFrom(final Matrix3 m) {
        return createFrom(m, Vector4f.createFrom(0, 0, 0, 1f));
    }

    /**
     * Creates a new {@link Matrix4 matrix} from the specified {@link Matrix3}
     * instance.
     *
     * @param m
     *            The {@link Matrix3 matrix} from which this {@link Matrix4 matrix}
     *            will be built.
     * @param position
     *            A {@link Vector3 vector} representing the current position.
     * @return A new {@link Matrix4 matrix} from the specified sub-{@link Matrix4
     *         matrix}.
     */
    public static Matrix4 createFrom(final Matrix3 m, final Vector3 position) {
        return createFrom(m, Vector4f.createFrom(position, 1f));
    }

    /**
     * Creates a new {@link Matrix4 matrix} from the specified {@link Matrix3}
     * instance.
     *
     * @param m
     *            The {@link Matrix3 matrix} from which this {@link Matrix4 matrix}
     *            will be built.
     * @param position
     *            A {@link Vector4 vector} representing the current position.
     * @return A new {@link Matrix4 matrix} from the specified sub-{@link Matrix4
     *         matrix}.
     */
    public static Matrix4 createFrom(final Matrix3 m, final Vector4 position) {
        Vector4 c0 = Vector4f.createFrom(m.column(0), 0f);
        Vector4 c1 = Vector4f.createFrom(m.column(1), 0f);
        Vector4 c2 = Vector4f.createFrom(m.column(2), 0f);
        return createFrom(c0, c1, c2, position);
    }

    /**
     * Creates a new inverse {@link Matrix4 matrix} from the specified values, if
     * possible.
     *
     * @param values
     *            An array of at least 16 values, in column-major order.
     * @return A new inverse {@link Matrix4 matrix} calculated from the specified
     *         values.
     * @throws ArithmeticException
     *             If the values represent a singular {@link Matrix4 matrix}. A
     *             singular {@link Matrix4 matrix} is non-invertible because its
     *             determinant is zero.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 16 elements.
     */
    public static Matrix4 createInverseFrom(final float[] values) {
        // @formatter:off
        return createInverseImpl(
            values[0] , values[1] , values[2] , values[3],
            values[4] , values[5] , values[6] , values[7],
            values[8] , values[9] , values[10], values[11],
            values[12], values[13], values[14], values[15]
        );
        // @formatter:on
    }

    /**
     * Creates a new inverse {@link Matrix4 matrix} from the specified values, if
     * possible.
     *
     * @param values
     *            A 4x4 array of values, in column-major order.
     * @return A new inverse {@link Matrix4 matrix} calculated from the specified
     *         values.
     * @throws ArithmeticException
     *             If the values represent a singular {@link Matrix4 matrix}. A
     *             singular {@link Matrix4 matrix} is non-invertible because its
     *             determinant is zero.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 elements per dimension.
     */
    public static Matrix4 createInverseFrom(final float[][] values) {
        // @formatter:off
        return createInverseImpl(
            values[0][0], values[0][1], values[0][2], values[0][3],
            values[1][0], values[1][1], values[1][2], values[1][3],
            values[2][0], values[2][1], values[2][2], values[2][3],
            values[3][0], values[3][1], values[3][2], values[3][3]
        );
        // @formatter:on
    }

    /**
     * Creates a new inverse {@link Matrix4 matrix} from the specified column
     * {@link Vector4 vectors}, if possible.
     *
     * @param c0
     *            A {@link Vector4 vector} with values for the first column.
     * @param c1
     *            A {@link Vector4 vector} with values for the second column.
     * @param c2
     *            A {@link Vector4 vector} with values for the third column.
     * @param c3
     *            A {@link Vector4 vector} with values for the fourth column.
     * @return A new {@link Matrix4 matrix} with the specified values.
     * @throws ArithmeticException
     *             If the values represent a singular {@link Matrix4 matrix}. A
     *             singular {@link Matrix4 matrix} is non-invertible because its
     *             determinant is zero.
     */
    public static Matrix4 createInverseFrom(final Vector4 c0, final Vector4 c1, final Vector4 c2, final Vector4 c3) {
        // @formatter:off
        return createInverseImpl(
            c0.x(), c0.y(), c0.z(), c0.w(),
            c1.x(), c1.y(), c1.z(), c1.w(),
            c2.x(), c2.y(), c2.z(), c2.w(),
            c3.x(), c3.y(), c3.z(), c3.w()
        );
        // @formatter:on
    }

    /**
     * Creates a new transposed {@link Matrix4 matrix} from the specified values.
     *
     * @param values
     *            An array of at least 16 values, in column-major order.
     * @return A new transposed {@link Matrix4 matrix} calculated from the specified
     *         values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 16 elements.
     */
    public static Matrix4 createTransposeFrom(final float[] values) {
        // @formatter:off
        return new Matrix4f(
            values[0], values[4], values[8] , values[12],
            values[1], values[5], values[9] , values[13],
            values[2], values[6], values[10], values[14],
            values[3], values[7], values[11], values[15]
        );
        // @formatter:on
    }

    /**
     * Creates a new transposed {@link Matrix4 matrix} from the specified values.
     *
     * @param values
     *            A 4x4 array of values, in column-major order.
     * @return A new transposed {@link Matrix4 matrix} calculated from the specified
     *         values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 elements per dimension.
     */
    public static Matrix4 createTransposeFrom(final float[][] values) {
        // @formatter:off
        return new Matrix4f(
            values[0][0], values[1][0], values[2][0], values[3][0],
            values[0][1], values[1][1], values[2][1], values[3][1],
            values[0][2], values[1][2], values[2][2], values[3][2],
            values[0][3], values[1][3], values[2][3], values[3][3]
        );
        // @formatter:on
    }

    /**
     * Creates a new transposed {@link Matrix4 matrix} from the specified column
     * {@link Vector4 vectors}.
     *
     * @param c0
     *            A {@link Vector4 vector} with values for the first column.
     * @param c1
     *            A {@link Vector4 vector} with values for the second column.
     * @param c2
     *            A {@link Vector4 vector} with values for the third column.
     * @param c3
     *            A {@link Vector4 vector} with values for the fourth column.
     * @return A new {@link Matrix4 matrix} with the specified values transposed.
     */
    public static Matrix4 createTransposeFrom(final Vector4 c0, final Vector4 c1, final Vector4 c2, final Vector4 c3) {
        // @formatter:off
        return new Matrix4f(
            c0.x(), c1.x(), c2.x(), c3.x(),
            c0.y(), c1.y(), c2.y(), c3.y(),
            c0.z(), c1.z(), c2.z(), c3.z(),
            c0.w(), c1.w(), c2.w(), c3.w()
        );
        // @formatter:on
    }

    /**
     * Creates a new {@link Matrix4 matrix} instance with the specified translation
     * values applied to the identity {@link Matrix4 matrix}.
     *
     * @param tx
     *            A scalar value by which to translate along the x-axis.
     * @param ty
     *            A scalar value by which to translate along the y-axis.
     * @param tz
     *            A scalar value by which to translate along the z-axis.
     * @return A new translation {@link Matrix4 matrix} based on the specified
     *         values.
     */
    public static Matrix4 createTranslationFrom(float tx, float ty, float tz) {
        // @formatter:off
        return new Matrix4f(
            1f, 0f, 0f, 0f,
            0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f,
            tx, ty, tz, 1f
        );
        // @formatter:on
    }

    /**
     * Creates a new {@link Matrix4 matrix} instance with the specified translation
     * {@link Vector3 vector} applied to the identity {@link Matrix4 matrix}.
     *
     * @param tv
     *            The {@link Vector3 vector} instance used to create this
     *            translation.
     * @return A new translation {@link Matrix4 matrix} based on the specified
     *         column {@link Vector3 vector}.
     */
    public static Matrix4 createTranslationFrom(final Vector3 tv) {
        return createTranslationFrom(tv.x(), tv.y(), tv.z());
    }

    /**
     * Creates a new {@link Matrix4 matrix} instance with the specified translation
     * {@link Vector4 vector} applied to the identity {@link Matrix4 matrix}.
     *
     * @param tv
     *            The {@link Vector4 vector} used to create this translation.
     * @return A new translation {@link Matrix4 matrix} based on the specified
     *         column {@link Vector4 vector}.
     */
    public static Matrix4 createTranslationFrom(final Vector4 tv) {
        return createTranslationFrom(tv.x(), tv.y(), tv.z());
    }

    /**
     * Creates a new scaling {@link Matrix4 matrix} from the specified scalar
     * values.
     *
     * @param sx
     *            A scalar value used to scale the x component in the first column.
     * @param sy
     *            A scalar value used to scale the y component in the second column.
     * @param sz
     *            A scalar value used to scale the z component in the third column.
     * @return A new {@link Matrix4 matrix} that can be used to scale another
     *         {@link Matrix4 matrix} when multiplied together.
     */
    public static Matrix4 createScalingFrom(float sx, float sy, float sz) {
        // @formatter:off
        return new Matrix4f(
            sx, 0f, 0f, 0f,
            0f, sy, 0f, 0f,
            0f, 0f, sz, 0f,
            0f, 0f, 0f, 1f
        );
        // @formatter:on
    }

    /**
     * Creates a new scaling {@link Matrix4 matrix} from the specified
     * column-{@link Vector3 vector}.
     *
     * @param sv
     *            A {@link Vector3 vector} with scaling values for the x, y, and z
     *            components.
     * @return A new scaling {@link Matrix4 matrix} based on the specified
     *         column-{@link Vector3 vector}.
     */
    public static Matrix4 createScalingFrom(final Vector3 sv) {
        return createScalingFrom(sv.x(), sv.y(), sv.z());
    }

    /**
     * Creates a new scaling {@link Matrix4 matrix} from the specified
     * column-{@link Vector4 vector}.
     *
     * @param sv
     *            A {@link Vector4 vector} with scaling values for the x, y, and z
     *            components.
     * @return A new scaling {@link Matrix4 matrix} based on the specified
     *         column-{@link Vector4 vector}.
     */
    public static Matrix4 createScalingFrom(final Vector4 sv) {
        return createScalingFrom(sv.x(), sv.y(), sv.z());
    }

    /**
     * Creates a new {@link Matrix4 matrix} rotated by the specified angle along the
     * specified {@link Vector3 axis}.
     *
     * @param angle
     *            The {@link Angle angle} of rotation to be applied.
     * @param axis
     *            A {@link Vector3 vector} specifying the axis of rotation.
     * @return A new {@link Matrix4 matrix} with the specified rotation applied.
     */
    public static Matrix4 createRotationFrom(final Angle angle, final Vector3 axis) {
        final Matrix3 rm = Matrix3f.createRotationFrom(angle, axis);
        return createFrom(rm, Vector4f.createFrom(0, 0, 0, 1f));
    }

    /**
     * Creates a new {@link Matrix4 matrix} rotated by the specified amount along
     * the specified {@link Vector4 axis}.
     *
     * @param angle
     *            The {@link Angle angle} of rotation to be applied.
     * @param axis
     *            A {@link Vector4 vector} specifying the axis of rotation.
     * @return A new {@link Matrix4 matrix} with the specified rotation applied.
     */
    public static Matrix4 createRotationFrom(final Angle angle, final Vector4 axis) {
        return createRotationFrom(angle, axis.toVector3());
    }

    @Override
    public float value(int row, int col) {
        return matrix[col][row];
    }

    @Override
    public Vector4 row(int row) {
        // @formatter:off
        return Vector4f.createFrom(
            matrix[0][row],
            matrix[1][row],
            matrix[2][row],
            matrix[3][row]
        );
        // @formatter:on
    }

    @Override
    public Vector4 column(int col) {
        // @formatter:off
        return Vector4f.createFrom(
            matrix[col][0],
            matrix[col][1],
            matrix[col][2],
            matrix[col][3]
        );
        // @formatter:on
    }

    @Override
    public Matrix4 add(final Matrix4 m) {
        final float m00 = matrix[0][0] + m.value(0, 0);
        final float m01 = matrix[0][1] + m.value(1, 0);
        final float m02 = matrix[0][2] + m.value(2, 0);
        final float m03 = matrix[0][3] + m.value(3, 0);
        final float m10 = matrix[1][0] + m.value(0, 1);
        final float m11 = matrix[1][1] + m.value(1, 1);
        final float m12 = matrix[1][2] + m.value(2, 1);
        final float m13 = matrix[1][3] + m.value(3, 1);
        final float m20 = matrix[2][0] + m.value(0, 2);
        final float m21 = matrix[2][1] + m.value(1, 2);
        final float m22 = matrix[2][2] + m.value(2, 2);
        final float m23 = matrix[2][3] + m.value(3, 2);
        final float m30 = matrix[3][0] + m.value(0, 3);
        final float m31 = matrix[3][1] + m.value(1, 3);
        final float m32 = matrix[3][2] + m.value(2, 3);
        final float m33 = matrix[3][3] + m.value(3, 3);
        return new Matrix4f(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    @Override
    public Matrix4 sub(final Matrix4 m) {
        final float m00 = matrix[0][0] - m.value(0, 0);
        final float m01 = matrix[0][1] - m.value(1, 0);
        final float m02 = matrix[0][2] - m.value(2, 0);
        final float m03 = matrix[0][3] - m.value(3, 0);
        final float m10 = matrix[1][0] - m.value(0, 1);
        final float m11 = matrix[1][1] - m.value(1, 1);
        final float m12 = matrix[1][2] - m.value(2, 1);
        final float m13 = matrix[1][3] - m.value(3, 1);
        final float m20 = matrix[2][0] - m.value(0, 2);
        final float m21 = matrix[2][1] - m.value(1, 2);
        final float m22 = matrix[2][2] - m.value(2, 2);
        final float m23 = matrix[2][3] - m.value(3, 2);
        final float m30 = matrix[3][0] - m.value(0, 3);
        final float m31 = matrix[3][1] - m.value(1, 3);
        final float m32 = matrix[3][2] - m.value(2, 3);
        final float m33 = matrix[3][3] - m.value(3, 3);
        return new Matrix4f(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    @Override
    public Matrix4 mult(float s) {
        final float m00 = matrix[0][0] * s;
        final float m01 = matrix[0][1] * s;
        final float m02 = matrix[0][2] * s;
        final float m03 = matrix[0][3] * s;
        final float m10 = matrix[1][0] * s;
        final float m11 = matrix[1][1] * s;
        final float m12 = matrix[1][2] * s;
        final float m13 = matrix[1][3] * s;
        final float m20 = matrix[2][0] * s;
        final float m21 = matrix[2][1] * s;
        final float m22 = matrix[2][2] * s;
        final float m23 = matrix[2][3] * s;
        final float m30 = matrix[3][0] * s;
        final float m31 = matrix[3][1] * s;
        final float m32 = matrix[3][2] * s;
        final float m33 = matrix[3][3] * s;
        return new Matrix4f(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    @Override
    public Vector4 mult(final Vector4 v) {
        final float tx = matrix[0][0] * v.x() + matrix[1][0] * v.y() + matrix[2][0] * v.z() + matrix[3][0] * v.w();
        final float ty = matrix[0][1] * v.x() + matrix[1][1] * v.y() + matrix[2][1] * v.z() + matrix[3][1] * v.w();
        final float tz = matrix[0][2] * v.x() + matrix[1][2] * v.y() + matrix[2][2] * v.z() + matrix[3][2] * v.w();
        final float tw = matrix[0][3] * v.x() + matrix[1][3] * v.y() + matrix[2][3] * v.z() + matrix[3][3] * v.w();
        return Vector4f.createFrom(tx, ty, tz, tw);
    }

    @Override
    public Matrix4 mult(final Matrix4 m) {
        // @formatter:off
        final float m00 = value(0, 0) * m.value(0, 0) + value(0, 1) * m.value(1, 0)
                        + value(0, 2) * m.value(2, 0) + value(0, 3) * m.value(3, 0);
        final float m10 = value(0, 0) * m.value(0, 1) + value(0, 1) * m.value(1, 1)
                        + value(0, 2) * m.value(2, 1) + value(0, 3) * m.value(3, 1);
        final float m20 = value(0, 0) * m.value(0, 2) + value(0, 1) * m.value(1, 2)
                        + value(0, 2) * m.value(2, 2) + value(0, 3) * m.value(3, 2);
        final float m30 = value(0, 0) * m.value(0, 3) + value(0, 1) * m.value(1, 3)
                        + value(0, 2) * m.value(2, 3) + value(0, 3) * m.value(3, 3);
        final float m01 = value(1, 0) * m.value(0, 0) + value(1, 1) * m.value(1, 0)
                        + value(1, 2) * m.value(2, 0) + value(1, 3) * m.value(3, 0);
        final float m11 = value(1, 0) * m.value(0, 1) + value(1, 1) * m.value(1, 1)
                        + value(1, 2) * m.value(2, 1) + value(1, 3) * m.value(3, 1);
        final float m21 = value(1, 0) * m.value(0, 2) + value(1, 1) * m.value(1, 2)
                        + value(1, 2) * m.value(2, 2) + value(1, 3) * m.value(3, 2);
        final float m31 = value(1, 0) * m.value(0, 3) + value(1, 1) * m.value(1, 3)
                        + value(1, 2) * m.value(2, 3) + value(1, 3) * m.value(3, 3);
        final float m02 = value(2, 0) * m.value(0, 0) + value(2, 1) * m.value(1, 0)
                        + value(2, 2) * m.value(2, 0) + value(2, 3) * m.value(3, 0);
        final float m12 = value(2, 0) * m.value(0, 1) + value(2, 1) * m.value(1, 1)
                        + value(2, 2) * m.value(2, 1) + value(2, 3) * m.value(3, 1);
        final float m22 = value(2, 0) * m.value(0, 2) + value(2, 1) * m.value(1, 2)
                        + value(2, 2) * m.value(2, 2) + value(2, 3) * m.value(3, 2);
        final float m32 = value(2, 0) * m.value(0, 3) + value(2, 1) * m.value(1, 3)
                        + value(2, 2) * m.value(2, 3) + value(2, 3) * m.value(3, 3);
        final float m03 = value(3, 0) * m.value(0, 0) + value(3, 1) * m.value(1, 0)
                        + value(3, 2) * m.value(2, 0) + value(3, 3) * m.value(3, 0);
        final float m13 = value(3, 0) * m.value(0, 1) + value(3, 1) * m.value(1, 1)
                        + value(3, 2) * m.value(2, 1) + value(3, 3) * m.value(3, 1);
        final float m23 = value(3, 0) * m.value(0, 2) + value(3, 1) * m.value(1, 2)
                        + value(3, 2) * m.value(2, 2) + value(3, 3) * m.value(3, 2);
        final float m33 = value(3, 0) * m.value(0, 3) + value(3, 1) * m.value(1, 3)
                        + value(3, 2) * m.value(2, 3) + value(3, 3) * m.value(3, 3);
        // @formatter:on
        return new Matrix4f(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    @Override
    public Matrix4 rotate(final Angle x, final Angle y, final Angle z) {
        Matrix4 rx = createRotationFrom(x, Vector4f.createUnitVectorX());
        Matrix4 ry = createRotationFrom(y, Vector4f.createUnitVectorY());
        Matrix4 rz = createRotationFrom(z, Vector4f.createUnitVectorZ());
        return mult(rx).mult(ry).mult(rz);
    }

    @Override
    public Matrix4 rotate(final Angle angle, final Vector3 axis) {
        return mult(createRotationFrom(angle, axis));
    }

    @Override
    public Matrix4 rotate(final Angle angle, final Vector4 axis) {
        return mult(createRotationFrom(angle, axis));
    }

    @Override
    public Matrix4 translate(float tx, float ty, float tz) {
        return mult(createTranslationFrom(tx, ty, tz));
    }

    @Override
    public Matrix4 translate(final Vector3 tv) {
        return translate(tv.x(), tv.y(), tv.z());
    }

    @Override
    public Matrix4 translate(final Vector4 tv) {
        return translate(tv.x(), tv.y(), tv.z());
    }

    @Override
    public Matrix4 scale(float sx, float sy, float sz) {
        return mult(createScalingFrom(sx, sy, sz));
    }

    @Override
    public Matrix4 scale(final Vector3 v) {
        return scale(v.x(), v.y(), v.z());
    }

    @Override
    public Matrix4 scale(final Vector4 v) {
        return scale(v.x(), v.y(), v.z());
    }

    @Override
    public float determinant() {
        // @formatter:off
        return getDeterminant(
            matrix[0][0], matrix[0][1], matrix[0][2], matrix[0][3],
            matrix[1][0], matrix[1][1], matrix[1][2], matrix[1][3],
            matrix[2][0], matrix[2][1], matrix[2][2], matrix[2][3],
            matrix[3][0], matrix[3][1], matrix[3][2], matrix[3][3]
        );
        // @formatter:on
    }

    @Override
    public Matrix4 inverse() {
        return createInverseFrom(matrix);
    }

    @Override
    public Matrix4 transpose() {
        return createTransposeFrom(matrix);
    }

    @Override
    public Matrix3 toMatrix3() {
        return Matrix3f.createFrom(matrix);
    }

    @Override
    public Quaternion toQuaternion() {
        return toMatrix3().toQuaternion();
    }

    /**
     * The values are ordered as documented {@link Matrix4f here}.
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
        if (!(obj instanceof Matrix4))
            return false;

        Matrix4 other = (Matrix4) obj;
        return MatrixUtil.areEqual(this, other, DIMENSIONS);
    }

    @Override
    public String toString() {
        // TODO: Improve formatting
        StringBuilder fmt = new StringBuilder();
        fmt.append(Matrix4f.class.getSimpleName() + " = [%9.5f | %9.5f | %9.5f | %9.5f]%n");
        fmt.append("           [%9.5f | %9.5f | %9.5f | %9.5f]%n");
        fmt.append("           [%9.5f | %9.5f | %9.5f | %9.5f]%n");
        fmt.append("           [%9.5f | %9.5f | %9.5f | %9.5f]");

        // @formatter:off
        return String.format(fmt.toString(),
            matrix[0][0], matrix[1][0], matrix[2][0], matrix[3][0],
            matrix[0][1], matrix[1][1], matrix[2][1], matrix[3][1],
            matrix[0][2], matrix[1][2], matrix[2][2], matrix[3][2],
            matrix[0][3], matrix[1][3], matrix[2][3], matrix[3][3]
        );
        // @formatter:on
    }

    private static float[] getAdjugate(float m00, float m01, float m02, float m03, float m10, float m11, float m12,
            float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {

        float a00 = m11 * (m22 * m33 - m32 * m23) + m21 * (m32 * m13 - m12 * m33) + m31 * (m12 * m23 - m22 * m13);
        float a01 = m12 * (m20 * m33 - m30 * m23) + m22 * (m30 * m13 - m10 * m33) + m32 * (m10 * m23 - m20 * m13);
        float a02 = m13 * (m20 * m31 - m30 * m21) + m23 * (m30 * m11 - m10 * m31) + m33 * (m10 * m21 - m20 * m11);
        float a03 = m10 * (m31 * m22 - m21 * m32) + m20 * (m11 * m32 - m31 * m12) + m30 * (m21 * m12 - m11 * m22);

        float a10 = m21 * (m02 * m33 - m32 * m03) + m31 * (m22 * m03 - m02 * m23) + m01 * (m32 * m23 - m22 * m33);
        float a11 = m22 * (m00 * m33 - m30 * m03) + m32 * (m20 * m03 - m00 * m23) + m02 * (m30 * m23 - m20 * m33);
        float a12 = m23 * (m00 * m31 - m30 * m01) + m33 * (m20 * m01 - m00 * m21) + m03 * (m30 * m21 - m20 * m31);
        float a13 = m20 * (m31 * m02 - m01 * m32) + m30 * (m01 * m22 - m21 * m02) + m00 * (m21 * m32 - m31 * m22);

        float a20 = m31 * (m02 * m13 - m12 * m03) + m01 * (m12 * m33 - m32 * m13) + m11 * (m32 * m03 - m02 * m33);
        float a21 = m32 * (m00 * m13 - m10 * m03) + m02 * (m10 * m33 - m30 * m13) + m12 * (m30 * m03 - m00 * m33);
        float a22 = m33 * (m00 * m11 - m10 * m01) + m03 * (m10 * m31 - m30 * m11) + m13 * (m30 * m01 - m00 * m31);
        float a23 = m30 * (m11 * m02 - m01 * m12) + m00 * (m31 * m12 - m11 * m32) + m10 * (m01 * m32 - m31 * m02);

        float a30 = m01 * (m22 * m13 - m12 * m23) + m11 * (m02 * m23 - m22 * m03) + m21 * (m12 * m03 - m02 * m13);
        float a31 = m02 * (m20 * m13 - m10 * m23) + m12 * (m00 * m23 - m20 * m03) + m22 * (m10 * m03 - m00 * m13);
        float a32 = m03 * (m20 * m11 - m10 * m21) + m13 * (m00 * m21 - m20 * m01) + m23 * (m10 * m01 - m00 * m11);
        float a33 = m00 * (m11 * m22 - m21 * m12) + m10 * (m21 * m02 - m01 * m22) + m20 * (m01 * m12 - m11 * m02);

        // @formatter:off
        // adjugate(M) = transpose(cofactor(M)); the array is already transposed
        // Ref: https://www.mathsisfun.com/algebra/matrix-inverse-minors-cofactors-adjugate.html
        return new float[] {
            a00, a10, a20, a30,
            a01, a11, a21, a31,
            a02, a12, a22, a32,
            a03, a13, a23, a33
        };
        // @formatter:on
    }

    private static float getDeterminant(float m00, float m01, float m02, float m03, float m10, float m11, float m12,
            float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {

        // @formatter:off
        float a = m00 * ( m11 * m22 * m33 + m21 * m32 * m13 + m31 * m12 * m23
                        - m31 * m22 * m13 - m11 * m32 * m23 - m21 * m12 * m33);
        float b = m10 * ( m01 * m22 * m33 + m21 * m32 * m03 + m31 * m02 * m23
                        - m31 * m22 * m03 - m01 * m32 * m23 - m21 * m02 * m33);
        float c = m20 * ( m01 * m12 * m33 + m11 * m32 * m03 + m31 * m02 * m13
                        - m31 * m12 * m03 - m01 * m32 * m13 - m11 * m02 * m33);
        float d = m30 * ( m01 * m12 * m23 + m11 * m22 * m03 + m21 * m02 * m13
                        - m21 * m12 * m03 - m01 * m22 * m13 - m11 * m02 * m23);
        // @formatter:on

        return a - b + c - d;
    }

    private static Matrix4 createInverseImpl(float m00, float m01, float m02, float m03, float m10, float m11,
            float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32,
            float m33) {

        // @formatter:off
        float det = getDeterminant(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        );
        // @formatter:on

        if (FloatUtil.isZero(det))
            throw new ArithmeticException("Matrix determinant is zero: non-invertible matrix");

        // @formatter:off
        // adjugate(M) = transpose(cofactor(M))
        Matrix4 adj = createFrom(getAdjugate(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        ));
        // @formatter:on

        // inverse(M) = 1/determinant(M) * adjugate(M)
        return adj.mult(1.0f / det);
    }
}
