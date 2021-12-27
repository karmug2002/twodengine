package util;

import java.util.Map;

import components.SpriteSheet;
import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;

public class AssetPool
{
	private static Map<String,Shader> shaders = new HashMap<>();
	private static Map<String,Texture> textures = new HashMap<>();
	private static Map<String,SpriteSheet> spriteSheets = new HashMap<>();


	public AssetPool()
	{

	}

	public static Shader getShader(String resourceName)
	{
		File file = new File(resourceName);
		if(AssetPool.shaders.containsKey(file.getAbsolutePath()))
		{
			return AssetPool.shaders.get(file.getAbsolutePath());
		}
		else
		{
			Shader shader = new Shader(resourceName);
			shader.compile();
			AssetPool.shaders.put(file.getAbsolutePath(), shader);
			return shader;
		}
	}
	
	public static Texture getTexture(String resourceName)
	{
		File file = new File(resourceName);
		if(AssetPool.textures.containsKey(file.getAbsolutePath()))
		{
			return AssetPool.textures.get(file.getAbsolutePath());
		}
		else
		{
			Texture texture = new Texture(resourceName);
			AssetPool.textures.put(file.getAbsolutePath(), texture);
			return texture;
		}
	}
	
	public static void addSpriteSheet(String resourceName,SpriteSheet sheet)
	{
		File file = new File(resourceName);
		if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
		{
			AssetPool.spriteSheets.put(file.getAbsolutePath(), sheet);
		}
	}
	
	public static SpriteSheet getSpriteSheet(String resourceName)
	{
		File file = new File(resourceName);
		if(!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
		{
			assert false : "Error : Tried to access spriteSheet '"+resourceName+"' and it has not been added to the list";
		}
		return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(),null);
	}
	
}
