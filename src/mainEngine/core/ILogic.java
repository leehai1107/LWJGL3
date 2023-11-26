package mainEngine.core;

public interface ILogic {

    void init() throws Exception;
    void input();
    void update(float interval);
    void render();
    void cleanUp();
}
