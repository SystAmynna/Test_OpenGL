import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Classe App.
 * Classe parente de toutes les applications.
 * Elle contient les méthodes et les attributs communs à toutes les applications.
 */
public abstract class App extends Thread {

    /**
     * la fenêtre (le pointeur de la fenêtre)
     */
    protected long window;

    /**
     * Longueur de la fenêtre
     */
    protected int width;
    /**
     * Hauteur de la fenêtre
     */
    protected int height;
    /**
     * Titre de la fenêtre
     */
    protected String title;

    /**
     * Construteur de la classe App.
     */
    public App(int width, int height, String title) {
        // Initialisation de la fenêtre
        this.width = width;
        this.height = height;
        this.title = title;
        // Start la classe
        this.start();
    }

    /**
     * Cours d'éxécution de la classe
     */
    @Override
    public final void run() {
        init(); // Initialisation
        loop(); // Boucle de rendu
        end(); // Fin
    }

    /**
     * Méthode d'initialisation
     */
    private final void init() {
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

        // Configurer le blending, afin de gérer les pixels transparent (alpha)
        GL11.glEnable(GL11.GL_BLEND); // Activer le blending
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // Configurer la fonction de blending

        GL11.glEnable(GL11.GL_DEPTH_TEST); // Activer le test de profondeur



        // Initialisation spécifique à l'app
        ini();

        // Activer le swap des buffers
        GLFW.glfwSwapInterval(1);

        // Rend la fenêtre visible
        GLFW.glfwShowWindow(window);
    }



    /**
     * Méthode d'initialisation spécifique à l'app.
     */
    protected abstract void ini();

    /**
     * Méthode de fin du programme.
     * Libèration des ressources
     */
    private final void end() {
        // Libération des ressources
        if (!Shader.SHADERS.isEmpty()) for (Shader shader : Shader.SHADERS) {
            shader.delete();
        }
        // Libération des ressources de la fenêtre
        GLFW.glfwDestroyWindow(window);
        // Libération des ressources de GLFW
        GLFW.glfwTerminate();
        // Libération des ressources du callback
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    /**
     * Méthode de boucle de rendu
     */
    private final void loop() {
        // Boucle de rendu
        while (!GLFW.glfwWindowShouldClose(window)) {
            // EFFACER LE BUFFER
            GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f); // Couleur de fond
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Effacer le buffer de couleur

            // Gestion des contrôles
            processInput();

            // RENDU --------------------

            render();

            // ---------------------------

            // Swap des buffers
            GLFW.glfwSwapBuffers(window);

            // Gestion des événements
            GLFW.glfwPollEvents();
        }
    }

    /**
     * Méthode qui gère les inputs
     */
    protected abstract void processInput();

    /**
     * Méthode de rendu
     */
    protected abstract void render();

}
