package twodengine;


import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import imgui.ImGui;
import imgui.ImGuiIO;

import imgui.gl3.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowImGui {
    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imguiLayer;
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();


    public float r, g, b, a;
    private boolean fadeToBlack = false;

    private static WindowImGui window = null;

    private static Scene currentScene;

    
    private WindowImGui()
    {
    	   this.width = 1920;
           this.height = 1080;
           this.title = "Mario";
           r = 1;
           b = 1;
           g = 1;
           a = 1;
    }
    
    WindowImGui(long window) {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r = 1;
        b = 1;
        g = 1;
        a = 1;
        this.glfwWindow = window;
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static WindowImGui get() {
        if (WindowImGui.window == null) {
            WindowImGui.window = new WindowImGui();
        }

        return WindowImGui.window;
    }

    public static Scene getScene() {
        //get();
		return WindowImGui.currentScene;
    }

    public void run(float dt) {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop(dt);

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        //if (!glfwInit()) {
            //throw new IllegalStateException("Unable to initialize GLFW.");
        //}

        // Configure GLFW
        //long monitor = glfwGetPrimaryMonitor();
        //GLFWVidMode mode = glfwGetVideoMode(monitor);
		//glfwDefaultWindowHints();
		//glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		//glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		//glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		// Create the window
		//glfwWindow = glfwCreateWindow(mode.width(),mode.height(),this.title,NULL,NULL);	
		if(glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create the GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            WindowImGui.setWidth(newWidth);
            WindowImGui.setHeight(newHeight);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        WindowImGui.changeScene(0);
        currentScene = new LevelEditorScene();
       currentScene.init();
       currentScene.start();
    }
    
    public void loop(float td) {
    	
  
       
            // Poll events
            glfwPollEvents();
           
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (td >= 0) {
                currentScene.update(td);
            }

            //this.imguiLayer.update(dt);
            glfwSwapBuffers(glfwWindow);

            
            //final ImGuiIO io = ImGui.getIO();
            //io.setDeltaTime(td);
    }

    public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }
}