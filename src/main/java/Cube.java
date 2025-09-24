// Cube.java
//
// Stores cube vertex data and generates VAO.
// According to UML: Cube is used by Init.


import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class Cube {



    private float[] cubeVertices = {


            // Each triangle is defined / each triangle's normals by three vertexes each.


            // Position         Normals

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



    public int vbo;



    public int createCubeVAO() {



        int vao = glGenVertexArrays();

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



        return vao;

    }

}
