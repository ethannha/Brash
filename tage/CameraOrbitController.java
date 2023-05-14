package tage;
import org.joml.Vector3f;

import client.MyGame;
import net.java.games.input.Event;
import tage.Camera;
import tage.Engine;
import tage.GameObject;
import tage.input.InputManager;
import tage.input.IInputManager.INPUT_ACTION_TYPE;
import tage.input.action.AbstractInputAction;
import java.lang.Math;

/**
 * This is a class method that controls a camera orbitting around a gameobject
 */
public class CameraOrbitController 
{
    private MyGame game;
    private Engine engine;
    private Camera camera;                 // The camera being controlled
    private GameObject avatar;          // The target avatar the camera looks at
    private float cameraAzimuth;        // Rotation around the target Y-axis
    private float cameraElevation;      // Elevation of camera above target
    private float cameraRadius;         // Distance between camera and target
    private float speedIncrement;
    
    public CameraOrbitController(Camera cam, GameObject av, String gpName, Engine e, MyGame g)
    {
        game = g;
        engine = e;
        camera = cam;
        avatar = av;
        cameraAzimuth = 0.0f;
        cameraElevation = 30.0f;
        cameraRadius = 3.0f;
        speedIncrement = 40.0f;

        setUpInputs(gpName);
        updateCameraPosition();
    }

    /**
     * Basic inputs of how this camera orbits
     * @param gp
     */
    private void setUpInputs(String gp)
    {
        OrbitAzimuthAction azmAction = new OrbitAzimuthAction();
        OrbitRadiusAction radiusAction = new OrbitRadiusAction();
        OrbitElevationAction eleAction = new OrbitElevationAction();
        InputManager im = engine.getInputManager();

        OrbitAzimuthActionOpposite azmActionOppo = new OrbitAzimuthActionOpposite();
        OrbitRadiusActionOpposite radiusActionOppo = new OrbitRadiusActionOpposite();
        OrbitElevationActionOpposite eleActionOppo = new OrbitElevationActionOpposite();

        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.UP, eleAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.DOWN, eleActionOppo, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.RIGHT, azmAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.LEFT, azmActionOppo, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.PAGEUP, radiusAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);  
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.PAGEDOWN, radiusActionOppo, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);  

        if (gp != null)
        {
            im.associateAction(gp, net.java.games.input.Component.Identifier.Axis.Y, eleAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
            im.associateAction(gp, net.java.games.input.Component.Identifier.Axis.RX, azmAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
            im.associateAction(gp, net.java.games.input.Component.Identifier.Axis.RY, radiusAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        }
    }

    /**
     * Compute the camera's azimuth, elevation, and distance, relative to
     * the target in spherical coordinates, then convert to world Cartesian
     * coordinates and set the camera position from that
     */
    public void updateCameraPosition()
    {

        Vector3f avatarRot = avatar.getWorldForwardVector();
        double avatarAngle = Math.toDegrees((double)avatarRot.angleSigned(new Vector3f(0.0f, 0.0f, -1.0f), new Vector3f(0.0f, 1.0f, 0.0f)));
        float totalAz = cameraAzimuth - (float)avatarAngle;
        double theta = Math.toRadians(cameraAzimuth);
        double phi = Math.toRadians(cameraElevation);
        float x = cameraRadius * (float)(Math.cos(phi) * Math.sin(theta));
        float y = cameraRadius * (float)Math.sin(phi);
        float z = cameraRadius * (float)(Math.cos(phi) * Math.cos(theta));

        Vector3f avatarLoc = new Vector3f(avatar.getWorldLocation());
        camera.setLocation(new Vector3f(x, y, z).add(avatarLoc));
        camera.lookAt(avatarLoc.x, avatarLoc.y + 0.8f, avatarLoc.z);
    }

    private class OrbitAzimuthAction extends AbstractInputAction
    {
        @Override
        public void performAction(float time, Event evt) 
        {
            float rotAmount;
            if (evt.getValue() < -0.2)
            {
                rotAmount = -(speedIncrement * game.getElapseTime());
            }
            else if (evt.getValue() > 0.2)
            {
                rotAmount = (speedIncrement * game.getElapseTime());
            }
            else
            {
                rotAmount = 0.0f;
            }
            cameraAzimuth += rotAmount;
            cameraAzimuth = cameraAzimuth % 360;
            updateCameraPosition();
        }
    }

    private class OrbitAzimuthActionOpposite extends AbstractInputAction
    {
        @Override
        public void performAction(float time, Event evt) 
        {
            float rotAmount;
            if (evt.getValue() > 0.2)
            {
                rotAmount = -(speedIncrement * game.getElapseTime());
            }
            else
            {
                rotAmount = 0.0f;
            }
            cameraAzimuth += rotAmount;
            cameraAzimuth = cameraAzimuth % 360;
            updateCameraPosition();
        }
    }
    
    private class OrbitRadiusAction extends AbstractInputAction
    {
        @Override
        public void performAction(float time, Event evt) 
        {
            float rotAmount;
            if (evt.getValue() < -0.2)
            {
                rotAmount = -(speedIncrement * game.getElapseTime())/8;
            }
            else if (evt.getValue() > 0.2)
            {
                rotAmount = (speedIncrement * game.getElapseTime())/8;
            }
            else
            {
                rotAmount = 0.0f;
            }
            
            if(cameraRadius + rotAmount > 2.0f && cameraRadius + rotAmount < 10.0f)
            {
                cameraRadius += rotAmount;
            }
            cameraRadius = cameraRadius % 360;
            updateCameraPosition();
        } 
    }

    private class OrbitRadiusActionOpposite extends AbstractInputAction
    {
        @Override
        public void performAction(float time, Event evt) 
        {
            float rotAmount;
            if (evt.getValue() > 0.2)
            {
                rotAmount = -(speedIncrement * game.getElapseTime())/8;
            }
            else
            {
                rotAmount = 0.0f;
            }

            if(cameraRadius + rotAmount > 2.0f && cameraRadius + rotAmount < 10.0f)
            {
                cameraRadius += rotAmount;
            }
            cameraRadius = cameraRadius % 360;
            updateCameraPosition();
        } 
    }

    private class OrbitElevationAction extends AbstractInputAction
    {

        @Override
        public void performAction(float time, Event evt) 
        {
            
            float eleAmount;
            
            if (evt.getValue() < -0.2)
            {
                eleAmount = -(speedIncrement * game.getElapseTime());
            }
            else if (evt.getValue() > 0.2)
            {
                eleAmount = (speedIncrement * game.getElapseTime());
            }
            else
            {
                eleAmount = 0.0f;
            }

            if(cameraElevation + eleAmount > 10.0f && cameraElevation + eleAmount < 80.0f)
            {
                cameraElevation += eleAmount;
            }
            cameraRadius = cameraRadius % 360;
            updateCameraPosition();
        } 
    }

    private class OrbitElevationActionOpposite extends AbstractInputAction
    {

        @Override
        public void performAction(float time, Event evt) 
        {
            float eleAmount;
            if (evt.getValue() > 0.2)
            {
                eleAmount = -(speedIncrement * game.getElapseTime());
            }
            else
            {
                eleAmount = 0.0f;
            }
            
            if(cameraElevation + eleAmount > 10.0f && cameraElevation + eleAmount < 80.0f)
            {
                cameraElevation += eleAmount;
            }
            cameraRadius = cameraRadius % 360;
            updateCameraPosition();
        } 
    }
}
