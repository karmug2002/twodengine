package twodengine;

public class LevelScene extends Scene
{
	private float timeToChangeScene = 2.0f;
	
	public LevelScene()
	{
		System.out.println("inside LevelScene");
	}

	@Override
	public void update(float dt)
	{
		//printCurrentFps(dt);
		float[] rgba = Window.getRGBA();
		
		for(int i = 0; i<rgba.length; i++)
		{
			rgba[i] += dt * 5.0f;
		}
		Window.setRGBA(rgba);
		
		timeToChangeScene -= dt;
		
		if(timeToChangeScene<0)
		{
			LevelEditorScene.changingScene = false;
			timeToChangeScene = 2.0f;
			Window.changeScene(0);
		}
		
	}

}
