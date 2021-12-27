package twodengine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Vector2f;
import org.joml.Vector4f;

import components.SpriteRenderer;
import renderer.Texture;
import util.AssetPool;

public class LevelEditorScene extends Scene 
{

    public LevelEditorScene() 
    {
		super();//automatically added by the compiler
    }

    @Override
    public void init() 
    {
    	Texture newTexture = AssetPool.getTexture("assets/images/testImage.jpg");
    	this.camera = new Camera(new Vector2f());
    	GameObject go = new GameObject("Object 1",new Transform(new Vector2f(100,100),new Vector2f(32,32)));
    	go.addComponent(new SpriteRenderer(newTexture));
    	this.addGameObjectToScene(go);
    	loadResources();
    }

    protected void loadResources()
	{
    	AssetPool.getShader("assets/shaders/default.glsl");
    	//AssetPool.getTexture("assets/images/testImage.png");
	}

	@Override
    public void update(float dt) 
    {
    	//printCurrentFps(dt);
		//moveCamera(dt, 200.0f);
		/*
    	for(GameObject g : this.gameObjects)
        {
    		moveObject(dt, 200.0f, g);
        }
        */
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
    
    public void moveObject(float dt, float speed,GameObject go) 
    {
    	if(KeyListener.isKeyPressed(GLFW_KEY_UP))
    	{
    		go.transform.position.y += dt * speed;
    	}
    	if(KeyListener.isKeyPressed(GLFW_KEY_DOWN))
    	{
    		go.transform.position.y -= dt * speed;
    	}
    	if(KeyListener.isKeyPressed(GLFW_KEY_LEFT))
    	{
    		go.transform.position.x -= dt * speed;
    	}
    	if(KeyListener.isKeyPressed(GLFW_KEY_RIGHT))
    	{
    		go.transform.position.x += dt * speed;
    	}
    }  
    
}