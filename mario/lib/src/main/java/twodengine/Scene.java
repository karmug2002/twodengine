package twodengine;

import java.util.ArrayList;
import java.util.List;

import renderer.Renderer;

public abstract class Scene
{
	protected Renderer renderer = new Renderer();
	protected Camera camera;
	private boolean isRunning = false;
	protected List<GameObject> gameObjects = new ArrayList<>();
	
	public Scene()
	{
		//this.gameObjects = new ArrayList<>();
	}
	
	public static void printCurrentFps(float dt)
	{
		System.out.println(String.format("Current FPS: %.0f", (1.0f/dt)));
	}
	
	public abstract void update(float dt);

	public void init()
	{
		
	}
	
	public void start()
	{
		for(GameObject go : gameObjects)
		{
			go.start();
			this.renderer.add(go);
		}
		isRunning = true;
	}
	
	public void addGameObjectToScene(GameObject go)
	{
		if(!isRunning)
		{
			gameObjects.add(go);
		}
		else 
		{
			gameObjects.add(go);
			go.start();
			this.renderer.add(go);
		}
	}
	
	public Camera getCamera()
	{
		return this.camera;
	}

}
