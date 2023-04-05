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
 * An immutable single-precision floating point 2-component column
 * {@link Vector2 vector}.
 * <p>
 * This means the input <code>float[] { 1, 2 };</code> produces the following
 * layout:
 * <pre>
 * 1
 * 2
 * </pre>
 * The implementation assumes the input arrays have enough elements to build the
 * vector. When the input array does not have enough elements, an
 * {@link IndexOutOfBoundsException} will be thrown. However, when the input
 * array has extra elements, the extra data are ignored.
 *
 * @author Raymond L. Rivera
 *
 */
public final class Vector2f implements Vector2 {

    private static final Vector2 ZERO_VECTOR = new Vector2f(0f, 0f);
    private final static Vector2 UNIT_X      = new Vector2f(1f, 0f);
    private final static Vector2 UNIT_Y      = new Vector2f(0f, 1f);

    private final float          x;
    private final float          y;

    private Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    private Vector2f(final float[] values) {
        this(values[0], values[1]);
    }

    /**
     * Creates a new {@link Vector2 vector} instance with all components set to
     * zero.
     *
     * @return A new zero-length {@link Vector2 vector}.
     */
    public static Vector2 createZeroVector() {
        return ZERO_VECTOR;
    }

    /**
     * Creates a new unit-length {@link Vector2 vector} instance along the x-axis.
     *
     * @return A new unit-length {@link Vector2 vector}.
     */
    public static Vector2 createUnitVectorX() {
        return UNIT_X;
    }

    /**
     * Creates a new unit-length {@link Vector2 vector} instance along the y-axis.
     *
     * @return A new unit-length {@link Vector2 vector}.
     */
    public static Vector2 createUnitVectorY() {
        return UNIT_Y;
    }

    /**
     * Creates a new {@link Vector2 vector} instance with the specified values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @return A new {@link Vector2 vector} with the specified values.
     */
    public static Vector2 createFrom(float x, float y) {
        return new Vector2f(x, y);
    }

    /**
     * Creates a new {@link Vector2 vector} instance with the specified values.
     *
     * @param values
     *            An array with the values for the x and y components in the first
     *            and second positions respectively.
     * @return A new {@link Vector2 vector} with the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 2 elements.
     */
    public static Vector2 createFrom(final float[] values) {
        return new Vector2f(values);
    }

    /**
     * Creates a new normalized {@link Vector2 vector} instance with the specified
     * values.
     *
     * @param x
     *            The value for the x component.
     * @param y
     *            The value for the y component.
     * @return A new normal {@link Vector2 vector} with the specified values and a
     *         length of 1.
     * @throws ArithmeticException
     *             If the length of the {@link Vector2 vector} is zero.
     */
    public static Vector2 createNormalizedFrom(float x, float y) {
        final float sqlen = MathUtil.lengthSquared(new float[] { x, y });
        if (FloatUtil.isZero(sqlen))
            throw new ArithmeticException("Cannot normalize zero-length vector");

        final float oneOverLen = MathUtil.invSqrt(sqlen);
        return new Vector2f(x * oneOverLen, y * oneOverLen);
    }

    /**
     * Creates a new normalized {@link Vector2 vector} instance with the specified
     * values.
     *
     * @param values
     *            An array with the values for the x and y components in the first
     *            and second positions respectively.
     * @return A new normal {@link Vector2 vector} with the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 2 elements.
     */
    public static Vector2 createNormalizedFrom(final float[] values) {
        return createNormalizedFrom(values[0], values[1]);
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
    public Vector2 add(final Vector2 v) {
        return new Vector2f(x + v.x(), y + v.y());
    }

    @Override
    public Vector2 add(float x, float y) {
        return new Vector2f(this.x + x, this.y + y);
    }

    @Override
    public Vector2 sub(final Vector2 v) {
        return new Vector2f(x - v.x(), y - v.y());
    }

    @Override
    public Vector2 sub(float x, float y) {
        return new Vector2f(this.x - x, this.y - y);
    }

    @Override
    public Vector2 mult(final Vector2 v) {
        return new Vector2f(x * v.x(), y * v.y());
    }

    @Override
    public Vector2 div(final Vector2 v) {
        return new Vector2f(x / v.x(), y / v.y());
    }

    @Override
    public Vector2 mult(float value) {
        return new Vector2f(x * value, y * value);
    }

    @Override
    public Vector2 div(float value) {
        value = 1.0f / value;
        return new Vector2f(x * value, y * value);
    }

    @Override
    public float dot(final Vector2 v) {
        return x * v.x() + y * v.y();
    }

    @Override
    public Vector2 normalize() {
        return createNormalizedFrom(x, y);
    }

    @Override
    public Vector2 rotate(final Angle angle) {
        return Matrix2f.createRotationFrom(angle).mult(this);
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
     * The values are in the following order: <code>[x, y]</code>.
     *
     * @see Bufferable#toFloatArray()
     */
    @Override
    public float[] toFloatArray() {
        return new float[] { x, y };
    }

    @Override
    public Vector2 lerp(Vector2 end, float t) {
        // t in [0, 1]
        t = MathUtil.clamp(t, 0f, 1f);
        return new Vector2f(MathUtil.lerp(toFloatArray(), end.toFloatArray(), t));
    }

    @Override
    public Vector2 negate() {
        return createFrom(-x, -y);
    }

    @Override
    public int compareTo(final Vector2 v) {
        return FloatUtil.compare(lengthSquared(), v.lengthSquared());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 3;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Vector2))
            return false;

        Vector2 other = (Vector2) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x()))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y()))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return Vector2f.class.getSimpleName() + "(" + x + ", " + y + ")";
    }

}
