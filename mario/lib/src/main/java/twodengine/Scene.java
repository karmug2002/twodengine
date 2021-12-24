package twodengine;

public abstract class Scene
{

	public Scene()
	{
	}
	
	public static void printCurrentFps(float dt)
	{
		System.out.println(String.format("Current FPS: %.0f", (1.0f/dt)));
	}
	
	public abstract void update(float dt);

}
