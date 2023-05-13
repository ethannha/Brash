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
 * A <i>linearly interpolable</i> object is one that supports
 * <a href="https://en.wikipedia.org/wiki/Linear_interpolation">linear
 * interpolation</a> between itself and some other end-point.
 * <p>
 * <b>L</b>inear int<b>erp</b>olation, or <i>lerp</i>, is a way to calculate
 * points or values between ones that are known or tabulated using the points or
 * values that surround them, such as calculating arbitrary mid-point values
 * along the straight line defined by two known points. This is calculation is
 * performed with an interporlation parameter in the <code>[0, 1]</code> range.
 * <p>
 * We can apply this principle to generate values between known quantities, such
 * as the new position of an object moving along a straight line using time
 * <code>t</code> as the interporlation parameter.
 * <p>
 * However, lerp is not always able to produce motion at a <i>constant
 * speed</i>. For constant-speed motion (e.g. animations),
 * {@link SphericallyInterpolable#slerp(Object, float) slerp} might be more
 * helpful.
 *
 * @see SphericallyInterpolable
 *
 * @author Raymond L. Rivera
 *
 * @param <T>
 *            The type that can be interpolated.
 */
interface LinearlyInterpolable<T> {

    /**
     * Performs linear interpolation from <code>this</code> to <code>end</code>
     * by <code>t</code>, which is automatically clamped to the
     * <code>[0, 1]</code> range.
     *
     * @param end
     *            The opposite end of the line along which interpolation will
     *            take place.
     * @param t
     *            The interpolation parameter.
     * @return A new instance with the result.
     * @throws NullPointerException
     *             If the <code>end</code> is <code>null</code>.
     */
    T lerp(T end, float t);

}
