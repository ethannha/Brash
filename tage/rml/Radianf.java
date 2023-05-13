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
 * An immutable single-precision floating point representation of an
 * {@link Angle}, measured in radians.
 *
 * @author Raymond L. Rivera
 *
 */
public final class Radianf implements Angle {

    private final float value;

    private Radianf(float radians) {
        value = radians;
    }

    /**
     * Creates a new instance with the specified measurement.
     *
     * @param radians
     *            The value, in radians, to be represented.
     * @see #createFrom(Angle)
     */
    public static Radianf createFrom(float radians) {
        return new Radianf(radians);
    }

    /**
     * Creates a new instance from the specified {@link Angle angle}.
     * <p>
     * This {@link Angle angle} is automatically converted to {@link Radianf
     * radians}.
     *
     * @param angle
     *            The {@link Angle} to be represented in {@link Radianf
     *            radians}.
     * @see #createFrom(float)
     */
    public static Radianf createFrom(Angle angle) {
        return new Radianf(angle.valueRadians());
    }

    @Override
    public float valueDegrees() {
        return MathUtil.toDegrees(value);
    }

    @Override
    public float valueRadians() {
        return value;
    }

    @Override
    public Angle add(Angle a) {
        return new Radianf(value + a.valueRadians());
    }

    @Override
    public Angle sub(Angle a) {
        return new Radianf(value - a.valueRadians());
    }

    @Override
    public Angle mult(float scalar) {
        return new Radianf(value * scalar);
    }

    @Override
    public Angle mult(Angle a) {
        return mult(a.valueRadians());
    }

    @Override
    public Angle div(float scalar) {
        if (FloatUtil.isZero(scalar))
            throw new ArithmeticException("Cannot divide by 0");

        return new Radianf(value / scalar);
    }

    @Override
    public Angle div(Angle a) {
        return div(a.valueRadians());
    }

    @Override
    public Angle negate() {
        return createFrom(-value);
    }

    @Override
    public int compareTo(final Angle angle) {
        return FloatUtil.compare(value, angle.valueRadians());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Angle))
            return false;

        Angle other = (Angle) obj;
        if (Float.floatToIntBits(value) != Float.floatToIntBits(other.valueRadians()))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 3;
        result = prime * result + Float.floatToIntBits(value);
        return result;
    }

    @Override
    public String toString() {
        return Radianf.class.getSimpleName() + "(" + value + ")";
    }

}
