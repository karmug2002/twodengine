package components;

import twodengine.Component;

public class FontRenderer extends Component
{

	public FontRenderer()
	{

	}

	@Override
	public void start()
	{
		if(gameObject.getComponent(SpriteRenderer.class) != null)
		{
			System.out.println("found font Renderer");
		}
	}

	
	@Override
	public void update(float dt)
	{
		
	}

}
