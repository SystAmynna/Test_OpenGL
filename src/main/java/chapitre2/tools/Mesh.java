package chapitre2.tools;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Classe Mesh. Représente un maillage 3D.
 */
public class Mesh {

    // ATTRIBUTS

    /**
     * Liste globale des Meshes.
     */
    public static final ArrayList<Mesh> MESHES = new ArrayList<>();

    /**
     * Visibilité du maillage.
     */
    private boolean visible = true;

    // Vertex

    /**
     * Identifiant du Vertex Array Object.
     */
    private int vaoId;
    /**
     * Liste des maillages.
     */
    private int vboId;
    /**
     * Identifiant du Element Buffer Object.
     */
    private int eboId;
    /**
     * Nombre de sommets.
     */
    private int vertexCount;

    // Interprétation des données

    /**
     * Enumération des types de données.
     */
    public enum DataType {
        POSITION, COLOR, TEXTURE, NORMAL
    }
    /**
     * Liste des types de données.
     */
    private final DataType[] DATA_TYPES;
    // Templates de données
    public static final DataType[] DT_POSITION = {DataType.POSITION};
    public static final DataType[] DT_POSITION_COLOR = {DataType.POSITION, DataType.COLOR};
    public static final DataType[] DT_POSITION_TEXTURE = {DataType.POSITION, DataType.TEXTURE};
    public static final DataType[] DT_POSITION_NORMAL = {DataType.POSITION, DataType.NORMAL};
    public static final DataType[] DT_POSITION_COLOR_TEXTURE = {DataType.POSITION, DataType.COLOR, DataType.TEXTURE};
    public static final DataType[] DT_POSITION_COLOR_TEXTURE_NORMAL = {DataType.POSITION, DataType.COLOR, DataType.TEXTURE, DataType.NORMAL};

    // Matrice de transformation

    /**
     * Matrice de transformation.
     */
    private Matrix4f model;
    /**
     * Vecteur de position.
     */
    private Vector3f position;
    /**
     * Vecteur de rotation.
     */
    private Vector3f rotation;
    /**
     * Vecteur d'échelle.
     */
    private Vector3f scale;

    // Shader

    /**
     * Shader du maillage.
     */
    private Shader3D shader;
    /**
     * Liste des textures.
     */
    private final Texture[] textures; // Array of textures

    // CONSTRUCTEUR

    /**
     * Constructeur de la classe Mesh.
     * @param vertices Tableau des sommets
     * @param indices Tableau des indices
     * @param dataType Liste des types de données
     * @param textures Liste des textures
     * @param shader Shader du maillage
     */
    public Mesh(float[] vertices, int[] indices, DataType[] dataType, Texture[] textures, Shader3D shader) {
        // ajouter le maillage à la liste globale
        MESHES.add(this);
        // transformation des données
        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
        this.model = new Matrix4f();
        // Types de données
        this.DATA_TYPES = dataType;
        // textures
        this.textures = textures;
        // shader
        this.shader = shader;
        // Initialisation des buffers
        initBuffers(vertices, indices);
        // Initialisation des matrices
        updateModelMatrix();
    }

    /**
     * Initialisation des buffers.
     * @param vertices Tableau des sommets
     * @param indices Tableau des indices
     */
    private void initBuffers(float[] vertices, int[] indices) {
        // VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        // VBO
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        // EBO
        if (indices != null) {
            // EBO
            eboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
            vertexCount = indices.length;
        } else {
            vertexCount = vertices.length / 8; // 3 for position + 3 for color + 2 for texture
        }

        // Configure vertex attributes
        interpretVertexData();
    }

    /**
     * Interprétation des données des sommets.
     */
    private void interpretVertexData() {
        // décalage
        int stride = 0;
        // calculer le décalage
        for (DataType dataType: DATA_TYPES) {
            switch (dataType) {
                case POSITION, COLOR, NORMAL -> stride += 3;
                case TEXTURE -> stride += 2;
            }
        }
        stride *= Float.BYTES; // ajuster le décalage

        // index de l'attribut
        int index = 0;
        // parcours des types de données du maillage
        for (DataType dataType: DATA_TYPES) {
            switch (dataType) {
                case POSITION:
                    glVertexAttribPointer(index, 3, GL_FLOAT, false, stride, 0);
                    glEnableVertexAttribArray(index);
                    break;
                case COLOR:
                    glVertexAttribPointer(index, 3, GL_FLOAT, false, stride, 3 * Float.BYTES);
                    glEnableVertexAttribArray(index);
                    break;
                case TEXTURE:
                    glVertexAttribPointer(index, 2, GL_FLOAT, false, stride, 6 * Float.BYTES);
                    glEnableVertexAttribArray(index);
                    break;
                case NORMAL:
                    glVertexAttribPointer(index, 3, GL_FLOAT, false, stride, 8 * Float.BYTES);
                    glEnableVertexAttribArray(index);
                    break;
            }
            index++;
        }

        // Unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

    }

    /**
     * Mettre à jour la matrice de transformation.
     */
    public void updateModelMatrix() {
        model.identity();
        model.translate(position);
        model.rotateX((float) Math.toRadians(rotation.x));
        model.rotateY((float) Math.toRadians(rotation.y));
        model.rotateZ((float) Math.toRadians(rotation.z));
        model.scale(scale);
    }

    /**
     * Rendu du maillage.
     */
    public void render() {
        // si le maillage n'est pas visible, on ne le rend pas
        if (!visible) return;

        // Mettre à jour la matrice de transformation
        updateModelMatrix();

        if (shader == null) {
            System.err.println("[WARN] (Mesh.render) pas de shader attaché au maillage. Rendu impossible.");
            return;
        }
        shader.use(); // activer le shader
        shader.setMat4f("model", model); // passer la matrice de transformation au shader

        // Gestions des textures
        if (!noTexture()) {
            // Lier et activer les textures
            for (int i = 0; i < textures.length; i++) {
                if (textures[i] != null) {
                    glActiveTexture(GL_TEXTURE0 + i);
                    textures[i].bind();
                    shader.setInt("textures[" + i + "]", i);
                }
            }
        }

        // Dessiner le maillage
        glBindVertexArray(vaoId);
        if (eboId != 0) {
            glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(GL_TRIANGLES, 0, vertexCount);
        }
        glBindVertexArray(0);
    }

    /**
     * Verifier si le maillage n'a pas de texture.
     * @return
     */
    private boolean noTexture() {
        if (textures == null) return true;
        for (Texture texture : textures) {
            if (texture != null) {
                return false;
            }
        }
        return true;
    }

    public void cleanup() {
        glDeleteBuffers(vboId);
        if (eboId != 0) {
            glDeleteBuffers(eboId);
        }
        glDeleteVertexArrays(vaoId);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        updateModelMatrix();
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
        updateModelMatrix();
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
        updateModelMatrix();
    }

    public Matrix4f getModelMatrix() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setShader(Shader3D shader) {
        this.shader = shader;
    }
}