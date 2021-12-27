package twodengine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;

public class LevelEditorScene extends Scene 
{

    public LevelEditorScene() 
    {
		super();//automatically added by the compiler
    }

    @Override
    public void init() 
    {
    	this.camera = new Camera(new Vector2f());
    	
    	int xOffset = 10;
    	int yOffset = 10;
    	
    	float totalWidth = (float)(600 - xOffset * 2);
    	float totalHeight = (float)(300 - yOffset * 2);
    	float sizeX = totalWidth / 100.0f;
    	float sizeY = totalHeight / 100.0f;
    	
    	for(int x = 0; x < 100; x++)
    	{
    		for(int y = 0; y < 100; y++)
    		{
    			float xPos = xOffset + (x * sizeX);
    			float yPos = yOffset + (y * sizeY);
    			
    			GameObject go = new GameObject("Obj" +x +"" + y, new Transform(new Vector2f(xPos,yPos) , new Vector2f(sizeX,sizeY)));
    			go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth,yPos / totalHeight,1,1)));
    			this.addGameObjectToScene(go);
    		}
    	}
    }

    @Override
    public void update(float dt) 
    {
    	printCurrentFps(dt);
    	moveCamera(dt, 200.0f);        
        for(GameObject g : this.gameObjects)
        {
        	g.update(dt);
        }
        
        this.renderer.render();
    }
    
    public void moveCamera(float dt, float speed) 
    {
    	if(KeyListener.isKeyPressed(GLFW_KEY_UP))
    	{
    		camera.position.y -= dt * speed;
    	}
    	if(KeyListener.isKeyPressed(GLFW_KEY_DOWN))
    	{
    		camera.position.y += dt * speed;
    	}
    	if(KeyListener.isKeyPressed(GLFW_KEY_LEFT))
    	{
    		camera.position.x += dt * speed;
    	}
    	if(KeyListener.isKeyPressed(GLFW_KEY_RIGHT))
    	{
    		camera.position.x -= dt * speed;
    	}
    }  
    
}