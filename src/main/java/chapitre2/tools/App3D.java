package chapitre2.tools;

import chapitre1.tools.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class App3D extends Thread {

    // FENETRE
    protected long window;
    protected int width, height;
    String title;

    // TEMPS
    float deltaTime = 0.0f;
    float lastFrame = 0.0f;

    // CAMERA
    protected Camera camera;
    boolean outWin = false;
    boolean outWinPressed = false;

    public App3D(String title, int width, int height) {
        super();
        // Initialisation des valeurs par défaut
        this.title = title;
        this.width = width;
        this.height = height;
        // démarre la classe
        this.start();
    }

    @Override
    public void run() {
        init();
        loop();
        end();
    }

    private void init() {
        // Callback des erreurs GLFW
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialisation de GLFW
        if (!GLFW.glfwInit()) throw new IllegalStateException("Impossible d'initialiser GLFW");

        // Config de la fenêtre
        GLFW.glfwDefaultWindowHints(); // Utiliser les paramètres par défaut.
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3); // Version d'OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3); // Version d'OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE); // Profile OpenGL
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // cache la fenêtre
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // redimensionnable

        // Création de la fenêtre
        window = glfwCreateWindow(width, height, title, NULL, NULL); // Initialisation de la fenêtre
        if (window == NULL) throw new RuntimeException("Impossible de créer la fenêtre GLFW");

        // Centre la fenêtre
        final int w = Objects.requireNonNull(GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())).width();
        final int h = Objects.requireNonNull(GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())).height();
        GLFW.glfwSetWindowPos(window, (w - width) / 2, (h - height) / 2);

        // initialise le contexte OpenGL, le passe en contexte courant
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities(); // Crée les capacités OpenGL (gère les pointeurs comme GLAD)

        // Definir la zone de rendu de la fenêtre
        GL11.glViewport(0, 0, width, height); // ici toute la fenêtre
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

        // Profondeur (éviter le chevauchement des éléments)
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Activer le test de profondeur


        // GESTION CAMERA -------------------------------------------------------

        // Désactive la souris sur la fenêtre
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        // Camera
        camera = new Camera(new Vector3f(0.0f, 0.0f, 3.0f));

        // matrice de projection
        Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) width / (float) height, 0.1f, 100.0f);
        Shader3D.setProjectionMatrix(projection);
        // Callback du scroll de la souris
        GLFW.glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
            camera.processMouseScroll((float) yoffset);
            Matrix4f p = new Matrix4f().perspective((float) Math.toRadians(camera.getFov()), (float) width / (float) height, 0.1f, 100.0f);
            //Shader.getCurrentShader().setMat4f("projection", p);
            Shader3D.setProjectionMatrix(p);
        });

        // matrice de vue
        Matrix4f view = camera.getViewMatrix();
        Shader3D.setViewMatrix(view);
        // Callback du mouvement de la souris
        GLFW.glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            if (outWin) {
                camera.setFirstMouse(true);
                return;
            }
            camera.processMouseMovement((float) xpos, (float) ypos);
            Matrix4f v = camera.getViewMatrix();
            //Shader.getCurrentShader().setMat4f("view", v);
            Shader3D.setViewMatrix(v);
        });

        // Initialisation spécifique à l'app
        ini();
        // verification de l'initialisation
        if (Shader.SHADERS.isEmpty()) throw new RuntimeException("Aucun shader n'a été chargé");
        if (Shader3D.SHADERS.isEmpty()) System.err.println("[WARN] (App3D) : Aucun shader 3D n'a été chargé");

        // -----------------------------------------------------------------------

        // Activer le swap des buffers
        GLFW.glfwSwapInterval(1);

        // Rend la fenêtre visible
        GLFW.glfwShowWindow(window);
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            // Temps
            float currentFrame = (float) GLFW.glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            // Nettoyer l'écran
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            // definir le fond de la fenêtre
            GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

            // Gestion des entrées
            defaultProcessInput();
            processInput();

            // mise à jour de la matrice de vue
            Shader3D.globalSetMat4f(Shader3D.view, camera.getViewMatrix());

            // update des objets
            update();

            // Rendu
            for (Mesh m : Mesh.MESHES) {
                m.render();
            }

            // Swap des buffers
            GLFW.glfwSwapBuffers(window);

            // Poll des événements
            GLFW.glfwPollEvents();
        }
    }

    private void end() {
        // Libération des ressources
        if (!Shader.SHADERS.isEmpty()) for (int i = 0; i < Shader.SHADERS.size(); i++) {
            Shader.SHADERS.get(i).delete();
        }
        // Libération des ressources de la fenêtre
        GLFW.glfwDestroyWindow(window);
        // Libération des ressources de GLFW
        GLFW.glfwTerminate();
        // Libération des ressources du callback
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    private void defaultProcessInput() {
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS) {
            GLFW.glfwSetWindowShouldClose(window, true);
        }
        // libère la souris ou la bloque
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS && !outWinPressed) {
            outWinPressed = true;
            if (outWin) {
                GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            } else {
                GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
                // Centrer la souris
                GLFW.glfwSetCursorPos(window, width / 2.0, height / 2.0);
            }
            outWin = !outWin;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_RELEASE) {
            outWinPressed = false;
        }
        camera.processKeyboard(window, deltaTime);
    }

    protected abstract void ini();
    protected abstract void processInput();
    protected abstract void update();
}