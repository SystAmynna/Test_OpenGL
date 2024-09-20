import static org.lwjgl.glfw.GLFW.*;

/**
 * Classe App4.
 * Transformation de matrices, rotations.
 */
public class App4 extends App {

    /**
     * Shader
     */
    private Shader shader;

    /**
     * Objet Rectangle
     */
    private Objet rectangle;

    /**
     * Construteur de la classe App.
     *
     * @param width
     * @param height
     * @param title
     */
    public App4(int width, int height, String title) {
        super(width, height, title);
    }

    /**
     * Méthode d'initialisation des shaders.
     */
    @Override
    protected void iniShaders() {
        shader = new Shader("shad4.vsh", "shad4.fsh");
    }

    /**
     * Méthode d'initialisation des objets.
     */
    @Override
    protected void iniObjects() {

        // RECTANGLE
        float [] vertices = {
             // positions        // Textures
             0.5f,  0.5f, 0.0f,  1.0f, 1.0f, // Haut droit
             0.5f, -0.5f, 0.0f,  1.0f, 0.0f, // Bas droit
            -0.5f, -0.5f, 0.0f,  0.0f, 0.0f, // Bas gauche
            -0.5f,  0.5f, 0.0f,  0.0f, 1.0f  // Haut gauche
        };
        int [] indices = {
            0, 1, 3, // Triangle 1
            1, 2, 3  // Triangle 2
        };
        rectangle = new Objet(vertices, indices, 5);
        rectangle.createTexture("eca.png");

    }

    /**
     * Méthode de gestion des entrées.
     */
    @Override
    protected void processInput() {
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetWindowShouldClose(window, true);
        }

    }

    /**
     * Méthode de rendu.
     */
    @Override
    protected void render() {

        rectangle.draw(shader);

    }

    /**
     * Méthode principale.
     * @param args
     */
    public static void main(String[] args) {
        new App4(800, 600, "App 4");

    }

}
