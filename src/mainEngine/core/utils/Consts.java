package mainEngine.core.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Consts {

    public static final String TITLE = "MyGameEngine";
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;
    public static final float MOUSE_SENSITIVE = 0.2f;
    public static final float SPECULAR_POWER = 10f;
    public static final float CAMERA_MOVE_SPEED = 0.2f;
    public static final int MAX_SPOT_LIGHTS = 5;
    public static final int MAX_POINT_LIGHTS = 5;

    public static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    public static final Vector3f AMBIENT_LIGHT = new Vector3f(0.5f, 0.5f, 0.5f);

    public static final float PLAYER_RUN_SPEED = 20;
    public static final float PLAYER_TURN_SPEED = 160;


}
