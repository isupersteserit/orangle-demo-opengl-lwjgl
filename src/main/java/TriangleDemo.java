import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

public class TriangleDemo {

    private long window;
    private int shaderProgram;
    private int vertexArrayObject;
    private int vertexBufferObject;


    public static void main(String[] args) {
        new TriangleDemo().run();
    }

    public void run () {
        init();
        loop();
        cleanup();
    }

    private void init () {

        if (!glfwInit()) {
            throw new IllegalStateException("GLFW is not able of being initialized, currently.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(800, 600, "Orangle Demo", NULL, NULL);

        if (window == NULL) {
            throw new RuntimeException("The window of being that is GLFW failed at being created.");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });


        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - 800) / 2, (vidmode.height() - 600) / 2);


        glfwMakeContextCurrent(window);
        GL.createCapabilities(); // OpenGL Bindings

        glfwSwapInterval(1);

        glfwShowWindow(window);

    }

    private void loop() {

        // Shaders

        String vertexShaderSource = "#version 330 core\n" + "layout (location = 0) in vec3 aPos;\n" + "void main() {\n" + "    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" + "}\n";

        String fragmentShaderSource = "#version 330 core\n" + "out vec4 FragColor;\n" + "void main() {\n" + "    FragColor = vec4(1.0f, 0.349f, 0.0f, 1.0f);\n" +  // The Orange Color
                "}\n";


        // Create Shaders, Compile Shaders

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("The compilation of the vertex shader was failed because: " + glGetShaderInfoLog(vertexShader));
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Fragment shader copiliation has failed: " + glGetShaderInfoLog(fragmentShader));
        }

        //Shaders linked into a program

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("Shader program linking failed: " + glGetProgramInfoLog(shaderProgram));

        }

        // Delete linked shaders

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        //Define triangle vertices so it can be rendered (-1.0 to 1.0)

        float[] vertices = {
                -0.5f, -0.5f, 0.0f, // Bottom-left
                0.5f, -0.5f, 0.0f, // Bottom-right
                0.0f, 0.5f, 0.0f, // Top of triangle
        };

        // Generate and bind the vertex objects

        vertexArrayObject = glGenVertexArrays();
        vertexBufferObject = glGenBuffers();

        glBindVertexArray(vertexArrayObject);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // define the vertex attributes/ their layour

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        //Unbind the VBO and the VAO

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        // set the color clear

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Finnally the main render loop

        while (!glfwWindowShouldClose(window)) {
            // Cleared Framebuffer

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Use the shader program bing the VAO etc
            glUseProgram(shaderProgram);
            glBindVertexArray(vertexArrayObject);

            // Draw the triangle

            glDrawArrays(GL_TRIANGLES, 0, 3);

            // Unbind the VAO

            glBindVertexArray(0);

            // Swap color buffer so that the rendered image is displayed

            glfwSwapBuffers(window);

            // Event polls

            glfwPollEvents();
        }
    }

    private void cleanup() {
        glDeleteProgram(shaderProgram);
        glDeleteVertexArrays(vertexArrayObject);
        glDeleteBuffers(vertexBufferObject);
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}