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
 * An immutable single-precision floating point 4-component column
 * {@link Vector4 vector}. This {@link Vector4 vector} is useful for
 * transformations where <i>homogeneous coordinates</i> are needed.
 * <p>
 * The homogeneous component <code>w</code> is set to <code>1</code> by default,
 * unless explicitly specified otherwise, and is not taken into account during
 * arithmetic operations. The homogeneous component is present to make
 * operations with transform {@link Matrix4 matrices} possible.
 * <p>
 * This means the input <code>float[] { 1, 2, 3, 4 };</code> produces the
 * following layout:
 * <pre>
 * 1
 * 2
 * 3
 * 4
 * </pre>
 * <p>
 * The implementation assumes the input arrays have enough elements to build the
 * vector. When the input array does not have enough elements, an
 * {@link IndexOutOfBoundsException} will be thrown. However, when the input
 * array has extra elements, the extra data are ignored.
 *
 * @author Raymond L. Rivera
 *
 */
public final class Vector4f implements Vector4 {

    private final static float   DEFAULT_W   = 1f;

    private final static Vector4 ZERO_VECTOR = new Vector4f(0f, 0f, 0f);
    private final static Vector4 UNIT_X      = new Vector4f(1f, 0f, 0f);
    private final static Vector4 UNIT_Y      = new Vector4f(0f, 1f, 0f);
    private final static Vector4 UNIT_Z      = new Vector4f(0f, 0f, 1f);

    private final float          x;
    private final float          y;
    private final float          z;
    private final float          w;

    private Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    private Vector4f(float x, float y, float z) {
        this(x, y, z, DEFAULT_W);
    }

    private Vector4f(final float[] values) {
        this(values[0], values[1], values[2], values[3]);
    }

    /**
     * Creates a new {@link Vector4 vector} with x, y, z components set to zero.
     *
     * @return A new zero-length {@link Vector4 vector}.
     */
    public static Vector4 createZeroVector() {
        return ZERO_VECTOR;
    }

    /**
     * Creates a new unit-length {@link Vector4 vector} along the x axis.
     *
     * @return A new unit-length {@link Vector4 vector}.
     */
    public static Vector4 createUnitVectorX() {
        return UNIT_X;
    }

    /**
     * Creates a new unit-length {@link Vector4 vector} along the y axis.
     *
     * @return A new unit-length {@link Vector4 vector}.
     */
    public static Vector4 createUnitVectorY() {
        return UNIT_Y;
    }

    /**
     * Creates a new unit-length {@link Vector4 vector} along the z axis.
     *
     * @return A new unit-length {@link Vector4 vector}.
     */
    public static Vector4 createUnitVectorZ() {
        return UNIT_Z;
    }

    /**
     * Creates a new {@link Vector4 vector} with the specified values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @param z
     *            The value for the z component.
     * @return A new {@link Vector4 vector} with the specified values for the x, y,
     *         and z components. The unspecified w component defaults to one.
     */
    public static Vector4 createFrom(float x, float y, float z) {
        return new Vector4f(x, y, z);
    }

    /**
     * Creates a new {@link Vector4 vector} with the specified values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @param z
     *            The value for the z component.
     * @param w
     *            The value for the w component.
     * @return A new {@link Vector4 vector} with the specified values.
     */
    public static Vector4 createFrom(float x, float y, float z, float w) {
        return new Vector4f(x, y, z, w);
    }

    /**
     * Creates a new {@link Vector4 vector} with the specified values.
     *
     * @param values
     *            An array with the values for the x, y, z, and w components in the
     *            first, second, third, and fourth positions respectively.
     * @return A new {@link Vector4 vector} with the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 elements.
     */
    public static Vector4 createFrom(final float[] values) {
        return new Vector4f(values);
    }

    /**
     * Creates a new {@link Vector4 vector} with the specified values.
     *
     * @param v3
     *            A {@link Vector3 vector} with values for the x, y, and z
     *            components.
     * @return A new {@link Vector4 vector} with the specified values of the input
     *         {@link Vector4 vector} for the x, y, and z components. The
     *         unspecified w component defaults to one.
     */
    public static Vector4 createFrom(final Vector3 v3) {
        return new Vector4f(v3.x(), v3.y(), v3.z());
    }

    /**
     * Creates a new {@link Vector4 vector} with the specified values.
     *
     * @param v3
     *            A {@link Vector3 vector} with values for the x, y, and z
     *            components.
     * @param w
     *            The value for the w component.
     * @return A new {@link Vector4 vector} with the specified values.
     */
    public static Vector4 createFrom(final Vector3 v3, float w) {
        return new Vector4f(v3.x(), v3.y(), v3.z(), w);
    }

    /**
     * Creates a new normalized {@link Vector4 vector} with the specified values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @param z
     *            The value for the z component.
     * @return A new normal {@link Vector4 vector} with the specified values for the
     *         x, y, and z components and a length of 1. The unspecified w component
     *         defaults to one.
     * @throws ArithmeticException
     *             If the length of the {@link Vector4 vector} is zero.
     */
    public static Vector4 createNormalizedFrom(float x, float y, float z) {
        return createNormalizedFrom(x, y, z, DEFAULT_W);
    }

    /**
     * Creates a new normalized {@link Vector4 vector} with the specified values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @param z
     *            The value for the z component.
     * @param w
     *            The value for the w component.
     * @return A new normal {@link Vector4 vector} with the specified values and a
     *         length of 1.
     * @throws ArithmeticException
     *             If the length of the {@link Vector4 vector} is zero.
     */
    public static Vector4 createNormalizedFrom(float x, float y, float z, float w) {
        final float sqlen = MathUtil.lengthSquared(new float[] { x, y, z });
        if (FloatUtil.isZero(sqlen))
            throw new ArithmeticException("Cannot normalize zero-length vector");

        final float oneOverLen = MathUtil.invSqrt(sqlen);
        return new Vector4f(x * oneOverLen, y * oneOverLen, z * oneOverLen, w);
    }

    /**
     * Creates a new normalized {@link Vector4 vector} with the specified values.
     *
     * @param values
     *            An array with the values for the x, y, z, and w components in the
     *            first, second, third, and fourth positions respectively.
     * @return A new normal {@link Vector4 vector} with the specified values and a
     *         length of 1.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 elements.
     * @throws ArithmeticException
     *             If the length of the {@link Vector4 vector} is zero.
     */
    public static Vector4 createNormalizedFrom(final float[] values) {
        return createNormalizedFrom(values[0], values[1], values[2], values[3]);
    }

    /**
     * Creates a new normalized {@link Vector4 vector} with the specified values.
     *
     * @param v3
     *            A {@link Vector3 vector} with values for the x, y, and z
     *            components.
     * @return A new normal {@link Vector4 vector} with the specified values of the
     *         input {@link Vector4 vector} for the x, y, and z components and a
     *         length of 1. The unspecified w component defaults to one.
     * @throws ArithmeticException
     *             If the length of the {@link Vector4 vector} is zero.
     */
    public static Vector4 createNormalizedFrom(final Vector3 v3) {
        return createNormalizedFrom(v3.x(), v3.y(), v3.z(), DEFAULT_W);
    }

    /**
     * Creates a new normalized {@link Vector4 vector} with the specified values.
     *
     * @param v3
     *            A {@link Vector3 vector} with values for the x, y, and z
     *            components.
     * @param w
     *            The value for the w component.
     * @return A new {@link Vector4 vector} with the specified values and a length
     *         of 1.
     * @throws ArithmeticException
     *             If the length of the {@link Vector4 vector} is zero.
     */
    public static Vector4 createNormalizedFrom(final Vector3 v3, float w) {
        return createNormalizedFrom(v3.x(), v3.y(), v3.z(), w);
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public float z() {
        return z;
    }

    @Override
    public float w() {
        return w;
    }

    @Override
    public Vector4 add(final Vector4 v) {
        return new Vector4f(x + v.x(), y + v.y(), z + v.z());
    }

    @Override
    public Vector4 add(float x, float y, float z) {
        return new Vector4f(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public Vector4 sub(final Vector4 v) {
        return new Vector4f(x - v.x(), y - v.y(), z - v.z());
    }

    @Override
    public Vector4 sub(float x, float y, float z) {
        return new Vector4f(this.x - x, this.y - y, this.z - z);
    }

    @Override
    public Vector4 mult(final Vector4 v) {
        return new Vector4f(x * v.x(), y * v.y(), z * v.z());
    }

    @Override
    public Vector4 div(final Vector4 v) {
        return new Vector4f(x / v.x(), y / v.y(), z / v.z());
    }

    @Override
    public Vector4 mult(float value) {
        return new Vector4f(x * value, y * value, z * value);
    }

    @Override
    public Vector4 div(float value) {
        value = 1f / value;
        return new Vector4f(x * value, y * value, z * value);
    }

    @Override
    public float dot(final Vector4 v) {
        return x * v.x() + y * v.y() + z * v.z();
    }

    @Override
    public Vector4 cross(final Vector4 v) {
        return new Vector4f(y * v.z() - z * v.y(), z * v.x() - x * v.z(), x * v.y() - y * v.x());
    }

    @Override
    public Vector4 normalize() {
        return createNormalizedFrom(x, y, z);
    }

    @Override
    public Vector4 rotate(Angle angle, Vector4 axis) {
        return Matrix4f.createRotationFrom(angle, axis).mult(this);
    }

    @Override
    public float length() {
        return MathUtil.length(new float[] { x, y, z });
    }

    @Override
    public float lengthSquared() {
        return MathUtil.lengthSquared(new float[] { x, y, z });
    }

    @Override
    public boolean isZeroLength() {
        return FloatUtil.isZero(lengthSquared(), FloatUtil.EPSILON);
    }

    /**
     * The values are in the following order: <code>[x, y, z, w]</code>.
     *
     * @see Bufferable#toFloatArray()
     */
    @Override
    public float[] toFloatArray() {
        return new float[] { x, y, z, w };
    }

    @Override
    public Vector4 lerp(Vector4 end, float t) {
        // t in [0, 1]
        t = MathUtil.clamp(t, 0f, 1f);

        // it's likely less overhead to let the method interpolate the
        // w-component, than it is to slice and create several array objects
        // just to avoid 2 multiplications and 1 addition, so we just let it
        // happen and then ignore the interpolated w-coordinate
        final float[] vm = MathUtil.lerp(toFloatArray(), end.toFloatArray(), t);
        return new Vector4f(vm[0], vm[1], vm[2], DEFAULT_W);
    }

    @Override
    public Vector4 negate() {
        // do not negate the homogeneous coordinate
        return createFrom(-x, -y, -z, w);
    }

    @Override
    public int compareTo(final Vector4 v) {
        return FloatUtil.compare(lengthSquared(), v.lengthSquared());
    }

    @Override
    public Vector3 toVector3() {
        return Vector3f.createFrom(x, y, z);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 3;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(z);
        result = prime * result + Float.floatToIntBits(w);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Vector4))
            return false;

        Vector4 other = (Vector4) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x()))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y()))
            return false;
        if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z()))
            return false;
        if (Float.floatToIntBits(w) != Float.floatToIntBits(other.w()))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return Vector4f.class.getSimpleName() + "(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

}
