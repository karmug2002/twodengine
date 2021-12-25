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

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader
{
	private int shaderProgramID;
	private boolean beingUsed = false;
	
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
		if(!beingUsed)
		{
			glUseProgram(shaderProgramID);
			beingUsed = true;
		}
	}
	
	public void detach()
	{
		glUseProgram(0);
		beingUsed = false;
	}
	
	public void uploadMat4f(String varName, Matrix4f mat4)
	{
		int varLocation = glGetUniformLocation(shaderProgramID,varName);
		this.use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
		mat4.get(matBuffer);
		glUniformMatrix4fv(varLocation, false, matBuffer);
	}
	
	public void uploadMat3f(String varName,Matrix3f mat3)
	{
		int varLocation = glGetUniformLocation(shaderProgramID,varName);
		this.use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
		mat3.get(matBuffer);
		glUniformMatrix3fv(varLocation, false, matBuffer);
	}
	
	public void uploadMat2f(String varName,Matrix2f mat2)
	{
		int varLocation = glGetUniformLocation(shaderProgramID,varName);
		this.use();
		FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4);
		mat2.get(matBuffer);
		glUniformMatrix3fv(varLocation, false, matBuffer);
	}
	
	public void uploadVec4f(String varName,Vector4f vec4f)
	{
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		this.use();
		glUniform4f(varLocation, vec4f.x,vec4f.y,vec4f.z,vec4f.w);
	}
	
	public void uploadVec3f(String varName,Vector3f vec3)
	{
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		this.use();
		glUniform3f(varLocation, vec3.x,vec3.y,vec3.z);
	}

	public void uploadVec2f(String varName,Vector2f vec2)
	{
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		this.use();
		glUniform2f(varLocation, vec2.x,vec2.y);
		
	}
	
	public void uploadFloat(String varName,float val)
	{
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		this.use();
		glUniform1f(varLocation, val);
	}
	
	public void uploadInt(String varName,int val)
	{
		int varLocation = glGetUniformLocation(shaderProgramID, varName);
		this.use();
		glUniform1i(varLocation, val);
	}

}
