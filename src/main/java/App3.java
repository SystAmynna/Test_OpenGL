import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.*;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Classe App3.
 * Utilisation des shaders.
 */
public class App3 extends Thread{

    /**
     * La fenêtre (le pointeur de la fenêtre)
     */
    private long window;

    /**
     * Longueur de la fenêtre
     */
    private int width = 1200;
    /**
     * Hauteur de la fenêtre
     */
    private int height = 1200;

    /**
     * Programme de shader
     */
    private Shader shader;
    /**
     * Programme de shader Wireframe
     */
    private Shader shaderWireframe;

    /**
     * Debug
     */
    private boolean debug = false;

    /**
     * Triangle à dessiner
     */
    private Objet triangle;
    /**
     * Rectangle à dessiner
     */
    private Objet rectangle;
    /**
     * Hexagone à dessiner
     */
    private Objet hexagone;

    /**
     * Constructeur de la classe App3.
     * Start la classe
     */
    public App3() {
        this.start();
    }

    /**
     * Cours d'éxécution de la classe
     */
    @Override
    public void run() {
        init(); // Initialisation
        loop(); // Boucle de rendu

        // libération des ressources
        GLFW.glfwDestroyWindow(window); // Destruction de la fenêtre
        GLFW.glfwTerminate(); // Terminaison de GLFW
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free(); // Libération de la mémoire
    }

    /**
     * Initialisation de la fenêtre
     */
    private void init() {
        // Callback des erreurs GLFW
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialisation de GLFW
        if (!GLFW.glfwInit()) { // SI l'initialisation de GLFW échoue
            throw new IllegalStateException("Impossible d'initialiser GLFW");
        }

        // Config de la fenêtre
        GLFW.glfwDefaultWindowHints(); // Utiliser les paramètres par défaut.
        // Utilisation de la version 3.3 d'OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3); // Version d'OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3); // Version d'OpenGL
        // Profile OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        // Affichage de la fenêtre
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        // Création de la fenêtre
        window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL); // Initialisation de la fenêtre
        if (window == NULL) { // si la fenêtre n'est pas créée
            throw new RuntimeException("Impossible de créer la fenêtre GLFW");
        }

        // Centre la fenêtre
        GLFW.glfwSetWindowPos(window, 650, 300);

        // initialise le contexte OpenGL, le passe en contexte courant
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities(); // Crée les capacités OpenGL (gère les pointeurs comme GLAD)

        // Definir la zone de rendu de la fenêtre
        GL11.glViewport(0, 0, width, height); // ici toute la fenêtre
        // Origine en bas à gauche

        // Appeller la méthode de redimensionnement à chaque redimensionnement de la fenêtre
        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            // Redéfinir les valeurs de la taille de la fenêtre
            this.width = width;
            this.height = height;
            // Redéfinir la zone de rendu
            GL11.glViewport(0, 0, width, height);
        });

        // Création des shaders
        shader = new Shader("shad3.vsh", "shad3.fsh");
        shaderWireframe = new Shader("shad1.vert", "shad1.frag");

        // Initialisation des objets
        iniObjects();


        // Configurer le blending, afin de gérer les pixels transparent (alpha)
        GL11.glEnable(GL11.GL_BLEND); // Activer le blending
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // Configurer la fonction de blending


        // Activer le swap des buffers
        GLFW.glfwSwapInterval(1);

        // Rend la fenêtre visible
        GLFW.glfwShowWindow(window);
    }

    /**
     * Initialiser les objets
     */
    private void iniObjects() {
        // TRIANGLE
        // Définition des vertices
        float [] vertices = {
                // Positions          // Couleurs
                // x    y     z       r     g     b
                -0.9f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f, // Bas gauche
                -0.1f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f, // Bas droit
                -0.5f,  0.5f, 0.0f,  0.0f, 0.0f, 1.0f  // Haut
        };
        int [] indices = {
            0, 1, 2 // Triangle
        };
        // Création de l'objet triangle
        triangle = new Objet(vertices, indices,6);

        // RECTANGLE
        // Définition des vertices
        float nbtf = 1.0f;
        float[] vertices2 = {
            // Positions        // Couleurs         // Coordonnées de texture
            0.5f,  0.5f, 0.0f,  1.0f, 0.0f, 0.0f,  nbtf, nbtf, // Haut droit
            0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f,  nbtf, 0.0f, // Bas droit
           -0.5f, -0.5f, 0.0f,  0.0f, 0.0f, 1.0f,  0.0f, 0.0f, // Bas gauche
           -0.5f,  0.5f, 0.0f,  1.0f, 1.0f, 0.0f,  0.0f, nbtf  // Haut gauche
        };

        // Definition des indices
        int [] indices2 = {
            0, 1, 3, // Triangle 1
            1, 2, 3  // Triangle 2
        };
        // Création de l'objet triangle
        rectangle = new Objet(vertices2, indices2, 8);
        rectangle.createTexture("wood.png");
        rectangle.createTexture2("wall.png");

        // HEXAGONE
        // Définition des vertices
        float [] vertices3 = {
                // Positions          // Couleurs
                // x    y     z       r     g     b
                0.0f, 0.5f, 0.0f,  1.0f, 0.0f, 0.0f, // Haut
                0.3f, 0.25f, 0.0f,  1.0f, 1.0f, 0.0f, // Haut droit
                0.3f, -0.25f, 0.0f,  0.0f, 1.0f, 0.0f, // Bas droit
                0.0f, -0.5f, 0.0f,  0.0f, 1.0f, 1.0f, // Bas
                -0.3f, -0.25f, 0.0f,  0.0f, 0.0f, 1.0f, // Bas gauche
                -0.3f, 0.25f, 0.0f,  1.0f, 0.0f, 1.0f // Haut gauche
        };
        // Definition des indices
        int [] indices3 = {
            0, 1, 5, // Triangle 1
            1, 2, 5, // Triangle 2
            2, 3, 5, // Triangle 3
            3, 4, 5 // Triangle 4
        };
        // Création de l'objet hexagone
        hexagone = new Objet(vertices3, indices3, 6);

    }

    /**
     * Boucle de rendu
     */
    private void loop() {
        // Boucle de rendu
        while (!GLFW.glfwWindowShouldClose(window)) {
            // EFFACER LE BUFFER
            GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f); // Couleur de fond
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); // Effacer le buffer de couleur

            // Gestion des contrôles
            processInput();

            // RENDU --------------------
            // TODO : CHANGER L'OBJET À DESSINER
            // Dessin du triangle
            rectangle.draw(shader);
            if (debug) rectangle.drawWireframe(shaderWireframe);


            // ---------------------------

            // Swap des buffers
            GLFW.glfwSwapBuffers(window);

            // Gestion des événements
            GLFW.glfwPollEvents();
        }
    }

    /**
     * Méthode qui traite les touches pressées
     */
    private void processInput() {
        // Liste des touches : https://www.glfw.org/docs/latest/group__keys.html
        // Verifier si la touche ECS est pressée
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            // Fermer la fenêtre
            GLFW.glfwSetWindowShouldClose(window, true);
        }
        debug = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS;
        shader.setBool("damaged", GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS);
    }






    /**
     * Méthode principale de la classe.
     * Est exécutée en premier
     * @param args
     */
    public static void main(String[] args) {
        new App3();
    }
}
