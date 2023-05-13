/**
 * Copyright (C) 2017 Raymond L. Rivera <ray.l.rivera@gmail.com>
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
 * Common mathematical operations that are shared by different types.
 *
 * @author Raymond L. Rivera
 *
 */
final class MathUtil {

    private static final float PI               = (float) Math.PI;
    private static final float DEGREE_TO_RADIAN = PI / 180.0f;
    private static final float RADIAN_TO_DEGREE = 180.0f / PI;

    private MathUtil() {}

    /**
     * Converts a scalar value, in radians, to degrees.
     *
     * @param radians
     *            The value to be converted, in radians.
     * @return The value converted to degrees.
     */
    public static float toDegrees(final float radians) {
        return radians * RADIAN_TO_DEGREE;
    }

    /**
     * Converts a scalar value, in degrees, to radians.
     *
     * @param degrees
     *            The value to be converted, in degrees.
     * @return The value converted to radians.
     */
    public static float toRadians(final float degrees) {
        return degrees * DEGREE_TO_RADIAN;
    }

    /**
     * Calculates the sine of the argument using {@link Math#sin(double)}, but with
     * automatic casting to float.
     *
     * @param radians
     *            The scalar angle, in radians.
     * @return The sine of the argument.
     * @see Math#sin(double)
     */
    public static float sin(final float radians) {
        return (float) Math.sin(radians);
    }

    /**
     * Calculates the arc sine of the argument using {@link Math#asin(double)}, but
     * with automatic casting to float.
     *
     * @param scalar
     *            The scalar value.
     * @return The arc sine of the argument.
     * @see Math#asin(double)
     */
    public static float asin(final float scalar) {
        return (float) Math.asin(scalar);
    }

    /**
     * Calculates the cosine of the argument using {@link Math#cos(double)}, but
     * with automatic casting to float.
     *
     * @param radians
     *            The scalar angle, in radians.
     * @return The cosine of the argument.
     * @see Math#cos(double)
     */
    public static float cos(final float radians) {
        return (float) Math.cos(radians);
    }

    /**
     * Calculates the arc cosine of the argument using {@link Math#acos(double)},
     * but with automatic casting to float.
     *
     * @param scalar
     *            The scalar value.
     * @return The arc cosine of the argument.
     * @see Math#acos(double)
     */
    public static float acos(final float radians) {
        return (float) Math.acos(radians);
    }

    /**
     * Calculates the tangent of the argument using {@link Math#tan(double)}, but
     * with automatic casting to float.
     *
     * @param scalar
     *            The scalar value.
     * @return The tangent of the argument.
     * @see Math#tan(double)
     */
    public static float tan(final float radians) {
        return (float) Math.tan(radians);
    }

    /**
     * Calculates the arc tangent of the argument using {@link Math#atan(double)},
     * but with automatic casting to float.
     *
     * @param scalar
     *            The scalar value.
     * @return The arc tangent of the argument.
     * @see Math#atan(double)
     */
    public static float atan(final float radians) {
        return (float) Math.atan(radians);
    }

    /**
     * Calculates the square root of a scalar value using {@link Math#sqrt(double)},
     * but with automatic casting to float.
     *
     * @param s
     *            The scalar value.
     * @return The square root of the argument.
     * @see Math#sqrt(double)
     */
    public static float sqrt(final float s) {
        return (float) Math.sqrt(s);
    }

    /**
     * Calculates the inverse square root of the scalar value.
     *
     * @param s
     *            The scalar value.
     * @return The inverse square root of the argument.
     */
    public static float invSqrt(final float s) {
        assert !FloatUtil.isZero(s);
        return 1.0f / sqrt(s);
    }

    /**
     * Clamps a specified value to be within the specified range, inclusive.
     *
     * @param s
     *            The scalar value to clamp.
     * @param min
     *            The minimum value allowed, inclusive.
     * @param max
     *            The maximum value allowed, inclusive.
     * @return A value <code>t in [min, max]</code>
     */
    public static float clamp(final float s, final float min, final float max) {
        return Math.max(min, Math.min(s, max));
    }

    /**
     * Calculates the length, or magnitude.
     * <p>
     * This is useful for {@link Vector vectors} of arbitrary dimensions and
     * {@link Quaternion quaternions}.
     * <p>
     * The original array is <i>not</i> modified.
     *
     * @param v
     *            Array of scalars, with at least 2 entries.
     * @return The result of <code>sqrt(v[0]^2 + v[1]^2 + ... + v[n-1]^2)</code>.
     */
    public static float length(final float[] v) {
        return (float) Math.sqrt(lengthSquared(v));
    }

    /**
     * Calculates the <i>squared</i> length, or magnitude.
     * <p>
     * This is useful for performance reasons (e.g. when comparing {@link Vector
     * vectors} or {@link Quaternion quaternions}), because it avoids expensive
     * <code>sqrt</code> computations.
     * <p>
     * The original array is <i>not</i> modified.
     *
     * @param v
     *            Array of scalars, with at least 2 entries.
     * @return The result of <code>v[0]^2 + v[1]^2 + ... + v[n-1]^2</code>.
     */
    public static float lengthSquared(final float[] v) {
        assert v.length > 1;

        float sqlen = 0f;
        for (int i = 0; i < v.length; ++i)
            sqlen += v[i] * v[i];

        return sqlen;
    }

    /**
     * Linearly interpolates the values in arrays of arbitrary dimensions,
     * calculated as:
     *
     * <pre>
     * lerp(s, e, t) = [(1 - t) * s] + [t * e]
     * </pre>
     *
     * where <code>s</code> is the starting position, <code>e</code> is the ending
     * position, and <code>t in [0, 1]</code> is the interpolation parameter.
     * <p>
     * This implementation:
     * <ul>
     * <li>returns <code>s iff t == 0.0</code></li>
     * <li>returns <code>e iff t == 1.0</code></li>
     * </ul>
     * <p>
     * This is useful for {@link Vector vectors} and {@link Quaternion quaternions}
     * alike.
     * <p>
     * It's the caller's responsibility to ensure that the interpolation parameter
     * is within a valid range.
     * <p>
     * The original arrays are <i>not</i> modified.
     *
     * @param s
     *            The starting position.
     * @param e
     *            The ending position.
     * @param t
     *            The interpolation parameter, <code>t in [0, 1]</code>.
     * @return A new array of the same dimension as the inputs containing mid-point
     *         values.
     */
    public static float[] lerp(final float[] s, final float[] e, final float t) {
        assert s.length == e.length;

        // avoid generating known end-points
        if (FloatUtil.isEqual(t, 0.0f))
            return s;
        if (FloatUtil.isEqual(t, 1.0f))
            return e;

        // lerp(s, e, t) = [(1 - t) * s] + [t * e]
        final float[] m = new float[s.length];
        final float d = 1.0f - t;
        for (int i = 0; i < s.length; ++i)
            m[i] = d * s[i] + t * e[i];

        return m;
    }

}
