package tage.physics.JBullet;

public class JBulletUtils {
	
    public static float[] double_to_float_array(double[] array)
    {
        float[] float_array = new float[array.length];
        for(int i=0; i<array.length; i++)
        {
            float_array[i] = (float) array[i];
        }
        return float_array;
    }
    public static void row_to_column_major(double[] array)
    {

    }
    public static double[] float_to_double_array(float[] array)
    {
        double[] double_array = new double[array.length];
        for(int i=0; i<array.length; i++)
        {
            double_array[i] = (double) array[i];
        }
        return double_array;
    }

}
