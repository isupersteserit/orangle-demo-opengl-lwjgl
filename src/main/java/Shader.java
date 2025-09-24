// Shader.java
//
// Contains GLSL shader source code and compile logic.
// According to UML: Shader is used by Init.


import static org.lwjgl.opengl.GL20.*;


public class Shader {



    private String vertexShaderSource = "#version 330 core\n" +
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



    private String fragmentShaderSource = "#version 330 core\n" +
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



    public int compileShader() {



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



        int shaderProgram = glCreateProgram();

        glAttachShader(shaderProgram, vertexShader);

        glAttachShader(shaderProgram, fragmentShader);

        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE)

            throw new RuntimeException(glGetProgramInfoLog(shaderProgram));



        glDeleteShader(vertexShader);

        glDeleteShader(fragmentShader);



        return shaderProgram;

    }

}
