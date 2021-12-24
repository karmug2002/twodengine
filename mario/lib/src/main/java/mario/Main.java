package mario;

import renderer.Shader;
import twodengine.Window;

public class Main
{

	public Main()
	{
	}

	public static void main(String[] args)
	{
		System.out.println("Greetings from twodengine! :)");
		Window window = Window.getWindow();
		window.run();
	}

}
