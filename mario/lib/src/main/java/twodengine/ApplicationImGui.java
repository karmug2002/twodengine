package twodengine;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.*;
import imgui.app.Window;
import imgui.app.Configuration;

public class ApplicationImGui extends Window {
	
	private WindowImGui gui;
	
    protected void configure(final Configuration config) 
    {
    	config.setTitle("hello imgui!!");
    }
    /**
     * Method called once, before application run loop.
     */
    protected void preRun() {
    }

    /**
     * Method called once, after application run loop.
     */
    protected void postRun() {
    }

    /**
     * Entry point of any ImGui application. Use it to start the application loop.
     *
     * @param app application instance to run
     */
    public static void launch(final ApplicationImGui app) {
        initialize(app);
        app.preRun();
        app.run();
        app.postRun();
        app.dispose();
    }

    private static void initialize(final ApplicationImGui app) {
        final Configuration config = new Configuration();
        app.configure(config);
        app.init(config);
        long window = app.getHandle();
        app.gui = new WindowImGui(window);
        app.gui.init();
        
}

	@Override
	public void process()
	{
		 final ImGuiIO io = ImGui.getIO();
		//startFrame();
		//ImGui.text("hello World");
		gui.loop(io.getDeltaTime());
		
		
		//endFrame();
		
		 
	}
	
	public static void main(String[] args)
	{
		launch(new ApplicationImGui());
	}
}