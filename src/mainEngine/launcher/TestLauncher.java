package mainEngine.launcher;

import mainEngine.core.*;
import mainEngine.core.Entities.*;
import mainEngine.core.Entities.camera.Camera;
import mainEngine.core.Entities.player.Player;
import mainEngine.core.Entities.terrain.Terrain;
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

public class TestLauncher implements ILogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private Camera camera;

    Vector3f cameraMovement;
    Player player;


    private ScenceManager scenceManager;

    public TestLauncher() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();

        cameraMovement = new Vector3f(0, 0, -90);
        scenceManager = new ScenceManager(-90);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/res/fern.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/fern.png")), 1f);

        model.getMaterial().setDisableCulling(true);

        Terrain terrain = new Terrain(new Vector3f(0, 1, -800), loader, new Material(new Texture(loader.loadTexture("textures/grass.png")), 0.1f));
        Terrain terrain1 = new Terrain(new Vector3f(-800, 1, -800), loader, new Material(new Texture(loader.loadTexture("textures/flower.png")), 0.1f));

        scenceManager.addTerrain(terrain);
        scenceManager.addTerrain(terrain1);

        Model playerModel = loader.loadOBJModel("/res/bunny.obj");
        playerModel.setTexture(new Texture(loader.loadTexture("textures/grass.png")), 1f);

        player = new Player(playerModel,new Vector3f(0, 2, -5f), new Vector3f(0, 0, 0), 1);

        Random rnd = new Random();
//        for (int i = 0; i < 200; i++) {
//            float x = rnd.nextFloat() * 800;
//            float z = rnd.nextFloat() * -800;
//            scenceManager.addEntity(new Entity(model, new Vector3f(x, 2, z),
//                    new Vector3f(rnd.nextFloat() * 180, rnd.nextFloat() * 180, 0),
//                    1));
//        }
//        scenceManager.addEntity(new Entity(model, new Vector3f(0, 2, -5f), new Vector3f(0, 0, 0), 1));
        scenceManager.addEntity(player);

        float lightIntensity = 1.0f;
        //point light
        Vector3f lightPosition = new Vector3f(-0.5f, -0.5f, -3.2f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        PointLight pointLight = new PointLight(lightColour, lightPosition, lightIntensity, 0, 0, 1);

        //spotlight
        Vector3f coneDir = new Vector3f(0, -50, 0);
        float cutoff = Math.cos(Math.toRadians(140));

        lightIntensity = 5000f;
        SpotLight spotLight = new SpotLight(new PointLight(new Vector3f(0.25f, 0f, 0f), new Vector3f(1f, 50f, -5f),
                lightIntensity, 0, 0, 0.2f), coneDir, cutoff);
        lightIntensity = 5000f;
        SpotLight spotLight1 = new SpotLight(new PointLight(new Vector3f(0f, 0.25f, 0f), new Vector3f(1f, 50f, -5f),
                lightIntensity, 0, 0, 0.2f), coneDir, cutoff);


        //directional light
        lightIntensity = 1f;

        lightPosition = new Vector3f(-1, -10, 0);
        lightColour = new Vector3f(1, 1, 1);
        scenceManager.setDirectionalLight(new DirectionalLight(lightColour, lightPosition, lightIntensity));
        scenceManager.setPointLights(new PointLight[]{pointLight});
        scenceManager.setSpotLights(new SpotLight[]{spotLight, spotLight1});
        camera.setPosition(0, 5, 0);
    }

    @Override
    public void input() {
//        if (window.isKeyPress(GLFW.GLFW_KEY_O))
//            pointLight.getPosition().x += 0.1f;
//        if (window.isKeyPress(GLFW.GLFW_KEY_P))
//            pointLight.getPosition().x -= 0.1f;

        float lightPos = scenceManager.getSpotLights()[0].getPointLight().getPosition().z;
        if (window.isKeyPress(GLFW.GLFW_KEY_N)) {
            scenceManager.getSpotLights()[0].getPointLight().getPosition().z = lightPos + 0.1f;
        }
        if (window.isKeyPress(GLFW.GLFW_KEY_M)) {
            scenceManager.getSpotLights()[0].getPointLight().getPosition().z = lightPos - 0.1f;
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        camera.movePosition(window);
        player.move(window);
        player.incRotation(0.0f,0.25f,0.0f);

        if (mouseInput.isLeftButtonPress()) {

            Vector2f rotVec = mouseInput.getDisplayVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVE, rotVec.y * Consts.MOUSE_SENSITIVE, 0);
        }

//        entity.incRotation(0.0f,0.25f,0.0f);

        scenceManager.incSpotAngle(0.15f);

        if (scenceManager.getSpotAngle() > 4)
            scenceManager.setSpotInc(-1);
        else if (scenceManager.getSpotAngle() <= -4)
            scenceManager.setSpotInc(1);

        double spotAngleRad = Math.toRadians(scenceManager.getSpotAngle());
        Vector3f coneDir = scenceManager.getSpotLights()[0].getPointLight().getPosition();
        coneDir.y = (float) Math.sin(spotAngleRad);


        coneDir = scenceManager.getSpotLights()[1].getPointLight().getPosition();
        coneDir.x = (float) Math.cos(spotAngleRad);

        scenceManager.incLightAngle(1.1f);

        scenceManager.setLightAngle(scenceManager.getLightAngle() + 1f);
        if (scenceManager.getLightAngle() > 90) {
            scenceManager.getDirectionalLight().setIntensity(0);
            if (scenceManager.getLightAngle() >= 360)
                scenceManager.setLightAngle(-90);
        } else if (scenceManager.getLightAngle() <= -80 || scenceManager.getLightAngle() >= 80) {
            float factor = 1 - (float) (Math.abs(scenceManager.getLightAngle()) - 80) / 10.0f;
            scenceManager.getDirectionalLight().setIntensity(factor);
            scenceManager.getDirectionalLight().getColour().y = Math.max(factor, 0.9f);
            scenceManager.getDirectionalLight().getColour().z = Math.max(factor, 0.5f);
        } else {
            scenceManager.getDirectionalLight().setIntensity(1);
            scenceManager.getDirectionalLight().getColour().x = 1;
            scenceManager.getDirectionalLight().getColour().y = 1;
            scenceManager.getDirectionalLight().getColour().z = 1;
        }

        double angRad = Math.toRadians(scenceManager.getLightAngle());
        scenceManager.getDirectionalLight().getDirection().x = (float) Math.sin(angRad);
        scenceManager.getDirectionalLight().getDirection().y = (float) Math.cos(angRad);


        for (Entity entity : scenceManager.getEntities()) {
            renderer.processEntity(entity);
        }
        for (Terrain terrain : scenceManager.getTerrains()) {
            renderer.processTerrain(terrain);
        }

    }

    @Override
    public void render() {
        renderer.render(camera, scenceManager);
    }

    @Override
    public void cleanUp() {
        renderer.cleanUp();
        loader.cleanUp();
    }
}
