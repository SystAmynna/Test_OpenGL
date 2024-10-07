package chapitre2.tools;

import chapitre1.tools.Shader;
import org.joml.Matrix4f;

import java.util.ArrayList;

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

    /**
     * Nom de la matrice de model.
     */
    private static String model = "model";
    /**
     * Nom de la matrice de view.
     */
    private static String view = "view";
    /**
     * Nom de la matrice de projection.
     */
    private static String projection = "projection";

    private static Matrix4f viewMatrix;
    private static Matrix4f projectionMatrix;

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

    @Override
    public void use() {
        super.use();
        sendViewMatrix(viewMatrix);
        sendProjectionMatrix(projectionMatrix);
    }

    /**
     * Mettre à jour la matrice de model.
     *
     * @param matrix
     */
    public static void sendModelMatrix(Matrix4f matrix) {
        shad3DSetMat4f(model, matrix);
    }
    /**
     * Mettre à jour la matrice de view.
     * @param matrix
     */
    public static void sendViewMatrix(Matrix4f matrix) {
        shad3DSetMat4f(view, matrix);
    }
    /**
     * Mettre à jour la matrice de projection.
     * @param matrix
     */
    public static void sendProjectionMatrix(Matrix4f matrix) {
        shad3DSetMat4f(projection, matrix);
    }

    /**
     * méthode générale de set de matrice.
     */
    private static void shad3DSetMat4f(String name, Matrix4f matrix) {
        if (SHADERS.isEmpty()) return;
        for (Shader3D shader : SHADERS) {
            shader.setMat4f(name, matrix);
        }
    }

    // GETTERS

    /**
     * Getter du nom de la matrice de model.
     */
    public static String getModel() {
        return model;
    }
    /**
     * Getter du nom de la matrice de view.
     */
    public static String getView() {
        return view;
    }
    /**
     * Getter du nom de la matrice de projection.
     */
    public static String getProjection() {
        return projection;
    }

    // SETTERS

    /**
     * Setter du nom de la matrice de model.
     */
    public static void setModel(String model) {
        Shader3D.model = model;
    }
    /**
     * Setter du nom de la matrice de view.
     */
    public static void setView(String view) {
        Shader3D.view = view;
    }
    /**
     * Setter du nom de la matrice de projection.
     */
    public static void setProjection(String projection) {
        Shader3D.projection = projection;
    }

    public static void setViewMatrix(Matrix4f viewMatrix) {
        Shader3D.viewMatrix = viewMatrix;
    }
    public static void setProjectionMatrix(Matrix4f projectionMatrix) {
        Shader3D.projectionMatrix = projectionMatrix;
    }


}
