package twodengine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import org.joml.Vector2f;
import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import renderer.Texture;
import util.AssetPool;


public class LevelEditorScene extends Scene 
{

	private GameObject obj1;
    private SpriteSheet sprites;
    private GameObject mario1;
    private GameObject mario2;
    public LevelEditorScene() 
    {
		super();//automatically added by the compiler
    }

    @Override
    public void init() 
    {    	
    	loadResources();
    	
    	this.camera = new Camera(new Vector2f(-250, 0));
    	
        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");
        
    	this.camera = new Camera(new Vector2f());
    	sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

    	  mario1 = new GameObject("Object 1",new Transform(new Vector2f(100,100),new Vector2f(64,64)), spriteIndex);
    	mario1.addComponent(new SpriteRenderer(sprites.getSprite(0)));

    	 mario2 = new GameObject("Object 1",new Transform(new Vector2f(150,200),new Vector2f(64,64)), spriteIndex);
    	mario2.addComponent(new SpriteRenderer(sprites.getSprite(15)));

    	this.addGameObjectToScene(mario1);
    	//this.addGameObjectToScene(mario2);
        
        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)),2);
        obj1.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/blendImage1.png")
        )));
        

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)),1);
        obj2.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/blendImage2.png")
        )));
        //this.addGameObjectToScene(obj1);
        //this.addGameObjectToScene(obj2);
    }

    protected void loadResources()
	{
    	AssetPool.getShader("assets/shaders/default.glsl");
    	Texture texture = AssetPool.getTexture("assets/images/spritesheet.png");
    	AssetPool.addSpriteSheet("assets/images/spritesheet.png",
    			new SpriteSheet(texture, 16 , 16 , 26 ,0));
	}

    
    
    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;


	@Override
    public void update(float dt) 
    {
    	//printCurrentFps(dt);
		//moveCamera(dt, 200.0f);

		spriteFlipTimeLeft -= dt;
		if(spriteFlipTimeLeft <= 0)
		{
			spriteFlipTimeLeft = spriteFlipTime;
			spriteIndex++;
			if(spriteIndex > 4)
			{
				spriteIndex = 0;
			}
			mario1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
		}


		//moveObject(dt, 200.0f, mario1);
		
    	for(GameObject g : this.gameObjects)
        {
    		moveObject(dt, 200.0f, g);
        }
  
        
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
    		System.out.println(go.transform.position);
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