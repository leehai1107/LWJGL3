package mainEngine.core.rendering;

import mainEngine.core.Camera;
import mainEngine.core.Entities.Entity;
import mainEngine.core.Entities.ScenceManager;
import mainEngine.core.Entities.terrain.Terrain;
import mainEngine.core.ShaderManager;
import mainEngine.core.WindowManager;
import mainEngine.core.lighting.DirectionalLight;
import mainEngine.core.lighting.PointLight;
import mainEngine.core.lighting.SpotLight;
import mainEngine.core.utils.Consts;
import mainEngine.launcher.Launcher;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {

    private final WindowManager window;

    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;

    public RenderManager() {
        window = Launcher.getWindow();
    }

    public void init() throws Exception {
        entityRenderer = new EntityRenderer();
        terrainRenderer = new TerrainRenderer();
        entityRenderer.init();
        terrainRenderer.init();
    }

    public static void renderLights(PointLight[] pointLights, SpotLight[] spotLights,
                                    DirectionalLight directionalLight, ShaderManager shader) {
        shader.setUniform("ambientLight", Consts.AMBIENT_LIGHT);
        shader.setUniform("specularPower", Consts.SPECULAR_POWER);

        int numLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            shader.setUniform("spotLights", spotLights[i], i);
        }
        numLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numLights; i++) {
            shader.setUniform("pointLights", pointLights[i], i);
        }
        shader.setUniform("directionalLight", directionalLight);

    }

    public void render(Camera camera, ScenceManager scenceManager) {
        clear();

        if (window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        entityRenderer.render(camera, scenceManager.getPointLights(), scenceManager.getSpotLights(), scenceManager.getDirectionalLight());
        terrainRenderer.render(camera,scenceManager.getPointLights(), scenceManager.getSpotLights(), scenceManager.getDirectionalLight());

    }

    public void processEntity(Entity entity) {
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if (entityList != null)
            entityList.add(entity);
        else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }
    }

    public void processTerrain(Terrain terrain) {
        terrainRenderer.getTerrains().add(terrain);
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanUp() {
        entityRenderer.cleanUp();
        terrainRenderer.cleanUp();
    }
}
