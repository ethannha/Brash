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
 * A <i>matrix</i> is a rectangular structure to efficiently hold scalar values.
 * <p>
 * This library's intent is for implementations to use column-major orderings
 * and offers a <code>#mult(Vector)</code> method to allow post-multiplication
 * with {@link Vector vectors}, but not the other way around. In other words,
 * for a {@link Vector vector} <code>v</code> and matrix <code>M</code>, the
 * column {@link Vector vectors} <code>v'</code> can be calculated as
 * <code>v' = M * v</code>, but not <code>v' = v * M</code>, which would require
 * a row-{@link Vector vectors} instead.
 * <p>
 * You should make sure the dimensions of the matrix type match those of the
 * {@link Vector vector} type. For example, A 2x2 matrix should only be matched
 * with a 2-component {@link Vector vector}, a 3x3 matrix with a 3-component
 * {@link Vector vector}, and so on.
 *
 * @author Raymond L. Rivera
 *
 * @param <M>
 *            The matrix type.
 * @param <V>
 *            The {@link Vector vector} type.
 */
interface Matrix<M extends Matrix<M, V>, V extends Vector<V>> extends Addable<M>, Subtractable<M>, Multiplicable<M>,
                Invertible<M>, Transposable<M>, Scalable<M, V>, Bufferable {

    /**
     * Returns the scalar value at the specified row and column.
     *
     * @param row
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} row.
     * @param col
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} column.
     * @return The scalar value found at the specified location.
     * @throws IndexOutOfBoundsException
     *             If either index is outside the row/column range for
     *             <code>this</code> {@link Matrix matrix}.
     */
    float value(int row, int col);

    /**
     * Returns the specified {@link Matrix matrix} row as a new {@link Vector
     * vector}.
     *
     * @param row
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} row.
     * @return A {@link Vector vector} with data from the specified row.
     * @throws IndexOutOfBoundsException
     *             If the row index is outside the range of rows for
     *             <code>this</code> {@link Matrix matrix}.
     */
    V row(int row);

    /**
     * Returns the specified {@link Matrix matrix} column as a new {@link Vector
     * vector}.
     *
     * @param col
     *            A zero-based index specifying the desired {@link Matrix
     *            matrix} column.
     * @return A column-{@link Vector vector} with data from the specified
     *         column.
     * @throws IndexOutOfBoundsException
     *             If the column index is outside the range of columns for
     *             <code>this</code> {@link Matrix matrix}.
     */
    V column(int col);

    /**
     * Returns a new {@link Matrix matrix} with the product (i.e. concatenation)
     * of <code>this</code> {@link Matrix matrix} multiplied by the specified
     * {@link Matrix matrix}.
     *
     * @param m
     *            The {@link Matrix matrix} that will be concatenated to
     *            <code>this</code> {@link Matrix matrix}.
     * @return A new instance with the product of this multiplication.
     */
    @Override
    M mult(final M m);

    /**
     * Returns a new column-{@link Vector vector} with the product (i.e.
     * concatenation) of <code>this</code> {@link Matrix matrix} multiplied by
     * the specified column-{@link Vector vector}.
     *
     * @param v
     *            The {@link Vector vector} that will be concatenated to
     *            <code>this</code> {@link Matrix matrix}.
     * @return A new column-{@link Vector vector} with the product of this
     *         multiplication.
     */
    V mult(final V v);

    /**
     * Returns a new {@link Matrix matrix} with the product (i.e. concatenation)
     * of <code>this</code> {@link Matrix matrix} multiplied by the specified
     * scalar value.
     *
     * @param s
     *            The scalar value that will be multiplied by each value in
     *            <code>this</code> {@link Matrix matrix}.
     * @return A new {@link Matrix matrix} with the product of this
     *         multiplication.
     */
    @Override
    M mult(float s);

    /**
     * Returns the determinant of <code>this</code> {@link Matrix matrix}.
     * <p>
     * Note that if the determinant is zero, then <code>this</code>
     * {@link Matrix matrix} is singular and cannot be inverted.
     *
     * @return The determinant of <code>this</code> {@link Matrix matrix}.
     */
    float determinant();

}
