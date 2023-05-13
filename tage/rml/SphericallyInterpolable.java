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
 * A <i>spherically interpolable</i> object is one that supports
 * <a href="https://en.wikipedia.org/wiki/Slerp">spherical linear
 * interpolation</a> between itself and some other end-point.
 * <p>
 * <b>S</b>pherical <b>L</b>inear int<b>erp</b>olation, or <i>slerp</i>, is a
 * method to calculate mid-points between the two ends of a unit-radius
 * <a href="https://en.wikipedia.org/wiki/Great_circle">great circle</a> arc
 * based on an interporlation parameter in the <code>[0, 1]</code> range.
 * <p>
 * An application of this principle is for smooth 3D animations (i.e. motion at
 * a constant speed), using time <code>t</code> as the interpolation parameter.
 * This method can overcome {@link LinearlyInterpolable#lerp(Object, float)
 * lerp} limitations that cause motions to vary in speed.
 * <p>
 * However, two different arcs, or paths, can be validly chosen between the same
 * two ends, just like <code>3*PI/2</code> and <code>-PI/2</code> reach the same
 * end point, which may not be desirable. It's the implemetation's
 * responsibility to make sure the <i>shortest</i> arc path is used. For arcs
 * that are sufficiently small, using
 * {@link LinearlyInterpolable#lerp(Object, float) lerp} might be preferable.
 *
 * @author Raymond L. Rivera
 *
 * @see LinearlyInterpolable
 *
 * @param <T>
 *            The type that can be interpolated.
 */
interface SphericallyInterpolable<T> {

    /**
     * Performs spherical linear interpolation from <code>this</code> to
     * <code>end</code> by <code>t</code>, which is automatically clamped to the
     * <code>[0, 1]</code> range.
     *
     * @param end
     *            The other end-point with which to interpolate along the arc.
     * @param t
     *            The interpolation parameter.
     * @return A new instance with the result.
     * @throws NullPointerException
     *             If the <code>end</code> is <code>null</code>.
     */
    T slerp(T end, float t);

}
