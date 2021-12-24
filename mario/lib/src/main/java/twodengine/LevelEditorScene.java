package twodengine;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene
{
	public static boolean changingScene = false;
	private float timeToChangeScene = 2.0f;

	public LevelEditorScene()
	{
		System.out.println("inside levelEditor Scene");
	}

	@Override
	public void update(float dt)
	{
		printCurrentFps(dt);
		
		if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
		{
			changingScene = true;
		}
		
		if(changingScene && timeToChangeScene > 0)
		{
			timeToChangeScene -= dt;
			float[] rgba = Window.getRGBA();
			for(int i = 0; i<rgba.length; i++)
			{
				rgba[i] -= dt * 5.0;
			}
			Window.setRGBA(rgba);
		}
		else if(changingScene)
		{
			Window.changeScene(1);
		}
			
	}
	
	

}
