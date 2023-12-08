package mainEngine.core.rendering;

import mainEngine.core.Camera;
import mainEngine.core.Entities.Model;
import mainEngine.core.lighting.DirectionalLight;
import mainEngine.core.lighting.PointLight;
import mainEngine.core.lighting.SpotLight;

public interface IRenderer<T> {
    public void init() throws Exception;
    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights,
                       DirectionalLight directionalLight);
    abstract void bind(Model model);
    public void unbind();
    public void prepare(T t, Camera camera);
    public void cleanUp();
}
