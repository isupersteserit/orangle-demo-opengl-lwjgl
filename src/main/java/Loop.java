// Loop.java
//
// Manages the render loop, calling animation and checking input.
// According to UML: Loop depends on Init and Animation.


import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Loop {



    private Init init;

    private Animation animation;



    public Loop(Init init) {

        this.init = init;

        this.animation = new Animation();

    }



    public void startLoop() {

        float angle = 0f;



        while (!glfwWindowShouldClose(init.window)) {



            // Clear screen
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);



            // Use program
            glUseProgram(init.shaderProgram);


            glBindVertexArray(init.vao);



            // Handle keyboard input
            checkKeybinds();



            // Rebuild camera view
            rebuildView();



            // Animate cube
            angle = animation.animateCube(angle, init.model);



            // Send matrices to GPU
            try (MemoryStack stack = MemoryStack.stackPush()) {

                FloatBuffer fb = stack.mallocFloat(16);

                glUniformMatrix4fv(init.uniModel, false, init.model.get(fb));

                glUniformMatrix4fv(init.uniView, false, init.view.get(fb));

                glUniformMatrix4fv(init.uniProjection, false, init.projection.get(fb));

            }



            // Light position
            glUniform3f(init.uniLightPos, 1.2f, 1.0f, 2.0f);



            // Draw cube
            glDrawArrays(GL_TRIANGLES, 0, 36);

            glBindVertexArray(0);



            glfwSwapBuffers(init.window);

            glfwPollEvents();

        }

    }



    public void checkKeybinds() {

        // W
        if (glfwGetKey(init.window, GLFW_KEY_W) == GLFW_PRESS)

            init.camera.cameraPos.add(new Vector3f(init.camera.cameraFront).mul(init.camera.cameraSpeed));



        // S
        if (glfwGetKey(init.window, GLFW_KEY_S) == GLFW_PRESS)

            init.camera.cameraPos.sub(new Vector3f(init.camera.cameraFront).mul(init.camera.cameraSpeed));



        // A
        if (glfwGetKey(init.window, GLFW_KEY_A) == GLFW_PRESS) {

            Vector3f cross = new Vector3f();

            init.camera.cameraFront.cross(init.camera.cameraUp, cross).normalize().mul(init.camera.cameraSpeed);

            init.camera.cameraPos.sub(cross);

        }



        // D
        if (glfwGetKey(init.window, GLFW_KEY_D) == GLFW_PRESS) {

            Vector3f cross = new Vector3f();

            init.camera.cameraFront.cross(init.camera.cameraUp, cross).normalize().mul(init.camera.cameraSpeed);

            init.camera.cameraPos.add(cross);

        }

    }



    public void rebuildView() {

        init.view.identity().lookAt(

                init.camera.cameraPos,

                new Vector3f(init.camera.cameraPos).add(init.camera.cameraFront),

                init.camera.cameraUp

        );

    }

}
