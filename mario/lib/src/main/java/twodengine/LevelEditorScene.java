package twodengine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import renderer.Shader;
import util.Time;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

//import java.awt.event.KeyEvent;
import static org.lwjgl.glfw.GLFW.*;


public class LevelEditorScene extends Scene
{
	private Shader defaultShader;
	
	private float[] vertexArray = {
			//position			//color
		
			100.5f,  0.5f , 0.0f,		1.0f,0.0f,0.0f,1.0f,  //bottom right
			0.5f,	 100.5f , 0.0f, 	0.0f,1.0f,0.0f,1.0f,  //Top left
			100.5f,  100.5f , 0.0f,		1.0f,0.0f,1.0f,1.0f,  //Top right
			0.5f, 	  0.5f , 0.0f, 	1.0f,1.0f,0.0f,1.0f,  //Bottom left
	};
	
	//IMPORTANT MUST BE IN COUNTER - CLOCKWISE ORDER
	private int[] elementArray = {
		//
		2,1,0,//top right triangle
		0,1,3,//bottom left triangle	
	};
	
	private int vaoID , vboID , eboID;

	public LevelEditorScene()
	{
		
	}

	@Override
	public void init()
	{
		this.camera = new Camera(new Vector2f());
		defaultShader = new Shader("assets/shaders/default.glsl");
		defaultShader.compile();
		//Generate Vertex array objs,Element array objs,Vertex buffer objs
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		//Create a float buffer of vertex
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
		vertexBuffer.put(vertexArray).flip();
		
		//Create VBO upload the vertex buffer
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER,vboID);
		glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);
		
		// Create the indices and upload
		IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
		elementBuffer.put(elementArray).flip();
		
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
		
		//Add the vertex attribute pointers
		int positionsSize = 3;
		int colorSize = 4;
		int floatSizeBytes = 4;
		int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;
		glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
		glEnableVertexAttribArray(0);
		
		glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize*floatSizeBytes);
		glEnableVertexAttribArray(1);
	}
	
	@Override
	public void update(float dt)
	{
		//printCurrentFps(dt);
		
		controlCamera(dt,200.0f);
		//Bind Shader Program
		defaultShader.use();
		defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
		defaultShader.uploadMat4f("uView", camera.getViewMatrix());
		defaultShader.uploadFloat("uTime", Time.getTime());
		//Bind the VAO
		glBindVertexArray(vaoID);
		
		//enable the vertex attribute pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, elementArray.length,GL_UNSIGNED_INT,0);
		
		//unbind everything
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		
		defaultShader.detach();
	}
	
	public void controlCamera(float dt,float speed)
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
