package client;

import tage.Utils;
import tage.shapes.Plane;

public class WorldPlane extends Plane
{
    public WorldPlane()
    {
        super();

        setMatAmb(Utils.silverAmbient());
        setMatDif(Utils.silverDiffuse());
        setMatSpe(Utils.silverSpecular());
        setMatShi(Utils.silverShininess());
    }
}
