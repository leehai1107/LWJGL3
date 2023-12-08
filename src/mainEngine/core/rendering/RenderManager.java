package mainEngine.core.rendering;

import mainEngine.core.Camera;
import mainEngine.core.Entities.Entity;
import mainEngine.core.Entities.Model;
import mainEngine.core.ShaderManager;
import mainEngine.core.WindowManager;
import mainEngine.core.lighting.DirectionalLight;
import mainEngine.core.lighting.PointLight;
import mainEngine.core.lighting.SpotLight;
import mainEngine.core.utils.Consts;
import mainEngine.core.utils.Transformation;
import mainEngine.core.utils.Utils;
import mainEngine.launcher.Launcher;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderManager {

    private final WindowManager window;

    private EntityRender entityRender;

    public RenderManager() {
        window = Launcher.getWindow();
    }

    public void init() throws Exception {
        entityRender = new EntityRender();

        entityRender.init();

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

    public void render(Camera camera, DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights) {
        clear();

        if (window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        entityRender.render(camera, pointLights, spotLights, directionalLight);


    }

    public void processEntity(Entity entity) {
        List<Entity> entityList = entityRender.getEntities().get(entity.getModel());
        if (entityList != null)
            entityList.add(entity);
        else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRender.getEntities().put(entity.getModel(), newEntityList);
        }
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanUp() {
        entityRender.cleanUp();
    }
}
