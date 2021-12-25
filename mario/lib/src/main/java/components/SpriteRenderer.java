package components;

import twodengine.Component;

public class SpriteRenderer extends Component
{
	private boolean firstTime = false;

	@Override
	public void start()
	{
		System.out.println("from start in spriteRenderer");
	}
	
	@Override
	public void update(float dt)
	{
		if(!firstTime)
		{
			System.out.println("updating in spriteRenderer");
			firstTime = true;
		}
	}

}
