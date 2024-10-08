package chapitre2;

import chapitre1.tools.Shader;
import chapitre2.tools.App3D;
import chapitre2.tools.Mesh;
import chapitre2.tools.Shader3D;
import chapitre2.tools.Texture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class App6 extends App3D {

    Mesh cube;
    Shader3D shader;

    public App6(String title, int width, int height) {
        super(title, width, height);
    }

    @Override
    protected void ini() {
        Shader.setPath("src/main/resources/chapitre2/shaders/");
        shader = new Shader3D("vertex/shad2.1.vsh", "fragment/shad2.1.fsh");

        Texture[] textures = new Texture[16];
        textures[0] = new Texture("src/main/resources/chapitre2/textures/wall.png");
        textures[1] = new Texture("src/main/resources/chapitre2/textures/wood.png");

        float[] vertices = {
            // Positions         // Couleurs         // Textures
            // Front face
            -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 0.0f,  1.0f, 0.0f,
             0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,
             0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f,  0.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0.0f,  1.0f, 1.0f,
            // Back face
            -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 1.0f,  0.0f, 0.0f,
             0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 1.0f,  1.0f, 0.0f,
             0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 1.0f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
            // Left face
            -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 0.0f,  0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0.0f,  0.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 1.0f,  1.0f, 0.0f,
            // Right face
             0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,
             0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f,  1.0f, 1.0f,
             0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 1.0f,  0.0f, 1.0f,
             0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 1.0f,  0.0f, 0.0f,
            // Top face
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0.0f,  0.0f, 0.0f,
             0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f,  1.0f, 0.0f,
             0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 1.0f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
            // Bottom face
            -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 0.0f,  0.0f, 0.0f,
             0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,
             0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 1.0f,  1.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 1.0f,  0.0f, 1.0f
        };

        int[] indices = {
            0, 1, 2, 2, 3, 0, // Front face
            4, 5, 6, 6, 7, 4, // Back face
            8, 9, 10, 10, 11, 8, // Left face
            12, 13, 14, 14, 15, 12, // Right face
            16, 17, 18, 18, 19, 16, // Top face
            20, 21, 22, 22, 23, 20  // Bottom face
        };

        cube = new Mesh(vertices, indices, Mesh.DT_POSITION_COLOR_TEXTURE, textures, shader);
        cube.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));



        Shader.stop();
    }

    @Override
    protected void update() {
        Vector3f r = cube.getRotation();
        float angleIncrementX = 0.5f;
        float angleIncrementY = 0.7f;
        float angleIncrementZ = 0.3f;

        float angleX = (r.x + angleIncrementX) % 360f;
        float angleY = (r.y + angleIncrementY) % 360f;
        float angleZ = (r.z + angleIncrementZ) % 360f;

        cube.setRotation(new Vector3f(angleX, angleY, angleZ));

        Vector3f p = cube.getPosition();
        p.x = (float) Math.sin((float) GLFW.glfwGetTime()) * 2.0f;
        cube.setPosition(p);
    }

    @Override
    protected void processInput() {}

    public static void main(String[] args) {
        new App6("Chapitre 2 : Application 6", 1600, 1200);
    }
}