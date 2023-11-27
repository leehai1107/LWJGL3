package mainEngine.launcher;

import mainEngine.core.EngineManager;
import mainEngine.core.WindowManager;
import mainEngine.core.utils.Consts;

public class Launcher {

    private static WindowManager window;
    private static TestLauncher game;

    public static void main(String[] args) {
        window = new WindowManager(Consts.TITLE, Consts.WIDTH, Consts.HEIGHT, true);
        game = new TestLauncher();
        EngineManager engine = new EngineManager();

        try {
            engine.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestLauncher getGame() {
        return game;
    }
}
