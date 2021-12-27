package twodengine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import util.Time;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;;


public class Window
{
	public int width,height;
	private String title;
	//private float r,g,b,a;
	private Vector4f clearColor;
	
	private static Window window = null;	
	private long glfwWindow;
	private static Scene currentScene;
	
	
	private Window()
	{
		this.width  = 1366;
		this.height = 768;
		//this.width = 1280;
		//this.height  = 720;
		//this.width = 1920;
		//this.height = 1080;
		this.title  = "Mario";
		clearColor = new Vector4f(1,1,1,1);
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
	
	public static Scene getScene()
	{
		return get().currentScene;
	}
	
	public static Window get()
	{
		if(Window.window == null)
			Window.window = new Window();
		return Window.window;
	}
	
	public static void setRGBA(Vector4f clearColor)
	{
		get().clearColor = clearColor;
	}
	
	public static Vector4f getRGBA()
	{
		return get().clearColor;
	}
	
	public Vector2f getWindowHW()
	{
		int[] width = new int[1];
		int[] height = new int[1];
		glfwGetWindowSize(glfwWindow, width, height);
		return new Vector2f(width[0], height[0]);
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
		glfwSetErrorCallback(null).free();
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
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		// Create the window
		glfwWindow = glfwCreateWindow(mode.width(),mode.height(),this.title,NULL,NULL);	
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
		//float beginTime = Time.getTime();
		//float endTime = Time.getTime();
		float beginTime = (float) glfwGetTime();
		float endTime = (float) glfwGetTime();
		float dt = -1.0f;
		while(!glfwWindowShouldClose(glfwWindow))
		{
			//Poll events
			glfwPollEvents();
			
			//glClearColor(clearColor.x,clearColor.y,clearColor.w,clearColor.z);
			glClearColor(1, 1, 1, 1);
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