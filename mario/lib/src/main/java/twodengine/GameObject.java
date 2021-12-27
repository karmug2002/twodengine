package twodengine;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
	private String name;
	private List<Component> components;
	public Transform transform;
	private int zIndex;

	public GameObject(String name)
	{
		init(name,new Transform(),0);
	}
	
	public GameObject(String name,Transform transform,int zIndex)
	{
		init(name,transform,zIndex);
	}
	
	public void init(String name,Transform transform,int zIndex)
	{
		this.name = name;
		this.components = new ArrayList<Component>();
		this.transform = transform;
		this.zIndex = zIndex;
	}
	
	public <T extends Component> T getComponent(Class<T> componentClass)
	{
		for(Component c : components)
		{
			if(componentClass.isAssignableFrom(c.getClass()))
			{
				try 
				{
					return componentClass.cast(c);
				}
				catch(ClassCastException e)
				{
					e.printStackTrace();
					assert false : "ERROR: Casting component";
				}
			}
		}
		return null;
	}
	
	public <T extends Component> void removeComponent(Class<T> componentClass)
	{
		for(int k = 0; k<components.size(); k++)
		{
			if(componentClass.isAssignableFrom(components.get(k).getClass()))
			{
				components.remove(k);
				return;
			}
		}
	}
	
	public void addComponent(Component c)
	{
		this.components.add(c);
		c.gameObject = this;
	}
	
	public void update(float dt)
	{
		for(int k = 0; k<components.size(); k++)
		{
			components.get(k).update(dt);
		}
	}
	
	public void start()
	{
		for(int k = 0; k<components.size(); k++)
		{
			components.get(k).start();
		}
	}
	
	public int zIndex() 
	{
		return this.zIndex;
	}

}
