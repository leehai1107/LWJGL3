package mainEngine.launcher;

import mainEngine.core.*;
import mainEngine.core.Entities.Entity;
import mainEngine.core.Entities.Model;
import mainEngine.core.Entities.Texture;
import mainEngine.core.Loader.ObjectLoader;
import mainEngine.core.lighting.DirectionalLight;
import mainEngine.core.lighting.PointLight;
import mainEngine.core.lighting.SpotLight;
import mainEngine.core.rendering.RenderManager;
import mainEngine.core.utils.Consts;
import org.joml.Math;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class TestLauncher implements ILogic {

    private static final float CAMERA_MOVE_SPEED = 0.025f;
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private List<Entity> entities;
    private Camera camera;

    Vector3f cameraInc;

    private float lightAngle, spotAngle = 0, spotInc = 1;
    private DirectionalLight directionalLight;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;

    public TestLauncher() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, -90);
        lightAngle = -90;
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/res/cube.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")), 1f);

        entities = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0;i<200;i++) {
            float x = rnd.nextFloat() * 100 -50;
            float y = rnd.nextFloat() * 100 -50;
            float z = rnd.nextFloat() * -300;
            entities.add(new Entity(model, new Vector3f(x,y,z),
                    new Vector3f(rnd.nextFloat()*180,rnd.nextFloat() *180,0),
                    1));
        }
        entities.add(new Entity(model,new Vector3f(0,0,-2f),new Vector3f(0,0,0),1));

        float lightIntensity = 1.0f;
        //point light
        Vector3f lightPosition = new Vector3f(-0.5f, -0.5f, -3.2f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity, 0, 0, 1);

        //spotlight
        Vector3f coneDir = new Vector3f(0, 0, 0);
        float cutoff = Math.cos(Math.toRadians(180));
        SpotLight spotLight = new SpotLight(new PointLight(lightColour, new Vector3f(0, 0, -3.6f),
                lightIntensity, 0, 0, 0.2f), coneDir, cutoff);

        SpotLight spotLight1 = new SpotLight(new PointLight(lightColour, lightPosition,
                lightIntensity, 0, 0, 1), coneDir, cutoff);
        spotLight1.getPointLight().setPosition(new Vector3f(0.5f, 0.5f, -3.6f));


        //directional light
        lightPosition = new Vector3f(-1, -10, 0);
        lightColour = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);

        pointLights = new PointLight[]{pointLight};
        spotLights = new SpotLight[]{spotLight, spotLight1};
    }

    @Override
    public void input() {
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

//        if (window.isKeyPress(GLFW.GLFW_KEY_O))
//            pointLight.getPosition().x += 0.1f;
//        if (window.isKeyPress(GLFW.GLFW_KEY_P))
//            pointLight.getPosition().x -= 0.1f;

        float lightPos = spotLights[0].getPointLight().getPosition().z;
        if (window.isKeyPress(GLFW.GLFW_KEY_N)) {
            spotLights[0].getPointLight().getPosition().z = lightPos + 0.1f;
        }
        if (window.isKeyPress(GLFW.GLFW_KEY_M)) {
            spotLights[0].getPointLight().getPosition().z = lightPos - 0.1f;
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED,
                cameraInc.y * CAMERA_MOVE_SPEED,
                cameraInc.z * CAMERA_MOVE_SPEED);

        if (mouseInput.isLeftButtonPress()) {

            Vector2f rotVec = mouseInput.getDisplayVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVE, rotVec.y * Consts.MOUSE_SENSITIVE, 0);
        }

//        entity.incRotation(0.0f,0.25f,0.0f);

        spotAngle += spotInc * 0.05f;
        if (spotAngle > 4)
            spotInc = -1;
        else if (spotAngle <= -4)
            spotInc = 1;

        double spotAngleRad = Math.toRadians(spotAngle);
        Vector3f coneDir = spotLights[0].getPointLight().getPosition();
        coneDir.y = (float) Math.sin(spotAngleRad);

        lightAngle += 0.5f;
        if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360)
                lightAngle = -90;
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColour().y = Math.max(factor, 0.9f);
            directionalLight.getColour().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
            directionalLight.getColour().x = 1;
            directionalLight.getColour().y = 1;
            directionalLight.getColour().z = 1;
        }

        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);

        for(Entity entity : entities) {
            renderer.processEntity(entity);
        }

    }

    @Override
    public void render() {
        renderer.render(camera, directionalLight, pointLights, spotLights);
    }

    @Override
    public void cleanUp() {
        renderer.cleanUp();
        loader.cleanUp();
    }
}
