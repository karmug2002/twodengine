package renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.*;


import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Shader
{
	private int shaderProgramID;
	
	private String vertexSource;
	private String fragmentSource;

	private String filepath;

	public Shader(String filepath)
	{
		this.filepath = filepath;
		try 
		{
			String source = new String(Files.readAllBytes(Paths.get(filepath)));
			String[] splitString = source.split("(#type)( )+([a-zA-z])+");
			int index = source.indexOf("#type")+6;
			int eol = source.indexOf("\n",index);
			String firstPattern = source.substring(index,eol).trim();
			
			index = source.indexOf("#type",eol)+6;
			eol = source.indexOf("\n",index);
			String secondPattern = source.substring(index,eol).trim();
			
			if(firstPattern.equals("vertex"))
			{
				vertexSource = splitString[1];
			}
			else if(firstPattern.equals("fragment"))
			{
				fragmentSource = splitString[1];
			}
			else
			{
				throw new IOException("Unexpected token"+firstPattern);
			}
			
			if(secondPattern.equals("vertex"))
			{
				vertexSource = splitString[2];
			}
			else if(secondPattern.equals("fragment"))
			{
				fragmentSource = splitString[2];
			}
			else
			{
				throw new IOException("Unexpected token"+firstPattern);
			}
			
		}
		catch(IOException e)
		{
			e.printStackTrace();
			
			assert false : "Error: could not open file for shader: " +filepath + "";
		}
	}
	
	public void compile()
	{	
		int vertexID,fragmentID;
		//compile and link shaders 
		
		//First load and compile vertex shaders
		vertexID = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexID, vertexSource);
		glCompileShader(vertexID);
		
		// Check for errors in compilation
		int sucess = glGetShaderi(vertexID, GL_COMPILE_STATUS);
		if(sucess == GL_FALSE)
		{
			int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: '"+filepath+"'"+"\n\t vertex shader failed to compile");
			System.out.println(glGetShaderInfoLog(vertexID, len));
			assert false:"";
		}
		//First load and compile vertex shaders
		fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentID, fragmentSource);
		glCompileShader(fragmentID);
		
		// Check for errors in compilation
		 sucess = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
		if(sucess == GL_FALSE)
		{
			int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
			System.out.println("ERROR: '"+filepath+"'"+"\n\t fragment shader failed to compile");
			System.out.println(glGetShaderInfoLog(fragmentID, len));
			assert false:"";
		}
		
		//Link shaders and check for errors
		shaderProgramID = glCreateProgram();
		glAttachShader(shaderProgramID, vertexID);
		glAttachShader(shaderProgramID, fragmentID);
		glLinkProgram(shaderProgramID);
		
		// Check for linking errors
		sucess = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
		
		if(sucess == GL_FALSE )
		{
			int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
			System.out.println(filepath+"\n\t shader program failed to link");
			System.out.println(glGetProgramInfoLog(shaderProgramID,len));
			assert false:"";
		}
		
	}
	
	public void use()
	{
		glUseProgram(shaderProgramID);
	}
	
	public void detach()
	{
		glUseProgram(0);
	}
	
	public void uploadMat4f(String varName, Matrix4f mat4)
	{
		int varLocation = glGetUniformLocation(shaderProgramID,varName);
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		glUniformMatrix4fv(varLocation, false, matBuffer);
	}

}
