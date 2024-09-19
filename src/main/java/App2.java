import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Classe App2.
 * Utilisation des shaders.
 */
public class App2 extends Thread{

    /**
     * La fenêtre (le pointeur de la fenêtre)
     */
    private long window;

    /**
     * Longueur de la fenêtre
     */
    private int width = 1600;
    /**
     * Hauteur de la fenêtre
     */
    private int height = 1200;

    /**
     * Adresse des fichiers Shaders
     */
    private final String SHADERS_PATH = "src/main/resources/shaders/";
    /**
     * Vertex shader
     */
    private String vertexShader = "rainbow.vert";
    /**
     * Fragment shader
     */
    private String fragmentShader = "rainbow.frag";

    /**
     * Facteur de vitesse de la couleur
     */
    private double factor;

    /**
     * Programme de shader
     */
    private int shaderProgram;

    /**
     * Constructeur de la classe App2.
     * Start la classe
     */
    public App2() {
        factor = 0.3    ;
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

        // Initialisation des Shaders
        int vertexShader = createShader(this.vertexShader, GL20.GL_VERTEX_SHADER); // Création du vertex shader
        int fragmentShader = createShader(this.fragmentShader, GL20.GL_FRAGMENT_SHADER); // Création du fragment shader
        // Création du programme de shader
        shaderProgram = GL20.glCreateProgram(); // Création du programme de shader
        // Attachement des shaders
        GL20.glAttachShader(shaderProgram, vertexShader); // Attachement du vertex shader
        GL20.glAttachShader(shaderProgram, fragmentShader); // Attachement du fragment shader
        // Linkage du programme
        GL20.glLinkProgram(shaderProgram); // Linkage du programme
        // Vérification du linkage
        if (GL20.glGetProgrami(shaderProgram, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            // Affichage de l'erreur
            System.err.println("Erreur de linkage du programme de shader: " + GL20.glGetProgramInfoLog(shaderProgram));
            // Fermeture du programme
            System.exit(1);
        }
        // Suppression des shaders
        GL20.glDeleteShader(vertexShader); // Suppression du vertex shader
        GL20.glDeleteShader(fragmentShader); // Suppression du fragment shader

        // Initialisation des objets
        // TODO : CHANGER L'OBJET À INITIALISER
        iniRectangle(); // Initialisation du rectangle

        // MODE DE RENDU
        //GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE); // Mode de rendu

        // Activer le swap des buffers
        GLFW.glfwSwapInterval(1);

        // Rend la fenêtre visible
        GLFW.glfwShowWindow(window);
    }

    /**
     * Méthode de création de shader
     * @param path Code source du shader
     * @param type Type de shader (GL_VERTEX_SHADER, GL_FRAGMENT_SHADER)
     * @return Retourne le shader
     */
    private int createShader(String path, int type) {
        // Vérifier si le fichier existe
        if (!Files.exists(Paths.get(SHADERS_PATH + path))) {
            System.err.println("Le fichier shader n'existe pas: " + SHADERS_PATH + path);
            return -1;
        }

        // Lire le fichier shader
        String source;
        try {
            source = new String(Files.readAllBytes(Paths.get(SHADERS_PATH + path)));
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture du fichier shader: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }

        // Création du shader
        int shader = glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        // Vérification de la compilation
        if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Erreur de compilation du shader: " + GL20.glGetShaderInfoLog(shader));
            return -1;
        }

        return shader;
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
            drawRectangle(); // Dessin du rectangle

            // ICI on doit désengager le VAO utilisé, car on ne peut pas le réutiliser
            // GL30.glBindVertexArray(0); // Désengagement du VAO
            // dans notre cas on ne le désengage pas car on ne réutilise pas le VAO

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
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_P) == GLFW.GLFW_PRESS) {
            factor += 0.1;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_M) == GLFW.GLFW_PRESS) {
            factor -= 0.1;
        }
    }

    /**
     * Méthode d'initialisation du triangle
     */
    private void iniTriangle() {

        // Définition des vertices
        float [] vertices = {
                // Positions          // Couleurs
                // x    y     z       r     g     b
                 0.5f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f, // Bas gauche
                -0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f, // Bas droit
                 0.0f,  0.5f, 0.0f,  0.0f, 0.0f, 1.0f  // Haut
        };

        // Création du VBO: Vertex Buffer Object
        int VAO = GL30.glGenVertexArrays(); // Création du VAO
        int VBO = GL15.glGenBuffers(); // Création du VBO
        // Binding du VAO AVANT le VBO
        GL30.glBindVertexArray(VAO); // Binding du VAO
        // Binding du VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO); // Binding du buffer
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW); // Stockage des données
        // Interprétation des données de position
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0); // Interprétation des données
        GL20.glEnableVertexAttribArray(0); // Activation de l'attribut
        // Interprétation des données de couleur
        GL20.glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES); // Interprétation des données
        GL20.glEnableVertexAttribArray(1); // Activation de l'attribut
    }
    /**
     * Méthode de dessin du Triangle
     */
    private void drawTriangle() {
        // Utiliser le programme de shader
        GL20.glUseProgram(shaderProgram); // Utilisation du programme de shader

        // Bind le VAO du triangle
        // ICI le bind est fait dans l'initialisation du triangle
        // NE PEUX PAS ÊTRE BIND ICI car le VAO n'est pas un attribut de la classe
        // GL30.glBindVertexArray(VAO); // Binding du VAO

        // Dessin du triangle
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3); // Dessin du triangle
    }

    /**
     * Méthode d'initialisation du Rectangle
     * utiilisation du EBO
     */
    private void iniRectangle() {

        // Définition des vertices
        float [] vertices = {
                // Positions sur X, Y, Z
                0.5f,  0.5f, 0.0f,  // top right
                0.5f, -0.5f, 0.0f,  // bottom right
                -0.5f, -0.5f, 0.0f,  // bottom left
                -0.5f,  0.5f, 0.0f   // top left

        };
        int [] indices = {
                0, 1, 3, // first triangle
                1, 2, 3  // second triangle
        };

        // Création du EBO: Element Buffer Object
        int EBO = GL15.glGenBuffers(); // Création du EBO
        // Création du VAO: Vertex Array Object
        int VAO = GL30.glGenVertexArrays(); // Création du VAO
        // Création du VBO: Vertex Buffer Object
        int VBO = GL15.glGenBuffers(); // Création du VBO

        // Binding du VAO AVANT le VBO
        GL30.glBindVertexArray(VAO); // Binding du VAO

        // Copier les vertices dans le VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO); // Binding du buffer
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW); // Stockage des données

        // Copier l'index dans le EBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO); // Binding du buffer
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW); // Stockage des données

        // Interprétation des données
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0); // Interprétation des données
        GL20.glEnableVertexAttribArray(0); // Activation de l'attribut


    }
    /**
     * Méthode de dessin du Rectangle
     */
    private void drawRectangle() {
        // Utiliser le programme de shader
        useShader();

        // Bind le VAO du rectangle
        // ICI le bind est fait dans l'initialisation du rectangle
        // NE PEUX PAS ÊTRE BIND ICI car le VAO n'est pas un attribut de la classe
        // GL30.glBindVertexArray(VAO); // Binding du VAO

        // Dessin du rectangle
        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0); // Dessin du rectangle
    }

    /**
     * Méthode pour configurer et appeler le shader avant le dessin
     */
    private void useShader() {
        double timeValue = GLFW.glfwGetTime(); // Temps
        float redValue = (float) (Math.sin(timeValue * factor) * 0.5 + 0.5); // Couleur rouge
        float greenValue = (float) (Math.sin(timeValue * factor + 2.0 * Math.PI / 3.0) * 0.5 + 0.5); // Couleur verte
        float blueValue = (float) (Math.sin(timeValue * factor + 4.0 * Math.PI / 3.0) * 0.5 + 0.5); // Couleur bleue
        // Récupération de l'emplacement de la variable uniforme
        int vertexColorLocation = GL20.glGetUniformLocation(shaderProgram, "ourColor"); // Récupération de l'emplacement de la variable uniforme
        // Utiliser le programme de shader
        GL20.glUseProgram(shaderProgram); // Utilisation du programme de shader
        // Définition de la couleur
        GL20.glUniform4f(vertexColorLocation, (float) redValue , (float) greenValue, (float) blueValue , 1.0f); // Définition de la couleur
    }

    /**
     * Méthode principale de la classe.
     * Est exécutée en premier
     * @param args
     */
    public static void main(String[] args) {
        new App2();
    }
}
