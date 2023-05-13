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
 * {@link Angle}, measured in degrees.
 *
 * @author Raymond L. Rivera
 *
 */
public final class Degreef implements Angle {

    private final float value;

    private Degreef(float degrees) {
        value = degrees;
    }

    /**
     * Creates a new instance with the specified measurement.
     *
     * @param degrees
     *            The value, in degrees, to be represented.
     * @see #createFrom(Angle)
     */
    public static Degreef createFrom(float degrees) {
        return new Degreef(degrees);
    }

    /**
     * Creates a new instance from the specified {@link Angle angle}.
     * <p>
     * This {@link Angle angle} is automatically converted to {@link Degreef
     * degrees}.
     *
     * @param angle
     *            The {@link Angle angle} to be represented in {@link Degreef
     *            degrees}.
     * @see #createFrom(float)
     */
    public static Degreef createFrom(Angle angle) {
        return new Degreef(angle.valueDegrees());
    }

    @Override
    public float valueDegrees() {
        return value;
    }

    @Override
    public float valueRadians() {
        return MathUtil.toRadians(value);
    }

    @Override
    public Angle add(Angle a) {
        return new Degreef(value + a.valueDegrees());
    }

    @Override
    public Angle sub(Angle a) {
        return new Degreef(value - a.valueDegrees());
    }

    @Override
    public Angle mult(float scalar) {
        return new Degreef(value * scalar);
    }

    @Override
    public Angle mult(Angle a) {
        return mult(a.valueDegrees());
    }

    @Override
    public Angle div(float scalar) {
        if (FloatUtil.isZero(scalar))
            throw new ArithmeticException("Cannot divide by 0");

        return new Degreef(value / scalar);
    }

    @Override
    public Angle div(Angle a) {
        return div(a.valueDegrees());
    }

    @Override
    public Angle negate() {
        return createFrom(-value);
    }

    @Override
    public int compareTo(final Angle angle) {
        return FloatUtil.compare(value, angle.valueDegrees());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Angle))
            return false;

        Angle other = (Angle) obj;
        if (Float.floatToIntBits(value) != Float.floatToIntBits(other.valueDegrees()))
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
        return Degreef.class.getSimpleName() + "(" + value + ")";
    }

}
