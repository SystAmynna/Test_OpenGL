package chapitre1.tools;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Classe chapitre1.tools.Camera.
 */
public class Camera {

    /**
     * Définit les possibles options de mouvement caméra.
     */
    private enum cameraMovement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    // CONSTANTES
    // VALEURS INITIALES

    private static final float YAW = -90.0f;
    private static final float PITCH = 0.0f;
    private static final float SPEED = 2.5f;
    private static final float SENSITIVITY = 0.3f;
    private static final float ZOOM = 45.0f;

    // ATTRIBUTS

    // Attributs de la camera
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;
    // Angles d'Euler
    float yaw;
    float pitch;
    // chapitre1.tools.Camera Options
    float movementSpeed;
    float mouseSensitivity;
    float zoom;

    /**
     * Construceur de la caméra.
     * Avec des vecteurs.
     */
    public Camera(Vector3f position, Vector3f up, float yaw, float pitch) {
        this.position = position;
        this.worldUp = up;
        this.yaw = yaw;
        this.pitch = pitch;
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.movementSpeed = SPEED;
        this.mouseSensitivity = SENSITIVITY;
        this.zoom = ZOOM;
        updateCameraVectors();
    }
    /**
     * Constructeur de la caméra.
     * Avec des coordonnées.
     */
    public Camera(float posX, float posY, float posZ, float upX, float upY, float upZ, float yaw, float pitch) {
        this(new Vector3f(posX, posY, posZ), new Vector3f(upX, upY, upZ), yaw, pitch);
    }

    /**
     * Retourne la matrice de vue.
     */
    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, position.add(front), up);
    }

    /**
     * Traitement des touches.
     */
    public void processKeyboard(cameraMovement direction, float deltaTime) {
        float velocity = movementSpeed * deltaTime;
        if (direction == cameraMovement.FORWARD) {
            position.add(front.mul(velocity));
        }
        if (direction == cameraMovement.BACKWARD) {
            position.sub(front.mul(velocity));
        }
        if (direction == cameraMovement.LEFT) {
            position.sub(right.mul(velocity));
        }
        if (direction == cameraMovement.RIGHT) {
            position.add(right.mul(velocity));
        }
    }

    /**
     * Traitement des mouvements de la souris.
     */
    public void processMouseMovement(float xoffset, float yoffset, boolean constrainPitch) {
        xoffset *= mouseSensitivity;
        yoffset *= mouseSensitivity;

        yaw += xoffset;
        pitch += yoffset;

        if (constrainPitch) {
            if (pitch > 89.0f) {
                pitch = 89.0f;
            }
            if (pitch < -89.0f) {
                pitch = -89.0f;
            }
        }

        updateCameraVectors();
    }

    /**
     * Traitement de la mollette de la souris.
     */
    public void processMouseScroll(float yoffset) {
        if (zoom >= 1.0f && zoom <= 45.0f) {
            zoom -= yoffset;
        }
        if (zoom <= 1.0f) {
            zoom = 1.0f;
        }
        if (zoom >= 45.0f) {
            zoom = 45.0f;
        }
    }

    /**
     * Mise à jour des vecteurs de la caméra.
     */
    private void updateCameraVectors() {
        Vector3f front = new Vector3f();
        front.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        this.front = front.normalize();
        this.right = this.front.cross(worldUp, new Vector3f()).normalize();
        this.up = this.right.cross(this.front, new Vector3f()).normalize();
    }


}
