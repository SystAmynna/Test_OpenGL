package chapitre2.tools;

import chapitre1.tools.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe Shader3D. Réprésente un shader 3D.
 * Cette classe sert à faciliter la mise à jour des matrices de transformation.
 */
public class Shader3D extends Shader {

    // ATTRIBUTS

    /**
     * Liste des shaders 3D.
     */
    public static final ArrayList<Shader3D> SHADERS = new ArrayList<Shader3D>();

    // Matrices

    /**
     * Nom de la matrice de model.
     */
    protected static String model = "model";
    /**
     * Nom de la matrice de view.
     */
    protected static String view = "view";
    /**
     * Nom de la matrice de projection.
     */
    protected static String projection = "projection";

    /**
     * Matrice de view.
     */
    private static Matrix4f viewMatrix;
    /**
     * Matrice de projection.
     */
    private static Matrix4f projectionMatrix;

    // Uniforms

    /**
     * Liste des uniforms.
     */
    private final HashMap<String, Object> UNIFORMS = new HashMap<>();

    // CONSTRUCTEUR

    /**
     * Constructeur de la classe chapitre1.tools.Shader.
     *
     * @param vertex
     * @param fragment
     */
    public Shader3D(String vertex, String fragment) {
        super(vertex, fragment);
        SHADERS.add(this);
    }

    // METHODES

    /**
     * Méthode pour utiliser, activer le shader.
     */
    @Override
    public void use() {
        // activer le programme shader
        super.use();
        // envoyer les matrices
        setMat4f(view, viewMatrix);
        setMat4f(projection, projectionMatrix);
        // envoyer les uniforms
        for (Map.Entry<String, Object> entry : UNIFORMS.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Integer) setInt(key, (int) value);
            if (value instanceof Float) setFloat(key, (float) value);
            if (value instanceof Vector3f) setVec3(key, (Vector3f) value);
            if (value instanceof Matrix4f) setMat4f(key, (Matrix4f) value);
        }
    }

    public static void globalSetMat4f(String name, Matrix4f value) {
        if (!SHADERS.isEmpty()) for (Shader3D shader : SHADERS) {
            shader.setMat4f(name, value);
        }
    }

    // UNIFORMS

    /**
     * Méthode pour ajouter un uniform.
     * @param name
     * @param value
     */
    public void addUniform(String name, Object value) {
        UNIFORMS.put(name, value);
    }

    public void removeUniform(String name) {
        UNIFORMS.remove(name);
    }

    // GETTERS


    // SETTERS

    public static void setViewMatrix(Matrix4f viewMatrix) {
        Shader3D.viewMatrix = viewMatrix;
    }
    public static void setProjectionMatrix(Matrix4f projectionMatrix) {
        Shader3D.projectionMatrix = projectionMatrix;
    }


}
