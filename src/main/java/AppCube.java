import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.Objects;
import java.util.Vector;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.system.MemoryUtil.NULL;

public class AppCube extends Thread{

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
     * Shader
     */
    private Shader shader;

    /**
     * Construteur de la classe App.
     */
    public AppCube(int width, int height, String title) {
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



    int texture1, texture2;
    int VAO;

    Vector3f cameraPos, cameraTarget, cameraDirection, cameraUp, cameraRight;
    Matrix4f view;


    protected void ini() {

        float [] vertices = {
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
                 0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
                 0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                 0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                 0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                 0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                 0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                 0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                 0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                 0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                 0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                 0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
                 0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                 0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                 0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                 0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        };

        int VBO;
        VAO = GL30.glGenVertexArrays();
        VBO = GL30.glGenBuffers();

        GL30.glBindVertexArray(VAO);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, VBO);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW);

        GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 5 * Float.BYTES, 0);
        GL30.glEnableVertexAttribArray(0);

        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        GL30.glEnableVertexAttribArray(1);


        texture1 = Objet.loadTexture("eca.png", false);

        shader = new Shader("shadCube.vsh", "shadCube.fsh");
        shader.use();
        shader.setInt("texture1", 0);

        cameraPos = new Vector3f(0.0f, 0.0f, 3.0f);
        cameraTarget = new Vector3f(0.0f, 0.0f, 0.0f);
        cameraDirection = new Vector3f(cameraPos).sub(cameraTarget).normalize();
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        cameraRight = new Vector3f(up).cross(cameraDirection).normalize();
        cameraUp = new Vector3f(cameraDirection).cross(cameraRight).normalize();

        view = new Matrix4f().lookAt(cameraPos, cameraTarget, cameraUp);

        Matrix4f projection = new Matrix4f();
        projection.perspective((float) Math.toRadians(45.0f), (float) width / (float) height, 0.1f, 100.0f);
        shader.setMat4("projection", projection.get(new float[16]));

    }


    protected void processInput() {}


    protected void render() {

        GL30.glActiveTexture(texture1);
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture1);

        shader.use();

        Matrix4f model = new Matrix4f();
        view = new Matrix4f();


        float radius = 10.0f;
        float camX = (float) Math.sin(GLFW.glfwGetTime()) * radius;
        float camZ = (float) Math.cos(GLFW.glfwGetTime()) * radius;
        view = new Matrix4f().lookAt(new Vector3f(camX, 0.0f, camZ), new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f));

        shader.setMat4("model", model.get(new float[16]));
        shader.setMat4("view", view.get(new float[16]));


        GL30.glBindVertexArray(VAO);

        Vector3f [] cubePositions = {
                new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f(2.0f, 5.0f, -15.0f),
                new Vector3f(-1.5f, -2.2f, -2.5f),
                new Vector3f(-3.8f, -2.0f, -12.3f),
                new Vector3f(2.4f, -0.4f, -3.5f),
                new Vector3f(-1.7f, 3.0f, -7.5f),
                new Vector3f(1.3f, -2.0f, -2.5f),
                new Vector3f(1.5f, 2.0f, -2.5f),
                new Vector3f(1.5f, 0.2f, -1.5f),
                new Vector3f(-1.3f, 1.0f, -1.5f)
        };

        for (int i = 0; i < cubePositions.length; i++) {
            model = new Matrix4f();
            model.translate(cubePositions[i]);
            float angle = 20.0f * i;

            float [] vect = {1.0f, 0.3f, 0.5f};
            float length = (float) Math.sqrt(vect[0] * vect[0] + vect[1] * vect[1] + vect[2] * vect[2]);
            model.rotate((float) (GLFW.glfwGetTime() * Math.toRadians(angle)), vect[0] / length, vect[1] / length, vect[2] / length);

            shader.setMat4("model", model.get(new float[16]));
            GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 36);
        }


        //GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 36);


    }



    public static void main(String[] args) {
       new AppCube(800, 600, "Cube");
    }


}
