// Camera.java
//
// Handles camera position, direction, and mouse input.
// According to UML: Camera is used by Init.


import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;


public class Camera {



    public Vector3f cameraPos = new Vector3f(0.0f, 0.0f, 3.0f);

    public Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);

    public Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);



    public float cameraSpeed = 0.05f;



    private float yaw = -90.0f;

    private float pitch = 0.0f;

    private double lastX = 640, lastY = 360;

    private boolean firstMouse = true;



    public void createCamera(long window) {



        // Capture mouse movement
        glfwSetCursorPosCallback(window, (win, xpos, ypos) -> {

            if (firstMouse) {

                lastX = xpos;

                lastY = ypos;

                firstMouse = false;

            }



            float xoffset = (float) (xpos - lastX);

            float yoffset = (float) (lastY - ypos);

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



        // Disable cursor
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

    }

}
