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
 * An <i>angle</i> is the figure formed by two rays sharing the same vertex end
 * point and represents a measurement of the amount of rotation between them.
 *
 * @author Raymond L. Rivera
 *
 */
public interface Angle extends Addable<Angle>, Subtractable<Angle>, Multiplicable<Angle>, Divisible<Angle>,
                       Comparable<Angle>, Negatable<Angle> {

    /**
     * Return the value of <code>this</code> {@link Angle angle}, in degrees.
     *
     * @return The value of <code>this</code> {@link Angle angle}, in degrees.
     */
    float valueDegrees();

    /**
     * Return the value of <code>this</code> {@link Angle angle}, in radians.
     *
     * @return The value of <code>this</code> {@link Angle angle}, in radians.
     */
    float valueRadians();

    /**
     * Adds the specified {@link Angle angle} to <code>this</code> {@link Angle
     * angle}.
     *
     * @param angle
     *            The {@link Angle angle} to be added to <code>this</code>
     *            {@link Angle angle}.
     * @return A new {@link Angle angle} with the total.
     */
    @Override
    Angle add(final Angle angle);

    /**
     * Subtracts the specified {@link Angle angle} from <code>this</code>
     * {@link Angle angle}.
     *
     * @param angle
     *            The {@link Angle angle} to be subtracted from
     *            <code>this</code> {@link Angle angle}.
     * @return A new {@link Angle angle} with the difference.
     */
    @Override
    Angle sub(final Angle angle);

    /**
     * Multiplies the specified {@link Angle angle} by <code>this</code>
     * {@link Angle angle}.
     *
     * @param angle
     *            The {@link Angle angle} to be multiplied by <code>this</code>
     *            {@link Angle angle}.
     * @return A new {@link Angle angle} with the product.
     */
    @Override
    Angle mult(final Angle angle);

    /**
     * Multiplies the specified scalar value by <code>this</code> {@link Angle
     * angle}.
     *
     * @param scalar
     *            The scalar value to be multiplied by <code>this</code>
     *            {@link Angle angle}.
     * @return A new {@link Angle angle} with the product.
     */
    @Override
    Angle mult(float scalar);

    /**
     * Divides <code>this</code> {@link Angle angle} by the specified
     * {@link Angle angle}.
     *
     * @param angle
     *            The {@link Angle angle} to be used as the denominator.
     * @return A new {@link Angle angle} with the quotient.
     * @throws ArithmeticException
     *             If a division by zero takes place.
     */
    @Override
    Angle div(final Angle angle);

    /**
     * Divides <code>this</code> {@link Angle angle} by the specified scalar
     * value.
     *
     * @param scalar
     *            The scalar value that will be used as the denominator.
     * @return A new {@link Angle angle} with the quotient.
     * @throws ArithmeticException
     *             If a division by zero takes place.
     */
    @Override
    Angle div(float scalar);

}
