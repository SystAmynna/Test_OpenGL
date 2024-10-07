package chapitre2;

import chapitre1.tools.Shader;
import chapitre2.tools.App3D;
import chapitre2.tools.Mesh;
import chapitre2.tools.Shader3D;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class App7 extends App3D {

    Mesh cube, lightCube;

    Shader3D lightShader;
    Shader3D light;

    public App7(String title, int width, int height) {
        super(title, width, height);
    }

    @Override
    protected void ini() {

        Shader.setPath("src/main/resources/chapitre2/shaders/");

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

        // Création des shaders
        lightShader = new Shader3D("vertex/shad2.1.vsh", "fragment/shad2.2.fsh");
        light = new Shader3D("vertex/shad2.1.vsh", "fragment/lightSource.fsh");

        // création des Mesh
        cube = new Mesh(vertices, indices, null, lightShader);
        lightCube = new Mesh(vertices, indices, null, light);

        // envoie des données uniformes
        cube.addUniform("objectColor", new Vector3f(1.0f, 0.5f, 0.31f));
        cube.addUniform("lightColor", new Vector3f(1.0f, 1.0f, 1.0f));

        // transformation du cube
        cube.setPosition(new Vector3f(0f, 0f, 0f));

        // transformation de la source de lumière
        lightCube.setPosition(new Vector3f(1.2f, 1.0f, 2.0f));
        lightCube.setScale(new Vector3f(0.2f, 0.2f, 0.2f));


    }

    @Override
    protected void processInput() {

    }

    @Override
    protected void update() {
        /*
        Matrix4f model = new Matrix4f().translate(lightCube.getPosition());
        Matrix4f view = camera.getViewMatrix();
        Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(camera.getFov()), (float) width / (float) height, 0.1f, 100.0f);

        light.use();
        light.setMat4f("model", model);
        light.setMat4f("view", view);
        light.setMat4f("projection", projection);
        lightShader.use();

         */

    }

    public static void main(String[] args) {
        new App7("Chapitre 2 : Application 7", 1600, 1200);
    }
}
