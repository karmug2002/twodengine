package twodengine;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;


public class JoyStickListener
{
	private static JoyStickListener instance;
	private  boolean present;

	public JoyStickListener()
	{
	}

	public static JoyStickListener get()
	{
		if(JoyStickListener.instance == null)
			JoyStickListener.instance = new JoyStickListener();
		return instance;
	}
	
	public static void joyStickCallback(int jid, int event)
	{
		
			
			
		if(event == GLFW_CONNECTED)
		{
			System.out.println("Joystick Connected!");
			get().present = glfwJoystickPresent(jid);
			System.out.println(get().present);
			System.out.println(glfwGetJoystickName(jid));
			ByteBuffer name = glfwGetJoystickButtons(jid);
			System.out.println(name.toString());
			if(name.get(0) == event)
			{
				System.out.println(name.get(0));
			}
			if(glfwJoystickIsGamepad(jid))
				System.out.println("it is gamepads");
		}
		else if(event == GLFW_DISCONNECTED)
		{
			System.out.println("Joystick DisConnected :(");
		}			
	}
	

}
