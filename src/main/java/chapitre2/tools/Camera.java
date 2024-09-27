package chapitre2.tools;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;

    private float yaw;
    private float pitch;

    private float movementSpeed;
    private float mouseSensitivity;
    private float zoom;

    private float lastX = 400, lastY = 300; // Initialiser avec les valeurs centrales de la fenêtre
    private boolean firstMouse = true;

    private Matrix4f viewMatrix;

    public Camera(Vector3f position) {
        this.position = position;
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.yaw = -90.0f;
        this.pitch = 0.0f;
        this.movementSpeed = 2.5f;
        this.mouseSensitivity = 0.1f;
        this.zoom = 45.0f;
        this.viewMatrix = new Matrix4f();
        updateCameraVectors();
        updateViewMatrix();
    }

    public void updateViewMatrix() {
        viewMatrix.identity();
        viewMatrix.lookAt(position, position.add(front, new Vector3f()), up);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        updateViewMatrix();
    }

    public void setFront(Vector3f front) {
        this.front = front;
        updateViewMatrix();
    }

    public void setUp(Vector3f up) {
        this.up = up;
        updateViewMatrix();
    }

    public float getFov() {
        return zoom;
    }

    public void processMouseScroll(float yOffset) {
        zoom -= yOffset;
        if (zoom < 1.0f) zoom = 1.0f;
        if (zoom > 45.0f) zoom = 45.0f;
    }

    public void processMouseMovement(float xPos, float yPos) {
        if (firstMouse) {
            lastX = xPos;
            lastY = yPos;
            firstMouse = false;
        }

        float xOffset = (xPos - lastX) * mouseSensitivity;
        float yOffset = (lastY - yPos) * mouseSensitivity; // Inverser car les coordonnées y vont de bas en haut

        lastX = xPos;
        lastY = yPos;

        yaw += xOffset;
        pitch += yOffset;

        if (pitch > 89.0f) pitch = 89.0f;
        if (pitch < -89.0f) pitch = -89.0f;

        updateCameraVectors();
    }

    public void processKeyboard(long window, float deltaTime) {
        float velocity = movementSpeed * deltaTime;
        // avancer
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            position.add(new Vector3f(front).mul(velocity));
        }
        // reculer
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            position.sub(new Vector3f(front).mul(velocity));
        }
        // aller à gauche
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            position.sub(new Vector3f(right).mul(velocity));
        }
        // aller à droite
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            position.add(new Vector3f(right).mul(velocity));
        }
        // monter
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            position.add(new Vector3f(up).mul(velocity));
        }
        // descendre
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            position.sub(new Vector3f(up).mul(velocity));
        }

        updateViewMatrix();
    }

    private void updateCameraVectors() {
        Vector3f front = new Vector3f();
        front.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        this.front = front.normalize();

        right = new Vector3f(front).cross(worldUp).normalize();
        up = new Vector3f(right).cross(front).normalize();
    }
}