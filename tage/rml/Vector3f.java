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
 * An immutable single-precision floating point 3-component column
 * {@link Vector3 vector}.
 * <p>
 * This means the input <code>float[] { 1, 2, 3 };</code> produces the following
 * <pre>
 * 1
 * 2
 * 3
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
public final class Vector3f implements Vector3 {

    private static final Vector3 ZERO_VECTOR = new Vector3f(0f, 0f, 0f);
    private final static Vector3 UNIT_X      = new Vector3f(1f, 0f, 0f);
    private final static Vector3 UNIT_Y      = new Vector3f(0f, 1f, 0f);
    private final static Vector3 UNIT_Z      = new Vector3f(0f, 0f, 1f);

    private final float          x;
    private final float          y;
    private final float          z;

    private Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private Vector3f(final float[] values) {
        this(values[0], values[1], values[2]);
    }

    /**
     * Creates a new {@link Vector3 vector} with all components set to zero.
     *
     * @return A new zero-length {@link Vector3 vector}.
     */
    public static Vector3 createZeroVector() {
        return ZERO_VECTOR;
    }

    /**
     * Creates a new unit-length {@link Vector3 vector} along the x axis.
     *
     * @return A new unit-length {@link Vector3 vector}.
     */
    public static Vector3 createUnitVectorX() {
        return UNIT_X;
    }

    /**
     * Creates a new unit-length {@link Vector3 vector} along the y axis.
     *
     * @return A new unit-length {@link Vector3 vector}.
     */
    public static Vector3 createUnitVectorY() {
        return UNIT_Y;
    }

    /**
     * Creates a new unit-length {@link Vector3 vector} along the z axis.
     *
     * @return A new unit-length {@link Vector3 vector}.
     */
    public static Vector3 createUnitVectorZ() {
        return UNIT_Z;
    }

    /**
     * Creates a new {@link Vector3 vector} with the specified values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @return A new {@link Vector3 vector} with the specified values. The
     *         unspecified z component defaults to zero.
     */
    public static Vector3 createFrom(float x, float y) {
        return new Vector3f(x, y, 0f);
    }

    /**
     * Creates a new {@link Vector3 vector} with the specified values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @param z
     *            The value for the z component.
     * @return A new {@link Vector3 vector} with the specified values.
     */
    public static Vector3 createFrom(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }

    /**
     * Creates a new {@link Vector3 vector} with the specified values.
     *
     * @param values
     *            An array with the values for the x, y, and z components in the
     *            first, second, and third positions respectively.
     * @return A new {@link Vector3 vector} with the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 3 elements.
     */
    public static Vector3 createFrom(final float[] values) {
        return new Vector3f(values);
    }

    /**
     * Creates a new {@link Vector3 vector} with the specified values.
     *
     * @param v2
     *            A {@link Vector2 vector} with values for the x and y components.
     * @return A new {@link Vector3 vector} with the specified values of the input
     *         {@link Vector3 vector} for the x and y components. The unspecified z
     *         component defaults to zero.
     */
    public static Vector3 createFrom(final Vector2 v2) {
        return new Vector3f(v2.x(), v2.y(), 0f);
    }

    /**
     * Creates a new {@link Vector3 vector} with the specified values.
     *
     * @param v2
     *            A {@link Vector2 vector} with values for the x and y components.
     * @param z
     *            The value for the z component.
     * @return A new {@link Vector3 vector} with the specified values.
     */
    public static Vector3 createFrom(final Vector2 v2, float z) {
        return new Vector3f(v2.x(), v2.y(), z);
    }

    /**
     * Creates a new normalized {@link Vector3 vector} with the specified values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @return A new normal {@link Vector3 vector} with the specified values and a
     *         length of 1. The unspecified z component defaults to zero.
     * @throws ArithmeticException
     *             If the length of the {@link Vector3 vector} is zero.
     */
    public static Vector3 createNormalizedFrom(float x, float y) {
        return createNormalizedFrom(x, y, 0f);
    }

    /**
     * Creates a new normalized {@link Vector3 vector} with the specified values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @param z
     *            The value for the z component.
     * @return A new normal {@link Vector3 vector} with the specified values and a
     *         length of 1.
     * @throws ArithmeticException
     *             If the length of the {@link Vector3 vector} is zero.
     */
    public static Vector3 createNormalizedFrom(float x, float y, float z) {
        final float sqlen = MathUtil.lengthSquared(new float[] { x, y, z });
        if (FloatUtil.isZero(sqlen))
            throw new ArithmeticException("Cannot normalize zero-length vector");

        final float oneOverLen = MathUtil.invSqrt(sqlen);
        return new Vector3f(x * oneOverLen, y * oneOverLen, z * oneOverLen);
    }

    /**
     * Creates a new normalized {@link Vector3 vector} with the specified values.
     *
     * @param values
     *            An array with the values for the x, y, and z components in the
     *            first, second, and third positions respectively.
     * @return A new normal {@link Vector3 vector} with the specified values and a
     *         length of 1.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 3 elements.
     * @throws ArithmeticException
     *             If the length of the {@link Vector3 vector} is zero.
     */
    public static Vector3 createNormalizedFrom(final float[] values) {
        return createNormalizedFrom(values[0], values[1], values[2]);
    }

    /**
     * Creates a new normalized {@link Vector3 vector} with the specified values.
     *
     * @param v2
     *            A {@link Vector2 vector} with values for the x and y components.
     *            The unspecified z component defaults to zero.
     * @return A new {@link Vector3 vector} with the specified values of the input
     *         {@link Vector3 vector} for the x and y components and a length of 1.
     *         The unspecified z component defaults to zero.
     * @throws ArithmeticException
     *             If the length of the {@link Vector3 vector} is zero.
     */
    public static Vector3 createNormalizedFrom(final Vector2 v2) {
        return createNormalizedFrom(v2.x(), v2.y(), 0f);
    }

    /**
     * Creates a new normalized {@link Vector3 vector} with the specified values.
     *
     * @param v2
     *            A {@link Vector2 vector} with values for the x and y components.
     * @param z
     *            The value for the z component.
     * @return A new {@link Vector3 vector} with the specified values and a length
     *         of 1.
     * @throws ArithmeticException
     *             If the length of the {@link Vector3 vector} is zero.
     */
    public static Vector3 createNormalizedFrom(final Vector2 v2, float z) {
        return createNormalizedFrom(v2.x(), v2.y(), z);
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
    public Vector3 add(final Vector3 v) {
        return new Vector3f(x + v.x(), y + v.y(), z + v.z());
    }

    @Override
    public Vector3 add(float x, float y, float z) {
        return new Vector3f(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public Vector3 sub(final Vector3 v) {
        return new Vector3f(x - v.x(), y - v.y(), z - v.z());
    }

    @Override
    public Vector3 sub(float x, float y, float z) {
        return new Vector3f(this.x - x, this.y - y, this.z - z);
    }

    @Override
    public Vector3 mult(final Vector3 v) {
        return new Vector3f(x * v.x(), y * v.y(), z * v.z());
    }

    @Override
    public Vector3 div(final Vector3 v) {
        return new Vector3f(x / v.x(), y / v.y(), z / v.z());
    }

    @Override
    public Vector3 mult(float value) {
        return new Vector3f(x * value, y * value, z * value);
    }

    @Override
    public Vector3 div(float value) {
        value = 1.0f / value;
        return new Vector3f(x * value, y * value, z * value);
    }

    @Override
    public float dot(final Vector3 v) {
        return x * v.x() + y * v.y() + z * v.z();
    }

    @Override
    public Vector3 cross(final Vector3 v) {
        return new Vector3f(y * v.z() - z * v.y(), z * v.x() - x * v.z(), x * v.y() - y * v.x());
    }

    @Override
    public Vector3 normalize() {
        return createNormalizedFrom(x, y, z);
    }

    @Override
    public Vector3 rotate(Angle angle, Vector3 axis) {
        return Matrix3f.createRotationFrom(angle, axis).mult(this);
    }

    @Override
    public float length() {
        return MathUtil.length(toFloatArray());
    }

    @Override
    public float lengthSquared() {
        return MathUtil.lengthSquared(toFloatArray());
    }

    @Override
    public boolean isZeroLength() {
        return FloatUtil.isZero(lengthSquared(), FloatUtil.EPSILON);
    }

    /**
     * The values are in the following order: <code>[x, y, z]</code>.
     *
     * @see Bufferable#toFloatArray()
     */
    @Override
    public float[] toFloatArray() {
        return new float[] { x, y, z };
    }

    @Override
    public Vector3 lerp(Vector3 end, float t) {
        // t in [0, 1]
        t = MathUtil.clamp(t, 0f, 1f);
        return new Vector3f(MathUtil.lerp(toFloatArray(), end.toFloatArray(), t));
    }

    @Override
    public Vector3 negate() {
        return createFrom(-x, -y, -z);
    }

    @Override
    public int compareTo(final Vector3 v) {
        return FloatUtil.compare(lengthSquared(), v.lengthSquared());
    }

    @Override
    public Vector2 toVector2() {
        return Vector2f.createFrom(x, y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 3;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(z);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Vector3))
            return false;

        Vector3 other = (Vector3) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x()))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y()))
            return false;
        if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z()))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return Vector3f.class.getSimpleName() + "(" + x + ", " + y + ", " + z + ")";
    }

}
