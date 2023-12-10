package mainEngine.core.Entities. player;

import mainEngine.core.EngineManager;
import mainEngine.core.Entities.Entity;
import mainEngine.core.Entities.Model;
import mainEngine.core.WindowManager;
import mainEngine.core.utils.Consts;
import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Player extends Entity {

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    public Player(Model model, Vector3f pos, Vector3f rotation, float scale) {
        super(model, pos, rotation, scale);
    }

    public void move(WindowManager window) {
        checkInput(window);
        super.incRotation(0,currentTurnSpeed* EngineManager.getFrameTime(),0);
        float distance = currentSpeed * EngineManager.getFrameTime();

        float dx = (float) (distance* Math.sin(Math.toRadians(super.getRotation().y)));
        float dz = (float) (distance* Math.cos(Math.toRadians(super.getRotation().y)));
        super.incPos(dx,0,dz);

    }

    public void checkInput(WindowManager window) {
        if (window.isKeyPress(GLFW.GLFW_KEY_UP))
            this.currentSpeed = Consts.PLAYER_RUN_SPEED;
        else if (window.isKeyPress(GLFW.GLFW_KEY_DOWN))
            this.currentSpeed = -Consts.PLAYER_RUN_SPEED;
        else
            this.currentSpeed = 0;

        if (window.isKeyPress(GLFW.GLFW_KEY_LEFT))
            this.currentTurnSpeed = -Consts.PLAYER_TURN_SPEED;
        else if (window.isKeyPress(GLFW.GLFW_KEY_RIGHT))
            this.currentTurnSpeed = Consts.PLAYER_TURN_SPEED;
        else
            this.currentTurnSpeed = 0;
    }


}
