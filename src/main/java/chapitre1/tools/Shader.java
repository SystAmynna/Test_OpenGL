package chapitre1.tools;

import org.lwjgl.opengl.GL20;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Classe de gestion des Shaders.
 */
public class Shader {

    /**
     * Liste des shaders.
     */
    public static final ArrayList<Shader> SHADERS = new ArrayList<>();

    /**
     * Chemin vers les shaders.
     */
    private static String path = "src/main/resources/chapitre1/shaders/";

    /**
     * chapitre1.tools.Shader courant.
     */
    private static Shader currentShader;

    /**
     * Identifiant du shader.
     */
    public final int ID;

    /**
     * Constructeur de la classe chapitre1.tools.Shader.
     */
    public Shader(String vertex, String fragment) {
        // Initialisation des fichiers
        File vertexFile = new File(path + vertex);
        File fragmentFile = new File(path + fragment);
        // Verifier l'existence des fichiers
        if (!vertexFile.exists() || !fragmentFile.exists()) {
            System.err.println("Erreur: Fichier introuvable.");
            System.exit(1);
        }
        // Récupération de la source des shaders
        String vertexSource = "", fragmentSource = "";
        try {
            vertexSource = new String(Files.readAllBytes(vertexFile.toPath()));
            fragmentSource = new String(Files.readAllBytes(fragmentFile.toPath()));
        } catch (Exception e) {
            System.err.println("Erreur: Impossible de lire le fichier.");
            System.exit(1);
        }
        // Création du shader
        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        // assignation de la source
        GL20.glShaderSource(vertexShader, vertexSource);
        GL20.glShaderSource(fragmentShader, fragmentSource);
        // Compilation du shader
        GL20.glCompileShader(vertexShader);
        GL20.glCompileShader(fragmentShader);
        // Vérification de la compilation
        if (GL20.glGetShaderi(vertexShader, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Erreur: Impossible de compiler le vertex shader.");
            System.err.println(GL20.glGetShaderInfoLog(vertexShader));
            System.exit(1);
        }
        if (GL20.glGetShaderi(fragmentShader, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            System.err.println("Erreur: Impossible de compiler le fragment shader.");
            System.err.println(GL20.glGetShaderInfoLog(fragmentShader));
            System.exit(1);
        }

        // Création du programme
        ID = GL20.glCreateProgram();
        // Attachement des shaders
        GL20.glAttachShader(ID, vertexShader);
        GL20.glAttachShader(ID, fragmentShader);
        // Linkage du programme
        GL20.glLinkProgram(ID);
        // Vérification du linkage
        if (GL20.glGetProgrami(ID, GL20.GL_LINK_STATUS) == GL20.GL_FALSE) {
            System.err.println("Erreur: Impossible de linker le programme.");
            System.err.println(GL20.glGetProgramInfoLog(ID));
            System.exit(1);
        }
        // Suppression des shaders
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);

        // ajout à la liste des shaders
        SHADERS.add(this);
    }

    /**
     * Méthode d'activation / utilisation du shader.
     */
    public void use() {
        if (currentShader == this) return;
        GL20.glUseProgram(ID);
        currentShader = this;
    }

    /**
     * Méthode de désactivation / arrêt du shader.
     */
    public static void stop() {
        GL20.glUseProgram(0);
    }

    /**
     * Méthode pour supprimer le shader.
     */
    public void delete() {
        SHADERS.remove(this);
        GL20.glDeleteProgram(ID);
    }

    /**
     * Setter du chemin des shaders.
     */
    public static void setPath(String path) {
        Shader.path = path;
    }

    /**
     * Getter du currentShader.
     */
    public static Shader getCurrentShader() {
        return currentShader;
    }


    // OUTILS d'uniformes

    /**
     * Méthode de définition d'un uniforme de type booléen.
     * @param name Nom de l'uniforme.
     * @param value Valeur de l'uniforme.
     */
    public void setBool(String name, boolean value) {
        GL20.glUniform1i(GL20.glGetUniformLocation(ID, name), value ? 1 : 0);
    }

    /**
     * Méthode de définition d'un uniforme de type entier.
     * @param name Nom de l'uniforme.
     * @param value Valeur de l'uniforme.
     */
    public void setInt(String name, int value) {
        GL20.glUniform1i(GL20.glGetUniformLocation(ID, name), value);
    }

    /**
     * Méthode de définition d'un uniforme de type flottant.
     * @param name Nom de l'uniforme.
     * @param value Valeur de l'uniforme.
     */
    public void setFloat(String name, float value) {
        GL20.glUniform1f(GL20.glGetUniformLocation(ID, name), value);
    }

    /**
     * Méthode de définition d'un uniforme de type vecteur 3D.
     * @param name Nom de l'uniforme.
     * @param x Valeur de l'uniforme en x.
     * @param y Valeur de l'uniforme en y.
     * @param z Valeur de l'uniforme en z.
     */
    public void setVec3(String name, float x, float y, float z) {
        GL20.glUniform3f(GL20.glGetUniformLocation(ID, name), x, y, z);
    }


    public void setMat4f(String name, float[] value) {
        GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(ID, name), false, value);
    }
    public void setMat4f(String name, org.joml.Matrix4f value) {
        setMat4f(name, value.get(new float[16]));
    }

}
