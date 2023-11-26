package mainEngine.core;

import mainEngine.core.utils.Consts;
import mainEngine.launcher.Launcher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANO_SECOND = 1000000000L;
    public static final float FRAME_RATE = 60;

    private static int fps;
    private static final float frameTime = 1.0f/ FRAME_RATE;

    private boolean isRunning;

    private WindowManager window;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;

    private void init() throws Exception{
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        gameLogic = Launcher.getGame();
        window.init();
        gameLogic.init();
    }

    public void start() throws Exception {
        init();
        if(isRunning)
            return;
        run();
    }

    public void run(){
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning){
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime/ (double) NANO_SECOND;
            frameCounter += passedTime;

           input();

            while (unprocessedTime > frameTime) {
                render = true;
                unprocessedTime -= frameTime;
                if(window.windowShouldClose())
                    stop();
                if(frameCounter >= NANO_SECOND) {
                    setFps(frames);
                    window.setTitle(Consts.TITLE +" FPS: "+ getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render){
                update();
                render();
                frames++;
            }
        }
        cleanUp();
    }

    private void stop(){
        if(!isRunning)
            return;
        isRunning = false;
    }

    private void input(){
        gameLogic.input();
    }

    private void render(){
        gameLogic.render();
        window.update();
    }

    private void update(){
        gameLogic.update(frameTime);
    }

    private void cleanUp(){
        window.cleanUp();
        gameLogic.cleanUp();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
