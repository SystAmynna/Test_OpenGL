package chapitre2;

import chapitre1.tools.Shader;
import chapitre2.tools.App3D;
import chapitre2.tools.Mesh;
import chapitre2.tools.Shader3D;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class App7 extends App3D {

    Mesh cube, lightCube;

    Shader lightShader;
    Shader light;

    public App7(String title, int width, int height) {
        super(title, width, height);
    }

    @Override
    protected void ini() {

        Shader.setPath("src/main/resources/chapitre2/shaders/");
        lightShader = new Shader3D("vertex/shad2.1.vsh", "fragment/shad2.2.fsh");
        lightShader.use();
        lightShader.setVec3("objectColor", 1.0f, 0.5f, 0.31f);
        lightShader.setVec3("lightColor", 1.0f, 1.0f, 1.0f);

        light = new Shader3D("vertex/shad2.1.vsh", "fragment/lightSource.fsh");

        // Définir les vertices et indices pour un cube avec couleurs et textures
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
        cube = new Mesh(vertices, indices, null);
        cube.setPosition(new Vector3f(0f, 0f, 0f));
        cube.setShader(lightShader);

        lightCube = new Mesh(vertices, indices, null);
        lightCube.setPosition(new Vector3f(1.2f, 1.0f, 2.0f));
        lightCube.setScale(new Vector3f(0.2f, 0.2f, 0.2f));
        lightCube.setShader(light);


    }

    @Override
    protected void processInput() {

    }

    @Override
    protected void update() {
        Matrix4f model = new Matrix4f().translate(lightCube.getPosition());
        Matrix4f view = camera.getViewMatrix();
        Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(camera.getFov()), (float) width / (float) height, 0.1f, 100.0f);

        light.use();
        light.setMat4f("model", model);
        light.setMat4f("view", view);
        light.setMat4f("projection", projection);
        lightShader.use();

    }

    public static void main(String[] args) {
        new App7("Chapitre 2 : Application 7", 1600, 1200);
    }
}
