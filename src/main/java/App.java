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
 * Elle contient les m�thodes et les attributs communs � toutes les applications.
 */
public abstract class App extends Thread {

    /**
     * la fen�tre (le pointeur de la fen�tre)
     */
    protected long window;

    /**
     * Longueur de la fen�tre
     */
    protected int width;
    /**
     * Hauteur de la fen�tre
     */
    protected int height;
    /**
     * Titre de la fen�tre
     */
    protected String title;

    /**
     * Construteur de la classe App.
     */
    public App(int width, int height, String title) {
        // Initialisation de la fen�tre
        this.width = width;
        this.height = height;
        this.title = title;
        // Start la classe
        this.start();
    }

    /**
     * Cours d'�x�cution de la classe
     */
    @Override
    public final void run() {
        init(); // Initialisation
        loop(); // Boucle de rendu
        end(); // Fin
    }

    /**
     * M�thode d'initialisation
     */
    private final void init() {
        // Callback des erreurs GLFW
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialisation de GLFW
        if (!GLFW.glfwInit()) { // SI l'initialisation de GLFW �choue
            throw new IllegalStateException("Impossible d'initialiser GLFW");
        }

        // Config de la fen�tre
        GLFW.glfwDefaultWindowHints(); // Utiliser les param�tres par d�faut.
        // Utilisation de la version 3.3 d'OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3); // Version d'OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3); // Version d'OpenGL
        // Profile OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        // Affichage de la fen�tre
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        // Cr�ation de la fen�tre
        window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL); // Initialisation de la fen�tre
        if (window == NULL) { // si la fen�tre n'est pas cr��e
            throw new RuntimeException("Impossible de cr�er la fen�tre GLFW");
        }

        // Centre la fen�tre
        GLFW.glfwSetWindowPos(window, 650, 300);

        // initialise le contexte OpenGL, le passe en contexte courant
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities(); // Cr�e les capacit�s OpenGL (g�re les pointeurs comme GLAD)

        // Definir la zone de rendu de la fen�tre
        GL11.glViewport(0, 0, width, height); // ici toute la fen�tre
        // Origine en bas � gauche

        // Appeller la m�thode de redimensionnement � chaque redimensionnement de la fen�tre
        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            // Red�finir les valeurs de la taille de la fen�tre
            this.width = width;
            this.height = height;
            // Red�finir la zone de rendu
            GL11.glViewport(0, 0, width, height);
        });

        // Configurer le blending, afin de g�rer les pixels transparent (alpha)
        GL11.glEnable(GL11.GL_BLEND); // Activer le blending
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // Configurer la fonction de blending

        GL11.glEnable(GL11.GL_DEPTH_TEST); // Activer le test de profondeur



        // Initialisation sp�cifique � l'app
        ini();

        // Activer le swap des buffers
        GLFW.glfwSwapInterval(1);

        // Rend la fen�tre visible
        GLFW.glfwShowWindow(window);
    }



    /**
     * M�thode d'initialisation sp�cifique � l'app.
     */
    protected abstract void ini();

    /**
     * M�thode de fin du programme.
     * Lib�ration des ressources
     */
    private final void end() {
        // Lib�ration des ressources
        if (!Shader.SHADERS.isEmpty()) for (Shader shader : Shader.SHADERS) {
            shader.delete();
        }
        // Lib�ration des ressources de la fen�tre
        GLFW.glfwDestroyWindow(window);
        // Lib�ration des ressources de GLFW
        GLFW.glfwTerminate();
        // Lib�ration des ressources du callback
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    /**
     * M�thode de boucle de rendu
     */
    private final void loop() {
        // Boucle de rendu
        while (!GLFW.glfwWindowShouldClose(window)) {
            // EFFACER LE BUFFER
            GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f); // Couleur de fond
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Effacer le buffer de couleur

            // Gestion des contr�les
            processInput();

            // RENDU --------------------

            render();

            // ---------------------------

            // Swap des buffers
            GLFW.glfwSwapBuffers(window);

            // Gestion des �v�nements
            GLFW.glfwPollEvents();
        }
    }

    /**
     * M�thode qui g�re les inputs
     */
    protected abstract void processInput();

    /**
     * M�thode de rendu
     */
    protected abstract void render();

}
