package archive;
// Java OMP (Open Math Library Imports)
import org.joml.Matrix4f;
import org.joml.Vector3f;

// Java I/O
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;



public class CubeDemo {

    
    private long window;
    private int shaderProgram;
    private int vao, vbo;


    private int uniModel, uniView, uniProjection, uniLightPos;



    // Camera
    private Vector3f cameraPos = new Vector3f(0.0f, 0.0f, 3.0f);
    private Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
    private Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);


    private float cameraSpeed = 0.05f;


    // Mouse look state
    private float yaw = -90.0f;   // start facing -Z
    private float pitch = 0.0f;
    private double lastX = 640, lastY = 360;
    private boolean firstMouse = true;


    private Matrix4f model = new Matrix4f();
    private Matrix4f view = new Matrix4f().translate(0, 0, -3);

    private Matrix4f projection;


    public static void main(String[] args) {
        new CubeDemo().run();
    }



    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {

        if (!glfwInit()) throw new IllegalStateException("GLFW init failed");


        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);


        window = glfwCreateWindow(1280, 720, "Orangle | Cube Demo", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Failed to create window");


        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {

            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)

                glfwSetWindowShouldClose(win, true);

        });


        // Mouse input (yaw/pitch)
        glfwSetCursorPosCallback(window, (win, xpos, ypos) -> {

            if (firstMouse) {
                lastX = xpos;
                lastY = ypos;
                firstMouse = false;

            }

            float xoffset = (float) (xpos - lastX);

            float yoffset = (float) (lastY - ypos); // y coords go from down to up

            lastX = xpos;
            lastY = ypos;

            float sensitivity = 0.1f;
            xoffset *= sensitivity;
            yoffset *= sensitivity;


            yaw += xoffset;
            pitch += yoffset;


            if (pitch > 89.0f) pitch = 89.0f;
            if (pitch < -89.0f) pitch = -89.0f;



            Vector3f front = new Vector3f();
            front.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
            front.y = (float) Math.sin(Math.toRadians(pitch));
            front.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
            cameraFront.set(front.normalize());

        });



        // what is even this

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);


        // resize keystorke

        glfwSetFramebufferSizeCallback(window, (win, w, h) -> {

            glViewport(0, 0, w, h);
            projection = new Matrix4f().perspective(
                    (float) Math.toRadians(70f),
                    (float) w / (float) h,
                    0.01f, 100f


            );

        });



        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(window,
                (vidmode.width() - 1280) / 2,
                (vidmode.height() - 720) / 2);


        glfwMakeContextCurrent(window);

        GL.createCapabilities();
        glfwSwapInterval(1);
        glfwShowWindow(window);


        glEnable(GL_DEPTH_TEST);



        // shaders
        String vertexShaderSource = "#version 330 core\n" +
                "layout(location = 0) in vec3 aPos;\n" +
                "layout(location = 1) in vec3 aNormal;\n" +
                "out vec3 FragPos;\n" +
                "out vec3 Normal;\n" +
                "uniform mat4 model;\n" +
                "uniform mat4 view;\n" +
                "uniform mat4 projection;\n" +
                "void main(){\n" +
                "   FragPos = vec3(model * vec4(aPos, 1.0));\n" +
                "   Normal = mat3(transpose(inverse(model))) * aNormal;\n" +
                "   gl_Position = projection * view * vec4(FragPos, 1.0);\n" +
                "}\n";



        String fragmentShaderSource = "#version 330 core\n" +
                "out vec4 FragColor;\n" +
                "in vec3 FragPos;\n" +
                "in vec3 Normal;\n" +
                "uniform vec3 lightPos;\n" +
                "void main(){\n" +
                "   vec3 lightColor = vec3(1.0, 1.0, 1.0);\n" +
                "   vec3 objectColor = vec3(1.0, 0.5, 0.0);\n" +
                "   vec3 norm = normalize(Normal);\n" +
                "   vec3 lightDir = normalize(lightPos - FragPos);\n" +
                "   float diff = max(dot(norm, lightDir), 0.0);\n" +
                "   vec3 diffuse = diff * lightColor;\n" +
                "   vec3 result = (diffuse + 0.2) * objectColor;\n" +
                "   FragColor = vec4(result, 1.0);\n" +
                "}\n";



        int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexShader, vertexShaderSource);

        glCompileShader(vertexShader);

        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException(glGetShaderInfoLog(vertexShader));


        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE)

            throw new RuntimeException(glGetShaderInfoLog(fragmentShader));



        shaderProgram = glCreateProgram();

        glAttachShader(shaderProgram, vertexShader);

        glAttachShader(shaderProgram, fragmentShader);

        glLinkProgram(shaderProgram);
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException(glGetProgramInfoLog(shaderProgram));


        glDeleteShader(vertexShader);

        glDeleteShader(fragmentShader);


        // cube w/ norms
        float[] cubeVertices = {
                // pos         // norms
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,

                0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
               -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,

                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
                0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,

                0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,

                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,

                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,

                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,

                0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,

                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f
        };



        vao = glGenVertexArrays();

        vbo = glGenBuffers();



        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glBufferData(GL_ARRAY_BUFFER, cubeVertices, GL_STATIC_DRAW);


        int stride = 6 * Float.BYTES;

        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0L);

        glEnableVertexAttribArray(0);



        glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);


        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);




        uniModel = glGetUniformLocation(shaderProgram, "model");

        uniView = glGetUniformLocation(shaderProgram, "view");

        uniProjection = glGetUniformLocation(shaderProgram, "projection");

        uniLightPos = glGetUniformLocation(shaderProgram, "lightPos");



        projection = new Matrix4f().perspective((float) Math.toRadians(70f), 1280f / 720f, 0.01f, 100f);
    }


    private void loop() {

        float angle = 0f;


        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


            glUseProgram(shaderProgram);
            glBindVertexArray(vao);


            // check keyboards
            if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)

                cameraPos.add(new Vector3f(cameraFront).mul(cameraSpeed));

            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)

                cameraPos.sub(new Vector3f(cameraFront).mul(cameraSpeed));

            if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {


                Vector3f cross = new Vector3f();
                cameraFront.cross(cameraUp, cross).normalize().mul(cameraSpeed);
                cameraPos.sub(cross);

            }

            if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {

                Vector3f cross = new Vector3f();

                cameraFront.cross(cameraUp, cross).normalize().mul(cameraSpeed);

                cameraPos.add(cross);


            }

            // rebuild view

            view.identity().lookAt(cameraPos, new Vector3f(cameraPos).add(cameraFront), cameraUp);

            // cube animation
            angle += 0.01f;
            model.identity().rotateY(angle).rotateX(angle * 0.5f);



            try (MemoryStack stack = MemoryStack.stackPush()) {

                FloatBuffer fb = stack.mallocFloat(16);

                glUniformMatrix4fv(uniModel, false, model.get(fb));

                glUniformMatrix4fv(uniView, false, view.get(fb));

                glUniformMatrix4fv(uniProjection, false, projection.get(fb));

            }



            glUniform3f(uniLightPos, 1.2f, 1.0f, 2.0f);

            glDrawArrays(GL_TRIANGLES, 0, 36);

            glBindVertexArray(0);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void cleanup() {
        glDeleteProgram(shaderProgram);
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
