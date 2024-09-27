package chapitre1.tools;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

/**
 * Classe chapitre1.tools.Objet.
 * Cette classe désigne un objet dessinable.
 */
public class Objet {

    /**
     * vSize Coordonnées seules
     */
    public static final int VSIZE_COORD = 3;
    /**
     * vSize Coordonnées et couleur
     */
    public static final int VSIZE_COORD_COLOR = 6;
    /**
     * vSize Coordonnées, couleur et Texture
     */
    public static final int VSIZE_COORD_COLOR_TEXTURE = 8;

    /**
     * Chemin des sprites
     */
    private static String TEXTURE_PATH = "src/main/resources/chapitre1/textures/";

    /**
     * VAO: Vertex Array Object.
     */
    private final int VAO;

    /**
     * VBO: Vertex Buffer Object.
     */
    private final int VBO;

    /**
     * EBO: Element Buffer Object.
     */
    private final int EBO;
    /**
     * Nombre d'indices.
     */
    private final int IC;

    /**
     * Texture de l'objet.
     */
    private int texture1 = -1;
    private int texture2 = -1;

    /**
     * Constructeur avec indices.
     * @param vertices Tableau de float contenant les données des sommets de l'objet.
     * @param indices Tableau d'entiers contenant les indices des sommets.
     * @param vSize Taille des données d'un sommet (dans vertices).
     */
    public Objet(float [] vertices, int [] indices, int vSize) {
        assert indices != null;
        // Création du EBO
        EBO = GL15.glGenBuffers();
        IC = indices.length;
        // Création du VAO
        VAO = GL30.glGenVertexArrays();
        // Création du VBO
        VBO = GL15.glGenBuffers();

        // Binding du VAO
        GL30.glBindVertexArray(VAO);
        // Binding du VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, VBO);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW); // stockage des données
        // Binding du EBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, EBO);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW); // stockage des indices

        // interpretation des données
        interpretData(vSize);
    }


    /**
     * Interpretation des données.
     * @param vSize Taille des données d'un sommet (dans vertices).
     */
    private void interpretData(int vSize) {
        // INTERPRETATION DES DONNEES
        // position
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, vSize * Float.BYTES, 0); // Interprétation des données
        GL20.glEnableVertexAttribArray(0); // Activation de l'attribut

        // couleur
        if (vSize > 3) {
            GL20.glVertexAttribPointer(1, 3, GL_FLOAT, false, vSize * Float.BYTES, 3 * Float.BYTES); // Interprétation des données
            GL20.glEnableVertexAttribArray(1); // Activation de l'attribut
        }
        // texture
        if (vSize > 6) {
            GL20.glVertexAttribPointer(2, 2, GL_FLOAT, false, vSize * Float.BYTES, 6 * Float.BYTES); // Interprétation des données
            GL20.glEnableVertexAttribArray(2); // Activation de l'attribut
        }
    }

    /**
     * Méthode de création de la texture
     */
    public void createTexture(String file) {
        texture1 = loadTexture(file, false);
    }
    public void createTexture2(String file) {
        texture2 = loadTexture(file, false);
    }

    /**
     * Destructeur de la classe chapitre1.tools.Objet.
     */
    public void destroy() {
        GL30.glDeleteVertexArrays(VAO); // Suppression du VAO
        GL15.glDeleteBuffers(VBO); // Suppression du VBO
        GL15.glDeleteBuffers(EBO); // Suppression du EBO
        GL30.glDeleteTextures(texture1); // Suppression de la texture
        GL30.glDeleteTextures(texture2); // Suppression de la texture
    }

    /**
     * Dessine l'objet.
     */
    public void draw(Shader shader) {

        // utiliser le shader
        shader.use();

        // dessiner
        if (texture1 != -1 && texture2 != -1) {
            // Definir les textures à utiliser dans l'uniform
            shader.setInt("texture1", 0);
            shader.setInt("texture2", 1);
            // Activer les textures
            GL30.glActiveTexture(GL30.GL_TEXTURE0); // Activer la texture 0
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture1); // Binding de la texture
            GL30.glActiveTexture(GL30.GL_TEXTURE1); // Activer la texture 1
            GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture2); // Binding de la texture
            // TEST
            rb(shader);
        }


        //processMatrix(shader); // Matrices de transformation

        _draw(VAO, IC); // Dessin
    }

    private void _draw(int VAO, int IC) {
        GL30.glBindVertexArray(VAO); // Binding du VAO
        GL11.glDrawElements(GL11.GL_TRIANGLES, IC, GL11.GL_UNSIGNED_INT, 0); // Dessin
        //GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 36); // Dessin
    }

    /**
     * Méthode qui traite les matrices de transformation
     */
    private void processMatrix(Shader shader) {

        // Cette méthode a servi lors des tentatives de l'App4, le code des simples rotations n'est plus présent

        Matrix4f model = new Matrix4f().identity();
        Matrix4f view = new Matrix4f().identity();
        Matrix4f projection = new Matrix4f().identity();

        model.rotate((float) GLFW.glfwGetTime(), new Vector3f(0.5f, 1.0f, 0.0f));
        view.translate(new Vector3f(0.0f, 0.0f, -3.0f));
        projection.perspective((float) Math.toRadians(45.0f), 1600.0f / 1200.0f, 0.1f, 100.0f);

        shader.setMat4f("model", model.get(new float[16]));
        shader.setMat4f("view", view.get(new float[16]));
        shader.setMat4f("projection", projection.get(new float[16]));


    }


    /**
     * Dessine l'objet en mode fil de fer.
     */
    public void drawWireframe(Shader shader) {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE); // Mode de rendu
        draw(shader); // Dessin
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL); // Reset le mode de rendu
    }

    /**
     * Méthode pour charger une texture
     * @param file Nom du fichier de la texture
     * @return Identifiant de la texture
     */
    public static int loadTexture(String file, boolean flip) {
        // Génération de la texture
        int texture = GL11.glGenTextures();
        // Binding de la texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        // CONFIG TEXTURE
        // répétition de la texture si elle est plus petite que l'objet
        // utiliser GL14 pour le dernier paramètre
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_REPEAT); // répétition de la texture X
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL14.GL_REPEAT); // répétition de la texture Y
        // Filtre de la texture, interpolation linéaire
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_NEAREST); //
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // Filtre de la texture

        // CHARGER L'IMAGE
        // création des buffers
        IntBuffer w = MemoryStack.stackMallocInt(1);
        IntBuffer h = MemoryStack.stackMallocInt(1);
        IntBuffer comp = MemoryStack.stackMallocInt(1);
        // Retourne l'image si demandé (OpenGL a l'origine est en bas à gauche)
        STBImage.stbi_set_flip_vertically_on_load(!flip); // retourne par défaut en raison de l'inversement de l'image par rapport à OpenGL
        // Charger l'image
        // 0 pour indiquer que l'image est chargée tel quel
        ByteBuffer image = STBImage.stbi_load(TEXTURE_PATH + file, w, h, comp, 4);
        if (image == null) {
            throw new RuntimeException("ERREUR: (chapitre1.tools.Objet) Impossible de charger l'image " + file);
        }
        // Stockage de l'image
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w.get(), h.get(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
        // Générer les mipmaps
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D); // Générer les mipmaps
        // Libération de la mémoire
        STBImage.stbi_image_free(image);

        // Retourner la texture
        return texture;
    }

    /**
     * Méthode pour configurer et appeler le shader avant le dessin
     * METHODE TEMPORAIRE
     */
    private void rb(Shader shader) {
        double factor = 5.0; // Facteur de vitesse
        double timeValue = GLFW.glfwGetTime(); // Temps
        float redValue = (float) (Math.sin(timeValue * factor) * 0.5 + 0.5); // Couleur rouge
        float greenValue = (float) (Math.sin(timeValue * factor + 2.0 * Math.PI / 3.0) * 0.5 + 0.5); // Couleur verte
        float blueValue = (float) (Math.sin(timeValue * factor + 4.0 * Math.PI / 3.0) * 0.5 + 0.5); // Couleur bleue
        // Récupération de l'emplacement de la variable uniforme
        shader.setVec3("rainbow", redValue, greenValue, blueValue); // Passage de la couleur
    }

    /**
     * Redéfinir le chemin des textures
     */
    public static void setTexturePath(String path) {
        TEXTURE_PATH = path;
    }


}
