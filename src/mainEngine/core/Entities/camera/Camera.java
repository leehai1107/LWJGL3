package mainEngine.core.Entities.camera;

import mainEngine.core.WindowManager;
import mainEngine.core.utils.Consts;
import org.joml.Math;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
    private Vector3f position, rotation;

    public Camera() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }


    public void movePosition(WindowManager window) {
        Vector3f cameraPos = cameraInput(window);
        if (cameraPos.z != 0) {
            position.x += Math.sin(Math.toRadians(rotation.y)) * -1.0f * cameraPos.z * Consts.CAMERA_MOVE_SPEED;
            position.z += Math.cos(Math.toRadians(rotation.y)) * cameraPos.z * Consts.CAMERA_MOVE_SPEED;
        }
        if (cameraPos.x != 0) {
            position.x += Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * cameraPos.x * Consts.CAMERA_MOVE_SPEED;
            position.z += Math.cos(Math.toRadians(rotation.y - 90)) * cameraPos.x * Consts.CAMERA_MOVE_SPEED;
        }
        position.y += cameraPos.y * Consts.CAMERA_MOVE_SPEED;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void moveRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    private Vector3f cameraInput(WindowManager window) {
        Vector3f cameraInc = new Vector3f(0, 0, -90);

        cameraInc.set(0, 0, 0);

        if (window.isKeyPress(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if (window.isKeyPress(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;

        if (window.isKeyPress(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if (window.isKeyPress(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;

        if (window.isKeyPress(GLFW.GLFW_KEY_Z))
            cameraInc.y = -1;
        if (window.isKeyPress(GLFW.GLFW_KEY_X))
            cameraInc.y = 1;

        return cameraInc;
    }
}
