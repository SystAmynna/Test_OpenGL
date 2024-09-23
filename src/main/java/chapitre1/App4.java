package chapitre1;

import chapitre1.tools.App;
import chapitre1.tools.Objet;
import chapitre1.tools.Shader;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Classe chapitre1.App4.
 * Transformation de matrices, rotations.
 */
public class App4 extends App {

    /**
     * chapitre1.tools.Shader
     */
    private Shader shader;
    /**
     * Wire shader
     */
    private Shader wireShader;

    /**
     * chapitre1.tools.Objet Rectangle
     */
    private Objet rectangle;

    /**
     * Debug
     */
    private boolean debug;

    /**
     * Construteur de la classe chapitre1.tools.App.
     *
     * @param width
     * @param height
     * @param title
     */
    public App4(int width, int height, String title) {
        super(width, height, title);
    }


    /**
     * Méthode d'initialisation.
     */
    @Override
    protected void ini() {

        shader = new Shader("shad4.vsh", "shad4.fsh");
        wireShader = new Shader("shad1.vert", "shad1.frag");

        // RECTANGLE
        /*
        float [] vertices = {
             // positions        // Couleurs        // Textures
             0.5f,  0.5f, 0.0f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f, // Haut droit
             0.5f, -0.5f, 0.0f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f, // Bas droit
            -0.5f, -0.5f, 0.0f,  0.0f, 0.0f, 0.0f,  0.0f, 0.0f, // Bas gauche
            -0.5f,  0.5f, 0.0f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f  // Haut gauche
        };
         */
        float [] vertices = {
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 0.0f,
                 0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,
                 0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f,
                 0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 0.0f,
                 0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f,
                 0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,

                 0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,
                 0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f,
                 0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                 0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                 0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                 0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f,
                 0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,
                 0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f,
                 0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 1.0f,
                 0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 0.0f,  0.0f, 1.0f
        };

        int [] indices = {
            0, 1, 3, // Triangle 1
            1, 2, 3  // Triangle 2
        };
        rectangle = new Objet(vertices, indices, 8);
        rectangle.createTexture("eca.png");

        System.err.println("CE CODE NE FONCTIONNE PAS... (et n'est pas sensé fonctionner)");

    }

    /**
     * Méthode de gestion des entrées.
     */
    @Override
    protected void processInput() {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }
        debug = glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS;

    }

    /**
     * Méthode de rendu.
     */
    @Override
    protected void render() {


        rectangle.draw(shader);
        if (debug) rectangle.drawWireframe(wireShader);

    }

    /**
     * Méthode principale.
     * @param args
     */
    public static void main(String[] args) {
        new App4(1600, 1200, "chapitre1.tools.App 4");

    }

}
