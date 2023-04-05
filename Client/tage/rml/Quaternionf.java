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
 * An immutable single-precision floating point implementation of a
 * {@link Quaternion quaternion}.
 * <p>
 * The implementation assumes the input arrays have enough elements to build the
 * quaternion. When the input array does not have enough elements, an
 * {@link IndexOutOfBoundsException} will be thrown. However, when the input
 * array has extra elements, the extra data are ignored.
 *
 * @author Raymond L. Rivera
 *
 */
public class Quaternionf implements Quaternion {

    private static final Quaternion IDENTITY_QUATERNION = new Quaternionf(1f, 0f, 0f, 0f);
    private static final Quaternion ZERO_QUATERNION     = new Quaternionf(0f, 0f, 0f, 0f);

    private final float             w;
    private final float             x;
    private final float             y;
    private final float             z;

    private Quaternionf(final Angle angle, final Vector3 axis) {
        final float halfAngle = angle.valueRadians() * 0.5f;
        final float cosHalfAngle = MathUtil.cos(halfAngle);
        final float sinHalfAngle = MathUtil.sin(halfAngle);

        w = cosHalfAngle;
        x = axis.x() * sinHalfAngle;
        y = axis.y() * sinHalfAngle;
        z = axis.z() * sinHalfAngle;
    }

    private Quaternionf(float w, float x, float y, float z) {
        // the raw input value of the scalar component must be in the [-1, 1]
        // range because it's the valid domain for the inverse cosine function,
        // which is necessary to get the angle
        this.w = MathUtil.clamp(w, -1f, 1f);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private Quaternionf(final float[] values) {
        this(values[0], values[1], values[2], values[3]);
    }

    /**
     * Creates an identity {@link Quaternion quaternion}. The identity
     * {@link Quaternion quaternion} is <code>(1, [0, 0, 0])</code>.
     *
     * @return The identity {@link Quaternion quaternion}.
     */
    public static Quaternion createIdentityQuaternion() {
        return IDENTITY_QUATERNION;
    }

    /**
     * Creates zero {@link Quaternion quaternion}. The zero {@link Quaternion
     * quaternion} is <code>(0, [0, 0, 0])</code>.
     *
     * @return The zero {@link Quaternion quaternion}.
     */
    public static Quaternion createZeroQuaternion() {
        return ZERO_QUATERNION;
    }

    /**
     * Creates a new {@link Quaternion quaternion} from the specified
     * <code>(w, [x, y, z])</code> values, where <code>w</code> is the scalar part
     * and <code>[x, y, z]</code> is the {@link Vector3 vector} part.
     * <p>
     * If the scalar <code>w</code> component is outside the <code>[-1, 1]</code>
     * range, it will be automatically clamped.
     *
     * @param values
     *            The <code>(w, [x, y, z])</code> components of the
     *            {@link Quaternion quaternion}.
     * @return A new {@link Quaternion quaternion} with the specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 elements.
     */
    public static Quaternion createFrom(final float[] values) {
        return new Quaternionf(values[0], values[1], values[2], values[3]);
    }

    /**
     * Creates a new {@link Quaternion quaternion} from the specified <code>(w,
     * [x, y, z])</code> values, where <code>w</code> is the scalar part and
     * <code>[x, y, z]</code> is the {@link Vector3 vector} part.
     *
     * @param w
     *            The value for the scalar part. If it's outside the
     *            <code>[-1, 1]</code> range, it's automatically clamped.
     * @param x
     *            The x component of the {@link Vector3 vector} part.
     * @param y
     *            The y component of the {@link Vector3 vector} part.
     * @param z
     *            The z component of the {@link Vector3 vector} part.
     * @return A new {@link Quaternion quaternion} with the specified values.
     */
    public static Quaternion createFrom(float w, float x, float y, float z) {
        return new Quaternionf(w, x, y, z);
    }

    /**
     * Creates a new {@link Quaternion quaternion} from the specified {@link Angle
     * angle} and {@link Vector3 axis}.
     *
     * @param angle
     *            The {@link Angle angle} of rotation.
     * @param axis
     *            The {@link Vector3 axis} of rotation.
     * @return A new {@link Quaternion quaternion}.
     */
    public static Quaternion createFrom(final Angle angle, final Vector3 axis) {
        return new Quaternionf(angle, axis);
    }

    /**
     * Creates a new {@link Quaternion quaternion} from the specified {@link Angle
     * angle} and {@link Vector3 axis}.
     *
     * @param w
     *            The scalar part of <code>this</code> {@link Quaternion
     *            quaternion}. If it's outside the <code>[-1, 1]</code> range, it's
     *            automatically clamped.
     * @param axis
     *            The {@link Vector3 axis} of rotation.
     * @return A new {@link Quaternion quaternion}.
     */
    public static Quaternion createFrom(float w, final Vector3 axis) {
        return new Quaternionf(w, axis.x(), axis.y(), axis.z());
    }

    /**
     * Creates a new <i>normalized</i> {@link Quaternion quaternion} from the
     * specified <code>(w, [x, y, z])</code> values, where <code>w</code> is the
     * scalar part and <code>[x, y, z]</code> is the {@link Vector3 vector} part.
     *
     * @param w
     *            The value for the scalar part. If it's outside the
     *            <code>[-1, 1]</code> range, it's automatically clamped.
     * @param x
     *            The x component of the {@link Vector3 vector} part.
     * @param y
     *            The y component of the {@link Vector3 vector} part.
     * @param z
     *            The z component of the {@link Vector3 vector} part.
     * @return A new <i>normalized</i> {@link Quaternion quaternion} with the
     *         specified values.
     */
    public static Quaternion createNormalizedFrom(float w, float x, float y, float z) {
        final float sqlen = MathUtil.lengthSquared(new float[] { w, x, y, z });
        if (FloatUtil.isEqual(sqlen, 0f))
            throw new ArithmeticException("Cannot normalize zero-length quaternion");

        final float invLen = MathUtil.invSqrt(sqlen);
        return new Quaternionf(w * invLen, x * invLen, y * invLen, z * invLen);
    }

    /**
     * Creates a new <i>normalized</i> {@link Quaternion quaternion} from the
     * specified <code>(w, [x, y, z])</code> values, where <code>w</code> is the
     * scalar part and <code>[x, y, z]</code> is the {@link Vector3 vector} part.
     *
     * @param values
     *            The <code>(w, [x, y, z])</code> components of the
     *            {@link Quaternion quaternion}.
     * @return A new <i>normalized</i> {@link Quaternion quaternion} with the
     *         specified values.
     * @throws IndexOutOfBoundsException
     *             If the input array has less than 4 elements.
     */
    public static Quaternion createNormalizedFrom(final float[] values) {
        return createNormalizedFrom(values[0], values[1], values[2], values[3]);
    }

    /**
     * Creates a new <i>normalized</i> {@link Quaternion quaternion} from the
     * specified {@link Angle angle} and {@link Vector3 axis}.
     *
     * @param angle
     *            The {@link Angle angle} of rotation.
     * @param axis
     *            The {@link Vector3 axis} of rotation.
     * @return A new <i>normalized</i> {@link Quaternion quaternion}.
     */
    public static Quaternion createNormalizedFrom(final Angle angle, final Vector3 axis) {
        return createFrom(angle, axis).normalize();
    }

    /**
     * Creates a new <i>normalized</i> {@link Quaternion quaternion} from the
     * specified {@link Angle angle} and {@link Vector3 axis}.
     *
     * @param w
     *            The scalar part of <code>this</code> {@link Quaternion
     *            quaternion}. If it's outside the <code>[-1, 1]</code> range, it's
     *            automatically clamped.
     * @param axis
     *            The {@link Vector3 axis} of rotation.
     * @return A new <i>normalized</i> {@link Quaternion quaternion}.
     */
    public static Quaternion createNormalizedFrom(float w, final Vector3 axis) {
        return createNormalizedFrom(w, axis.x(), axis.y(), axis.z());
    }

    @Override
    public float w() {
        return w;
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
    public Angle angle() {
        return Radianf.createFrom((2.0f * MathUtil.acos(w)));
    }

    @Override
    public Vector3 axis() {
        // sin^2(theta/2) = 1 - cos^2(theta/2)
        float sinHalfAngle = MathUtil.sqrt(1f - w * w);

        if (FloatUtil.isZero(sinHalfAngle))
            sinHalfAngle = 1f;

        final float invSinHalfAngle = 1.0f / sinHalfAngle;
        return Vector3f.createNormalizedFrom(x * invSinHalfAngle, y * invSinHalfAngle, z * invSinHalfAngle);
    }

    @Override
    public Quaternion add(Quaternion q) {
        return new Quaternionf(w + q.w(), x + q.x(), y + q.y(), z + q.z());
    }

    @Override
    public Quaternion sub(Quaternion q) {
        return new Quaternionf(w - q.w(), x - q.x(), y - q.y(), z - q.z());
    }

    /**
     * Returns a new {@link Quaternion quaternion} with the product of
     * <code>this</code> {@link Quaternion quaternion} post-multiplied by the
     * specified {@link Quaternion quaternion}. Note also that the returned
     * {@link Quaternion quaternion} is not <i>necessarily</i> a unit
     * {@link Quaternion quaternion} and may need to be normalized (e.g. if at least
     * one of the two {@link Quaternion quaternions} being multiplied is not
     * unit-length).
     * <p>
     * {@link Quaternion} multiplication can be used to compose rotations, similar
     * to {@link Matrix3 matrix} concatenation. The product of two {@link Quaternion
     * quaternions} <code>p</code> and <code>q</code> is defined as follows:
     *
     * <pre>
     * p = (w1, v1), where v1 = [x1, y1, z1]
     * q = (w2, v2), where v2 = [x2, y2, z2]
     *
     * pq = (w1 * w2 - (v1 . v2), [v2 * w1 + v1 * w2 + (v1 x v2)])
     * </pre>
     *
     * where <code>w</code> represents the scalar part and <code>v</code> represents
     * the {@link Vector3 vector} part.
     *
     * @param q
     *            The {@link Quaternion quaternion} to be post-multiplied with
     *            <code>this</code> {@link Quaternion quaternion}
     * 
     * @return A new {@link Quaternion quaternion} <code>p' = pq</code>
     */
    @Override
    public Quaternion mult(Quaternion q) {
        final float rw = w * q.w() - x * q.x() - y * q.y() - z * q.z();
        final float rx = w * q.x() + x * q.w() + y * q.z() - z * q.y();
        final float ry = w * q.y() - x * q.z() + y * q.w() + z * q.x();
        final float rz = w * q.z() + x * q.y() - y * q.x() + z * q.w();
        return createNormalizedFrom(rw, rx, ry, rz);
    }

    /**
     * Returns a new {@link Quaternion quaternion} with the result of multiplying
     * (scaling) <code>this</code> {@link Quaternion quaternion} by the specified
     * scalar value.
     * <p>
     * The definition of multiplying a {@link Quaternion quaternion} by a scalar
     * value is that it produces a new {@link Quaternion quaternion} where each
     * component of the original has been multiplied by the scalar value. Note also
     * that the returned {@link Quaternion quaternion} is not necessarily a unit
     * {@link Quaternion quaternion} and may need to be normalized.
     *
     * @param s
     *            The value to be multiplied by each component of <code>this</code>
     *            {@link Quaternion quaternion}.
     * @return A new {@link Quaternion quaternion} with the result of this
     *         multiplication.
     */
    @Override
    public Quaternion mult(float s) {
        return new Quaternionf(w * s, x * s, y * s, z * s);
    }

    @Override
    public float dot(final Quaternion q) {
        return w * q.w() + x * q.x() + y * q.y() + z * q.z();
    }

    /**
     * Returns a new {@link Quaternion quaternion} that is the inverse of
     * <code>this</code> one.
     * <p>
     * The inverse {@link Quaternion quaternion} <code>q"</code> is defined as:
     *
     * <pre>
     * q' = q* / ||q||^2
     * </pre>
     *
     * where <code>q*</code> is the conjugate and <code>||q||</code> is the length
     * of <code>this</code> instance.
     */
    @Override
    public Quaternion inverse() {
        final float sqlen = lengthSquared();

        if (FloatUtil.isZero(sqlen))
            throw new ArithmeticException("Cannot invert zero-length quaternion");
        if (FloatUtil.isEqual(sqlen, 1f))
            return conjugate();

        final float invLen = 1f / sqlen;
        return new Quaternionf(w * invLen, -x * invLen, -y * invLen, -z * invLen);
    }

    @Override
    public Quaternion normalize() {
        return createNormalizedFrom(w, x, y, z);
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

    @Override
    public Vector3 rotate(Vector3 axis) {
        // work with normalized quaternions to make sure we're only rotating and
        // not accumulating other transforms as a side-effect
        Quaternion p = createNormalizedFrom(0, axis);
        Quaternion q = normalize();

        // The formula[1] to apply a rotation to a vector using a quaternion is:
        // p' = qpq*
        // where q is this quaternion, p has the vector being rotated, q* is
        // the conjugate of q, and p' has the new vector with the rotation
        // applied.
        //
        // [1] Game Engine Architecture, p.172
        return q.mult(p).mult(q.conjugate()).normalize().axis();
    }

    @Override
    public Vector4 rotate(Vector4 axis) {
        return Vector4f.createFrom(rotate(axis.toVector3()), axis.w());
    }

    @Override
    public Quaternion conjugate() {
        return new Quaternionf(w, -x, -y, -z);
    }

    @Override
    public Matrix3 toMatrix3() {
        final Quaternion q = normalize();
        final float w = q.w();
        final float x = q.x();
        final float y = q.y();
        final float z = q.z();

        final float x2 = x * x;
        final float y2 = y * y;
        final float z2 = z * z;
        final float wx = w * x;
        final float wy = w * y;
        final float wz = w * z;
        final float xy = x * y;
        final float xz = x * z;
        final float yz = y * z;

        // @formatter:off
        final float[] m3x3 = new float[] {
            1f - 2f * (y2 + z2),    2f * (xy + wz)  ,    2f * (xz - wy)  ,
               2f * (xy - wz)  , 1f - 2f * (x2 + z2),    2f * (yz + wx)  ,
               2f * (wy + xz)  ,    2f * (yz - wx)  , 1f - 2f * (x2 + y2)
        };
        // @formatter:on

        return Matrix3f.createFrom(m3x3);
    }

    @Override
    public Matrix4 toMatrix4() {
        return Matrix4f.createFrom(toMatrix3());
    }

    /**
     * The values are in the following order: <code>[w, x, y, z]</code>.
     *
     * @see Bufferable#toFloatArray()
     */
    @Override
    public float[] toFloatArray() {
        return new float[] { w, x, y, z };
    }

    @Override
    public Quaternion lerp(Quaternion end, float t) {
        // t in [0, 1]
        t = MathUtil.clamp(t, 0f, 1f);
        return createFrom(MathUtil.lerp(toFloatArray(), end.toFloatArray(), t));
    }

    /**
     * {@inheritDoc}
     * <p>
     * {@link Quaternion} slerp between <code>p</code> and <code>q</code> by
     * <code>t</code> is defined as:
     *
     * <pre>
     * slerp(p, q, t) = p*lambda + q*phi | t in [0, 1]
     * </pre>
     *
     * where:
     *
     * <pre>
     * lambda = sin(theta*[1 - t]) / sin(theta)
     * phi    = sin(theta*t) / sin(theta)
     * theta  = acos(p . q)
     * </pre>
     *
     * There're some edge-cases the implementation accounts for:
     *
     * <ul>
     * <li>returns <code>this iff t == 0.0</code></li>
     * <li>returns <code>end iff t == 1.0</code></li>
     * <li>uses {@link #lerp(Quaternion, float)} if <code>this</code> and
     * <code>end</code> are very close, i.e. <code>cos(theta) ~ +1</code></li>
     * <li>uses {@link #lerp(Quaternion, float)} as fallback if <code>this</code>
     * and <code>end</code> are almost opposite, i.e.,
     * <code>cos(theta) ~ -1</code></li>
     * </ul>
     *
     * The last {@link #lerp(Quaternion, float)} fallback case is necessary due to
     * an <i>infinite</i> number of possible interpolation paths along the
     * <a href="https://en.wikipedia.org/wiki/Great-circle_distance">great arc</a>
     * without a reliable way to choose the correct one.
     * <p>
     * For more details, see:
     * <ul>
     * <li>Game Engine Architecture, 2nd Ed, p.208;</li>
     * <li><a href="https://www.3dgep.com/understanding-quaternions/">Understanding
     * Quaternions</a>; and</li>
     * <li><code>Quaternion::Slerp(float, Quaternion, Quaternion, bool)</code> from
     * <a href= "http://www.ogre3d.org">OGRE3D</a></li>
     * </ul>
     */
    @Override
    public Quaternion slerp(Quaternion end, float t) {
        // t in [0, 1]
        t = MathUtil.clamp(t, 0f, 1f);

        // avoid generating known end-points
        if (FloatUtil.isEqual(t, 0.0f))
            return this;
        if (FloatUtil.isEqual(t, 1.0f))
            return end;

        // only normalized quaternions are valid rotations
        Quaternion p = normalize();
        Quaternion q = end.normalize();

        float cosTheta = p.dot(q);
        if (FloatUtil.compare(cosTheta, 0f) < 0) {
            // edge-case: quaternions have opposite handed-ness and slerp will
            // take the longer path along the arc (i.e. it won't be
            // torque-minimal); to fix this, just reverse one quaternion
            q = q.negate();
            cosTheta = -cosTheta;
        }

        if (FloatUtil.isEqual(Math.abs(cosTheta), 1f)) {
            // @formatter:off
            // There are two possibilities in this edge-case:
            //
            // 1. p and q are close enough (cos(theta) ~ +1) to lerp safely, or
            //
            // 2. p and q are almost inverse of each other (cos(theta) ~ -1)
            //    with an infinite number of possible interpolation
            //    paths, and no way to reliably choose the correct one,
            //    so we fall back to lerp.
            //
            // This requires re-normalization.
            // @formatter:on
            return createNormalizedFrom(MathUtil.lerp(p.toFloatArray(), q.toFloatArray(), t));
        }

        // This is the standard slerp case; cos(theta) is far enough from 1 to avoid
        // turning 1/sin(theta) into a division by zero
        final float theta = MathUtil.acos(cosTheta);
        final float sinTheta = MathUtil.sin(theta);
        final float invSinTheta = 1f / sinTheta;
        final float lambda = MathUtil.sin((1f - t) * theta) * invSinTheta;
        final float phi = MathUtil.sin(t * theta) * invSinTheta;

        return p.mult(lambda).add(q.mult(phi));
    }

    @Override
    public Quaternion negate() {
        return createFrom(-w, -x, -y, -z);
    }

    @Override
    public int compareTo(Quaternion q) {
        return FloatUtil.compare(lengthSquared(), q.lengthSquared());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 7;

        result = prime * result + Float.floatToIntBits(w);
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(z);

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Quaternion))
            return false;

        Quaternion rhs = (Quaternion) obj;
        if (Float.floatToIntBits(w) != Float.floatToIntBits(rhs.w()))
            return false;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(rhs.x()))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(rhs.y()))
            return false;
        if (Float.floatToIntBits(z) != Float.floatToIntBits(rhs.z()))
            return false;

        return true;
    }

    @Override
    public String toString() {
        return Quaternionf.class.getSimpleName() + "(" + w + ", [" + x + ", " + y + ", " + z + "])";
    }

}
