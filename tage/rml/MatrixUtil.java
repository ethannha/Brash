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
 * Utility class with helper functions for matrices.
 *
 * @author Raymond L. Rivera
 *
 */
final class MatrixUtil {

    private MatrixUtil() {}

    /**
     * Converts a two-dimensional, column-major, square array into a one-dimensional
     * array, <i>without modifying</i> the source array.
     * <p>
     * For example,
     *
     * <pre>
     * // input
     * float[][] {
     *     { 1, 2 },
     *     { 3, 4 }
     * }
     *
     * // output
     * float[] { 1, 2, 3, 4 }
     * </pre>
     *
     * @param matrix
     *            A two-dimensional, square, column-major array.
     * @return A new one-dimensional column-major array.
     */
    public static float[] toFlatArray(final float[][] matrix) {
        assert matrix != null;

        final int dimensions = matrix.length;
        final float[] dest = new float[dimensions * dimensions];
        for (int c = 0; c < dimensions; ++c)
            for (int r = 0; r < dimensions; ++r)
                dest[c * dimensions + r] = matrix[c][r];

        return dest;
    }

    /**
     * Compares each entry in a {@link Matrix matrix} to determine whether both
     * {@link Matrix matrices} are equal or not.
     * <p>
     * {@link Matrix Matrices} are considered equal if all entries are within
     * machine epsilon of each other.
     *
     * @param lhs
     *            The left-hand side.
     * @param rhs
     *            The right-hand side.
     * @param dimensions
     *            The number of rows/columns both {@link Matrix matrices} have.
     * @return True if they're equal. Otherwise false.
     */
    public static boolean areEqual(final Matrix<?, ?> lhs, Matrix<?, ?> rhs, final int dimensions) {
        for (int i = 0; i < dimensions; ++i)
            for (int j = 0; j < dimensions; ++j)
                if (FloatUtil.compare(lhs.value(i, j), rhs.value(i, j)) != 0)
                    return false;

        return true;
    }

}
