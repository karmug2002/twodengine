package twodengine;

import java.util.ArrayList;
import java.util.List;

import renderer.Renderer;
import util.Time;

public abstract class Scene
{
	protected Renderer renderer = new Renderer();
	protected Camera camera;
	private boolean isRunning = false;
	protected List<GameObject> gameObjects = new ArrayList<>();
	private static  int average;
	private static int total;
	
	public Scene()
	{
		//this.gameObjects = new ArrayList<>();
	}
	
	public static void printCurrentFps(float dt)
	{
		//System.out.println(String.format("Current FPS: %.0f", (1.0f/dt)));
		average += (1/dt);
		total +=1;
		if(total == 10)
		{
			System.out.println(average/total +" "+ average +" " + total);
			total = 0;
			average = 0;
			System.out.println(Time.getTime());
		}
	}
	
	public static String getCurrentFps(float dt,boolean realTime,int average)
	{
		String CurrentFps = "CurrentFps: ";
		//System.out.println(String.format("Current FPS: %.0f", (1.0f/dt)));
		
		if(realTime)
		{
			CurrentFps = String.format("Current FPS: %.0f", (1.0f/dt));
		}
		else if(total == average)
		{
			System.out.println(total);
			
			
				//System.out.println(average/total +" "+ average +" " + total);
				CurrentFps = CurrentFps+ average/total;
				//System.out.println(Time.getTime());
				//System.out.println(String.format("Current FPS: %.0f", (1.0f/dt)));

			
			total = 0;
			average = 0;
		}
		else
		{
			average += (1/dt);
			total +=1;
		}
		
		return CurrentFps;
	}
	
	
	public abstract void update(float dt);
	protected abstract void loadResources();

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
