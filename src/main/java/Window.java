// Window.java
//
// Responsible for creating GLFW window and handling resize events.
// According to UML: Window is used by Init.


import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {



    public long createWindow() {



        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);



        long window = glfwCreateWindow(1280, 720, "Orangle | Cube Demo", NULL, NULL);

        if (window == NULL) throw new RuntimeException("Failed to create GLFW window");



        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {

            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)

                glfwSetWindowShouldClose(win, true);

        });



        // Resize callback
        glfwSetFramebufferSizeCallback(window, (win, w, h) -> {

            glViewport(0, 0, w, h);

        });



        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(window,

                (vidmode.width() - 1280) / 2,

                (vidmode.height() - 720) / 2

        );



        return window;

    }

}
