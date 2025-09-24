// Init.java
//
// Handles initialization of the OpenGL context, window, shaders, cube setup.
// According to UML: Init uses Camera, Shader, Window, Cube.


import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Init {



    // Handles
    public long window;

    public int shaderProgram;

    public int vao, vbo;

    public int uniModel, uniView, uniProjection, uniLightPos;



    // Matrices
    public Matrix4f model = new Matrix4f();

    public Matrix4f view = new Matrix4f().translate(0, 0, -3);

    public Matrix4f projection;



    // Components
    public Camera camera;

    public Shader shader;

    public Cube cube;

    public Window windowHandler;



    public void progStart() {

        // Initialize GLFW
        if (!glfwInit()) throw new IllegalStateException("GLFW initialization failed");



        // Create window using Window class
        windowHandler = new Window();

        window = windowHandler.createWindow();



        // OpenGL context
        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        glfwSwapInterval(1);

        glfwShowWindow(window);



        // Enable depth test
        glEnable(GL_DEPTH_TEST);



        // Setup camera
        camera = new Camera();

        camera.createCamera(window);



        // Compile shaders
        shader = new Shader();

        shaderProgram = shader.compileShader();



        // Setup cube geometry
        cube = new Cube();

        vao = cube.createCubeVAO();

        vbo = cube.vbo;



        // Uniform locations
        uniModel = glGetUniformLocation(shaderProgram, "model");

        uniView = glGetUniformLocation(shaderProgram, "view");

        uniProjection = glGetUniformLocation(shaderProgram, "projection");

        uniLightPos = glGetUniformLocation(shaderProgram, "lightPos");



        // Default projection matrix
        projection = new Matrix4f().perspective(

                (float) Math.toRadians(70f),

                1280f / 720f,

                0.01f,

                100f

        );

    }



    public void cleanup() {

        // Cleanup GL resources
        glDeleteProgram(shaderProgram);

        glDeleteVertexArrays(vao);

        glDeleteBuffers(vbo);

        glfwDestroyWindow(window);

        glfwTerminate();

    }

}
