package tage;
import org.joml.Vector3f;

import net.java.games.input.Event;
import tage.Camera;
import tage.Engine;
import tage.GameObject;
import tage.input.InputManager;
import tage.input.IInputManager.INPUT_ACTION_TYPE;
import tage.input.action.AbstractInputAction;
import java.lang.Math;

public class CameraOverviewController 
{
    private Engine engine;
    private Camera camera;                 // The camera being controlled
    private GameObject avatar;          // The target avatar the camera looks at
    private float cameraTranslationX;        // Rotation around the target Y-axis
    private float cameraTranslationZ;      // Elevation of camera above target
    private float cameraRadius;         // Distance between camera and target
    
    public CameraOverviewController(Camera cam, GameObject av, String gpName, Engine e)
    {
        engine = e;
        camera = cam;
        avatar = av;
        cameraTranslationX = 0.0f;
        cameraTranslationZ = 0.0f;
        cameraRadius = 4.0f;
        setUpInputs(gpName);
        updateOverviewPosition();
    }

    private void setUpInputs(String gp)
    {
        TranslateX translateX = new TranslateX();
        OrbitRadiusAction radiusAction = new OrbitRadiusAction();
        TranslateZ translateZ = new TranslateZ();
        InputManager im = engine.getInputManager();

        TranslateXOpp translateXOpp = new TranslateXOpp();
        OrbitRadiusActionOpposite radiusActionOppo = new OrbitRadiusActionOpposite();
        TranslateZOpp translateZOpp = new TranslateZOpp();

        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.I, translateZOpp, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.K, translateZ, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.L, translateX, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.J, translateXOpp, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.RBRACKET, radiusActionOppo, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);  
        im.associateActionWithAllKeyboards(net.java.games.input.Component.Identifier.Key.LBRACKET, radiusAction, INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);  
    }

    public void updateOverviewPosition()
    {
        float x = cameraTranslationX;
        float y = cameraRadius;
        float z = cameraTranslationZ;
        camera.setLocation(new Vector3f(x, y, z));
    }

    private class OrbitRadiusAction extends AbstractInputAction
    {
        @Override
        public void performAction(float time, Event evt) 
        {
            float rotAmount;
            if (evt.getValue() < -0.2)
            {
                rotAmount = -0.5f;
                if (cameraRadius <= 1.0f)
                {
                    rotAmount = 0.0f;
                }
            }
            else
            {
                if (evt.getValue() > 0.2)
                {
                    rotAmount = 0.5f;
                }
                else
                {
                    rotAmount = 0.0f;
                }
            }
            cameraRadius += rotAmount;
            cameraRadius = cameraRadius % 360;
            updateOverviewPosition();
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
                rotAmount = -0.5f;
                if (cameraRadius <= 1.0f)
                {
                    rotAmount = 0.0f;
                }
            }
            else
            {
                rotAmount = 0.0f;
            }
            cameraRadius += rotAmount;
            cameraRadius = cameraRadius % 360;
            updateOverviewPosition();
        } 
    }

    private class TranslateX extends AbstractInputAction
    {
        @Override
        public void performAction(float time, Event evt) 
        {
            float rotAmount;
            if (evt.getValue() < -0.2)
            {
                rotAmount = -0.5f;
            }
            else
            {
                if (evt.getValue() > 0.2)
                {
                    rotAmount = 0.5f;
                }
                else
                {
                    rotAmount = 0.0f;
                }
            }
            cameraTranslationX += rotAmount;
            updateOverviewPosition();
        }
    }

    private class TranslateXOpp extends AbstractInputAction
    {
        @Override
        public void performAction(float time, Event evt) 
        {
            float rotAmount;
            if (evt.getValue() > 0.2)
            {
                rotAmount = -0.5f;
            }
            else
            {
                rotAmount = 0.0f;
            }
            cameraTranslationX += rotAmount;
            updateOverviewPosition();
        }
    }

    private class TranslateZ extends AbstractInputAction
    {

        @Override
        public void performAction(float time, Event evt) 
        {
            float eleAmount;
            if (evt.getValue() < -0.2)
            {
                eleAmount = -0.5f;
            }
            else
            {
                if (evt.getValue() > 0.2)
                {
                    eleAmount = 0.5f;
                }
                else
                {
                    eleAmount = 0.0f;
                }
            }
            cameraTranslationZ += eleAmount;
            updateOverviewPosition();
        } 
    }

    private class TranslateZOpp extends AbstractInputAction
    {

        @Override
        public void performAction(float time, Event evt) 
        {
            float eleAmount;
            if (evt.getValue() > 0.2)
            {
                eleAmount = -0.5f;
            }
            else
            {
                eleAmount = 0.0f;
            }
            cameraTranslationZ += eleAmount;
            updateOverviewPosition();
        } 
    }
}
