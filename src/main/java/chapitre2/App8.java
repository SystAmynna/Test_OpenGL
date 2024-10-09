package chapitre2;

import chapitre1.tools.Shader;
import chapitre2.tools.App3D;
import chapitre2.tools.Mesh;
import chapitre2.tools.Shader3D;
import chapitre2.tools.Texture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class App8 extends App3D {

    Mesh cube, cube2, light;
    Shader3D shader, lightShader;
    boolean texture = true;
    boolean textPressed = false;


    public App8(String title, int width, int height) {
        super(title, width, height);
    }

    @Override
    protected void ini() {

        Shader.setPath("src/main/resources/chapitre2/shaders/");

        float[] vertices = {
                // Positions         // Normales
                // Front face
                -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 0.0f,
                 0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 0.0f,
                 0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  0.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f,  1.0f, 1.0f,
                // Back face
                -0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f, 0.0f,
                 0.5f, -0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  0.0f,  1.0f,  0.0f, 1.0f,
                // Left face
                -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                // Right face
                 0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 0.0f,
                 0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,  1.0f, 1.0f,
                 0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 1.0f,
                 0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,  0.0f, 0.0f,
                // Top face
                -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 0.0f,
                 0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,  0.0f, 1.0f,
                // Bottom face
                -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 0.0f,
                 0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 0.0f,
                 0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  1.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,  0.0f, 1.0f
        };
        int[] indices = {
                0, 1, 2, 2, 3, 0, // Front face
                4, 5, 6, 6, 7, 4, // Back face
                8, 9, 10, 10, 11, 8, // Left face
                12, 13, 14, 14, 15, 12, // Right face
                16, 17, 18, 18, 19, 16, // Top face
                20, 21, 22, 22, 23, 20  // Bottom face
        };

        Texture[] textures = new Texture[16];
        textures[0] = new Texture("src/main/resources/chapitre2/textures/eyeU.png");

        Texture[] textures2 = new Texture[16];
        textures2[0] = new Texture("src/main/resources/chapitre2/textures/water.png");

        Vector3f lightPos = new Vector3f(1.2f, 1.0f, 2.0f);
        Vector3f lightColor = new Vector3f(1.0f, 1.0f, 1.0f);

        shader = new Shader3D("vertex/shad8.vsh", "fragment/shad8.fsh");
        lightShader = new Shader3D("vertex/light.vsh", "fragment/lightSource.fsh");

        shader.addUniform("objectColor", new Vector3f(1.0f, 0.5f, 0.31f));
        shader.addUniform("lightColor", lightColor);
        shader.addUniform("lightPos", lightPos);
        shader.addUniform("viewPos", camera.getPosition());
        shader.addUniform("useTexture", texture);

        lightShader.addUniform("lightColor", lightColor);

        Mesh.DataType[] dataTypes = {Mesh.DataType.POSITION, Mesh.DataType.NORMAL, Mesh.DataType.TEXTURE};

        cube = new Mesh(vertices, indices, dataTypes, textures, shader);
        cube2 = new Mesh(vertices, indices, dataTypes, textures2, shader);
        light = new Mesh(vertices, indices, dataTypes, null, lightShader);

        // transformation du cube
        cube.setPosition(new Vector3f(0f, 0f, 0f));

        cube2.setPosition(new Vector3f(0f, 0f, 0f));
        cube2.setScale(new Vector3f(0.5f, 0.5f, 0.5f));

        // transformation de la source de lumière
        light.setPosition(lightPos);
        light.setScale(new Vector3f(0.2f, 0.2f, 0.2f));



    }

    @Override
    protected void processInput() {
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_X) == GLFW.GLFW_PRESS && !textPressed) {
            texture = !texture;
            textPressed = true;
            System.out.println(texture);
        } else if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_X) == GLFW.GLFW_RELEASE) {
            textPressed = false;
        }

    }

    @Override
    protected void update() {

        // modifier la position de la lumière en fonction du temps en rotation autour de l'axe Y
        // ajouter une rotation autrour de x
        float time = (float) GLFW.glfwGetTime();
        float x = (float) Math.sin(time) * 2.0f;
        float z = (float) Math.cos(time) * 2.0f;
        float y = (float) Math.sin(time) * 1.2f;

        light.setPosition(new Vector3f(x, y, z));

        cube2.setPosition(new Vector3f(x*1.3f, -z*1.3f, y*1.3f));

        shader.addUniform("lightPos", light.getPosition());
        shader.addUniform("viewPos", camera.getPosition());
        shader.addUniform("useTexture", texture ? 1 : 0);



    }

    public static void main(String[] args) {
        new App8("App8", 800, 600);
    }
}
