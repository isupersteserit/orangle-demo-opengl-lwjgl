// Animation.java
//
// Controls cube rotation and animation state.
// According to UML: Animation is used by Loop.


import org.joml.Matrix4f;


public class Animation {



    private float cubeAngle = 0f;



    public float animateCube(float angle, Matrix4f model) {



        angle += 0.01f;

        cubeAngle = angle;



        model.identity().rotateY(angle).rotateX(angle * 0.5f);



        return angle;

    }

}
