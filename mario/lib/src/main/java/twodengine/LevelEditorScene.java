package twodengine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene
{
	private String vertexShaderSrc = "#version 330 core\n"
			+ "\n"
			+ "layout (location = 0) in vec3 aPos;\n"
			+ "layout (location = 1) in vec4 aColor;\n"
			+ "\n"
			+ "out vec4 fColor;\n"
			+ "\n"
			+ "void main()\n"
			+ "{\n"
			+ "    fColor = aColor;\n"
			+ "    gl_Position = vec4(aPos,1.0);\n"
			+ "}\n"
			+ "";
	private String fragmentShaderSrc = "#version 330 core\n"
			+ "\n"
			+ "in vec4 fColor;\n"
			+ "\n"
			+ "out vec4 color;\n"
			+ "\n"
			+ "void main()\n"
			+ "{\n"
			+ "	color = fColor;\n"
			+ "}\n"
			+ "";
	
	private int vertexID, fragmentID , shaderProgram;
	
	private float[] vertexArray = {
			//position			//color
		
			0.5f, -0.5f , 0.0f,		1.0f,0.0f,0.0f,1.0f,  //bottom right
			-0.5f, 0.5f , 0.0f, 	0.0f,1.0f,0.0f,1.0f,  //Top left
			0.5f,  0.5f , 0.0f,		1.0f,0.0f,1.0f,1.0f,  //Top right
			-0.5f,-0.5f , 0.0f, 	1.0f,1.0f,0.0f,1.0f,  //Bottom left
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
		//compile and link shaders 
		
		//First load and compile vertex shaders
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexShaderSrc);
		glCompileShader(vertexID);
		
		// Check for errors in compilation
		int sucess = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if(sucess == GL_FALSE)
		{
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("defaultShader.glsl\n\t vertex shader failed to compile");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert false:"";
		}
		//First load and compile vertex shaders
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, fragmentShaderSrc);
		glCompileShader(fragmentID);
		
		// Check for errors in compilation
		 sucess = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if(sucess == GL_FALSE)
		{
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("defaultShader.glsl\n\t fragment shader failed to compile");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false:"";
		}
		
		//Link shaders and check for errors
		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexID);
		glAttachShader(shaderProgram, fragmentID);
		glLinkProgram(shaderProgram);
		
		// Check for linking errors
		sucess = glGetProgrami(shaderProgram, GL_LINK_STATUS);
		
		if(sucess == GL_FALSE )
		{
			int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
			System.out.println("defaultShader.glsl\n\t shader program failed to link");
			System.out.println(glGetProgramInfoLog(shaderProgram,len));
			assert false:"";
		}
		
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
		//Bind Shader Program
		glUseProgram(shaderProgram);
		
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
		
		glUseProgram(0);

	}
	
	

}
