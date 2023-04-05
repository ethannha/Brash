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
 * A <i>quaternion</i> represents an orientation about an axis in 3D space.
 * <p>
 * Quaternions have a <i>scalar</i> part, which correlates to an {@link Angle
 * angle} of rotation, and a <i>{@link Vector3 vector}</i> part, which
 * represents the axis of rotation. Note that the encoded scalar part is not
 * encoded as an {@link Angle angle} directly. This means that quaternion
 * <code>q = [w, v] = [w, v=(x, y, z)]</code>, where <code>w</code> is the
 * scalar part and <code>v</code> is the {@link Vector3 vector} part, is defined
 * as follows:
 *
 * <pre>
 * w = cos(theta/2)
 * v = [x * sin(theta/2), y * sin(theta/2), z * sin(theta/2)]
 * </pre>
 *
 * where <code>theta</code> is the actual {@link Angle angle} of rotation around
 * <code>v</code>, in radians. This means extraction of the <code>w</code> and
 * <code>v</code> components is as follows:
 * <p>
 *
 * <pre>
 * theta = 2 * arccos(w)
 * v     = [x / sin(theta/2), y / sin(theta/2), z / sin(theta/2)]
 * </pre>
 *
 * @author Raymond L. Rivera
 *
 */
public interface Quaternion extends FourDimensional, Addable<Quaternion>, Subtractable<Quaternion>,
                            Multiplicable<Quaternion>, ScalarProduct<Quaternion>, Normalizable<Quaternion>,
                            Invertible<Quaternion>, Comparable<Quaternion>, Bufferable,
                            LinearlyInterpolable<Quaternion>, SphericallyInterpolable<Quaternion>,
                            Negatable<Quaternion> {

    /**
     * Returns the {@link Angle angle} of rotation encoded by <code>this</code>
     * {@link Quaternion quaternion}.
     *
     * @return The {@link Angle angle} of rotation.
     */
    Angle angle();

    /**
     * Returns the rotation axis of <code>this</code> {@link Quaternion quaternion}
     * as a {@link Vector3 vector}.
     *
     * @return The {@link Vector3 vector} rotation axis.
     */
    Vector3 axis();

    /**
     * Rotate the specified {@link Vector3 vector} by <code>this</code>
     * {@link Quaternion quaternion}.
     *
     * @param v
     *            The {@link Vector3 vector} to be rotated by <code>this</code>
     *            {@link Quaternion quaternion}.
     * @return A new {@link Vector3 vector} with the rotation applied.
     */
    Vector3 rotate(Vector3 v);

    /**
     * Rotate the specified {@link Vector4 vector} by <code>this</code>
     * {@link Quaternion quaternion}.
     *
     * @param v
     *            The {@link Vector4 vector} to be rotated by <code>this</code>
     *            {@link Quaternion quaternion}.
     * @return A new {@link Vector4 vector} with the rotation applied.
     */
    Vector4 rotate(Vector4 v);

    /**
     * Returns the conjugate of <code>this</code> {@link Quaternion quaternion} as a
     * new {@link Quaternion quaternion}.
     * <p>
     * The conjugate <code>q'</code> of a {@link Quaternion quaternion}
     * <code>q = (w, [x, y, z])</code> is defined as:
     *
     * <pre>
     * q' = (w, [-x, -y, -z])
     * </pre>
     *
     * @return A new {@link Quaternion quaternion} that is the conjugate of
     *         <code>this</code> {@link Quaternion quaternion}.
     */
    Quaternion conjugate();

    /**
     * Converts <code>this</code> {@link Quaternion quaternion} to a pure rotation
     * {@link Matrix3 matrix}.
     *
     * @return The orientation represented by <code>this</code> {@link Quaternion
     *         quaternion} as a pure rotation {@link Matrix3 matrix}.
     */
    Matrix3 toMatrix3();

    /**
     * Converts <code>this</code> {@link Quaternion quaternion} to a {@link Matrix4
     * matrix}, representing a rotation with no translation.
     *
     * @return The orientation represented by <code>this</code> {@link Quaternion
     *         quaternion} as a {@link Matrix4 matrix} with a rotation and no
     *         translation.
     */
    Matrix4 toMatrix4();

}
