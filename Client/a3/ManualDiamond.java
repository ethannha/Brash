package a3;

import tage.*;
import tage.shapes.*;
import org.joml.*;

public class ManualDiamond extends ManualObject
{
    private Vector3f[] vertices = new Vector3f[6];
    private Vector2f[] texCoords = new Vector2f[6];
    private Vector3f[] normals = new Vector3f[6];
    private int[] indices = new int[]
    {
        0, 1, 2,
        0, 1, 4,
        0, 2, 3,
        0, 3, 4,
        1, 2, 3,
        1, 4, 3,
        5, 1, 2,
        5, 1, 4,
        5, 2, 3,
        5, 3, 4
    };

    public ManualDiamond()
    {
        super();

        vertices[0] = (new Vector3f()).set(0.0f, 1.0f, 0.0f);
        vertices[1] = (new Vector3f()).set(-1.0f, -1.0f, 1.0f);
        vertices[2] = (new Vector3f()).set(1.0f, -1.0f, 1.0f);
        vertices[3] = (new Vector3f()).set(1.0f, -1.0f, -1.0f);
        vertices[4] = (new Vector3f()).set(-1.0f, -1.0f, -1.0f);
        vertices[5] = (new Vector3f()).set(0.0f, -3.0f, 0.0f);

        texCoords[0] = (new Vector2f()).set(0.0f, 0.0f);
        texCoords[1] = (new Vector2f()).set(0.5f, 1.0f);
        texCoords[2] = (new Vector2f()).set(0.5f, 0.0f);
        texCoords[3] = (new Vector2f()).set(1.0f, 1.0f);
        texCoords[4] = (new Vector2f()).set(1.0f, 1.0f);
        texCoords[5] = (new Vector2f()).set(0.0f, 0.0f);

        normals[0] = (new Vector3f()).set(0.0f, 0.0f, 1.0f);
        normals[1] = (new Vector3f()).set(0.0f, 0.0f, 1.0f);
        normals[2] = (new Vector3f()).set(0.0f, 0.0f, 1.0f);
        normals[3] = (new Vector3f()).set(0.0f, 0.0f, 1.0f);
        normals[4] = (new Vector3f()).set(0.0f, 0.0f, 1.0f);
        normals[5] = (new Vector3f()).set(0.0f, 0.0f, 1.0f);

        setNumVertices(30);
        setVerticesIndexed(indices, vertices);
        setTexCoordsIndexed(indices, texCoords);
        setNormalsIndexed(indices, normals);
        
        setMatAmb(Utils.silverAmbient());
        setMatDif(Utils.silverDiffuse());
        setMatSpe(Utils.silverSpecular());
        setMatShi(Utils.silverShininess());
    }
}
