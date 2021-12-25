package twodengine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import util.Time;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;;


public class Window
{
	private int width,height;
	private String title;
	private float r,g,b,a;
	
	private static Window window = null;	
	private long glfwWindow;
	private static Scene currentScene;
	
	
	private Window()
	{
		//this.width  = 1366;
		//his.hieght = 768;
		this.width = 1280;
		this.height  = 720;
		//this.hieght = 1920;
		//this.width  = 1080;
		this.title  = "Mario";
		this.r = 1;
		this.g = 1;
		this.b = 1;
		this.a = 1;
	}
	
	public static void changeScene(int newScene)
	{
		switch(newScene)
		{
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
				assert false : "Unknown Scene" + newScene;
		}
	}
	
	public static Window getWindow()
	{
		if(Window.window == null)
			Window.window = new Window();
		return Window.window;
	}
	
	public static void setRGBA(float[] rgba)
	{
		getWindow().r = rgba[0];
		getWindow().g = rgba[1];
		getWindow().b = rgba[2];
		getWindow().a = rgba[3];
	}
	
	public static float[] getRGBA()
	{
		float[] rgba = new float[4];
		rgba[0] =  getWindow().r;
		rgba[1] =  getWindow().g;
		rgba[2] =  getWindow().b;
		rgba[3] =  getWindow().a;
		return rgba;
	}
	
	public void run()
	{
		System.out.println("Hello LWJGL "+Version.getVersion());
		
		init();
		loop();
		
		glfwFreeCallbacks(glfwWindow); //is not available :(
		glfwDestroyWindow(glfwWindow);
		
		// Terminate glfw and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();;
		

	}
	
	public void init()
	{
		// Setup an error callback
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW
		if(!glfwInit())
		{
			throw new IllegalStateException("Unable to Initialize glfw");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		// Create the window
		glfwWindow = glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);
		
		if(glfwWindow == NULL)
		{
			throw new IllegalStateException("Failed to create the GLFW window.");
		}
		
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		glfwSetJoystickCallback(JoyStickListener::joyStickCallback);
		
		//Make the OpenGL context current
		glfwMakeContextCurrent(glfwWindow);
		
		//Enable v-sync
		glfwSwapInterval(1);
		
		//Make the window visible
		glfwShowWindow(glfwWindow);
		
		// This line is critical for LWJGL's inter operation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
		
		GL.createCapabilities();
		
		Window.changeScene(0);
	}
	
	public void loop()
	{
		float beginTime = Time.getTime();
		float endTime = Time.getTime();
		float dt = -1.0f;
		while(!glfwWindowShouldClose(glfwWindow))
		{
			//Poll events
			glfwPollEvents();
			
			glClearColor(r,g,b,a);
			glClear(GL_COLOR_BUFFER_BIT);
			
			if(dt>0)
				currentScene.update(dt);
			
			glfwSwapBuffers(glfwWindow);
			
			endTime = Time.getTime();
			dt = endTime - beginTime;
			beginTime = endTime;
			
		}
	}
}